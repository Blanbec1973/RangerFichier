package org.heyner.rangerfichier.domain.services;

public class ReportService {
    StringBuilder rapport = new StringBuilder();

    public ReportService append(String text) {
        rapport.append(text);
        return this;
    }

    public void addTotalReport(int moveCounter) {
        if (moveCounter == 0) {
            rapport.append("Aucun fichier déplacé.");
        } else {
            rapport.append(moveCounter).append(" fichier(s) déplacé(s).");
        }
    }

    public String getReport() { return rapport.toString();}

}
