package faturefournisseur;

import bean.ClassFille;

import java.sql.Connection;

public class DmdAchatFille extends ClassFille {
    private String id;
    private String idmere;
    private String idproduit;
    private String designation;
    private double quantite;
    private double pu;
    private double tva;
    String idCategorieProduit;


    public DmdAchatFille() throws Exception {
        this.setNomTable("DMDACHATFILLE");
        this.setNomClasseMere("faturefournisseur.DmdAchat");
        this.setLiaisonMere("idmere");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdmere() {
        return idmere;
    }

    public void setIdmere(String idmere) {
        this.idmere = idmere;
    }

    public String getIdproduit() {
        return idproduit;
    }

    public void setIdproduit(String idproduit) {
        this.idproduit = idproduit;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getPu() {
        return pu;
    }

    public void setPu(double pu) {
        this.pu = pu;
    }

    public double getTva() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva = tva;
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("DMDAF", "GETSEQDMDACHATFILLE");
        this.setId(makePK(c));
    }

    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    public proforma.ProformaAchatDetail genererProformaAchatDt() throws Exception {
        proforma.ProformaAchatDetail detail = new proforma.ProformaAchatDetail();

        detail.setDesignation(this.getDesignation());
        detail.setIdDmdAchatFille(this.getId());
        detail.setIdMere(this.getIdmere());
        detail.setQte(this.getQuantite());
//        detail.setPu(produit.getPu());

        return detail;
    }
}
