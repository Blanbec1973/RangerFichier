package org.heyner.rangerfichier.domain.services;

public class ReportBuilder {
    private final StringBuilder report = new StringBuilder();

    public ReportBuilder append(String text) {
        report.append(text);
        return this;
    }

    public void addTotalReport(int moveCounter) {
        if (moveCounter == 0) {
            report.append("Aucun fichier déplacé.");
        } else {
            report.append(moveCounter).append(" fichier(s) déplacé(s).");
        }
    }

    public String getReport() { return report.toString();}

    public void clear() {
        report.setLength(0);
    }
}
