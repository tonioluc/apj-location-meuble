package proforma;

public class ProformaAchatDetaiLib extends ProformaAchatDetail{
    private String idDmdAchat;

    private String idProduit;

    private String idProduitLib;

    private double montant;

    private String idFournisseur;

    private String idFournisseurLib;

    public ProformaAchatDetaiLib() throws Exception {
        this.setNomTable("PROFORMAACHATDETAILIB");
    }

    public String getIdDmdAchat() {
        return idDmdAchat;
    }

    public void setIdDmdAchat(String idDmdAchat) {
        this.idDmdAchat = idDmdAchat;
    }

    public String getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(String idProduit) {
        this.idProduit = idProduit;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public String getIdProduitLib() {
        return idProduitLib;
    }
    public void setIdProduitLib(String idProduitLib) {
        this.idProduitLib = idProduitLib;
    }
    public String getIdFournisseurLib() {
        return idFournisseurLib;
    }
    public void setIdFournisseurLib(String idFournisseurLib) {
        this.idFournisseurLib = idFournisseurLib;
    }
}
