package com.datang.common.tools.index;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datang.common.tools.annotation.Necessary;
import com.datang.common.tools.excel.ExcelReader;
import com.datang.common.tools.sql.ModelNameConstant;
import com.datang.common.util.IOUtils;
import com.datang.common.util.ReflectUtil;

public class IndexService {

	/**
	 * 需插入数据的数据库表名
	 */
	private String tableName = "IADS_KPI";

	private static Map<String, String> kpiTitles = new HashMap<String, String>();
	static {
		kpiTitles.put("指标序号", "id");
		kpiTitles.put("KPI编号", "kpiCode");
		kpiTitles.put("指标名称", "nameCh");
		kpiTitles.put("英文名称", "nameEn");
		kpiTitles.put("报表类型", "reportType");
		kpiTitles.put("一级分类", "stairClassify");
		// kpiTitles.put("二级分类", "stair2Classify");
		// kpiTitles.put("三级分类", "stair3Classify");
		kpiTitles.put("数据单位", "unit");
		// kpiTitles.put("计算公式", "formula");
		// kpiTitles.put("KPI定义", "kpiDefinition");
		kpiTitles.put("英文名称(开发使用)", "alias");
		kpiTitles.put("英文公式(开发使用)", "summaryFormula");
		kpiTitles.put("visible", "visible");
		kpiTitles.put("英文汇总公式(开发使用)", "totalSummaryFormula");
		// kpiTitles.put("公式备注", "formulaRemark");
	}

	/**
	 * LOG
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(IndexService.class);

	/**
	 * Excel解析器
	 */
	private ExcelReader reader;

	/**
	 * @param reader
	 */
	public IndexService() {
		super();
	}

	public IndexService(String tableName) {
		super();
		this.tableName = tableName;
	}

	/**
	 * 1. 从EXCEL中读取数据生成指标模型实体<IndexModel>,然后生成SQL语句,写入文件
	 */
	public void createSqlFromBean(String sqlFile, String[][] xlsFile) {
		Writer writer = null;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(sqlFile);
			writer = new OutputStreamWriter(fileOutputStream,
					Charset.forName("UTF-8"));
			writer.write(buildSql(xlsFile));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			IOUtils.close(writer);
			IOUtils.close(fileOutputStream);
		}
	}

	/**
	 * 从EXCEL中读取数据生成指标模型实体<IndexModel>,然后生成SQL语句
	 * 
	 * @return
	 */
	public String buildSql(String[][] xlsFile) {

		// 返回结果
		StringBuilder sql = new StringBuilder();
		for (String[] files : xlsFile) {
			// excel路径和名称
			String xls = files[0];
			String[] sheetNames = null;
			if (files.length > 1) {
				sheetNames = new String[files.length - 1];
				sheetNames = Arrays.copyOfRange(files, 1, files.length);
			}
			// 当sheet名称为null时
			if (null == sheetNames) {
				// 读取Excel文件中第一行为Bean的属性名
				List<String> fieldNames = getFieldNames(xls, null);
				// 根据Excel中数据和实体Bean的属性名列表,构造实体Bean列表
				List<Index> indexs = buildIndexBeans(fieldNames, xls, null);
				// 根据Excel第一行属性名列表来 构造INSERT语句前缀
				StringBuilder insertPrefix = buildInsertPrefix(fieldNames);
				for (Index index : indexs) {
					// 根据Excel第一行属性名列表获取<Index>实体Bean对象里对应属性的值来构造INSERT语句后缀
					StringBuilder insertSuffix = buildInsertSuffix(fieldNames,
							index);
					// 如果insertSuffix为空,则表明该行数据非法,则跳过该行
					if (0 == insertSuffix.length())
						continue;
					sql.append(insertPrefix);
					sql.append(insertSuffix);
					// 回车换行
					sql.append('\r');
					sql.append('\n');
				}

			} else {// 当sheet名称不为null时

				for (int j = 0; j < sheetNames.length; j++) {
					// 读取Excel文件中第一行为Bean的属性名
					List<String> fieldNames = getFieldNames(xls, sheetNames[j]);

					// 根据Excel中数据和实体Bean的属性名列表,构造实体Bean列表
					List<Index> indexs = buildIndexBeans(fieldNames, xls,
							sheetNames[j]);

					// 根据Excel第一行属性名列表来 构造INSERT语句前缀
					StringBuilder insertPrefix = buildInsertPrefix(fieldNames);
					for (Index index : indexs) {
						// 根据Excel第一行属性名列表获取<Index>实体Bean对象里对应属性的值来构造INSERT语句后缀
						StringBuilder insertSuffix = buildInsertSuffix(
								fieldNames, index);
						// 如果insertSuffix为空,则表明该行数据非法,则跳过该行
						if (0 == insertSuffix.length())
							continue;
						sql.append(insertPrefix);
						sql.append(insertSuffix);
						// 回车换行
						sql.append('\r');
						sql.append('\n');
					}
				}

			}

		}
		return sql.toString();
	}

	/**
	 * 根据Excel第一行属性名列表来 构造INSERT语句前缀:insert into tb_kpi
	 * (nameEn,nameDb,objFormula,timeFormula) values(...)
	 * 
	 * @param fieldNames
	 * @return
	 */
	private StringBuilder buildInsertPrefix(List<String> fieldNames) {

		StringBuilder insertPrefix = new StringBuilder("INSERT INTO "
				+ tableName + "(");

		for (String fieldName : fieldNames) {
			if (!fieldName.isEmpty())
				insertPrefix.append(fieldName + ",");
		}
		insertPrefix.setCharAt(insertPrefix.lastIndexOf(","), ')');
		insertPrefix.append(" values(");
		return insertPrefix;
	}

	/**
	 * 根据Excel第一行属性名列表获取<Index>实体Bean对象里对应属性的值来
	 * 构造INSERT语句后缀:'1','rrcreqsucnum','I1','Sum(rrcreqsucnum)',...,'-1'
	 * 
	 * @param fieldNames
	 * @param index
	 * @return
	 */
	private StringBuilder buildInsertSuffix(List<String> fieldNames, Index index) {

		StringBuilder insertSuffix = new StringBuilder();

		for (String fieldName : fieldNames) {
			if (!fieldName.isEmpty()) {
				try {
					Field field = index.getClass().getDeclaredField(fieldName);
					boolean hasNecessary = field
							.isAnnotationPresent(Necessary.class);
					Object fieldValue = ReflectUtil.getField(index, fieldName);
					if (null == fieldValue
							|| 0 == fieldValue.toString().length()) {
						if (hasNecessary)
							// 只要有必填字段为空,则表明该行数据非法
							return new StringBuilder();
						else
							insertSuffix.append("'',");
					}

					else {
						insertSuffix.append("'" + fieldValue + "',");
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}

		}
		insertSuffix.setCharAt(insertSuffix.lastIndexOf(","), ')');
		insertSuffix.append(';');
		return insertSuffix;
	}

	/**
	 * 读取Excel文件中第一行为Bean的属性名
	 * 
	 * @return
	 */
	private List<String> getFieldNames(String xls, String subSheetName) {
		if (null == reader)
			openReader(xls);
		// 读取Excel文件中第一行为Bean的属性名
		List<String> titles = new ArrayList<String>();
		List<String> fieldNames = new ArrayList<String>();

		// 从EXCEL中读取数据生成Index实体列表
		String sheetName = new String();
		if (null == subSheetName || subSheetName.isEmpty()) {
			sheetName = reader.goToSheet(0);
		} else {
			sheetName = reader.goToSheet(subSheetName);
		}
		if (null != sheetName && !sheetName.isEmpty()) {
			if (reader.hasNext())
				titles = reader.next();

			for (String title : titles) {
				if (kpiTitles.containsKey(title)) {
					fieldNames.add(kpiTitles.get(title));
				}
			}
		}
		closeReader();
		return fieldNames;
	}

	/**
	 * 根据Excel中数据和实体Bean的属性名列表,构造实体Bean列表
	 * 
	 * @param fieldNames
	 * @return
	 */
	private List<Index> buildIndexBeans(List<String> fieldNames, String xls,
			String subSheetName) {

		// 返回结果
		List<Index> indexs = new ArrayList<Index>();

		// Excel中每行的数据
		List<String> rowData = new ArrayList<String>();

		// 如果解析器没打开,则需要打开解析器并跳过第一行
		if (null == reader) {
			openReader(xls);
		}

		String sheetName = new String();
		// 从EXCEL中读取数据生成Index实体列表
		if (null == subSheetName || subSheetName.isEmpty()) {
			sheetName = reader.goToSheet(0);
		} else {
			sheetName = reader.goToSheet(subSheetName);
		}
		if (null != sheetName && !sheetName.isEmpty()) {
			if (reader.hasNext())
				reader.next();
			while (reader.hasNext()) {
				rowData = reader.next();

				try {
					if (0 == rowData.size())
						continue;
					Index index = Index.class.newInstance();
					for (int i = 0; i < fieldNames.size(); i++) {
						if (!fieldNames.get(i).isEmpty()) {
							try {
								if (i < rowData.size()) {
									ReflectUtil.setField(index,
											fieldNames.get(i), rowData.get(i));
								}
							} catch (Exception e) {
								LOG.error(e.getMessage(), e);
							}
						}
					}
					indexs.add(index);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
		closeReader();
		return indexs;
	}

	/**
	 * 打开Excel解析器
	 */
	private void openReader(String xls) {
		reader = new ExcelReader(xls);
	}

	/**
	 * 关闭Excel解析器
	 */
	private void closeReader() {
		reader.close();
		reader = null;
	}

	/**
	 * @return the reader
	 */
	public ExcelReader getReader() {
		return reader;
	}

	/**
	 * @param reader
	 *            the reader to set
	 */
	public void setReader(ExcelReader reader) {
		this.reader = reader;
	}

	/**
	 * 1. KPI
	 */
	private static void createKpiSql() {
		IndexService s = new IndexService("IADS_KPI");
		/**
		 * Oracle 脚本
		 */
		s.createSqlFromBean(FileConstant.VOLTE_KPIS_SQL, new String[][] { {
				FileConstant.VOLTE_KPIS_XLS, ModelNameConstant.VoLTE_KPI } });
	}

	/**
	 * 1. 5G KPI
	 */
	private static void create5gKpiSql() {
		IndexService s = new IndexService("IADS_KPI");
		/**
		 * Oracle 脚本
		 */
		s.createSqlFromBean(FileConstant.FG_KPIS_SQL, new String[][] { {
				FileConstant.FG_KPIS_XLS, ModelNameConstant.VoLTE_KPI } });
	}

	public static void main(String[] args) {
		// 生成SQL文件
		// 1. KPI
		// createKpiSql();
		create5gKpiSql();
	}
}
