package com.brillio.verizon.readapi.controllers;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.brillio.verizon.readapi.objects.FinalResponse;
import com.brillio.verizon.readapi.objects.ValuatedApis;
import com.brillio.verizon.readapi.services.FetchDataService;
import com.brillio.verizon.readapi.services.MatchDataService;

@RestController
public class matchApiController {
//	private static final Logger logger = LoggerFactory.getLogger(matchApiController.class);

	@Autowired
	FetchDataService fetchDataService;
	
	@Autowired
	MatchDataService matchDataService;
	
	@Autowired
	FinalResponse finalResponse;
	
	@RequestMapping(value = "/api/test", method = RequestMethod.GET)
	public ResponseEntity<?> fetchData(){
		finalResponse.setRequestTimeStamp(Instant.now().toString());
		List<ValuatedApis> apiResponses = new ArrayList<>();
		List<String> apis = new ArrayList<>();
		apis.add("abc");
		apis.add("def");
		apis.add("ghi");
		apis.add("jkl");
		int flag =apis.size();
		List<String> apiExceptions = new ArrayList<>();
		for(String api : apis) {
			System.out.println(api);
			try {
				ValuatedApis valuatedApis=new ValuatedApis();
				valuatedApis.setApiName(api);
				HttpResponse cassandera = null;
				try {
					cassandera = fetchDataService.fetchAllData(api+"_cassandraUrl");
					if(cassandera.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
						--flag;
						valuatedApis.setResponse("Cassandra Response Failed with "+ EntityUtils.toString(cassandera.getEntity()));
						apiResponses.add(valuatedApis);
						apiExceptions.add(valuatedApis.getResponse());
						continue;
					}
				}catch(Exception e) {
					--flag;
					valuatedApis.setResponse(e.getMessage());
					apiResponses.add(valuatedApis);
					apiExceptions.add("Cassandra failed due to "+e.getMessage());
					continue;
				}
				HttpResponse mongo = null;
				try {
					mongo = fetchDataService.fetchAllData(api+"_mongoUrl");
					if(mongo.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
						valuatedApis.setResponse("Mongo Response Failed with "+ EntityUtils.toString(mongo.getEntity()));
						apiResponses.add(valuatedApis);
						apiExceptions.add(valuatedApis.getResponse());
						--flag;
						continue;
					}
				}catch(Exception e) {
					valuatedApis.setResponse(e.getMessage());
					apiResponses.add(valuatedApis);
					apiExceptions.add("Mongo failed due to "+e.getMessage());
					--flag;
					continue;
				}
				if(mongo.getEntity()!=null && cassandera.getEntity()!=null) {
					String one = EntityUtils.toString(mongo.getEntity());
					String two = EntityUtils.toString(cassandera.getEntity());
					boolean match = matchDataService.matchDbs(one, two );
					if(match) {
						valuatedApis.setResponse("Cassandera and Mongo Responses are matching");
						apiResponses.add(valuatedApis);
					}
					else {
						valuatedApis.setResponse("Cassandera and Mongo Responses are not matching");
						apiResponses.add(valuatedApis);
					}
				}
				
			}catch(Exception e) {
				finalResponse.setStatus("FAILURE");
				finalResponse.setDescription(e.toString());
				finalResponse.setApiList(null);
				break;
			}
		}
		if(flag==0) {
			finalResponse.setStatus("FAILURE");
			String result = String.join(" New Line \n",apiExceptions);
			finalResponse.setDescription(result);
			finalResponse.setApiList(null);
		}
		else if(finalResponse.getStatus()==null) {
			finalResponse.setStatus("SUCCESS");
			finalResponse.setDescription("");
			finalResponse.setApiList(apiResponses);
		}
		finalResponse.setResposeTimeStamp(Instant.now().toString());
	    return new ResponseEntity<>(finalResponse, HttpStatus.OK);
	}
}
