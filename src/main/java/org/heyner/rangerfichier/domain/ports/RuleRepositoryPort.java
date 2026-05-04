package org.heyner.rangerfichier.domain.ports;

import org.heyner.rangerfichier.domain.Rule;

import java.util.List;

public interface RuleRepositoryPort {
    List<Rule> findAllRules();
}
