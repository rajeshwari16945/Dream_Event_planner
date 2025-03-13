package com.adminvendor.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.adminvendor.dao.BookingDao;

@Service
public class BookingService {

    @Autowired
    private BookingDao dao;

    public Map<String, Object> save(Map<String, Object> reqData) {
    	Map<String, Object> res = new HashMap<String, Object>();
    	if(dao.verifyBooking((String) reqData.get("firstName"), (String) reqData.get("lastName"), (String) reqData.get("eventDate"), (int) reqData.get("user"), (int) reqData.get("service"))) 
    		res.put("errors", "You have already made the request. Please wait till the vendor approves the request.");
    	if(res.isEmpty()) {
    		int id = dao.save((String) reqData.get("firstName"), (String) reqData.get("lastName"), (int) reqData.get("guest"), (String) reqData.get("eventDate"), 
    				(String) reqData.get("vision"), (int) reqData.get("user"), (int) reqData.get("service"), "Requested");
    		res.put("uid", id);
    	}
    	System.out.println(res);
        return res;
    }
    
    public Map<String, Object> updateBookingStatus(Map<String, Object> reqData) {
    	Map<String, Object> res = new HashMap<String, Object>();
    	if(res.isEmpty()) {
    		int id = dao.updateBookingStatus((int) reqData.get("id"), (String) reqData.get("status"), (int) reqData.get("price"));
    		res.put("uid", (int) reqData.get("id"));
    	}
        return res;
    }
    
	public Map<String, Object> getBookingCount(int serviceId) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("bookingCount", dao.getBookingCount(serviceId));
		return res;
	}
	
    public Map<String, Object> listBookingsByVendor(Map<String, Object> reqData) {
    	Map<String, Object> res = new HashMap<String, Object>();
    	List<Map<String, Object>> data = dao.listBookingsByVendor((int) reqData.get("id"));
    	res.put("data", data);
    	System.out.println(res);
    	return res;
    }
    
    public Map<String, Object> listBookingsByUser(Map<String, Object> reqData) {
    	Map<String, Object> res = new HashMap<String, Object>();
    	List<Map<String, Object>> data = dao.listBookingsByUser((int) reqData.get("id"));
    	res.put("data", data);
    	System.out.println(res);
    	return res;
    }
    
    public Map<String, Object> getEachBooking(int id) {
    	Map<String, Object> res= new HashMap<String, Object>();
    	Map<String, Object> data = dao.getEachBooking(id);
    	res.put("data", data);
    	return res;
    }
}
