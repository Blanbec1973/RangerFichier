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
    private  final Parameter parametres;

    public RegleRepository(Connexion connexion, Parameter parametres) {
        this.connexion = connexion;
        this.parametres = parametres;
    }

    public List<Regle> findAllRegles() {
        List<Regle> regles = new ArrayList<>();
        String sql = parametres.getProperty("sql");
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
