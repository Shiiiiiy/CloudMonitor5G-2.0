package com.datang.web.action;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datang.common.util.SslUtils;
import com.datang.constant.RailWayHttpConstants;
import com.datang.domain.railway.TrainTimeInterfacePojo;
import com.datang.util.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

/**
 * 测试类
 * @author lucheng
 * @date 2020年12月23日 下午1:01:23
 */
public class TestUtilClass {

	public static void main(String[] args) throws URISyntaxException {
//		StringBuffer uri = new StringBuffer(RailWayHttpConstants.TRAIN_12306_QUERY_REALTIME);
//		uri.append("query").append("?");
//		//https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date=2021-11-25&leftTicketDTO.from_station=BJP&
//		//leftTicketDTO.to_station=SHH&purpose_codes=ADULT
//		uri.append("leftTicketDTO.train_date=").append("2021-11-25");
//		uri.append("&leftTicketDTO.from_station=").append("BJP");
//		uri.append("&leftTicketDTO.to_station=").append("SHH");
//		uri.append("&purpose_codes=ADULT");
//		String resultString = HttpClientUtils.httpGet(uri.toString());
//		System.out.println(resultString);

		CloseableHttpClient httpClient = HttpClients.createDefault();

		String from_code = "BJP";
		String ro_code = "SHH";
		//uri的构造器
		URI uri = new URIBuilder()
				.setScheme("https")
				.setHost("kyfw.12306.cn")
				.setPath("/otn/leftTicket/query")
				.setParameter("leftTicketDTO.train_date", "2021-11-25") //2018-05-04
				.setParameter("leftTicketDTO.from_station", from_code)
				.setParameter("leftTicketDTO.to_station", ro_code)
				.setParameter("purpose_codes", "ADULT")
				.build();
		// HttpGet httpGet = new HttpGet("https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date=2018-05-03&leftTicketDTO.from_station=HKN&leftTicketDTO.to_station=HAN&purpose_codes=ADULT");
		System.out.println(uri);
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
		httpGet.setHeader("Cookie","_uab_collina=WX:xiaosin2333; JSESSIONID=WX:xiaosin2333;");
		//System.out.println(httpGet.getURI());
		List<Map<String,String>> list = new ArrayList<>();
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			//System.out.println(entity);
			String message = EntityUtils.toString(entity);
			if(org.apache.commons.lang3.StringUtils.isNotBlank(message)&& message.contains("data")) {
				JSONObject result = JSON.parseObject(message);
				if(!result.isEmpty() && (result.getIntValue("httpstatus") == 200 && result.containsKey("data"))){
					JSONObject jsonData = result.getJSONObject("data");
					if(!jsonData.isEmpty() && jsonData.containsKey("result")){
						JSONObject jsonMap = jsonData.getJSONObject("map");
						JSONArray resultArray = jsonData.getJSONArray("result");
						for(int index = 0;index < resultArray.size(); index++){
							String data = resultArray.get(index).toString();
							if(data.contains("|预订|") && data.contains("|Y|")){
								Map<String,String> map = new HashMap<>();
								int locationIndex2 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 2);
								int locationIndex3 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 3);
								int locationIndex4 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 4);
								int locationIndex5 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 5);
								int locationIndex6 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 6);
								int locationIndex7 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 7);
								int locationIndex8 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 8);
								int locationIndex9 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 9);
								int locationIndex10 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 10);
								int locationIndex11 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 11);

								String trainNo = data.substring(locationIndex2 + 1, locationIndex3); //车次编码
								String trainCode = data.substring(locationIndex3 + 1, locationIndex4); //车次
								String startStation = data.substring(locationIndex4 + 1, locationIndex5); //始发站
								String endStation = data.substring(locationIndex5 + 1, locationIndex6); //终点站
								String beginStation = data.substring(locationIndex6 + 1, locationIndex7); //出发地
								String arriveStation = data.substring(locationIndex7 + 1, locationIndex8); //目的地
								String beginTime = data.substring(locationIndex8 + 1, locationIndex9); //开始时间
								String endTime = data.substring(locationIndex9 + 1, locationIndex10); //结束时间
								String overTime = data.substring(locationIndex10 + 1, locationIndex11); //时长
								map.put("trainNo",trainNo);
								map.put("trainCode",trainCode);
								map.put("startStation",startStation);
								map.put("endStation",endStation);
								map.put("beginStation",beginStation);
								map.put("arriveStation",arriveStation);
								map.put("beginTime",beginTime);
								map.put("endTime",endTime);
								map.put("overTime",overTime);
								list.add(map);
							}
						}
					}
				}
			}
			System.out.println(list);


			response.close();
			httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过递归的方式罗列出所有的排列结果
	 * @param a：初始数组
	 * @param result：排列数组初始状态
	 * @param resultIndex：比较的起始索引
	 */
	public static void arrangementSort(String[] a, String[] result, int resultIndex,List<List<String>> list){
		int result_length = result.length;
		if(resultIndex >= result_length){
			list.add(new ArrayList<>(Arrays.asList(result)));
			System.out.println(Arrays.toString(result));
			return;
		}
		for(int i=0; i<a.length; i++){
			// 判断待选的数是否存在于排列的结果中
			boolean exist = false;
			for(int j=0; j<resultIndex; j++){
				if(a[i] == result[j]){  // 若已存在，则不能重复选
					exist = true;
					break;
				}
			}
			if(!exist){  // 若不存在，则可以选择
				result[resultIndex] = a[i];
				arrangementSort(a, result, resultIndex+1,list);
			}
		}
	}


	/**
	 * 提取中括号中内容，忽略中括号中的中括号
	 * @param msg
	 * @return
	 */
	public static List<String> extractMessage(String msg) {
 
		List<String> list = new ArrayList<String>();
		int start = 0;
		int startFlag = 0;
		int endFlag = 0;
		for (int i = 0; i < msg.length(); i++) {
			if (msg.charAt(i) == '<') {
				startFlag++;
				if (startFlag == endFlag + 1) {
					start = i;
				}
			} else if (msg.charAt(i) == '>') {
				endFlag++;
				if (endFlag == startFlag) {
					list.add(msg.substring(start + 1, i));
				}
			}
		}
		return list;
	}


}
