/**
 * 
 */
package com.datang.web.action.mos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.mos.AppTestInfo;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.service.mos.IAppService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * app文件打分接口
 * 
 * @author yinzhipeng
 * @date:2015年8月19日 上午11:17:48
 * @version
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class CityPerceptAnalysisAction extends PageAction {
	@Autowired
	private IAppService appService;

	//筛选的开始日期
	private Date beginDate;
	//筛选的结束日期
	private Date endDate;
	//省
	private String provinces;
	//市
	private String citys;
	//分析粒度类型 0:省   1:市   2:区
	private String statisticType;
	
	//以下为APP上传记录的接口字段
	private String appId;
	private String devType;
	private String fileName;
	private String sampleFileName;
	private String sn;
	private String uploadCode;
	private File FileStream;

	private String limit;
	private String type;

	private String pingDelay;// ”ping时延”,
	private String upSpeedAvg;// ”上传平均速度”,
	private String downSpeedAvg;// ”下载平均速度”,
	private String mosScore;// ”mos值”,
	private String networkType;// ”网络类型”,
	private String lac_ci;// ”LAC/CI”,
	private String tac_eci;// ”TAC/ECI”,
	private String network;// ”信号强度”,
	private String lteNetwork;// LTE”信号强度”,
	private String sinr;// ”SINR”,
	private String pci;// ”PCI”,
	private String mcc;// ”MCC”,
	private String mnc;// ”MNC”,
	private String imei;// ”IMEI”,
	private String imsi;// ”IMSI”,
	private String mkr;// ”手机厂家”,
	private String model;// ”手机型号”,
	private String latitude;// ”纬度”,
	private String longitude;// ”经度”
	private String timestamp;// ”时间戳”
	private String id;// ”唯一标识”
	private String vmos;
	private String sQuality;
	private String sLoading;
	private String sStalling;
	
	private String province;// "省"
	private String city;// "市"
	private String district;// "区"
	private String nrTAC;// "NR TAC"
	private String nrArfcn;// "NR ARFCN"
	private String nrPci;// "NR PCI"
	private String ssRsrp;// "NR RSRP"
	private String ssSinr;// "NR SINR"
	private String earfcn;// "EARFCN"
//	private Long phoneNUmber;// "手机号"
//	private Long friendRate;// "朋友圈打开速率"
	private Double httpDownloadAvg;// "HTTP下载速率"
	private Double httpBrowseDelay;// "HTTP浏览时延"

	/**
	 * 跳转到app测试信息LIST界面
	 * 
	 * @return
	 */
	public String appTestInfoListUI() {
		return ReturnType.LISTUI;
	}
	
	/**
	 * 初始化省复选框
	 * @author lucheng
	 * @date 2020年9月18日 下午4:48:53
	 * @return
	 */
	public String getInitProvince(){
		HashMap<String, Object> param = new HashMap<String, Object>();
		PageList pageList = new PageList();
		
		List<AppTestInfo> tl = appService.findAppTestInfo(pageList);
		
		String provinceStr="";
		Map<String,List<String>> regionMap = new HashMap<String, List<String>>();
		Set<String> provinceSet = new HashSet<String>(); 
		for (AppTestInfo appTestInfo : tl) {
			if(StringUtils.hasText(appTestInfo.getProvince())){
				if(provinceStr.indexOf(appTestInfo.getProvince())==-1){
					provinceStr = provinceStr + appTestInfo.getProvince()+","; 
				}
				provinceSet.add(appTestInfo.getProvince());
				if(regionMap.get(appTestInfo.getProvince())==null){
					List<String> citylist = new ArrayList<String>();
					citylist.add(appTestInfo.getCity());
					regionMap.put(appTestInfo.getProvince(),citylist);
				}else{
					List<String> citys = regionMap.get(appTestInfo.getProvince());
					if(!citys.contains(appTestInfo.getCity())){
						citys.add(appTestInfo.getCity());
					}
				}
			}
		}
		
		List<HashMap<String, Object>> tlList = new ArrayList<HashMap<String,Object>>();
		for (String province : provinceSet) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", province);
			map.put("name", province);
			tlList.add(map);
		}
		ActionContext.getContext().getValueStack().set("tlList", tlList);
		ActionContext.getContext().getValueStack().set("provinceStr", provinceStr);
		ActionContext.getContext().getValueStack().set("regionMap", regionMap);
		return ReturnType.JSON;
	}	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinamobile.common.action.page.PageAction#doPageQuery(com.chinamobile
	 * .common.action.page.PageList)
	 */
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		pageList.putParam("beginDate", beginDate);
		pageList.putParam("endDate", endDate);
		
		if(!StringUtils.hasText((String)statisticType)){
			statisticType = "1";
		}
		pageList.putParam("statisticType", statisticType);
		
		//分析粒度类型
		List<AppTestInfo> list = new ArrayList<AppTestInfo>();
		if(statisticType.equals("0")){
			//省
			if(StringUtils.hasText((String)provinces)){
				pageList.putParam("provinces", provinces);
				list = appService.getCityAnalysisInfo(pageList);
			}
		}else if(statisticType.equals("1") || statisticType.equals("2")){
			//市
			if(StringUtils.hasText((String)citys)){
				pageList.putParam("citys", citys);
				list = appService.getCityAnalysisInfo(pageList);
			}
		}
		
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		
		int firstResult = (pageNum - 1) * rowsCount;
		List<AppTestInfo> rltList = new ArrayList<AppTestInfo>();
		for (int i = firstResult; i < list.size(); i++) {
			if(i<pageNum*rowsCount){
				rltList.add(list.get(i));
			}
		}
		
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(rltList);
		easyuiPageList.setTotal(rltList.size() + "");
		return easyuiPageList;
	}
	
	/**
	 * 查询城市数据排名
	 * @author lucheng
	 * @date 2020年9月21日 下午6:42:48
	 * @return
	 */
	public String queryCityRankData() {
		PageList pageList = new PageList();
		if(provinces ==null && citys == null){
			return null;
		}
		pageList.putParam("beginDate", beginDate);
		pageList.putParam("endDate", endDate);
		pageList.putParam("provinces", provinces);
		pageList.putParam("citys", citys);
		pageList.putParam("statisticType", statisticType);
		
		
		String[] regionsArry = null;
		if(!StringUtils.hasText((String)statisticType)){
			statisticType = "1";
		}
		if(statisticType.equals("0")){
			regionsArry = provinces.replace(" ", "").split(",");
		}else if(statisticType.equals("1")){
			regionsArry = citys.replace(" ", "").split(",");
		}else if(statisticType.equals("2")){
			List<AppTestInfo> info = appService.findAppTestInfo(pageList);
			List<String> districtList = new ArrayList<String>();
			for (AppTestInfo appTestInfo : info) {
				if(appTestInfo.getDistrict()!=null){
					if(!districtList.contains(appTestInfo.getDistrict())){
						districtList.add(appTestInfo.getDistrict());
					}
				}
			}
			regionsArry = districtList.toArray(new String[districtList.size()]);
		}
        //比较器来实现排序,升序
		Comparator<Entry<String,Integer>> comparetorAsc = new ComparatorAsc();
        //比较器来实现排序,降序
		Comparator<Entry<String,Integer>> comparetorDesc = new ComparatorDesc();
		Map<String,Map<String,String>> rltMap = new HashMap<String,Map<String,String>>();
		
		//视频打分排名：从低到高升序排序，视频质量分（降序）、视频业务VMOS（降序）、视频业务开始缓冲分（降序）、视频卡顿分（降序），4个指标的排名值的和
		//下面参数必须与AppTestInfo对象的字段名称相同
		Map<String, Object> squalityMaps = appService.findRankData(pageList,"sQuality");
		Map<String, Object> vmosMaps = appService.findRankData(pageList,"vmos");
		Map<String, Object> sLoadingMaps = appService.findRankData(pageList,"sLoading");
		Map<String, Object> sStallingMaps = appService.findRankData(pageList,"sStalling");
		
		Map<String,Integer> videoRankMap = new HashMap<String, Integer>();
		for (String name : regionsArry) {
			//指标排名值的和
			Integer rankValue = 0;
			if(squalityMaps.get(name)!=null){
				rankValue = rankValue + Integer.valueOf(squalityMaps.get(name).toString());
			}
			if(vmosMaps.get(name)!=null){
				rankValue = rankValue + Integer.valueOf(vmosMaps.get(name).toString());
			}
			if(sLoadingMaps.get(name)!=null){
				rankValue = rankValue + Integer.valueOf(sLoadingMaps.get(name).toString());
			}
			if(sStallingMaps.get(name)!=null){
				rankValue = rankValue + Integer.valueOf(sStallingMaps.get(name).toString());
			}
			if(rankValue!=0){
				videoRankMap.put(name, rankValue);
			}
		}
		//这里将map.entrySet()转换成list
		ArrayList<Entry<String,Integer>> videoRanklist = new ArrayList<Entry<String,Integer>>(videoRankMap.entrySet());
		Map<String,String> videoMap = new HashMap<String, String>();
		//然后通过比较器来实现升序
        Collections.sort(videoRanklist,comparetorAsc);
        //保存前四名
		for(int i=1;i<videoRanklist.size()+1;i++){
			if(i<=4){
				videoMap.put("TOP"+String.valueOf(i),videoRanklist.get(i-1).getKey());
			}
		}
		//然后通过比较器来实现降序
        Collections.sort(videoRanklist,comparetorDesc);
        //保存后四名
  		for(int i=1;i<videoRanklist.size()+1;i++){
  			if(i<=4){
  				videoMap.put("TOP-"+String.valueOf(i),videoRanklist.get(i-1).getKey());
  			}
  		}
  		rltMap.put("videoRank",videoMap);
        
		//数据打分排名：从低到高升序排序，数据业务上传速率（降序）、数据业务下载速率（降序）、ping（升序）3个指标的排名值的和
		Map<String, Object> upSpeedAvgMaps = appService.findRankData(pageList,"upSpeedAvg");
		Map<String, Object> downSpeedAvgMaps = appService.findRankData(pageList,"downSpeedAvg");
		Map<String, Object> pingDelayMaps = appService.findRankData(pageList,"pingDelay");
		
		Map<String,Integer> dataRankMap = new HashMap<String, Integer>();
		for (String name : regionsArry) {
			//指标排名值的和
			Integer rankValue = 0;
			if(upSpeedAvgMaps.get(name)!=null){
				rankValue = rankValue + Integer.valueOf(upSpeedAvgMaps.get(name).toString());
			}
			if(downSpeedAvgMaps.get(name)!=null){
				rankValue = rankValue + Integer.valueOf(downSpeedAvgMaps.get(name).toString());
			}
			if(pingDelayMaps.get(name)!=null){
				rankValue = rankValue + Integer.valueOf(pingDelayMaps.get(name).toString());
			}
			if(rankValue!=0){
				dataRankMap.put(name, rankValue);
			}
		}
		//这里将map.entrySet()转换成list
		ArrayList<Entry<String,Integer>> dataRanklist = new ArrayList<Entry<String,Integer>>(dataRankMap.entrySet());
		Map<String,String> dataMap = new HashMap<String, String>();
		//然后通过比较器来实现升序
        Collections.sort(dataRanklist,comparetorAsc);
        //保存前四名
		for(int i=1;i<dataRanklist.size()+1;i++){
			if(i<=4){
				dataMap.put("TOP"+String.valueOf(i),dataRanklist.get(i-1).getKey());
			}
		}
		//然后通过比较器来实现降序
        Collections.sort(dataRanklist,comparetorDesc);
        //保存后四名
  		for(int i=1;i<dataRanklist.size()+1;i++){
  			if(i<=4){
  				dataMap.put("TOP-"+String.valueOf(i),dataRanklist.get(i-1).getKey());
  			}
  		}
  		rltMap.put("dataRank",dataMap);  		
		
		ActionContext.getContext().getValueStack().push(rltMap);
		return ReturnType.JSON;
	}

	public String heartbeat() {
		return null;
	}

	/**
	 * @return the appIdappId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId
	 *            the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the devTypedevType
	 */
	public String getDevType() {
		return devType;
	}

	/**
	 * @param devType
	 *            the devType to set
	 */
	public void setDevType(String devType) {
		this.devType = devType;
	}

	/**
	 * @return the fileNamefileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the sampleFileNamesampleFileName
	 */
	public String getSampleFileName() {
		return sampleFileName;
	}

	/**
	 * @param sampleFileName
	 *            the sampleFileName to set
	 */
	public void setSampleFileName(String sampleFileName) {
		this.sampleFileName = sampleFileName;
	}

	/**
	 * @return the snsn
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * @param sn
	 *            the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * @return the uploadCodeuploadCode
	 */
	public String getUploadCode() {
		return uploadCode;
	}

	/**
	 * @param uploadCode
	 *            the uploadCode to set
	 */
	public void setUploadCode(String uploadCode) {
		this.uploadCode = uploadCode;
	}

	/**
	 * @return the fileStreamFileStream
	 */
	public File getFileStream() {
		return FileStream;
	}

	/**
	 * @param fileStream
	 *            the fileStream to set
	 */
	public void setFileStream(File fileStream) {
		FileStream = fileStream;
	}

	/**
	 * @return the limitlimit
	 */
	public String getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}

	/**
	 * @return the typetype
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the pingDelaypingDelay
	 */
	public String getPingDelay() {
		return pingDelay;
	}

	/**
	 * @param pingDelay
	 *            the pingDelay to set
	 */
	public void setPingDelay(String pingDelay) {
		this.pingDelay = pingDelay;
	}

	/**
	 * @return the upSpeedAvgupSpeedAvg
	 */
	public String getUpSpeedAvg() {
		return upSpeedAvg;
	}

	/**
	 * @param upSpeedAvg
	 *            the upSpeedAvg to set
	 */
	public void setUpSpeedAvg(String upSpeedAvg) {
		this.upSpeedAvg = upSpeedAvg;
	}

	/**
	 * @return the downSpeedAvgdownSpeedAvg
	 */
	public String getDownSpeedAvg() {
		return downSpeedAvg;
	}

	/**
	 * @param downSpeedAvg
	 *            the downSpeedAvg to set
	 */
	public void setDownSpeedAvg(String downSpeedAvg) {
		this.downSpeedAvg = downSpeedAvg;
	}

	/**
	 * @return the mosScoremosScore
	 */
	public String getMosScore() {
		return mosScore;
	}

	/**
	 * @param mosScore
	 *            the mosScore to set
	 */
	public void setMosScore(String mosScore) {
		this.mosScore = mosScore;
	}

	/**
	 * @return the networkTypenetworkType
	 */
	public String getNetworkType() {
		return networkType;
	}

	/**
	 * @param networkType
	 *            the networkType to set
	 */
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	/**
	 * @return the lac_cilac_ci
	 */
	public String getLac_ci() {
		return lac_ci;
	}

	/**
	 * @param lac_ci
	 *            the lac_ci to set
	 */
	public void setLac_ci(String lac_ci) {
		this.lac_ci = lac_ci;
	}

	/**
	 * @return the tac_ecitac_eci
	 */
	public String getTac_eci() {
		return tac_eci;
	}

	/**
	 * @param tac_eci
	 *            the tac_eci to set
	 */
	public void setTac_eci(String tac_eci) {
		this.tac_eci = tac_eci;
	}

	/**
	 * @return the networknetwork
	 */
	public String getNetwork() {
		return network;
	}

	/**
	 * @param network
	 *            the network to set
	 */
	public void setNetwork(String network) {
		this.network = network;
	}

	/**
	 * @return the sinrsinr
	 */
	public String getSinr() {
		return sinr;
	}

	/**
	 * @param sinr
	 *            the sinr to set
	 */
	public void setSinr(String sinr) {
		this.sinr = sinr;
	}

	/**
	 * @return the pcipci
	 */
	public String getPci() {
		return pci;
	}

	/**
	 * @param pci
	 *            the pci to set
	 */
	public void setPci(String pci) {
		this.pci = pci;
	}

	/**
	 * @return the mccmcc
	 */
	public String getMcc() {
		return mcc;
	}

	/**
	 * @param mcc
	 *            the mcc to set
	 */
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	/**
	 * @return the mncmnc
	 */
	public String getMnc() {
		return mnc;
	}

	/**
	 * @param mnc
	 *            the mnc to set
	 */
	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	/**
	 * @return the imeiimei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * @param imei
	 *            the imei to set
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * @return the imsiimsi
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * @param imsi
	 *            the imsi to set
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	/**
	 * @return the mkrmkr
	 */
	public String getMkr() {
		return mkr;
	}

	/**
	 * @param mkr
	 *            the mkr to set
	 */
	public void setMkr(String mkr) {
		this.mkr = mkr;
	}

	/**
	 * @return the modelmodel
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the latitudelatitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitudelongitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the timestamptimestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the idid
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the lteNetworklteNetwork
	 */
	public String getLteNetwork() {
		return lteNetwork;
	}

	/**
	 * @param lteNetwork
	 *            the lteNetwork to set
	 */
	public void setLteNetwork(String lteNetwork) {
		this.lteNetwork = lteNetwork;
	}


	/**
	 * @return the vmosvmos
	 */
	public String getVmos() {
		return vmos;
	}

	/**
	 * @param vmos
	 *            the vmos to set
	 */
	public void setVmos(String vmos) {
		this.vmos = vmos;
	}

	/**
	 * @return the sQualitysQuality
	 */
	public String getsQuality() {
		return sQuality;
	}

	/**
	 * @param sQuality
	 *            the sQuality to set
	 */
	public void setsQuality(String sQuality) {
		this.sQuality = sQuality;
	}

	/**
	 * @return the sLoadingsLoading
	 */
	public String getsLoading() {
		return sLoading;
	}

	/**
	 * @param sLoading
	 *            the sLoading to set
	 */
	public void setsLoading(String sLoading) {
		this.sLoading = sLoading;
	}

	/**
	 * @return the sStallingsStalling
	 */
	public String getsStalling() {
		return sStalling;
	}

	/**
	 * @param sStalling
	 *            the sStalling to set
	 */
	public void setsStalling(String sStalling) {
		this.sStalling = sStalling;
	}



	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}



	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}



	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}



	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}



	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}



	/**
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}



	/**
	 * @return the nrTAC
	 */
	public String getNrTAC() {
		return nrTAC;
	}



	/**
	 * @param nrTAC the nrTAC to set
	 */
	public void setNrTAC(String nrTAC) {
		this.nrTAC = nrTAC;
	}



	/**
	 * @return the nrArfcn
	 */
	public String getNrArfcn() {
		return nrArfcn;
	}



	/**
	 * @param nrArfcn the nrArfcn to set
	 */
	public void setNrArfcn(String nrArfcn) {
		this.nrArfcn = nrArfcn;
	}

	/**
	 * @return the nrPci
	 */
	public String getNrPci() {
		return nrPci;
	}

	/**
	 * @param nrPci the nrPci to set
	 */
	public void setNrPci(String nrPci) {
		this.nrPci = nrPci;
	}

	/**
	 * @return the ssRsrp
	 */
	public String getSsRsrp() {
		return ssRsrp;
	}

	/**
	 * @param ssRsrp the ssRsrp to set
	 */
	public void setSsRsrp(String ssRsrp) {
		this.ssRsrp = ssRsrp;
	}

	/**
	 * @return the ssSinr
	 */
	public String getSsSinr() {
		return ssSinr;
	}

	/**
	 * @param ssSinr the ssSinr to set
	 */
	public void setSsSinr(String ssSinr) {
		this.ssSinr = ssSinr;
	}

	/**
	 * @return the earfcn
	 */
	public String getEarfcn() {
		return earfcn;
	}

	/**
	 * @param earfcn the earfcn to set
	 */
	public void setEarfcn(String earfcn) {
		this.earfcn = earfcn;
	}

	/**
	 * @return the httpDownloadAvg
	 */
	public Double getHttpDownloadAvg() {
		return httpDownloadAvg;
	}

	/**
	 * @param httpDownloadAvg the httpDownloadAvg to set
	 */
	public void setHttpDownloadAvg(Double httpDownloadAvg) {
		this.httpDownloadAvg = httpDownloadAvg;
	}

	/**
	 * @return the httpBrowseDelay
	 */
	public Double getHttpBrowseDelay() {
		return httpBrowseDelay;
	}

	/**
	 * @param httpBrowseDelay the httpBrowseDelay to set
	 */
	public void setHttpBrowseDelay(Double httpBrowseDelay) {
		this.httpBrowseDelay = httpBrowseDelay;
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the provinces
	 */
	public String getProvinces() {
		return provinces;
	}

	/**
	 * @param provinces the provinces to set
	 */
	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}

	/**
	 * @return the citys
	 */
	public String getCitys() {
		return citys;
	}

	/**
	 * @param citys the citys to set
	 */
	public void setCitys(String citys) {
		this.citys = citys;
	}

	/**
	 * @return the statisticType
	 */
	public String getStatisticType() {
		return statisticType;
	}

	/**
	 * @param statisticType the statisticType to set
	 */
	public void setStatisticType(String statisticType) {
		this.statisticType = statisticType;
	}
	
	

}

/**
 * 比较器降序
 * @author lucheng
 * @date 2020年9月22日 下午3:21:37
 */
class ComparatorDesc implements Comparator<Entry<String,Integer>>{
	@Override
	public int compare(Entry<String,Integer> arg0,Entry<String,Integer> arg1) {
		int result=(int)(arg0.getValue() - arg1.getValue()); //首先根据优先级排序
		return result==0?0:(result>0?-1:1);
	}	
}

/**
 * 比较器升序
 * @author lucheng
 * @date 2020年9月22日 下午3:21:53
 */
class ComparatorAsc implements Comparator<Entry<String,Integer>>{
	@Override
	public int compare(Entry<String,Integer> arg0, Entry<String,Integer> arg1) {
		int result=(int)(arg0.getValue() - arg1.getValue()); //首先根据优先级排序
		return result==0?0:(result>0?1:-1);
	}
}
