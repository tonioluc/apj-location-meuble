/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vente;

import java.sql.Date;

/**
 *
 * @author Angela
 */
public class VenteLib extends Vente{
    private String idMagasinLib;
    private String etatLib;
    private double montanttotal;
    private String idDevise;
    private String idClientLib;
    private double montantpaye;
    private double montantreste;
    private double montantttc;
    double montantTtcAr;
    protected double avoir;
    private Date datyprevu;
    private String periode, etatlogistiquelib;
    private int etatlogistique;
    private String telephone;
    private double montantRemise;

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Date getDatyprevu() {
        return datyprevu;
    }

    public void setDatyprevu(Date datyprevu) {
        this.datyprevu = datyprevu;
    }

    public double getAvoir() {
        return avoir;
    }

    public void setAvoir(double avoir) {
        this.avoir = avoir;
    }

    public String getDesignation() {
        return designation;
    }

    public String getEtatlogistiquelib() {
        return etatlogistiquelib;
    }

    public void setEtatlogistiquelib(String etatlogistiquelib) {
        this.etatlogistiquelib = etatlogistiquelib;
    }

    public int getEtatlogistique() {
        return etatlogistique;
    }

    public void setEtatlogistique(int etatlogistique) {
        this.etatlogistique = etatlogistique;
    }

    public double getMontantTtcAr() {
        return montantTtcAr;
    }

    public void setMontantTtcAr(double montantTtcAr) {
        this.montantTtcAr = montantTtcAr;
    }

    public double getMontantttc() {
        return montantttc;
    }

    public void setMontantttc(double montantttc) {
        this.montantttc = montantttc;
    }
        

    public void setMontantpaye(double montantpaye) {
        this.montantpaye = montantpaye;
    }

    public double getMontantpaye() {
        return montantpaye;
    }

    public void setMontantreste(double montantreste) {
        this.montantreste = montantreste;
    }

    public double getMontantreste() {
        return montantreste;
    }

    public String getIdClientLib() {
        return idClientLib;
    }

    public void setIdClientLib(String idClientLib) {
        this.idClientLib = idClientLib;
    }

    public String getIdDevise() {
        return idDevise;
    }

    public void setIdDevise(String idDevise) {
        this.idDevise = idDevise;
    }

    public double getMontanttotal() {
        return montanttotal;
    }

    public void setMontanttotal(double montanttotal) {
        this.montanttotal = montanttotal;
    }

    public VenteLib() {
        this.setNomTable("VENTE_CPL");
    }

    public String getIdMagasinLib() {
        return idMagasinLib;
    }

    public void setIdMagasinLib(String idMagasinLib) {
        this.idMagasinLib = idMagasinLib;
    }

    public String getEtatLib() {
        return etatLib;
    }

    public String getChaineEtat(){
        return chaineEtat(this.getEtat());
    }

    public void setEtatLib(String etatLib) {
        this.etatLib = etatLib;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public double getMontantRemise() {
        return montantRemise;
    }

    public void setMontantRemise(double montantRemise) {
        this.montantRemise = montantRemise;
    }
}
