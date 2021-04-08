package com.datang.web.action.platform.security;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.datang.common.util.DES3Utils;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 登录登出管理Action
 * 
 * @author yinzhipeng
 * @date:2015年10月10日 上午11:41:53
 * @version
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class SecurityAction extends ActionSupport {

	private String username;
	private String password;
	private Boolean rememberPassword;

	public String login() throws IOException {
		UsernamePasswordToken token = new UsernamePasswordToken(username,
				password);
		Subject subject = SecurityUtils.getSubject();
		String error = null;
		if (subject.isAuthenticated()) {
			return SUCCESS;
		} else {
			try {
				subject.login(token);
				Session session = subject.getSession();
				// 记住用户名密码
				if (null != rememberPassword && rememberPassword) {

					byte[] username_key = new BASE64Decoder()
							.decodeBuffer(DES3Utils.USERNAME_KEY);
					byte[] username_data = username.getBytes("UTF-8");
					byte[] username__str = DES3Utils.des3EncodeECB(
							username_key, username_data);// 加密
					String desUsername = new BASE64Encoder()
							.encode(username__str);
					byte[] password_key = new BASE64Decoder()
							.decodeBuffer(DES3Utils.PASSWORD_KEY);
					byte[] password_data = password.getBytes("UTF-8");
					byte[] password_str = DES3Utils.des3EncodeECB(password_key,
							password_data);// 加密
					String desPassword = new BASE64Encoder()
							.encode(password_str);
					Cookie cookie = new Cookie("LOGIN_USERNAME", desUsername);
					Cookie cookie1 = new Cookie("LOGIN_PASSWORD", desPassword);
					Cookie cookie2 = new Cookie("LOGIN_REMEMBER",
							rememberPassword + "");
					cookie.setMaxAge(180 * 24 * 60 * 60);
					cookie1.setMaxAge(180 * 24 * 60 * 60);
					cookie2.setMaxAge(180 * 24 * 60 * 60);
					ServletActionContext.getResponse().addCookie(cookie);
					ServletActionContext.getResponse().addCookie(cookie1);
					ServletActionContext.getResponse().addCookie(cookie2);
				} else {
					// 删除cookie
					Cookie cookie = new Cookie("LOGIN_USERNAME", "");
					Cookie cookie1 = new Cookie("LOGIN_PASSWORD", "");
					Cookie cookie2 = new Cookie("LOGIN_REMEMBER",
							rememberPassword + "");
					cookie.setMaxAge(0);
					cookie1.setMaxAge(0);
					cookie2.setMaxAge(0);
					ServletActionContext.getResponse().addCookie(cookie);
					ServletActionContext.getResponse().addCookie(cookie1);
					ServletActionContext.getResponse().addCookie(cookie2);
				}
				return SUCCESS;
			} catch (UnknownSessionException use) {
				ActionContext.getContext().getValueStack()
						.set("userName", username);
				error = "异常会话";
			} catch (UnknownAccountException ex) {
				ActionContext.getContext().getValueStack()
						.set("userName", username);
				error = "账号错误";
			} catch (IncorrectCredentialsException ice) {
				ActionContext.getContext().getValueStack()
						.set("userName", username);
				error = "密码错误";
			} catch (LockedAccountException lae) {
				ActionContext.getContext().getValueStack()
						.set("userName", username);
				error = "账号已被锁定，请与系统管理员联系";
			} catch (AuthenticationException ae) {
				ae.printStackTrace();
				ActionContext.getContext().getValueStack()
						.set("userName", username);
				error = "您没有授权";
			} catch (Exception e) {
				ActionContext.getContext().getValueStack()
						.set("userName", username);
				error = "出现未知异常,请与系统管理员联系";
			}
			ActionContext.getContext().getValueStack().set("error", error);
			return ERROR;
		}
	}

	public String jsonLogin() {
		UsernamePasswordToken token = new UsernamePasswordToken(username,
				password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			Session session = subject.getSession();
		} catch (UnknownSessionException use) {
			ActionContext.getContext().getValueStack().set("errorMsg", "异常会话");
		} catch (UnknownAccountException ex) {
			ActionContext.getContext().getValueStack().set("errorMsg", "账号错误");
		} catch (IncorrectCredentialsException ice) {
			ActionContext.getContext().getValueStack().set("errorMsg", "密码错误");
		} catch (LockedAccountException lae) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", "账号已被锁定，请与系统管理员联系");
		} catch (AuthenticationException ae) {
			ae.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "您没有授权");
		} catch (Exception e) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", "出现未知异常,请与系统管理员联系");
		}
		return ReturnType.JSON;
	}

	/*
	 * public String goToChangeOverFailList() { return "debug"; }
	 */

	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.invalidate();
		return ERROR;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the rememberPasswordrememberPassword
	 */
	public Boolean getRememberPassword() {
		return rememberPassword;
	}

	/**
	 * @param rememberPassword
	 *            the rememberPassword to set
	 */
	public void setRememberPassword(Boolean rememberPassword) {
		this.rememberPassword = rememberPassword;
	}

}
