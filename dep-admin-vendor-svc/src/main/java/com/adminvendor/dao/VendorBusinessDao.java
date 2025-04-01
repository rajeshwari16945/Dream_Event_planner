package com.adminvendor.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.adminvendor.util.Utility;

@Repository
public class VendorBusinessDao {

	@Autowired
	private final JdbcTemplate jdbcTemplate;

    public VendorBusinessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    // This service saves the requested vendor
    public int saveVendorService(String name, String title, String address, String summary, int guests, int price, int vendor ) {
    	 String insertQuery = "INSERT INTO `vendor_services`(`name`, `title`, `address`, `summary`, `guest`, `price`, `vendor`)\r\n"
    	 		+ "VALUES(?, ?, ?, ?, ?, ?, ?);";
         // KeyHolder to capture the generated ID
         KeyHolder keyHolder = new GeneratedKeyHolder();
         jdbcTemplate.update(connection -> {
             PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
             ps.setString(1, name);
             ps.setString(2, title);
             ps.setString(3, address);
             ps.setString(4, summary);
             ps.setInt(5, guests);
             ps.setInt(6, price);
             ps.setInt(7, vendor);
             return ps;
         }, keyHolder);
         System.out.println(keyHolder.getKey().intValue());
         // Return the generated ID
         return keyHolder.getKey().intValue();
    }
    
    public int saveVendorServiceImages(int serviceId, List<String> imagePaths) {
    	String imageSql = "INSERT INTO vendor_service_images (vendor_service, image) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(imageSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, serviceId);
                ps.setString(2, imagePaths.get(i));
            }

            @Override
            public int getBatchSize() {
                return imagePaths.size();
            }
        });
		return imagePaths.size();
    }
    
    public boolean verifyName(String name, int id) {
    	String verifyNameQuery = "Select id from `vendor_services` where name=? and id!=?";
    	try {
    		// Query will return a list of results, each row is a Map
            List<Map<String, Object>> result = jdbcTemplate.queryForList(verifyNameQuery, name, id);
            // If list is not empty, that means the email exists in the table
            return !result.isEmpty();
        } catch (EmptyResultDataAccessException e) {
            // If no result is found (empty result set), return false
            return false;
        }
    }
    
    public int updateVendorService(int id, String name, String title, String address, String summary, int guests, int price, int vendor) {
    	String sql = "UPDATE `vendor_services` SET name = ?, title=?, address=?, summary=?, guest=?, price=?, vendor=? WHERE id = ?";
        return jdbcTemplate.update(sql, name, title, address, summary, guests, price, vendor, id);
    }
    
    public List<Map<String, Object>> listServicesByVendor(int id) {
    	String listQuery = "SELECT vs.`id`, v.`first_name` `vendor.first_name`, v.`email` `vendor.email`, \r\n"
    			+ "vs.`title`, vs.`name`, vs.`address`, vs.`summary`, GROUP_CONCAT(vsi.image SEPARATOR ',') AS images  \r\n"
    			+ "FROM vendor_services `vs` \r\n"
    			+ "JOIN vendor `v` ON v.`id`=vs.`vendor`\r\n"
    			+ "JOIN vendor_service_images `vsi` ON vsi.`vendor_service`=vs.`id`\r\n"
    			+ "WHERE v.`id`=?\r\n"
    			+ "GROUP BY vs.`id` ORDER BY vs.`id` DESC;";
    	// Query will return a list of results, each row is a Map
        List<Map<String, Object>> result = jdbcTemplate.queryForList(listQuery, id);
        return result;
    }
    
    public List<Map<String, Object>> listServicesByCategory(String category, String search, String sort) {
    	String whereCondition = "";
    	if(!Utility.isNullOrEmpty(search)) {
    		String[] words = search.split(" ");
    		for(int i=0; i<words.length; i++) {
    			if(i==0) { 
                    whereCondition+= " AND (";
                }
                else { 
                    whereCondition+= " OR "; 
                }
                whereCondition +=  " `v`.`first_name` LIKE '%"+words[i]+"%' OR `v`.`last_name` LIKE '%"+words[i]+"%' OR `vs`.`address` LIKE '%"+words[i]+"%' OR `vs`.`title` LIKE '%"+words[i]+"%' OR `vs`.`name` LIKE '%"+words[i]+"%'";
    		}
    		whereCondition += ")";
    	}
    	String sortCondition = "`vs`.`id` DESC";
    	if(!Utility.isNullOrEmpty(sort)) {
    		if(sort.equalsIgnoreCase("oldest")) sortCondition = "`vs`.`id` ASC";
    	}
    	String listQuery = "SELECT vs.`id`, v.`first_name` `vendor.first_name`, v.`email` `vendor.email`, \r\n"
    			+ "vs.`title`, vs.`name`, vs.`address`, vs.`summary`, GROUP_CONCAT(vsi.image SEPARATOR ',') AS images  \r\n"
    			+ "FROM vendor_services `vs` \r\n"
    			+ "JOIN vendor `v` ON v.`id`=vs.`vendor`\r\n"
    			+ "JOIN vendor_service_images `vsi` ON vsi.`vendor_service`=vs.`id`\r\n"
    			+ "WHERE v.`category`=? " + whereCondition + "\r\n"
    			+ "GROUP BY vs.`id` ORDER BY " + sortCondition + ";";
    	System.out.println(listQuery);
    	// Query will return a list of results, each row is a Map
        List<Map<String, Object>> result = jdbcTemplate.queryForList(listQuery, category);
        return result;
    }
    
    public Map<String, Object> getEachService(int id) {
    	String selectQuery = "SELECT vs.`id`, CONCAT(v.`first_name`, ' ', v.`last_name`) `vendor_name`, v.`email` `vendor.email`, v.`category`, vs.`price`, vs.`guest`, \r\n"
    			+ "v.`image` `vendor_image`, vs.`title`, vs.`name`, vs.`address`, vs.`summary`, GROUP_CONCAT(vsi.image SEPARATOR ',') AS images  \r\n"
    			+ "FROM vendor_services `vs` \r\n"
    			+ "JOIN vendor `v` ON v.`id`=vs.`vendor`\r\n"
    			+ "JOIN vendor_service_images `vsi` ON vsi.`vendor_service`=vs.`id`\r\n"
    			+ "WHERE vs.`id`=?\r\n"
    			+ "GROUP BY vs.`id` ORDER BY vs.`id` DESC;";
    	// Query will return a list of results, each row is a Map
        Map<String, Object> result = jdbcTemplate.queryForMap(selectQuery, id);
        return result;
    }
    
    
}
