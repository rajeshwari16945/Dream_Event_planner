package com.adminvendor.validation;

import java.util.HashMap;
import java.util.Map;

import com.adminvendor.util.Utility;

public class VendorValidation {
	
	public static String VALIDATION_MESSAGE = "This field is required.";
	public static String EMAIL_INVALID = "The Email is invalid";
	public static String PHONE_INVALID = "The Phone is invalid";
	
	public static final void validateVendor(Map<String, Object> reqData, boolean isUpdate)throws Exception {
		Map<String,Object> msgs=new HashMap<String,Object>();
		if(isUpdate && Utility.isNullOrEmpty(reqData.get("id"))) msgs.put("id", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("firstName"))) msgs.put("firstName", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("lastName"))) msgs.put("lastName", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("email"))) msgs.put("email", VALIDATION_MESSAGE);
		else if(!Utility.isValidEmail((String) reqData.get("email"))) msgs.put("email", EMAIL_INVALID);
		if(Utility.isNullOrEmpty(reqData.get("phone"))) msgs.put("phone", VALIDATION_MESSAGE);
		else if(!Utility.isValidPhoneNumber((String) reqData.get("phone"))) msgs.put("phone", PHONE_INVALID);
		if(!isUpdate && Utility.isNullOrEmpty(reqData.get("password"))) msgs.put("password", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("company"))) msgs.put("company", VALIDATION_MESSAGE);
		if(!isUpdate && Utility.isNullOrEmpty(reqData.get("category"))) msgs.put("category", VALIDATION_MESSAGE);
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

	public static final void validatePhone(Map<String, Object> reqData) throws Exception {
		Map<String,Object> msgs=new HashMap<String,Object>();
		if(Utility.isNullOrEmpty(reqData.get("phone"))) msgs.put("phone", VALIDATION_MESSAGE);
		if(!msgs.isEmpty()) {
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("Success", false);
			obj.put("error", msgs);
			throw new Exception(obj.toString());
		}
	}
	
	public static final void validateLogin(Map<String, Object> reqData) throws Exception {
		Map<String,Object> msgs=new HashMap<String,Object>();
		if(Utility.isNullOrEmpty(reqData.get("email"))) msgs.put("email", VALIDATION_MESSAGE);
		if(Utility.isNullOrEmpty(reqData.get("password"))) msgs.put("password", VALIDATION_MESSAGE);
		if(!msgs.isEmpty()) {
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("Success", false);
			obj.put("error", msgs);
			obj.put("Message", "Failed to Login.");
			throw new Exception(obj.toString());
		}
	}
}
