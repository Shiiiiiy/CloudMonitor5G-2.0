/**
 * 
 */
package com.datang.service.platform.projectParam.impl;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.util.ObjectUtils;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.ReflectUtil;
import com.datang.common.util.StringUtils;
import com.datang.dao.oppositeOpen.OppositeOpen3dCompletionShowDao;
import com.datang.dao.platform.projectParam.CellInfoDao;
import com.datang.dao.platform.projectParam.GsmCellDao;
import com.datang.dao.platform.projectParam.LteCellDao;
import com.datang.dao.platform.projectParam.StationDistanceLTEDao;
import com.datang.dao.platform.projectParam.StationDistanceNRDao;
import com.datang.dao.testManage.terminal.TerminalGroupDao;
import com.datang.domain.embbCover.StationDistanceLTEPojo;
import com.datang.domain.embbCover.StationDistanceNRPojo;
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
import com.datang.domain.platform.stationParam.StationBasicParamPojo;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.exception.ApplicationException;
import com.datang.service.platform.projectParam.IProjectParamService;
import com.datang.tools.ComputeStationDistance;
import com.datang.tools.DrawGisLteCell;
import com.datang.util.GPSUtils;
import com.datang.web.action.platform.projectParam.ProjectParamInfoType;

/**
 * 工程参数Service实现
 * 
 * @author yinzhipeng
 * @date:2015年10月19日 下午4:40:05
 * @version
 */
@Service
@Transactional
public class ProjectParamServiceImpl implements IProjectParamService {
	@Autowired
	private TerminalGroupDao terminalGroupDao;
	@Autowired
	private CellInfoDao cellInfoDao;
	@Autowired
	private LteCellDao lteCellDao;
	@Autowired
	private GsmCellDao gsmCellDao;
	@Autowired
	private ComputeStationDistance computeStationDistance;
	@Autowired
	private StationDistanceNRDao stationDistanceNRDao;
	@Autowired
	private StationDistanceLTEDao stationDistanceLTEDao;
	@Autowired
	private OppositeOpen3dCompletionShowDao oppositeOpen3dCompletionShowDao;
	@Value("${gisDb.url}")
	private String gisDb;
	@Value("${gis.drawCellUrl}")
	private String gisDrawCellUrl;
	@Value("${gis.drawCellPath}")
	private String gisDrawCellPath;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.prozjectParam.IProjectParamService#importCell
	 * (java.lang.Long, java.lang.String, java.lang.String, java.io.File)
	 */
	@Override
	public int[] importCell(Long cityId, String infoType, String operatorType,
			File xlsFile) {
		TerminalGroup terminalGroup = terminalGroupDao.find(cityId);
		if (null == terminalGroup) {
			throw new ApplicationException("分组已经不存在");
		}
		Workbook workbook = null;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(xlsFile));
		} catch (IOException e) {
			throw new ApplicationException("导入小区表失败");
		} catch (IllegalArgumentException e) {
			try {
				workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
			} catch (IOException e1) {
				throw new ApplicationException("导入小区表失败");
			} catch (IllegalArgumentException e1) {
				throw new ApplicationException("不是EXCEL文件");
			} catch (Exception e1) {
				throw new ApplicationException("导入小区表失败");
			}
		} finally {
			if (null != workbook) {
				try {
					workbook.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}
		Sheet sheetAt = workbook.getSheetAt(0);
		// 总的excel中记录数
		int totalRowNum = sheetAt.getLastRowNum() - sheetAt.getFirstRowNum();
		if (0 == totalRowNum) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}
		Row row = sheetAt.getRow(sheetAt.getFirstRowNum());
		if (null == row) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}
		int mcc = -1;
		int mnc = -1;
		int mmeGroupID = -1;
		int mmeId = -1;
		int eNBId = -1;
		int siteName = -1;
		int cellName = -1;
		int localCellId = -1;
		int cellId = -1;
		int tac = -1;
		int pci = -1;
		int frequency1 = -1;
		int frequency2 = -1;
		int frequency3 = -1;
		int frequency4 = -1;
		int frequency5 = -1;
		int frequency6 = -1;
		int frequency7 = -1;
		int frequency8 = -1;
		int frequency9 = -1;
		int frequency10 = -1;
		int frequency11 = -1;
		int bandwidth1 = -1;
		int bandwidth2 = -1;
		int freqCount = -1;
		int longitude = -1;
		int latitude = -1;
		int sectorType = -1;
		int doorType = -1;
		int tiltTotal = -1;
		int tiltM = -1;
		int tiltE = -1;
		int azimuth = -1;
		int beamWidth = -1;
		int vBeamWidth = -1;
		int aheight = -1;
		int region = -1;
		int vender = -1;
		int coverScene = -1;
		int isBelongtoNetwork = -1;

		int rac = -1;
		int lac = -1;
		int ci = -1;
		int bcch = -1;
		int bsic = -1;
		int ncc = -1;
		int bcc = -1;
		int dcs = -1;

		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if (null == cell) {
				continue;
			}
			String value = cell.getStringCellValue().trim().toLowerCase();
			if (!StringUtils.hasText(value)) {
				continue;
			}
			if ("mcc".equals(value)) {
				mcc = i;
			} else if ("mnc".equals(value)) {
				mnc = i;
			} else if ("mme group id".equals(value)) {
				mmeGroupID = i;
			} else if ("*mme id".equals(value)) {
				mmeId = i;
			} else if ("enb id".equals(value)) {
				eNBId = i;
			} else if ("sitename".equals(value)) {
				siteName = i;
			} else if ("*cellname".equals(value)) {
				cellName = i;
			} else if ("localcellid".equals(value)) {
				localCellId = i;
			} else if ("*cell id".equals(value)) {
				cellId = i;
			} else if ("*tac".equals(value)) {
				tac = i;
			} else if ("*pci".equals(value)) {
				pci = i;
			} else if ("*frequency1".equals(value)) {
				frequency1 = i;
			} else if ("frequency2".equals(value)) {
				frequency2 = i;
			} else if ("frequency3".equals(value)) {
				frequency3 = i;
			} else if ("frequency4".equals(value)) {
				frequency4 = i;
			} else if ("frequency5".equals(value)) {
				frequency5 = i;
			} else if ("frequency6".equals(value)) {
				frequency6 = i;
			} else if ("frequency7".equals(value)) {
				frequency7 = i;
			} else if ("frequency8".equals(value)) {
				frequency8 = i;
			} else if ("frequency9".equals(value)) {
				frequency9 = i;
			} else if ("frequency10".equals(value)) {
				frequency10 = i;
			} else if ("frequency11".equals(value)) {
				frequency11 = i;
			} else if ("bandwidth1".equals(value)) {
				bandwidth1 = i;
			} else if ("bandwidth2".equals(value)) {
				bandwidth2 = i;
			} else if ("freqcount".equals(value)) {
				freqCount = i;
			} else if ("*longitude".equals(value)) {
				longitude = i;
			} else if ("*latitude".equals(value)) {
				latitude = i;
			} else if ("sectortype".equals(value)
					|| "*sectortype".equals(value)) {
				sectorType = i;
			} else if ("doortype".equals(value) || "*doortype".equals(value)) {
				doorType = i;
			} else if ("*tilt total".equals(value)) {
				tiltTotal = i;
			} else if ("tilt m".equals(value)) {
				tiltM = i;
			} else if ("tilt e".equals(value)) {
				tiltE = i;
			} else if ("*azimuth".equals(value)) {
				azimuth = i;
			} else if ("*beamwidth".equals(value)) {
				beamWidth = i;
			} else if ("vbeamwidth".equals(value)) {
				vBeamWidth = i;
			} else if ("aheight".equals(value)) {
				aheight = i;
			} else if ("*region".equals(value)) {
				region = i;
			} else if ("rac".equals(value)) {
				rac = i;
			} else if ("*lac".equals(value)) {
				lac = i;
			} else if ("*ci".equals(value)) {
				ci = i;
			} else if ("*bcch".equals(value)) {
				bcch = i;
			} else if ("*bsic".equals(value)) {
				bsic = i;
			} else if ("ncc".equals(value)) {
				ncc = i;
			} else if ("bcc".equals(value)) {
				bcc = i;
			} else if ("dcs".equals(value)) {
				dcs = i;
			} else if ("厂家".equals(value)) {
				vender = i;
			} else if ("覆盖场景".equals(value)) {
				coverScene = i;
			} else if ("是否属于网格".equals(value)) {
				isBelongtoNetwork = i;
			}
		}
		// 失败的记录数
		int failRowNum = 0;
		// 存储lte小区
		List<LteCell> lteCells = new ArrayList<>();
		// 存储gsm小区
		List<GsmCell> gsmcCells = new ArrayList<>();
		//存储cellName，保证唯一性
		List<String> cellNameList = new ArrayList<>();
		
		for (int i = sheetAt.getFirstRowNum() + 1; i <= sheetAt.getLastRowNum(); i++) {
			Row rowContext = sheetAt.getRow(i);

			// 防止出现中间空白行和防止代码getLastRowNum()的不准确
			short firstCellNum = rowContext.getFirstCellNum();
			short lastCellNum = rowContext.getLastCellNum();
			if (0 == lastCellNum - firstCellNum) {
				totalRowNum = totalRowNum - 1;
				continue;
			}

			LteCell lteCell = new LteCell();
			GsmCell gsmCell = new GsmCell();
			try {
				if (vender != -1) {
					String value = getCellValue(rowContext.getCell(vender));
					if (StringUtils.hasText(value)) {
						lteCell.setVender(value);
					}
				}
				if (coverScene != -1) {
					String value = getCellValue(rowContext.getCell(coverScene));
					if (StringUtils.hasText(value)) {
						lteCell.setCoverScene(value);
					}
				}
				if (isBelongtoNetwork != -1) {
					String value = getCellValue(rowContext
							.getCell(isBelongtoNetwork));
					if (StringUtils.hasText(value)) {
						lteCell.setIsBelongtoNetwork(value);
					}
				}
				if (mcc != -1) {
					String value = getCellValue(rowContext.getCell(mcc));
					if (StringUtils.hasText(value)) {
						lteCell.setMcc(value);
						gsmCell.setMcc(value);
					}
				}
				if (mnc != -1) {
					String value = getCellValue(rowContext.getCell(mnc));
					if (StringUtils.hasText(value)) {
						lteCell.setMnc(value);
						gsmCell.setMnc(value);
					}
				}
				if (mmeGroupID != -1) {
					String value = getCellValue(rowContext.getCell(mmeGroupID));
					if (StringUtils.hasText(value)) {
						lteCell.setMmeGroupId(value);
					}
				}

				if (eNBId != -1) {
					String value = getCellValue(rowContext.getCell(eNBId));
					if (StringUtils.hasText(value)) {
						lteCell.seteNBId(value);
					}
				}
				if (siteName != -1) {
					String value = getCellValue(rowContext.getCell(siteName));
					if (StringUtils.hasText(value)) {
						lteCell.setSiteName(value);
						gsmCell.setSiteName(value);
					}
				}
				if (cellName != -1) {
					String value = getCellValue(rowContext.getCell(cellName));
					if (StringUtils.hasText(value)) {
						if(cellNameList.contains(value)){
							failRowNum++;
							continue;
						}
						cellNameList.add(value);
						lteCell.setCellName(value);
						gsmCell.setCellName(value);
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*CellName\"列");
				}
				if (localCellId != -1) {
					String value = getCellValue(rowContext.getCell(localCellId));
					if (StringUtils.hasText(value)) {
						lteCell.setLocalCellId(Integer.parseInt(value));
					}
				}

				if (frequency2 != -1) {
					String value = getCellValue(rowContext.getCell(frequency2));
					if (StringUtils.hasText(value)) {
						lteCell.setFrequency2(Long.parseLong(value));
					}
				}
				if (frequency3 != -1) {
					String value = getCellValue(rowContext.getCell(frequency3));
					if (StringUtils.hasText(value)) {
						lteCell.setFrequency3(Long.parseLong(value));
					}
				}
				if (frequency4 != -1) {
					String value = getCellValue(rowContext.getCell(frequency4));
					if (StringUtils.hasText(value)) {
						lteCell.setFrequency4(Long.parseLong(value));
					}
				}
				if (frequency5 != -1) {
					String value = getCellValue(rowContext.getCell(frequency5));
					if (StringUtils.hasText(value)) {
						lteCell.setFrequency5(Long.parseLong(value));
					}
				}
				if (frequency6 != -1) {
					String value = getCellValue(rowContext.getCell(frequency6));
					if (StringUtils.hasText(value)) {
						lteCell.setFrequency6(Long.parseLong(value));
					}
				}
				if (frequency7 != -1) {
					String value = getCellValue(rowContext.getCell(frequency7));
					if (StringUtils.hasText(value)) {
						lteCell.setFrequency7(Long.parseLong(value));
					}
				}
				if (frequency8 != -1) {
					String value = getCellValue(rowContext.getCell(frequency8));
					if (StringUtils.hasText(value)) {
						lteCell.setFrequency8(Long.parseLong(value));
					}
				}
				if (frequency9 != -1) {
					String value = getCellValue(rowContext.getCell(frequency9));
					if (StringUtils.hasText(value)) {
						lteCell.setFrequency9(Long.parseLong(value));
					}
				}
				if (frequency10 != -1) {
					String value = getCellValue(rowContext.getCell(frequency10));
					if (StringUtils.hasText(value)) {
						lteCell.setFrequency10(Long.parseLong(value));
					}
				}
				if (frequency11 != -1) {
					String value = getCellValue(rowContext.getCell(frequency11));
					if (StringUtils.hasText(value)) {
						lteCell.setFrequency11(Long.parseLong(value));
					}
				}
				if (bandwidth1 != -1) {
					String value = getCellValue(rowContext.getCell(bandwidth1));
					if (StringUtils.hasText(value)) {
						lteCell.setBandwidth1(value);
					}
				}
				if (bandwidth2 != -1) {
					String value = getCellValue(rowContext.getCell(bandwidth2));
					if (StringUtils.hasText(value)) {
						lteCell.setBandwidth2(value);
					}
				}
				if (freqCount != -1) {
					String value = getCellValue(rowContext.getCell(freqCount));
					if (StringUtils.hasText(value)) {
						lteCell.setFreqCount(Integer.parseInt(value));
					}
				}
				if (longitude != -1) {
					String value = getCellValue(rowContext.getCell(longitude));
					if (StringUtils.hasText(value)) {
						if (0.0d == Double.parseDouble(value)) {
							failRowNum++;
							continue;
						}
						lteCell.setLongitude(Float.parseFloat(value));
						gsmCell.setLongitude(Float.parseFloat(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*Longitude\"列");
				}
				if (latitude != -1) {
					String value = getCellValue(rowContext.getCell(latitude));
					if (StringUtils.hasText(value)) {
						if (0.0d == Double.parseDouble(value)) {
							failRowNum++;
							continue;
						}
						lteCell.setLatitude(Float.parseFloat(value));
						gsmCell.setLatitude(Float.parseFloat(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*Latitude\"列");
				}

				if (ProjectParamInfoType.GSM.equals(operatorType)) {
					// gsm下部分特殊处理字段
					if (lac != -1) {
						String value = getCellValue(rowContext.getCell(lac));
						if (StringUtils.hasText(value)) {
							gsmCell.setLac(Long.parseLong(value));
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException("导入EXCEL中缺少\"*LAC\"列");
					}
					if (ci != -1) {
						String value = getCellValue(rowContext.getCell(ci));
						if (StringUtils.hasText(value)) {
							gsmCell.setCi(Long.parseLong(value));
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException("导入EXCEL中缺少\"*CI\"列");
					}
					if (bcch != -1) {
						String value = getCellValue(rowContext.getCell(bcch));
						if (StringUtils.hasText(value)) {
							gsmCell.setBcch(Long.parseLong(value));
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException("导入EXCEL中缺少\"*BCCH\"列");
					}
					if (bsic != -1) {
						String value = getCellValue(rowContext.getCell(bsic));
						if (StringUtils.hasText(value)) {
							gsmCell.setBsic(Long.parseLong(value));
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException("导入EXCEL中缺少\"*BSIC\"列");
					}
					if (sectorType != -1) {
						String value = getCellValue(rowContext
								.getCell(sectorType));
						if (StringUtils.hasText(value)) {
							gsmCell.setSectorType(value);
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException(
								"导入EXCEL中缺少\"*SectorType\"列");
					}
					if (doorType != -1) {
						String value = getCellValue(rowContext
								.getCell(doorType));
						if (StringUtils.hasText(value)) {
							gsmCell.setDoorType(value);
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException(
								"导入EXCEL中缺少\"*DoorType\"列");
					}
				} else if (ProjectParamInfoType.LTE.equals(operatorType)) {
					// lte部分特殊处理字段
					if (mmeId != -1) {
						String value = getCellValue(rowContext.getCell(mmeId));
						if (StringUtils.hasText(value)) {
							lteCell.setMmeId(value);
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException("导入EXCEL中缺少\"*MME ID\"列");
					}
					if (cellId != -1) {
						String value = getCellValue(rowContext.getCell(cellId));
						if (StringUtils.hasText(value)) {
							lteCell.setCellId(Long.parseLong(value));
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException(
								"导入EXCEL中缺少\"*CELL ID\"列");
					}
					if (tac != -1) {
						String value = getCellValue(rowContext.getCell(tac));
						if (StringUtils.hasText(value)) {
							lteCell.setTac(value);
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException("导入EXCEL中缺少\"*TAC\"列");
					}
					if (pci != -1) {
						String value = getCellValue(rowContext.getCell(pci));
						if (StringUtils.hasText(value)) {
							lteCell.setPci(Long.parseLong(value));
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException("导入EXCEL中缺少\"*PCI\"列");
					}
					if (frequency1 != -1) {
						String value = getCellValue(rowContext
								.getCell(frequency1));
						if (StringUtils.hasText(value)) {
							lteCell.setFrequency1(Long.parseLong(value));
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException(
								"导入EXCEL中缺少\"*Frequency1\"列");
					}
					if (sectorType != -1) {
						String value = getCellValue(rowContext
								.getCell(sectorType));
						if (StringUtils.hasText(value)) {
							lteCell.setSectorType(value);
						}
					}
					if (doorType != -1) {
						String value = getCellValue(rowContext
								.getCell(doorType));
						if (StringUtils.hasText(value)) {
							lteCell.setDoorType(value);
						}
					}
					if (region != -1) {
						String value = getCellValue(rowContext.getCell(region));
						if (StringUtils.hasText(value)) {
							lteCell.setRegion(value);
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException("导入EXCEL中缺少\"*Region\"列");
					}
				}

				if (tiltTotal != -1) {
					String value = getCellValue(rowContext.getCell(tiltTotal));
					if (StringUtils.hasText(value)) {
						lteCell.setTotalTilt(Float.parseFloat(value));
						gsmCell.setTotalTilt(Float.parseFloat(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*Tilt Total\"列");
				}
				if (tiltM != -1) {
					String value = getCellValue(rowContext.getCell(tiltM));
					if (StringUtils.hasText(value)) {
						lteCell.setMechTilt(Float.parseFloat(value));
						gsmCell.setMechTilt(Float.parseFloat(value));
					}
				}
				if (tiltE != -1) {
					String value = getCellValue(rowContext.getCell(tiltE));
					if (StringUtils.hasText(value)) {
						lteCell.setElecTilt(Float.parseFloat(value));
						gsmCell.setElecTilt(Float.parseFloat(value));
					}
				}
				if (azimuth != -1) {
					String value = getCellValue(rowContext.getCell(azimuth));
					if (StringUtils.hasText(value)) {
						lteCell.setAzimuth(Float.parseFloat(value));
						gsmCell.setAzimuth(Float.parseFloat(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*Azimuth\"列");
				}
				if (beamWidth != -1) {
					String value = getCellValue(rowContext.getCell(beamWidth));
					if (StringUtils.hasText(value)) {
						lteCell.setBeamWidth(Float.parseFloat(value));
						gsmCell.setBeamWidth(Float.parseFloat(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*Beamwidth\"列");
				}
				if (vBeamWidth != -1) {
					String value = getCellValue(rowContext.getCell(vBeamWidth));
					if (StringUtils.hasText(value)) {
						lteCell.setvBeamWidth(Float.parseFloat(value));
						gsmCell.setvBeamWidth(Float.parseFloat(value));
					}
				}
				if (aheight != -1) {
					String value = getCellValue(rowContext.getCell(aheight));
					if (StringUtils.hasText(value)) {
						lteCell.setAntHigh(Float.parseFloat(value));
						gsmCell.setAntHigh(Float.parseFloat(value));
					}
				}

				if (rac != -1) {
					String value = getCellValue(rowContext.getCell(rac));
					if (StringUtils.hasText(value)) {
						gsmCell.setRac(value);
					}
				}

				if (ncc != -1) {
					String value = getCellValue(rowContext.getCell(ncc));
					if (StringUtils.hasText(value)) {
						gsmCell.setNcc(Integer.parseInt(value));
					}
				}
				if (bcc != -1) {
					String value = getCellValue(rowContext.getCell(bcc));
					if (StringUtils.hasText(value)) {
						gsmCell.setBcc(Integer.parseInt(value));
					}
				}
				if (dcs != -1) {
					String value = getCellValue(rowContext.getCell(dcs));
					if (StringUtils.hasText(value)) {
						gsmCell.setDcs(Integer.parseInt(value));
					}
				}

				lteCells.add(lteCell);
				gsmCell.setRegion(terminalGroup.getName());
				gsmcCells.add(gsmCell);
			} catch (ApplicationException applicationException) {
				throw new ApplicationException(
						applicationException.getMessage());
			} catch (Exception e) {
				failRowNum++;
			}
		}
		// 获取cellinfo
		// CellInfo findByCityIdAndOperatorType = cellInfoDao
		// .findByCityIdAndOperatorType(cityId, infoType);
		// 删除旧数据

		terminalGroup = terminalGroupDao.find(cityId);
		CellInfo cellInfo = terminalGroup.getCellInfo(infoType);
		if (null == cellInfo) {
			// cellinfo为null时新增一个cellinfo并入库
			CellInfo newCellInfo = new CellInfo();
			newCellInfo.setOperatorType(infoType);
			newCellInfo.setGroup(terminalGroup);
			terminalGroup.getCellInfos().add(newCellInfo);
			terminalGroupDao.update(terminalGroup);
			cellInfo = terminalGroup.getCellInfo(infoType);
		}
		try {
			cellInfoDao.delCells(cityId, infoType, operatorType);
			if (ProjectParamInfoType.GSM.equals(operatorType)) {
				cellInfo.getGsmCells().size();
				cellInfo.getGsmCells().clear();
				for (GsmCell gsmCell : gsmcCells) {
					gsmCell.setCellInfo(cellInfo);
				}
				cellInfo.getGsmCells().addAll(gsmcCells);
				cellInfo.setGsmCellsImport(true);
			} else if (ProjectParamInfoType.LTE.equals(operatorType)) {
				List<LteCell> lteCelllistsql = new ArrayList<>(cellInfo.getLteCells());
				for (LteCell lteCell : lteCelllistsql) {
					if(!cellNameList.contains(lteCell.getCellName())){
						LteCell lteCellClone = new LteCell();
						lteCellClone = (LteCell)lteCell.clone();
						lteCells.add(lteCellClone);
					}else{
						for(int i=0;i<lteCells.size();i++){
							if(lteCells.get(i).getCellName().equals(lteCell.getCellName())){
								replaceClass(lteCells.get(i),lteCell);
							}
						}
					}
				}
				cellInfo.getLteCells().size();
				cellInfo.getLteCells().clear();
				for (LteCell lteCell : lteCells) {
					lteCell.setCellInfo(cellInfo);
				}
				cellInfo.getLteCells().addAll(lteCells);
				cellInfo.setLteCellsImport(true);
			}
			cellInfo.setImportDate(new Date());
			String usename = (String) SecurityUtils.getSubject().getPrincipal();
			cellInfo.setUserName(usename);
			cellInfo.setLteCellDrawSucc(0);
			cellInfo.setLteCellGisFolderName(gisDrawCellPath);
			String fileName = UUID.randomUUID().toString().replaceAll("-", "")
					.toUpperCase().trim();
			cellInfo.setLteCellGisFileName(fileName);
			cellInfoDao.update(cellInfo);
			
			if(lteCells.size()>0){
				//设置站间距
				computeStationDistance.setOperatorType(ProjectParamInfoType.LTE);
				computeStationDistance.setRegion(terminalGroup.getName());
				computeStationDistance.setStationDistancePojoList(lteCells);
				new Thread(computeStationDistance).start();
			}
			
			new Thread(new DrawGisLteCell(gisDrawCellPath, fileName,
					cellInfo.getId(), gisDrawCellUrl, gisDb,1)).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ApplicationException(
					e.getMessage());
		}
		return new int[] { totalRowNum, failRowNum };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.projectParam.IProjectParamService#importNbCell
	 * (java.lang.Long, java.lang.String, java.lang.String, java.io.File)
	 */
	@Override
	public int[] importNbCell(Long cityId, String infoType,
			String operatorType, File xlsFile) {
		TerminalGroup terminalGroup = terminalGroupDao.find(cityId);
		if (null == terminalGroup) {
			throw new ApplicationException("分组已经不存在");
		}
		Workbook workbook = null;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(xlsFile));
		} catch (IOException e) {
			throw new ApplicationException("导入邻区表失败");
		} catch (IllegalArgumentException e) {
			try {
				workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
			} catch (IOException e1) {
				throw new ApplicationException("导入邻区表失败");
			} catch (IllegalArgumentException e1) {
				throw new ApplicationException("不是EXCEL文件");
			} catch (Exception e1) {
				throw new ApplicationException("导入邻区表失败");
			}
		} finally {
			if (null != workbook) {
				try {
					workbook.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}

		Sheet sheetAt = workbook.getSheetAt(0);
		// 总的excel中记录数
		int totalRowNum = sheetAt.getLastRowNum() - sheetAt.getFirstRowNum();
		if (0 == totalRowNum) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}
		Row row = sheetAt.getRow(sheetAt.getFirstRowNum());
		if (null == row) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}

		int mmeGroupID = -1;
		int mmeId = -1;
		int eNBId = -1;
		int cellName = -1;
		int cellId = -1;

		int nbmmeGroupID = -1;
		int nbmmeId = -1;
		int nbeNBId = -1;
		int nbcellName = -1;
		int nbcellId = -1;

		int nblac = -1;
		int nbci = -1;

		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if (null == cell) {
				continue;
			}
			String value = cell.getStringCellValue().trim().toLowerCase();
			if (!StringUtils.hasText(value)) {
				continue;
			}
			if ("mme group id".equals(value)) {
				mmeGroupID = i;
			} else if ("mme id".equals(value)) {
				mmeId = i;
			} else if ("enb id".equals(value)) {
				eNBId = i;
			} else if ("cellname".equals(value)) {
				cellName = i;
			} else if ("*cell id".equals(value)) {
				cellId = i;
			} else if ("邻区mme group id".equals(value)) {
				nbmmeGroupID = i;
			} else if ("邻区mme id".equals(value)) {
				nbmmeId = i;
			} else if ("邻区enb id".equals(value)) {
				nbeNBId = i;
			} else if ("邻区cellname".equals(value)) {
				nbcellName = i;
			} else if ("*邻区cell id".equals(value)) {
				nbcellId = i;
			} else if ("*邻区lac".equals(value)) {
				nblac = i;
			} else if ("*邻区ci".equals(value)) {
				nbci = i;
			}
		}
		// 失败的记录数
		int failRowNum = 0;
		// 存储tdl邻区
		List<TdlNbCell> tdlNbCells = new ArrayList<>();
		// 存储tdl-gsm邻区
		List<TdlGsmNbCell> tdlGsmNbCells = new ArrayList<>();

		for (int i = sheetAt.getFirstRowNum() + 1; i <= sheetAt.getLastRowNum(); i++) {
			Row rowContext = sheetAt.getRow(i);
			// 防止出现中间空白行和防止代码getLastRowNum()的不准确
			short firstCellNum = rowContext.getFirstCellNum();
			short lastCellNum = rowContext.getLastCellNum();
			if (0 == lastCellNum - firstCellNum) {
				totalRowNum = totalRowNum - 1;
				continue;
			}
			TdlNbCell tdlNbCell = new TdlNbCell();
			TdlGsmNbCell tdlGsmNbCell = new TdlGsmNbCell();
			try {
				if (mmeGroupID != -1) {
					String value = getCellValue(rowContext.getCell(mmeGroupID));
					if (StringUtils.hasText(value)) {
						tdlNbCell.setMmeGroupId(value);
						tdlGsmNbCell.setMmeGroupId(value);
					}
				}
				if (mmeId != -1) {
					String value = getCellValue(rowContext.getCell(mmeId));
					if (StringUtils.hasText(value)) {
						tdlNbCell.setMmeId(value);
						tdlGsmNbCell.setMmeId(value);
					}
				}
				if (eNBId != -1) {
					String value = getCellValue(rowContext.getCell(eNBId));
					if (StringUtils.hasText(value)) {
						tdlNbCell.seteNBId(value);
						tdlGsmNbCell.seteNBId(value);
					}
				}
				if (cellName != -1) {
					String value = getCellValue(rowContext.getCell(cellName));
					if (StringUtils.hasText(value)) {
						tdlNbCell.setCellName(value);
						tdlGsmNbCell.setCellName(value);
					}
				}
				if (cellId != -1) {
					String value = getCellValue(rowContext.getCell(cellId));
					if (StringUtils.hasText(value)) {
						tdlNbCell.setCellId(Long.parseLong(value));
						tdlGsmNbCell.setCellId(Long.parseLong(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*CELL ID\"列");
				}

				if (nbmmeGroupID != -1) {
					String value = getCellValue(rowContext
							.getCell(nbmmeGroupID));
					if (StringUtils.hasText(value)) {
						tdlNbCell.setNbMmeGroupId(value);
					}
				}
				if (nbmmeId != -1) {
					String value = getCellValue(rowContext.getCell(nbmmeId));
					if (StringUtils.hasText(value)) {
						tdlNbCell.setNbMmeId(value);
					}
				}
				if (nbeNBId != -1) {
					String value = getCellValue(rowContext.getCell(nbeNBId));
					if (StringUtils.hasText(value)) {
						tdlNbCell.setNbENBId(value);
					}
				}
				if (nbcellName != -1) {
					String value = getCellValue(rowContext.getCell(nbcellName));
					if (StringUtils.hasText(value)) {
						tdlNbCell.setNbCellName(value);
						tdlGsmNbCell.setNbCellName(value);
					}
				}

				if (ProjectParamInfoType.TDL_GSM_NB.equals(operatorType)) {

					if (nblac != -1) {
						String value = getCellValue(rowContext.getCell(nblac));
						if (StringUtils.hasText(value)) {
							tdlGsmNbCell.setnbLac(value);
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException("导入EXCEL中缺少\"*邻区LAC\"列");
					}
					if (nbci != -1) {
						String value = getCellValue(rowContext.getCell(nbci));
						if (StringUtils.hasText(value)) {
							tdlGsmNbCell.setnbCi(value);
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException("导入EXCEL中缺少\"*邻区CI\"列");
					}

				} else if (ProjectParamInfoType.TDL_NB.equals(operatorType)) {
					if (nbcellId != -1) {
						String value = getCellValue(rowContext
								.getCell(nbcellId));
						if (StringUtils.hasText(value)) {
							tdlNbCell.setNbCellId(Long.parseLong(value));
						} else {
							failRowNum++;
							continue;
						}
					} else {
						throw new ApplicationException(
								"导入EXCEL中缺少\"*邻区CELL ID\"列");
					}
				}
				tdlNbCell.setRegion(terminalGroup.getName());
				tdlGsmNbCell.setRegion(terminalGroup.getName());
				tdlNbCells.add(tdlNbCell);
				tdlGsmNbCells.add(tdlGsmNbCell);
			} catch (ApplicationException applicationException) {
				throw new ApplicationException(
						applicationException.getMessage());
			} catch (Exception e) {
				failRowNum++;
			}
		}

		terminalGroup = terminalGroupDao.find(cityId);
		CellInfo cellInfo = terminalGroup.getCellInfo(infoType);
		if (null == cellInfo) {
			// cellinfo为null时新增一个cellinfo并入库
			CellInfo newCellInfo = new CellInfo();
			newCellInfo.setOperatorType(infoType);
			newCellInfo.setGroup(terminalGroup);
			terminalGroup.getCellInfos().add(newCellInfo);
			terminalGroupDao.update(terminalGroup);
			cellInfo = terminalGroup.getCellInfo(infoType);
		}
		cellInfoDao.delCells(cityId, infoType, operatorType);
		if (ProjectParamInfoType.TDL_GSM_NB.equals(operatorType)) {
			cellInfo.getTdlGsmNbCells().size();
			cellInfo.getTdlGsmNbCells().clear();
			for (TdlGsmNbCell tdlGsmNbCell : tdlGsmNbCells) {
				tdlGsmNbCell.setCellInfo(cellInfo);
			}
			cellInfo.getTdlGsmNbCells().addAll(tdlGsmNbCells);
			cellInfo.setTdlGsmNbCellsImport(true);
		} else if (ProjectParamInfoType.TDL_NB.equals(operatorType)) {
			cellInfo.getTdlNbCells().size();
			cellInfo.getTdlNbCells().clear();
			for (TdlNbCell tdlNbCell : tdlNbCells) {
				tdlNbCell.setCellInfo(cellInfo);
			}
			cellInfo.getTdlNbCells().addAll(tdlNbCells);
			cellInfo.setTdlNbCellsImport(true);
		}

		cellInfo.setImportDate(new Date());
		if (!StringUtils.hasText(cellInfo.getUserName())) {
			String usename = (String) SecurityUtils.getSubject().getPrincipal();
			cellInfo.setUserName(usename);
		}
		cellInfoDao.update(cellInfo);

		return new int[] { totalRowNum, failRowNum };
	}
	
	@Override
	public int[] importLte5GCell(Long cityId, String infoType,String operatorType, File xlsFile) {
		TerminalGroup terminalGroup = terminalGroupDao.find(cityId);
		if (null == terminalGroup) {
			throw new ApplicationException("分组已经不存在");
		}
		Workbook workbook = null;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(xlsFile));
		} catch (IOException e) {
			throw new ApplicationException("导入邻区表失败");
		} catch (IllegalArgumentException e) {
			try {
				workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
			} catch (IOException e1) {
				throw new ApplicationException("导入邻区表失败");
			} catch (IllegalArgumentException e1) {
				throw new ApplicationException("不是EXCEL文件");
			} catch (Exception e1) {
				throw new ApplicationException("导入邻区表失败");
			}
		} finally {
			if (null != workbook) {
				try {
					workbook.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}

		Sheet sheetAt = workbook.getSheetAt(0);
		// 总的excel中记录数
		int totalRowNum = sheetAt.getLastRowNum() - sheetAt.getFirstRowNum();
		if (0 == totalRowNum) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}
		Row row = sheetAt.getRow(sheetAt.getFirstRowNum());
		if (null == row) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}
		
		int enbid = -1;  //必填
		int cellid = -1;  //必填
		int cellname = -1;  //选填
		int pamfgroupid = -1;  //选填
		int pamfid = -1;  //选填
		int pgnbid = -1;  //选填
		int pcellname = -1;  //选填
		int pcellid = -1;  //必填
		
		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if (null == cell) {
				continue;
			}
			String value = cell.getStringCellValue().trim().toLowerCase();
			if (!StringUtils.hasText(value)) {
				continue;
			}
			if ("*enb id".equals(value)) {
				enbid = i;
			} else if ("*cell id".equals(value)) {
				cellid = i;
			} else if ("cellname".equals(value)) {
				cellname = i;
			} else if ("配对小区amf group id".equals(value)) {
				pamfgroupid = i;
			} else if ("配对小区amf id".equals(value)) {
				pamfid = i;
			} else if ("配对小区gnb id".equals(value)) {
				pgnbid = i;
			} else if ("配对小区cellname".equals(value)) {
				pcellname = i;
			} else if ("*配对小区cell id".equals(value)) {
				pcellid = i;
			}
		}
		
		// 失败的记录数
		int failRowNum = 0;
		// 存储lte-5g对比小区
		List<Lte5GCell> lte5GCellList = new ArrayList<>();

		for (int i = sheetAt.getFirstRowNum() + 1; i <= sheetAt.getLastRowNum(); i++) {
			Row rowContext = sheetAt.getRow(i);
			// 防止出现中间空白行和防止代码getLastRowNum()的不准确
			short firstCellNum = rowContext.getFirstCellNum();
			short lastCellNum = rowContext.getLastCellNum();
			if (0 == lastCellNum - firstCellNum) {
				totalRowNum = totalRowNum - 1;
				continue;
			}
			Lte5GCell lte5GCell = new Lte5GCell();
			try {
				if (enbid != -1) {
					String value = getCellValue(rowContext.getCell(enbid));
					if (StringUtils.hasText(value)) {
						lte5GCell.setEnbId(value);
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*eNB ID\"列");
				}
				
				if (cellid != -1) {
					String value = getCellValue(rowContext.getCell(cellid));
					if (StringUtils.hasText(value)) {
						lte5GCell.setCelld(Integer.parseInt(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*CELL ID\"列");
				}
				
				if (cellname != -1) {
					String value = getCellValue(rowContext.getCell(cellname));
					if (StringUtils.hasText(value)) {
						lte5GCell.setCellName(value);
					}
				}
				
				if (pamfgroupid != -1) {
					String value = getCellValue(rowContext.getCell(pamfgroupid));
					if (StringUtils.hasText(value)) {
						lte5GCell.setPairmfGroupId(value);
					}
				}
				
				if (pamfid != -1) {
					String value = getCellValue(rowContext.getCell(pamfid));
					if (StringUtils.hasText(value)) {
						lte5GCell.setPairAmfId(Integer.parseInt(value));
					}
				}

				if (pgnbid != -1) {
					String value = getCellValue(rowContext.getCell(pgnbid));
					if (StringUtils.hasText(value)) {
						lte5GCell.setPairGnbId(Integer.parseInt(value));
					}
				}
				
				if (pcellname != -1) {
					String value = getCellValue(rowContext.getCell(pcellname));
					if (StringUtils.hasText(value)) {
						lte5GCell.setPairCellName(value);
					}
				}
				
				if (pcellid != -1) {
					String value = getCellValue(rowContext.getCell(pcellid));
					if (StringUtils.hasText(value)) {
						lte5GCell.setPairCellId(Integer.parseInt(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*配对小区CELL ID\"列");
				}

				lte5GCell.setRegion(terminalGroup.getName());
				lte5GCellList.add(lte5GCell);
			} catch (ApplicationException applicationException) {
				throw new ApplicationException(
						applicationException.getMessage());
			} catch (Exception e) {
				failRowNum++;
			}
		}
		
		terminalGroup = terminalGroupDao.find(cityId);
		CellInfo cellInfo = terminalGroup.getCellInfo(infoType);
		if (null == cellInfo) {
			// cellinfo为null时新增一个cellinfo并入库
			CellInfo newCellInfo = new CellInfo();
			newCellInfo.setOperatorType(infoType);
			newCellInfo.setGroup(terminalGroup);
			terminalGroup.getCellInfos().add(newCellInfo);
			terminalGroupDao.update(terminalGroup);
			cellInfo = terminalGroup.getCellInfo(infoType);
		}
		//删除所有对应的Lte5GCell
		cellInfoDao.delCells(cityId, infoType, operatorType);
		
		cellInfo.getLte5GCells().size();
		cellInfo.getLte5GCells().clear();
		for (Lte5GCell lte5GCell : lte5GCellList) {
			lte5GCell.setCellInfo(cellInfo);
		}
		cellInfo.getLte5GCells().addAll(lte5GCellList);
		cellInfo.setLte5GCellsImport(true);
		
		cellInfo.setImportDate(new Date());
		if (!StringUtils.hasText(cellInfo.getUserName())) {
			String usename = (String) SecurityUtils.getSubject().getPrincipal();
			cellInfo.setUserName(usename);
		}
		cellInfoDao.update(cellInfo);

		return new int[] { totalRowNum, failRowNum };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.projectParam.IProjectParamService#import5GCell
	 * (java.lang.Long, java.lang.String, java.io.File)
	 */
	@Override
	public int[] import5GCell(Long cityId, String infoType, File xlsFile) {
		TerminalGroup terminalGroup = terminalGroupDao.find(cityId);
		if (null == terminalGroup) {
			throw new ApplicationException("分组已经不存在");
		}
		Workbook workbook = null;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(xlsFile));
		} catch (IOException e) {
			throw new ApplicationException("导入小区表失败");
		} catch (IllegalArgumentException e) {
			try {
				workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
			} catch (IOException e1) {
				throw new ApplicationException("导入小区表失败");
			} catch (IllegalArgumentException e1) {
				throw new ApplicationException("不是EXCEL文件");
			} catch (Exception e1) {
				throw new ApplicationException("导入小区表失败");
			}
		} finally {
			if (null != workbook) {
				try {
					workbook.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}
		Sheet sheetAt = workbook.getSheetAt(0);
		// 总的excel中记录数
		int totalRowNum = sheetAt.getLastRowNum() - sheetAt.getFirstRowNum();
		if (0 == totalRowNum) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}
		Row row = sheetAt.getRow(sheetAt.getFirstRowNum());
		if (null == row) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}

		int mcc = -1;
		int mnc = -1;
		int amfGroupID = -1;
		int amfId = -1;
		int gNBId = -1;

		int siteName = -1;
		int cellName = -1;
		int localCellId = -1;
		int cellId = -1;
		int tac = -1;
		int pci = -1;

		int frequency1 = -1;
		int frequency2 = -1;
		int frequency3 = -1;
		int frequency4 = -1;
		int frequency5 = -1;
		int frequency6 = -1;
		int frequency7 = -1;
		int frequency8 = -1;
		int frequency9 = -1;
		int frequency10 = -1;
		int frequency11 = -1;
		int bandwidth1 = -1;
		int bandwidth2 = -1;
		int freqCount = -1;
		int longitude = -1;
		int latitude = -1;

		int sectorType = -1;
		int doorType = -1;
		int tiltTotal = -1;
		int tiltM = -1;
		int tiltE = -1;
		int azimuth = -1;
		int beamCount = -1;
		int height = -1;

		int region = -1;
		int vender = -1;
		int coverScene = -1;
		int isBelongtoNetwork = -1;

		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if (null == cell) {
				continue;
			}
			String value = cell.getStringCellValue().trim().toLowerCase();
			if (!StringUtils.hasText(value)) {
				continue;
			}
			if ("mcc".equals(value)) {
				mcc = i;
			} else if ("mnc".equals(value)) {
				mnc = i;
			} else if ("amf group id".equals(value)) {
				amfGroupID = i;
			} else if ("amf id".equals(value)) {
				amfId = i;
			} else if ("gnb id".equals(value)) {
				gNBId = i;
			} else if ("sitename".equals(value)) {
				siteName = i;
			} else if ("*cellname".equals(value)) {
				cellName = i;
			} else if ("localcellid".equals(value)) {
				localCellId = i;
			} else if ("*cell id".equals(value)) {
				cellId = i;
			} else if ("*tac".equals(value)) {
				tac = i;
			} else if ("*pci".equals(value)) {
				pci = i;
			} else if ("*frequency1".equals(value)) {
				frequency1 = i;
			} else if ("frequency2".equals(value)) {
				frequency2 = i;
			} else if ("frequency3".equals(value)) {
				frequency3 = i;
			} else if ("frequency4".equals(value)) {
				frequency4 = i;
			} else if ("frequency5".equals(value)) {
				frequency5 = i;
			} else if ("frequency6".equals(value)) {
				frequency6 = i;
			} else if ("frequency7".equals(value)) {
				frequency7 = i;
			} else if ("frequency8".equals(value)) {
				frequency8 = i;
			} else if ("frequency9".equals(value)) {
				frequency9 = i;
			} else if ("frequency10".equals(value)) {
				frequency10 = i;
			} else if ("frequency11".equals(value)) {
				frequency11 = i;
			} else if ("bandwidth1".equals(value)) {
				bandwidth1 = i;
			} else if ("bandwidth2".equals(value)) {
				bandwidth2 = i;
			} else if ("freqcount".equals(value)) {
				freqCount = i;
			} else if ("*longitude".equals(value)) {
				longitude = i;
			} else if ("*latitude".equals(value)) {
				latitude = i;
			} else if ("sectortype".equals(value)) {
				sectorType = i;
			} else if ("doortype".equals(value)) {
				doorType = i;
			} else if ("*tilt total".equals(value)) {
				tiltTotal = i;
			} else if ("tilt m".equals(value)) {
				tiltM = i;
			} else if ("tilt e".equals(value)) {
				tiltE = i;
			} else if ("*azimuth".equals(value)) {
				azimuth = i;
			} else if ("*beamcount".equals(value)) {
				beamCount = i;
			} else if ("*height".equals(value)) {
				height = i;
			} else if ("*region".equals(value)) {
				region = i;
			} else if ("厂家".equals(value)) {
				vender = i;
			} else if ("覆盖场景".equals(value)) {
				coverScene = i;
			} else if ("是否属于网格".equals(value)) {
				isBelongtoNetwork = i;
			}
		}
		// 失败的记录数
		int failRowNum = 0;
		//存储cellName，保证唯一性
		List<String> cellNameList = new ArrayList<>();
		// 存储5g小区
		List<Cell5G> cells5g = new ArrayList<>();
		for (int i = sheetAt.getFirstRowNum() + 1; i <= sheetAt.getLastRowNum(); i++) {
			Row rowContext = sheetAt.getRow(i);

			// 防止出现中间空白行和防止代码getLastRowNum()的不准确
			short firstCellNum = rowContext.getFirstCellNum();
			short lastCellNum = rowContext.getLastCellNum();
			if (0 == lastCellNum - firstCellNum) {
				totalRowNum = totalRowNum - 1;
				continue;
			}

			Cell5G cell5g = new Cell5G();
			try {

				if (mcc != -1) {
					String value = getCellValue(rowContext.getCell(mcc));
					if (StringUtils.hasText(value)) {
						cell5g.setMcc(value);
					}
				}
				if (mnc != -1) {
					String value = getCellValue(rowContext.getCell(mnc));
					if (StringUtils.hasText(value)) {
						cell5g.setMnc(value);
					}
				}
				if (amfGroupID != -1) {
					String value = getCellValue(rowContext.getCell(amfGroupID));
					if (StringUtils.hasText(value)) {
						cell5g.setAmfGroupId(value);
					}
				}
				if (amfId != -1) {
					String value = getCellValue(rowContext.getCell(amfId));
					if (StringUtils.hasText(value)) {
						cell5g.setAmfId(value);
					}
				}
				if (gNBId != -1) {
					String value = getCellValue(rowContext.getCell(gNBId));
					if (StringUtils.hasText(value)) {
						cell5g.setgNBId(value);
					}
				}
				if (siteName != -1) {
					String value = getCellValue(rowContext.getCell(siteName));
					if (StringUtils.hasText(value)) {
						cell5g.setSiteName(value);
					}
				}
				if (cellName != -1) {
					String value = getCellValue(rowContext.getCell(cellName));
					if (StringUtils.hasText(value)) {
						if(cellNameList.contains(value)){
							failRowNum++;
							continue;
						}
						cellNameList.add(value);
						cell5g.setCellName(value);
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*CellName\"列");
				}
				if (localCellId != -1) {
					String value = getCellValue(rowContext.getCell(localCellId));
					if (StringUtils.hasText(value)) {
						cell5g.setLocalCellId(Integer.parseInt(value));
					}
				}
				if (cellId != -1) {
					String value = getCellValue(rowContext.getCell(cellId));
					if (StringUtils.hasText(value)) {
						cell5g.setCellId(Long.parseLong(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*CELL ID\"列");
				}
				if (tac != -1) {
					String value = getCellValue(rowContext.getCell(tac));
					if (StringUtils.hasText(value)) {
						cell5g.setTac(value);
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*TAC\"列");
				}
				if (pci != -1) {
					String value = getCellValue(rowContext.getCell(pci));
					if (StringUtils.hasText(value)) {
						cell5g.setPci(Long.parseLong(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*PCI\"列");
				}
				if (frequency1 != -1) {
					String value = getCellValue(rowContext.getCell(frequency1));
					if (StringUtils.hasText(value)) {
						cell5g.setFrequency1(Long.parseLong(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*Frequency1\"列");
				}

				if (frequency2 != -1) {
					String value = getCellValue(rowContext.getCell(frequency2));
					if (StringUtils.hasText(value)) {
						cell5g.setFrequency2(Long.parseLong(value));
					}
				}
				if (frequency3 != -1) {
					String value = getCellValue(rowContext.getCell(frequency3));
					if (StringUtils.hasText(value)) {
						cell5g.setFrequency3(Long.parseLong(value));
					}
				}
				if (frequency4 != -1) {
					String value = getCellValue(rowContext.getCell(frequency4));
					if (StringUtils.hasText(value)) {
						cell5g.setFrequency4(Long.parseLong(value));
					}
				}
				if (frequency5 != -1) {
					String value = getCellValue(rowContext.getCell(frequency5));
					if (StringUtils.hasText(value)) {
						cell5g.setFrequency5(Long.parseLong(value));
					}
				}
				if (frequency6 != -1) {
					String value = getCellValue(rowContext.getCell(frequency6));
					if (StringUtils.hasText(value)) {
						cell5g.setFrequency6(Long.parseLong(value));
					}
				}
				if (frequency7 != -1) {
					String value = getCellValue(rowContext.getCell(frequency7));
					if (StringUtils.hasText(value)) {
						cell5g.setFrequency7(Long.parseLong(value));
					}
				}
				if (frequency8 != -1) {
					String value = getCellValue(rowContext.getCell(frequency8));
					if (StringUtils.hasText(value)) {
						cell5g.setFrequency8(Long.parseLong(value));
					}
				}
				if (frequency9 != -1) {
					String value = getCellValue(rowContext.getCell(frequency9));
					if (StringUtils.hasText(value)) {
						cell5g.setFrequency9(Long.parseLong(value));
					}
				}
				if (frequency10 != -1) {
					String value = getCellValue(rowContext.getCell(frequency10));
					if (StringUtils.hasText(value)) {
						cell5g.setFrequency10(Long.parseLong(value));
					}
				}
				if (frequency11 != -1) {
					String value = getCellValue(rowContext.getCell(frequency11));
					if (StringUtils.hasText(value)) {
						cell5g.setFrequency11(Long.parseLong(value));
					}
				}
				if (bandwidth1 != -1) {
					String value = getCellValue(rowContext.getCell(bandwidth1));
					if (StringUtils.hasText(value)) {
						cell5g.setBandwidth1(value);
					}
				}
				if (bandwidth2 != -1) {
					String value = getCellValue(rowContext.getCell(bandwidth2));
					if (StringUtils.hasText(value)) {
						cell5g.setBandwidth2(value);
					}
				}
				if (freqCount != -1) {
					String value = getCellValue(rowContext.getCell(freqCount));
					if (StringUtils.hasText(value)) {
						cell5g.setFreqCount(Integer.parseInt(value));
					}
				}
				if (longitude != -1) {
					String value = getCellValue(rowContext.getCell(longitude));
					if (StringUtils.hasText(value)) {
						if (0.0d == Double.parseDouble(value)) {
							failRowNum++;
							continue;
						}
						cell5g.setLongitude(Float.parseFloat(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*Longitude\"列");
				}
				if (latitude != -1) {
					String value = getCellValue(rowContext.getCell(latitude));
					if (StringUtils.hasText(value)) {
						if (0.0d == Double.parseDouble(value)) {
							failRowNum++;
							continue;
						}
						cell5g.setLatitude(Float.parseFloat(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*Latitude\"列");
				}
				if (sectorType != -1) {
					String value = getCellValue(rowContext.getCell(sectorType));
					if (StringUtils.hasText(value)) {
						cell5g.setSectorType(value);
					}
				}
				if (doorType != -1) {
					String value = getCellValue(rowContext.getCell(doorType));
					if (StringUtils.hasText(value)) {
						cell5g.setDoorType(value);
					}
				}
				if (tiltTotal != -1) {
					String value = getCellValue(rowContext.getCell(tiltTotal));
					if (StringUtils.hasText(value)) {
						cell5g.setTotalTilt(Float.parseFloat(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*Tilt Total\"列");
				}
				if (tiltM != -1) {
					String value = getCellValue(rowContext.getCell(tiltM));
					if (StringUtils.hasText(value)) {
						cell5g.setMechTilt(Float.parseFloat(value));
					}
				}
				if (tiltE != -1) {
					String value = getCellValue(rowContext.getCell(tiltE));
					if (StringUtils.hasText(value)) {
						cell5g.setElecTilt(Float.parseFloat(value));
					}
				}
				if (azimuth != -1) {
					String value = getCellValue(rowContext.getCell(azimuth));
					if (StringUtils.hasText(value)) {
						cell5g.setAzimuth(Float.parseFloat(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*Azimuth\"列");
				}
				if (beamCount != -1) {
					String value = getCellValue(rowContext.getCell(beamCount));
					if (StringUtils.hasText(value)) {
						cell5g.setBeanCount(Float.parseFloat(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*BeamCount\"列");
				}
				if (height != -1) {
					String value = getCellValue(rowContext.getCell(height));
					if (StringUtils.hasText(value)) {
						cell5g.setHeight(Float.parseFloat(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*Height\"列");
				}
				if (region != -1) {
					String value = getCellValue(rowContext.getCell(region));
					if (StringUtils.hasText(value)) {
						cell5g.setRegion(value);
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*Region\"列");
				}
				if (vender != -1) {
					String value = getCellValue(rowContext.getCell(vender));
					if (StringUtils.hasText(value)) {
						cell5g.setVender(value);
					}
				}
				if (coverScene != -1) {
					String value = getCellValue(rowContext.getCell(coverScene));
					if (StringUtils.hasText(value)) {
						cell5g.setCoverScene(value);
					}
				}
				if (isBelongtoNetwork != -1) {
					String value = getCellValue(rowContext
							.getCell(isBelongtoNetwork));
					if (StringUtils.hasText(value)) {
						cell5g.setIsBelongtoNetwork(value);
					}
				}
				cells5g.add(cell5g);
			} catch (ApplicationException applicationException) {
				throw new ApplicationException(
						applicationException.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				failRowNum++;
			}
		}
		// 获取cellinfo
		// CellInfo findByCityIdAndOperatorType = cellInfoDao
		// .findByCityIdAndOperatorType(cityId, infoType);
		// 删除旧数据
		terminalGroup = terminalGroupDao.find(cityId);
		CellInfo cellInfo = terminalGroup.getCellInfo(infoType);
		if (null == cellInfo) {
			// cellinfo为null时新增一个cellinfo并入库
			CellInfo newCellInfo = new CellInfo();
			newCellInfo.setOperatorType(infoType);
			newCellInfo.setGroup(terminalGroup);
			terminalGroup.getCellInfos().add(newCellInfo);
			terminalGroupDao.update(terminalGroup);
			cellInfo = terminalGroup.getCellInfo(infoType);
		}
		
		try {
			cellInfoDao.delCells(cityId, infoType, ProjectParamInfoType.FG);
			List<Cell5G> cell5Glistsql = new ArrayList<>(cellInfo.getCells5g());
			for (Cell5G cell5G : cell5Glistsql) {
				if(!cellNameList.contains(cell5G.getCellName())){
					Cell5G cell5GClone = new Cell5G();
					cell5GClone = (Cell5G)cell5G.clone();
					cells5g.add(cell5GClone);
				}else{
					for(int i=0;i<cells5g.size();i++){
						if(cells5g.get(i).getCellName().equals(cell5G.getCellName())){
							replaceClass(cells5g.get(i),cell5G);
						}
					}
				}
			}
			
			cellInfo.getCells5g().size();
			cellInfo.getCells5g().clear();
			for (Cell5G cell5G_ : cells5g) {
				cell5G_.setCellInfo(null);
				cell5G_.setCellInfo(cellInfo);
			}
			cellInfo.getCells5g().addAll(cells5g);
			cellInfo.setCells5gImport(true);

			cellInfo.setImportDate(new Date());
			String usename = (String) SecurityUtils.getSubject().getPrincipal();
			cellInfo.setUserName(usename);

			// 设置小区绘制的属性
			cellInfo.setLteCellDrawSucc(0);
			cellInfo.setLteCellGisFolderName(gisDrawCellPath);
			String fileName = UUID.randomUUID().toString().replaceAll("-", "")
			.toUpperCase().trim();
			cellInfo.setNrCellGisFileName(fileName);
			cellInfoDao.update(cellInfo);
			if(cells5g.size()>0){
				//设置站间距
				computeStationDistance.setOperatorType(ProjectParamInfoType.FG);
				computeStationDistance.setRegion(terminalGroup.getName());
				computeStationDistance.setStationDistancePojoList(cells5g);
				new Thread(computeStationDistance).start();
			}
			// 小区绘制
			new Thread(new DrawGisLteCell(gisDrawCellPath, fileName,
			cellInfo.getId(), gisDrawCellUrl, gisDb,4)).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ApplicationException(
					e.getMessage());
		}
		return new int[] { totalRowNum, failRowNum };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.projectParam.IProjectParamService#import5GNbCell
	 * (java.lang.Long, java.lang.String, java.lang.String, java.io.File)
	 */
	@Override
	public int[] import5GNbCell(Long cityId, String infoType,
			String operatorType, File xlsFile) {
		TerminalGroup terminalGroup = terminalGroupDao.find(cityId);
		if (null == terminalGroup) {
			throw new ApplicationException("分组已经不存在");
		}
		Workbook workbook = null;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(xlsFile));
		} catch (IOException e) {
			throw new ApplicationException("导入邻区表失败");
		} catch (IllegalArgumentException e) {
			try {
				workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
			} catch (IOException e1) {
				throw new ApplicationException("导入邻区表失败");
			} catch (IllegalArgumentException e1) {
				throw new ApplicationException("不是EXCEL文件");
			} catch (Exception e1) {
				throw new ApplicationException("导入邻区表失败");
			}
		} finally {
			if (null != workbook) {
				try {
					workbook.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}

		Sheet sheetAt = workbook.getSheetAt(0);
		// 总的excel中记录数
		int totalRowNum = sheetAt.getLastRowNum() - sheetAt.getFirstRowNum();
		if (0 == totalRowNum) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}
		Row row = sheetAt.getRow(sheetAt.getFirstRowNum());
		if (null == row) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}

		// AMF Group ID
		// AMF ID
		// gNB ID
		// CellName
		// *CELL ID
		//
		// 邻区AMF Group ID
		// 邻区AMF ID
		// 邻区gNB ID
		// 邻区CellName
		// *邻区CELL ID
		//
		//
		// AMF Group ID
		// AMF ID
		// gNB ID
		// CellName
		// *CELL ID
		//
		// MME Group ID
		// MME ID
		// eNB ID
		// 邻区CellName
		// *邻区CELL ID

		int amfGroupID = -1;
		int amfId = -1;
		int gNBId = -1;
		int cellName = -1;
		int cellId = -1;

		int nbamfGroupID = -1;
		int nbamfId = -1;
		int nbgNBId = -1;
		int nbcellName = -1;
		int nbcellId = -1;

		int mmeGroupID = -1;
		int mmeId = -1;
		int eNBId = -1;

		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if (null == cell) {
				continue;
			}
			String value = cell.getStringCellValue().trim().toLowerCase();
			if (!StringUtils.hasText(value)) {
				continue;
			}
			if ("amf group id".equals(value)) {
				amfGroupID = i;
			} else if ("amf id".equals(value)) {
				amfId = i;
			} else if ("gnb id".equals(value)) {
				gNBId = i;
			} else if ("cellname".equals(value)) {
				cellName = i;
			} else if ("*cell id".equals(value)) {
				cellId = i;
			} else if ("邻区AMF Group ID".equals(value)) {
				nbamfGroupID = i;
			} else if ("邻区AMF ID".equals(value)) {
				nbamfId = i;
			} else if ("邻区gNB ID".equals(value)) {
				nbgNBId = i;
			} else if ("邻区cellname".equals(value)) {
				nbcellName = i;
			} else if ("*邻区cell id".equals(value)) {
				nbcellId = i;
			} else if ("mme group id".equals(value)) {
				mmeGroupID = i;
			} else if ("mme id".equals(value)) {
				mmeId = i;
			} else if ("enb id".equals(value)) {
				eNBId = i;
			}
		}
		// 失败的记录数
		int failRowNum = 0;
		// 存储5g-5g邻区
		List<Cell5GNbCell> cell5GNbCells = new ArrayList<>();
		// 存储5g-lte邻区
		List<Cell5GtdlNbCell> cell5GtdlNbCells = new ArrayList<>();

		for (int i = sheetAt.getFirstRowNum() + 1; i <= sheetAt.getLastRowNum(); i++) {
			Row rowContext = sheetAt.getRow(i);
			// 防止出现中间空白行和防止代码getLastRowNum()的不准确
			short firstCellNum = rowContext.getFirstCellNum();
			short lastCellNum = rowContext.getLastCellNum();
			if (0 == lastCellNum - firstCellNum) {
				totalRowNum = totalRowNum - 1;
				continue;
			}
			Cell5GNbCell cell5gNbCell = new Cell5GNbCell();
			Cell5GtdlNbCell cell5GtdlNbCell = new Cell5GtdlNbCell();
			try {
				if (amfGroupID != -1) {
					String value = getCellValue(rowContext.getCell(amfGroupID));
					if (StringUtils.hasText(value)) {
						cell5gNbCell.setAmfGroupId(value);
						cell5GtdlNbCell.setAmfGroupId(value);
					}
				}
				if (amfId != -1) {
					String value = getCellValue(rowContext.getCell(amfId));
					if (StringUtils.hasText(value)) {
						cell5gNbCell.setAmfId(value);
						cell5GtdlNbCell.setAmfId(value);
					}
				}
				if (gNBId != -1) {
					String value = getCellValue(rowContext.getCell(gNBId));
					if (StringUtils.hasText(value)) {
						cell5gNbCell.setgNBId(value);
						cell5GtdlNbCell.setgNBId(value);
					}
				}
				if (cellName != -1) {
					String value = getCellValue(rowContext.getCell(cellName));
					if (StringUtils.hasText(value)) {
						cell5gNbCell.setCellName(value);
						cell5GtdlNbCell.setCellName(value);
					}
				}
				if (cellId != -1) {
					String value = getCellValue(rowContext.getCell(cellId));
					if (StringUtils.hasText(value)) {
						cell5gNbCell.setCellId(Long.parseLong(value));
						cell5GtdlNbCell.setCellId(Long.parseLong(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*CELL ID\"列");
				}

				if (nbamfGroupID != -1) {
					String value = getCellValue(rowContext
							.getCell(nbamfGroupID));
					if (StringUtils.hasText(value)) {
						cell5gNbCell.setNbAmfId(value);
					}
				}
				if (nbamfId != -1) {
					String value = getCellValue(rowContext.getCell(nbamfId));
					if (StringUtils.hasText(value)) {
						cell5gNbCell.setNbAmfId(value);
					}
				}
				if (nbgNBId != -1) {
					String value = getCellValue(rowContext.getCell(nbgNBId));
					if (StringUtils.hasText(value)) {
						cell5gNbCell.setNbGNBId(value);
					}
				}
				if (nbcellName != -1) {
					String value = getCellValue(rowContext.getCell(nbcellName));
					if (StringUtils.hasText(value)) {
						cell5gNbCell.setNbCellName(value);
						cell5GtdlNbCell.setNbCellName(value);
					}
				}
				if (nbcellId != -1) {
					String value = getCellValue(rowContext.getCell(nbcellId));
					if (StringUtils.hasText(value)) {
						cell5gNbCell.setNbCellId(Long.parseLong(value));
						cell5GtdlNbCell.setNbCellId(Long.parseLong(value));
					} else {
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*邻区CELL ID\"列");
				}

				if (mmeGroupID != -1) {
					String value = getCellValue(rowContext.getCell(mmeGroupID));
					if (StringUtils.hasText(value)) {
						cell5GtdlNbCell.setMmeGroupId(value);
					}
				}
				if (mmeId != -1) {
					String value = getCellValue(rowContext.getCell(mmeId));
					if (StringUtils.hasText(value)) {
						cell5GtdlNbCell.setMmeId(value);
					}
				}
				if (eNBId != -1) {
					String value = getCellValue(rowContext.getCell(eNBId));
					if (StringUtils.hasText(value)) {
						cell5GtdlNbCell.seteNBId(value);
					}
				}
				cell5gNbCell.setRegion(terminalGroup.getName());
				cell5GtdlNbCell.setRegion(terminalGroup.getName());
				cell5GNbCells.add(cell5gNbCell);
				cell5GtdlNbCells.add(cell5GtdlNbCell);
			} catch (ApplicationException applicationException) {
				throw new ApplicationException(
						applicationException.getMessage());
			} catch (Exception e) {
				failRowNum++;
			}
		}

		terminalGroup = terminalGroupDao.find(cityId);
		CellInfo cellInfo = terminalGroup.getCellInfo(infoType);
		if (null == cellInfo) {
			// cellinfo为null时新增一个cellinfo并入库
			CellInfo newCellInfo = new CellInfo();
			newCellInfo.setOperatorType(infoType);
			newCellInfo.setGroup(terminalGroup);
			terminalGroup.getCellInfos().add(newCellInfo);
			terminalGroupDao.update(terminalGroup);
			cellInfo = terminalGroup.getCellInfo(infoType);
		}
		cellInfoDao.delCells(cityId, infoType, operatorType);
		if (ProjectParamInfoType.FG_FG.equals(operatorType)) {
			cellInfo.getCells5gNb().size();
			cellInfo.getCells5gNb().clear();
			for (Cell5GNbCell cell5GNbCell_ : cell5GNbCells) {
				cell5GNbCell_.setCellInfo(cellInfo);
			}
			cellInfo.getCells5gNb().addAll(cell5GNbCells);
			cellInfo.setCells5gNbImport(true);
		} else if (ProjectParamInfoType.FG_LTE.equals(operatorType)) {
			cellInfo.getCells5gTdlNb().size();
			cellInfo.getCells5gTdlNb().clear();
			for (Cell5GtdlNbCell cell5GtdlNbCell_ : cell5GtdlNbCells) {
				cell5GtdlNbCell_.setCellInfo(cellInfo);
			}
			cellInfo.getCells5gTdlNb().addAll(cell5GtdlNbCells);
			cellInfo.setCells5gTdlNbImport(true);
		}
		cellInfo.setImportDate(new Date());
		if (!StringUtils.hasText(cellInfo.getUserName())) {
			String usename = (String) SecurityUtils.getSubject().getPrincipal();
			cellInfo.setUserName(usename);
		}
		cellInfoDao.update(cellInfo);

		return new int[] { totalRowNum, failRowNum };
	}

	/**
	 * 获取Cell中的值
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellValue(Cell cell) {
		String value = new String();

		if (null == cell)
			return value;

		// 简单的查检列类型
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:// 字符串
			value = cell.getRichStringCellValue().getString().trim();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:// 数字
			// 读取实数
			double dd = cell.getNumericCellValue();
			long l = (long) dd;

			if (dd - l > 0) {
				// 说明是Double
				value = new Double(dd).toString().trim();
			} else {
				// 说明是Long
				value = new Long(l).toString().trim();
			}
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			value = new String();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			value = String.valueOf(cell.getCellFormula()).trim();
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:// boolean型值
			value = String.valueOf(cell.getBooleanCellValue()).trim();
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			value = String.valueOf(cell.getErrorCellValue()).trim();
			break;
		default:
			break;
		}
		return value;
	}
	
	@Override
	public String[] import5GPlanManageParam(Long cityId, String operatorType, File xlsFile,String importFileFileName){
		String errMsg="";
		TerminalGroup terminalGroup = terminalGroupDao.find(cityId);
		if (null == terminalGroup) {
			throw new ApplicationException("分组已经不存在");
		}
		Workbook workbook = null;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(xlsFile));
		} catch (IOException e) {
			throw new ApplicationException("导入邻区表失败");
		} catch (IllegalArgumentException e) {
			try {
				workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
			} catch (IOException e1) {
				throw new ApplicationException("导入邻区表失败");
			} catch (IllegalArgumentException e1) {
				throw new ApplicationException("不是EXCEL文件");
			} catch (Exception e1) {
				throw new ApplicationException("导入邻区表失败");
			}
		} finally {
			if (null != workbook) {
				try {
					workbook.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}
		Sheet sheetAt = workbook.getSheetAt(0);
		// 总的excel中记录数
		int totalRowNum = sheetAt.getLastRowNum() - sheetAt.getFirstRowNum();
		if (0 == totalRowNum) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}
		Row row = sheetAt.getRow(sheetAt.getFirstRowNum());
		if (null == row) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}
		
		int gnbId = -1;	//必填  	 *gnb id
		int cellid = -1;  //必填 	*cellid
		int sitename = -1;  //必填	*sitename
		int cellname = -1;  //必填	*cellname
		int localcellid = -1;  //必填 	*localcellid
		int tac = -1;  //选填	tac
		int pci = -1;  //必填	pci
		int frequency1 = -1;  //选填		frequency1
		int lon = -1;  //必填	*longitude
		int lat = -1;  //必填	*latitude
		int tiltm = -1;  //选填	tilt m
		int tilte=-1;	//选填	Tilt E
		int azimuth = -1;  //选填	azimuth
		int height = -1;  //选填		height
		int aauModel = -1; //选填		AAU型号
		int nrfrequency = -1;  //选填	nr频率
		int cellbandwidth = -1;  //选填	小区带宽
		int specialRatio = -1;  //选填	特殊子帧配比
		int upAndDownRatio = -1;  //选填		上下行比例
		int ssbWaveInterval = -1;  //选填	ssb子载波间隔
		int ssbSending = -1;  //选填		ssb发送功率(dbm)
		int region = -1;  //选填		region
		
		int rootSeq = -1;  //必填		*根序列
		int antennaMaf = -1;  //选填		天线厂家
		int cellTRx = -1;  //选填		小区TRx
		int frameStructure = -1;  //选填		帧结构
		int p_a = -1;  //选填		P-a
		int p_b = -1;  //选填		P-b
		int pdcchSymbol = -1; //选填  pdcch符号数
		
		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if (null == cell) {
				continue;
			}
			String value = cell.getStringCellValue().trim().toLowerCase();
			if (!StringUtils.hasText(value)) {
				continue;
			}
			if ("*gnb id".equals(value)) {
				gnbId = i;
			} else if ("*cellid".equals(value)) {
				cellid = i;
			} else if ("*sitename".equals(value)) {
				sitename = i;
			} else if ("*cellname".equals(value)) {
				cellname = i;
			} else if ("*localcellid".equals(value)) {
				localcellid = i;
			} else if ("tac".equals(value)) {
				tac = i;
			} else if ("*pci".equals(value)) {
				pci = i;
			} else if ("frequency1".equals(value)) {
				frequency1 = i;
			} else if ("*longitude".equals(value)) {
				lon = i;
			}else if ("*latitude".equals(value)) {
				lat = i;
			}else if ("tilt m".equals(value)) {
				tiltm = i;
			}else if ("tilt e".equals(value)) {
				tilte = i;
			}else if ("azimuth".equals(value)) {
				azimuth = i;
			}else if ("height".equals(value)) {
				height = i;
			}else if ("aau型号".equals(value)) {
				aauModel = i;
			}else if ("nr频率".equals(value)) {
				nrfrequency = i;
			}else if ("小区带宽".equals(value)) {
				cellbandwidth = i;
			}else if ("特殊子帧配比".equals(value)) {
				specialRatio = i;
			}else if ("上下行比例".equals(value)) {
				upAndDownRatio = i;
			}else if ("ssb子载波间隔".equals(value)) {
				ssbWaveInterval = i;
			}else if ("ssb发送功率(dbm)".equals(value)) {
				ssbSending = i;
			}else if ("*根序列".equals(value)) {
				rootSeq = i;
			}else if ("天线厂家".equals(value)) {
				antennaMaf = i;
			}else if ("小区trx".equals(value)) {
				cellTRx = i;
			}else if ("帧结构".equals(value)) {
				frameStructure = i;
			}else if ("p-a".equals(value)) {
				p_a = i;
			}else if ("p-b".equals(value)) {
				p_b = i;
			}else if ("pdcch符号数".equals(value)) {
				pdcchSymbol = i;
			}else if ("region".equals(value)) {
				region = i;
			}
		}
		
		//查询城市信息
		terminalGroup = terminalGroupDao.find(cityId);
		
		// 失败的记录数
		int failRowNum = 0;
		// 存储5G工参
		List<PlanParamPojo> planParamPojoList = new ArrayList<>();
		//存储cellName，保证唯一性
		List<String> cellNameList = new ArrayList<>();
		for (int i = sheetAt.getFirstRowNum() + 1; i <= sheetAt.getLastRowNum(); i++) {
			Row rowContext = null;
			try {
				rowContext = sheetAt.getRow(i);
				// 防止出现中间空白行和防止代码getLastRowNum()的不准确
				short firstCellNum = rowContext.getFirstCellNum();
				short lastCellNum = rowContext.getLastCellNum();
				if (0 == lastCellNum - firstCellNum) {
					totalRowNum = totalRowNum - 1;
					continue;
				}
			} catch (Exception e) {
				throw new ApplicationException(
						"导入数据异常：请删除数据下面的空行，并确认数据是否有问题");
			}
			PlanParamPojo planParamPojo = new PlanParamPojo();
			try {
				if (gnbId != -1) {
					String value = getCellValue(rowContext.getCell(gnbId));
					if (StringUtils.hasText(value)) {
						planParamPojo.setGnbId(Long.parseLong(value));
					} else {
						errMsg = errMsg+"第"+i+"行的\"*gNB ID\"列为空;";
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*gNB ID\"列");
				}
				
				if (cellid != -1) {
					String value = getCellValue(rowContext.getCell(cellid));
					if (StringUtils.hasText(value)) {
						planParamPojo.setCellId(value);
					}else {
						errMsg = errMsg+"第"+i+"行的\"*cellid\"列为空;";
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*cellid\"列");
				}
				
				if (sitename != -1) {
					String value = getCellValue(rowContext.getCell(sitename));
					if (StringUtils.hasText(value)) {
						planParamPojo.setSiteName(value);
					}else {
						errMsg = errMsg+"第"+i+"行的\"*SiteName\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*SiteName\"列");
				}
				
				if (localcellid != -1) {
					String value = getCellValue(rowContext.getCell(localcellid));
					if (StringUtils.hasText(value)) {
						planParamPojo.setLocalCellID(Integer.valueOf(value));
					}else {
						errMsg = errMsg+"第"+i+"行的\"*LocalCellId\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*LocalCellId\"列");
				}
				
				if (pci != -1) {
					String value = getCellValue(rowContext.getCell(pci));
					if (StringUtils.hasText(value)) {
						planParamPojo.setPci(Integer.parseInt(value));
					}else {
						errMsg = errMsg+"第"+i+"行的\"*PCI\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*PCI\"列");
				}
				
				if (lon != -1) {
					String value = getCellValue(rowContext.getCell(lon));
					if (StringUtils.hasText(value)) {
						planParamPojo.setLon(Float.parseFloat(value));
					}else {
						errMsg = errMsg+"第"+i+"行的\"*Longitude\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*Longitude\"列");
				}
				
				if (lat != -1) {
					String value = getCellValue(rowContext.getCell(lat));
					if (StringUtils.hasText(value)) {
						planParamPojo.setLat(Float.parseFloat(value));
					}else {
						errMsg = errMsg+"第"+i+"行的\"*Latitude\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*Latitude\"列");
				}
				
				if (rootSeq != -1) {
					String value = getCellValue(rowContext.getCell(rootSeq));
					if (StringUtils.hasText(value)) {
						planParamPojo.setRootSeq(value);
					}else {
						errMsg = errMsg+"第"+i+"行的\"*根序列\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*根序列\"列");
				}
				
				if (cellname != -1) {
					String value = getCellValue(rowContext.getCell(cellname));
					if (StringUtils.hasText(value)) {
						if(cellNameList.contains(value)){
							failRowNum++;
							continue;
						}
						cellNameList.add(value);
						planParamPojo.setCellName(value);
					}else {
						errMsg = errMsg+"第"+i+"行的\"*CellName\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*CellName\"列");
				}			
				
				if (tac != -1) {
					String value = getCellValue(rowContext.getCell(tac));
					if (StringUtils.hasText(value)) {
						planParamPojo.setTac(Integer.parseInt(value));
					}
				}
				if (frequency1 != -1) {
					String value = getCellValue(rowContext.getCell(frequency1));
					if (StringUtils.hasText(value)) {
						planParamPojo.setFrequency1(Integer.parseInt(value));
					}
				}
				if (tiltm != -1) {
					String value = getCellValue(rowContext.getCell(tiltm));
					if (StringUtils.hasText(value)) {
						planParamPojo.setTiltM(Integer.parseInt(value));
					}
				}
				if (tilte != -1) {
					String value = getCellValue(rowContext.getCell(tilte));
					if (StringUtils.hasText(value)) {
						planParamPojo.setTiltE(Integer.parseInt(value));
					}
				}
				if (azimuth != -1) {
					String value = getCellValue(rowContext.getCell(azimuth));
					if (StringUtils.hasText(value)) {
						planParamPojo.setAzimuth(Integer.parseInt(value));
					}
				}
				if (height != -1) {
					String value = getCellValue(rowContext.getCell(height));
					if (StringUtils.hasText(value)) {
						planParamPojo.setHeight(Integer.parseInt(value));
					}
				}
				if (aauModel != -1) {
					String value = getCellValue(rowContext.getCell(aauModel));
					if (StringUtils.hasText(value)) {
						planParamPojo.setAauModel(value);
					}
				}
				if (nrfrequency != -1) {
					String value = getCellValue(rowContext.getCell(nrfrequency));
					if (StringUtils.hasText(value)) {
						planParamPojo.setNrFrequency(value);
					}
				}
				if (cellbandwidth != -1) {
					String value = getCellValue(rowContext.getCell(cellbandwidth));
					if (StringUtils.hasText(value)) {
						planParamPojo.setCellBroadband(value);
					}
				}
				if (specialRatio != -1) {
					String value = getCellValue(rowContext.getCell(specialRatio));
					if (StringUtils.hasText(value)) {
						planParamPojo.setSpecialRatio(value);
					}
				}
				if (upAndDownRatio != -1) {
					String value = getCellValue(rowContext.getCell(upAndDownRatio));
					if (StringUtils.hasText(value)) {
						planParamPojo.setUpAndDownRatio(value);
					}
				}
				if (ssbWaveInterval != -1) {
					String value = getCellValue(rowContext.getCell(ssbWaveInterval));
					if (StringUtils.hasText(value)) {
						planParamPojo.setSsbWaveInterval(value);
					}
				}
				if (ssbSending != -1) {
					String value = getCellValue(rowContext.getCell(ssbSending));
					if (StringUtils.hasText(value)) {
						planParamPojo.setSsbSending(value);
					}
				}
				if (antennaMaf != -1) {
					String value = getCellValue(rowContext.getCell(antennaMaf));
					if (StringUtils.hasText(value)) {
						planParamPojo.setAntennaManufacturer(value);
					}
				}
				if (cellTRx != -1) {
					String value = getCellValue(rowContext.getCell(cellTRx));
					if (StringUtils.hasText(value)) {
						planParamPojo.setCellTRx(value);
					}
				}
				if (frameStructure != -1) {
					String value = getCellValue(rowContext.getCell(frameStructure));
					if (StringUtils.hasText(value)) {
						planParamPojo.setFrameStructure(value);
					}
				}
				if (p_a != -1) {
					String value = getCellValue(rowContext.getCell(p_a));
					if (StringUtils.hasText(value)) {
						planParamPojo.setP_a(value);
					}
				}
				if (p_b != -1) {
					String value = getCellValue(rowContext.getCell(p_b));
					if (StringUtils.hasText(value)) {
						planParamPojo.setP_b(value);
					}
				}
				if (pdcchSymbol != -1) {
					String value = getCellValue(rowContext.getCell(pdcchSymbol));
					if (StringUtils.hasText(value)) {
						planParamPojo.setPdcchSymbol(value);
					}
				}
				if (region != -1) {
					String value = getCellValue(rowContext.getCell(region));
					if (StringUtils.hasText(value)) {
						planParamPojo.setRegion(value);
					}
				}
				planParamPojo.setCity(terminalGroup.getName());
				planParamPojoList.add(planParamPojo);
			} catch (ApplicationException applicationException) {
				throw new ApplicationException(
						applicationException.getMessage());
			} catch (Exception e) {
				errMsg = errMsg+e.getMessage()+", ";
				failRowNum++;
			}
		}
		
		CellInfo cellInfo = terminalGroup.getCellInfo(operatorType);
		if (null == cellInfo) {
			// planParamManageInfo为null时新增一个planParamManageInfo并入库
			CellInfo newInfo = new CellInfo();
			newInfo.setOperatorType(operatorType);
			newInfo.setGroup(terminalGroup);
			terminalGroup.getCellInfos().add(newInfo);
			terminalGroupDao.update(terminalGroup);
			cellInfo = terminalGroup.getCellInfo(operatorType);
		}
		
		try {
			List<PlanParamPojo> planParamPojolistsql = new ArrayList<>(cellInfo.getPlanParams());
			for (PlanParamPojo planParamPojo : planParamPojolistsql) {
				if(!cellNameList.contains(planParamPojo.getCellName())){
					PlanParamPojo planParamPojoClone = new PlanParamPojo();
					planParamPojoClone = (PlanParamPojo)planParamPojo.clone();
					planParamPojoList.add(planParamPojoClone);
				}else{
					for(int i=0;i<planParamPojoList.size();i++){
						if(planParamPojoList.get(i).getCellName().equals(planParamPojo.getCellName())){
							replaceClass(planParamPojoList.get(i),planParamPojo);
						}
					}
				}
			}
		
		
			cellInfo.getPlanParams().size();
			cellInfo.getPlanParams().clear();
			for (PlanParamPojo planParamPojo2 : planParamPojoList) {
				planParamPojo2.setCellInfo(null);
				planParamPojo2.setCellInfo(cellInfo);
			}
			
			int successNum = totalRowNum - failRowNum;
			cellInfo.getPlanParams().addAll(planParamPojoList);
			cellInfo.setPlanSheetName(importFileFileName.substring(0,importFileFileName.lastIndexOf(".")));
			cellInfo.setTotalCellNum(planParamPojoList.size());
			cellInfo.setImportDate(new Date());
			cellInfo.setLteCellGisFolderName(gisDrawCellPath);
			cellInfo.setRegion(terminalGroup.getName());
			if (!StringUtils.hasText(cellInfo.getUserName())) {
				String usename = (String) SecurityUtils.getSubject().getPrincipal();
				cellInfo.setUserName(usename);
			}
			String fileName = UUID.randomUUID().toString().replaceAll("-", "")
					.toUpperCase().trim();
			cellInfo.setLteCellGisFileName(fileName);
			cellInfoDao.update(cellInfo);
//			computeStationDistance.setOperatorType("5GPARAM");
//			computeStationDistance.setRegion(terminalGroup.getName());
//			computeStationDistance.setStationDistancePojoList(planParamPojoList);
//			new Thread(computeStationDistance).start();
			
			new Thread(new DrawGisLteCell(gisDrawCellPath, fileName,
					cellInfo.getId(), gisDrawCellUrl, gisDb,2)).start();
			return new String[] { String.valueOf(totalRowNum), String.valueOf(failRowNum), errMsg};
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(
					e.getMessage());
		}
	}
	
	@Override
	public String[] import4GPlanManageParam(Long cityId, String operatorType, File xlsFile,String importFileFileName){
		String errMsg="";
		TerminalGroup terminalGroup = terminalGroupDao.find(cityId);
		if (null == terminalGroup) {
			throw new ApplicationException("分组已经不存在");
		}
		Workbook workbook = null;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(xlsFile));
		} catch (IOException e) {
			throw new ApplicationException("导入邻区表失败");
		} catch (IllegalArgumentException e) {
			try {
				workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
			} catch (IOException e1) {
				throw new ApplicationException("导入邻区表失败");
			} catch (IllegalArgumentException e1) {
				throw new ApplicationException("不是EXCEL文件");
			} catch (Exception e1) {
				throw new ApplicationException("导入邻区表失败");
			}
		} finally {
			if (null != workbook) {
				try {
					workbook.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}
		Sheet sheetAt = workbook.getSheetAt(0);
		// 总的excel中记录数
		int totalRowNum = sheetAt.getLastRowNum() - sheetAt.getFirstRowNum();
		if (0 == totalRowNum) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}
		Row row = sheetAt.getRow(sheetAt.getFirstRowNum());
		if (null == row) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}
		int mcc = -1;
		int mnc = -1;
		int enbId = -1;
		int siteName = -1;//必填
		int cellName = -1;//必填
		int localCellId = -1;//必填
		int cellId = -1;//必填
		int reverseOpen3d = -1;//必填
		int tac = -1;
		int broadBand = -1;
		int frequencyDl = -1;
		int pci = -1;//必填
		int longitude = -1;//必填
		int latitude = -1;//必填
		int high = -1;
		int beamWidth = -1;
		int azimuth = -1;//必填
		int tilt_m = -1;
		int tilt_e = -1;
		int type = -1;
		int earfcn = -1;//必填
		int subFrameConfig = -1;
		int specialSubFrameConfig = -1;
		int rs_epre = -1;
		int p_a = -1;
		int p_b = -1;
		int rootSeq = -1;  //必填		*根序列
		int aauModel = -1; 
		int antennaMaf = -1;  
		int frameStructure = -1;  
		int pdcchSymbol = -1; 
		int region = -1;

		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if (null == cell) {
				continue;
			}
			String value = cell.getStringCellValue().trim().toLowerCase();
			if (!StringUtils.hasText(value)) {
				continue;
			}
			if ("mcc".equals(value)) {
				mcc = i;
			} else if ("mnc".equals(value)) {
				mnc = i;
			} else if ("enodeb-id".equals(value)) {
				enbId = i;
			} else if ("*sitename".equals(value)) {
				siteName = i;
			} else if ("*cellname".equals(value)) {
				cellName = i;
			} else if ("*cellid".equals(value)) {
				cellId = i;
			} else if ("*localcellid".equals(value)) {
				localCellId = i;
			} else if ("*是否反开3d".equals(value)) {
				reverseOpen3d = i;
			} else if ("tac".equals(value)) {
				tac = i;
			} else if ("带宽".equals(value)) {
				broadBand = i;
			} else if ("frequency_dl".equals(value)) {
				frequencyDl = i;
			} else if ("*pci".equals(value)) {
				pci = i;
			} else if ("*longitude".equals(value)) {
				longitude = i;
			} else if ("*latitude".equals(value)) {
				latitude = i;
			} else if ("high".equals(value)) {
				high = i;
			} else if ("beamwidth".equals(value)) {
				beamWidth = i;
			} else if ("*azimuth".equals(value)) {
				azimuth = i;
			} else if ("tilt_m".equals(value)) {
				tilt_m = i;
			} else if ("tilt_e".equals(value)) {
				tilt_e = i;
			} else if ("type".equals(value)) {
				type = i;
			} else if ("*earfcn".equals(value)) {
				earfcn = i;
			}else if ("子帧配置".equals(value)) {
				subFrameConfig = i;
			}else if ("特殊子帧配置".equals(value)) {
				specialSubFrameConfig = i;
			}else if ("rs epre".equals(value)) {
				rs_epre = i;
			}else if ("p-a".equals(value)) {
				p_a = i;
			}else if ("p-b".equals(value)) {
				p_b = i;
			}else if ("*根序列".equals(value)) {
				rootSeq = i;
			}else if ("aau型号".equals(value)) {
				aauModel = i;
			}else if ("天线厂家".equals(value)) {
				antennaMaf = i;
			}else if ("帧结构".equals(value)) {
				frameStructure = i;
			}else if ("pdcch符号数".equals(value)) {
				pdcchSymbol = i;
			}else if ("region".equals(value)) {
				region = i;
			}
		}
		
		// 失败的记录数
		int failRowNum = 0;
		// 存储5G工参
		List<Plan4GParam> plan4GParamList = new ArrayList<>();
		//存储cellName，保证唯一性
		List<String> cellNameList = new ArrayList<>();
		for (int i = sheetAt.getFirstRowNum() + 1; i <= sheetAt.getLastRowNum(); i++) {
			Row rowContext = null;
			try {
				rowContext = sheetAt.getRow(i);
				// 防止出现中间空白行和防止代码getLastRowNum()的不准确
				short firstCellNum = rowContext.getFirstCellNum();
				short lastCellNum = rowContext.getLastCellNum();
				if (0 == lastCellNum - firstCellNum) {
					totalRowNum = totalRowNum - 1;
					continue;
				}
			} catch (Exception e) {
				throw new ApplicationException(
						"导入数据异常：请删除数据下面的空行，并确认数据是否有问题");

			}
			Plan4GParam plan4GParam = new Plan4GParam();
			try {
				if (cellId != -1) {
					String value = getCellValue(rowContext.getCell(cellId));
					if (StringUtils.hasText(value)) {
						plan4GParam.setCellId(Long.parseLong(value));
					} else {
						errMsg = errMsg+"第"+i+"行的\"*CellID\"列为空;";
						failRowNum++;
						continue;
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"*CellID\"列");
				}
				
				if (siteName != -1) {
					String value = getCellValue(rowContext.getCell(siteName));
					if (StringUtils.hasText(value)) {
						plan4GParam.setSiteName(value);
					}else {
						errMsg = errMsg+"第"+i+"行的\"*SiteName\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*SiteName\"列");
				}
				
				if (localCellId != -1) {
					String value = getCellValue(rowContext.getCell(localCellId));
					if (StringUtils.hasText(value)) {
						plan4GParam.setLocalCellId(Integer.parseInt(value));
					}else {
						errMsg = errMsg+"第"+i+"行的\"*LocalCellId\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*LocalCellId\"列");
				}
				
				if (reverseOpen3d != -1) {
					String value = getCellValue(rowContext.getCell(reverseOpen3d));
					if (StringUtils.hasText(value)) {
						plan4GParam.setReverseOpen3d(value);
					}else {
						errMsg = errMsg+"第"+i+"行的\"*是否反开3d\"为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*是否反开3d\"列");
				}
				
				if (pci != -1) {
					String value = getCellValue(rowContext.getCell(pci));
					if (StringUtils.hasText(value)) {
						plan4GParam.setPci(Long.parseLong(value));
					}else {
						errMsg = errMsg+"第"+i+"行的\"*PCI\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*PCI\"列");
				}
				
				if (longitude != -1) {
					String value = getCellValue(rowContext.getCell(longitude));
					if (StringUtils.hasText(value)) {
						plan4GParam.setLongitude(Float.parseFloat(value));
					}else {
						errMsg = errMsg+"第"+i+"行的\"*Longitude\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*Longitude\"列");
				}
				
				if (latitude != -1) {
					String value = getCellValue(rowContext.getCell(latitude));
					if (StringUtils.hasText(value)) {
						plan4GParam.setLatitude(Float.parseFloat(value));
					}else {
						errMsg = errMsg+"第"+i+"行的\"*Latitude\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*Latitude\"列");
				}
				
				if (azimuth != -1) {
					String value = getCellValue(rowContext.getCell(azimuth));
					if (StringUtils.hasText(value)) {
						plan4GParam.setAzimuth(Float.parseFloat(value));
					}else {
						errMsg = errMsg+"第"+i+"行的\"*Azimuth\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*Azimuth\"列");
				}
				
				if (earfcn != -1) {
					String value = getCellValue(rowContext.getCell(earfcn));
					if (StringUtils.hasText(value)) {
						plan4GParam.setEarfcn(value);
					}else {
						errMsg = errMsg+"第"+i+"行的\"*Earfcn\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*Earfcn\"列");
				}
				
				if (rootSeq != -1) {
					String value = getCellValue(rowContext.getCell(rootSeq));
					if (StringUtils.hasText(value)) {
						plan4GParam.setRootSeq(value);
					}else {
						errMsg = errMsg+"第"+i+"行的\"*根序列\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*根序列\"列");
				}
				
				if (cellName != -1) {
					String value = getCellValue(rowContext.getCell(cellName));
					if (StringUtils.hasText(value)) {
						if(cellNameList.contains(value)){
							failRowNum++;
							continue;
						}
						cellNameList.add(value);
						plan4GParam.setCellName(value);
					}else {
						errMsg = errMsg+"第"+i+"行的\"*CellName\"列为空;";
						failRowNum++;
						continue;
					}
				}else {
					throw new ApplicationException("导入EXCEL中缺少\"*CellName\"列");
				}

				
				if (mcc != -1) {
					String value = getCellValue(rowContext.getCell(mcc));
					if (StringUtils.hasText(value)) {
						plan4GParam.setMcc(value);
					}
				}
				if (mnc != -1) {
					String value = getCellValue(rowContext.getCell(mnc));
					if (StringUtils.hasText(value)) {
						plan4GParam.setMnc(value);
					}
				}
				if (enbId != -1) {
					String value = getCellValue(rowContext.getCell(enbId));
					if (StringUtils.hasText(value)) {
						plan4GParam.setEnbId(value);
					}
				}
				if (tac != -1) {
					String value = getCellValue(rowContext.getCell(tac));
					if (StringUtils.hasText(value)) {
						plan4GParam.setTac(Integer.parseInt(value));
					}
				}
				if (broadBand != -1) {
					String value = getCellValue(rowContext.getCell(broadBand));
					if (StringUtils.hasText(value)) {
						plan4GParam.setBroadBand(Float.parseFloat(value));
					}
				}
				if (frequencyDl != -1) {
					String value = getCellValue(rowContext.getCell(frequencyDl));
					if (StringUtils.hasText(value)) {
						plan4GParam.setFrequencyDl(value);
					}
				}
				if (high != -1) {
					String value = getCellValue(rowContext.getCell(high));
					if (StringUtils.hasText(value)) {
						plan4GParam.setHigh(Float.parseFloat(value));
					}
				}
				if (beamWidth != -1) {
					String value = getCellValue(rowContext.getCell(beamWidth));
					if (StringUtils.hasText(value)) {
						plan4GParam.setBeamWidth(Float.parseFloat(value));
					}
				}
				if (tilt_m != -1) {
					String value = getCellValue(rowContext.getCell(tilt_m));
					if (StringUtils.hasText(value)) {
						plan4GParam.setTilt_m(Float.parseFloat(value));
					}
				}
				if (tilt_e != -1) {
					String value = getCellValue(rowContext.getCell(tilt_e));
					if (StringUtils.hasText(value)) {
						plan4GParam.setTilt_e(Float.parseFloat(value));
					}
				}
				if (type != -1) {
					String value = getCellValue(rowContext.getCell(type));
					if (StringUtils.hasText(value)) {
						plan4GParam.setType(value);
					}
				}
				if (subFrameConfig != -1) {
					String value = getCellValue(rowContext.getCell(subFrameConfig));
					if (StringUtils.hasText(value)) {
						plan4GParam.setSubFrameConfig(value);
					}
				}
				if (specialSubFrameConfig != -1) {
					String value = getCellValue(rowContext.getCell(specialSubFrameConfig));
					if (StringUtils.hasText(value)) {
						plan4GParam.setSpecialSubFrameConfig(value);
					}
				}
				if (rs_epre != -1) {
					String value = getCellValue(rowContext.getCell(rs_epre));
					if (StringUtils.hasText(value)) {
						plan4GParam.setRs_epre(value);
					}
				}
				if (p_a != -1) {
					String value = getCellValue(rowContext.getCell(p_a));
					if (StringUtils.hasText(value)) {
						plan4GParam.setP_a(value);
					}
				}
				if (p_b != -1) {
					String value = getCellValue(rowContext.getCell(p_b));
					if (StringUtils.hasText(value)) {
						plan4GParam.setP_b(value);
					}
				}
				if (antennaMaf != -1) {
					String value = getCellValue(rowContext.getCell(antennaMaf));
					if (StringUtils.hasText(value)) {
						plan4GParam.setAntennaManufacturer(value);
					}
				}
				if (aauModel != -1) {
					String value = getCellValue(rowContext.getCell(aauModel));
					if (StringUtils.hasText(value)) {
						plan4GParam.setAauModel(value);
					}
				}
				if (frameStructure != -1) {
					String value = getCellValue(rowContext.getCell(frameStructure));
					if (StringUtils.hasText(value)) {
						plan4GParam.setFrameStructure(value);
					}
				}
				if (pdcchSymbol != -1) {
					String value = getCellValue(rowContext.getCell(pdcchSymbol));
					if (StringUtils.hasText(value)) {
						plan4GParam.setPdcchSymbol(value);
					}
				}
				if (region != -1) {
					String value = getCellValue(rowContext.getCell(region));
					if (StringUtils.hasText(value)) {
						plan4GParam.setRegion(value);
					}
				}
				plan4GParam.setRegion(terminalGroup.getName());
				plan4GParamList.add(plan4GParam);
			} catch (ApplicationException applicationException) {
				throw new ApplicationException(
						applicationException.getMessage());
			} catch (Exception e) {
				errMsg = errMsg+e.getMessage()+", ";
				failRowNum++;
			}
		}
		
		terminalGroup = terminalGroupDao.find(cityId);
		CellInfo cellInfo = terminalGroup.getCellInfo(operatorType);
		if (null == cellInfo) {
			// planParamManageInfo为null时新增一个planParamManageInfo并入库
			CellInfo newInfo = new CellInfo();
			newInfo.setOperatorType(operatorType);
			newInfo.setGroup(terminalGroup);
			terminalGroup.getCellInfos().add(newInfo);
			terminalGroupDao.update(terminalGroup);
			cellInfo = terminalGroup.getCellInfo(operatorType);
		}
		
		try {
			List<Plan4GParam> plan4GParamlistsql = new ArrayList<>(cellInfo.getPlan4GParams());
			for (Plan4GParam plan4GParam : plan4GParamlistsql) {
				if(!cellNameList.contains(plan4GParam.getCellName())){
					Plan4GParam plan4GParamClone = new Plan4GParam();
					plan4GParamClone = (Plan4GParam)plan4GParam.clone();
					plan4GParamList.add(plan4GParamClone);
				}else{
					for(int i=0;i<plan4GParamList.size();i++){
						if(plan4GParamList.get(i).getCellName().equals(plan4GParam.getCellName())){
							replaceClass(plan4GParamList.get(i),plan4GParam);
						}
					}
				}
			}
		
			cellInfo.getPlan4GParams().size();
			cellInfo.getPlan4GParams().clear();
			for (Plan4GParam plan4GParam2 : plan4GParamList) {
				plan4GParam2.setCellInfo(null);
				plan4GParam2.setCellInfo(cellInfo);
			}
			
			int successNum = totalRowNum - failRowNum;
			cellInfo.getPlan4GParams().addAll(plan4GParamList);
			cellInfo.setPlanSheetName(importFileFileName.substring(0,importFileFileName.lastIndexOf(".")));
			cellInfo.setTotalCellNum(plan4GParamList.size());
			cellInfo.setImportDate(new Date());
			cellInfo.setLteCellGisFolderName(gisDrawCellPath);
			cellInfo.setRegion(terminalGroup.getName());
			if (!StringUtils.hasText(cellInfo.getUserName())) {
				String usename = (String) SecurityUtils.getSubject().getPrincipal();
				cellInfo.setUserName(usename);
			}
			
			String fileName = UUID.randomUUID().toString().replaceAll("-", "")
					.toUpperCase().trim();
			cellInfo.setLteCellGisFileName(fileName);
			cellInfoDao.update(cellInfo);
//			computeStationDistance.setOperatorType("4GPARAM");
//			computeStationDistance.setRegion(terminalGroup.getName());
//			computeStationDistance.setStationDistancePojoList(plan4GParamList);
//			new Thread(computeStationDistance).start();
	//		new Thread(new ComputeStationDistance(plan4GParamList,terminalGroup.getName(),"4GPARAM")).start();
			
			new Thread(new DrawGisLteCell(gisDrawCellPath, fileName,
					cellInfo.getId(), gisDrawCellUrl, gisDb,3)).start();
	
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(
					e.getMessage());
		}
		return new String[] { String.valueOf(totalRowNum), String.valueOf(failRowNum), errMsg};
		
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
    
    public static void replaceClass(Object class1,Object class2) throws Exception{
    	PropertyDescriptor[] fields = BeanUtils
				.getPropertyDescriptors(class1.getClass());
		Object reportTypeValue = null;
		for (PropertyDescriptor field : fields) {
			String fieldName = field.getName();
			reportTypeValue = ReflectUtil.getField(class1, fieldName);
			if(isEmpty(reportTypeValue)){
				reportTypeValue = ReflectUtil.getField(class2, fieldName);
				ReflectUtil.setField(class1,fieldName,reportTypeValue);
			}
		}
    }
	
	@Override
	public AbstractPageList doPageQuery(PageList pageList){
		return cellInfoDao.doPageQuery(pageList);
	}
	
	@Override
	public AbstractPageList doPageQueryParam5G(PageList pageList){
		return cellInfoDao.doPageQueryParam5G(pageList);
	}
	
	@Override
	public AbstractPageList doPageQueryParam4G(PageList pageList){
		return cellInfoDao.doPageQueryParam4G(pageList);
	}
	
	@Override
	public CellInfo findById(Long id){
		return cellInfoDao.find(id);
	}
	
	@Override
	public void createNrDistance(StationDistanceNRPojo stationDistanceNRPojo){
		stationDistanceNRDao.create(stationDistanceNRPojo);
	}
	
	@Override
	public void createLteDistance(StationDistanceLTEPojo stationDistanceLTEPojo){
		stationDistanceLTEDao.create(stationDistanceLTEPojo);
	}
	
	@Override
	public List<StationDistanceNRPojo> queryStationNRDistance(String region){
		return stationDistanceNRDao.queryStationNRDistance(region);
	}
	
	@Override
	public List<StationDistanceLTEPojo> queryStationLteDistance(String region){
		return stationDistanceLTEDao.queryStationLteDistance(region);
	}
	
	@Override
	public void deleteNrDistance(List<StationDistanceNRPojo> list){
		for (StationDistanceNRPojo stationDistanceNRPojo : list) {
			stationDistanceNRDao.delete(stationDistanceNRPojo);
		}
	}
	
	@Override
	public void deleteLteDistance(List<StationDistanceLTEPojo> list){
		for (StationDistanceLTEPojo stationDistanceLTEPojo : list) {
			stationDistanceLTEDao.delete(stationDistanceLTEPojo);
		}
	}
	
	@Override
	public Object getDistanceByCellName(String cellName,Integer coverType){
		return stationDistanceNRDao.getDistanceByCellName(cellName,coverType);
	}
	
	@Override
	public void deleteCellById(List<String> idsList){
		for (String id : idsList) {
			cellInfoDao.delete(Long.valueOf(id));
		}
	}

}
