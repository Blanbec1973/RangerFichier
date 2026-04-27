package model.util;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PathNormalizerTest {

    @Test
    void normalizeWithUserProfile() {
        String raw = "%USERPROFILE%/Downloads/Test";
        Path path = PathNormalizer.normalize(raw);

        assertTrue(path.toString().contains(System.getProperty("user.home")));
        assertTrue(path.endsWith("Downloads/Test"));
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