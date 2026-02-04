package model;

public class ReportService {
    StringBuilder rapport = new StringBuilder();

    ReportService append(String text) {
        rapport.append(text);
        return this;
    }

    void addTotalReport(int moveCounter) {
        if (moveCounter == 0) {
            rapport.append("Aucun fichier déplacé.");
        } else {
            rapport.append(moveCounter).append(" fichier(s) déplacé(s).");
        }
    }

    public String getReport() { return rapport.toString();}

}
