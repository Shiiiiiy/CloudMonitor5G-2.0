/**
 * 
 */
package com.datang.tools;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datang.util.SqlCreateUtils;

/**
 * 调取gis服务主动渲染小区线程
 * 
 * @author yinzhipeng
 * @date:2015年11月24日 下午4:46:08
 * @version
 */
public class DrawGisLteCell implements Runnable {
	private String folderName;// 文件夹名称
	private String fileName;// 文件名
	private Long cellInfoId;// 小区信息ID
	private String gisDrawUrl;// GIS小区渲染接口
	private String gisQueryDb;// GIS小区渲染查询数据库
	private Integer sqlType;//sql注入类型
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DrawGisLteCell.class);
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// 调用GIS接口绘制小区图层
		// 'sqlcom': sqlcom,//数据库查询语句
		// 'connectionstring': connectionstring,//数据库连接
		// 'path':WorkspaceIDpath,//文件保存的路径
		// 'filename':"GridCeshi",//文件名称
		// 'sqltype':sqltype,//数据库类型（sql或oracle）
		// 'Radsize':’d=50’,//小区半径
		// 'f': "json"
		// 'nes':""
		try {
			// 线程休眠,规避事物
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			Thread.currentThread().interrupt();
			e1.printStackTrace();
		}
		long beginTimeMillis = System.currentTimeMillis();
		// gisDrawUrl=http\://172.30.4.114\:6080/arcgis/rest/services/zhejiang/zhejiang/MapServer/exts/myRestSOE/sampleOperation
		PostMethod postMethod = new PostMethod(gisDrawUrl);
		if(sqlType == 1){ // 导入4g工参
			postMethod.addParameter("sqlcom",
					SqlCreateUtils.creatCellSql(cellInfoId));
		}else if(sqlType == 2){//单站验证 5g规划工参
			String sql2 = "select THIS1.CELL_NAME AS CELLNAME,THIS1.CELL_ID AS CELLID,THIS1.CITY AS CITY,THIS1.GNB_ID AS GNBID, " +
							"THIS1.LAT AS LATITUDE,THIS1.LOCAL_CELL_ID AS LOCALCELLID,THIS1.LON AS LONGITUDE, " +
							"THIS1.SITE_NAME AS SITENAME,60 BEAMWIDTH, " +
							"THIS1.REPORT_CREATE_DATE AS REPORTCREATEDATE,THIS1.AZIMUTH, THIS1.TAC,THIS1.PCI, " +
							"THIS1.FREQUENCY1 AS FREQUENCY1,THIS1.TILT_M AS TILTM,THIS1.NR_FREQUENCY AS NRFREQUENCY, " +
							"THIS1.HEIGHT AS HEIGHT,THIS1.CELL_BROADBAND AS CELLBROADBAND,THIS1.SPECIAL_RATIO AS SPECIALRATIO, " +
							"THIS1.SSB_SENDING AS SSBSENDING,THIS1.SSB_WAVE_INTERVAL AS SSBWAVEINTERVAL,"
							+ "THIS1.TEST_EVENT_ALLSTATUS AS TESTEVENTALLSTATUS,MOD(pci, 4) AS MODEL3 " +
						"from IADS_PLAN_PARAM THIS1,IADS_CELL_INFO THIS2 " +
						"WHERE THIS1.CI_ID = THIS2.ID AND THIS2.ID = " + cellInfoId;
			postMethod.addParameter("sqlcom",sql2);
		}else if(sqlType == 3){//反开3d 4g规划工参
			String sql2 = "select " +
					" THIS1.AZIMUTH,THIS1.BEAM_WIDTH AS BEAMWIDTH,THIS1.BROAD_BAND AS BROADBAND, " +
					" THIS1.CELL_ID AS CELLID,THIS1.CELL_NAME AS CELLNAME,THIS1.EARFCN,THIS1.ECI,THIS1.ENB_ID AS ENBID, " +
					" THIS1.FREQUENCY_DL AS FREQUENCY1,THIS1.HIGH AS HEIGHT,THIS1.LOCAL_CELL_ID AS LOCALCELLID,THIS1.LAT AS LATITUDE,THIS1.LON AS LONGITUDE,THIS1.MCC, " +
					" THIS1.P_A AS PA,THIS1.P_B AS PB,THIS1.REGION AS CITY,THIS1.PCI,THIS1.REVERSE_OPEN_3D AS REVERSEOPEN3D,THIS1.RS_RPRE AS RSRPRE, " +
					" THIS1.SITE_NAME AS SITE_NAME,THIS1.SPECIAL_SUBFRAME_CONFIG AS SPECIALSUBFRAMECONFIG, " +
					" THIS1.SUBFRAME_CONFIG AS SUBFRAMECONFIG,THIS1.TAC,THIS1.TOTAL_TILT AS TOTALTILT,THIS1.TYPE,THIS1.TILT_M AS TILT_M,MOD(pci, 3) AS MODEL3 " +
			"from IADS_PROJECT_PLAN_4GPARAM THIS1,IADS_CELL_INFO THIS2 " +
			"WHERE THIS1.CI_ID = THIS2.ID AND THIS2.ID = "+ cellInfoId;
			postMethod.addParameter("sqlcom",sql2);
		}else if(sqlType == 4){//导入5g工参
			String sql2 = "select THIS1.MCC AS MCC,THIS1.MNC AS MNC,THIS1.AMF_GROUP_ID AS AMFGROUPID,THIS1.AMF_ID AS AMFID,THIS1.GNB_ID AS GNODEBID, " +
					"THIS1.SITE_NAME AS SITENAME,THIS1.CELL_NAME AS CELLNAME,THIS1.LOCAL_CELL_ID AS LOCALCELLI,THIS1.CELL_ID AS CELLID, " +
					"THIS1.TAC AS TAC,THIS1.PCI AS PCI,THIS1.FREQUENCY1 AS FREQ1,THIS1.FREQUENCY2 AS FREQ2,THIS1.FREQUENCY3 AS FREQUENCY3, " +
					"THIS1.FREQUENCY4 AS FREQUENCY4,THIS1.FREQUENCY5 AS FREQUENCY5, THIS1.FREQUENCY6 AS FREQUENCY6,THIS1.FREQUENCY7 AS FREQUENCY7, " +
					"THIS1.FREQUENCY8 AS FREQUENCY8,THIS1.FREQUENCY9 AS FREQUENCY9,THIS1.FREQUENCY10 AS FREQUENCY10, THIS1.FREQUENCY11 AS FREQUENCY11," +
					"THIS1.BAND_WIDTH1 AS BANDWIDTH1,THIS1.BAND_WIDTH2 AS BANDWIDTH2,THIS1.FREQ_COUNT AS FREQCOUNT,THIS1.LONGITUDE AS LONGITUDE, " +
					"THIS1.LATITUDE AS LATITUDE,THIS1.SECTOR_TYPE AS SECTORTYPE,THIS1.DOOR_TYPE AS DOORTYPE,THIS1.TOTAL_TILT AS TILTTOTAL, "+
					"THIS1.MECH_TILT AS MECH_TILT,THIS1.AZIMUTH AS AZIMUTH,THIS1.BEAM_COUNT AS BEAM_COUNT,THIS1.HEIGHT AS HEIGHT,MOD(pci, 4) AS MODEL3, " +
					"THIS1.REGION AS REGION,THIS1.VENDER AS VENDER,THIS1.COVER_SCENE AS COVER_SCENE,THIS1.ISBELONGTO_NETWORK AS ISBELONGTO_NETWORK,60 BEAMWIDTH " +
				"from IADS_5G_CELL THIS1,IADS_CELL_INFO THIS2 " +
				"WHERE THIS1.CI_ID = THIS2.ID AND THIS2.ID = " + cellInfoId;
			postMethod.addParameter("sqlcom",sql2);
		}
		
		postMethod.addParameter("connectionstring", gisQueryDb);
		postMethod.addParameter("path", folderName);
		postMethod.addParameter("filename", fileName);
		postMethod.addParameter("sqltype", "oracle");
		postMethod.addParameter("Radsize", "d=50");
		postMethod.addParameter("f", "json");
		postMethod.addParameter("nes", "");
		try {
			HttpClient httpClient = new HttpClient();
			httpClient.getHttpConnectionManager().getParams()
					.setConnectionTimeout(30000);// 链接时间
			httpClient.getHttpConnectionManager().getParams()
					.setSoTimeout(300000);// 读取时间
			httpClient.executeMethod(postMethod);
			String responseBodyAsString = postMethod.getResponseBodyAsString();
			LOGGER.info("[DrawGisLteCell] draw LteCell response "
					+ responseBodyAsString);
			System.out.println("[DrawGisLteCell] draw LteCell response "
					+ responseBodyAsString);
			JSONObject jsonObject = JSONObject.fromObject(responseBodyAsString);
			Object object = jsonObject.get("success");
			if (null != object) {
				long endTimeMillis = System.currentTimeMillis();
				// 绘图成功
				LOGGER.info("[DrawGisLteCell] draw LteCell success,TIME["
						+ ((double) (endTimeMillis - beginTimeMillis)) / 1000
						+ "s]");
				System.out.println("[DrawGisLteCell] draw LteCell success,TIME["
						+ ((double) (endTimeMillis - beginTimeMillis)) / 1000
						+ "s]");
			} else {
				LOGGER.info("[DrawGisLteCell] draw LteCell error,message:"
						+ responseBodyAsString);
				System.out.println("[DrawGisLteCell] draw LteCell error,message:"
						+ responseBodyAsString);
			}
		} catch (HttpException e) {
			LOGGER.error("[DrawGisLteCell]" + e.getMessage(), e);
			System.out.println("[DrawGisLteCell]" + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("[DrawGisLteCell]" + e.getMessage(), e);
			System.out.println("[DrawGisLteCell]" + e.getMessage());
		}
	}

	/**
	 * @param folderName
	 * @param fileName
	 * @param cellInfoId
	 * @param gisDrawUrl
	 * @param gisQueryDb
	 */
	public DrawGisLteCell(String folderName, String fileName, Long cellInfoId,
			String gisDrawUrl, String gisQueryDb,Integer sqlType) {
		super();
		this.folderName = folderName;
		this.fileName = fileName;
		this.cellInfoId = cellInfoId;
		this.gisDrawUrl = gisDrawUrl;
		this.gisQueryDb = gisQueryDb;
		this.sqlType = sqlType;
	}

	/**
	 * @return the folderNamefolderName
	 */
	public String getFolderName() {
		return folderName;
	}

	/**
	 * @param folderName
	 *            the folderName to set
	 */
	public void setFolderName(String folderName) {
		this.folderName = folderName;
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
	 * @return the cellInfoIdcellInfoId
	 */
	public Long getCellInfoId() {
		return cellInfoId;
	}

	/**
	 * @param cellInfoId
	 *            the cellInfoId to set
	 */
	public void setCellInfoId(Long cellInfoId) {
		this.cellInfoId = cellInfoId;
	}

	/**
	 * @return the gisDrawUrlgisDrawUrl
	 */
	public String getGisDrawUrl() {
		return gisDrawUrl;
	}

	/**
	 * @param gisDrawUrl
	 *            the gisDrawUrl to set
	 */
	public void setGisDrawUrl(String gisDrawUrl) {
		this.gisDrawUrl = gisDrawUrl;
	}

	/**
	 * @return the gisQueryDbgisQueryDb
	 */
	public String getGisQueryDb() {
		return gisQueryDb;
	}

	/**
	 * @param gisQueryDb
	 *            the gisQueryDb to set
	 */
	public void setGisQueryDb(String gisQueryDb) {
		this.gisQueryDb = gisQueryDb;
	}

}
