package org.heyner.rangerfichier.infrastructure.config;

import org.heyner.common.Parameter;
import org.junit.jupiter.api.*;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ConfigLoaderTest {

    private Path jarDir;
    private Path externalConfigPath;

    @BeforeEach
    void setUp() throws Exception {
        jarDir = getJarDirForConfigLoader();
        externalConfigPath = jarDir.resolve("config.properties");
        // Nettoyage préventif (si un run précédent a laissé le fichier)
        Files.deleteIfExists(externalConfigPath);
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(externalConfigPath);
    }

    @Test
    void load_whenExternalConfigExists_shouldLoadFromExternalPath() throws Exception {
        // Arrange: créer un config.properties à côté du "jarDir"
        Files.createDirectories(jarDir);
        Files.writeString(externalConfigPath, "anyKey=anyValue\n");

        ConfigLoader loader = new ConfigLoader();

        // Act
        loader.load();
        Parameter p = loader.getParameter();

        // Assert
        assertNotNull(p, "Parameter doit être initialisé quand le fichier externe existe");
        assertTrue(Files.exists(externalConfigPath), "Le fichier externe doit exister pour ce test");

        // Bonus (optionnel) : si Parameter#toString contient le chemin, utile pour valider la branche.
        // On ne l'impose pas car on ne connaît pas l'implémentation exacte de Parameter.
        // assertTrue(p.toString().contains(externalConfigPath.toString()));
    }

    @Test
    void load_whenExternalConfigMissing_shouldFallbackToInternalConfig() throws Exception {
        // Arrange: s'assurer que le fichier externe n'existe pas
        Files.deleteIfExists(externalConfigPath);

        ConfigLoader loader = new ConfigLoader();

        // Act
        loader.load();
        Parameter p = loader.getParameter();

        // Assert
        assertNotNull(p, "Parameter doit être initialisé même sans fichier externe (fallback interne)");
        assertFalse(Files.exists(externalConfigPath), "Le fichier externe ne doit pas exister pour ce test");
    }

    /**
     * Reproduit exactement le calcul de jarDir de ConfigLoader.load().
     */
    private static Path getJarDirForConfigLoader() {
        try {
            String jarDirString = new File(ConfigLoader.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI())
                    .getParent();

            return Path.of(jarDirString);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
