package TestAutenticazione;

import it.unisa.c07.biblionet.BiblionetApplication;
import org.springframework.modulith.core.ApplicationModules;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.docs.Documenter;

public class ApplicationModularityTest {
    ApplicationModules modules = ApplicationModules.of(BiblionetApplication.class);

    @Test
    void verifiesModularStructure() {
        modules.verify();
    }

    @Test
    void createModuleDocumentation() {
        new Documenter(modules)
                .writeDocumentation()
                .writeIndividualModulesAsPlantUml();
    }

    @Test
    void createApplicationModuleModel() {
        modules.forEach(System.out::println);
    }
}