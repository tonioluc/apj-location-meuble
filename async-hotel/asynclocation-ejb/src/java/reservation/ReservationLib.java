package reservation;

import caution.ReservationVerifDetailsLib;
import utils.ConstanteLocation;

import java.sql.Date;

public class ReservationLib extends Reservation
{
    String idclientlib;
    String etatlib;

    String datePrevisionRetour, datePrevisionDepart;
    double montant, montanttotal, montantTva,montantTTC,paye,resteAPayer, revient, marge, montantremise;
    String equiperesp;
    String desceequiperesp;
    int etatlogistique;
    String etatlogistiquelib;
    int etatpayment;
    String etatpaymentlib;
    double montantcaution;
    Date datyCaution;
    double debitCaution;
    String periode;
    String modelivraison;

    public String getModelivraisonAffiche() throws Exception {
        return this.isLivraison(null)? "Livraison":"R&eacute;cup&eacute;ration";
    }

    public String getModelivraison() throws Exception {
        return modelivraison;
    }

    public void setModelivraison(String modelivraison) {
        this.modelivraison = modelivraison;
    }

    public int getEtatpayment() {
        return etatpayment;
    }

    public void setEtatpayment(int etatpayment) {
        this.etatpayment = etatpayment;
    }

    public String getEtatpaymentlib() {
        return etatpaymentlib;
    }

    public void setEtatpaymentlib(String etatpaymentlib) {
        this.etatpaymentlib = etatpaymentlib;
    }

    private ReservationVerifDetailsLib[] verification;

    public ReservationVerifDetailsLib[] getVerification() {
        return verification;
    }

    public String getDatePrevisionRetour() {
        return datePrevisionRetour;
    }

    public void setDatePrevisionRetour(String datePrevisionRetour) {
        this.datePrevisionRetour = datePrevisionRetour;
    }

    public void setVerification(ReservationVerifDetailsLib[] verification) {
        this.verification = verification;
    }

    public int getEtatlogistique() {
        return etatlogistique;
    }

    public void setEtatlogistique(int etatlogistique) {
        this.etatlogistique = etatlogistique;
    }

    String idVoiture;

    public String getEquiperesp() {
        return equiperesp;
    }

    public void setEquiperesp(String equiperesp) {
        this.equiperesp = equiperesp;
    }

    public String getDesceequiperesp() {
        return desceequiperesp;
    }

    public void setDesceequiperesp(String desceequiperesp) {
        this.desceequiperesp = desceequiperesp;
    }

    public double getMontantremise() {
        return montantremise;
    }

    public double getMontanttotal() {
        return montanttotal;
    }

    public void setMontanttotal(double montanttotal) {
        this.montanttotal = montanttotal;
    }

    public void setMontantremise(double montantremise) {
        this.montantremise = montantremise;
    }

    public String getIdVoiture() {
        return idVoiture;
    }

    public void setIdVoiture(String idVoiture) {
        this.idVoiture = idVoiture;
    }

    public double getPaye() {
        return paye;
    }

    public void setPaye(double paye) {
        this.paye = paye;
    }

    public double getResteAPayer() {
        return resteAPayer;
    }

    public void setResteAPayer(double resteAPayer) {
        this.resteAPayer = resteAPayer;
    }

    public double getMontantTva() {
        return montantTva;
    }

    public double getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(double montantTTC) {
        this.montantTTC = montantTTC;
    }

    public void setMontantTva(double montantTva) {
        this.montantTva = montantTva;
    }

    public String getIdclientlib() {
        return idclientlib;
    }

    public void setIdclientlib(String idclientlib) {
        this.idclientlib = idclientlib;
    }

    public String getEtatlib() {
        return etatlib;
    }

    public void setEtatlib(String etatlib) {
        this.etatlib = etatlib;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getRevient() {
        return revient;
    }

    public void setRevient(double revient) {
        this.revient = revient;
    }

    public double getMarge() {
        return marge;
    }

    public void setMarge(double marge) {
        this.marge = marge;
    }

    public String getDatePrevisionDepart() {
        return datePrevisionDepart;
    }

    public void setDatePrevisionDepart(String datePrevisionDepart) {
        this.datePrevisionDepart = datePrevisionDepart;
    }

    public double getMontantcaution() {
        return montantcaution;
    }

    public void setMontantcaution(double montantcaution) {
        this.montantcaution = montantcaution;
    }

    public ReservationLib() throws Exception {
        super();
        setNomTable("RESERVATION_LIB");
    }

    public String getEtatlogistiquelib() {
        return etatlogistiquelib;
    }

    public void setEtatlogistiquelib(String etatlogistiquelib) {
        this.etatlogistiquelib = etatlogistiquelib;
    }

    @Override
    public String[] getMotCles() {
        String[] motCles={"id","idclientlib","daty","etatlib"};
        return motCles;
    }

    public Date getDatyCaution() {
        return datyCaution;
    }

    public void setDatyCaution(Date datyCaution) {
        this.datyCaution = datyCaution;
    }

    public double getDebitCaution() {
        return debitCaution;
    }

    public void setDebitCaution(double debitCaution) {
        this.debitCaution = debitCaution;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }
}
