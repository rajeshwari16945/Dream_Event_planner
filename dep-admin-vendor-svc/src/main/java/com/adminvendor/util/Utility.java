package com.adminvendor.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Utility {

	private static final String UPLOAD_DIR = "C://UPLOADS/";

	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null) {
			return true; // Null check
		}
		if (obj instanceof String) {
			return ((String) obj).trim().isEmpty(); // Check for empty or whitespace strings
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).isEmpty(); // Check for empty collections (e.g., List, Set)
		}
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).isEmpty(); // Check for empty maps
		}
		if (obj.getClass().isArray()) {
			return ((Object[]) obj).length == 0; // Check for empty arrays
		}
		return false; // Non-null and non-empty for unsupported types
	}

	public static boolean isValidEmail(String email) {
		String regex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
		if (email == null || email.trim().isEmpty()) {
			return false; // Null or empty check
		}
		return Pattern.matches(regex, email);
	}

	public static boolean isValidPhoneNumber(String phoneNumber) {
		String regex = "^[+]?[0-9]{10,15}$";
		if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
			return false; // Null or empty check
		}
		return Pattern.matches(regex, phoneNumber);
	}

	public static String singleFileUpload(MultipartFile image) {
		String filePath = null;
		if (image != null && !image.isEmpty()) {
			filePath = UPLOAD_DIR + image.getOriginalFilename();
			File file = new File(filePath);
			// Ensure the directory exists before saving the file
			File directory = new File(file.getParent());
			if (!directory.exists()) {
				directory.mkdirs(); // Creates the directory structure if it doesn't exist
			}
			try {
				image.transferTo(file); // Save image to "uploads/" folder
				return filePath;
			} catch (IOException e) {
				throw new RuntimeException("Failed to save image: " + e.getMessage());
			}
		}
		return filePath;
	}

	public static List<String> multipleFileUpload(List<MultipartFile> images) {
		List<String> imagePaths = new ArrayList<>();
		for (MultipartFile image : images) {
			imagePaths.add(singleFileUpload(image));
		}
		return imagePaths;
	}

}
