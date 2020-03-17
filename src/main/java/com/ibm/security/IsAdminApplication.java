package com.ibm.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@EnableZuulProxy
public class IsAdminApplication {
	
	private static Logger log = LoggerFactory.getLogger(IsAdminApplication.class);
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@PostMapping("/logout")
	public void logout(HttpServletRequest request){
		request.getSession().invalidate();
	}
	
	@GetMapping("/me")
	public TokenInfo me(HttpServletRequest request) {
		TokenInfo info = (TokenInfo)request.getSession().getAttribute("token");
		return info;
	}
	
	// no used, be replaced by auth server login
//	@PostMapping("/login")
//	public void login(@RequestBody Credentials credentials, HttpServletRequest request) {
////		String token = StringUtils.substringAfter(authHeader, "bearer ");
//		String oauthServiceUrl = "http://localhost:9070/token/oauth/token";
//		
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//		headers.setBasicAuth("admin", "123456");
//		
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//		params.add("username", credentials.getUsername());
//		params.add("password", credentials.getPassword());
//		params.add("grant_type", "password");
//		params.add("scope", "read write");
//		
//		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
//		
//		ResponseEntity<TokenInfo> response = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
//		request.getSession().setAttribute("token", response.getBody());
//		
//	}
	
	@GetMapping("/oauth/callback")
	public void callback (@RequestParam String code, String state, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		log.info("state is "+state);
		
		String oauthServiceUrl = "http://gateway.ibm.com:9070/token/oauth/token";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth("admin", "123456");
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", code);
		params.add("grant_type", "authorization_code");
		params.add("redirect_uri", "http://admin.ibm.com:8080/oauth/callback");
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		
		ResponseEntity<TokenInfo> token = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
//		request.getSession().setAttribute("token", token.getBody());
		// after add refresh token why  token.getBody().init()?
		request.getSession().setAttribute("token", token.getBody().init());
		// should according state to decided where page to go
		response.sendRedirect("/");
	}
		
	public static void main(String[] args) {
		SpringApplication.run(IsAdminApplication.class, args);
	}

}
