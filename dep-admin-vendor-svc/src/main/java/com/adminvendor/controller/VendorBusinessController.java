package com.adminvendor.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.adminvendor.service.VendorBusinessService;
import com.adminvendor.validation.VendorServiceValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/vendor-service")
public class VendorBusinessController {

	@Autowired
	private VendorBusinessService service;

	@PostMapping("/save")
	public ResponseEntity<Map<String, Object>> saveVendorService(@RequestPart("serviceDetails") String serviceDetailsStr,
	        @RequestPart("images") List<MultipartFile> images) throws JsonMappingException, JsonProcessingException {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		// Convert JSON string to Map
	    ObjectMapper objectMapper = new ObjectMapper();
	    Map<String, Object> serviceDetails = objectMapper.readValue(serviceDetailsStr, new TypeReference<Map<String, Object>>() {});
		try {
			// Validate user input
			VendorServiceValidation.validateService(serviceDetails, false);
			// Save user and get response
			response = service.saveVendorService(serviceDetails, images);
			boolean success = !response.containsKey("errors");
			// Build success response
			finalResponse.put("success", success);
			if (response.containsKey("uid"))
				finalResponse.put("uid", response.get("uid"));
			if (response.containsKey("errors"))
				finalResponse.put("errors", response.get("errors"));
			finalResponse.put("message", success ? "Saved successfully." : "Failed to save.");
			return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception for debugging
			finalResponse.put("success", false);
			finalResponse.put("errors", "An unexpected error occurred: " + e.getMessage());
			finalResponse.put("message", "Operation failed due to a server error.");
			return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/update")
	public ResponseEntity<Map<String, Object>> updateVendorService(@RequestBody Map<String, Object> serviceDetails) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Validate user input
			VendorServiceValidation.validateService(serviceDetails, true);
			// Save user and get response
			response = service.updateVendorService(serviceDetails);
			boolean success = !response.containsKey("errors");
			// Build success response
			finalResponse.put("success", success);
			if (response.containsKey("uid"))
				finalResponse.put("uid", response.get("uid"));
			if (response.containsKey("errors"))
				finalResponse.put("errors", response.get("errors"));
			finalResponse.put("message", success ? "Updated successfully." : "Failed to update.");
			return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception for debugging
			finalResponse.put("success", false);
			finalResponse.put("errors", "An unexpected error occurred: " + e.getMessage());
			finalResponse.put("message", "Operation failed due to a server error.");
			return new ResponseEntity<>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/listByVendor")
	public ResponseEntity<Map<String, Object>> listServicesByVendor(@RequestBody Map<String, Object> serviceDetails) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Save user and get response
			response = service.listServicesByVendor(serviceDetails);
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
	
	@PostMapping("/listByCategory")
	public ResponseEntity<Map<String, Object>> listServicesByCategory(@RequestBody Map<String, Object> serviceDetails) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Save user and get response
			response = service.listServicesByCategory(serviceDetails);
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
	public ResponseEntity<Map<String, Object>> getEachService(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Save user and get response
			response = service.getEachService(id);
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
	
	// Serve image file from file system
	@GetMapping("/images/{filename}")
	public ResponseEntity<Resource> getImage(@PathVariable String filename) {
	    try {
	    	String filePath = "C://UPLOADS/" + filename;
	        File file = new File(filePath);
	        // Debugging: Print the file path
	        System.out.println("Checking file path: " + filePath);
	        if (!file.exists()) {
	            System.out.println("File not found: " + filePath);
	            return ResponseEntity.notFound().build();
	        }
	        // Determine the mime type of the image
	        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
	        if (mimeType == null) {
	            mimeType = Files.probeContentType(file.toPath());
	        }
	        // If mimeType is still null, default to JPEG
	        if (mimeType == null) {
	            mimeType = "image/jpeg";
	        }
	        // Create a resource from the file
	        Resource resource = new UrlResource(file.toURI());
	        // Return the image with the correct Content-Type
	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(mimeType))  // Dynamically set content type
	                .body(resource);
	    } catch (IOException e) {
	        System.out.println("Error loading file: " + e.getMessage());
	        return ResponseEntity.internalServerError().build();
	    }
	}

}
