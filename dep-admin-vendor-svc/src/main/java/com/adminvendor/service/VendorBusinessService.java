package com.adminvendor.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.adminvendor.dao.VendorBusinessDao;
import com.adminvendor.util.Utility;

@SuppressWarnings("unchecked")
@Service
public class VendorBusinessService {

    @Autowired
    private VendorBusinessDao dao;
    
    private static final String UPLOAD_DIR = "C://UPLOADS/";
   
    @Transactional(rollbackFor = Exception.class) 
	public Map<String, Object> saveVendorService(Map<String, Object> reqData, List<MultipartFile> images) {
    	Map<String, Object> res = new HashMap<String, Object>();
    	if(dao.verifyName((String) reqData.get("name"), 0)) res.put("errors", new HashMap() {{ put("name", "Name Already Exists."); }});
    	try {
    		if(res.isEmpty()) {
        		int serviceId = dao.saveVendorService((String) reqData.get("name"), (String) reqData.get("title"), (String) reqData.get("address"), (String) reqData.get("summary"), Utility.isNullOrEmpty(reqData.get("guests"))? 0 : (int) reqData.get("guests"), 
        				Utility.isNullOrEmpty(reqData.get("price"))? 0 : (int) reqData.get("price"), (int) ((Map<String, Object>) reqData.get("vendor")).get("id"));
        		
        		res.put("uid", serviceId);
        		List<String> imagePaths = new ArrayList<>();

                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String filePath = UPLOAD_DIR + image.getOriginalFilename();
                        File file = new File(filePath);
                        // Ensure the directory exists before saving the file
                        File directory = new File(file.getParent());
                        if (!directory.exists()) {
                            directory.mkdirs(); // Creates the directory structure if it doesn't exist
                        }
                        try {
                            image.transferTo(file); // Save image to "uploads/" folder
                            imagePaths.add(filePath);
                        } catch (IOException e) {
                        	throw new RuntimeException("Failed to save image: " + e.getMessage());
                        }
                    }
                }
        		dao.saveVendorServiceImages(serviceId,imagePaths);
        	}
        	System.out.println(res);
            return res;
    	} catch(Exception e) {
    		System.out.println(e.getMessage());
    		throw new RuntimeException("Failed to save");
    	}
//		return res;
    }
    
    public Map<String, Object> updateVendorService(Map<String, Object> reqData) {
    	Map<String, Object> res = new HashMap<String, Object>();
    	if(dao.verifyName((String) reqData.get("name"), (int) reqData.get("id"))) res.put("errors", new HashMap() {{ put("name", "Name Already Exists."); }});
    	if(res.isEmpty()) {
    		int id = dao.updateVendorService((int) reqData.get("id"), (String) reqData.get("name"), (String) reqData.get("title"), (String) reqData.get("address"), (String) reqData.get("summary"), Utility.isNullOrEmpty(reqData.get("guests"))? 0 : (int) reqData.get("guests"), 
    				Utility.isNullOrEmpty(reqData.get("price"))? 0 : (int) reqData.get("price"), (int) ((Map<String, Object>) reqData.get("vendor")).get("id"));
    		if(id<=0) res.put("errors", "No updation performed");
    		else res.put("uid", reqData.get("id"));
    	}
        return res;
    }
    
    public Map<String, Object> listServicesByVendor(Map<String, Object> reqData) {
    	Map<String, Object> res = new HashMap<String, Object>();
    	List<Map<String, Object>> data = dao.listServicesByVendor((int) reqData.get("id"));
    	res.put("data", data);
    	System.out.println(res);
    	return res;
    }
    
    public Map<String, Object> listServicesByCategory(Map<String, Object> reqData) {
    	Map<String, Object> res = new HashMap<String, Object>();
    	List<Map<String, Object>> data = dao.listServicesByCategory((String) reqData.get("category"), (String) reqData.get("search"), (String) reqData.get("sort"));
    	res.put("data", data);
    	System.out.println(res);
    	return res;
    }
    
    public Map<String, Object> getEachService(int id) {
    	Map<String, Object> res= new HashMap<String, Object>();
    	Map<String, Object> data = dao.getEachService(id);
    	res.put("data", data);
    	return res;
    }
}
