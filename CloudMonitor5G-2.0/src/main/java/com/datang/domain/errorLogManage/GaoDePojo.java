package com.datang.domain.errorLogManage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GaoDePojo {

	@Value("${errorlog.gaodeUrl}")
	private String gaodeUrl;

	/**]
	 * 高德地反地理编码路径
	 * @return
	 */
	public String getGaodeUrl() {
		return gaodeUrl;
	}

	public void setGaodeUrl(String gaodeUrl) {
		this.gaodeUrl = gaodeUrl;
	}
	
	
}
