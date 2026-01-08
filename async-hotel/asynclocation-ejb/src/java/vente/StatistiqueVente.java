package vente;

import java.sql.Date;

public class StatistiqueVente extends VenteLib{

    private Date debut;
    private String mois;
    private double ca;
    private int annee;
    private int rang;

    private int moisInt;

    private int qteJour;
    private String idProduitLib;
    private int top;

    public StatistiqueVente(){
        this.setNomTable("CA_PAR_MOIS");
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public double getCa() {
        return ca;
    }

    public void setCa(double ca) {
        this.ca = ca;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public int getMoisInt() {
        return moisInt;
    }
    public void setMoisInt(int moisInt) {
        this.moisInt = moisInt;
    }

    public int getQteJour() {
        return qteJour;
    }

    public void setQteJour(int qteJour) {
        this.qteJour = qteJour;
    }

    public String getIdProduitLib() {
        return idProduitLib;
    }
    public void setIdProduitLib(String idProduitLib) {
        this.idProduitLib = idProduitLib;
    }
    public int getTop() {
        return top;
    }
    public void setTop(int top) {
        this.top = top;
    }
}
