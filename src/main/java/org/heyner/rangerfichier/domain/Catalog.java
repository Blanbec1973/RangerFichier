package org.heyner.rangerfichier.domain;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class Catalog {
    private final List<Rule> rules;
    private static final Logger logger = LogManager.getLogger(Catalog.class);

    public Catalog(List<Rule> rules) {
        this.rules = List.copyOf(rules);
        logger.info("Catalog loaded with {} rules", this.getSize());
    }

    public Optional<String> searchTargetDirectory(String fileName) {
        int i = 0;
        boolean found = false;
        logger.info("File to parse with regexes : {}", fileName);
        while (i < this.rules.size() && !found) {
            found = fileName.matches(this.rules.get(i).regex());
            String str = "i : " + i + " " + this.rules.get(i).regex() + " " + found;
            logger.info(str);
            i = i + 1;
        }

        return found ? Optional.of(this.rules.get(i - 1).targetDirectory()) : Optional.empty();
    }

    public int getSize() {
        return rules.size();
    }
}
