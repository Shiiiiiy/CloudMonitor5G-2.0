package com.datang.domain.errorLogManage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FTPInfo {

	@Value("${errorlog.username}")
	private String username;
	
	@Value("${errorlog.password}")
	private String password;
	
	@Value("${errorlog.port}")
	private String port;
	
	@Value("${errorlog.ip}")
	private String ip;
	
	@Value("${errorlog.url}")
	private String url;
	
	public String getUsername() {
		return username;
	}
 
	public void setUsername(String username) {
		this.username = username;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
