package caution;


import bean.ClassFille;

import java.sql.Connection;
import java.sql.Date;

public class ReservationVerifDetails extends ClassFille {

    private Date dateretour;
    private double etat_materiel,jour_retard,retenue;
    private String id,idreservationverif,idreservationdetails, observation, designation;

    public ReservationVerifDetails() throws Exception {
        this.setNomTable("reservation_verif_details");
        this.setLiaisonMere("idreservationverif");
        this.setNomClasseMere("caution.ReservationVerification");
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getRetenue() {
        return retenue;
    }

    public void setRetenue(double retenue) {
        this.retenue = retenue;
    }

    public Date getDateretour() {
        return dateretour;
    }

    public void setDateretour(Date dateretour) {
        this.dateretour = dateretour;
    }

    public double getEtat_materiel() {
        return etat_materiel;
    }

    public void setEtat_materiel(double etat_materiel) {
        this.etat_materiel = etat_materiel;
    }

    public double getJour_retard() {
        return jour_retard;
    }

    public void setJour_retard(double jour_retard) {
        this.jour_retard = jour_retard;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdreservationverif() {
        return idreservationverif;
    }

    public void setIdreservationverif(String idreservationverif) {
        this.idreservationverif = idreservationverif;
    }

    public String getIdreservationdetails() {
        return idreservationdetails;
    }

    public void setIdreservationdetails(String idreservationdetails) {
        this.idreservationdetails = idreservationdetails;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("RVD", "GETSEQRESERVATIONVERIFDETAILS");
        this.setId(makePK(c));
    }

    @Override
    public void setLiaisonMere(String liaisonMere) {
        super.setLiaisonMere("idreservationverif");
    }

}

