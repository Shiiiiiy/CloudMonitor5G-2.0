/**
 * 
 */
package com.datang.web.action.gisSql;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * rtp连续丢包路段的颜色配置
 * 
 * @explain
 * @name LostPacketGpsPointInfo
 * @author shenyanwei
 * @date 2017年2月21日上午11:00:05
 */
@Component
@Scope("singleton")
public class LostPacketGpsPointInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3408894004805093916L;
	@Value("${gis.lostPacket.sendUEColor}")
	private String sendUEColor;
	@Value("${gis.lostPacket.sendS1Color}")
	private String sendS1Color;
	@Value("${gis.lostPacket.sendSGiColor}")
	private String sendSGiColor;
	@Value("${gis.lostPacket.receUEColor}")
	private String receUEColor;
	@Value("${gis.lostPacket.receS1Color}")
	private String receS1Color;
	@Value("${gis.lostPacket.receSGiColor}")
	private String receSGiColor;

	/**
	 * 获取所有LP路段的颜色配置的形式
	 * 
	 * @param indexType
	 * @return
	 */
	public List<Map<String, String>> getColorListMap() {
		List<Map<String, String>> listmMaps = new LinkedList<>();
		// {
		// "qbrType" : "",//路段类型
		// "color" : "#ffffff"//该路段的颜色
		// }
		Map<String, String> sendUEColorMap = new LinkedHashMap<String, String>();
		sendUEColorMap.put("lpType", " sendUE");
		sendUEColorMap.put("lpTypeName", "发送端手机上行丢包");
		sendUEColorMap.put("color", sendUEColor);
		listmMaps.add(sendUEColorMap);
		Map<String, String> sendS1ColorMap = new LinkedHashMap<String, String>();
		sendS1ColorMap.put("lpType", "sendS1");
		sendS1ColorMap.put("lpTypeName", "发送端S1口丢包");
		sendS1ColorMap.put("color", sendS1Color);
		listmMaps.add(sendS1ColorMap);
		Map<String, String> sendSGiColorMap = new LinkedHashMap<String, String>();
		sendSGiColorMap.put("lpType", "sendSGi");
		sendSGiColorMap.put("lpTypeName", "发送端SGi口丢包");
		sendSGiColorMap.put("color", sendSGiColor);
		listmMaps.add(sendSGiColorMap);
		Map<String, String> receUEColorMap = new LinkedHashMap<String, String>();
		receUEColorMap.put("lpType", "receUE");
		receUEColorMap.put("lpTypeName", "接收端Uu口丢包");
		receUEColorMap.put("color", receUEColor);
		listmMaps.add(receUEColorMap);
		Map<String, String> receS1ColorMap = new LinkedHashMap<String, String>();
		receS1ColorMap.put("lpType", "receS1");
		receS1ColorMap.put("lpTypeName", "接收端S1口丢包");
		receS1ColorMap.put("color", receS1Color);
		listmMaps.add(receS1ColorMap);
		Map<String, String> receSGiColorMap = new LinkedHashMap<String, String>();
		receSGiColorMap.put("lpType", "receSGi");
		receSGiColorMap.put("lpTypeName", "接收端SGi口丢包");
		receSGiColorMap.put("color", receSGiColor);
		listmMaps.add(receSGiColorMap);
		return listmMaps;
	}

}
