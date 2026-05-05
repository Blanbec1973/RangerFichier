package org.heyner.rangerfichier.domain.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class OperationFichierTest {

    @TempDir
    Path tempDir;

    @Test
    void moveShouldTransferFileToTargetDirectory() throws IOException {
        Path inDir = tempDir.resolve("in");
        Path outDir = tempDir.resolve("out");

        Path source = inDir.resolve("toto.txt");
        Files.createDirectories(inDir);
        Files.writeString(source, "hello");

        OperationFichier op = new OperationFichier();
        op.move(source, outDir);

        assertFalse(Files.exists(source));
        assertTrue(Files.exists(outDir.resolve("toto.txt")));
    }
}