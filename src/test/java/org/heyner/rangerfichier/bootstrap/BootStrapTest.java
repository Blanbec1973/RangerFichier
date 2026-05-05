package org.heyner.rangerfichier.bootstrap;

import org.heyner.common.Parameter;
import org.heyner.rangerfichier.infrastructure.config.ConfigLoader;
import org.heyner.rangerfichier.infrastructure.sgbd.Connexion;
import org.heyner.rangerfichier.infrastructure.sgbd.RuleRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BootStrapTest {

    @Test
    void createApp_shouldBuildAndReturnApp() {
        // Arrange
        Parameter mockParameters = mock(Parameter.class);
        when(mockParameters.getVersion()).thenReturn("1.0-test");
        when(mockParameters.getProperty("url")).thenReturn("jdbc:test://localhost");

        // Mock de new ConfigLoader() + comportement load()/getParameter()
        try (MockedConstruction<ConfigLoader> mockedConfigLoader =
                     mockConstruction(ConfigLoader.class, (mock, context) -> {
                         doNothing().when(mock).load();
                         when(mock.getParameter()).thenReturn(mockParameters);
                     });

             // Mock de new Connexion(url) dans le try-with-resources
             MockedConstruction<Connexion> mockedConnexion =
                     mockConstruction(Connexion.class, (mock, context) -> {
                         // Rien à faire : juste éviter une vraie connexion
                     });

             // Mock de new RuleRepositoryAdapter(parameters, connexion)
             MockedConstruction<RuleRepositoryAdapter> mockedRepoAdapter =
                     mockConstruction(RuleRepositoryAdapter.class, (mock, context) -> when(mock.findAllRules()).thenReturn(Collections.emptyList()))
        ) {
            BootStrap bootStrap = new BootStrap();

            // Act
            RangerFichierApp app = bootStrap.createApp();

            // Assert
            assertNotNull(app, "createApp() doit retourner une application non nulle");

            // Vérifie que ConfigLoader a été construit et utilisé
            assertEquals(1, mockedConfigLoader.constructed().size(), "ConfigLoader doit être instancié une fois");
            ConfigLoader usedLoader = mockedConfigLoader.constructed().get(0);
            verify(usedLoader, times(1)).load();
            verify(usedLoader, times(1)).getParameter();

            // Vérifie que Connexion a été instanciée avec l'URL provenant des paramètres
            assertEquals(1, mockedConnexion.constructed().size(), "Connexion doit être instanciée une fois");

            // Vérifie que le repository adapter a été instancié et interrogé
            assertEquals(1, mockedRepoAdapter.constructed().size(), "RuleRepositoryAdapter doit être instancié une fois");
            RuleRepositoryAdapter usedAdapter = mockedRepoAdapter.constructed().get(0);
            verify(usedAdapter, times(1)).findAllRules();
        }
    }
}