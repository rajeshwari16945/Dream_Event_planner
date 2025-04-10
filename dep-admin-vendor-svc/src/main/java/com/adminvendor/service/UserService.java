package com.adminvendor.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.adminvendor.dao.UserDao;
import com.adminvendor.util.PasswordUtil;
import com.adminvendor.util.Utility;

@Service
public class UserService {

	@Autowired
    private UserDao dao;

	public Map<String, Object> saveUser(Map<String, Object> reqData) {
		Map<String, Object> res = new HashMap<String, Object>();
		if(dao.verifyEmail((String) reqData.get("email"))) res.put("errors", new HashMap<String, Object> () {{ put("email", "Email Already Exists."); }});
    	if(res.isEmpty()) {
    		int id = dao.saveUser((String) reqData.get("email"), PasswordUtil.hashPassword((String) reqData.get("password")));
    		res.put("uid", id);
    	}
    	System.out.println(res);
        return res;
	}
	
	public Map<String, Object> updateUser(Map<String, Object> reqData) {
		Map<String, Object> res = new HashMap<String, Object>();
		int id= dao.updateUserDetails((int) reqData.get("id"), (String) reqData.get("name"),  (String) reqData.get("partnerName"), (String) reqData.get("address"));
		res.put("uid", id);
		return res;
	}
	
	public Map<String, Object> updatePassword(Map<String, Object> reqData) {
		Map<String, Object> res = new HashMap<String, Object>();
		int id= dao.updateUserPassword((int) reqData.get("id"), PasswordUtil.hashPassword((String) reqData.get("password")));
		res.put("uid", id);
		return res;
	}
	
	public Map<String, Object> updateUserImage(Map<String, Object> reqData, MultipartFile image) {
		Map<String, Object> res = new HashMap<String, Object>();
    	String imagePath = Utility.singleFileUpload(image);
		int id = dao.updateUserImage((int) reqData.get("id"), imagePath);
		if(id<=0) res.put("errors", "No updation performed");
		else res.put("uid", reqData.get("id"));
		return res;
	}

    public Map<String, Object> verifyEmail(Map<String, Object> reqData) {
    	Map<String, Object> res = new HashMap<String, Object>();
    	if(dao.verifyEmail((String) reqData.get("email"))) res.put("errors", new HashMap() {{ put("email", "Email Already Exists."); }});
    	return res;
    }
    
	public Map<String, Object> login(Map<String, Object> reqData) {
		Map<String, Object> res = new HashMap<String, Object>();
		Map<String, Object> data = dao.checkLogin((String) reqData.get("email"), (String) reqData.get("password"));
		if(Utility.isNullOrEmpty(data) || !PasswordUtil.verifyPassword((String) reqData.get("password"), (String) data.get("password"))) res.put("errors", new HashMap() {{ put("details", "Invalid Credentials."); }});
		else res.put("data", data);
		return res;
	}
	
	public void saveToken(int user, String token) {
		dao.saveToken(user, token);
	}
	
	public Map<String, Object> getToken(String token) {
		return dao.getTokenData(token);
	}
	
	public Map<String, Object> getUser(int userId) {
		Map<String, Object> res = new HashMap<String, Object>();
		Map<String, Object> data = dao.getUser(userId);
		if(Utility.isNullOrEmpty(data)) res.put("errors", new HashMap() {{ put("details", "User not exists"); }});
		else res.put("data", data);
		return res;
	}

	public Map<String, Object> getUserByEmail(String email) {
		return dao.getUserByEmail(email);
	}
	
	public void saveResetToken(String token, String email) {
		dao.saveResetToken(token, email);
	}
	
}
