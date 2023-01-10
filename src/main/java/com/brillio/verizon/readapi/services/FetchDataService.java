package com.brillio.verizon.readapi.services;

import org.apache.http.HttpResponse;

public interface FetchDataService {
	HttpResponse fetchAllData(String url);
}
