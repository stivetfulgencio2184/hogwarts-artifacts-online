package org.alpha.omega.hogwarts_artifacts_online.common.constant;

public class TestConstant {


    private TestConstant() {}

    public static final String ARTIFACT_ID = "10435344876";
    public static final Long WIZARD_ID = 1L;
    public static final String ARTIFACT = "artifact";
    public static final String WIZARD = "wizard";

    public static class Exception {

        private Exception() {}

        public static final Object INVALID_ARGUMENTS = "Provided arguments are invalid, see data for details.";
        public static final String NOT_FOUND_OBJECT = "Could not find %s with Id: %s";
    }
}
