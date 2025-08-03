package org.alpha.omega.hogwarts_artifacts_online;

import org.alpha.omega.hogwarts_artifacts_online.artifact.controller.ArtifactControllerIntegrationTest;
import org.alpha.omega.hogwarts_artifacts_online.artifact.controller.ArtifactControllerTest;
import org.alpha.omega.hogwarts_artifacts_online.artifact.service.ArtifactServiceTest;
import org.alpha.omega.hogwarts_artifacts_online.user.controller.UserControllerIntegrationTest;
import org.alpha.omega.hogwarts_artifacts_online.user.service.UserServiceTest;
import org.alpha.omega.hogwarts_artifacts_online.wizard.controller.WizardControllerIntegrationTest;
import org.alpha.omega.hogwarts_artifacts_online.wizard.controller.WizardControllerTest;
import org.alpha.omega.hogwarts_artifacts_online.wizard.service.WizardServiceTest;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestClassOrder;
import org.springframework.test.context.ActiveProfiles;

/*
* To skip running tests from base test classes make that base test class abstract and then
* define a class as this where we're extends each abstract test class, as show here, and define there order of execution.
* It’s useful when we want to have some explicit control over execution order or
* need to run tests in a specific sequence.
* */
@TestClassOrder(value = ClassOrderer.OrderAnnotation.class)
@ActiveProfiles(value = "dev")
class ExecutionOrderTest {

    @Nested
    @Order(value = 1)
    class ArtifactControllerIntegrationTests extends ArtifactControllerIntegrationTest {}

    @Nested
    @Order(value = 2)
    class ArtifactControllerTests extends ArtifactControllerTest {}

    @Nested
    @Order(value = 3)
    class ArtifactServiceTests extends ArtifactServiceTest {}

    @Nested
    @Order(value = 4)
    class WizardControllerIntegrationTests extends WizardControllerIntegrationTest {}

    @Nested
    @Order(value = 5)
    class WizardControllerTests extends WizardControllerTest {}

    @Nested
    @Order(value = 6)
    class WizardServiceTests extends WizardServiceTest {}

    @Nested
    @Order(value = 7)
    class UserControllerIntegrationTests extends UserControllerIntegrationTest {}

    @Nested
    @Order(value = 8)
    class UserControllerTests extends ArtifactControllerTest {}

    @Nested
    @Order(value = 9)
    class UserServiceTests extends UserServiceTest{}
}
