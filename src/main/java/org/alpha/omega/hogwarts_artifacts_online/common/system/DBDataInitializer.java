package org.alpha.omega.hogwarts_artifacts_online.common.system;

import org.alpha.omega.hogwarts_artifacts_online.entity.Artifact;
import org.alpha.omega.hogwarts_artifacts_online.entity.Role;
import org.alpha.omega.hogwarts_artifacts_online.entity.User;
import org.alpha.omega.hogwarts_artifacts_online.entity.Wizard;
import org.alpha.omega.hogwarts_artifacts_online.artifact.repository.ArtifactRepository;
import org.alpha.omega.hogwarts_artifacts_online.role.UserRole;
import org.alpha.omega.hogwarts_artifacts_online.role.repository.RoleRepository;
import org.alpha.omega.hogwarts_artifacts_online.user.service.UserService;
import org.alpha.omega.hogwarts_artifacts_online.wizard.repository.WizardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;
    private final WizardRepository wizardRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;

    public DBDataInitializer(ArtifactRepository artifactRepository, WizardRepository wizardRepository, UserService userService, RoleRepository roleRepository) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }


    @Transactional
    @Override
    public void run(String... args) throws Exception {
        Artifact firstArtifact = Artifact.builder()
                .id("425344871")
                .name("Deluminator")
                .description("A deluminator is a device invented by Albus ")
                .imageUrl("first imageUrl")
                .build();
        Artifact secondArtifact = Artifact.builder()
                .id("425344872")
                .name("Invisibility Cloak")
                .description("A invisibility cloak is used to make the wearer")
                .imageUrl("second imageUrl")
                .build();
        Artifact thirdArtifact = Artifact.builder()
                .id("425344873")
                .name("Elder Wand")
                .description("The Elder Wand, know throughout history as the")
                .imageUrl("third imageUrl")
                .build();
        Artifact fourthArtifact = Artifact.builder()
                .id("425344874")
                .name("The Marauder's Map")
                .description("A magical map of Hogwarts created by Remus Lupin")
                .imageUrl("fourth imageUrl")
                .build();
        Artifact fifthArtifact = Artifact.builder()
                .id("425344875")
                .name("The Sword of Gryffindor")
                .description("A goblin-made sword adorned with large rubies on")
                .imageUrl("fifth imageUrl")
                .build();
        Artifact sixthArtifact = Artifact.builder()
                .id("425344876")
                .name("Resurrection stone")
                .description("The resurrection stone allows the holder to")
                .imageUrl("sixth imageUrl")
                .build();

        Wizard firstWizard = Wizard.builder()
                .name("Albus Dumbledore")
                .artifacts(new HashSet<>())
                .build();
        firstWizard.addArtifact(firstArtifact);
        firstWizard.addArtifact(thirdArtifact);

        Wizard secondWizard = Wizard.builder()
                .name("Harry Potter")
                .artifacts(new HashSet<>())
                .build();
        secondWizard.addArtifact(secondArtifact);
        secondWizard.addArtifact(fourthArtifact);

        Wizard thirdWizard = Wizard.builder()
                .name("Neville Longbottom")
                .artifacts(new HashSet<>())
                .build();
        thirdWizard.addArtifact(fifthArtifact);

        Role adminRole = Role.builder()
                .name(UserRole.ADMIN.name())
                .build();

        Role userRole = Role.builder()
                .name(UserRole.USER.name())
                .build();

        Role sysAdminRole = Role.builder()
                .name(UserRole.SYS_ADMIN.name())
                .build();
        this.roleRepository.saveAll(Arrays.asList(adminRole, userRole, sysAdminRole));

        this.userService.saveUser(User.builder()
                .username("admin")
                .password("$$SadracFul21")
                .enabled(Boolean.TRUE)
                .userRoles(Set.of(adminRole))
                .build());

        this.userService.saveUser(User.builder()
                .username("sfulgencio")
                .password("$$StivetFul2184$$")
                .enabled(Boolean.TRUE)
                .userRoles(Set.of(sysAdminRole, userRole))
                .build());

        this.userService.saveUser(User.builder()
                .username("jarce")
                .password("$#jdarce#$")
                .enabled(Boolean.FALSE)
                .userRoles(Set.of(userRole))
                .build());

        this.wizardRepository.saveAll(Arrays.asList(firstWizard, secondWizard, thirdWizard));
        this.artifactRepository.save(sixthArtifact);
    }
}
