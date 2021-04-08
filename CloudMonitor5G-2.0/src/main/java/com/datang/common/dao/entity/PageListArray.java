package com.datang.common.dao.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 非具体Bean的数据结果集. 数据形式一般为<Object[]>
 * 
 * @author zhangchongfeng
 * 
 */
public class PageListArray<T> extends Page<T> implements ArrayResult<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1735373742371979847L;

	/**
	 * 根据查询指标的顺序,进行调整后的表头列表
	 */
	private List<String> titles = new ArrayList<String>();

	/**
	 * 原始顺序的属性名列表
	 */
	private List<String> originalFields = new ArrayList<String>();

	/**
	 * 按查询指标的顺序,进行调整后的属性名列表
	 */
	private List<String> fields = new ArrayList<String>();

	/**
	 * 按查询指标的顺序,进行调整后的数据在数组中的索引列表
	 */
	private List<Integer> arrayIndexs = new ArrayList<Integer>();

	/**
	 * 数据列表
	 */
	private List<T> datas = new ArrayList<T>();

	/**
	 * 
	 */
	public PageListArray() {
		super();
	}

	/**
	 * @param p
	 */
	public PageListArray(PageListArray<T> p) {
		super();
		this.titles = p.titles;
		this.fields = p.fields;
		this.arrayIndexs = p.arrayIndexs;
		this.datas = p.datas;
	}

	/**
	 * 向数据源中添加一列信息
	 * 
	 * @param title
	 *            列名
	 * @param field
	 *            列字段名
	 */
	@SuppressWarnings("unchecked")
	public void addColumn(String title, String field) {
		if (0 != datas.size()) {
			if (datas.get(0) instanceof Object[]) {
				List<Object[]> orginalDatas = (List<Object[]>) datas;
				arrayIndexs.add(orginalDatas.get(0).length + 1);
				while (originalFields.size() < orginalDatas.get(0).length) {
					originalFields.add("");
				}
				originalFields.add(field);
				titles.add(title);
				fields.add(field);

				for (int i = 0; i < orginalDatas.size(); i++) {
					Object[] data = new Object[orginalDatas.get(i).length + 2];
					System.arraycopy(orginalDatas.get(i), 0, data, 0,
							orginalDatas.get(i).length);
					datas.set(i, (T) data);
				}
			}
		} else {
			originalFields.add(field);
			titles.add(title);
			fields.add(field);
			arrayIndexs.add(originalFields.size() - 1);
		}
	}

	/**
	 * 向数据源中的某一个字段后添加一列信息
	 * 
	 * @param title
	 *            列名
	 * @param field
	 *            列字段名 *
	 * @param location
	 *            需要插入到该字段之后
	 */
	@SuppressWarnings("unchecked")
	public void addColumnAfterLocation(String title, String field,
			String location) {
		if (0 != datas.size()) {
			int locationIndex = fields.indexOf(location);
			if (datas.get(0) instanceof Object[]) {
				// fields
				fields.add(locationIndex + 1, field);
				// titles
				titles.add(locationIndex + 1, title);
				// originalFields
				List<Object[]> orginalDatas = (List<Object[]>) datas;
				while (originalFields.size() < orginalDatas.get(0).length) {
					originalFields.add("");
				}
				originalFields.add(field);
				originalFields.add("");
				// arrayIndexs
				arrayIndexs.add(locationIndex + 1,
						orginalDatas.get(0).length + 1);
				// datas
				for (int i = 0; i < orginalDatas.size(); i++) {
					Object[] data = new Object[orginalDatas.get(i).length + 2];
					System.arraycopy(orginalDatas.get(i), 0, data, 0,
							orginalDatas.get(i).length);
					datas.set(i, (T) data);
				}
			}
		} else {
			int locationIndex = fields.indexOf(location);
			fields.add(locationIndex + 1, field);
			titles.add(locationIndex + 1, title);
			originalFields.add(field);
			arrayIndexs.add(locationIndex + 1, originalFields.size() - 1);
		}

	}

	/**
	 * 把数据源中的某一个字段移动带指定列之后
	 * 
	 * @param title
	 *            列名
	 * @param field
	 *            列字段名 *
	 * @param location
	 *            需要插入到该字段之后
	 */
	@SuppressWarnings("unchecked")
	public void moveColumnAfterLocation(String field, String location) {
		if (0 != datas.size()) {
			int fieldIndex = fields.indexOf(field);
			int locationIndex = fields.indexOf(location);
			if (-1 == locationIndex || -1 == fieldIndex
					|| fieldIndex == locationIndex)
				return;
			if (locationIndex < fields.size() - 1)
				locationIndex++;
			if (datas.get(0) instanceof Object[]) {
				// originalFields
				List<Object[]> orginalDatas = (List<Object[]>) datas;
				while (originalFields.size() < orginalDatas.get(0).length) {
					originalFields.add("");
				}

				String title = titles.get(fieldIndex);
				String originalField = originalFields.get(fieldIndex);
				Integer arrayIndex = arrayIndexs.get(fieldIndex);

				// fields
				fields.remove(fieldIndex);
				fields.add(locationIndex, field);
				// titles
				titles.remove(fieldIndex);
				titles.add(locationIndex, title);
				// originalFields
				originalFields.remove(fieldIndex);
				originalFields.add(locationIndex, originalField);
				// arrayIndexs
				arrayIndexs.remove(fieldIndex);
				arrayIndexs.add(locationIndex, arrayIndex);

			}
		} else {
			int fieldIndex = fields.indexOf(field);
			int locationIndex = fields.indexOf(location);
			if (-1 == locationIndex || -1 == fieldIndex
					|| fieldIndex == locationIndex)
				return;
			if (locationIndex < fields.size() - 1)
				locationIndex++;
			String title = titles.get(fieldIndex);
			String originalField = originalFields.get(fieldIndex);
			Integer arrayIndex = arrayIndexs.get(fieldIndex);

			// fields
			fields.remove(fieldIndex);
			fields.add(locationIndex, field);
			// titles
			titles.remove(fieldIndex);
			titles.add(locationIndex, title);
			// originalFields
			originalFields.remove(fieldIndex);
			originalFields.add(locationIndex, originalField);
			// arrayIndexs
			arrayIndexs.remove(fieldIndex);
			arrayIndexs.add(locationIndex, arrayIndex);
		}

	}

	/**
	 * 根据字段名获取数组索引
	 * 
	 * @param fieldName
	 *            字段名称
	 * @return 对象数组索引
	 */
	public int getIndex(String fieldName) {
		int i = fields.indexOf(fieldName);
		return i < 0 ? i : arrayIndexs.get(i);
	}

	/**
	 * 根据字段名获取数组索引
	 * 
	 * @param fieldName
	 *            字段名称
	 * @return 对象数组索引
	 */
	public int getOldIndex(String fieldName) {
		return originalFields.indexOf(fieldName);
	}

	/**
	 * @return the fields
	 */
	public List<String> getFields() {
		return fields;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	/**
	 * @return the titles
	 */
	public List<String> getTitles() {
		return titles;
	}

	/**
	 * @param titles
	 *            the titles to set
	 */
	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

	/**
	 * @return the arrayIndexs
	 */
	public List<Integer> getArrayIndexs() {
		return arrayIndexs;
	}

	/**
	 * @param arrayIndexs
	 *            the arrayIndexs to set
	 */
	public void setArrayIndexs(List<Integer> arrayIndexs) {
		this.arrayIndexs = arrayIndexs;
	}

	/**
	 * @return the datas
	 */
	public List<T> getDatas() {
		return datas;
	}

	/**
	 * @param datas
	 *            the datas to set
	 */
	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	/**
	 * @return the originalFields
	 */
	public List<String> getOriginalFields() {
		return originalFields;
	}

	/**
	 * @param originalFields
	 *            the originalFields to set
	 */
	public void setOriginalFields(List<String> originalFields) {
		this.originalFields = originalFields;
	}

	/**
	 * 字符串显示该对象
	 * 
	 * @return toString
	 */
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("TITLE  :");
		for (String title : titles) {
			s.append(title + '\t');
		}
		s.append('\n');
		s.append("FIELD  :");
		for (String field : fields) {
			s.append(field + '\t');
		}
		s.append('\n');
		s.append("OFIELD :");
		for (String originalField : originalFields) {
			s.append(originalField + '\t');
		}
		s.append('\n');
		s.append("INDEX  :");
		for (Integer index : arrayIndexs) {
			s.append(index.toString() + '\t');
		}
		s.append('\n');

		for (T data : datas) {
			s.append("DATA   :");
			if (data instanceof Object[]) {
				Object[] os = (Object[]) data;
				for (Object o : os) {
					if (null != o)
						s.append(o.toString() + '\t');
					else
						s.append("null" + '\t');
				}
			}
			s.append('\n');
		}
		s.append('\n');

		return s.toString();
	}

	public static void main(String[] args) {
		PageListArray<Object[]> r = new PageListArray<Object[]>();
		r.getDatas().add(
				new Object[] { "rnc11", "cell11", "11.1", "22.2", "33.3" });
		r.getFields().add("rncid");
		r.getFields().add("cellid");
		r.getFields().add("kpi2");
		r.getFields().add("kpi1");
		r.getFields().add("kpi3");
		r.getOriginalFields().add("rncid");
		r.getOriginalFields().add("cellid");
		r.getOriginalFields().add("kpi2");
		r.getOriginalFields().add("kpi1");
		r.getOriginalFields().add("kpi3");
		r.getTitles().add("RNC ID");
		r.getTitles().add("CELL ID");
		r.getTitles().add("KPI2");
		r.getTitles().add("KPI1");
		r.getTitles().add("KPI3");
		r.getArrayIndexs().add(0);
		r.getArrayIndexs().add(1);
		r.getArrayIndexs().add(3);
		r.getArrayIndexs().add(2);
		r.getArrayIndexs().add(4);
		System.out.println(r);

		// r.addColumnAfterLocation("新列", "new", "kpi1");
		// int ratioIndex = r.getOldIndex("new");// 比率在结果数据集中的索引位置
		// int ratioStringIndex = r.getIndex("new");// 比率*100%在结果数据集中的索引位置
		// r.getDatas().get(0)[ratioIndex] = 1;
		// r.getDatas().get(0)[ratioStringIndex] = "100%";

		r.moveColumnAfterLocation("kpi1", "kpi3");
		System.out.println(r);
	}

}
