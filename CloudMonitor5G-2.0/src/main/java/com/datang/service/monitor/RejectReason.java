package com.datang.service.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * @explain
 * @name RejectReason
 * @author shenyanwei
 * @date 2016年7月11日下午2:24:20
 */
public class RejectReason {
	private static final Map<String, String[]> rejectReasonMap = new HashMap<String, String[]>();
	static {
		rejectReasonMap.put("00", new String[] { "Normal", "成功，没有错误",
				"如果成功，可以不下发Code", "ALL" });
		rejectReasonMap.put("01", new String[] { "Configuration error", "配置错误",
				"如数据库配置错误等，无法找到数据，无法验证信息，设置不正确等", "ALL" });
		rejectReasonMap.put("02", new String[] { "Invalid Device ID", "终端ID非法",
				"设备终端ID在服务器上找不到", "Login" });
		rejectReasonMap.put("03", new String[] { "Wrong Password", "密码错误",
				"密码不匹配", "Login" });
		rejectReasonMap.put("04", new String[] { "Already logon", "已经登录",
				"终端已经登录，重复登录了", "Login" });
		rejectReasonMap.put("05", new String[] { "Not logon", "终端未登录",
				"终端必须在Login指令之后才能继续其他的指令", "除Login外" });
		rejectReasonMap.put("06", new String[] { "Unknown command", "未知的指令",
				"服务器不支持相关指令，可能是错误指令也可能是服务器版本较老", "ALL" });
		rejectReasonMap.put("07", new String[] { "Upgrade file missing",
				"升级文件丢失", "终端软件升级包文件丢失，不存在", "Upgrade" });
		rejectReasonMap.put("08", new String[] { "Invalid Data", "无效数据",
				"非法的，无效的，不可解释的数据", "Alarm, GPS, Event, MOS, Status" });
		rejectReasonMap.put("09", new String[] { "Invalid Packet", "无效数据包",
				"数据包或者命令包不符合格式", "ALL" });
		rejectReasonMap.put("0A", new String[] { "File open error", "打开文件失败",
				"在Upload指令中打开对应数据文件失败", "Upload" });
		rejectReasonMap.put("0B", new String[] { "File close error", "关闭文件失败",
				"在Eof指令中关闭对应数据失败", "Eof" });
		rejectReasonMap.put("0C", new String[] { "Test Config Error", "测试配置错误",
				"测试配置非法，不存在，错误，冲突，格式，不对等等", "Config" });
	}

	/**
	 * 根据错误码获取错误涵义
	 * 
	 * @param codeWithout0x
	 * @return
	 */
	public static String getMeaningByCode(String codeWithout0x) {
		return rejectReasonMap.get(codeWithout0x)[1];
	}
}
