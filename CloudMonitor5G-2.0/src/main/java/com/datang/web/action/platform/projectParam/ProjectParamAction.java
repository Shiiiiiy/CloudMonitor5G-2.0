/**
 * 
 */
package com.datang.web.action.platform.projectParam;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.action.poi.PoiExcelAction;
import com.datang.common.util.ClassUtil;
import com.datang.common.util.POIExcelUtil;
import com.datang.common.util.StringUtils;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Cell5G;
import com.datang.domain.platform.projectParam.Cell5GNbCell;
import com.datang.domain.platform.projectParam.Cell5GtdlNbCell;
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.platform.projectParam.GsmCell;
import com.datang.domain.platform.projectParam.Lte5GCell;
import com.datang.domain.platform.projectParam.LteCell;
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.platform.projectParam.TdlGsmNbCell;
import com.datang.domain.platform.projectParam.TdlNbCell;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.exception.ApplicationException;
import com.datang.service.platform.projectParam.IProjectParamService;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.platform.projectParam.ProjectParamResponsePageBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * ????????????ACTION
 * 
 * @author yinzhipeng
 * @date:2015???10???16??? ??????10:35:51
 * @version
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class ProjectParamAction extends PoiExcelAction {
	/**
	 * ??????????????????
	 */
	@Autowired
	private IMenuManageService menuManageService;
	/**
	 * ???????????????
	 */
	@Autowired
	private TerminalGroupService groupService;
	/**
	 * ??????????????????
	 */
	@Autowired
	private IProjectParamService projectParamService;

	/**
	 * ????????????(ChinaMobile,ChinaUnicom,ChinaTelecom)
	 */
	private String infoType;
	/**
	 * ????????????
	 */
	private File importFile;
	
	private String importFileFileName;
	
	private String importFileContentType;
	
	/**
	 * ??????????????????
	 */
	private String operatorType;
	/**
	 * ?????????ID,terminalgroupid
	 */
	private Long cityId;

	/**
	 * ????????????????????????
	 */
	private Date beginImportDate;// ??????????????????
	private Date endImportDate;// ??????????????????
	private String userName;// ??????????????????
	private String cityIds;// ??????????????????
	private String citNames;// ????????????????????????
	
	private String idsStr; //????????????id

	/**
	 * ???????????????????????????
	 * 
	 * @return
	 */
	public String projectParamListUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		valueStack.set("ChinaMobile", ProjectParamInfoType.CH_MO);
		valueStack.set("ChinaUnicom", ProjectParamInfoType.CH_UN);
		valueStack.set("ChinaTelecom", ProjectParamInfoType.CH_TE);
		return ReturnType.LISTUI;
	}

	/**
	 * ???????????????????????????tab??????
	 * 
	 * @return
	 */
	public String projectParamInfoUI() {
		ActionContext.getContext().getValueStack().set("infoType", infoType);
		ActionContext.getContext().getValueStack()
				.set("FG", ProjectParamInfoType.FG);
		ActionContext.getContext().getValueStack()
				.set("LTE", ProjectParamInfoType.LTE);
		ActionContext.getContext().getValueStack()
				.set("GSM", ProjectParamInfoType.GSM);
		ActionContext.getContext().getValueStack()
				.set("FG_FG", ProjectParamInfoType.FG_FG);
		ActionContext.getContext().getValueStack()
				.set("FG_LTE", ProjectParamInfoType.FG_LTE);
		ActionContext.getContext().getValueStack()
				.set("TDL_NB", ProjectParamInfoType.TDL_NB);
		ActionContext.getContext().getValueStack()
				.set("TDL_GSM_NB", ProjectParamInfoType.TDL_GSM_NB);
		ActionContext.getContext().getValueStack()
				.set("LTE_5G", ProjectParamInfoType.LTE_5G);
		return "info";
	}
	
	/**
	 * ???????????????????????????tab??????
	 * 
	 * @return
	 */
	public String PlanParamManageUI() {
		return "planParamManageUI";
	}

	/**
	 * ????????????????????????????????????
	 * 
	 * @return
	 */
	public String importProjectParamUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		valueStack.set("ChinaMobile", ProjectParamInfoType.CH_MO);
		valueStack.set("ChinaUnicom", ProjectParamInfoType.CH_UN);
		valueStack.set("ChinaTelecom", ProjectParamInfoType.CH_TE);
		valueStack.set("GSM", ProjectParamInfoType.GSM);
		valueStack.set("LTE", ProjectParamInfoType.LTE);
		valueStack.set("TDL_NB", ProjectParamInfoType.TDL_NB);
		valueStack.set("TDL_GSM_NB", ProjectParamInfoType.TDL_GSM_NB);
		valueStack.set("FG", ProjectParamInfoType.FG);
		valueStack.set("FG_FG", ProjectParamInfoType.FG_FG);
		valueStack.set("FG_LTE", ProjectParamInfoType.FG_LTE);
		valueStack.set("LTE_5G", ProjectParamInfoType.LTE_5G);

		return "import";
	}
	
	/**
	 * ?????????????????????????????????????????????
	 * 
	 * @return
	 */
	public String importPlanManageParamUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		valueStack.set("MODEL_5G", "MODEL_5G_PARAM");
		valueStack.set("MODEL_4G", "MODEL_4G_PARAM");
		return "importPlanManageParam";
	}
	
	/**
	 * ???????????????
	 */
	public String doPageList() {

		Set<ProjectParamResponsePageBean> set = new TreeSet<>();
		// ???????????????????????????????????????menu
		List<TerminalMenu> cities = menuManageService.getCities();
		// ???????????????????????????
		if (StringUtils.hasText(cityIds)) {
			List<Long> includeCityIds = new ArrayList<>();
			String[] split = cityIds.split(",");
			for (String string : split) {
				try {
					includeCityIds.add(Long.parseLong(string));
				} catch (NumberFormatException e) {
					continue;
				}
			}
			for (Iterator iterator = cities.iterator(); iterator.hasNext();) {
				TerminalMenu terminalMenu = (TerminalMenu) iterator.next();
				if (!includeCityIds.contains(terminalMenu.getId())) {
					iterator.remove();
				}
			}
		}
		// ????????????menu?????????terminalGroup
		List<TerminalGroup> groupsByMenus = groupService
				.getGroupsByMenus(cities);
		for (TerminalGroup terminalGroup : groupsByMenus) {
			CellInfo cellInfo = terminalGroup.getCellInfo(infoType.trim());
			if (null == cellInfo) {
				cellInfo = new CellInfo();
			}
			// ???????????????????????????
			if (null != beginImportDate) {
				Date importDate = cellInfo.getImportDate();
				if (null == importDate) {
					continue;
				}
				if ((importDate.getTime() - beginImportDate.getTime()) < 0) {
					continue;
				}
			}
			if (null != endImportDate) {
				Date importDate = cellInfo.getImportDate();
				if (null == importDate) {
					continue;
				}
				if ((importDate.getTime() - endImportDate.getTime()) > 0) {
					continue;
				}
			}
			// ??????????????????????????????
			if (StringUtils.hasText(userName)) {
				String cellInfoUserName = cellInfo.getUserName();
				if (!StringUtils.hasText(cellInfoUserName)) {
					continue;
				}
				if (!cellInfoUserName.trim().toUpperCase()
						.equals(userName.trim().toUpperCase())) {
					continue;
				}
			}

			// ?????????????????????terminalGroup??????????????????
			String provinceName = groupService
					.getProvinceNameByCityGroup(terminalGroup);
			ProjectParamResponsePageBean paramResponsePageBean = new ProjectParamResponsePageBean(
					terminalGroup.getId(), provinceName,
					terminalGroup.getName());

			paramResponsePageBean.setIs5gImport(cellInfo.getCells5gImport());
			paramResponsePageBean.setLteImport(cellInfo.getLteCellsImport());
			paramResponsePageBean.setGsmImport(cellInfo.getGsmCellsImport());
			paramResponsePageBean
					.setIs5g5gImport(cellInfo.getCells5gNbImport());
			paramResponsePageBean.setIs5gLteImport(cellInfo
					.getCells5gTdlNbImport());
			paramResponsePageBean.setIsLte5GImport(cellInfo
					.getLte5GCellsImport());
			paramResponsePageBean.setTdlImport(cellInfo.getTdlNbCellsImport());
			paramResponsePageBean.setTdlGsmImport(cellInfo
					.getTdlGsmNbCellsImport());

			paramResponsePageBean.setUserName(cellInfo.getUserName());
			paramResponsePageBean.setImportDate(cellInfo.getImportDate());
			set.add(paramResponsePageBean);
		}
		ActionContext.getContext().getValueStack().push(set);
		return ReturnType.JSON;
	}

	/**
	 * ???????????????
	 * 
	 * @return
	 */
	public String importCell() {
		try {
			ValueStack valueStack = ActionContext.getContext().getValueStack();
			if (ProjectParamInfoType.FG.equals(operatorType)) {
				int[] importCell = projectParamService.import5GCell(cityId,
						infoType, importFile);
				valueStack.set("totalRowNum", importCell[0]);
				valueStack.set("failRowNum", importCell[1]);
			} else if (ProjectParamInfoType.FG_FG.equals(operatorType)
					|| ProjectParamInfoType.FG_LTE.equals(operatorType)) {
				int[] importCell = projectParamService.import5GNbCell(cityId,
						infoType, operatorType, importFile);
				valueStack.set("totalRowNum", importCell[0]);
				valueStack.set("failRowNum", importCell[1]);
			} else if (operatorType.contains("NB")) {
				int[] importCell = projectParamService.importNbCell(cityId,
						infoType, operatorType, importFile);
				valueStack.set("totalRowNum", importCell[0]);
				valueStack.set("failRowNum", importCell[1]);
			} else if (operatorType.contains("LTE_5G")) {
				int[] importCell = projectParamService.importLte5GCell(cityId,infoType, operatorType, importFile);
				valueStack.set("totalRowNum", importCell[0]);
				valueStack.set("failRowNum", importCell[1]);
			} else {
				int[] importCell = projectParamService.importCell(cityId,
						infoType, operatorType, importFile);
				valueStack.set("totalRowNum", importCell[0]);
				valueStack.set("failRowNum", importCell[1]);
			}
			String operatorType1 = "";
			String infoType1 = "";
			switch (infoType) {
			case ProjectParamInfoType.CH_MO:
				operatorType1 = "????????????";
				break;
			case ProjectParamInfoType.CH_UN:
				operatorType1 = "????????????";
				break;
			case ProjectParamInfoType.CH_TE:
				operatorType1 = "????????????";
				break;
			default:
				break;
			}
			switch (operatorType) {
			case ProjectParamInfoType.GSM:
				infoType1 = "GSM";
				break;
			case ProjectParamInfoType.LTE:
				infoType1 = "TD-LTE";
				break;
			case ProjectParamInfoType.TDL_NB:
				infoType1 = "TDL??????";
				break;
			case ProjectParamInfoType.TDL_GSM_NB:
				infoType1 = "TDL-GSM??????";
				break;
			case ProjectParamInfoType.FG:
				infoType1 = "5G";
				break;
			case ProjectParamInfoType.FG_FG:
				infoType1 = "5G-5G?????????";
				break;
			case ProjectParamInfoType.FG_LTE:
				infoType1 = "5G-LTE?????????";
				break;
			default:
				break;
			}
			valueStack.set("infoType", infoType1);
			valueStack.set("operatorType", operatorType1);
		} catch (ApplicationException appEx) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", appEx.getMessage());
		}
		return ReturnType.JSON;
	}
	
	/**
	 * ?????????????????????
	 * 
	 * @return
	 */
	public String importPlanManageParam() {
		try {
			ValueStack valueStack = ActionContext.getContext().getValueStack();
			if ("5G".equals(operatorType)) {
				String[] importCell = projectParamService.import5GPlanManageParam(cityId, operatorType,importFile,importFileFileName);
				valueStack.set("totalRowNum", importCell[0]);
				valueStack.set("failRowNum", importCell[1]);
				valueStack.set("msg", importCell[2]);
			} else if ("4G".equals(operatorType)) {
				String[] importCell = projectParamService.import4GPlanManageParam(cityId,operatorType,importFile,importFileFileName);
				valueStack.set("totalRowNum", importCell[0]);
				valueStack.set("failRowNum", importCell[1]);
				valueStack.set("msg", importCell[2]);
			}
			valueStack.set("operatorType", operatorType);
		} catch (ApplicationException appEx) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", appEx.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * ????????????
	 * 
	 * @return
	 */
	public String downloadTemp() {
		return "downloadTemp";
	}

	public InputStream getDownloadTemp() throws IOException {
		InputStream is = null;
		if (ProjectParamInfoType.GSM.equals(operatorType)) {
			ActionContext.getContext().put("fileName",
					new String("GSM???????????????.xls".getBytes(), "ISO8859-1"));
			is = ClassUtil.getResourceAsStream("templates/GSM.xls");
		} else if (ProjectParamInfoType.LTE.equals(operatorType)) {
			ActionContext.getContext().put("fileName",
					new String("LTE???????????????.xls".getBytes(), "ISO8859-1"));
			is = ClassUtil.getResourceAsStream("templates/LTE.xls");
		} else if (ProjectParamInfoType.TDL_NB.equals(operatorType)) {
			ActionContext.getContext().put("fileName",
					new String("TDL???????????????.xls".getBytes(), "ISO8859-1"));
			is = ClassUtil.getResourceAsStream("templates/TDL.xls");
		} else if (ProjectParamInfoType.TDL_GSM_NB.equals(operatorType)) {
			ActionContext.getContext().put("fileName",
					new String("TDL-GSM???????????????.xls".getBytes(), "ISO8859-1"));
			is = ClassUtil.getResourceAsStream("templates/TDL-GSM.xls");
		} else if (ProjectParamInfoType.FG.equals(operatorType)) {
			ActionContext.getContext().put("fileName",
					new String("5G???????????????.xls".getBytes(), "ISO8859-1"));
			is = ClassUtil.getResourceAsStream("templates/5G.xls");
		} else if (ProjectParamInfoType.FG_FG.equals(operatorType)) {
			ActionContext.getContext().put("fileName",
					new String("5G-5G???????????????.xls".getBytes(), "ISO8859-1"));
			is = ClassUtil.getResourceAsStream("templates/5G-5G.xls");
		} else if (ProjectParamInfoType.FG_LTE.equals(operatorType)) {
			ActionContext.getContext().put("fileName",
					new String("5G-LTE???????????????.xls".getBytes(), "ISO8859-1"));
			is = ClassUtil.getResourceAsStream("templates/5G-LTE.xls");
		}else if (ProjectParamInfoType.LTE_5G.equals(operatorType)) {
			ActionContext.getContext().put("fileName",new String("LTE-5G??????????????????.xls".getBytes(), "ISO8859-1"));
			is = ClassUtil.getResourceAsStream("templates/LTE-5G.xls");
		}else if ("MODEL_5G_PARAM".equals(operatorType)) {
			ActionContext.getContext().put("fileName",new String("5G??????????????????.xlsx".getBytes(), "ISO8859-1"));
			is = ClassUtil.getResourceAsStream("templates/5G??????????????????.xlsx");
		}else if ("MODEL_4G_PARAM".equals(operatorType)) {
			ActionContext.getContext().put("fileName",new String("4G??????????????????.xlsx".getBytes(), "ISO8859-1"));
			is = ClassUtil.getResourceAsStream("templates/4G??????????????????.xlsx");
		}
		
		return is;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.action.page.PageAction#doPageQuery(com.datang.common
	 * .action.page.PageList)
	 */
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		AbstractPageList list = null;
		pageList.putParam("idsStr", idsStr);
		if(idsStr == null && operatorType==null){
			//??????????????????????????????????????????????????????
			if(citNames == null || !StringUtils.hasText(citNames)){
				citNames="";
				// ???????????????????????????????????????menu
				List<TerminalMenu> cities = menuManageService.getCities();
				// ????????????menu?????????terminalGroup
				List<TerminalGroup> groupsByMenus = groupService
						.getGroupsByMenus(cities);
				for(int i=0;i<groupsByMenus.size();i++){
					citNames = citNames+groupsByMenus.get(i).getName();
					if(i!=groupsByMenus.size()-1){
						citNames = citNames+",";
					}
				}
			}
			pageList.putParam("beginImportDate", beginImportDate);
			pageList.putParam("endImportDate", endImportDate);
			pageList.putParam("citNames", citNames);
			list = projectParamService.doPageQuery(pageList);
			List<CellInfo> cellinfos = list.getRows();
			for (CellInfo cellInfo : cellinfos) {
				cellInfo.setPlan4GParams(null);
				cellInfo.setPlanParams(null);
				cellInfo.setGroup(null);
			}
		}else if(idsStr != null && operatorType.equals("5G")){
			list = projectParamService.doPageQueryParam5G(pageList);
			List<PlanParamPojo> planParamPojolist = list.getRows();
			for (PlanParamPojo planParamPojo : planParamPojolist) {
				planParamPojo.setCellInfo(null);;
			}
		}else if(idsStr != null && operatorType.equals("4G")){
			list = projectParamService.doPageQueryParam4G(pageList);
			List<Plan4GParam> plan4GParamlist = list.getRows();
			for (Plan4GParam plan4GParam : plan4GParamlist) {
				plan4GParam.setCellInfo(null);;
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.action.poi.PoiExcelAction#doPageExcel(com.datang.common
	 * .action.page.PageList)
	 */
	@Override
	public Workbook doPageExcel(PageList pageList) throws IOException {
		Workbook transformToExcel = null;
		if(operatorType.equals("planParamExpotExcel")){
			long currentTimeMillis = System.currentTimeMillis();
			String[] ids = idsStr.split(",");
			for (String id : ids) {
				CellInfo cellInfo = projectParamService.findById(Long.parseLong(id));
				if(cellInfo.getOperatorType().equals("5G")){
					Set<PlanParamPojo> planParamPojo = cellInfo.getPlanParams();
					Map<String, Collection> hashMap = new HashMap<>();
					long currentTimeMillis2 = System.currentTimeMillis();
					System.out.println("????????????:"
							+ (currentTimeMillis2 - currentTimeMillis));
					hashMap.put("plan5GParams", planParamPojo);
					if (planParamPojo.size() >= 65536) {
						// ??????excel2007
						transformToExcel = POIExcelUtil.transformToExcel(
								hashMap, "templates/5G_plan_param_temp.xlsx");
						ActionContext
								.getContext()
								.put("fileName",
										new String(
												(cellInfo.getPlanSheetName() + ".xlsx")
														.getBytes(),
												"ISO8859-1"));
					} else {
						// ??????excel2003
						transformToExcel = POIExcelUtil.transformToExcel(
								hashMap, "templates/5G_plan_param_temp.xls");
						ActionContext
								.getContext()
								.put("fileName",
										new String(
												(cellInfo.getPlanSheetName() + ".xls")
														.getBytes(),
												"ISO8859-1"));
					}

					long currentTimeMillis3 = System.currentTimeMillis();
					System.out.println("????????????:"
							+ (currentTimeMillis3 - currentTimeMillis2));
					
				}else if(cellInfo.getOperatorType().equals("4G")){
					Set<Plan4GParam> plan4GParams = cellInfo.getPlan4GParams();
					Map<String, Collection> hashMap = new HashMap<>();
					long currentTimeMillis2 = System.currentTimeMillis();
					System.out.println("????????????:"
							+ (currentTimeMillis2 - currentTimeMillis));
					hashMap.put("plan4GParams", plan4GParams);
					if (plan4GParams.size() >= 65536) {
						// ??????excel2007
						transformToExcel = POIExcelUtil.transformToExcel(
								hashMap, "templates/4G_plan_param_temp.xlsx");
						ActionContext
								.getContext()
								.put("fileName",
										new String(
												(cellInfo.getPlanSheetName() + ".xlsx")
														.getBytes(),
												"ISO8859-1"));
					} else {
						// ??????excel2003
						transformToExcel = POIExcelUtil.transformToExcel(
								hashMap, "templates/4G_plan_param_temp.xls");
						ActionContext
								.getContext()
								.put("fileName",
										new String(
												(cellInfo.getPlanSheetName() + ".xls")
														.getBytes(),
												"ISO8859-1"));
					}

					long currentTimeMillis3 = System.currentTimeMillis();
					System.out.println("????????????:"
							+ (currentTimeMillis3 - currentTimeMillis2));
				}
			}
		}else {
			TerminalGroup findGroupById = groupService.findGroupById(cityId);
			if (null != findGroupById) {
				long currentTimeMillis = System.currentTimeMillis();
				CellInfo cellInfo = findGroupById.getCellInfo(infoType.trim());
				if (null != cellInfo) {
					if (ProjectParamInfoType.GSM.equals(operatorType)) {
						Set<GsmCell> gsmCells = cellInfo.getGsmCells();
						Map<String, Collection> hashMap = new HashMap<>();
						long currentTimeMillis2 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis2 - currentTimeMillis));
						hashMap.put("gsmCell", gsmCells);
						if (gsmCells.size() >= 65536) {
							// ??????excel2007
							transformToExcel = POIExcelUtil.transformToExcel(
									hashMap, "templates/GSM_TEMP.xlsx");
							ActionContext
									.getContext()
									.put("fileName",
											new String(
													(findGroupById.getName() + "_GSM?????????.xlsx")
															.getBytes(),
													"ISO8859-1"));
						} else {
							// ??????excel2003
							transformToExcel = POIExcelUtil.transformToExcel(
									hashMap, "templates/GSM_TEMP.xls");
							ActionContext
									.getContext()
									.put("fileName",
											new String(
													(findGroupById.getName() + "_GSM?????????.xls")
															.getBytes(),
													"ISO8859-1"));
						}

						long currentTimeMillis3 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis3 - currentTimeMillis2));

					} else if (ProjectParamInfoType.LTE.equals(operatorType)) {
						Set<LteCell> lteCells = cellInfo.getLteCells();
						Map<String, Collection> hashMap = new HashMap<>();
						hashMap.put("lteCell", lteCells);
						long currentTimeMillis2 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis2 - currentTimeMillis));
						if (lteCells.size() >= 65536) {
							// ??????excel2007
							transformToExcel = POIExcelUtil.transformToExcel(
									hashMap, "templates/LTE_TEMP.xlsx");
							ActionContext
									.getContext()
									.put("fileName",
											new String(
													(findGroupById.getName() + "_LTE?????????.xlsx")
															.getBytes(),
													"ISO8859-1"));
						} else {
							// ??????excel2003
							transformToExcel = POIExcelUtil.transformToExcel(
									hashMap, "templates/LTE_TEMP.xls");
							ActionContext
									.getContext()
									.put("fileName",
											new String(
													(findGroupById.getName() + "_LTE?????????.xls")
															.getBytes(),
													"ISO8859-1"));
						}
						long currentTimeMillis3 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis3 - currentTimeMillis2));

					} else if (ProjectParamInfoType.FG.equals(operatorType)) {
						Set<Cell5G> cells5g = cellInfo.getCells5g();
						Map<String, Collection> hashMap = new HashMap<>();
						hashMap.put("cell5g", cells5g);
						long currentTimeMillis2 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis2 - currentTimeMillis));
						if (cells5g.size() >= 65536) {
							// ??????excel2007
							transformToExcel = POIExcelUtil.transformToExcel(
									hashMap, "templates/5G_TEMP.xlsx");
							ActionContext
									.getContext()
									.put("fileName",
											new String(
													(findGroupById.getName() + "_5G?????????.xlsx")
															.getBytes(),
													"ISO8859-1"));
						} else {
							// ??????excel2003
							transformToExcel = POIExcelUtil.transformToExcel(
									hashMap, "templates/5G_TEMP.xls");
							ActionContext
									.getContext()
									.put("fileName",
											new String(
													(findGroupById.getName() + "_5G?????????.xls")
															.getBytes(),
													"ISO8859-1"));
						}
						long currentTimeMillis3 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis3 - currentTimeMillis2));

					} else if (ProjectParamInfoType.FG_FG.equals(operatorType)) {
						Set<Cell5GNbCell> cells5gNb = cellInfo.getCells5gNb();
						Map<String, Collection> hashMap = new HashMap<>();
						hashMap.put("cell5gNb", cells5gNb);
						long currentTimeMillis2 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis2 - currentTimeMillis));
						if (cells5gNb.size() >= 65536) {
							// ??????excel2007
							transformToExcel = POIExcelUtil.transformToExcel(
									hashMap, "templates/5G-5G_TEMP.xlsx");
							ActionContext
									.getContext()
									.put("fileName",
											new String(
													(findGroupById.getName() + "_5G-5G?????????.xlsx")
															.getBytes(),
													"ISO8859-1"));
						} else {
							// ??????excel2003
							transformToExcel = POIExcelUtil.transformToExcel(
									hashMap, "templates/5G-5G_TEMP.xlsx");
							ActionContext
									.getContext()
									.put("fileName",
											new String(
													(findGroupById.getName() + "_5G-5G?????????.xlsx")
															.getBytes(),
													"ISO8859-1"));
						}
						long currentTimeMillis3 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis3 - currentTimeMillis2));

					} else if (ProjectParamInfoType.FG_LTE.equals(operatorType)) {
						Set<Cell5GtdlNbCell> cells5gTdlNb = cellInfo
								.getCells5gTdlNb();
						Map<String, Collection> hashMap = new HashMap<>();
						hashMap.put("cell5gTdlNb", cells5gTdlNb);
						long currentTimeMillis2 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis2 - currentTimeMillis));
						if (cells5gTdlNb.size() >= 65536) {
							// ??????excel2007
							transformToExcel = POIExcelUtil.transformToExcel(
									hashMap, "templates/5G-LTE_TEMP.xlsx");
							ActionContext
									.getContext()
									.put("fileName",
											new String(
													(findGroupById.getName() + "_5G-LTE?????????.xlsx")
															.getBytes(),
													"ISO8859-1"));
						} else {
							// ??????excel2003
							transformToExcel = POIExcelUtil.transformToExcel(
									hashMap, "templates/5G-LTE_TEMP.xlsx");
							ActionContext
									.getContext()
									.put("fileName",
											new String(
													(findGroupById.getName() + "_5G-LTE?????????.xlsx")
															.getBytes(),
													"ISO8859-1"));
						}
						long currentTimeMillis3 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis3 - currentTimeMillis2));

					} else if (ProjectParamInfoType.TDL_NB.equals(operatorType)) {
						Set<TdlNbCell> tdlNbCells = cellInfo.getTdlNbCells();
						Map<String, Collection> hashMap = new HashMap<>();
						hashMap.put("tdlNbCell", tdlNbCells);
						long currentTimeMillis2 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis2 - currentTimeMillis));
						if (tdlNbCells.size() >= 65536) {
							// ??????excel2007
							transformToExcel = POIExcelUtil.transformToExcel(
									hashMap, "templates/TDL_TEMP.xlsx");
							ActionContext
									.getContext()
									.put("fileName",
											new String(
													(findGroupById.getName() + "_TDL?????????.xlsx")
															.getBytes(),
													"ISO8859-1"));
						} else {
							// ??????excel2003
							transformToExcel = POIExcelUtil.transformToExcel(
									hashMap, "templates/TDL_TEMP.xls");
							ActionContext
									.getContext()
									.put("fileName",
											new String(
													(findGroupById.getName() + "_TDL?????????.xls")
															.getBytes(),
													"ISO8859-1"));
						}
						long currentTimeMillis3 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis3 - currentTimeMillis2));

					} else if (ProjectParamInfoType.TDL_GSM_NB.equals(operatorType)) {
						Set<TdlGsmNbCell> tdlGsmNbCells = cellInfo
								.getTdlGsmNbCells();
						Map<String, Collection> hashMap = new HashMap<>();
						hashMap.put("tdlGsmNbCell", tdlGsmNbCells);
						long currentTimeMillis2 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis2 - currentTimeMillis));
						if (tdlGsmNbCells.size() >= 65536) {
							// ??????excel2007
							transformToExcel = POIExcelUtil.transformToExcel(
									hashMap, "templates/TDL-GSM_TEMP.xlsx");
							ActionContext
									.getContext()
									.put("fileName",
											new String(
													(findGroupById.getName() + "_TDL-GSM?????????.xlsx")
															.getBytes(),
													"ISO8859-1"));
						} else {
							// ??????excel2003
							transformToExcel = POIExcelUtil.transformToExcel(
									hashMap, "templates/TDL-GSM_TEMP.xls");
							ActionContext
									.getContext()
									.put("fileName",
											new String(
													(findGroupById.getName() + "_TDL-GSM?????????.xls")
															.getBytes(),
													"ISO8859-1"));
						}
						long currentTimeMillis3 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis3 - currentTimeMillis2));

					} else if (ProjectParamInfoType.LTE_5G.equals(operatorType)) {
						Set<Lte5GCell> lte5gCells = cellInfo.getLte5GCells();
						Map<String, Collection> hashMap = new HashMap<>();
						hashMap.put("lte5gCells", lte5gCells);
						long currentTimeMillis2 = System.currentTimeMillis();
						System.out.println("????????????:"+ (currentTimeMillis2 - currentTimeMillis));
						if (lte5gCells.size() >= 65536) {
							// ??????excel2007
							transformToExcel = POIExcelUtil.transformToExcel(hashMap, "templates/LTE-5G_TEMP.xlsx");
							ActionContext.getContext()
									.put("fileName",new String((findGroupById.getName() + "_LTE-5G???????????????.xlsx").getBytes(),"ISO8859-1"));
						} else {
							// ??????excel2003
							transformToExcel = POIExcelUtil.transformToExcel(hashMap, "templates/LTE-5G_TEMP.xls");
							ActionContext.getContext()
									.put("fileName",new String((findGroupById.getName() + "_LTE-5G???????????????.xls").getBytes(),"ISO8859-1"));
						}
						long currentTimeMillis3 = System.currentTimeMillis();
						System.out.println("????????????:"
								+ (currentTimeMillis3 - currentTimeMillis2));

					}
				}
			}
		}
		return transformToExcel;
	}
	
	public String delectParams(){
		try {
			if(operatorType.equals("planParamDeleteCell")){
				long currentTimeMillis = System.currentTimeMillis();
				String[] ids = idsStr.split(",");
				List<String> idsList = Arrays.asList(ids);
				projectParamService.deleteCellById(idsList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", e.getMessage());
		}
		ActionContext.getContext().getValueStack().set("success", "??????????????????");
		return ReturnType.JSON;
	}
	
	

	/**
	 * @return the infoTypeinfoType
	 */
	public String getInfoType() {
		return infoType;
	}

	/**
	 * @param infoType
	 *            the infoType to set
	 */
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	/**
	 * @return the importFileimportFile
	 */
	public File getImportFile() {
		return importFile;
	}

	/**
	 * @param importFile
	 *            the importFile to set
	 */
	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}

	/**
	 * @return the importFileFileName
	 */
	public String getImportFileFileName() {
		return importFileFileName;
	}

	/**
	 * @param importFileFileName the importFileFileName to set
	 */
	public void setImportFileFileName(String importFileFileName) {
		this.importFileFileName = importFileFileName;
	}

	/**
	 * @return the importFileContentType
	 */
	public String getImportFileContentType() {
		return importFileContentType;
	}

	/**
	 * @param importFileContentType the importFileContentType to set
	 */
	public void setImportFileContentType(String importFileContentType) {
		this.importFileContentType = importFileContentType;
	}

	/**
	 * @return the cityIdcityId
	 */
	public Long getCityId() {
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the operatorTypeoperatorType
	 */
	public String getOperatorType() {
		return operatorType;
	}

	/**
	 * @param operatorType
	 *            the operatorType to set
	 */
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}


	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param cityIds
	 *            the cityIds to set
	 */
	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}

	/**
	 * @return the citNames
	 */
	public String getCitNames() {
		return citNames;
	}

	/**
	 * @param citNames the citNames to set
	 */
	public void setCitNames(String citNames) {
		this.citNames = citNames;
	}

	/**
	 * @return the idsStr
	 */
	public String getIdsStr() {
		return idsStr;
	}

	/**
	 * @param idsStr the idsStr to set
	 */
	public void setIdsStr(String idsStr) {
		this.idsStr = idsStr;
	}

	/**
	 * @return the beginImportDate
	 */
	public Date getBeginImportDate() {
		return beginImportDate;
	}

	/**
	 * @param beginImportDate the beginImportDate to set
	 */
	public void setBeginImportDate(Date beginImportDate) {
		this.beginImportDate = beginImportDate;
	}

	/**
	 * @return the endImportDate
	 */
	public Date getEndImportDate() {
		return endImportDate;
	}

	/**
	 * @param endImportDate the endImportDate to set
	 */
	public void setEndImportDate(Date endImportDate) {
		this.endImportDate = endImportDate;
	}


}
