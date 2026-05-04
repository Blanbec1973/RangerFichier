package org.heyner.rangerfichier.infrastructure.sgbd;

import org.heyner.rangerfichier.shared.exceptions.DatabaseAccessException;
import org.heyner.common.Parameter;
import org.heyner.rangerfichier.domain.Rule;
import org.heyner.rangerfichier.domain.ports.RuleRepositoryPort;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RuleRepositoryAdapter implements RuleRepositoryPort {
    private final Connexion connexion;
    private final Parameter parameters;

    public RuleRepositoryAdapter(Parameter parameters, Connexion connexion) {
        this.parameters = parameters;
        this.connexion = connexion;
    }

//    public RuleRepositoryAdapter(Parameter parameters) {
//        this.parameters = parameters;
//        this.connexion = new Connexion(parameters.getProperty("url"));
//        connexion.connect();
//    }

    @Override
    public List<Rule> findAllRules() {
        List<Rule> rules = new ArrayList<>();
        String sql = parameters.getProperty("sql");
        try (Statement stmt = connexion.getConn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String regex = rs.getString("REGEX");
                String destinationDirectory = rs.getString("DOSSIERDEST");
                rules.add(new Rule(regex, destinationDirectory));
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Erreur lors de la récupération des règles", e);
        }
        return rules;
    }
}
