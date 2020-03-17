package com.ibm.security;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class TokenInfo {

	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	private String access_token;
	private String token_type;
	private Long expires_in;
	private String scope;
	
	private String refresh_token;
	
	public Long getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	private LocalDateTime expireTime;
	
	public TokenInfo init() {
		expireTime = LocalDateTime.now().plusSeconds(expires_in - 3);
		return this;
	}

	@JsonIgnore
	public boolean isExpired() {
		return expireTime.isBefore(LocalDateTime.now());
	}
	
}
