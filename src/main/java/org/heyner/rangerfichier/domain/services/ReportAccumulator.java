package org.heyner.rangerfichier.domain.services;

public class ReportAccumulator {
    private final StringBuilder report = new StringBuilder();

    public ReportAccumulator append(String text) {
        report.append(text);
        return this;
    }

    public void addSummary(int moveCounter) {
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
