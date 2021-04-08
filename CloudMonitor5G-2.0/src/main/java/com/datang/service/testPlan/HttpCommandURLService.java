package com.datang.service.testPlan;

import java.util.List;

import com.datang.domain.testPlan.HttpCommandURL;

/**
 * httpCommand测试命令中必须浏览地址和随机浏览地址
 * 
 * @author yinzhipeng 2015-04-23
 * 
 */
public interface HttpCommandURLService {

	/**
	 * 查询可添加的必选浏览地址
	 * 
	 * @param hasAddIds
	 *            已经添加过的必选地址id数组
	 * @return
	 */
	public List<HttpCommandURL> queryCanAddMustURL(List<Integer> hasAddIds);

	/**
	 * 查询可添加的随机浏览地址
	 * 
	 * @param hasAddIds
	 *            已经添加过的随机地址id数组
	 * @return
	 */
	public List<HttpCommandURL> queryCanAddRandomURL(List<Integer> hasAddIds);

	/**
	 * 根据url和类型查询网址信息
	 * 
	 * @param id
	 * @return
	 */
	public HttpCommandURL queryHttpCommandURL(String url, boolean isMustUrl);

	/**
	 * 根据id查询网址信息
	 * 
	 * @param id
	 * @return
	 */
	public HttpCommandURL queryHttpCommandURL(Integer id);

}
