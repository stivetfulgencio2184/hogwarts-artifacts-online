package org.alpha.omega.hogwarts_artifacts_online.common;

public class Constant {

    private Constant() {}

    public static final String ARTIFACT = "artifact";
    public static final String WIZARD = "wizard";
    public static final String USER = "user";
    public static final String ID = "Id";
    public static final String USERNAME = "username";

    public static class Security {

        private Security() {}

        public static final String WILDCARD_URL_USER = "/users/**";
        public static final String USER_INFO = "userInfo";
        public static final String TOKEN = "token";
        public static final Integer KEY_SIZE = 2048;

        public static class Jwt {

            private Jwt() {}

            public static final String ISSUER = "self";
            public static final String CLAIM_NAME = "authorities";
        }

        public static class ExceptionMessage {

            private ExceptionMessage() {}

            public static final String USERNAME_PASSWORD_INCORRECT = "Username or password is incorrect.";
            public static final String USER_ACCOUNT_ABNORMAL = "User account is abnormal.";
            public static final String INVALID_BEARER_TOKEN = "The access token provided is expired, revoked, malformed or invalid for other reasons.";
            public static final String NO_PERMISSION = "No permission.";
            public static final String INTERNAL_SERVER_ERROR = "A server internal error occurs.";
        }
    }

    public static class CustomExMessage {

        private CustomExMessage() {}

        public static final String INVALID_ARGUMENTS = "Provided arguments are invalid, see data for details.";
        public static final String NOT_FOUND_OBJECT = "Could not find %s with %s: %s";
        public static final String ALREADY_REGISTERED_OBJECT = "The %s with %s already registered.";
        public static final String USERNAME_NOT_FOUND = "Username: %s is not found.";
    }
}
