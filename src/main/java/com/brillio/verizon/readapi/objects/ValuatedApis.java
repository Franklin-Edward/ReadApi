package com.brillio.verizon.readapi.objects;

import org.springframework.stereotype.Component;

@Component
public class ValuatedApis {
	private String apiName;
	private String response;
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
}
