package com.adminvendor.validation;

import java.util.HashMap;
import java.util.Map;

import com.adminvendor.util.Utility;

public class VendorServiceValidation {

	public static String VALIDATION_MESSAGE = "This field is required.";
	
	public static final void validateService(Map<String, Object> reqData, boolean update)throws Exception {
		Map<String,Object> msgs=new HashMap<String,Object>();
		if(update) if(Utility.isNullOrEmpty(reqData.get("id"))) msgs.put("id", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("name"))) msgs.put("name", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("title"))) msgs.put("title", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("address"))) msgs.put("address", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("summary"))) msgs.put("summary", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("vendor")) || Utility.isNullOrEmpty(((Map<String, Object>) reqData.get("vendor")).get("id"))) msgs.put("vendor", VALIDATION_MESSAGE);
		if(!msgs.isEmpty()) {
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("Success", false);
			obj.put("error", msgs);
			obj.put("Message", "Failed to Save.");
			throw new Exception(obj.toString());
		}
	}
}
