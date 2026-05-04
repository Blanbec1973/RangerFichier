package org.heyner.rangerfichier.domain;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class Catalog {
    private final List<Rule> rules;
    private static final Logger logger = LogManager.getLogger(Catalog.class);

    public Catalog(List<Rule> rules) {
        this.rules = List.copyOf(rules);
    }

    public Optional<Path> searchTargetDirectory(String fileName) {
        int i = 0;
        boolean trouve = false;
        logger.info("File to parse with regexs : {}", fileName);
        while (i < this.rules.size() && !trouve) {
            trouve = fileName.matches(this.rules.get(i).regex());
            String str = "i : " + i + " " + this.rules.get(i).regex() + " " + trouve;
            logger.info(str);
            i = i + 1;
        }
        Path destinationDirectory = null;
        if (trouve) {
            destinationDirectory = Path.of(this.rules.get(i - 1).destinationDirectory());
        }
        return trouve ? Optional.of(destinationDirectory) : Optional.empty();
    }
}
