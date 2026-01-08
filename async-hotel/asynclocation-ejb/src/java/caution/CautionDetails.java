package caution;

import bean.*;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CautionDetails extends ClassFille {

    String id, idcaution, idreservationdetails,idingredient,designation;
    double pct_applique,montant,montantreservation;

    public double getMontantreservation() {
        return montantreservation;
    }

    public void setMontantreservation(double montantreservation) {
        this.montantreservation = montantreservation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdcaution() {
        return idcaution;
    }

    public void setIdcaution(String idcaution) {
        this.idcaution = idcaution;
    }

    public String getIdreservationdetails() {
        return idreservationdetails;
    }

    public void setIdreservationdetails(String idreservationdetails) {
        this.idreservationdetails = idreservationdetails;
    }

    public String getIdingredient() {
        return idingredient;
    }

    public void setIdingredient(String idingredient) {
        this.idingredient = idingredient;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getPct_applique() {
        return pct_applique;
    }

    public void setPct_applique(double pct_applique) {
        this.pct_applique = pct_applique;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    @Override
    public boolean isSynchro(){
        return true;
    }

    public CautionDetails(String nomtable){
        super.setNomTable(nomtable);
    }

    public CautionDetails() {
        super.setNomTable("cautiondetails");
        try {
            this.setNomClasseMere("caution.Caution");
            this.setLiaisonMere("idcaution");
        } catch (Exception ex) {
            Logger.getLogger(CautionDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getTuppleID() {
        return this.getId();
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("VTD", "getSeqCautionDetails");
        this.setId(makePK(c));
    }

    @Override
    public void setLiaisonMere(String liaisonMere) {
        super.setLiaisonMere("idcaution");
    }

}