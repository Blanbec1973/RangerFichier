package org.heyner.rangerfichier.shared.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathNormalizer {

    public interface HomeProvider {
        String getUserHome();
    }

    public static final class SystemHomeProvider implements HomeProvider {
        @Override
        public String getUserHome() {
            return System.getProperty("user.home");
        }
    }

    private final HomeProvider homeProvider;

    public PathNormalizer(HomeProvider homeProvider) {
        if (homeProvider == null) {
            throw new IllegalArgumentException("homeProvider ne doit pas être null");
        }
        this.homeProvider = homeProvider;
    }

    public Path normalize(String rawPath) {
        if (rawPath == null || rawPath.isBlank()) {
            throw new IllegalArgumentException("Chemin invalide (null ou vide)");
        }

        String normalized = rawPath;

        if (rawPath.startsWith("%USERPROFILE%")) {
            normalized = homeProvider.getUserHome() + rawPath.substring("%USERPROFILE%".length());
        } else if (rawPath.startsWith("~")) {
            normalized = homeProvider.getUserHome() + rawPath.substring("~".length());
        }

        return Paths.get(normalized);
    }

    public static Path normalizeWithSystemHome(String rawPath) {
        return new PathNormalizer(new SystemHomeProvider()).normalize(rawPath);
    }
}