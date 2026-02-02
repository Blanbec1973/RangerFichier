package model;

import exceptions.DatabaseAccessException;
import org.heyner.common.Parameter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RegleRepository {
    private final Connexion connexion;
    private final Parameter parameters;

    public RegleRepository(Parameter parameters, Connexion connexion) {
        this.parameters = parameters;
        this.connexion = connexion;
    }

    public RegleRepository(Parameter parameters) {
        this.parameters = parameters;
        this.connexion = new Connexion(parameters.getProperty("url"));
        connexion.connect();
    }

    public List<Regle> findAllRegles() {
        List<Regle> regles = new ArrayList<>();
        String sql = parameters.getProperty("sql");
        try (Statement stmt = connexion.getConn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String regex = rs.getString("REGEX");
                String dossier = rs.getString("DOSSIERDEST");
                regles.add(new Regle(regex, dossier));
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Erreur lors de la récupération des règles", e);
        }
        return regles;
    }
}
