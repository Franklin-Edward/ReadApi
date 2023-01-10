package com.brillio.verizon.readapi.objects;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class FinalResponse {
	private String RequestTimeStamp;
	private String ResposeTimeStamp;
	private String Status;
	private String Description;
	private List<ValuatedApis> apiList;
	public String getRequestTimeStamp() {
		return RequestTimeStamp;
	}
	public void setRequestTimeStamp(String requestTimeStamp) {
		RequestTimeStamp = requestTimeStamp;
	}
	public String getResposeTimeStamp() {
		return ResposeTimeStamp;
	}
	public void setResposeTimeStamp(String resposeTimeStamp) {
		ResposeTimeStamp = resposeTimeStamp;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public List<ValuatedApis> getApiList() {
		return apiList;
	}
	public void setApiList(List<ValuatedApis> apiList) {
		this.apiList = apiList;
	}
}
