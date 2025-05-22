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
    }

    public static class CustomExMessage {

        private CustomExMessage() {}

        public static final String INVALID_ARGUMENTS = "Provided arguments are invalid, see data for details.";
        public static final String NOT_FOUND_OBJECT = "Could not find %s with %s: %s";
        public static final String ALREADY_REGISTERED_OBJECT = "The %s with %s already registered.";
        public static final String USERNAME_NOT_FOUND = "Username: %s is not found.";
    }
}
