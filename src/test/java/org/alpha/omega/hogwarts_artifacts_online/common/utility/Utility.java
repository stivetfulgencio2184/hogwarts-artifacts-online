package org.alpha.omega.hogwarts_artifacts_online.common.utility;

import org.alpha.omega.hogwarts_artifacts_online.entity.Artifact;
import org.alpha.omega.hogwarts_artifacts_online.entity.Wizard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

public class Utility {

    public static List<Artifact> produceArtifacts(Integer quantity) {
        List<Artifact> artifacts = new ArrayList<>();

        for (int item = 0; item < quantity; item++) {
            Long id = (long) (item + 1);
            Artifact artifact = Artifact.builder()
                    .id(String.valueOf(id))
                    .name("This is name of the artifact: "+id)
                    .description("This is a description of the artifact: "+id)
                    .imageUrl("ImageUrl-"+id)
                    .build();
            artifacts.add(artifact);
        }
        return artifacts;
    }

    public static boolean areSameArtifacts(List<Artifact> expectedArtifacts, List<Artifact> artifactsToVerified) {
        List<Artifact> notFoundArtifacts = new ArrayList<>();

        expectedArtifacts.forEach(expectedArtifact -> {
            Artifact foundArtifact = artifactsToVerified
                    .stream().filter(artifactToVerified -> artifactToVerified.getId().equals(expectedArtifact.getId()))
                    .findAny()
                    .orElse(null);

            if(foundArtifact == null)
                notFoundArtifacts.add(expectedArtifact);
        });
        return notFoundArtifacts.isEmpty();
    }

    public static List<Wizard> produceWizards(Integer quantity) {
        List<Wizard> wizards = new ArrayList<>();
        IntStream.range(0, quantity).forEach(index ->
            wizards.add(Wizard.builder()
                    .id((long) (index + 1))
                            .name("The wizard name: "+(index + 1))
                            .artifacts(new HashSet<>(produceArtifacts(index+1)))
                    .build())
        );
        return wizards;
    }
}
