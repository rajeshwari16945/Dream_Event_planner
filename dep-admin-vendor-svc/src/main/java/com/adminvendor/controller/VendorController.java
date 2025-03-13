package com.adminvendor.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.adminvendor.service.VendorService;
import com.adminvendor.util.JwtUtil;
import com.adminvendor.util.Utility;
import com.adminvendor.validation.VendorValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/vendor")
public class VendorController {
	
	@Autowired
    private VendorService service;

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> saveVendor(@RequestBody Map<String, Object> vendorDetails) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Validate user input
	        VendorValidation.validateVendor(vendorDetails, false);
	        // Save user and get response
	        response = service.saveVendor(vendorDetails);
	        boolean success = !response.containsKey("errors");
	        // Build success response
	        finalResponse.put("success", success);
	        if (response.containsKey("uid")) finalResponse.put("uid", response.get("uid"));
	        if (response.containsKey("errors")) finalResponse.put("errors", response.get("errors"));
	        finalResponse.put("message", success ? "Saved successfully." : "Failed to save.");
	        return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace(); // Log the exception for debugging
	        finalResponse.put("success", false);
	        finalResponse.put("errors", "An unexpected error occurred: " + e.getMessage());
	        finalResponse.put("message", "Operation failed due to a server error.");
	        return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }        
	}
	
	@PostMapping("/update")
	public ResponseEntity<Map<String, Object>> updateVendor(@RequestPart("vendorDetails") String vendorDetailsStr,
	        @RequestPart(value = "image", required = false) MultipartFile image) throws JsonMappingException, JsonProcessingException {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		// Convert JSON string to Map
	    ObjectMapper objectMapper = new ObjectMapper();
	    Map<String, Object> vendorDetails = objectMapper.readValue(vendorDetailsStr, new TypeReference<Map<String, Object>>() {});
		try {
			// Validate user input
	        VendorValidation.validateVendor(vendorDetails, true);
	        // Save user and get response
	        response = service.updateVendor(vendorDetails, image);
	        boolean success = !response.containsKey("errors");
	        // Build success response
	        finalResponse.put("success", success);
	        if (response.containsKey("uid")) finalResponse.put("uid", response.get("uid"));
	        if (response.containsKey("errors")) finalResponse.put("errors", response.get("errors"));
	        finalResponse.put("message", success ? "Updated successfully." : "Failed to update.");
	        return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace(); // Log the exception for debugging
	        finalResponse.put("success", false);
	        finalResponse.put("errors", "An unexpected error occurred: " + e.getMessage());
	        finalResponse.put("message", "Operation failed due to a server error.");
	        return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }        
	}
	
	@PostMapping("/verify-email")
	public ResponseEntity<Map<String, Object>> verifyEmail(@RequestBody Map<String, Object> vendorDetails) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Validate user input
			VendorValidation.validateEmail(vendorDetails);
			//verify email and get response
			response = service.verifyEmail(vendorDetails);
	        boolean success = !response.containsKey("errors");
	        // Build success response
			finalResponse.put("success", success);
	        if (response.containsKey("errors")) finalResponse.put("errors", response.get("errors"));
			return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace(); // Log the exception for debugging
	        finalResponse.put("success", false);
	        finalResponse.put("errors", "An unexpected error occurred: " + e.getMessage());
	        finalResponse.put("message", "Operation failed due to a server error.");
	        return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    } 
	}
	
	@PostMapping("/verify-phone")
	public ResponseEntity<Map<String, Object>> verifyPhone(@RequestBody Map<String, Object> vendorDetails) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Validate user input
			VendorValidation.validatePhone(vendorDetails);
			//verify email and get response
			response = service.verifyPhone(vendorDetails);
	        boolean success = !response.containsKey("errors");
	        // Build success response
			finalResponse.put("success", success);
	        if (response.containsKey("errors")) finalResponse.put("errors", response.get("errors"));
			return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace(); // Log the exception for debugging
	        finalResponse.put("success", false);
	        finalResponse.put("errors", "An unexpected error occurred: " + e.getMessage());
	        finalResponse.put("message", "Operation failed due to a server error.");
	        return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    } 
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> vendorDetails, HttpServletResponse servletResponse) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			//validate user input
			VendorValidation.validateLogin(vendorDetails);
			//check if user exists
			response = service.login(vendorDetails);
			boolean success = !response.containsKey("errors");
			if(success) {
				String token = JwtUtil.generateToken((String) vendorDetails.get("email"));

		        // Set HTTP-only cookie
		        Cookie cookie = new Cookie("sessionToken", token);
		        cookie.setHttpOnly(true); // Prevent access via JavaScript
		        cookie.setSecure(false); // Set true for HTTPS, false for local development
		        cookie.setPath("/"); // Cookie is accessible across all endpoints
		        cookie.setMaxAge(3600); // Set expiration (1 hour)
		        servletResponse.addCookie(cookie); // Add cookie to response
		        
		        //save token
		        System.out.println(token);
		        service.saveToken((int) ((Map<String, Object>) response.get("data")).get("id"), token);
			}
	        // Build success response
			finalResponse.put("success", success);
	        if (response.containsKey("errors")) finalResponse.put("errors", response.get("errors"));
	        else finalResponse.put("data", response.get("data"));
			return new ResponseEntity<>(finalResponse, (success)? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
		} catch(Exception e) {
			e.printStackTrace(); // Log the exception for debugging
	        finalResponse.put("success", false);
	        finalResponse.put("errors", "An unexpected error occurred: " + e.getMessage());
	        finalResponse.put("message", "Operation failed due to a server error.");
	        return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    } 
	}
	
	@GetMapping("/session")
	public ResponseEntity<?> checkSession(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
	    Cookie[] cookies = request.getCookies();
	    boolean success = false;
		if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if ("sessionToken".equals(cookie.getName())) {
	                String token = cookie.getValue();
	                success = JwtUtil.validateToken(token);
	                if (success) {
	                	Map<String, Object> tokenData = service.getToken(token);
	                	if(!Utility.isNullOrEmpty(tokenData)) response = service.getVendor((int) tokenData.get("vendor"));
	                	System.out.println(response);
	                	finalResponse.put("success", success);
	            		finalResponse.put("message", "User data fetched successfully.");
	            		finalResponse.put("data", response.get("data"));
	            		System.out.println(finalResponse);
	            		return new ResponseEntity<>(finalResponse, HttpStatus.OK);
	                }
	            }
	        }
	    }
		 // Build success response
		finalResponse.put("success", success);
		finalResponse.put("message", "Invalid or expired session.");
		return new ResponseEntity<>(finalResponse, HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> getVendor(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			//get the user
			response = service.getVendor(id);
	        boolean success = !response.containsKey("errors");
	        // Build success response
			finalResponse.put("success", success);
	        if (response.containsKey("errors")) finalResponse.put("errors", response.get("errors"));
	        else finalResponse.put("data", response.get("data"));
			return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace(); // Log the exception for debugging
	        finalResponse.put("success", false);
	        finalResponse.put("errors", "An unexpected error occurred: " + e.getMessage());
	        finalResponse.put("message", "Operation failed due to a server error.");
	        return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    } 
	}
	
	@GetMapping("/logout")
	public ResponseEntity<Map<String, Object>> logout(HttpServletResponse response) {
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Clear the cookie
		    Cookie cookie = new Cookie("sessionToken", null);
		    cookie.setHttpOnly(true);
		    cookie.setSecure(false); // Set true for HTTPS
		    cookie.setPath("/");
		    cookie.setMaxAge(0); // Clear the cookie immediately
		    response.addCookie(cookie);
		    // Build success response
			finalResponse.put("success", true);
	        finalResponse.put("message", "Logged out successfully.");
			return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace(); // Log the exception for debugging
	        finalResponse.put("success", false);
	        finalResponse.put("errors", "An unexpected error occurred: " + e.getMessage());
	        finalResponse.put("message", "Operation failed due to a server error.");
	        return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/dashboard")
	public ResponseEntity<Map<String, Object>> dashboard(@RequestBody Map<String, Object> vendorDetails) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
	        // Save user and get response
	        response = service.getDashboardDetails((int)vendorDetails.get("id"));
	        boolean success = !response.containsKey("errors");
	        // Build success response
	        finalResponse.put("success", success);
	        finalResponse.put("data", response);
	        return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace(); // Log the exception for debugging
	        finalResponse.put("success", false);
	        finalResponse.put("errors", "An unexpected error occurred: " + e.getMessage());
	        finalResponse.put("message", "Operation failed due to a server error.");
	        return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }        
	}
	
}
