package com.m15.clicknbuy.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryHelper {

	public String saveToCloudinary(MultipartFile image) {
		try {
			if (image.isEmpty()) {
				return "https://placehold.co/600x400";
			}
			
			String fileName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
			Path uploadPath = Paths.get("./uploads");
			
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			try (InputStream inputStream = image.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				return "/uploads/" + fileName;
			} catch (IOException e) {
				e.printStackTrace();
				return "https://placehold.co/600x400";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "https://placehold.co/600x400";
		}
	}
}
