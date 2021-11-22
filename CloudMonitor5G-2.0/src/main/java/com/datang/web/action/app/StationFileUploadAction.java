package com.datang.web.action.app;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.util.ReflectUtil;
import com.datang.common.util.StringUtils;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.stationParam.StationProspectParamPojo;
import com.datang.service.platform.stationParam.StationProspectParamService;
import com.datang.util.ZipUtils;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;


/**
 * APP端单验文件上传Action
 * @author maxuancheng
 *
 */
@Controller
@Scope("prototype")
public class StationFileUploadAction implements ModelDriven<StationProspectParamPojo>{
	
	private File file;//上传文件
	
	private String fileFileName;//上传文件名
	
	private String fileContentType;//上传文件类型
	
	private StationProspectParamPojo stationProspectParamPojo = new StationProspectParamPojo();
	
	@Value("${stationZipUrl}")
	private String stationZipUrl;
	
	@Value("${fileSaveUrl}")
	private String fileSaveUrl;
	
	
	/**
	 * 基站勘察服务
	 */
	@Autowired
	private StationProspectParamService stationProspectParamService;
	
	/**
	 * 图片上传
	 * @author maxuancheng
	 * date:2020年6月23日 下午4:38:43
	 * @return
	 */
	public String imgUpload(){
		
		try {
			//保存图片
			if(file!=null && StringUtils.hasText(fileFileName)){
				ZipUtils.saveFile(file, stationZipUrl);
				String filePath = fileSaveUrl+ fileFileName.substring(fileFileName.indexOf("#")+1,fileFileName.indexOf("#", fileFileName.indexOf("#")+1))+"/";
				//解压Zip文件
				ZipUtils.unZip(stationZipUrl + file.getName(), filePath);
				ActionContext.getContext().getValueStack().push("上传成功");
			}else{
				ActionContext.getContext().getValueStack().push("上传图片为空");
			}
		} catch (IOException e) {
			ActionContext.getContext().getValueStack().push("上传失败");
			e.printStackTrace();
		}
		return ReturnType.JSON;
	}
	
	/**
	 * App端form表单提交
	 * @author maxuancheng
	 * date:2020年6月23日 下午4:38:20
	 * @return
	 */
	public String formUpload(){
		try {
			if(stationProspectParamPojo !=null && StringUtils.hasText(stationProspectParamPojo.getSiteName())){
				StationProspectParamPojo exitStationprospect = stationProspectParamService
						.findBySiteName(stationProspectParamPojo.getSiteName());
				if(exitStationprospect != null){
					if(exitStationprospect.getLongitude() != null){
						replaceClass(stationProspectParamPojo,exitStationprospect);
						stationProspectParamService.update(stationProspectParamPojo);
						ActionContext.getContext().getValueStack().push("上传成功");
					}
				}else{
					stationProspectParamService.save(stationProspectParamPojo);
					ActionContext.getContext().getValueStack().push("上传成功");
				}
			}else{
				ActionContext.getContext().getValueStack().set("errorMsg", "提交的基站勘察数据为空");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "保存基站勘察数据过程中发生错误:"+e);
		}
		return ReturnType.JSON;
	}

	/**
	 * App端form表单下载
	 * @author maxuancheng
	 * date:2020年6月23日 下午4:38:20
	 * @return
	 */
	public String formDownload(){
		try {
			if(stationProspectParamPojo !=null && StringUtils.hasText(stationProspectParamPojo.getSiteName())){
				StationProspectParamPojo exitStationprospect = stationProspectParamService
						.findBySiteName(stationProspectParamPojo.getSiteName());
				if(exitStationprospect != null){
					ActionContext.getContext().getValueStack().push(exitStationprospect);
				}else{
					ActionContext.getContext().getValueStack().set("errorMsg", "下载内容为空，无基站名对应数据");
				}
			}else{
				ActionContext.getContext().getValueStack().set("errorMsg", "基站名为空");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "下载基站勘察数据过程中发生错误:"+e);
		}
		return ReturnType.JSON;
	}
	
	/**
	 * 工具类替换值
	 * @param class1Obj
	 * @param class2Exit
	 * @throws Exception
	 */
	public static void replaceClass(Object class1Obj,Object class2Exit) throws Exception{
    	PropertyDescriptor[] fields = BeanUtils
				.getPropertyDescriptors(class1Obj.getClass());
		Object reportTypeValue = null;
		for (PropertyDescriptor field : fields) {
			String fieldName = field.getName();
			reportTypeValue = ReflectUtil.getField(class1Obj, fieldName);
			if(isEmpty(reportTypeValue)){
				reportTypeValue = ReflectUtil.getField(class2Exit, fieldName);
				ReflectUtil.setField(class1Obj,fieldName,reportTypeValue);
			}
		}
    }
	
	public static boolean isEmpty(Object obj) throws Exception{
	    if (obj == null)
	    {
	      return true;
	    }
	    if ((obj instanceof List))
	    {
	      return ((List) obj).size() == 0;
	    }
	    if ((obj instanceof String))
	    {
	      return ((String) obj).trim().equals("");
	    }
	    return false;
	}
	
	@Override
	public StationProspectParamPojo getModel() {
		
		return stationProspectParamPojo;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
}
