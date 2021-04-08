/**
 * 
 */
package com.datang.web.action.VoLTEDissertation.qualityBadRoad;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zeromq.ZMQ.Socket;

import com.datang.common.util.StringUtils;
import com.datang.util.ZMQUtils;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * volte质量专题---volte语音质差--其他问题路段信令解码
 * 
 * @author yinzhipeng
 * @date:2016年1月5日 下午1:42:30
 * @version
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class VoiceQualityBadRoadSignllingAction extends ActionSupport {
	@Value("${decode.signalling.ip}")
	private String decodeIp;
	@Value("${decode.signalling.port}")
	private String decodePort;

	/**
	 * 信令码流
	 */
	private String stream;
	private String stream2;

	public String signllingStreamDecode() {

		Map<String, String> map = new HashMap<>();
		if (StringUtils.hasText(stream2)) {
			stream += stream2;
		}
		map.put("asn", stream);
		JSONObject requJson = new JSONObject();
		requJson.put("getASNDetail", map);
		String request = requJson.toString();
		Socket socket = ZMQUtils.getZMQSocket();
		try {
			socket.setReceiveTimeOut(1300);
			socket.connect("tcp://" + decodeIp + ":" + decodePort); // 与response端建立连接
			socket.send(request.getBytes()); // 向reponse端发送数据
			byte[] responseBytes = socket.recv(); // 接收response发送回来的数据
			if (null == responseBytes) {
				socket.close();
				ActionContext.getContext().getValueStack()
						.set("errorMsg", "解码失败:Rece Null Error");
			} else {
				ZMQUtils.releaseZMQSocket(socket);
				String detail = null;
				String response = new String(responseBytes, "UTF8");
				JSONObject respJson = JSONObject.fromObject(response);
				if (null != respJson) {
					JSONObject getASNDetail = respJson
							.getJSONObject("getASNDetail");
					if (null != getASNDetail) {
						detail = getASNDetail.getString("detail");
					}
				}
				if (StringUtils.hasText(detail)) {
					String detailReplace = detail.trim()
							.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
							.replaceAll("\r|\n", "<br>");
					String replacDetaile = detailReplace.replaceAll(" ",
							"&nbsp;");
					ActionContext.getContext().getValueStack()
							.push(replacDetaile);
				} else {
					ActionContext.getContext().getValueStack()
							.set("errorMsg", "解码失败:Detail Null Error");
				}
			}
		} catch (Exception e) {
			socket.close();
			ActionContext.getContext().getValueStack()
					.set("errorMsg", "解码失败:Send Fail Error");
		}
		return ReturnType.JSON;
	}

	/**
	 * @return the streamstream
	 */
	public String getStream() {
		return stream;
	}

	/**
	 * @param stream
	 *            the stream to set
	 */
	public void setStream(String stream) {
		this.stream = stream;
	}

	/**
	 * @return the decodeIpdecodeIp
	 */
	public String getDecodeIp() {
		return decodeIp;
	}

	/**
	 * @param decodeIp
	 *            the decodeIp to set
	 */
	public void setDecodeIp(String decodeIp) {
		this.decodeIp = decodeIp;
	}

	/**
	 * @return the decodePortdecodePort
	 */
	public String getDecodePort() {
		return decodePort;
	}

	/**
	 * @param decodePort
	 *            the decodePort to set
	 */
	public void setDecodePort(String decodePort) {
		this.decodePort = decodePort;
	}

	/**
	 * @return the stream2stream2
	 */
	public String getStream2() {
		return stream2;
	}

	/**
	 * @param stream2
	 *            the stream2 to set
	 */
	public void setStream2(String stream2) {
		this.stream2 = stream2;
	}

}
