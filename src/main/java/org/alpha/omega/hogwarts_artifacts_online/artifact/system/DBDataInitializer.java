package org.alpha.omega.hogwarts_artifacts_online.artifact.system;

import org.alpha.omega.hogwarts_artifacts_online.artifact.entity.Artifact;
import org.alpha.omega.hogwarts_artifacts_online.artifact.entity.Wizard;
import org.alpha.omega.hogwarts_artifacts_online.artifact.repository.ArtifactRepository;
import org.alpha.omega.hogwarts_artifacts_online.artifact.repository.WizardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;
    private final WizardRepository wizardRepository;

    public DBDataInitializer(ArtifactRepository artifactRepository, WizardRepository wizardRepository) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        Artifact firstArtifact = Artifact.builder()
                .id("425344871")
                .name("Deluminator")
                .description("A deluminator is a device invented by Albus ")
                .imageUrl("imageUrl")
                .build();
        Artifact secondArtifact = Artifact.builder()
                .id("425344872")
                .name("Invisibility Cloak")
                .description("A invisibility cloak is used to make the wearer")
                .imageUrl("imageUrl")
                .build();
        Artifact thirdArtifact = Artifact.builder()
                .id("425344873")
                .name("Elder Wand")
                .description("The Elder Wand, know throughout history as the")
                .imageUrl("imageUrl")
                .build();
        Artifact fourthArtifact = Artifact.builder()
                .id("425344874")
                .name("The Marauder's Map")
                .description("A magical map of Hogwarts created by Remus Lupin")
                .imageUrl("imageUrl")
                .build();
        Artifact fifthArtifact = Artifact.builder()
                .id("425344875")
                .name("The Sword of Gryffindor")
                .description("A goblin-made sword adorned with large rubies on")
                .imageUrl("imageUrl")
                .build();
        Artifact sixthArtifact = Artifact.builder()
                .id("425344876")
                .name("Resurrection stone")
                .description("The resurrection stone allows the holder to")
                .imageUrl("imageUrl")
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

        this.wizardRepository.saveAll(Arrays.asList(firstWizard, secondWizard, thirdWizard));
        this.artifactRepository.save(sixthArtifact);
    }
}
