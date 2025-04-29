package org.alpha.omega.hogwarts_artifacts_online.common.constant;

public class TestConstant {


    private TestConstant() {}

    public static final String ARTIFACT_ID = "10435344876";
    public static final Long WIZARD_ID = 1L;

    public static class Exception {

        private Exception() {}

        public static final Object INVALID_ARGUMENTS = "Provided arguments are invalid, see data for details.";

        public static class Artifact {

            private Artifact() {}

            public static final String NOT_FOUNT_ARTIFACT = "Could not find artifact with Id: %s";
        }

        public static class Wizard {

            private Wizard() {}

            public static final String NOT_FOUND_WIZARD = "Could not find wizard with Id: %s";
        }
    }
}
