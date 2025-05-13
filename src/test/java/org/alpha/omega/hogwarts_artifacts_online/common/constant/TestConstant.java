package org.alpha.omega.hogwarts_artifacts_online.common.constant;

public class TestConstant {

    private TestConstant() {}

    public static final String ARTIFACT_ID = "10435344876";
    public static final Long WIZARD_ID = 1L;
    public static final Integer USER_ID = 1;
    public static final String ARTIFACT = "artifact";
    public static final String WIZARD = "wizard";
    public static final String USER = "user";

    public static class Exception {

        private Exception() {}

        public static final Object INVALID_ARGUMENTS = "Provided arguments are invalid, see data for details.";
        public static final String NOT_FOUND_OBJECT = "Could not find %s with Id: %s";
        public static final String ALREADY_REGISTERED_OBJECT = "The %s with %s already registered.";
    }
}
