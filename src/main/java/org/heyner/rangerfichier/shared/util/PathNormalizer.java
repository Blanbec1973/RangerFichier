package org.heyner.rangerfichier.shared.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathNormalizer {

    private PathNormalizer() {
        // utilitaire : pas d'instance
    }

    public static Path normalize(String rawPath) {
        if (rawPath == null || rawPath.isBlank()) {
            throw new IllegalArgumentException("Chemin invalide (null ou vide)");
        }

        String normalized = rawPath;

        if (rawPath.startsWith("%USERPROFILE%")) {
            String userHome = System.getProperty("user.home");
            normalized = userHome + rawPath.substring("%USERPROFILE%".length());
        }

        if (rawPath.startsWith("~")) {
            String userHome = System.getProperty("user.home");
            normalized = userHome + rawPath.substring("~".length());
        }

        return Paths.get(normalized);
    }
}
