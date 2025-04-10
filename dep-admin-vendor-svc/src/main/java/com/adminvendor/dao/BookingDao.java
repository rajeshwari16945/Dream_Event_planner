package com.adminvendor.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.adminvendor.util.Utility;

@Repository
public class BookingDao {

	@Autowired
	private final JdbcTemplate jdbcTemplate;

    public BookingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
 // This service saves the requested vendor
    public int save(String firstName, String lastName, int guest, String eventDate, String vision, int user, int service, String status ) {
    	 String insertQuery = "INSERT INTO `bookings`(`first_name`, `last_name`, `guest`, `vision`, `event_date`, `user`, `service`, `status`)\r\n"
    	 		+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
         // KeyHolder to capture the generated ID
         KeyHolder keyHolder = new GeneratedKeyHolder();
         jdbcTemplate.update(connection -> {
             PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
             ps.setString(1, firstName);
             ps.setString(2, lastName);
             ps.setInt(3, guest);
             ps.setString(4, vision);
             ps.setString(5, eventDate);
             ps.setInt(6, user);
             ps.setInt(7, service);
             ps.setString(8, status);
             return ps;
         }, keyHolder);
         System.out.println(keyHolder.getKey().intValue());
         // Return the generated ID
         return keyHolder.getKey().intValue();
    }
    
    public int updateBookingStatus(int id, String status, int price) {
    	String sql = "UPDATE `bookings` SET status = ?, price=? WHERE id = ?";
        return jdbcTemplate.update(sql, status, price, id);
    }
    
    public boolean verifyBooking(String firstName, String lastName, String eventDate, int user, int service) {
    	String verifyNameQuery = "SELECT id FROM `bookings` WHERE first_name=? AND last_name=? AND event_date=? AND user=? AND service=?";
    	try {
    		// Query will return a list of results, each row is a Map
            List<Map<String, Object>> result = jdbcTemplate.queryForList(verifyNameQuery, firstName, lastName, eventDate, user, service);
            // If list is not empty, that means the email exists in the table
            return !result.isEmpty();
        } catch (EmptyResultDataAccessException e) {
            // If no result is found (empty result set), return false
            return false;
        }
    }
    
    public int getBookingCount(int id) {
    	String query = "SELECT count(*) `count` FROM bookings where service=?;";
    	Map<String, Object> result = jdbcTemplate.queryForMap(query, id);
    	Long count =(Long) result.get("count");
    	return count != null ? count.intValue() : 0;
    }
    
    public List<Map<String, Object>> listBookingsByVendor(int vendorId) {
    	String listQuery = "SELECT `b`.`id`, `b`.`first_name` `firstName`, `b`.`last_name` `lastName`, `b`.`guest`,  `b`.`event_date` `eventDate`, `b`.`vision`,\r\n"
    			+ "`b`.`status`, `vs`.`name` `serviceName`, `vs`.`title`, `vs`.`address`, `vs`.`summary`, `v`.`category`\r\n"
    			+ "FROM bookings `b` \r\n"
    			+ "JOIN vendor_services `vs` ON `b`.`service`=`vs`.`id`\r\n"
    			+ "JOIN vendor `v` ON `vs`.`vendor`=`v`.`id`\r\n"
    			+ "WHERE `vs`.`vendor`=? \r\n"
    			+ "ORDER BY `b`.`id` DESC";
    	// Query will return a list of results, each row is a Map
        List<Map<String, Object>> result = jdbcTemplate.queryForList(listQuery, vendorId);
        return result;
    }
    
    public List<Map<String, Object>> listBookingsByUser(int userId) {
    	String listQuery = "SELECT `b`.`id`, `b`.`first_name` `firstName`, `b`.`last_name` `lastName`, `b`.`guest`,  `b`.`event_date` `eventDate`, `b`.`vision`,\r\n"
    			+ "`b`.`status`, `b`.`price`, `vs`.`name` `serviceName`, `vs`.`title`, `vs`.`address`, `vs`.`summary`, `v`.`category`\r\n"
    			+ "FROM bookings `b` \r\n"
    			+ "JOIN vendor_services `vs` ON `b`.`service`=`vs`.`id`\r\n"
    			+ "JOIN vendor `v` ON `vs`.`vendor`=`v`.`id`\r\n"
    			+ "WHERE `b`.`user`=? \r\n"
    			+ "ORDER BY `b`.`id` DESC";
    	// Query will return a list of results, each row is a Map
        List<Map<String, Object>> result = jdbcTemplate.queryForList(listQuery, userId);
        return result;
    }
    
    public Map<String, Object> getEachBooking(int id) {
    	String selectQuery = "SELECT `b`.`id`, `b`.`first_name` `firstName`, `b`.`last_name` `lastName`, `b`.`guest`,  `b`.`event_date` `eventDate`, `b`.`vision`,\r\n"
    			+ "`b`.`status`, `vs`.`name` `serviceName`, `vs`.`title`, `vs`.`address`, `vs`.`summary`, `v`.`category`, `b`.`price`\r\n"
    			+ "FROM bookings `b` \r\n"
    			+ "JOIN vendor_services `vs` ON `b`.`service`=`vs`.`id`\r\n"
    			+ "JOIN vendor `v` ON `vs`.`vendor`=`v`.`id`\r\n"
    			+ "WHERE `b`.`id`=? \r\n"
    			+ "ORDER BY `b`.`id` DESC";
    	// Query will return a list of results, each row is a Map
        Map<String, Object> result = jdbcTemplate.queryForMap(selectQuery, id);
        return result;
    }
    
    public Map<String, Object> getBookingDatesByService(int id) {
    	String query = "SELECT GROUP_CONCAT(event_date) AS date_array FROM bookings WHERE service= ? AND status = 'Booked';";
    	Map<String, Object> result = jdbcTemplate.queryForMap(query, id);
    	return result;
    }
}
