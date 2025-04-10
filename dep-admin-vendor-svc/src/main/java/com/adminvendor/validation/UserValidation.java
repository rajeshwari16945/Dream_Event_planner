package com.adminvendor.validation;

import java.util.HashMap;
import java.util.Map;

import com.adminvendor.util.Utility;

public class UserValidation {
	public static String VALIDATION_MESSAGE = "This field is required.";
	public static String EMAIL_INVALID = "The Email is invalid";
	public static String PHONE_INVALID = "The Phone is invalid";
	
	public static final void validateUser(Map<String, Object> reqData, boolean isUpdate)throws Exception {
		Map<String,Object> msgs=new HashMap<String,Object>();
		if(isUpdate && Utility.isNullOrEmpty(reqData.get("id"))) msgs.put("id", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("email"))) msgs.put("email", VALIDATION_MESSAGE);
		else if(!Utility.isValidEmail((String) reqData.get("email"))) msgs.put("email", EMAIL_INVALID);
		if(!isUpdate && Utility.isNullOrEmpty(reqData.get("password"))) msgs.put("password", VALIDATION_MESSAGE);
		if(!msgs.isEmpty()) {
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("Success", false);
			obj.put("error", msgs);
			obj.put("Message", "Failed to Save.");
			throw new Exception(obj.toString());
		}
	}
	
	public static final void validateUserPassword(Map<String, Object> reqData)throws Exception {
		Map<String,Object> msgs=new HashMap<String,Object>();
		if(Utility.isNullOrEmpty(reqData.get("id"))) msgs.put("id", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("password"))) msgs.put("password", VALIDATION_MESSAGE);
		else if(Utility.isNullOrEmpty(reqData.get("confirmPassword"))) msgs.put("confirmPassword", VALIDATION_MESSAGE);
		else if(!reqData.get("password").equals(reqData.get("confirmPassword"))) msgs.put("password", "Doesnt Match");
		if(!msgs.isEmpty()) {
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("Success", false);
			obj.put("error", msgs);
			obj.put("Message", "Failed to Save.");
			throw new Exception(obj.toString());
		}
	}
	
	public static final void validateEmail(Map<String, Object> reqData) throws Exception {
		Map<String,Object> msgs=new HashMap<String,Object>();
		if(Utility.isNullOrEmpty(reqData.get("email"))) msgs.put("email", VALIDATION_MESSAGE);
		if(!msgs.isEmpty()) {
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("Success", false);
			obj.put("error", msgs);
			throw new Exception(obj.toString());
		}
	}
}
