/**
 * 
 */
package com.datang.web.action.session;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 全局session处理action
 * 
 * @author yinzhipeng
 * @date:2016年7月18日 下午1:59:56
 * @version
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class SessionAction extends ActionSupport {
	/**
	 * 鉴权用户停止操作的时间是否超过20分钟
	 * 
	 * @return
	 */
	public String checkLastPostTime() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		Object attribute = session.getAttribute("lastPostTime");
		ActionContext.getContext().getValueStack().push(false);
		if (null != attribute) {
			Long lastPostTime = (Long) attribute;
			long currentTimeMillis = System.currentTimeMillis();
			if (1200000 <= (currentTimeMillis - lastPostTime)) {
				ActionContext.getContext().getValueStack().push(true);
			}
		}
		return ReturnType.JSON;
	}
}
