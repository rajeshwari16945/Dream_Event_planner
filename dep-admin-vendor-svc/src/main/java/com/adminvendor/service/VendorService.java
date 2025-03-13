package com.adminvendor.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.adminvendor.dao.VendorDao;
import com.adminvendor.util.PasswordUtil;
import com.adminvendor.util.Utility;

@Service
public class VendorService {

    @Autowired
    private VendorDao dao;

    public Map<String, Object> saveVendor(Map<String, Object> reqData) {
    	Map<String, Object> res = new HashMap<String, Object>();
    	if(dao.verifyEmail(0, (String) reqData.get("email"))) res.put("errors", new HashMap() {{ put("email", "Email Already Exists."); }});
    	if(dao.verifyPhone(0, (String) reqData.get("phone"))) res.put("errors", new HashMap() {{ put("phone", "Phone Already Exists."); }});
    	if(res.isEmpty()) {
    		int id = dao.saveVendor((String) reqData.get("firstName"), (String) reqData.get("lastName"), (String) reqData.get("email"),  PasswordUtil.hashPassword((String) reqData.get("password")), 
    				(String) reqData.get("phone"), (String) reqData.get("category"), (String) reqData.get("company"));
    		res.put("uid", id);
    	}
    	System.out.println(res);
        return res;
    }
    
    public Map<String, Object> updateVendor(Map<String, Object> reqData, MultipartFile image) {
    	Map<String, Object> res = new HashMap<String, Object>();
    	if(dao.verifyEmail((int) reqData.get("id"), (String) reqData.get("email"))) res.put("errors", new HashMap() {{ put("email", "Email Already Exists."); }});
    	if(dao.verifyPhone((int) reqData.get("id"), (String) reqData.get("phone"))) res.put("errors", new HashMap() {{ put("phone", "Phone Already Exists."); }});
    	if(res.isEmpty()) {
		String imagePath = null;
            if (image != null && !image.isEmpty()) {
                String filePath = "C://UPLOADS/" + image.getOriginalFilename();
                File file = new File(filePath);
                // Ensure the directory exists before saving the file
                File directory = new File(file.getParent());
                if (!directory.exists()) {
                    directory.mkdirs(); // Creates the directory structure if it doesn't exist
                }
                try {
                    image.transferTo(file); // Save image to "uploads/" folder
                    imagePath=filePath;
                } catch (IOException e) {
                	throw new RuntimeException("Failed to save image: " + e.getMessage());
                }
            }
    		int id = dao.updateVendorDetails((int) reqData.get("id"), (String) reqData.get("firstName"), (String) reqData.get("lastName"), (String) reqData.get("email"), (String) reqData.get("phone"), (String) reqData.get("company"), imagePath);
    		if(id<=0) res.put("errors", "No updation performed");
    		else res.put("uid", reqData.get("id"));
    	}
        return res;
    }
    
    public Map<String, Object> verifyEmail(Map<String, Object> reqData) {
    	Map<String, Object> res = new HashMap<String, Object>();
    	if(dao.verifyEmail(0, (String) reqData.get("email"))) res.put("errors", new HashMap<String, Object>() {{ put("email", "Email Already Exists."); }});
    	return res;
    }
    
    public Map<String, Object> verifyPhone(Map<String, Object> reqData) {
    	Map<String, Object> res = new HashMap<String, Object>();
    	if(dao.verifyPhone(0, (String) reqData.get("phone"))) res.put("errors", new HashMap() {{ put("phone", "Phone Already Exists."); }});
    	return res;
    }

	public Map<String, Object> login(Map<String, Object> reqData) {
		Map<String, Object> res = new HashMap<String, Object>();
		Map<String, Object> data = dao.checkLogin((String) reqData.get("email"), (String) reqData.get("password"));
		if(Utility.isNullOrEmpty(data) || !PasswordUtil.verifyPassword((String) reqData.get("password"), (String) data.get("password"))) res.put("errors", new HashMap() {{ put("details", "Invalid Credentials."); }});
		else res.put("data", data);
		return res;
	}
	
	public void saveToken(int vendor, String token) {
		dao.saveToken(vendor, token);
	}
	
	public Map<String, Object> getToken(String token) {
		return dao.getTokenData(token);
	}
	
	public Map<String, Object> getVendor(int id) {
		Map<String, Object> res = new HashMap<String, Object>();
		Map<String, Object> data = dao.getVendor(id);
		if(Utility.isNullOrEmpty(data)) res.put("errors", new HashMap() {{ put("details", "User not exists"); }});
		else res.put("data", data);
		return res;
	}
	
	public Map<String, Object> getVendorByEmail(String email) {
		Map<String, Object> res = new HashMap<String, Object>();
		Map<String, Object> data = dao.getVendorByEmail(email);
		if(Utility.isNullOrEmpty(data)) res.put("errors", new HashMap() {{ put("details", "User not exists"); }});
		else res.put("data", data);
		return res;
	}
	
	public Map<String, Object> getDashboardDetails(int vendorId) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("serviceCount", dao.getVendorServicesCount(vendorId));
		res.put("bookingCount", dao.getBookingCount(vendorId));
		res.put("confirmedBookingCount", dao.getConfirmedBookingCount(vendorId));
		return res;
	}
}
