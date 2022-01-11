package com.datang.web.action.railwaySubwayLIne;

import com.datang.bean.railway.Line;
import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.railway.TrainXmlTablePojo;
import com.datang.exception.ApplicationException;
import com.datang.service.RailWayStation.TrainLineService;
import com.datang.service.RailWayStation.TrainUpdateSchduleService;
import com.datang.util.ZipMultiFile;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@Scope("prototype")
@Slf4j
public class RailwayLineAction extends PageAction implements ModelDriven<TrainXmlTablePojo>{

	private static final long serialVersionUID = 3144913319996457061L;
	
	private TrainXmlTablePojo traintXmlTablePojo = new TrainXmlTablePojo();

	private static final ExecutorService trainTaskExecutor = Executors.newFixedThreadPool(3);

	@Value("${railwaySubwayLineFileUrl}")
	private String railwaySubwayLineFileUrl;

	@Autowired
	private TrainLineService trainLineService;

	@Autowired
	private TrainUpdateSchduleService trainUpdateSchduleService;

	private Line line = new Line();

	/**
	 * 上传文件
	 */
	private File importFile;

	private Date startDate;// 开始时间

	private Date endDate;// 结束时间

	private String trainXmlIds;
	
	public String gotoRailwayLineListUI(){
		return ReturnType.LISTUI;
	}

	/**
	 * 进入导入界面
	 */
	public String goImport() {
		return "import";
	}

	public String goAddRailwayXmlPage(){

		ValueStack valueStack = ActionContext.getContext().getValueStack();
		List<Map<String,String>> list = new ArrayList<>();
		//编辑
		if(traintXmlTablePojo.getId()!=null){
			TrainXmlTablePojo entity = trainLineService.find(traintXmlTablePojo.getId());
			valueStack.set("entity",entity);
			//解析原来的xml
			File file = new File(entity.getXmlFilePath());
			if(file.exists()){
				SAXReader reader = new SAXReader();
				try {
					Document doc = reader.read(file);
					Element root = doc.getRootElement();
					Element city = root.element("City");
					Element metroLine = city.element("MetroLine");

					for (Object element : metroLine.elements()) {
						Element e = (Element) element;
						Map<String,String> map = new HashMap<>();
						map.put("sid",e.attribute("SID").getText());
						map.put("name",e.attribute("Name").getText());
						map.put("arriveTime",e.attribute("ArriveTime").getText());
						map.put("startTime",e.attribute("StartTime").getText());
						map.put("longitude",e.attribute("Longitude").getText());
						map.put("latitude",e.attribute("Latitude").getText());
						list.add(map);
					}
					list.sort(Comparator.comparing(o ->Integer.valueOf(o.get("sid"))));
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			}
		}

		if(list.size()<1){
			Map<String,String> map = new HashMap<>();
			list.add(map);
		}

		valueStack.set("stopList",list);
		return "manualAddRailwayXml";
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {

		if(startDate!=null){
			pageList.putParam("startTime", startDate.getTime());
		}
		if(endDate!=null){
			pageList.putParam("endTime", endDate.getTime());
		}
		pageList.putParam("fromStation", traintXmlTablePojo.getStartStation());
		pageList.putParam("toStation", traintXmlTablePojo.getDestStation());
		pageList.putParam("trainCode", traintXmlTablePojo.getTrainCode());
		return trainLineService.doPageQuery(pageList);
	}

	/**
	 * 导入高铁csv线路文件
	 *
	 * @return
	 */
	public String importRailwayTrail() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		try {
			Long xmlNum = trainLineService.importRailwayTrail(importFile);
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
				List<TrainXmlTablePojo> list = trainLineService.getTrainXmlFile(trainXmlIds);
				list.stream().forEach(pojo->{
					trainLineService.deleteTrainXml(pojo.getId());
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

	public String manualAddTrainXml() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		try {
			trainLineService.manualAddTrainXml(line);
		} catch (ApplicationException appEx) {
			valueStack.set("errorMsg", appEx.getMessage());
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
				File file1 = new File(railwaySubwayLineFileUrl+ "/trainZipFile/");
				if (!file1.exists()) {
					file1.mkdirs();
				}

				deleteFile(file1);
				File zipFile = new File(railwaySubwayLineFileUrl + "/trainZipFile/" + "高铁线路文件列表.zip");

				List<File> fileList = new ArrayList<File>();
				List<TrainXmlTablePojo> list = trainLineService.getTrainXmlFile(trainXmlIds);
				for (TrainXmlTablePojo pojo : list) {
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

	public String anasisTrainFahrplan() {
		trainTaskExecutor.submit(new Thread(new Runnable() {
			@Override
			public void run() {
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				log.info("获取高铁信息quartz定时任务开始！");
				try {
					log.info(dateFormat.format(new Date())+" - 执行全路客运车站信息数据 - start");
					trainUpdateSchduleService.analysisStationInfo();
					log.info(dateFormat.format(new Date())+" - 执行车次列表数据更新 - start");
					//1、同步今日车次
					trainUpdateSchduleService.syncTrainListByStation();
					//2、更新完，同步车次时刻信息
					log.info(dateFormat.format(new Date())+" - 执行同步车次时刻信息更新 - start");
					trainUpdateSchduleService.autoSyncTrainFahrplan();
					log.info("执行所有车次列表数据更新结束 - end");
				} catch (Exception e) {
					e.printStackTrace();
					log.error("获取高铁信息quartz定时任务失败！");
				}
				log.info(dateFormat.format(new Date())+" - 获取高铁信息quartz定时任务结束！");
			}
		}));
		return ReturnType.JSON;
	}

	@Override
	public TrainXmlTablePojo getModel() {
		return traintXmlTablePojo;
	}

	public TrainXmlTablePojo getTraintXmlTablePojo() {
		return traintXmlTablePojo;
	}

	public void setTraintXmlTablePojo(TrainXmlTablePojo traintXmlTablePojo) {
		this.traintXmlTablePojo = traintXmlTablePojo;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
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
}
