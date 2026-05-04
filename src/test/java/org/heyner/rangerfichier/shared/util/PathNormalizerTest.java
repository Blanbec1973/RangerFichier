package org.heyner.rangerfichier.shared.util;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PathNormalizerTest {

    @Test
    void normalizeWithUserProfile() {
        String oldUserHome = System.getProperty("user.home");
        try {
            System.setProperty("user.home", "C:/Users/testuser");

            Path expected = Path.of("C:/Users/testuser", "Downloads", "Test");
            Path actual = PathNormalizer.normalize("%USERPROFILE%/Downloads/Test");

            assertEquals(expected, actual);
        } finally {
            System.setProperty("user.home", oldUserHome);
        }
    }

    @Test
    void normalizeWithoutVariable() {
        String raw = "C:/Temp/Test";
        Path path = PathNormalizer.normalize(raw);

        assertEquals(Path.of("C:/Temp/Test"), path);
    }

    @Test
    void normalizeNullThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> PathNormalizer.normalize(null));
    }

    @Test
    void normalizeEmptyThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> PathNormalizer.normalize(" "));
    }
}