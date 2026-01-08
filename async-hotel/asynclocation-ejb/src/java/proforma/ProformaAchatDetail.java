package proforma;

import bean.ClassFille;
import faturefournisseur.DmdAchatFille;
import faturefournisseur.As_BonDeCommande_Fille;

import java.sql.Connection;

public class ProformaAchatDetail extends ClassFille {

    private String id;
    private String idMere;
    private String IdDmdAchatFille;
    private String designation;
    private double qte;
    private double pu;

    public ProformaAchatDetail() throws Exception {
        this.setNomTable("ProformaAchatDetail");
        this.setLiaisonMere("idMere");
        this.setNomClasseMere("proforma.ProformaAchat");
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("PFRMAD", "get_seq_dmd_achat_proforma_fl");
        this.setId(makePK(c));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdMere() {
        return idMere;
    }

    public void setIdMere(String idMere) {
        this.idMere = idMere;
    }

    public String getIdDmdAchatFille() {
        return IdDmdAchatFille;
    }

    public void setIdDmdAchatFille(String idDmdAchatFille) {
        IdDmdAchatFille = idDmdAchatFille;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getQte() {
        return qte;
    }

    public void setQte(double qte) {
        this.qte = qte;
    }

    public double getPu() {
        return pu;
    }

    public void setPu(double pu) {
        this.pu = pu;
    }

    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }
    
    public As_BonDeCommande_Fille genereBcFille() throws Exception {
        try {
            As_BonDeCommande_Fille ligne = new As_BonDeCommande_Fille();
            DmdAchatFille dmdAchatFille = (DmdAchatFille) new DmdAchatFille().getById(this.getIdDmdAchatFille(), "DMDACHATFILLE", null);

            ligne.setProduit(dmdAchatFille.getIdproduit());
            ligne.setQuantite(this.getQte());
            ligne.setPu(this.getPu());

            return ligne;
        }catch(Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }
}
