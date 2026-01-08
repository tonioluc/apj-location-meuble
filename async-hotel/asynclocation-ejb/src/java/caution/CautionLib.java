package caution;

public class CautionLib extends Caution{
    String idreservationlib, modepaiement;
    double montantgrp, credit, debit;

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public String getIdreservationlib() {
        return idreservationlib;
    }

    public void setIdreservationlib(String idreservationlib) {
        this.idreservationlib = idreservationlib;
    }

    public String getModepaiement() {
        return modepaiement;
    }

    public void setModepaiement(String modepaiement) {
        this.modepaiement = modepaiement;
    }

    public double getMontantgrp() {
        return montantgrp;
    }

    public void setMontantgrp(double montantgrp) {
        this.montantgrp = montantgrp;
    }

    public CautionLib() throws Exception {
        setNomTable("cautionlib");
    }
}
