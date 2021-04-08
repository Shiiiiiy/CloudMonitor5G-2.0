/**
 * 
 */
package com.datang.web.action.testLogItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.zeromq.ZMQ.Socket;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.security.User;
import com.datang.domain.testLogItem.NetworkTestLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.exception.ApplicationException;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.security.IUserService;
import com.datang.service.testLogItem.INetworkTestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.util.IoUtil;
import com.datang.util.ReadPcapUtils;
import com.datang.util.ZMQUtils;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.testLogItem.TestLogItemPageQueryRequestBean;
import com.datang.web.servlet.Range;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 网络侧日志action
 * 
 * @author yinzhipeng
 * @date:2017年2月6日 下午1:42:41
 * @version
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class NetworkTestLogItemAction extends PageAction implements
		ModelDriven<TestLogItemPageQueryRequestBean> {
	@Value("${decode.signalling.ip}")
	private String decodeIp;
	@Value("${decode.signalling.port}")
	private String decodePort;
	@Autowired
	private INetworkTestLogItemService networkTestLogItemService;
	/**
	 * 菜单管理服务
	 */
	@Autowired
	private IMenuManageService menuManageService;
	/**
	 * 区域管理服务
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;
	/**
	 * 用户管理服务
	 */
	@Autowired
	private IUserService userService;

	private NetworkTestLogItem networkTestLogItem = new NetworkTestLogItem();
	/**
	 * 测试日志筛选参数
	 */
	private TestLogItemPageQueryRequestBean testLogItemPageQueryRequestBean = new TestLogItemPageQueryRequestBean();

	private String name;
	private Long size;
	private String token;
	/**
	 * 网络侧文件类型（S1，SGI等）<br>
	 * 0:S1<br>
	 * 1:SGI<br>
	 */
	private Integer testFileType;

	/**
	 * box-id
	 */
	private String boxId;

	/**
	 * 模块号
	 */
	private String moduleNo;

	private File myfile;// 上传文件

	/**
	 * 跳转到网络侧日志list界面
	 * 
	 * @return
	 */
	public String networkTestLogItemListUI() {
		return ReturnType.LISTUI;
	}

	/**
	 * 跳转到导入网络侧日志界面
	 * 
	 * @return
	 */
	public String goImport() {
		return "import";
	}

	/**
	 * 生成上传日志文件用的token
	 * 
	 * @return
	 */
	public String token() {
		JSONObject json = new JSONObject();
		if (StringUtils.hasText(name)) {
			NetworkTestLogItem findByFileName = networkTestLogItemService
					.findByFileName(name);
			if (null == findByFileName) {
				json.put("token", name);
			}
		}
		json.put("success", true);
		json.put("message", "");
		ActionContext.getContext().getValueStack().push(json);
		return ReturnType.JSON;
	}

	/**
	 * 文件上传,结合前台JS上传,使用FLASH的多文件上传,JS多文件上传,实际是每次只传一个传完一个再传一个,
	 * 支持HTML5的浏览器结合HTML5H的上传请参见html5UploadFile
	 * 
	 * @return
	 */
	public String formUploadFile() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String uploadPath = request.getServletContext().getRealPath("/");
		File file = new File(uploadPath);
		File parentFile = file.getParentFile().getParentFile().getParentFile();
		String parentFilePath = parentFile.getAbsolutePath() + File.separator
				+ "NetworkTestLog";
		JSONObject json = new JSONObject();
		long start = 0;
		boolean success = true;
		String message = "";
		Long length = null;
		File f = null;
		try {
			f = IoUtil.getFile(token, parentFilePath);
			length = myfile.length();
			FileCopyUtils.copy(myfile, f);

		} catch (IOException fne) {
			message = "Error: " + fne.getMessage();
			success = false;
		}
		if (success) {
			Date beginDate = null;
			Date endDate = null;
			try {
				/**
				 * 读取文件解析开始时间和结束时间
				 */
				List<Date> readDates = ReadPcapUtils.readDates(f);
				if (null != readDates && 0 != readDates.size()) {
					beginDate = readDates.get(0);
					endDate = readDates.get(readDates.size() - 1);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			if (null != beginDate) {
				networkTestLogItem.setStartDateLong(beginDate.getTime());
			}
			if (null != endDate) {
				networkTestLogItem.setEndDateLong(endDate.getTime());
			}
			networkTestLogItem.setBoxId(boxId);
			networkTestLogItem.setModuleNo(moduleNo);
			networkTestLogItem.setTestFileType(testFileType);
			networkTestLogItem.setFilelink(parentFilePath + File.separator
					+ token);
			networkTestLogItem.setFileName(token);
			networkTestLogItem.setFtpByteSize(length);
			networkTestLogItem.setTestFileStatus(0);
			networkTestLogItem.setUploadedSize(length);
			networkTestLogItemService.addNetworkTestLogItem(networkTestLogItem);
			json.put("start", start);
		}
		json.put("success", success);
		json.put("massage", message);
		ActionContext.getContext().getValueStack().push(json);
		return ReturnType.JSON;
	}

	/**
	 * 文件上传,结合前台JS上传,使用HTML5的多文件上传,JS多文件上传,实际是每次只传一个每次传10M,传完一个再传一个,
	 * 不支持HTML5的浏览器结合FLASH的form上传请参见formUploadFile
	 * 
	 * @return
	 */
	public String html5UploadFile() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String uploadPath = request.getServletContext().getRealPath("/");
		String method = request.getMethod();
		File file = new File(uploadPath);
		File parentFile = file.getParentFile().getParentFile().getParentFile();
		String parentFilePath = parentFile.getAbsolutePath() + File.separator
				+ "NetworkTestLog";
		JSONObject json = new JSONObject();
		long start = 0;
		boolean success = true;
		String message = "";
		switch (method) {
		case "GET":
			try {
				if ("0".equals(size) && 0 == start) {
					// 文件大小为0
					message = "Error: 文件大小为0Byte!";
					success = false;
				} else {
					File f = IoUtil.getFile(token, parentFilePath);
					start = f.length();
				}
			} catch (IOException fne) {
				message = "Error: " + fne.getMessage();
				success = false;
			}
			break;
		case "POST":
			OutputStream out = null;
			InputStream content = null;
			try {
				Range range = IoUtil.parseRange(request);
				File f = IoUtil.getFile(token, parentFilePath);
				if (f.length() != range.getFrom()) {
					throw new IOException("File from position error!");
				}
				out = new FileOutputStream(f, true);
				content = request.getInputStream();
				int read = 0;
				byte[] bytes = new byte[10240];
				while ((read = content.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				start = f.length();
				if (range.getTo() == start && range.getTo() == range.getSize()) {
					Date beginDate = null;
					Date endDate = null;
					try {
						/**
						 * 读取文件解析开始时间和结束时间
						 */
						List<Date> readDates = ReadPcapUtils.readDates(f);
						if (null != readDates && 0 != readDates.size()) {
							beginDate = readDates.get(0);
							endDate = readDates.get(readDates.size() - 1);
						}
					} catch (Exception e) {
						System.out.println(e);
					}
					if (null != beginDate) {
						networkTestLogItem
								.setStartDateLong(beginDate.getTime());
					}
					if (null != endDate) {
						networkTestLogItem.setEndDateLong(endDate.getTime());
					}
					networkTestLogItem.setBoxId(boxId);
					networkTestLogItem.setModuleNo(moduleNo);
					networkTestLogItem.setTestFileType(testFileType);
					networkTestLogItem.setFilelink(parentFilePath
							+ File.separator + token);
					networkTestLogItem.setFileName(name);
					networkTestLogItem.setFtpByteSize(size);
					networkTestLogItem.setTestFileStatus(0);
					networkTestLogItem.setUploadedSize(size);
					networkTestLogItemService
							.addNetworkTestLogItem(networkTestLogItem);
				}
			} catch (IOException fne) {
				message = "Error: " + fne.getMessage();
				success = false;
			} finally {
				if (null != out) {
					try {
						out.close();
					} catch (IOException e) {
					}
				}
				if (null != content) {
					try {
						content.close();
					} catch (IOException e) {
					}
				}
			}
			break;
		default:
			break;
		}
		if (success)
			json.put("start", start);
		json.put("success", success);
		json.put("massage", message);
		ActionContext.getContext().getValueStack().push(json);
		return ReturnType.JSON;
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
		// System.out.println(testLogItemPageQueryRequestBean);
		Set<String> boxIdsList = testLogItemPageQueryRequestBean
				.getBoxIdsList();
		List<Long> cityIdsList = testLogItemPageQueryRequestBean
				.getCityIdsList();
		Set<String> boxIdsSet = testLogItemPageQueryRequestBean.getBoxIdsSet();
		if (0 == boxIdsList.size() && 0 == cityIdsList.size()) {// 默认没选中区域也没选中设备,则获取权限范围内的数据
			String username = (String) SecurityUtils.getSubject()
					.getPrincipal();
			User user = userService.findByUsername(username);
			if (!user.isPowerUser()) {
				List<String> terminalBoxIDs = menuManageService
						.getTerminalBoxIDs();
				if (null == terminalBoxIDs || 0 == terminalBoxIDs.size()) {// 用户权限范围内的设备为null
					return new EasyuiPageList();
				} else {
					boxIdsSet.addAll(terminalBoxIDs);
				}
			}
		} else if (0 == boxIdsList.size() && 0 != cityIdsList.size()) {// 选中区域没选中设备,则获取区域范围内的数据
			for (Long cityId : cityIdsList) {
				List<Terminal> allTerminalByGroupId = terminalGroupService
						.getAllTerminalByGroupId(cityId);
				for (Terminal terminal : allTerminalByGroupId) {
					boxIdsSet.add(terminal.getBoxId());
				}
			}
		} else {// 选中设备或者选中区域和设备,则获取设备范围内的数据
			boxIdsSet.addAll(boxIdsList);
		}
		pageList.putParam("pageQueryBean", testLogItemPageQueryRequestBean);
		return networkTestLogItemService.pageList(pageList);
	}

	/**
	 * 通知后台分析
	 * 
	 * @return
	 */
	public String goSocket() {
		// 请求：{"importpcap":{ "id":[1,2,3] // 数据库id } }
		// 应答：{ "importpcap":{ "result":0 } }
		// ServletContext application=this.getServletContext();
		ServletContext context = ServletActionContext.getRequest().getSession()
				.getServletContext();
		Object attribute = context.getAttribute("importpcapIds");
		// System.out.println(attribute);
		if (attribute != null) {
			String[] split = String.valueOf(attribute).split(",");
			ArrayList<Long> arrayList = new ArrayList<Long>();
			for (String str : split) {
				arrayList.add(Long.valueOf(str));
			}
			Map<String, Object> map = new HashMap<>();
			map.put("id", arrayList);
			JSONObject requJson = new JSONObject();
			requJson.put("importpcap", map);
			String request = requJson.toString();
			Socket socket = ZMQUtils.getZMQSocket();
			try {
				socket.setReceiveTimeOut(5000);
				socket.connect("tcp://" + decodeIp + ":" + decodePort); // 与response端建立连接
				socket.send(request.getBytes()); // 向reponse端发送数据
				byte[] responseBytes = socket.recv(); // 接收response发送回来的数据
				if (null == responseBytes) {
					throw new ApplicationException("后台通信异常!");
				} else {
					ZMQUtils.releaseZMQSocket(socket);
					Integer result = null;
					String response = new String(responseBytes, "UTF8");
					JSONObject respJson = JSONObject.fromObject(response);
					if (null != respJson) {
						JSONObject conftestplan = respJson
								.getJSONObject("importpcap");
						if (null != conftestplan) {
							result = conftestplan.getInt("result");
							// System.out.println(result);
						}
					}
					if (null == result) {
						throw new ApplicationException("后台通信异常!");
					} else {
						switch (result) {
						case 0:
							// 通信成功清空缓存ID
							context.setAttribute("importpcapIds", null);
							System.out.println("推送完毕！！！！");
							break;
						default:
							break;
						}
					}
				}
			} catch (Exception e) {
				if (e instanceof ApplicationException) {
					// throw new ApplicationException(e.getMessage());
					context.setAttribute("importpcapIds", null);
					System.out.println(e.getMessage());
				} else {
					socket.close();
					// throw new ApplicationException("后台通信异常!");
					context.setAttribute("importpcapIds", null);
					System.out.println("后台通信异常!");
				}
			}
		}
		return ReturnType.JSON;
	}

	/**
	 * @return the namename
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the sizesize
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(Long size) {
		this.size = size;
	}

	/**
	 * @return the tokentoken
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the myfilemyfile
	 */
	public File getMyfile() {
		return myfile;
	}

	/**
	 * @param myfile
	 *            the myfile to set
	 */
	public void setMyfile(File myfile) {
		this.myfile = myfile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public TestLogItemPageQueryRequestBean getModel() {
		return testLogItemPageQueryRequestBean;
	}

	/**
	 * @return the networkTestLogItemnetworkTestLogItem
	 */
	public NetworkTestLogItem getNetworkTestLogItem() {
		return networkTestLogItem;
	}

	/**
	 * @param networkTestLogItem
	 *            the networkTestLogItem to set
	 */
	public void setNetworkTestLogItem(NetworkTestLogItem networkTestLogItem) {
		this.networkTestLogItem = networkTestLogItem;
	}

	/**
	 * @return the
	 *         testLogItemPageQueryRequestBeantestLogItemPageQueryRequestBean
	 */
	public TestLogItemPageQueryRequestBean getTestLogItemPageQueryRequestBean() {
		return testLogItemPageQueryRequestBean;
	}

	/**
	 * @param testLogItemPageQueryRequestBean
	 *            the testLogItemPageQueryRequestBean to set
	 */
	public void setTestLogItemPageQueryRequestBean(
			TestLogItemPageQueryRequestBean testLogItemPageQueryRequestBean) {
		this.testLogItemPageQueryRequestBean = testLogItemPageQueryRequestBean;
	}

	/**
	 * @return the testFileTypetestFileType
	 */
	public Integer getTestFileType() {
		return testFileType;
	}

	/**
	 * @param testFileType
	 *            the testFileType to set
	 */
	public void setTestFileType(Integer testFileType) {
		this.testFileType = testFileType;
	}

	/**
	 * @return the boxIdboxId
	 */
	public String getBoxId() {
		return boxId;
	}

	/**
	 * @param boxId
	 *            the boxId to set
	 */
	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	/**
	 * @return the moduleNomoduleNo
	 */
	public String getModuleNo() {
		return moduleNo;
	}

	/**
	 * @param moduleNo
	 *            the moduleNo to set
	 */
	public void setModuleNo(String moduleNo) {
		this.moduleNo = moduleNo;
	}

}
