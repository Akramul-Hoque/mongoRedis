package com.example.mongoRedis.common.ApiEndpoints;

public final class ApiEndpoints {

    private ApiEndpoints() {} // prevent instantiation

    public static class Auth {
        public static final String BASE = "/auth";
        public static final String SIGNUP = BASE + "/signup";
        public static final String LOGIN = BASE + "/login";
        public static final String REFRESH = BASE + "/refresh";
        public static final String LOGOUT = BASE + "/logout";
    }

    public static class User {
        public static final String BASE = "/users";
        public static final String GET = BASE + "/{id}";
        public static final String CREATE = BASE + "/create";
    }
    // add more endpoints here
}