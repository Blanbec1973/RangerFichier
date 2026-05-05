package org.heyner.rangerfichier.domain.services;

public class ReportBuilder {
    private StringBuilder rapport = new StringBuilder();

    public ReportBuilder append(String text) {
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

    public void clear() {
        rapport = new StringBuilder();
    }
}
