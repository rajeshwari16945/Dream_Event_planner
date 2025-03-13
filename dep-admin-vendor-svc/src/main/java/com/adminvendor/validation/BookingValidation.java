package com.adminvendor.validation;

import java.util.HashMap;
import java.util.Map;

import com.adminvendor.util.Utility;

public class BookingValidation {

	public static String VALIDATION_MESSAGE = "This field is required.";
	
	public static final void validateBooking(Map<String, Object> reqData, boolean isUpdate)throws Exception {
		Map<String,Object> msgs=new HashMap<String,Object>();
		if(isUpdate && Utility.isNullOrEmpty(reqData.get("id"))) msgs.put("id", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("firstName"))) msgs.put("firstName", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("lastName"))) msgs.put("lastName", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("guest"))) msgs.put("guest", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("vision"))) msgs.put("vision", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("eventDate"))) msgs.put("eventDate", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("user"))) msgs.put("user", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("service"))) msgs.put("service", VALIDATION_MESSAGE);
		if(!msgs.isEmpty()) {
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("Success", false);
			obj.put("error", msgs);
			obj.put("Message", "Failed to Save.");
			throw new Exception(obj.toString());
		}
	}
	
	public static final void validateBookingStatus(Map<String, Object> reqData) throws Exception {
		Map<String,Object> msgs=new HashMap<String,Object>();
		if(Utility.isNullOrEmpty(reqData.get("id"))) msgs.put("id", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("status"))) msgs.put("status", VALIDATION_MESSAGE);
		if(!msgs.isEmpty()) {
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("Success", false);
			obj.put("error", msgs);
			obj.put("Message", "Failed to Save.");
			throw new Exception(obj.toString());
		}
	}
}
