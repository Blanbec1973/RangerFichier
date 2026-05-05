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
        logger.info("File to parse with regexes : {}", fileName);

        return rules.stream()
                .filter(rule -> fileName.matches(rule.regex()))
                .peek(rule -> logger.info("Matched regex: {}", rule.regex()))
                .map(Rule::targetDirectory)
                .findFirst();
    }

    public int getSize() {
        return rules.size();
    }
}
