package com.datang.web.action.testManage.errorLogAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.errorLogManage.ErrorLogManagePojo;
import com.datang.domain.errorLogManage.FTPInfo;
import com.datang.domain.errorLogManage.GaoDePojo;
import com.datang.service.errorLogManage.impl.ErrorLogManageService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 错误日志管理
 * @author maxuancheng
 * @date 2019年5月30日
 */

@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class ErrorLogManageAction extends PageAction implements ModelDriven<ErrorLogManagePojo>{
	
	@Autowired
	private ErrorLogManageService errorLogManageService;
	
	private ErrorLogManagePojo errorLogManagePojo = new ErrorLogManagePojo();
	
	@Autowired
	private FTPInfo ftpInfo;
	
	@Autowired
	private GaoDePojo gaodePojo;
	
	private Date startTime;
	
	private Date endTime;
	
	 /**
	 * 跳转到错误日志管理list页面
	 * 
	 * @return
	 */
	public String errorLogManageListUI(){
		return ReturnType.LISTUI;
	}
	
	/**
	 * 上传埋点信息
	 * @author maxuancheng
	 * @return
	 */
	public String uploadLogMessage(){
		ValueStack vs = ActionContext.getContext().getValueStack();
		try {
			if(errorLogManagePojo == null){
				vs.set("status", "403");
				vs.set("msg", "数据不能为空");
				return ReturnType.JSON;
			}
			String city = getCityOfLonAndLat(errorLogManagePojo.getLon() + "",errorLogManagePojo.getLat() + "");
			errorLogManagePojo.setCity(city);
			Boolean flag = errorLogManageService.errorLogManageService(errorLogManagePojo);
		} catch (Exception e) {
			vs.set("status", "403");
			vs.set("msg", "系统繁忙,请稍后再试");
			e.printStackTrace();
			return ReturnType.JSON;
		}
		vs.set("status", "200");
		vs.set("msg", "上传成功");
		return ReturnType.JSON;
	}
	
	/**
	 * Ftp下载接口
	 * @author maxuancheng
	 * @return JSON
	 */
	public String downloadFTPFile(){
		try {
			String boxid = errorLogManagePojo.getBoxid();
			String uploadTime = errorLogManagePojo.getUploadTime() + "";
			if(!StringUtils.hasText(boxid) || !StringUtils.hasText(uploadTime)){
				ValueStack vs = ActionContext.getContext().getValueStack();
				vs.set("msg", "没有错误日志!");
				return ReturnType.JSON;
			}
			FTPClient client = getFtpClient();
			String printWorkingDirectory = client.printWorkingDirectory();
			FTPFile[] listFiles = client.listFiles();
			Boolean flag = true;
			if(listFiles.length > 0){
				for(FTPFile ftpFile : listFiles){
					String[] split = ftpFile.getName().split("&");
					if(split[0].equals(boxid)){
						flag = false; //找到了该设备错误日志
						break;
					}
				}
			}else{
				ValueStack vs = ActionContext.getContext().getValueStack();
				vs.set("msg", "没有错误日志!");
				return ReturnType.JSON;
			}
			if(flag){
				ValueStack vs = ActionContext.getContext().getValueStack();
				vs.set("msg", "没有错误日志!");
				return ReturnType.JSON;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "downloadFtp";
	}

	/**
	 * Ftp下载具体执行方法
	 * @author maxuancheng
	 * @return
	 * @throws IOException
	 */
	public InputStream getDownloadFtp() throws IOException {
		InputStream is = null;
		FTPClient client = null;
		String boxid = errorLogManagePojo.getBoxid();
		Long uploadTime = errorLogManagePojo.getUploadTime().getTime();
		try {
			client  = getFtpClient();
			String printWorkingDirectory = client.printWorkingDirectory();
			FTPFile[] listFiles = client.listFiles();
			int boxidIsExist = 0; // 一boxid为前缀的文件名是否存在,默认不存在
			Long timeInterval = -1L; // 时间间隔
			String fileName = "";
			for(FTPFile ftpFile : listFiles){
				String[] split = ftpFile.getName().split("&");
				if(split[0].equals(boxid) && boxidIsExist == 0){
					boxidIsExist = 1;//改为存在状态
					fileName = split[1];
					timeInterval = Math.abs(Long.parseLong(split[1]) - uploadTime);
				} else if(split[0].equals(boxid) && boxidIsExist == 1){
					if(Math.abs(Long.parseLong(split[1]) - uploadTime) < timeInterval){ // 时间差更小
						fileName = split[1];
						timeInterval = Math.abs(Long.parseLong(split[1]) - uploadTime);
					}
				} else if(boxidIsExist == 1){
					break;
				}
			}
			is = client.retrieveFileStream(boxid + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}
	@Override
	public ErrorLogManagePojo getModel() {
		return errorLogManagePojo;
	}
	
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		pageList.putParam("errorLogManagePojo", errorLogManagePojo);
		if(startTime != null){
			pageList.putParam("startTime", startTime);
		}
		if(endTime != null){
			pageList.putParam("endTime", endTime);
		}
		return errorLogManageService.getPageDataOfFactor(pageList);
	}

	public ErrorLogManagePojo getErrorLogManagePojo() {
		return errorLogManagePojo;
	}

	public void setErrorLogManagePojo(ErrorLogManagePojo errorLogManagePojo) {
		this.errorLogManagePojo = errorLogManagePojo;
	}
	
	/**
	 * 创建FTP连接对象
	 * @author maxuancheng
	 * @return
	 * @throws NumberFormatException
	 * @throws SocketException
	 * @throws IOException
	 */
	public FTPClient getFtpClient() throws NumberFormatException, SocketException, IOException{
		FTPClient client = new FTPClient();
		client.connect(ftpInfo.getIp(),Integer.parseInt(ftpInfo.getPort()));
		client.login(ftpInfo.getUsername(), ftpInfo.getPassword());
		//client.enterLocalPassiveMode();
		boolean changeWorkingDirectory = client.changeWorkingDirectory(ftpInfo.getUrl());
		return client;
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss 格式转时间戳
	 * @author maxuancheng
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public long DateToTimeLong(String time) throws ParseException{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.parse(time).getTime();
	}

	/**
	 * 时间戳转 yyyy-mm-dd hh:mm:ss
	 * @author maxuancheng
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public Date TimeLongToDate(String time) throws ParseException{
		SimpleDateFormat sf = new SimpleDateFormat();
		return sf.parse(time);
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * 根据经纬度获取城市
	 * @author maxuancheng
	 * @param lon
	 * @param lat
	 * @return
	 */
	public String getCityOfLonAndLat(String lon,String lat){
		HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(gaodePojo + lon + "," + lat);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
                JSONObject fromObject = JSONObject.fromObject(result);
                Object regeocode = fromObject.get("regeocode");
                JSONObject fromObject2 = JSONObject.fromObject(regeocode);
                Object addressComponent = fromObject2.get("addressComponent");
                JSONObject fromObject3 = JSONObject.fromObject(addressComponent);
                Object city = fromObject3.get("city");
                if(city.toString().contains("[")){//说明是省
                	Object province = fromObject3.get("province");
                	result = province.toString();
                }else{//否则是市
                	result = city.toString();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
	}
}
