/**
 * 
 */
package com.datang.util;

import com.datang.constant.CellSQLConstant;
import com.datang.constant.SqlConstant;

/**
 * Sql创建工具类
 * 
 * @author yinzhipeng
 * @date:2015年11月25日 上午9:59:58
 * @version
 */
public class SqlCreateUtils {
	/**
	 * 生成小区SQL
	 * 
	 * @param cellInfoId
	 *            小区信息ID
	 * @return
	 */
	public static String creatCellSql(Long cellInfoId) {
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
		/**
		 * 2016-06-27增加mod(tac,10)
		 */
		// MOD(THIS1.TAC,10) AS MOD_TAC
		buffer.append(",");
		buffer.append(SqlConstant.MOD);
		buffer.append("(");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_TAC);
		buffer.append(",10)");
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.MODEL_TAC);
		buffer.append(SqlConstant.SPACE);
		//THIS1.ANT_HIGH AS height
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_ANT_HIGH);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.HEIGHT);
		buffer.append(SqlConstant.SPACE);
		//THIS1.REGION AS region
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_REGION);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.AS);
		buffer.append(SqlConstant.SPACE);
		buffer.append(CellSQLConstant.REGION);
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
		// AND THIS2.ID={cellInfoId}
		buffer.append(SqlConstant.AND);
		buffer.append(SqlConstant.SPACE);
		buffer.append(SqlConstant.THIS2);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_INFO_ID);
		buffer.append(SqlConstant.SPACE);
		buffer.append("=");
		buffer.append(cellInfoId + "");
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
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_ANT_HIGH);
		buffer.append(",");
		buffer.append(SqlConstant.THIS1);
		buffer.append(".");
		buffer.append(CellSQLConstant.CELL_REGION);
		return buffer.toString();
	}

	public static void main(String[] args) {
		System.out.println(creatCellSql(1l));
	}
}
