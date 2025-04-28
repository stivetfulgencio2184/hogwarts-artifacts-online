package org.alpha.omega.hogwarts_artifacts_online.common;

public class Constant {

    private Constant() {}

    public static class CustomExMessage {

        private CustomExMessage() {}

        public static final String INVALID_ARGUMENTS = "Provided arguments are invalid, see data for details.";

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
