package com.backend.mobicomm.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.mobicomm.model.User;
import com.backend.mobicomm.security.JwtUtil;
import com.backend.mobicomm.service.UserService;

@RestController
@RequestMapping("/userdash")
public class UserdashController 
{
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserService userService;
	   

	@GetMapping("/details")
	public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String authHeader) {
	    String token = authHeader.substring(7); 
	    String email = jwtUtil.extractEmail(token);

	    Optional<User> optionalUser = userService.findByEmail(email);
	    if (optionalUser.isPresent()) {
	        User user = optionalUser.get();
	        Map<String, String> userData = new HashMap<>();
	        userData.put("name", user.getName());
	        userData.put("email", user.getEmail());
	        userData.put("mobileNumber", user.getMobileNumber());
	        return ResponseEntity.ok(userData);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found."));
	    }
	}
}
