package com.brillio.verizon.readapi.services.impl;

import org.springframework.stereotype.Service;

import com.brillio.verizon.readapi.services.MatchDataService;

@Service
public class MatchDataServiceImpl implements MatchDataService {

	@Override
	public Boolean matchDbs(String mongo, String cassandra) {
		if(mongo.equals(cassandra)) {
			return true;
		}
		return false;
	}

}
