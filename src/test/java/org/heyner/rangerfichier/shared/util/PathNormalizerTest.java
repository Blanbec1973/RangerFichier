package org.heyner.rangerfichier.shared.util;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PathNormalizerTest {

    @Test
    void normalizeWithUserProfile() {
        PathNormalizer normalizer = new PathNormalizer(() -> "C:/Users/testuser");

        Path expected = Path.of("C:/Users/testuser", "Downloads", "Test");
        Path actual = normalizer.normalize("%USERPROFILE%/Downloads/Test");

        assertEquals(expected, actual);
    }

    @Test
    void normalizeWithTilde() {
        PathNormalizer normalizer = new PathNormalizer(() -> "C:/Users/testuser");

        Path expected = Path.of("C:/Users/testuser", "Downloads", "Test");
        Path actual = normalizer.normalize("~/Downloads/Test");

        assertEquals(expected, actual);
    }

    @Test
    void normalizeWithoutVariable() {
        PathNormalizer normalizer = new PathNormalizer(() -> "C:/Users/testuser");

        assertEquals(Path.of("C:/Temp/Test"), normalizer.normalize("C:/Temp/Test"));
    }

    @Test
    void normalizeNullThrowsException() {
        PathNormalizer normalizer = new PathNormalizer(() -> "C:/Users/testuser");

        assertThrows(IllegalArgumentException.class, () -> normalizer.normalize(null));
    }

    @Test
    void normalizeEmptyThrowsException() {
        PathNormalizer normalizer = new PathNormalizer(() -> "C:/Users/testuser");

        assertThrows(IllegalArgumentException.class, () -> normalizer.normalize(" "));
    }

//    @Test
//    void normalizeWithInvalidPathThrowsException() {
//        PathNormalizer normalizer = new PathNormalizer(() -> "C:/Users/testuser");
//        assertThrows(IllegalArgumentException.class, () -> normalizer.normalize(";InvalidPath"));
//    }
}