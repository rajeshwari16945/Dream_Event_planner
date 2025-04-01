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
import org.springframework.web.bind.annotation.RestController;
import com.adminvendor.service.BookingService;
import com.adminvendor.validation.BookingValidation;

@RestController
@RequestMapping("/booking")
public class BookingController {

	@Autowired
    private BookingService service;

	@PostMapping("/save")
	public ResponseEntity<Map<String, Object>> save(@RequestBody Map<String, Object> bookingDetails) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Validate user input
	        BookingValidation.validateBooking(bookingDetails, false);
	        // Save user and get response
	        response = service.save(bookingDetails);
	        boolean success = !response.containsKey("errors");
	        // Build success response
	        finalResponse.put("success", success);
	        if (response.containsKey("uid")) finalResponse.put("uid", response.get("uid"));
	        if (response.containsKey("errors")) finalResponse.put("errors", response.get("errors"));
	        finalResponse.put("message", success ? "Saved successfully." : response.get("errors"));
	        return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace(); // Log the exception for debugging
	        finalResponse.put("success", false);
	        finalResponse.put("errors", "An unexpected error occurred: " + e.getMessage());
	        finalResponse.put("message", "Operation failed due to a server error.");
	        return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }        
	}
	
	@PostMapping("/update-status")
	public ResponseEntity<Map<String, Object>> updateBookingStatus(@RequestBody Map<String, Object> bookingDetails) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Validate user input
	        BookingValidation.validateBookingStatus(bookingDetails);
	        // Save user and get response
	        response = service.updateBookingStatus(bookingDetails);
	        boolean success = !response.containsKey("errors");
	        // Build success response
	        finalResponse.put("success", success);
	        if (response.containsKey("uid")) finalResponse.put("uid", response.get("uid"));
	        if (response.containsKey("errors")) finalResponse.put("errors", response.get("errors"));
	        finalResponse.put("message", success ? "Saved successfully." : response.get("errors"));
	        return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace(); // Log the exception for debugging
	        finalResponse.put("success", false);
	        finalResponse.put("errors", "An unexpected error occurred: " + e.getMessage());
	        finalResponse.put("message", "Operation failed due to a server error.");
	        return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }        
	}
	
	@GetMapping("/count/{id}")
	public ResponseEntity<Map<String, Object>> getBookingCount(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
	        // Save user and get response
	        response = service.getBookingCount(id);
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
	
	@PostMapping("/listByVendor")
	public ResponseEntity<Map<String, Object>> listBookingsByVendor(@RequestBody Map<String, Object> bookingDetails) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Save user and get response
			response = service.listBookingsByVendor(bookingDetails);
			// Build success response
			finalResponse.put("success", true);
			finalResponse.put("message", "Fetched Succesfully");
			finalResponse.put("data", response.get("data"));
			return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception for debugging
			finalResponse.put("success", false);
			finalResponse.put("errors", "An unexpected error occurred: " + e.getMessage());
			return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/listByUser")
	public ResponseEntity<Map<String, Object>> listBookingsByUser(@RequestBody Map<String, Object> bookingDetails) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Save user and get response
			response = service.listBookingsByUser(bookingDetails);
			// Build success response
			finalResponse.put("success", true);
			finalResponse.put("message", "Fetched Succesfully");
			finalResponse.put("data", response.get("data"));
			return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception for debugging
			finalResponse.put("success", false);
			finalResponse.put("errors", "An unexpected error occurred: " + e.getMessage());
			return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> getEachBooking(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Save user and get response
			response = service.getEachBooking(id);
			// Build success response
			finalResponse.put("success", true);
			finalResponse.put("message", "Fetched Succesfully");
			finalResponse.put("data", response.get("data"));
			return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception for debugging
			finalResponse.put("success", false);
			finalResponse.put("message", "Operation failed due to a server error.");
			return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getDatesByService/{id}")
	public ResponseEntity<Map<String, Object>> getBookingDatesByService(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Save user and get response
			response = service.getBookingDatesByService(id);
			// Build success response
			finalResponse.put("success", true);
			finalResponse.put("message", "Fetched Succesfully");
			finalResponse.put("data", response.get("data"));
			return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception for debugging
			finalResponse.put("success", false);
			finalResponse.put("message", "Operation failed due to a server error.");
			return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
