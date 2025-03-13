package com.adminvendor.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.adminvendor.util.Utility;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@Repository
public class VendorDao {

	@Autowired
	private final JdbcTemplate jdbcTemplate;

    public VendorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // This service saves the requested vendor
    public int saveVendor(String firstName, String lastName, String email, String password, String phone, String category, String business) {
    	 String insertQuery = "INSERT INTO `vendor`(`first_name`, `last_name`, `email`, `phone`, `password`, `category`, `business`)\r\n"
    	 		+ "VALUES(?, ?, ?, ?, ?, ?, ?);";
         // KeyHolder to capture the generated ID
         KeyHolder keyHolder = new GeneratedKeyHolder();
         jdbcTemplate.update(connection -> {
             PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
             ps.setString(1, firstName);
             ps.setString(2, lastName);
             ps.setString(3, email);
             ps.setString(4, phone);
             ps.setString(5, password);
             ps.setString(6, category);
             ps.setString(7, business);
             return ps;
         }, keyHolder);
         System.out.println(keyHolder.getKey().intValue());
         // Return the generated ID
         return keyHolder.getKey().intValue();
    }
    
    public int updateVendorDetails(int id, String firstName, String lastName, String email, String phone, String business, String image) {
    	String sql = "UPDATE `vendor` SET first_name = ?, last_name=?, email=?, phone=?, business=?, image = ? WHERE id = ?";
        return jdbcTemplate.update(sql, firstName, lastName, email, phone, business, image, id);
    }
    
    public int updateVendorImage(int id, String image) {
    	String sql = "UPDATE `vendor` SET image = ? WHERE id = ?";
    	return jdbcTemplate.update(sql, image, id);
    }
    
    public boolean verifyEmail(int id, String email) {
    	String verifyEmailQuery = "Select id from `vendor` where email=? and id!=?";
    	try {
    		// Query will return a list of results, each row is a Map
            List<Map<String, Object>> result = jdbcTemplate.queryForList(verifyEmailQuery, email, id);
            // If list is not empty, that means the email exists in the table
            return !result.isEmpty();
        } catch (EmptyResultDataAccessException e) {
            // If no result is found (empty result set), return false
            return false;
        }
    }
    
    public boolean verifyPhone(int id, String phone) {
    	String verifyPhoneQuery = "Select id from `vendor` where phone=? and id!=?";
    	try {
    		// Query will return a list of results, each row is a Map
            List<Map<String, Object>> result = jdbcTemplate.queryForList(verifyPhoneQuery, phone, id);
            // If list is not empty, that means the email exists in the table
            return !result.isEmpty();
        } catch (EmptyResultDataAccessException e) {
            // If no result is found (empty result set), return false
            return false;
        }
    }
    
    public Map<String, Object> checkLogin(String email, String password) {
    	String selectQuery = "Select * from `vendor` where email=?";
    	// Execute the query and retrieve results
        List<Map<String, Object>> result = jdbcTemplate.queryForList(selectQuery, email);
        // Return the first result if the list is not empty, otherwise return null or an empty map
        return Utility.isNullOrEmpty(result) ? null : result.get(0);
    }
    
    public int saveToken(int vendor, String token) {
    	String insertQuery = "INSERT INTO `vendor_login`(`vendor`, `token`) VALUES(?, ?);";
        // KeyHolder to capture the generated ID
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, vendor);
            ps.setString(2, token);
            return ps;
        }, keyHolder);
        System.out.println(keyHolder.getKey().intValue());
        // Return the generated ID
        return keyHolder.getKey().intValue();
    }
    
    public Map<String, Object> getTokenData(String token) {
    	String selectQuery = "Select * from `vendor_login` where token=?";
    	// Execute the query and retrieve results
    	List<Map<String, Object>> result = jdbcTemplate.queryForList(selectQuery, token);
        // Return the first result if the list is not empty, otherwise return null or an empty map
        return Utility.isNullOrEmpty(result) ? null : result.get(0);
    }
    
    public Map<String, Object> getVendor(int id) {
    	String selectQuery = "Select * from `vendor` where id=?";
    	// Execute the query and retrieve results
        List<Map<String, Object>> result = jdbcTemplate.queryForList(selectQuery, id);
        // Return the first result if the list is not empty, otherwise return null or an empty map
        return Utility.isNullOrEmpty(result) ? null : result.get(0);
    }
    
    public Map<String, Object> getVendorByEmail(String email) {
    	String selectQuery = "Select * from `vendor` where email=?";
    	// Execute the query and retrieve results
        List<Map<String, Object>> result = jdbcTemplate.queryForList(selectQuery, email);
        // Return the first result if the list is not empty, otherwise return null or an empty map
        return Utility.isNullOrEmpty(result) ? null : result.get(0);
    }
    
    public int getVendorServicesCount(int vendorId) {
    	String query = "SELECT count(*) `count` FROM vendor_services where vendor=?;";
    	Map<String, Object> result = jdbcTemplate.queryForMap(query, vendorId);
    	Long count =(Long) result.get("count");
    	return count != null ? count.intValue() : 0;
    }
    
    public int getBookingCount(int vendorId) {
    	String query = "SELECT count(*) `booking_count` FROM bookings b\r\n"
    			+ "JOIN vendor_services vs ON vs.id = b.service\r\n"
    			+ "WHERE vs.vendor = ?;";
    	Map<String, Object> result = jdbcTemplate.queryForMap(query, vendorId);
    	Long count =(Long) result.get("booking_count");
    	return count != null ? count.intValue() : 0;
    }
    
    public int getConfirmedBookingCount(int vendorId) {
    	String query = "SELECT count(*) `count` FROM bookings b\r\n"
    			+ "JOIN vendor_services vs ON vs.id = b.service\r\n"
    			+ "WHERE vs.vendor = ? AND b.status=\"Booked\" AND b.event_date<NOW();";
    	Map<String, Object> result = jdbcTemplate.queryForMap(query, vendorId);
    	Long count =(Long) result.get("count");
    	return count != null ? count.intValue() : 0;
    }
}
