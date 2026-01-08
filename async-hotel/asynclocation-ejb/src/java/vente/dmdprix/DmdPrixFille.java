package vente.dmdprix;

import bean.CGenUtil;
import bean.ClassFille;
import produits.Ingredients;

import java.sql.Connection;

public class DmdPrixFille extends ClassFille {

    private String id;

    private String idMere;

    private String produit, desce;

    private String remarque;

    private String idPersonne;

    public DmdPrixFille()throws Exception{
        this.setNomTable("DMDPRIXFILLE");
        setLiaisonMere("idMere");
        setNomClasseMere("vente.dmdprix.DmdPrix");
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("DMDPF", "getseqDmdPrixFille");
        this.setId(makePK(c));
    }

    @Override
    public String getTuppleID() {
        return this.id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
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


    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public String getIdPersonne() {
        return idPersonne;
    }

    public void setIdPersonne(String idPersonne) {
        this.idPersonne = idPersonne;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public String getDesce() {
        return desce;
    }

    public void setDesce(String desce) {
        this.desce = desce;
    }

//    public double sommeDevis()throws Exception{
//        double somme = 0;
//        Devis devis = new Devis();
//        Devis[] devis1 = (Devis[]) CGenUtil.rechercher(devis, null, null, " and idDmdPrixFille = '"+this.getId()+"'");
//        DevisFilleLib devisFilleLib = new DevisFilleLib();
//        if(devis1!=null && devis1.length!=0){
//            for (int i = 0; i < devis1.length; i++) {
//                DevisFilleLib[] devisFilleLibs = (DevisFilleLib[]) CGenUtil.rechercher(devisFilleLib, null, null, " and idMere = '"+devis1[i].getId()+"'");
//                somme += AdminGen.calculSommeDouble(devisFilleLibs, "montant");
//            }
//        }
//        return somme;
//    }

    public Ingredients getProduit(Connection c) throws Exception {
        Ingredients ing = new Ingredients();
        ing.setId(this.getProduit());
        Ingredients[] ingredients = (Ingredients[])CGenUtil.rechercher(ing, (String[])null, (String[])null, c, "");
        return ingredients != null && ingredients.length > 0 ? ingredients[0] : null;
    }
}
