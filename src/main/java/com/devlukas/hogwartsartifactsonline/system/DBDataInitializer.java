package com.devlukas.hogwartsartifactsonline.system;

import com.devlukas.hogwartsartifactsonline.artifact.Artifact;
import com.devlukas.hogwartsartifactsonline.artifact.ArtifactRepository;
import com.devlukas.hogwartsartifactsonline.wizard.Wizard;
import com.devlukas.hogwartsartifactsonline.wizard.WizardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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


        var a1 = generateArtifact("123451",
                "Deluminator",
                "The Deluminator,[1] also known as the Put-Outer,[3] was a magical device used by Albus Dumbledore " +
                        "to remove light sources from the Deluminator's immediate surroundings",
                "imageUrl");
        var a2 = generateArtifact("123452",
                "Invisibility Cloak",
                "An Invisibility Cloak was a magical " +
                        "garment which rendered whomever or whatever it covered invisible",
                "imageUrl");
        var a3 = generateArtifact("123453",
                "Elder Wand",
                "The Elder Wand was one of three magical objects that made up the fabled Deathly Hallows, " +
                        "along with the Resurrection Stone and the Cloak of Invisibility.",
                "imageUrl");
        var a4 = generateArtifact("123454",
                "The Marauder's Map",
                "The Marauder's Map was a magical document that revealed all of Hogwarts School of Witchcraft and Wizardry.",
                "imageUrl");
        var a5 = generateArtifact("123455",
                "The Sword Of Gryffindor",
                "The Sword of Gryffindor was a thousand-year-old, " +
                        "goblin-made magical sword owned by the famed wizard Godric Gryffindor, " +
                        "one of the four founders of Hogwarts School of Witchcraft and Wizardry", "imageUrl");
        var a6 = generateArtifact("123456",
                "The Resurrection Stone",
                "The Resurrection Stone was said to be the only object that would bring back the spirits of the holder's deceased loved ones", "imageUrl");

        var w1 = generateWizard(1, "Albus Dumbledore");
        w1.addArtifact(a1);
        w1.addArtifact(a3);

        var w2 = generateWizard(2, "Harry Potter");
        w2.addArtifact(a2);
        w2.addArtifact(a4);

        var w3 = generateWizard(3, "Nevile Longbottom");
        w3.addArtifact(a5);

        wizardRepository.save(w1);
        wizardRepository.save(w2);
        wizardRepository.save(w3);

        artifactRepository.save(a6);
    }

    private static Artifact generateArtifact(String id, String name, String description, String imageUrl) {
        var a = new Artifact();
        a.setId(id);
        a.setName(name);
        a.setDescription(description);
        a.setImageUrl(imageUrl);
        return a;
    }

    private static Wizard generateWizard(Integer id, String name) {
        var w = new Wizard();
        w.setId(id);
        w.setName(name);
        return w;
    }
}
