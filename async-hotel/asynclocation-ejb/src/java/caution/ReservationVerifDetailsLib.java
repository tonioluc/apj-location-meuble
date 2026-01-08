package caution;

import java.sql.Date;

public class ReservationVerifDetailsLib extends ReservationVerifDetails{

    private String libelleproduit,idcaution;
    private double pu,montant,montantretenue;

    public double getMontantretenue() {
        return montantretenue;
    }

    public void setMontantretenue(double montantretenue) {
        this.montantretenue = montantretenue;
    }

    public ReservationVerifDetailsLib() throws Exception {
        this.setNomTable("reservation_verif_details");
    }

    public String getLibelleproduit() {
        return libelleproduit;
    }
    public void setLibelleproduit(String libelleproduit) {
        this.libelleproduit = libelleproduit;
    }

    public double getPu() {
        return pu;
    }

    public void setPu(double pu) {
        this.pu = pu;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getIdcaution() {
        return idcaution;
    }

    public void setIdcaution(String idcaution) {
        this.idcaution = idcaution;
    }
}
