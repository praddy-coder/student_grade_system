package com.smartgrade.dto;

public class LoginResponse {
    private String token;
    private String role;
    private String username;
    private String refId;
    private String name;

    public LoginResponse() {}
    public LoginResponse(String token, String role, String username, String refId, String name) {
        this.token = token;
        this.role = role;
        this.username = username;
        this.refId = refId;
        this.name = name;
    }

    public static LoginResponseBuilder builder() { return new LoginResponseBuilder(); }

    public String getToken() { return token; }
    public String getRole() { return role; }
    public String getUsername() { return username; }
    public String getRefId() { return refId; }
    public String getName() { return name; }

    public static class LoginResponseBuilder {
        private String token;
        private String role;
        private String username;
        private String refId;
        private String name;
        public LoginResponseBuilder token(String token) { this.token = token; return this; }
        public LoginResponseBuilder role(String role) { this.role = role; return this; }
        public LoginResponseBuilder username(String username) { this.username = username; return this; }
        public LoginResponseBuilder refId(String refId) { this.refId = refId; return this; }
        public LoginResponseBuilder name(String name) { this.name = name; return this; }
        public LoginResponse build() { return new LoginResponse(token, role, username, refId, name); }
    }
}
