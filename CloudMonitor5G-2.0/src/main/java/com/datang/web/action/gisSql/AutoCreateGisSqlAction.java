/**
 * 
 */
package com.datang.web.action.gisSql;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.util.StringUtils;
import com.datang.constant.CellSQLConstant;
import com.datang.constant.SqlConstant;
import com.datang.constant.VolteAnalysisThresholdTypeConstant;
import com.datang.dao.gis.TestLogItemGpsPointDetailDao;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDropping;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDroppingInt;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDroppingSRVCC;
import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisThreshold;
import com.datang.domain.platform.projectParam.Cell5G;
import com.datang.domain.platform.projectParam.Cell5GNbCell;
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.platform.projectParam.TdlNbCell;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.VoLTEDissertation.compareAnalysis.ITestLogItemGridService;
import com.datang.service.VoLTEDissertation.handoffDropping.VolteDroppingService;
import com.datang.service.gis.ICEDEGpsPointService;
import com.datang.service.gis.ICWBRGpsPointService;
import com.datang.service.gis.ICWBRNbCellRoadCellToCelllService;
import com.datang.service.gis.IDroppingGpsPointService;
import com.datang.service.gis.IEEGpsPointService;
import com.datang.service.gis.ILostPacketGpsPointService;
import com.datang.service.gis.INbCellRoadCellToCelllService;
import com.datang.service.gis.IQBRGpsPointService;
import com.datang.service.gis.ISVQBGpsPointService;
import com.datang.service.gis.ISVQBNPlotCellToCelllService;
import com.datang.service.gis.ITestLogItemGpsPointService;
import com.datang.service.gis.IVQBGpsPointService;
import com.datang.service.gis.IVQBNPlotCellToCelllService;
import com.datang.service.platform.analysisThreshold.IVolteAnalysisThresholdService;
import com.datang.service.platform.projectParam.IProjectParamService;
import com.datang.service.service5g.gis5g.IEmbbCoverGpsPointService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.util.GPSUtils;
import com.datang.web.action.ReturnType;
import com.datang.web.action.platform.projectParam.ProjectParamInfoType;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

import javassist.expr.NewArray;

/**
 * GIS??????????????????
 * 
 * @author yinzhipeng
 * @date:2015???11???11??? ??????3:43:55
 * @modify:yinzhipeng 2017???7???24???
 * @version 1.5.2
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class AutoCreateGisSqlAction extends ActionSupport {

	@Value("${gisDb.url}")
	private String gisDb;
	@Value("${gis.model3.color0}")
	private String cellModel3Color0;
	@Value("${gis.model3.color1}")
	private String cellModel3Color1;
	@Value("${gis.model3.color2}")
	private String cellModel3Color2;
	@Value("${gis.testLogItemGpsPoint.color}")
	private String testLogItemGpsPointColor;
	@Value("${gis.eeGpsPointColor.color}")
	private String eeGpsPointColor;
	@Value("${gis.hofGpsPointColor.color}")
	private String hofGpsPointColor;
	@Value("${gis.cedeGpsPointColor.color}")
	private String cedeGpsPointColor;
	@Value("${gis.compareTestLogItemGpsPoint.color}")
	private String compareTestLogItemGpsPointColor;
	@Value("${gis.compareGridBaddest.color}")
	private String compareGridBaddestColor;
	@Value("${gis.compareGridBad.color}")
	private String compareGridBadColor;
	@Value("${gis.compareGridOther.color}")
	private String compareGridOtherColor;
	@Autowired
	private QBRIndexGpsPointInfo qbrIndexGpsPointInfo;
	@Autowired
	private VideoQBIndexGpsPointInfo videoQbrIndexGpsPointInfo;
	@Autowired
	private CWBRGpsPointInfo cwbrGpsPointInfo;
	@Autowired
	private CEDEGpsPointInfo cedeGpsPointInfo;
	@Autowired
	private QBRGpsPointInfo qbrGpsPointInfo;
	@Autowired
	private VideoQBGpsPointInfo videoQBGpsPointInfo;
	@Autowired
	private StreamQBGpsPointInfo streamQBGpsPointInfo;
	@Autowired
	private TMGpsPointInfo tmGpsPointInfo;
	@Autowired
	private LostPacketGpsPointInfo lpGpsPointInfo;
	@Autowired
	private TestLogItemGpsPointDetailDao testLogItemGpsPointDetailDao;
	@Autowired
	private ILostPacketGpsPointService lpGpsPointService;
	@Autowired
	private IEmbbCoverGpsPointService embbCoverGpsPointService;
	@Autowired
	private IVolteAnalysisThresholdService analysisThresholdService;
	/**
	 * ????????????id???','???????????????
	 */
	private String testLogItemIds;
	/**
	 * ??????????????????id???','???????????????
	 */
	private String compareTestLogItemIds;
	/**
	 * ????????????id
	 */
	private Long badRoadId;
	private Long videoQualityBadId;
	/**
	 * ??????MOS??????????????????????????????ID?????????????????????id
	 */
	private String compareBadRoadIds;
	/**
	 * MOS?????????????????????
	 */
	private String mosBadLatitude;
	private String mosBadLongitude;

	/**
	 * ????????????????????????
	 */
	private Integer indexType;

	/**
	 * EMBB????????????
	 */
	private Integer coverType;

	/**
	 * ??????????????????(0???????????????,1????????????,2IMS????????????,3CSFB??????,4???????????????,5????????????)
	 */
	private Integer eeType;
	/**
	 * ??????????????????(0SRVCC????????????,1?????????????????????)
	 */
	private Integer hofType;
	/**
	 * ??????????????????????????????????????????(??????????????????????????????????????????)
	 */
	private String iconType;
	/**
	 * ?????????(0??????,1??????)
	 */
	private Integer callType;
	/**
	 * ????????????id
	 */
	private Long eeId;
	/**
	 * ????????????id
	 */
	private Long hofId;
	/*
	 * ??????????????????Id
	 */
	private Long cedeId;

	/**
	 * ?????????
	 */
	private Long cellId;
	/**
	 * ????????????
	 */
	private Long failId;
	
	/**
	 * ??????????????????
	 */
	private String cellName;
	
	/**
	 * ???????????????????????????
	 */
	private String colorMapType;
	/**
	 * ??????????????????
	 */
	@Autowired
	private ITestLogItemService testlogItemService;
	/**
	 * ???????????????
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;
	/**
	 * ?????????????????????????????????
	 */
	@Autowired
	private ITestLogItemGpsPointService testLogItemGpsPointService;

	/**
	 * ?????????????????????????????????
	 */
	@Autowired
	private IQBRGpsPointService qbrGpsPointService;
	/**
	 * ????????????????????????????????????
	 */
	@Autowired
	private INbCellRoadCellToCelllService nbCellRoadCellToCelllService;
	/*
	 * CWBR????????????????????????????????????
	 */
	@Autowired
	private ICWBRNbCellRoadCellToCelllService cwbrNbCellRoadCellToCelllService;
	/**
	 * ?????????????????????????????????
	 */
	@Autowired
	private IEEGpsPointService eeGpsPointService;
	/*
	 * ?????????????????????????????????
	 */
	@Autowired
	private IDroppingGpsPointService droppingGpsPointService;
	/*
	 * ????????????Service
	 */
	@Autowired
	private VolteDroppingService volteDroppingService;
	/*
	 * ??????????????????????????????????????????
	 */
	@Autowired
	private ICWBRGpsPointService cwbrGpsPointService;
	/*
	 * ?????????????????????????????????????????????
	 */
	@Autowired
	private ICEDEGpsPointService cedeGpsPointService;
	/**
	 * ??????????????????????????????service
	 */
	@Autowired
	private ITestLogItemGridService testLogItemGridService;
	/**
	 * ?????????????????????????????????
	 */
	@Autowired
	private IVQBGpsPointService vqbGpsPointService;
	/**
	 * ????????????????????????????????????????????????
	 */
	@Autowired
	private ISVQBGpsPointService svqbGpsPointService;
	/**
	 * ????????????????????????????????????????????????
	 */
	@Autowired
	private IVQBNPlotCellToCelllService vqbnPlotCellToCelllService;
	/**
	 * ?????????????????????????????????????????????????????????
	 */
	@Autowired
	private ISVQBNPlotCellToCelllService svqbnPlotCellToCelllService;
	
	/**
	 * ????????????????????????????????????
	 */
	@Autowired 
	private IProjectParamService projectParamService;

	/**
	 * ??????????????????(??????????????????????????????,?????????3???color??????)
	 * 
	 * @return
	 */
	public String queryCellInfo() {
		List<Object> returnList = new ArrayList<>();
		List<String> filenameStrings = new ArrayList();
		List<String> filename4GStrings = new ArrayList();
		Map<String, String> mapFileName = new HashMap<String, String>();
		List<Map<String, Object>> NcellList5g = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> NcellList4g = new ArrayList<Map<String,Object>>();
		List<TestLogItem> queryTestLogItems = testlogItemService
				.queryTestLogItems(testLogItemIds);
		String location = null;
		if (0 != queryTestLogItems.size()) {
			List<TerminalGroup> allCityGroup = terminalGroupService
					.getAllCityGroup();
			outter: for (TestLogItem testLogItem : queryTestLogItems) {
				String terminalGroupName = testLogItem.getTerminalGroup();
				if (!StringUtils.hasText(terminalGroupName)) {
					continue outter;
				}
				inner: for (TerminalGroup terminalGroup : allCityGroup) {
					String name = terminalGroup.getName();
					if (!StringUtils.hasText(name)) {
						continue inner;
					}
					if (terminalGroupName.equals(name)) {
						CellInfo cellInfo = terminalGroup
								.getCellInfo(ProjectParamInfoType.CH_MO);
						if (null == cellInfo
								|| (!StringUtils.hasText(cellInfo.getNrCellGisFileName())
										&&!StringUtils.hasText(cellInfo.getLteCellGisFileName()))) {
							continue outter;
						}else{
							if(StringUtils.hasText(cellInfo.getNrCellGisFileName())){
								if(!filenameStrings.contains(cellInfo.getNrCellGisFileName()
										.trim())){	
									filenameStrings.add(cellInfo.getNrCellGisFileName()
											.trim());
									mapFileName.put("nr", cellInfo.getNrCellGisFileName()
											.trim());
								}
							}
							if(StringUtils.hasText(cellInfo.getLteCellGisFileName())){
								if(!filenameStrings.contains(cellInfo.getLteCellGisFileName()
										.trim())){
									filenameStrings.add(cellInfo.getLteCellGisFileName()
											.trim());
									mapFileName.put("lte", cellInfo.getLteCellGisFileName()
											.trim());
								}
							}
						}
//						filenameStrings.add("0D90ED53ABD24678B0A56988FD8B771E");
						if(cellInfo.getCells5g() != null){
//							filename5GStrings.add("0D90ED53ABD24678B0A56988FD8B771E");
							Set<Cell5G> cell5GSet =  cellInfo.getCells5g();
							inInner: for (Cell5G cell5g : cell5GSet) {
								if(cell5g.getLongitude()!=null){
									location = String.valueOf(cell5g.getLongitude());
									location = location+","+ String.valueOf(cell5g.getLatitude());
									break inInner;
								}
							}
							filename4GStrings.add(cellInfo.getLteCellGisFileName().trim());
							Set<Cell5GNbCell> cells5gNb = cellInfo.getCells5gNb();
							for (Cell5GNbCell cell5gNbCell : cells5gNb) {
								Map<String, Object> hashMap = new HashMap<String, Object>();
								hashMap.put("cellId", cell5gNbCell.getCellId() + "");
								hashMap.put("nbCellId", cell5gNbCell.getCellId() + "");
								hashMap.put("top", 1);
								NcellList5g.add(hashMap);
							}
						}
						if(cellInfo.getLteCells() != null){
							Set<TdlNbCell> tdlNbCells = cellInfo.getTdlNbCells();
							for (TdlNbCell tdlNbCell : tdlNbCells) {
								Map<String, Object> hashMap = new HashMap<String, Object>();
								hashMap.put("cellId", tdlNbCell.getCellId() + "");
								hashMap.put("nbCellId", tdlNbCell.getCellId() + "");
								hashMap.put("top", 1);
								NcellList4g.add(hashMap);
							}
						}
					}
				}
			}
		}
		returnList.add(0, filenameStrings);
		JSONObject jsonObject0 = new JSONObject();
		jsonObject0.put("modelValue", 0);
		jsonObject0.put("color", cellModel3Color0);
		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("modelValue", 1);
		jsonObject1.put("color", cellModel3Color1);
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("modelValue", 2);
		jsonObject2.put("color", cellModel3Color2);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors", new JSONObject[] { jsonObject0, jsonObject1,
				jsonObject2 });
		returnList.add(1, jsonObject);
		//4G?????????
		returnList.add(2, filename4GStrings);
		//??????
		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("NcellList5g", NcellList5g);
		jsonObject3.put("NcellList4g", NcellList4g);
		returnList.add(3, jsonObject3);
		//5g???????????????????????????
		returnList.add(4, location);
		//????????????????????????
		returnList.add(5, mapFileName);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ?????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryTestLogItemGpsPoint() {
		// [
		// [[{},{},...,{}],[{},{},...,{}],...,[{},{},...,{}]],//???????????????????????????
		// {"color":"#868687"}
		// ]
		List<TestLogItemGpsPoint> pointsByTestLogIds = testLogItemGpsPointService
				.getPointsByTestLogIds(testLogItemIds);
		// System.out.println(pointsByTestLogIds.size());
		List<Object> returnList = new ArrayList<>();
		List<List<TestLogItemGpsPoint>> pointLists = new ArrayList<>();
		if (0 != pointsByTestLogIds.size()) {
			Map<Long, LinkedList<TestLogItemGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemGpsPoint>>();
			for (TestLogItemGpsPoint testLogItemGpsPoint : pointsByTestLogIds) {
				if (null == map.get(testLogItemGpsPoint.getRecSeqNo())) {
					map.put(testLogItemGpsPoint.getRecSeqNo(),
							new LinkedList<TestLogItemGpsPoint>());
				}
				LinkedList<TestLogItemGpsPoint> linkedList = map
						.get(testLogItemGpsPoint.getRecSeqNo());
				linkedList.add(testLogItemGpsPoint);
			}
			pointLists.addAll(map.values());
		}
		returnList.add(0, pointLists);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("color", testLogItemGpsPointColor);
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ??????????????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryCWBRIndexGpsPoint() {

		List<TestLogItemIndexGpsPoint> pointsByQBRIdAndIndexType = cwbrGpsPointService
				.getPointsByCWBRIdAndIndexType(badRoadId, indexType);
		List<Object> returnList = new ArrayList<>();
		List<List<Object>> pointLists = new ArrayList<>();
		if (0 != pointsByQBRIdAndIndexType.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint badRoadGpsPoint : pointsByQBRIdAndIndexType) {
				Long cellId = badRoadGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(badRoadGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		returnList.add(0, pointLists);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors",
				qbrIndexGpsPointInfo.getIndexColorListMap(indexType));
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ???????????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryCWBRDirection() {
		// [
		// {
		// "latitude" : 31.269161,//???????????????
		// "longitude" : 121.4899//???????????????
		// },..,{}
		// ]
		List<TestLogItemGpsPoint> pointsByQBRIdAndIndexType = cwbrGpsPointService
				.getPointDirectionsByCWBRIdAndIndexType(badRoadId, indexType);
		LinkedList<TestLogItemGpsPoint> badRoadGpsPoints = new LinkedList<>();
		for (int i = 0; i < pointsByQBRIdAndIndexType.size(); i++) {
			TestLogItemGpsPoint testLogItemGpsPoint = pointsByQBRIdAndIndexType
					.get(i);
			if (0 == i) {
				// ????????????
				badRoadGpsPoints.addLast(testLogItemGpsPoint);
			}
			if ((i + 1) < pointsByQBRIdAndIndexType.size()) {
				TestLogItemGpsPoint nexttestLogItemGpsPoint = pointsByQBRIdAndIndexType
						.get(i + 1);
				Double latitude = testLogItemGpsPoint.getLatitude();
				Double longitude = testLogItemGpsPoint.getLongitude();
				Double latitude2 = nexttestLogItemGpsPoint.getLatitude();
				Double longitude2 = nexttestLogItemGpsPoint.getLongitude();
				try {
					double distance = GPSUtils.distance(latitude, longitude,
							latitude2, longitude2);
					// ????????????????????????2???
					if (distance >= 2) {
						if ((i + 1) != pointsByQBRIdAndIndexType.size() - 1) {
							badRoadGpsPoints.addLast(nexttestLogItemGpsPoint);
						}
					}
				} catch (Exception e) {
					// to do nothing
				}
			} else {
				// ???????????????
				badRoadGpsPoints.addLast(testLogItemGpsPoint);
			}
		}

		ActionContext.getContext().getValueStack().push(badRoadGpsPoints);
		return ReturnType.JSON;
	}

	/**
	 * ???????????????????????????????????????LTE??????????????????????????????
	 * 
	 * @return
	 */
	public String queryCWBRCellToCell() {
		// [
		// {
		// "cellId" :,//LTE??????cellid
		// "nbCellId" : //??????cellid
		// },..,{}
		// ]
		ActionContext
				.getContext()
				.getValueStack()
				.push(cwbrNbCellRoadCellToCelllService
						.getCellToCellInfo(badRoadId));
		return ReturnType.JSON;
	}

	/**
	 * ????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryCWBRGpsPoint() {

		List<Object> returnList = new ArrayList<>();
		List<List<Object>> everyCWBRGpsPointsByTestlogIds = cwbrGpsPointService
				.getEveryCWBRGpsPointsByTestlogIds(testLogItemIds);
		if (0 != everyCWBRGpsPointsByTestlogIds.size()) {
			returnList.add(0, everyCWBRGpsPointsByTestlogIds);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors", cwbrGpsPointInfo.getColorListMap());
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryEEGpsPoint() {
		// [
		// [[{},{},...,{}],[{},{},...,{}],...,[{},{},...,{}]],//?????????????????????????????????
		// [{"index":,"iconType":,"latitude":,"longitude":},...,{}]//?????????????????????????????????
		// {"color":"#868687"}
		// ]
		List<List<TestLogItemGpsPoint>> eeGpsPointsByTestlogIdsAndEEType = eeGpsPointService
				.getEEGpsPointsByTestlogIdsAndEEType(testLogItemIds, eeType);
		List<TestLogItemIndexGpsPoint> eventPointsByTestlogIdsAndIconType = eeGpsPointService
				.getEventPointsByTestlogIdsAndIconType(testLogItemIds,
						iconType, eeType);
		for (int i = 0; i < eventPointsByTestlogIdsAndIconType.size(); i++) {
			eventPointsByTestlogIdsAndIconType.get(1).setIndex(i + 1);
		}
		List<Object> returnList = new ArrayList<>();
		returnList.add(0, eeGpsPointsByTestlogIdsAndEEType);
		returnList.add(1, eventPointsByTestlogIdsAndIconType);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("color", eeGpsPointColor);
		returnList.add(2, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryDroppingGpsPoint() {
		// [
		// [[{},{},...,{}],[{},{},...,{}],...,[{},{},...,{}]],//?????????????????????????????????
		// [{"index":,"iconType":,"latitude":,"longitude":},...,{}]//??????????????????????????????
		// {"color":"#868687"}
		// ]
		List<List<TestLogItemGpsPoint>> droppingGpsPointsByTestlogIdsAndHofType = droppingGpsPointService
				.getTheEveryDroppingGpsPointsByTestlogIdsAndHofType(
						testLogItemIds, hofType);

		List<TestLogItemIndexGpsPoint> eventPointsByTestlogIdsAndIconType = droppingGpsPointService
				.getEventPointsByTestlogIdsAndIconType(testLogItemIds,
						Integer.valueOf(iconType), hofType);
		// ?????????????????????????????????
		for (int i = 0; i < eventPointsByTestlogIdsAndIconType.size(); i++) {
			eventPointsByTestlogIdsAndIconType.get(i).setIndex(i + 1);
		}
		List<Object> returnList = new ArrayList<>();
		returnList.add(0, droppingGpsPointsByTestlogIdsAndHofType);
		returnList.add(1, eventPointsByTestlogIdsAndIconType);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("color", hofGpsPointColor);
		returnList.add(2, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ?????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryEEIndexGpsPoint() {
		// [
		// [
		// [
		// "cellId",//??????cellid??????
		// [
		// {
		// "indexValue" : 2.33,//?????????
		// "latitude" : 31.269161,//???????????????
		// "longitude" : 121.4899//???????????????
		// },..,{}
		// ]
		// ],...,
		// [
		// "cellId",
		// [
		// {
		// "indexValue" : 2.33,
		// "latitude" : 31.269161,
		// "longitude" : 121.4899
		// },???,{}
		// ]
		// ]
		// ],
		// {
		// "colors" : [
		// {
		// "beginValue" : 108,//?????????????????????
		// "color" : "#ffffff",//??????????????????????????????
		// "endValue" : 110//?????????????????????
		// },...,
		// {
		// "beginValue" : 108,
		// "color" : "#ffffff",
		// "endValue" : 110
		// }
		// ]
		// }
		// ]
		// TODO
		List<TestLogItemIndexGpsPoint> pointsByEEIdAndIndexType = eeGpsPointService
				.getPointsByEEIdAndIndexType(eeId, indexType, callType);
		List<Object> returnList = new ArrayList<>();
		List<List<Object>> pointLists = new ArrayList<>();
		if (0 != pointsByEEIdAndIndexType.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint eeGpsPoint : pointsByEEIdAndIndexType) {
				Long cellId = eeGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(eeGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		returnList.add(0, pointLists);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors",
				videoQbrIndexGpsPointInfo.getIndexColorListMap(indexType));
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ?????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryDroppingIndexGpsPoint() {
		// [
		// [
		// [
		// "cellId",//??????cellid??????
		// [
		// {
		// "indexValue" : 2.33,//?????????
		// "latitude" : 31.269161,//???????????????
		// "longitude" : 121.4899//???????????????
		// },..,{}
		// ]
		// ],...,
		// [
		// "cellId",
		// [
		// {
		// "indexValue" : 2.33,
		// "latitude" : 31.269161,
		// "longitude" : 121.4899
		// },???,{}
		// ]
		// ]
		// ],
		// {
		// "colors" : [
		// {
		// "beginValue" : 108,//?????????????????????
		// "color" : "#ffffff",//??????????????????????????????
		// "endValue" : 110//?????????????????????
		// },...,
		// {
		// "beginValue" : 108,
		// "color" : "#ffffff",
		// "endValue" : 110
		// }
		// ]
		// }
		// ]
		List<TestLogItemIndexGpsPoint> pointsByEventGpsPoints = droppingGpsPointService
				.getPointsByvolteDroppingIdAndIndexType(hofId, indexType);
		List<Object> returnList = new ArrayList<>();
		List<List<Object>> pointLists = new ArrayList<>();
		if (0 != pointsByEventGpsPoints.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint dGpsPoint : pointsByEventGpsPoints) {
				Long cellId = dGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(dGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		returnList.add(0, pointLists);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors",
				qbrIndexGpsPointInfo.getIndexColorListMap(indexType));
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ??????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryEEEventGpsPoint() {
		// [
		// ["cellId",[{"iconType":0,"latitude":31.269161,"longitude":121.4899},???,{}]]
		// ,...,
		// ["cellId",[{"iconType":1,"latitude":31.269161,"longitude":121.4899},???,{}]]
		// ]
		List<TestLogItemIndexGpsPoint> eventPoints = eeGpsPointService
				.getEventPointsByEEIdAndIndexType(eeId, callType);
		List<List<Object>> returnList = new ArrayList<>();
		if (0 != eventPoints.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint eventGpsPoint : eventPoints) {
				Long cellId = eventGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(eventGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				returnList.add(list);
			}
		}
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryDroppingEventGpsPoint() {
		// [
		// ["cellId",[{"iconType":0,"latitude":31.269161,"longitude":121.4899},???,{}]]
		// ,...,
		// ["cellId",[{"iconType":1,"latitude":31.269161,"longitude":121.4899},???,{}]]
		// ]
		List<TestLogItemIndexGpsPoint> eventPointsByHofIdAndIndexType = droppingGpsPointService
				.getEventPointsByHopIdAndIndexType(hofId);
		List<List<Object>> returnList = new ArrayList<>();
		if (0 != eventPointsByHofIdAndIndexType.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint eventGpsPoint : eventPointsByHofIdAndIndexType) {
				Long cellId = eventGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(eventGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				returnList.add(list);
			}
		}
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ??????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryEEDirection() {
		// [
		// {
		// "latitude" : 31.269161,//???????????????
		// "longitude" : 121.4899//???????????????
		// },..,{}
		// ]
		List<TestLogItemGpsPoint> pointDirectionsByEEIdAndIndexType = eeGpsPointService
				.getPointDirectionsByEEIdAndIndexType(eeId, indexType, callType);
		LinkedList<TestLogItemGpsPoint> eeGpsPoints = new LinkedList<>();
		for (int i = 0; i < pointDirectionsByEEIdAndIndexType.size(); i++) {
			TestLogItemGpsPoint testLogItemGpsPoint = pointDirectionsByEEIdAndIndexType
					.get(i);
			if (0 == i) {
				// ????????????
				eeGpsPoints.addLast(testLogItemGpsPoint);
			}
			if ((i + 1) < pointDirectionsByEEIdAndIndexType.size()) {
				TestLogItemGpsPoint nexttestLogItemGpsPoint = pointDirectionsByEEIdAndIndexType
						.get(i + 1);
				Double latitude = testLogItemGpsPoint.getLatitude();
				Double longitude = testLogItemGpsPoint.getLongitude();
				Double latitude2 = nexttestLogItemGpsPoint.getLatitude();
				Double longitude2 = nexttestLogItemGpsPoint.getLongitude();
				try {
					double distance = GPSUtils.distance(latitude, longitude,
							latitude2, longitude2);
					// ????????????????????????2???
					if (distance >= 2) {
						if ((i + 1) != pointDirectionsByEEIdAndIndexType.size() - 1) {
							eeGpsPoints.addLast(nexttestLogItemGpsPoint);
						}
					}
				} catch (Exception e) {
					// to do nothing
				}
			} else {
				// ???????????????
				eeGpsPoints.addLast(testLogItemGpsPoint);
			}
		}
		ActionContext.getContext().getValueStack().push(eeGpsPoints);
		return ReturnType.JSON;
	}

	/**
	 * ??????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryDroppingDirection() {
		// [
		// {
		// "latitude" : 31.269161,//???????????????
		// "longitude" : 121.4899//???????????????
		// },..,{}
		// ]
		List<TestLogItemGpsPoint> pointsByEventGpsPoints = droppingGpsPointService
				.getPointDirectionsByDroppingIdAndIndexType(hofId, indexType);
		LinkedList<TestLogItemGpsPoint> dGpsPoints = new LinkedList<>();
		for (int i = 0; i < pointsByEventGpsPoints.size(); i++) {
			TestLogItemGpsPoint dGpsPoint = pointsByEventGpsPoints.get(i);
			if (0 == i) {
				// ????????????
				dGpsPoints.addLast(dGpsPoint);
			}
			if ((i + 1) < pointsByEventGpsPoints.size()) {
				TestLogItemGpsPoint nexteeGpsPoint = pointsByEventGpsPoints
						.get(i + 1);
				Double latitude = nexteeGpsPoint.getLatitude();
				Double longitude = nexteeGpsPoint.getLongitude();
				Double latitude2 = nexteeGpsPoint.getLatitude();
				Double longitude2 = nexteeGpsPoint.getLongitude();
				try {
					double distance = GPSUtils.distance(latitude, longitude,
							latitude2, longitude2);
					// ????????????????????????2???
					if (distance >= 2) {
						if ((i + 1) != pointsByEventGpsPoints.size() - 1) {
							dGpsPoints.addLast(nexteeGpsPoint);
						}
					}
				} catch (Exception e) {
					// to do nothing
				}
			} else {
				// ???????????????
				dGpsPoints.addLast(dGpsPoint);
			}
		}
		ActionContext.getContext().getValueStack().push(dGpsPoints);
		return ReturnType.JSON;
	}

	/*
	 * ??????????????????????????????????????????????????????
	 */
	public String queryDroppingCellToCell() {
		VolteDropping volteDropping = volteDroppingService
				.getVolteDropping(hofId);
		Map<String, Long> map = new HashMap<String, Long>();
		if (volteDropping != null && volteDropping.getCellId() != null
				&& volteDropping.getCellId() != 0
				&& volteDropping.getFailId() != null
				&& volteDropping.getFailId() != 0) {
			map.put("cellId", volteDropping.getCellId());
			map.put("nbCellId", volteDropping.getFailId());
		}
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}

	/**
	 * ?????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryQBRIndexGpsPoint() {

		List<TestLogItemIndexGpsPoint> pointsByQBRIdAndIndexType = qbrGpsPointService
				.getPointsByQBRIdAndIndexType(badRoadId, indexType);
		List<Object> returnList = new ArrayList<>();
		List<List<Object>> pointLists = new ArrayList<>();
		if (0 != pointsByQBRIdAndIndexType.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint badRoadGpsPoint : pointsByQBRIdAndIndexType) {
				Long cellId = badRoadGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(badRoadGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		returnList.add(0, pointLists);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors",
				qbrIndexGpsPointInfo.getIndexColorListMap(indexType));
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ?????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryQBRIndexGpsPoint5g() {

		List<TestLogItemIndexGpsPoint> pointsByQBRIdAndIndexType = qbrGpsPointService
				.getPointsByQBRIdAndIndexType5g(badRoadId, indexType);
		List<Object> returnList = new ArrayList<>();
		List<List<Object>> pointLists = new ArrayList<>();
		if (0 != pointsByQBRIdAndIndexType.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint badRoadGpsPoint : pointsByQBRIdAndIndexType) {
				Long cellId = badRoadGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(badRoadGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		returnList.add(0, pointLists);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors",
				qbrIndexGpsPointInfo.getIndexColorListMap5g(indexType));
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ??????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryQBRDirection() {
		// [
		// {
		// "latitude" : 31.269161,//???????????????
		// "longitude" : 121.4899//???????????????
		// },..,{}
		// ]
		List<TestLogItemGpsPoint> pointsByQBRIdAndIndexType = qbrGpsPointService
				.getPointDirectionsByQBRIdAndIndexType(badRoadId, indexType);
		LinkedList<TestLogItemGpsPoint> badRoadGpsPoints = new LinkedList<>();
		for (int i = 0; i < pointsByQBRIdAndIndexType.size(); i++) {
			TestLogItemGpsPoint testLogItemGpsPoint = pointsByQBRIdAndIndexType
					.get(i);
			if (0 == i) {
				// ????????????
				badRoadGpsPoints.addLast(testLogItemGpsPoint);
			}
			if ((i + 1) < pointsByQBRIdAndIndexType.size()) {
				TestLogItemGpsPoint nexttestLogItemGpsPoint = pointsByQBRIdAndIndexType
						.get(i + 1);
				Double latitude = testLogItemGpsPoint.getLatitude();
				Double longitude = testLogItemGpsPoint.getLongitude();
				Double latitude2 = nexttestLogItemGpsPoint.getLatitude();
				Double longitude2 = nexttestLogItemGpsPoint.getLongitude();
				try {
					double distance = GPSUtils.distance(latitude, longitude,
							latitude2, longitude2);
					// ????????????????????????2???
					if (distance >= 2) {
						if ((i + 1) != pointsByQBRIdAndIndexType.size() - 1) {
							badRoadGpsPoints.addLast(nexttestLogItemGpsPoint);
						}
					}
				} catch (Exception e) {
					// to do nothing
				}
			} else {
				// ???????????????
				badRoadGpsPoints.addLast(testLogItemGpsPoint);
			}
		}
		ActionContext.getContext().getValueStack().push(badRoadGpsPoints);
		return ReturnType.JSON;
	}

	/**
	 * ??????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryQBRDirection5g() {
		// [
		// {
		// "latitude" : 31.269161,//???????????????
		// "longitude" : 121.4899//???????????????
		// },..,{}
		// ]
		List<TestLogItemGpsPoint> pointsByQBRIdAndIndexType = qbrGpsPointService
				.getPointDirectionsByQBRIdAndIndexType5g(badRoadId, indexType);
		LinkedList<TestLogItemGpsPoint> badRoadGpsPoints = new LinkedList<>();
		for (int i = 0; i < pointsByQBRIdAndIndexType.size(); i++) {
			TestLogItemGpsPoint testLogItemGpsPoint = pointsByQBRIdAndIndexType
					.get(i);
			if (0 == i) {
				// ????????????
				badRoadGpsPoints.addLast(testLogItemGpsPoint);
			}
			if ((i + 1) < pointsByQBRIdAndIndexType.size()) {
				TestLogItemGpsPoint nexttestLogItemGpsPoint = pointsByQBRIdAndIndexType
						.get(i + 1);
				Double latitude = testLogItemGpsPoint.getLatitude();
				Double longitude = testLogItemGpsPoint.getLongitude();
				Double latitude2 = nexttestLogItemGpsPoint.getLatitude();
				Double longitude2 = nexttestLogItemGpsPoint.getLongitude();
				try {
					double distance = GPSUtils.distance(latitude, longitude,
							latitude2, longitude2);
					// ????????????????????????2???
					if (distance >= 2) {
						if ((i + 1) != pointsByQBRIdAndIndexType.size() - 1) {
							badRoadGpsPoints.addLast(nexttestLogItemGpsPoint);
						}
					}
				} catch (Exception e) {
				}
			} else {
				// ???????????????
				badRoadGpsPoints.addLast(testLogItemGpsPoint);
			}
		}
		ActionContext.getContext().getValueStack().push(badRoadGpsPoints);
		return ReturnType.JSON;
	}

	/**
	 * ????????????????????????LTE??????????????????????????????
	 * 
	 * @return
	 */
	public String queryQBRCellToCell() {
		// [
		// {
		// "cellId" :,//LTE??????cellid
		// "nbCellId" : //??????cellid
		// },..,{}
		// ]
		ActionContext
				.getContext()
				.getValueStack()
				.push(nbCellRoadCellToCelllService.getCellToCellInfo(badRoadId));
		return ReturnType.JSON;
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryQBRGpsPoint() {
		// [
		// [["WeakCover",[{},{}],[{},{}]],["Disturb",[{},{}],[{},{}]]],
		// {"colors" : [{"color" : "#ffffff","qbrType" : "WeakCover"},{"color" :
		// "#ffffff","qbrType" : "Disturb"}]}
		// ]

		List<Object> returnList = new ArrayList<>();
		List<List<Object>> everyQBRGpsPointsByTestlogIds = qbrGpsPointService
				.getEveryQBRGpsPointsByTestlogIds(testLogItemIds);
		if (0 != everyQBRGpsPointsByTestlogIds.size()) {
			returnList.add(0, everyQBRGpsPointsByTestlogIds);
		}
		// JSONObject jsonObject = new JSONObject();
		// jsonObject.put("color", testLogItemGpsPointColor);
		// returnList.add(1, jsonObject);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors", qbrGpsPointInfo.getColorListMap());
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ???????????????????????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryCEDEIndexGpsPoint() {

		List<TestLogItemIndexGpsPoint> pointsByQBRIdAndIndexType = cedeGpsPointService
				.getPointsByCedeIdAndIndexType(cedeId, indexType);
		List<Object> returnList = new ArrayList<>();
		List<List<Object>> pointLists = new ArrayList<>();
		if (0 != pointsByQBRIdAndIndexType.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint badRoadGpsPoint : pointsByQBRIdAndIndexType) {
				Long cellId = badRoadGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(badRoadGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		returnList.add(0, pointLists);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors",
				qbrIndexGpsPointInfo.getIndexColorListMap(indexType));
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ????????????????????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryCEDEDirection() {
		// [
		// {
		// "latitude" : 31.269161,//???????????????
		// "longitude" : 121.4899//???????????????
		// },..,{}
		// ]
		List<TestLogItemGpsPoint> pointsByQBRIdAndIndexType = cedeGpsPointService
				.getPointDirectionsByCedeIdAndIndexType(cedeId, indexType);
		LinkedList<TestLogItemGpsPoint> badRoadGpsPoints = new LinkedList<>();
		for (int i = 0; i < pointsByQBRIdAndIndexType.size(); i++) {
			TestLogItemGpsPoint testLogItemGpsPoint = pointsByQBRIdAndIndexType
					.get(i);
			if (0 == i) {
				// ????????????
				badRoadGpsPoints.addLast(testLogItemGpsPoint);
			}
			if ((i + 1) < pointsByQBRIdAndIndexType.size()) {
				TestLogItemGpsPoint nexttestLogItemGpsPoint = pointsByQBRIdAndIndexType
						.get(i + 1);
				Double latitude = testLogItemGpsPoint.getLatitude();
				Double longitude = testLogItemGpsPoint.getLongitude();
				Double latitude2 = nexttestLogItemGpsPoint.getLatitude();
				Double longitude2 = nexttestLogItemGpsPoint.getLongitude();
				try {
					double distance = GPSUtils.distance(latitude, longitude,
							latitude2, longitude2);
					// ????????????????????????2???
					if (distance >= 2) {
						if ((i + 1) != pointsByQBRIdAndIndexType.size() - 1) {
							badRoadGpsPoints.addLast(nexttestLogItemGpsPoint);
						}
					}
				} catch (Exception e) {
					// to do nothing
				}
			} else {
				// ???????????????
				badRoadGpsPoints.addLast(testLogItemGpsPoint);
			}
		}
		ActionContext.getContext().getValueStack().push(badRoadGpsPoints);
		return ReturnType.JSON;
	}

	/**
	 * ????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryCEDEEventGpsPoint() {
		List<TestLogItemIndexGpsPoint> eventPointsByHofIdAndIndexType = cedeGpsPointService
				.getEventPointsByCedeIdAndIndexType(cedeId);
		List<List<Object>> returnList = new ArrayList<>();
		if (0 != eventPointsByHofIdAndIndexType.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint eventGpsPoint : eventPointsByHofIdAndIndexType) {
				Long cellId = eventGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(eventGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				returnList.add(list);
			}
		}
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ?????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryCEDEGpsPoint() {
		List<List<Object>> cedeGpsPointsByTestlogIdsAndCedeType = cedeGpsPointService
				.getTheEveryGpsPointsByTestlogIdsAndCedeType(testLogItemIds);

		List<TestLogItemIndexGpsPoint> eventPointsByTestlogIdsAndIconType = cedeGpsPointService
				.getEventPointsByTestlogIdsAndIconType(testLogItemIds,
						Integer.valueOf(iconType));
		// ?????????????????????????????????
		for (int i = 0; i < eventPointsByTestlogIdsAndIconType.size(); i++) {
			eventPointsByTestlogIdsAndIconType.get(i).setIndex(i + 1);
		}
		List<Object> returnList = new ArrayList<>();
		if (0 != cedeGpsPointsByTestlogIdsAndCedeType.size()) {
			returnList.add(0, cedeGpsPointsByTestlogIdsAndCedeType);
		}
		returnList.add(1, eventPointsByTestlogIdsAndIconType);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors", cedeGpsPointInfo.getColorListMap());
		returnList.add(2, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ???????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryCompareTestLogItemGpsPoint() {
		// [
		// [[{},{},...,{}],[{},{},...,{}],...,[{},{},...,{}]],//???????????????????????????
		// {"color":"#868687","compareTestLogColor":"#868687"}
		// ]
		List<TestLogItemGpsPoint> pointsByTestLogIds = testLogItemGpsPointService
				.getPointsByTestLogIds(testLogItemIds);

		List<Object> returnList = new ArrayList<>();
		List<List<TestLogItemGpsPoint>> pointLists = new ArrayList<>();
		if (0 != pointsByTestLogIds.size()) {
			Map<Long, LinkedList<TestLogItemGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemGpsPoint>>();
			for (TestLogItemGpsPoint testLogItemGpsPoint : pointsByTestLogIds) {
				if (null == map.get(testLogItemGpsPoint.getRecSeqNo())) {
					map.put(testLogItemGpsPoint.getRecSeqNo(),
							new LinkedList<TestLogItemGpsPoint>());
				}
				LinkedList<TestLogItemGpsPoint> linkedList = map
						.get(testLogItemGpsPoint.getRecSeqNo());
				linkedList.add(testLogItemGpsPoint);
			}
			pointLists.addAll(map.values());
		}
		returnList.add(0, pointLists);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("color", testLogItemGpsPointColor);
		jsonObject.put("compareTestLogColor", compareTestLogItemGpsPointColor);
		jsonObject.put("name", "????????????");
		jsonObject.put("compareName", "????????????");
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ??????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryTestLogItemGrid() {

		// [
		// [{"value":,"compareValue":,"color":,"minx":,"miny":,"maxx":,"maxy":},..,{}],
		// {"colors":[{"color":,"name":},...,{}]}
		// ]
		List<Long> ids = new ArrayList<>();
		if (StringUtils.hasText(testLogItemIds)) {
			String[] logIds = testLogItemIds.trim().split(",");
			for (int i = 0; i < logIds.length; i++) {
				if (StringUtils.hasText(logIds[i])) {
					try {
						ids.add(Long.parseLong(logIds[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
		}
		List<Long> compareIds = new ArrayList<>();
		if (StringUtils.hasText(compareTestLogItemIds)) {
			String[] compareLogIds = compareTestLogItemIds.trim().split(",");
			// ??????TestLogItem???id??????
			for (int i = 0; i < compareLogIds.length; i++) {
				if (StringUtils.hasText(compareLogIds[i])) {
					try {
						compareIds.add(Long.parseLong(compareLogIds[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
		}

		List<Object> returnList = new ArrayList<>();
		List<Map<String, Object>> doTestLogItemGridAnalysis = testLogItemGridService
				.doTestLogItemGridAnalysis(ids, compareIds, indexType);
		returnList.add(0, doTestLogItemGridAnalysis);

		Map<String, String> colorMap0 = new HashMap<>();
		colorMap0.put("color", compareGridBaddestColor);
		colorMap0.put("name", "??????");
		Map<String, String> colorMap1 = new HashMap<>();
		colorMap1.put("color", compareGridBadColor);
		colorMap1.put("name", "??????");
		Map<String, String> colorMap2 = new HashMap<>();
		colorMap2.put("color", compareGridOtherColor);
		colorMap2.put("name", "??????");
		List<Map<String, String>> colors = new ArrayList<>();
		colors.add(0, colorMap0);
		colors.add(1, colorMap1);
		colors.add(2, colorMap2);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors", colors);
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ??????????????????MOS???????????????
	 * 
	 * @return
	 */
	public String queryMosBad() {
		// [
		// [[{"latitude":,"longitude":},...,{}],...,[]],
		// [[{"latitude":,"longitude":},...,{}],...,[]],
		// {mosBadLatitude:,mosBadLongitude:},
		// {"color":,"name":,"compareColor":,"compareName":}
		// ]
		List<Object> returnList = new ArrayList<>();

		List<List<TestLogItemGpsPoint>> mosBadRoadGpsList = new ArrayList<>();
		returnList.add(0, mosBadRoadGpsList);
		List<TestLogItemGpsPoint> pointsByQBRId = qbrGpsPointService
				.getPointsByQBRId(badRoadId);
		if (0 != pointsByQBRId.size()) {
			mosBadRoadGpsList.add(pointsByQBRId);
		}

		List<List<TestLogItemGpsPoint>> compareMosBadRoadGpsList = new ArrayList<>();
		returnList.add(1, compareMosBadRoadGpsList);
		if (StringUtils.hasText(compareBadRoadIds)) {
			String[] compareMosBadIds = compareBadRoadIds.trim().split(",");
			// ??????TestLogItem???id??????
			for (int i = 0; i < compareMosBadIds.length; i++) {
				if (StringUtils.hasText(compareMosBadIds[i])) {
					// ???????????????mos???????????????????????????????????????id
					if (null != badRoadId
							&& compareMosBadIds[i].trim()
									.equals(badRoadId + "")) {
						continue;
					}
					try {
						List<TestLogItemGpsPoint> compareMosBadPoints = qbrGpsPointService
								.getPointsByQBRId(Long
										.parseLong(compareMosBadIds[i].trim()));
						if (0 != compareMosBadPoints.size()) {
							compareMosBadRoadGpsList.add(compareMosBadPoints);
						}
					} catch (NumberFormatException e) {
					}
				}
			}
		}

		Map<String, String> mosBadCenterMap = new HashMap<>();
		mosBadCenterMap.put("mosBadLatitude", mosBadLatitude);
		mosBadCenterMap.put("mosBadLongitude", mosBadLongitude);
		returnList.add(2, mosBadCenterMap);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("color", testLogItemGpsPointColor);
		jsonObject.put("compareColor", compareTestLogItemGpsPointColor);
		jsonObject.put("name", "????????????");
		jsonObject.put("compareName", "????????????");
		returnList.add(3, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ????????????????????????,Srvcc???????????????????????????????????????,????????????
	 * 
	 * @return
	 */
	public String queryCompareHof() {

		// [

		// [
		// ["cellId",[{"indexValue":,"latitude":,"longitude":},...,{}]],...,
		// ["cellId",[{"indexValue":,"latitude":,"longitude":},...,{}]]
		// ],

		// [
		// ["cellId",[{"index":,"iconType":,"latitude":,"longitude":},...,{}]],...,
		// ["cellId",[{"index":,"iconType":,"latitude":,"longitude":},...,{}]]
		// ]

		// {
		// "colors" :
		// [{"beginValue":,"color":,"endValue":},...,{"beginValue":,"color":,"endValue":}]
		// }

		// ]

		// ???????????????????????????id(???????????????????????????????????????)
		List<Long> ids = new ArrayList<>();
		if (StringUtils.hasText(testLogItemIds)) {
			String[] testLogItemIdString = testLogItemIds.trim().split(",");
			// ??????TestLogItem???id??????
			for (int i = 0; i < testLogItemIdString.length; i++) {
				if (StringUtils.hasText(testLogItemIdString[i])) {
					try {
						ids.add(Long.parseLong(testLogItemIdString[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
		}
		if (StringUtils.hasText(compareTestLogItemIds)) {
			String[] testLogItemIdString = compareTestLogItemIds.trim().split(
					",");
			// ??????TestLogItem???id??????
			for (int i = 0; i < testLogItemIdString.length; i++) {
				if (StringUtils.hasText(testLogItemIdString[i])) {
					try {
						ids.add(Long.parseLong(testLogItemIdString[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
		}

		// ?????????????????????id??????????????????id?????????cellid???failid?????????????????????id
		List<Long> hofIdList = new LinkedList<>();
		if (null != hofType) {
			if (0 == hofType) {
				List<VolteDroppingSRVCC> queryVolteDroppingSRVCCsByLogIdsAndFailId = volteDroppingService
						.queryVolteDroppingSRVCCsByLogIdsAndFailId(ids, failId);
				for (VolteDroppingSRVCC volteDroppingSRVCC : queryVolteDroppingSRVCCsByLogIdsAndFailId) {
					hofIdList.add(volteDroppingSRVCC.getId());
				}

			} else if (1 == hofType) {
				List<VolteDroppingInt> queryVolteDroppingIntsByLogIdsAndCellIdAndFailId = volteDroppingService
						.queryVolteDroppingIntsByLogIdsAndCellIdAndFailId(ids,
								cellId, failId);
				for (VolteDroppingInt volteDroppingInt : queryVolteDroppingIntsByLogIdsAndCellIdAndFailId) {
					hofIdList.add(volteDroppingInt.getId());
				}
			}
		}

		// ????????????
		List<Object> returnList = new ArrayList<>();
		List<List<Object>> pointLists = new ArrayList<>();
		List<List<Object>> iconLists = new ArrayList<>();
		returnList.add(0, pointLists);
		returnList.add(1, iconLists);

		// ?????????????????????id?????????????????????????????????
		Map<Long, LinkedList<TestLogItemIndexGpsPoint>> indexMap = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
		Map<Long, LinkedList<TestLogItemIndexGpsPoint>> iconMap = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
		for (int i = 0; i < hofIdList.size(); i++) {
			// ????????????
			List<TestLogItemIndexGpsPoint> pointsByEventGpsPoints = droppingGpsPointService
					.getPointsByvolteDroppingIdAndIndexType(hofIdList.get(i),
							indexType);
			// ??????????????????
			List<TestLogItemIndexGpsPoint> eventPointsByHofIdAndIndexType = droppingGpsPointService
					.getEventPointsByHopIdAndIndexType(hofIdList.get(i));

			// ??????????????????
			if (0 != pointsByEventGpsPoints.size()) {
				for (TestLogItemIndexGpsPoint dGpsPoint : pointsByEventGpsPoints) {
					Long cellId = dGpsPoint.getCellId();
					if (null == cellId) {
						continue;
					}
					if (null == indexMap.get(cellId)) {
						indexMap.put(cellId,
								new LinkedList<TestLogItemIndexGpsPoint>());
					}
					LinkedList<TestLogItemIndexGpsPoint> linkedList = indexMap
							.get(cellId);
					linkedList.add(dGpsPoint);
				}
			}
			// ????????????????????????
			if (0 != eventPointsByHofIdAndIndexType.size()) {
				for (TestLogItemIndexGpsPoint eventGpsPoint : eventPointsByHofIdAndIndexType) {
					Integer eventType = eventGpsPoint.getIndexType();
					// ????????????
					if (null == eventType) {
						continue;
					}
					// ?????????????????????????????????
					if (17 == eventType || 20 == eventType) {
						eventGpsPoint.setIndex(i + 1);
					}
					Long cellId = eventGpsPoint.getCellId();
					if (null == cellId) {
						continue;
					}
					if (null == iconMap.get(cellId)) {
						iconMap.put(cellId,
								new LinkedList<TestLogItemIndexGpsPoint>());
					}
					LinkedList<TestLogItemIndexGpsPoint> linkedList = iconMap
							.get(cellId);
					linkedList.add(eventGpsPoint);
				}
			}
		}

		for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : indexMap
				.entrySet()) {
			List<Object> list = new LinkedList<>();
			list.add(entry.getKey());
			list.add(entry.getValue());
			pointLists.add(list);
		}
		for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : iconMap
				.entrySet()) {
			List<Object> list = new LinkedList<>();
			list.add(entry.getKey());
			list.add(entry.getValue());
			iconLists.add(list);
		}

		// ??????????????????
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors",
				qbrIndexGpsPointInfo.getIndexColorListMap(indexType));
		returnList.add(2, jsonObject);

		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ????????????????????????,Srvcc???????????????????????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryCompareHofCellToCell() {
		List<Map<String, Object>> returnList = new ArrayList<>();
		List<Long> ids = new ArrayList<>();
		if (StringUtils.hasText(testLogItemIds)) {
			String[] testLogItemIdString = testLogItemIds.trim().split(",");
			// ??????TestLogItem???id??????
			for (int i = 0; i < testLogItemIdString.length; i++) {
				if (StringUtils.hasText(testLogItemIdString[i])) {
					try {
						ids.add(Long.parseLong(testLogItemIdString[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
		}
		if (StringUtils.hasText(compareTestLogItemIds)) {
			String[] testLogItemIdString = compareTestLogItemIds.trim().split(
					",");
			// ??????TestLogItem???id??????
			for (int i = 0; i < testLogItemIdString.length; i++) {
				if (StringUtils.hasText(testLogItemIdString[i])) {
					try {
						ids.add(Long.parseLong(testLogItemIdString[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
		}
		// ?????????????????????id??????????????????id?????????cellid???failid??????????????????
		if (null != hofType) {
			if (0 == hofType) {
				List<VolteDroppingSRVCC> queryVolteDroppingSRVCCsByLogIdsAndFailId = volteDroppingService
						.queryVolteDroppingSRVCCsByLogIdsAndFailId(ids, failId);
				for (VolteDroppingSRVCC volteDroppingSRVCC : queryVolteDroppingSRVCCsByLogIdsAndFailId) {
					Map<String, Object> map = new HashMap<String, Object>();
					if (volteDroppingSRVCC != null
							&& volteDroppingSRVCC.getCellId() != null
							&& volteDroppingSRVCC.getCellId() != 0
							&& volteDroppingSRVCC.getFailId() != null
							&& volteDroppingSRVCC.getFailId() != 0) {
						map.put("cellId", volteDroppingSRVCC.getCellId());
						map.put("nbCellId", volteDroppingSRVCC.getFailId());
					}
					returnList.add(map);
				}
			} else if (1 == hofType) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("cellId", cellId);
				map.put("nbCellId", failId);
				returnList.add(map);
			}
		}
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ??????RTP???????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryLPGpsPoint() {
		List<Object> returnList = new ArrayList<>();
		List<List<Object>> everyQBRGpsPointsByTestlogIds = lpGpsPointService
				.getEveryLPGpsPointsByTestlogIds(testLogItemIds);
		if (0 != everyQBRGpsPointsByTestlogIds.size()) {
			returnList.add(0, everyQBRGpsPointsByTestlogIds);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors", lpGpsPointInfo.getColorListMap());
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ?????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryVQBIndexGpsPoint() {
		List<TestLogItemIndexGpsPoint> pointsByVQBIdAndIndexType = vqbGpsPointService
				.getPointsByVQBIdAndIndexType(videoQualityBadId, indexType);
		List<Object> returnList = new ArrayList<>();
		List<List<Object>> pointLists = new ArrayList<>();
		if (0 != pointsByVQBIdAndIndexType.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();

			for (TestLogItemIndexGpsPoint badRoadGpsPoint : pointsByVQBIdAndIndexType) {
				Long cellId = badRoadGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(badRoadGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		returnList.add(0, pointLists);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(
				"colors",
				indexType == 6 ? tmGpsPointInfo.getColorListMap()
						: videoQbrIndexGpsPointInfo
								.getIndexColorListMap(indexType));
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ??????????????????LTE???????????????????????? ??????????????????IconType???50????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryVQBLTEGpsPoint() {
		List<TestLogItemIndexGpsPoint> gpsPoints = vqbGpsPointService
				.getLTEPointsByVQBId(videoQualityBadId);
		List<List<Object>> pointLists = new ArrayList<>();
		if (0 != gpsPoints.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint badRoadGpsPoint : gpsPoints) {
				Long cellId = badRoadGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				/*
				 * TestLogItemIndexGpsPoint videoQualityBadLTEGpsPoint = new
				 * TestLogItemIndexGpsPoint();
				 * videoQualityBadLTEGpsPoint.setCellId(cellId);
				 * videoQualityBadLTEGpsPoint.setLatitude(badRoadGpsPoint
				 * .getLatitude());
				 * videoQualityBadLTEGpsPoint.setLongitude(badRoadGpsPoint
				 * .getLongitude());
				 * videoQualityBadLTEGpsPoint.setIconType(badRoadGpsPoint
				 * .getIndexType());
				 */
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(badRoadGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		ActionContext.getContext().getValueStack().push(pointLists);
		return ReturnType.JSON;
	}

	/**
	 * ??????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryVQBDirection() {
		// [
		// {
		// "latitude" : 31.269161,//???????????????
		// "longitude" : 121.4899//???????????????
		// },..,{}
		// ]
		List<TestLogItemGpsPoint> pointDirectionsByVQBIdAndIndexType = vqbGpsPointService
				.getPointDirectionsByVQBIdAndIndexType(videoQualityBadId,
						indexType);
		LinkedList<TestLogItemGpsPoint> badRoadGpsPoints = new LinkedList<>();
		for (int i = 0; i < pointDirectionsByVQBIdAndIndexType.size(); i++) {
			TestLogItemGpsPoint testLogItemGpsPoint = pointDirectionsByVQBIdAndIndexType
					.get(i);
			if (0 == i) {
				// ????????????
				badRoadGpsPoints.addLast(testLogItemGpsPoint);
			}
			if ((i + 1) < pointDirectionsByVQBIdAndIndexType.size()) {
				TestLogItemGpsPoint nexttestLogItemGpsPoint = pointDirectionsByVQBIdAndIndexType
						.get(i + 1);
				Double latitude = testLogItemGpsPoint.getLatitude();
				Double longitude = testLogItemGpsPoint.getLongitude();
				Double latitude2 = nexttestLogItemGpsPoint.getLatitude();
				Double longitude2 = nexttestLogItemGpsPoint.getLongitude();
				try {
					double distance = GPSUtils.distance(latitude, longitude,
							latitude2, longitude2);
					// ????????????????????????2???
					if (distance >= 2) {
						if ((i + 1) != pointDirectionsByVQBIdAndIndexType
								.size() - 1) {
							badRoadGpsPoints.addLast(nexttestLogItemGpsPoint);
						}
					}
				} catch (Exception e) {
					// to do nothing
				}
			} else {
				// ???????????????
				badRoadGpsPoints.addLast(testLogItemGpsPoint);
			}
		}
		ActionContext.getContext().getValueStack().push(badRoadGpsPoints);
		return ReturnType.JSON;
	}

	/**
	 * ????????????????????????????????????LTE??????????????????????????????
	 * 
	 * @return
	 */
	public String queryVQBCellToCell() {
		// [
		// {
		// "cellId" :,//LTE??????cellid
		// "nbCellId" : //??????cellid
		// },..,{}
		// ]
		ActionContext
				.getContext()
				.getValueStack()
				.push(vqbnPlotCellToCelllService
						.getCellToCellInfo(videoQualityBadId));
		return ReturnType.JSON;
	}

	/**
	 * ??????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryVQBGpsPoint() {
		// [
		// [["WeakCover",[{},{}],[{},{}]],["Disturb",[{},{}],[{},{}]]],
		// {"colors" : [{"color" : "#ffffff","qbrType" : "WeakCover"},{"color" :
		// "#ffffff","qbrType" : "Disturb"}]}
		// ]

		List<Object> returnList = new ArrayList<>();
		List<List<Object>> everyVQBGpsPointsByTestlogIds = vqbGpsPointService
				.getEveryVQBGpsPointsByTestlogIds(testLogItemIds);
		if (0 != everyVQBGpsPointsByTestlogIds.size()) {
			returnList.add(0, everyVQBGpsPointsByTestlogIds);
		}
		// JSONObject jsonObject = new JSONObject();
		// jsonObject.put("color", testLogItemGpsPointColor);
		// returnList.add(1, jsonObject);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors", videoQBGpsPointInfo.getColorListMap());
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ???????????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String querySVQBGpsPoint() {
		// [
		// [["WeakCover",[{},{}],[{},{}]],["Disturb",[{},{}],[{},{}]]],
		// {"colors" : [{"color" : "#ffffff","qbrType" : "WeakCover"},{"color" :
		// "#ffffff","qbrType" : "Disturb"}]}
		// ]

		List<Object> returnList = new ArrayList<>();
		List<List<Object>> everyVQBGpsPointsByTestlogIds = svqbGpsPointService
				.getEveryVQBGpsPointsByTestlogIds(testLogItemIds);
		if (0 != everyVQBGpsPointsByTestlogIds.size()) {
			returnList.add(0, everyVQBGpsPointsByTestlogIds);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors", streamQBGpsPointInfo.getColorListMap());
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ??????????????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String querySVQBIndexGpsPoint() {
		List<TestLogItemIndexGpsPoint> pointsByVQBIdAndIndexType = svqbGpsPointService
				.getPointsByVQBIdAndIndexType(videoQualityBadId, indexType);
		List<Object> returnList = new ArrayList<>();
		List<List<Object>> pointLists = new ArrayList<>();
		if (0 != pointsByVQBIdAndIndexType.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();

			for (TestLogItemIndexGpsPoint badRoadGpsPoint : pointsByVQBIdAndIndexType) {
				Long cellId = badRoadGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(badRoadGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		returnList.add(0, pointLists);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(
				"colors",
				indexType == 6 ? tmGpsPointInfo.getColorListMap()
						: videoQbrIndexGpsPointInfo
								.getIndexColorListMap(indexType));
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ???????????????????????????LTE????????????????????????
	 * 
	 * @return
	 */
	public String querySVQBLTEGpsPoint() {
		List<TestLogItemIndexGpsPoint> gpsPoints = svqbGpsPointService
				.getLTEPointsByVQBId(videoQualityBadId);
		List<List<Object>> pointLists = new ArrayList<>();
		if (0 != gpsPoints.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint badRoadGpsPoint : gpsPoints) {
				Long cellId = badRoadGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				/*
				 * TestLogItemIndexGpsPoint videoQualityBadLTEGpsPoint = new
				 * TestLogItemIndexGpsPoint();
				 * videoQualityBadLTEGpsPoint.setCellId(cellId);
				 * videoQualityBadLTEGpsPoint.setLatitude(badRoadGpsPoint
				 * .getLatitude());
				 * videoQualityBadLTEGpsPoint.setLongitude(badRoadGpsPoint
				 * .getLongitude());
				 * videoQualityBadLTEGpsPoint.setIconType(badRoadGpsPoint
				 * .getIndexType());
				 */
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(badRoadGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		ActionContext.getContext().getValueStack().push(pointLists);
		return ReturnType.JSON;
	}

	/**
	 * ???????????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String querySVQBDirection() {
		// [
		// {
		// "latitude" : 31.269161,//???????????????
		// "longitude" : 121.4899//???????????????
		// },..,{}
		// ]
		List<TestLogItemGpsPoint> pointDirectionsByVQBIdAndIndexType = svqbGpsPointService
				.getPointDirectionsByVQBIdAndIndexType(videoQualityBadId,
						indexType);
		LinkedList<TestLogItemGpsPoint> badRoadGpsPoints = new LinkedList<>();
		for (int i = 0; i < pointDirectionsByVQBIdAndIndexType.size(); i++) {
			TestLogItemGpsPoint testLogItemGpsPoint = pointDirectionsByVQBIdAndIndexType
					.get(i);
			if (0 == i) {
				// ????????????
				badRoadGpsPoints.addLast(testLogItemGpsPoint);
			}
			if ((i + 1) < pointDirectionsByVQBIdAndIndexType.size()) {
				TestLogItemGpsPoint nexttestLogItemGpsPoint = pointDirectionsByVQBIdAndIndexType
						.get(i + 1);
				Double latitude = testLogItemGpsPoint.getLatitude();
				Double longitude = testLogItemGpsPoint.getLongitude();
				Double latitude2 = nexttestLogItemGpsPoint.getLatitude();
				Double longitude2 = nexttestLogItemGpsPoint.getLongitude();
				try {
					double distance = GPSUtils.distance(latitude, longitude,
							latitude2, longitude2);
					// ????????????????????????2???
					if (distance >= 2) {
						if ((i + 1) != pointDirectionsByVQBIdAndIndexType
								.size() - 1) {
							badRoadGpsPoints.addLast(nexttestLogItemGpsPoint);
						}
					}
				} catch (Exception e) {
					// to do nothing
				}
			} else {
				// ???????????????
				badRoadGpsPoints.addLast(testLogItemGpsPoint);
			}
		}
		ActionContext.getContext().getValueStack().push(badRoadGpsPoints);
		return ReturnType.JSON;
	}

	/**
	 * ?????????????????????????????????????????????LTE??????????????????????????????
	 * 
	 * @return
	 */
	public String querySVQBCellToCell() {
		// [
		// {
		// "cellId" :,//LTE??????cellid
		// "nbCellId" : //??????cellid
		// },..,{}
		// ]
		ActionContext
				.getContext()
				.getValueStack()
				.push(svqbnPlotCellToCelllService
						.getCellToCellInfo(videoQualityBadId));
		return ReturnType.JSON;
	}

	/**
	 * (5G)??????EMBB?????????????????????????????????????????????????????????(??????RSRP)
	 * 
	 * @return
	 */
	public String queryEmbbCoverRoadsPoints() {

		// [
		// [
		// [
		// "cellId",//??????cellid??????
		// [
		// {
		// "indexValue" : 2.33,//?????????
		// "latitude" : 31.269161,//???????????????
		// "longitude" : 121.4899//???????????????
		// },..,{}
		// ]
		// ],...,
		// [
		// "cellId",
		// [
		// {
		// "indexValue" : 2.33,
		// "latitude" : 31.269161,
		// "longitude" : 121.4899
		// },???,{}
		// ]
		// ]
		// ],
		// {
		// "colors" : [
		// {
		// "beginValue" : 108,//?????????????????????
		// "color" : "#ffffff",//??????????????????????????????
		// "endValue" : 110//?????????????????????
		// },...,
		// {
		// "beginValue" : 108,
		// "color" : "#ffffff",
		// "endValue" : 110
		// }
		// ]
		// }
		// ]
		if (null == indexType) {
			indexType = 1;
		}
		List<TestLogItemIndexGpsPoint> pointsByQBRIdAndIndexType = embbCoverGpsPointService
				.getPointsByTestLogItem(testLogItemIds, coverType, indexType);
		List<Object> returnList = new ArrayList<>();
		List<List<Object>> pointLists = new ArrayList<>();
		if (0 != pointsByQBRIdAndIndexType.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint badRoadGpsPoint : pointsByQBRIdAndIndexType) {
				Long cellId = badRoadGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(badRoadGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		returnList.add(0, pointLists);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors",
				qbrIndexGpsPointInfo.getIndexColorListMap5g(indexType));
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;

	}
	
	/**
	 * (5G)?????????????????????????????????????????????????????????????????????(??????SINR)
	 * 
	 * @return
	 */
	public String queryQualityBadRoadsPoints() {

		if (null == indexType) {
			indexType = 2;
		}
		List<TestLogItemIndexGpsPoint> pointsByQBRIdAndIndexType = qbrGpsPointService.getPointsByTestLogItem(testLogItemIds,indexType);
		List<Object> returnList = new ArrayList<>();
		List<List<Object>> pointLists = new ArrayList<>();
		if (0 != pointsByQBRIdAndIndexType.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint badRoadGpsPoint : pointsByQBRIdAndIndexType) {
				Long cellId = badRoadGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(badRoadGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		returnList.add(0, pointLists);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors",
				qbrIndexGpsPointInfo.getIndexColorListMap5g(indexType));
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;

	}

	/**
	 * (5G)??????EMBB???????????????????????????????????????(??????RSRP)
	 * 
	 * @return
	 */
	public String queryEmbbCoverRoadPoints() {
		// [
		// [
		// [
		// "cellId",//??????cellid??????
		// [
		// {
		// "indexValue" : 2.33,//?????????
		// "latitude" : 31.269161,//???????????????
		// "longitude" : 121.4899//???????????????
		// },..,{}
		// ]
		// ],...,
		// [
		// "cellId",
		// [
		// {
		// "indexValue" : 2.33,
		// "latitude" : 31.269161,
		// "longitude" : 121.4899
		// },???,{}
		// ]
		// ]
		// ],
		// {
		// "colors" : [
		// {
		// "beginValue" : 108,//?????????????????????
		// "color" : "#ffffff",//??????????????????????????????
		// "endValue" : 110//?????????????????????
		// },...,
		// {
		// "beginValue" : 108,
		// "color" : "#ffffff",
		// "endValue" : 110
		// }
		// ]
		// }
		// ]
		if (null == indexType) {
			indexType = 1;
		}
		List<TestLogItemIndexGpsPoint> pointsByQBRIdAndIndexType = embbCoverGpsPointService
				.getPointsByEmbbIdAndIndexType(badRoadId, indexType);
		List<Object> returnList = new ArrayList<>();
		List<List<Object>> pointLists = new ArrayList<>();
		if (0 != pointsByQBRIdAndIndexType.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint badRoadGpsPoint : pointsByQBRIdAndIndexType) {
				Long cellId = badRoadGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(badRoadGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		returnList.add(0, pointLists);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors",
				qbrIndexGpsPointInfo.getIndexColorListMap5g(indexType));
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * (5G)??????EMBB??????????????????????????????????????????????????????(??????RSRP)
	 * 
	 * @return
	 */
	public String queryEmbbCoverRoadDirection() {
		// [
		// {
		// "latitude" : 31.269161,//???????????????
		// "longitude" : 121.4899//???????????????
		// },..,{}
		// ]
		if (null == indexType) {
			indexType = 1;
		}
		List<TestLogItemGpsPoint> pointsByQBRIdAndIndexType = embbCoverGpsPointService
				.getPointDirectionsByEmbbIdAndIndexType(badRoadId, indexType);
		LinkedList<TestLogItemGpsPoint> badRoadGpsPoints = new LinkedList<>();
		for (int i = 0; i < pointsByQBRIdAndIndexType.size(); i++) {
			TestLogItemGpsPoint testLogItemGpsPoint = pointsByQBRIdAndIndexType
					.get(i);
			if (0 == i) {
				// ????????????
				badRoadGpsPoints.addLast(testLogItemGpsPoint);
			}
			if ((i + 1) < pointsByQBRIdAndIndexType.size()) {
				TestLogItemGpsPoint nexttestLogItemGpsPoint = pointsByQBRIdAndIndexType
						.get(i + 1);
				Double latitude = testLogItemGpsPoint.getLatitude();
				Double longitude = testLogItemGpsPoint.getLongitude();
				Double latitude2 = nexttestLogItemGpsPoint.getLatitude();
				Double longitude2 = nexttestLogItemGpsPoint.getLongitude();
				try {
					double distance = GPSUtils.distance(latitude, longitude,
							latitude2, longitude2);
					// ????????????????????????2???
					if (distance >= 2) {
						if ((i + 1) != pointsByQBRIdAndIndexType.size() - 1) {
							badRoadGpsPoints.addLast(nexttestLogItemGpsPoint);
						}
					}
				} catch (Exception e) {
					// to do nothing
				}
			} else {
				// ???????????????
				badRoadGpsPoints.addLast(testLogItemGpsPoint);
			}
		}
		ActionContext.getContext().getValueStack().push(badRoadGpsPoints);
		return ReturnType.JSON;
	}
	
	/**
	 * (5G)???????????????????????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String queryTestTrailRoadsPoints() {

		// [
		// [
		// [
		// "cellId",//??????cellid??????
		// [
		// {
		// "indexValue" : 2.33,//?????????
		// "latitude" : 31.269161,//???????????????
		// "longitude" : 121.4899//???????????????
		// },..,{}
		// ]
		// ],...,
		// [
		// "cellId",
		// [
		// {
		// "indexValue" : 2.33,
		// "latitude" : 31.269161,
		// "longitude" : 121.4899
		// },???,{}
		// ]
		// ]
		// ],
		// {
		// "colors" : [
		// {
		// "beginValue" : 108,//?????????????????????
		// "color" : "#ffffff",//??????????????????????????????
		// "endValue" : 110//?????????????????????
		// },...,
		// {
		// "beginValue" : 108,
		// "color" : "#ffffff",
		// "endValue" : 110
		// }
		// ]
		// }
		// ]
		List<TestLogItem> queryTestLogItems = testlogItemService
				.queryTestLogItems(testLogItemIds);
		Set<String> logNameSet = new HashSet<String>();
		for (TestLogItem testLogItem : queryTestLogItems) {
			logNameSet.add(testLogItem.getFileName());
		}
		List<String> logNameList = new ArrayList(logNameSet);
		List<TestLogItemIndexGpsPoint> pointsByLogName = embbCoverGpsPointService.getPointsExceptionEtgTral(logNameList);

//		if (null == indexType) {
//			indexType = 1;
//		}
//		List<TestLogItemIndexGpsPoint> pointsByQBRIdAndIndexType = embbCoverGpsPointService
//				.getPointsByTestLogItem(testLogItemIds, coverType, indexType);
		List<Object> returnList = new ArrayList<>();
		List<List<Object>> pointLists = new ArrayList<>();
		if (0 != pointsByLogName.size()) {
			Map<Long, LinkedList<TestLogItemIndexGpsPoint>> map = new HashMap<Long, LinkedList<TestLogItemIndexGpsPoint>>();
			for (TestLogItemIndexGpsPoint badRoadGpsPoint : pointsByLogName) {
				Long cellId = badRoadGpsPoint.getCellId();
				if (null == cellId) {
					continue;
				}
				if (null == map.get(cellId)) {
					map.put(cellId, new LinkedList<TestLogItemIndexGpsPoint>());
				}
				LinkedList<TestLogItemIndexGpsPoint> linkedList = map
						.get(cellId);
				linkedList.add(badRoadGpsPoint);
			}
			for (Entry<Long, LinkedList<TestLogItemIndexGpsPoint>> entry : map
					.entrySet()) {
				List<Object> list = new LinkedList<>();
				list.add(entry.getKey());
				list.add(entry.getValue());
				pointLists.add(list);
			}
		}
		returnList.add(0, pointLists);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors",
				qbrIndexGpsPointInfo.getIndexColorListMap5g(indexType));
		returnList.add(1, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;

	}
	
	public String getNcellNamesByCellName(){
		try {
			Object obj = projectParamService.getDistanceByCellName(cellName,coverType);
			Class<? extends Object> cellDataClass = obj.getClass();
			Object nCell = cellDataClass.getMethod("getnCellNames").invoke(obj);
			ActionContext.getContext().getValueStack().push(nCell);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ReturnType.JSON;
		
	}
	

	/**
	 * ???????????? ??????????????????????????????????????????????????? ????????????
	 * 
	 * @return
	 */
	// public String queryTestLongIndexGpsPoint() {
	// List<TestLogItemGpsPoint> pointsByTestLogIds = testLogItemGpsPointService
	// .getPointsByTestLogIds(testLogItemIds);
	// List<List<TestLogItemGpsPoint>> pointLists2 = new ArrayList<>();
	// ArrayList<String> arrayStrings = new ArrayList<String>();
	// ArrayList<String> arrayStrings2 = new ArrayList<String>();
	// if (0 != pointsByTestLogIds.size()) {
	// for (TestLogItemGpsPoint testLogItemGpsPoint : pointsByTestLogIds) {
	// arrayStrings.add(testLogItemGpsPoint.getLatitude() + ","
	// + testLogItemGpsPoint.getLongitude());
	// }
	// }
	// String[] logIds = testLogItemIds.trim().split(",");
	// // ??????TestLogItem???id??????
	// List<Long> ids = new ArrayList<>();
	// for (int i = 0; i < logIds.length; i++) {
	// if (StringUtils.hasText(logIds[i])) {
	// try {
	// ids.add(Long.parseLong(logIds[i].trim()));
	// } catch (NumberFormatException e) {
	// continue;
	// }
	// }
	// }
	// List<TestLogItemIndexGpsPoint> gpsPoints = testLogItemGpsPointDetailDao
	// .getTestLogsItemGpsIndexPoint(ids, indexType, null, null);
	//
	// List<Object> returnList = new ArrayList<>();
	// List<Object> pointLists = new ArrayList<>();
	//
	// if (0 != gpsPoints.size()) {
	// Map<String, LinkedList<TestLogItemIndexGpsPoint>> map = new
	// HashMap<String, LinkedList<TestLogItemIndexGpsPoint>>();
	// for (int i = 0; i < gpsPoints.size(); i++) {
	// // System.out.println(gpsPoints.get(i).getCellId() + ","
	// // + gpsPoints.get(i).getLatitude() + "####"
	// // + gpsPoints.get(i).getLongitude());
	// Float latitude = gpsPoints.get(i).getLatitude();
	// Float longitude = gpsPoints.get(i).getLongitude();
	// if (null == latitude || longitude == null) {
	// continue;
	// }
	// if (null == map.get(latitude + "," + longitude)) {
	// map.put(latitude + "," + longitude,
	// new LinkedList<TestLogItemIndexGpsPoint>());
	// arrayStrings2.add(latitude + "," + longitude);
	// }
	// LinkedList<TestLogItemIndexGpsPoint> linkedList = map
	// .get(latitude + "," + longitude);
	// linkedList.add(gpsPoints.get(i));
	//
	// }
	// for (int i = 0; i < arrayStrings.size(); i++) {
	// System.out.println("1" + arrayStrings.get(i) + i);
	// System.out.println("2" + arrayStrings2.get(i) + i);
	//
	// }
	// System.out.println(arrayStrings.size());
	// System.out.println(arrayStrings2.size());
	// for (int i = 0; i < arrayStrings.size() - 1; i++) {
	// // System.out.println(arrayStrings.get(i));
	// String str1 = arrayStrings.get(i);
	// String str2 = arrayStrings.get(i + 1);
	// Integer sum = map.get(str1).size();
	// List<TestLogItemIndexGpsPoint> testLogItemIndexGpsPoints =
	// getTestLogItemIndexGpsPoints(
	// str1, str2, sum);
	// for (int j = 0; j < testLogItemIndexGpsPoints.size(); j++) {
	// if (map.get(str1).size() > j) {
	// testLogItemIndexGpsPoints.get(j).setIndexValue(
	// map.get(str1).get(j).getIndexValue());
	// pointLists.add(testLogItemIndexGpsPoints.get(j));
	// }
	// }
	// }
	// }
	// returnList.add(0, pointLists);
	// JSONObject jsonObject = new JSONObject();
	// jsonObject.put("colors",
	// qbrIndexGpsPointInfo.getIndexColorListMap(indexType));
	// returnList.add(1, jsonObject);
	// ActionContext.getContext().getValueStack().push(returnList);
	// return ReturnType.JSON;
	//
	// }

	/**
	 * ???????????? ????????????????????? ????????????
	 * 
	 * @param str1
	 * @param str2
	 * @param sum
	 * @return
	 */
	// public List<TestLogItemIndexGpsPoint> getTestLogItemIndexGpsPoints(
	// String str1, String str2, Integer sum) {
	// ArrayList<TestLogItemIndexGpsPoint> list = new
	// ArrayList<TestLogItemIndexGpsPoint>();
	// String[] split1 = str1.trim().split(",");
	// String[] split2 = str2.trim().split(",");
	// Float latitude1 = Float.valueOf(split1[0]);
	// Float longitude1 = Float.valueOf(split1[1]);
	// Float latitude2 = Float.valueOf(split2[0]);
	// Float longitude2 = Float.valueOf(split2[1]);
	// for (int i = 0; i < sum; i++) {
	// Random random = new Random();
	// Float indexvFloat = Float.valueOf(String.valueOf((random
	// .nextInt(20) + 80) * (-1.0)));
	// Float latitudeNew = latitude1 + ((latitude2 - latitude1) / sum) * i;
	// Float longitudeNew = longitude1 + ((longitude2 - longitude1) / sum)
	// * i;
	// TestLogItemIndexGpsPoint testLogItemIndexGpsPoint = new
	// TestLogItemIndexGpsPoint();
	// testLogItemIndexGpsPoint.setLatitude(latitudeNew);
	// testLogItemIndexGpsPoint.setLongitude(longitudeNew);
	// testLogItemIndexGpsPoint.setIndexValue(indexvFloat);
	// list.add(testLogItemIndexGpsPoint);
	// }
	// return list;
	// }

	/**
	 * ??????????????????(???????????????SQL,?????????3???color??????,SQL???????????????);
	 * 
	 * @Date 20151126 yinzhipeng????????????
	 * 
	 * @return
	 */
	@Deprecated
	public String queryCellSql() {
		List<Object> returnList = new ArrayList<>();
		returnList.add(0, creatCellSql(testLogItemIds));
		returnList.add(1, gisDb);
		JSONObject jsonObject0 = new JSONObject();
		jsonObject0.put("modelValue", 0);
		jsonObject0.put("color", cellModel3Color0);
		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("modelValue", 1);
		jsonObject1.put("color", cellModel3Color1);
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("modelValue", 2);
		jsonObject2.put("color", cellModel3Color2);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("colors", new JSONObject[] { jsonObject0, jsonObject1,
				jsonObject2 });
		returnList.add(2, jsonObject);
		ActionContext.getContext().getValueStack().push(returnList);
		return ReturnType.JSON;
	}

	/**
	 * ????????????SQL
	 * 
	 * @Date 20151126 yinzhipeng????????????
	 * 
	 * @param ids
	 *            ???????????????ID???","??????????????????
	 * @return
	 */
	@Deprecated
	public String creatCellSql(String ids) {
		StringBuffer buffer = new StringBuffer();

		// SELECT THIS1.MCC AS MCC
		buffer.append(SqlConstant.SELECT);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_MCC);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.MCC);
		buffer.append(",");
		// THIS1.MNC AS MNC,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_MNC);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.MNC);
		buffer.append(",");
		// THIS1.MME_GROUP_ID AS MMEGROUPID,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_MME_GROUP_ID);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.MMEGROUPID);
		buffer.append(",");
		// THIS1.MME_ID AS MMEID,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_MME_ID);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.MMEID);
		buffer.append(",");
		// THIS1.ENB_ID AS ENODEBID,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_ENB_ID);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.ENB_ID);
		buffer.append(",");
		// THIS1.SITE_NAME AS SITENAME,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_SITE_NAME);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.SITENAME);
		buffer.append(",");
		// THIS1.CELL_NAME AS CELLNAME,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_CELL_NAME);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.CELL_NAME);
		buffer.append(",");
		// THIS1.LOCAL_CELL_ID AS LOCALCELLI,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_LOCAL_CELL_ID);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.LOCALCELLI);
		buffer.append(",");
		// THIS1.CELL_ID AS CELLID,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_CELL_ID);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.CELL_ID);
		buffer.append(",");
		// THIS1.TAC AS TAC,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_TAC);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.TAC);
		buffer.append(",");
		// THIS1.PCI AS PCI,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_PCI);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.PCI);
		buffer.append(",");
		// THIS1.FREQUENCY1 AS FREQ1,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_FREQUENCY1);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.FREQ1);
		buffer.append(",");
		// THIS1.FREQUENCY2 AS FREQ2,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_FREQUENCY2);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.FREQ2);
		buffer.append(",");
		// THIS1.BAND_WIDTH1 AS BANDWIDTH1,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_BAND_WIDTH1);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.BANDWIDTH1);
		buffer.append(",");
		// THIS1.BAND_WIDTH2 AS BANDWIDTH1,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_BAND_WIDTH2);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.BANDWIDTH2);
		buffer.append(",");
		// THIS1.FREQ_COUNT AS FREQCOUNT,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_FREQ_COUNT);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.FREQCOUNT);
		buffer.append(",");
		// THIS1.LONGITUDE AS LONGITUDE,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_LONGITUDE);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.LONGITUDE);
		buffer.append(",");
		// THIS1.LATITUDE AS LATITUDE,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_LATITUDE);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.LATITUDE);
		buffer.append(",");
		// THIS1.SECTOR_TYPE AS SECTORTYPE,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_SECTOR_TYPE);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.SECTOR_TYPE);
		buffer.append(",");
		// THIS1.DOOR_TYPE AS DOORTYPE,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_DOOR_TYPE);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.DOORTYPE);
		buffer.append(",");
		// THIS1.TOTAL_TILT AS TILTTOTAL,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_TOTAL_TILT);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.TILTTOTAL);
		buffer.append(",");
		// THIS1.MECH_TILT AS TILTM,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_MECH_TILT);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.TILTM);
		buffer.append(",");
		// THIS1.ELEC_TILT AS TILTE,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_ELEC_TILT);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.TILTE);
		buffer.append(",");
		// THIS1.AZIMUTH AS AZIMUTH,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_AZIMUTHE);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.AZIMUTHE);
		buffer.append(",");
		// THIS1.BEAM_WIDTH AS BEAMWIDTH,
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_BEAM_WIDTH);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.BEAM_WIDTH);
		buffer.append(",");
		// MOD(THIS1.PCI,3) AS MODEL3
		buffer.append(SqlConstant.MOD);
		buffer.append("(");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_PCI);
		buffer.append(",3)");
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.MODEL3);
		buffer.append(SqlConstant.SPACE);
		// FROM IADS_LTE_CELL THIS1,
		buffer.append(SqlConstant.FROM);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.LTE_CELL_TABLE);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS1);
		buffer.append(",");
		// IADS_CELL_INFO THIS2,
		buffer.append(CellSQLConstant.CELL_INFO_TABLE);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS2);
		buffer.append(",");
		// IADS_TERMINAL_GROUP THIS3,
		buffer.append(CellSQLConstant.TERMINAL_GROUP_TABLE);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS3);
		buffer.append(",");
		// IADS_TESTLOG_ITEM THIS4
		buffer.append(CellSQLConstant.TESTLOG_ITEM_TABLE);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS4);
		buffer.append(SqlConstant.SPACE);
		// WHERE THIS1.CI_ID = THIS2.ID
		buffer.append(SqlConstant.WHERE);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_CI_ID);
		buffer.append(SqlConstant.SPACE);
		buffer.append("=");
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS2);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_INFO_ID);
		buffer.append(SqlConstant.SPACE);
		// AND THIS2.OPERATOR_TYPE = THIS4.OPERATOR_NAME
		buffer.append(SqlConstant.AND);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS2);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_INFO_OPERATOR_TYPE);
		buffer.append(SqlConstant.SPACE);
		buffer.append("=");
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS4);
		buffer.append(".");
		buffer.append(CellSQLConstant.TESTLOG_ITEM_OPERATOR_NAME);
		buffer.append(SqlConstant.SPACE);
		// AND THIS2.TID = THIS3.ID
		buffer.append(SqlConstant.AND);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS2);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_INFO_TID);
		buffer.append(SqlConstant.SPACE);
		buffer.append("=");
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS3);
		buffer.append(".");
		buffer.append(CellSQLConstant.TERMINAL_GROUP_ID);
		buffer.append(SqlConstant.SPACE);
		// AND THIS3.NAME = THIS4.TERMINAL_GROUP
		buffer.append(SqlConstant.AND);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS3);
		buffer.append(".");
		buffer.append(CellSQLConstant.TERMINAL_GROUP_NAME);
		buffer.append(SqlConstant.SPACE);
		buffer.append("=");
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS4);
		buffer.append(".");
		buffer.append(CellSQLConstant.TESTLOG_ITEM_TERMINAL_GROUP);
		buffer.append(SqlConstant.SPACE);
		// AND THIS4.RECSEQNO IN(1,2)
		buffer.append(SqlConstant.AND);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS4);
		buffer.append(".");
		buffer.append(CellSQLConstant.TESTLOG_ITEM_RECSEQNO);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.IN);
		buffer.append("(");
		buffer.append(ids);
		buffer.append(")");
		buffer.append(SqlConstant.SPACE);
		// GROUP BY
		buffer.append(SqlConstant.GROUP_BY);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_MCC);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_MNC);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_MME_GROUP_ID);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_MME_ID);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_ENB_ID);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_SITE_NAME);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_CELL_NAME);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_LOCAL_CELL_ID);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_CELL_ID);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_TAC);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_PCI);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_FREQUENCY1);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_FREQUENCY2);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_BAND_WIDTH1);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_BAND_WIDTH2);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_FREQ_COUNT);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_LONGITUDE);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_LATITUDE);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_SECTOR_TYPE);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_DOOR_TYPE);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_TOTAL_TILT);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_MECH_TILT);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_ELEC_TILT);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_AZIMUTHE);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_BEAM_WIDTH);

		return buffer.toString();
	}

	/**
	 * @return the testLogItemIdstestLogItemIds
	 */
	public String getTestLogItemIds() {
		return testLogItemIds;
	}

	/**
	 * @param testLogItemIds
	 *            the testLogItemIds to set
	 */
	public void setTestLogItemIds(String testLogItemIds) {
		this.testLogItemIds = testLogItemIds;
	}

	/**
	 * @return the badRoadIdbadRoadId
	 */
	public Long getBadRoadId() {
		return badRoadId;
	}

	/**
	 * @param badRoadId
	 *            the badRoadId to set
	 */
	public void setBadRoadId(Long badRoadId) {
		this.badRoadId = badRoadId;
	}

	/**
	 * @return the indexTypeindexType
	 */
	public Integer getIndexType() {
		return indexType;
	}

	/**
	 * @param indexType
	 *            the indexType to set
	 */
	public void setIndexType(Integer indexType) {
		this.indexType = indexType;
	}

	/**
	 * @return the eeTypeeeType
	 */
	public Integer getEeType() {
		return eeType;
	}

	/**
	 * @param eeType
	 *            the eeType to set
	 */
	public void setEeType(Integer eeType) {
		this.eeType = eeType;
	}

	/**
	 * @return the hofTypehofType
	 */
	public Integer getHofType() {
		return hofType;
	}

	/**
	 * @param hofType
	 *            the hofType to set
	 */
	public void setHofType(Integer hofType) {
		this.hofType = hofType;
	}

	/**
	 * @return the iconTypeiconType
	 */
	public String getIconType() {
		return iconType;
	}

	/**
	 * @param iconType
	 *            the iconType to set
	 */
	public void setIconType(String iconType) {
		this.iconType = iconType;
	}

	/**
	 * @return the callTypecallType
	 */
	public Integer getCallType() {
		return callType;
	}

	/**
	 * @param callType
	 *            the callType to set
	 */
	public void setCallType(Integer callType) {
		this.callType = callType;
	}

	/**
	 * @return the eeIdeeId
	 */
	public Long getEeId() {
		return eeId;
	}

	/**
	 * @param eeId
	 *            the eeId to set
	 */
	public void setEeId(Long eeId) {
		this.eeId = eeId;
	}

	/**
	 * @return the hofIdhofId
	 */
	public Long getHofId() {
		return hofId;
	}

	/**
	 * @param hofId
	 *            the hofId to set
	 */
	public void setHofId(Long hofId) {
		this.hofId = hofId;
	}

	public Long getCedeId() {
		return cedeId;
	}

	public void setCedeId(Long cedeId) {
		this.cedeId = cedeId;
	}

	/**
	 * @return the compareTestLogItemIdscompareTestLogItemIds
	 */
	public String getCompareTestLogItemIds() {
		return compareTestLogItemIds;
	}

	/**
	 * @param compareTestLogItemIds
	 *            the compareTestLogItemIds to set
	 */
	public void setCompareTestLogItemIds(String compareTestLogItemIds) {
		this.compareTestLogItemIds = compareTestLogItemIds;
	}

	/**
	 * @return the compareBadRoadIdscompareBadRoadIds
	 */
	public String getCompareBadRoadIds() {
		return compareBadRoadIds;
	}

	/**
	 * @param compareBadRoadIds
	 *            the compareBadRoadIds to set
	 */
	public void setCompareBadRoadIds(String compareBadRoadIds) {
		this.compareBadRoadIds = compareBadRoadIds;
	}

	/**
	 * @return the mosBadLatitudemosBadLatitude
	 */
	public String getMosBadLatitude() {
		return mosBadLatitude;
	}

	/**
	 * @param mosBadLatitude
	 *            the mosBadLatitude to set
	 */
	public void setMosBadLatitude(String mosBadLatitude) {
		this.mosBadLatitude = mosBadLatitude;
	}

	/**
	 * @return the mosBadLongitudemosBadLongitude
	 */
	public String getMosBadLongitude() {
		return mosBadLongitude;
	}

	/**
	 * @param mosBadLongitude
	 *            the mosBadLongitude to set
	 */
	public void setMosBadLongitude(String mosBadLongitude) {
		this.mosBadLongitude = mosBadLongitude;
	}

	/**
	 * @return the cellIdcellId
	 */
	public Long getCellId() {
		return cellId;
	}

	/**
	 * @param cellId
	 *            the cellId to set
	 */
	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}

	/**
	 * @return the failIdfailId
	 */
	public Long getFailId() {
		return failId;
	}

	/**
	 * @param failId
	 *            the failId to set
	 */
	public void setFailId(Long failId) {
		this.failId = failId;
	}

	/**
	 * @return the videoQualityBadId
	 */
	public Long getVideoQualityBadId() {
		return videoQualityBadId;
	}

	/**
	 * @param the
	 *            videoQualityBadId to set
	 */

	public void setVideoQualityBadId(Long videoQualityBadId) {
		this.videoQualityBadId = videoQualityBadId;
	}

	public Integer getCoverType() {
		return coverType;
	}

	public void setCoverType(Integer coverType) {
		this.coverType = coverType;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public String getColorMapType() {
		return colorMapType;
	}

	public void setColorMapType(String colorMapType) {
		this.colorMapType = colorMapType;
	}

	
}
