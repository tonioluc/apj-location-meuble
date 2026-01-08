package proforma;

import bean.CGenUtil;
import bean.ClassFille;
import produits.Ingredients;
import reservation.ReservationDetails;
import vente.BonDeCommandeFille;

import java.sql.Connection;
import java.sql.Date;

public class ProformaDetails extends ClassFille{
    private String id,idProforma,idProduit,idOrigine,idDevise,designation,compte,idDemandePrixFille;
    private int qte,nombre;
    private double pu,remise,tva,puAchat,puVente,tauxDeChange,puRevient, margemoins, margeplus;
    private String unite;
    private Date datedebut;

    public Date getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(Date datedebut) {
        this.datedebut = datedebut;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public ProformaDetails()throws Exception {
        this.setNomTable("PROFORMA_DETAILS");
        this.setLiaisonMere("idProforma");
        this.setNomClasseMere("proforma.Proforma");
    }
    @Override
    public String getTuppleID() {
        return this.id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("PROFD", "getSeqProformaDetails");
        this.setId(makePK(c));
    }

    @Override
    public String getNomClasseMere(){
        return "proforma.Proforma";
    }

    @Override
    public String getLiaisonMere(){
        return "idProforma";
    }

    public double getMargeplus() {
        return margeplus;
    }

    public void setMargeplus(double margeplus) {
        this.margeplus = margeplus;
    }

    public double getMargemoins() {
        return margemoins;
    }

    public void setMargemoins(double margemoins) {
        this.margemoins = margemoins;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public double getPuAchat() {
        return puAchat;
    }

    public void setPuAchat(double puAchat) {
        this.puAchat = puAchat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProforma() {
        return idProforma;
    }

    public void setIdProforma(String idProforma) {
        this.idProforma = idProforma;
    }

    public String getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(String idProduit) {
        this.idProduit = idProduit;
    }

    public String getIdOrigine() {
        return idOrigine;
    }

    public void setIdOrigine(String idOrigine) {
        this.idOrigine = idOrigine;
    }

    public String getIdDevise() {
        return idDevise;
    }

    public void setIdDevise(String idDevise) {
        this.idDevise = idDevise;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCompte() {
        return compte;
    }

    public void setCompte(String compte) {
        this.compte = compte;
    }

    public String getIdDemandePrixFille() {
        return idDemandePrixFille;
    }

    public void setIdDemandePrixFille(String idDemandePrixFille) {
        this.idDemandePrixFille = idDemandePrixFille;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte)throws Exception {
        if(qte<=0){
            throw new Exception("La quantite doit etre superieur a zero!");
        }
        this.qte = qte;
    }

    public double getPu() {
        return pu;
    }

    public void setPu(double pu) {
        this.pu = pu;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public double getTva() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva = tva;
    }

    public double getPuVente() {
        return puVente;
    }

    public void setPuVente(double puVente) {
        this.puVente = puVente;
    }

    public double getTauxDeChange() {
        return tauxDeChange;
    }

    public void setTauxDeChange(double tauxDeChange) {
        this.tauxDeChange = tauxDeChange;
    }

    public double getPuRevient() {
        return puRevient;
    }

    public void setPuRevient(double puRevient) {
        this.puRevient = puRevient;
    }

    public BonDeCommandeFille createBonDeCommandeFille() throws Exception {
        try {
            BonDeCommandeFille ligne = new BonDeCommandeFille();

            ligne.setProduit(this.getIdProduit());
            ligne.setQuantite(this.getQte());
            ligne.setPu(this.getPu());
            ligne.setTva(this.getTva());
            ligne.setIdDevise(this.getIdDevise());
            ligne.setUnite(this.getUnite());
            return ligne;
        }catch(Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }

    public ReservationDetails createReservationDetail() throws Exception {
        try {
            ReservationDetails ligne = new ReservationDetails();
            ligne.setDaty(this.getDatedebut());
            ligne.setIdproduit(this.getIdProduit());
            ligne.setQte(this.getQte());
            ligne.setPu(this.getPu());
            ligne.setQtearticle(1);
            return ligne;
        }catch(Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }

    public Ingredients getProduit(Connection c) throws Exception {
        Ingredients ing = new Ingredients();
        //ing.setId(this.getIdProduit());
        Ingredients [] ingredients = (Ingredients[]) CGenUtil.rechercher(ing,null,null,c," AND ID='"+this.getIdProduit()+"'");
        if(ingredients!=null && ingredients.length>0){
            return ingredients[0];
        }
        return null;
    }
    public Ingredients changerPrixProduit(String u,Connection c) throws Exception {
        Ingredients ing = this.getProduit(c);
        if (ing!=null) {
            ing.setPv(this.getPu());
            ing.updateToTableWithHisto(u,c);
            return ing;
        }
        return null;
    }
}
