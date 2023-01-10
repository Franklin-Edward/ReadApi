package com.brillio.verizon.readapi.services.impl;


import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.brillio.verizon.readapi.services.FetchDataService;

@Service
@PropertySource(value={"classpath:services.properties"})
public class FetchDataServiceImpl implements FetchDataService{
	
//	private static final Logger logger = LoggerFactory.getLogger(FetchDataServiceImpl.class);
	
	@Autowired
	private Environment env;
	
	@Override
	public HttpResponse fetchAllData(String url) {
	    CloseableHttpClient httpClient = HttpClients.createDefault();
	    String fetchUrl = env.getProperty("service.read."+url);
	    HttpGet request = new HttpGet(fetchUrl);
	    HttpResponse response = null;
	    try {
			response = httpClient.execute(request);
		} catch (Exception e) {
			response = new BasicHttpResponse(HttpVersion.HTTP_1_1, 400, "Bad Request");
			response.setEntity(new StringEntity(e.getMessage(),"UTF-8"));
		    return response;
		}
	    return response;
	}
	
}
