package com.datang.web.action.railwaySubwayLIne;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.railway.SubwayXmlTablePojo;
import com.datang.domain.railway.TrainXmlTablePojo;
import com.datang.exception.ApplicationException;
import com.datang.service.RailWayStation.SubwayLineService;
import com.datang.service.RailWayStation.TrainUpdateSchduleService;
import com.datang.util.ZipMultiFile;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@Scope("prototype")
@Slf4j
public class SubwayLineAction extends PageAction implements ModelDriven<SubwayXmlTablePojo>{

	private static final long serialVersionUID = 3144913319996457061L;
	
	private SubwayXmlTablePojo subwayXmlTablePojo = new SubwayXmlTablePojo();

	@Value("${railwaySubwayLineFileUrl}")
	private String railwaySubwayLineFileUrl;

	@Autowired
	private SubwayLineService subwayLineService;

	/**
	 * 上传文件
	 */
	private File importFile;

	private String importFileFileName;

	private Date startDate;// 开始时间

	private Date endDate;// 结束时间

	private String trainXmlIds;
	
	public String gotoSubwayLineListUI(){
		return ReturnType.LISTUI;
	}

	/**
	 * 进入导入界面
	 */
	public String goImport() {
		return "import";
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {

		if(startDate!=null){
			pageList.putParam("startTime", startDate.getTime());
		}
		if(endDate!=null){
			pageList.putParam("endTime", endDate.getTime());
		}
		pageList.putParam("city", subwayXmlTablePojo.getCity());
		pageList.putParam("lineNo", subwayXmlTablePojo.getLineNo());
		return subwayLineService.doPageQuery(pageList);
	}

	/**
	 * 导入高铁csv线路文件
	 *
	 * @return
	 */
	public String importSubwayTrail() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		try {
			Long xmlNum = subwayLineService.importSubwayTrail(importFile,importFileFileName);
			valueStack.set("xmlNum", xmlNum);
		} catch (ApplicationException appEx) {
			valueStack.set("errorMsg", appEx.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * 删除指定的高铁csv线路文件
	 *
	 * @return
	 */
	public String deleteXmlFile() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		try {
			if (StringUtils.hasText(trainXmlIds)) {
				List<SubwayXmlTablePojo> list = subwayLineService.getSubwayXmlFile(trainXmlIds);
				list.stream().forEach(pojo->{
					subwayLineService.deleteSubwayXml(pojo.getId());
					if(pojo.getXmlFilePath()!=null){
						String xmlFilePath = pojo.getXmlFilePath();
						File file = new File(xmlFilePath);
						if(file.exists()){
							file.delete();
						}
					}
				});
			}
		}catch (Exception e) {
			e.printStackTrace();
			valueStack.set("errorMsg", e.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * 下载模板
	 * @return
	 */
	public String downloadTrainLog() {
		return "downloadTemp";
	}

	/**
	 * 下载xnml文件
	 * 
	 * @return
	 */
	public InputStream getDownloadTemp(){
		try {
			InputStream is = null;
			if (StringUtils.hasText(trainXmlIds)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String newDate = sdf.format(new Date());
				File file1 = new File(railwaySubwayLineFileUrl+ "/subwayZipFile/");
				if (!file1.exists()) {
					file1.mkdirs();
				}

				deleteFile(file1);
				File zipFile = new File(railwaySubwayLineFileUrl + "/subwayZipFile/" + "地铁线路文件列表.zip");

				List<File> fileList = new ArrayList<File>();
				List<SubwayXmlTablePojo> list = subwayLineService.getSubwayXmlFile(trainXmlIds);
				for (SubwayXmlTablePojo pojo : list) {
					if (null != pojo && StringUtils.hasText(pojo.getXmlFilePath())) {
						String filePath = pojo.getXmlFilePath();
						File xml = new File(filePath);
						if (xml.exists() && xml.isFile()) {
							fileList.add(xml);
						}
					}
				}
				ActionContext.getContext().put("fileName",new String(zipFile.getName().getBytes(),"ISO8859-1"));
				ZipMultiFile.zipFiles(fileList, zipFile);
				FileInputStream zipIn = new FileInputStream(zipFile);
				zipFile.delete();
				return zipIn;

			}
			return is;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void deleteFile(File file){
		//判断文件不为null或文件目录存在
		if (file == null || !file.exists()){
			System.out.println("文件删除失败,请检查文件路径是否正确");
			return;
		}
		//取得这个目录下的所有子文件对象
		File[] files = file.listFiles();
		//遍历该目录下的文件对象
		for (File f: files){
			f.delete();
		}
	}

	@Override
	public SubwayXmlTablePojo getModel() {
		return subwayXmlTablePojo;
	}

	public SubwayXmlTablePojo getSubwayXmlTablePojo() {
		return subwayXmlTablePojo;
	}

	public void setSubwayXmlTablePojo(SubwayXmlTablePojo subwayXmlTablePojo) {
		this.subwayXmlTablePojo = subwayXmlTablePojo;
	}

	/**
	 * @return importFile
	 */
	public File getImportFile() {
		return importFile;
	}

	/**
	 * @param importFile to set
	 */
	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getTrainXmlIds() {
		return trainXmlIds;
	}

	public void setTrainXmlIds(String trainXmlIds) {
		this.trainXmlIds = trainXmlIds;
	}

	public String getImportFileFileName() {
		return importFileFileName;
	}

	public void setImportFileFileName(String importFileFileName) {
		this.importFileFileName = importFileFileName;
	}
}
