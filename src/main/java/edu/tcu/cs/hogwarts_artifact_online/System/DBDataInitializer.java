package edu.tcu.cs.hogwarts_artifact_online.System;

import edu.tcu.cs.hogwarts_artifact_online.Artifact.Artifact;
import edu.tcu.cs.hogwarts_artifact_online.Artifact.ArtifactRepository;
import edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.HogwartsUser;
import edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.UserService;
import edu.tcu.cs.hogwarts_artifact_online.Wizard.Wizard;
import edu.tcu.cs.hogwarts_artifact_online.Wizard.WizardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
//@Profile("dev")
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;

    private final WizardRepository wizardRepository;

    private final UserService userService;

    public DBDataInitializer(ArtifactRepository artifactRepository, WizardRepository wizardRepository, UserService userService) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        Artifact a1 = new Artifact();
        a1.setId("125732919271923");
        a1.setName("Invisibility Cloak");
        a1.setDescription("Invisibility Cloak is used to make the wearer invisible");
        a1.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("125732919271924");
        a2.setName("Ring of Power");
        a2.setDescription("The Ring of Power grants immense strength to its wearer");
        a2.setImageUrl("ImageUrl");

        Artifact a3 = new Artifact();
        a3.setId("125732919271925");
        a3.setName("Staff of Wisdom");
        a3.setDescription("The Staff of Wisdom enhances the magical abilities of its wielder");
        a3.setImageUrl("ImageUrl");

        Artifact a4 = new Artifact();
        a4.setId("125732919271926");
        a4.setName("Boots of Swiftness");
        a4.setDescription("Boots of Swiftness increase the speed and agility of the wearer");
        a4.setImageUrl("ImageUrl");

        Artifact a5 = new Artifact();
        a5.setId("125732919271927");
        a5.setName("Shield of Invincibility");
        a5.setDescription("The Shield of Invincibility provides unparalleled defense in battle");
        a5.setImageUrl("ImageUrl");

        Artifact a6 = new Artifact();
        a6.setId("125732919271928");
        a6.setName("Helm of Insight");
        a6.setDescription("The Helm of Insight grants the ability to see through illusions and deceptions");
        a6.setImageUrl("ImageUrl");

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        w1.addArtifact(a1);
        w1.addArtifact(a3);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a2);
        w2.addArtifact(a4);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Ron Weasley");
        w3.addArtifact(a5);

        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("tony");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");

        wizardRepository.save(w1);
        wizardRepository.save(w2);
        wizardRepository.save(w3);

        artifactRepository.save(a6);

        userService.addUser(u1);
        userService.addUser(u2);
        userService.addUser(u3);
    }
}
