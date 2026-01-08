/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stock;

import bean.ClassFille;
import bean.CGenUtil;
import bean.ClassMAPTable;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import produits.Ingredients;
import utils.ConstanteLocation;
import utils.ConstanteStation;
import vente.As_BondeLivraisonClientFille;
import vente.As_BondeLivraisonClientFille_Cpl;


public class MvtStockFille extends ClassFille{
    private String id, idMvtStock, idProduit, idVenteDetail, idTransfertDetail;
    private double entree, sortie,pu,montant,reste;
    private String mvtSrc;

    public double getReste() {
        return reste;
    }

    public void setReste(double reste) {
        this.reste = reste;
    }

    public String getMvtSrc() {
        return mvtSrc;
    }

    public void setMvtSrc(String mvtSrc) {
        this.mvtSrc = mvtSrc;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getPu() {
        return pu;
    }

    public void setPu(double pu) {
        this.pu = pu;
    }

    @Override
    public boolean isSynchro(){
        return true;
    }
    
    public MvtStockFille() throws Exception{
        setNomTable("MvtStockFille");
        this.setLiaisonMere("idMvtStock");
        this.setNomClasseMere("stock.MvtStock");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdMvtStock() {
        return idMvtStock;
    }

    public void setIdMvtStock(String idMvtStock) {
        this.idMvtStock = idMvtStock;
    }

    public String getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(String idProduit) {
        this.idProduit = idProduit;
    }

    public String getIdVenteDetail() {
        return idVenteDetail;
    }

    public void setIdVenteDetail(String idVenteDetail) {
        this.idVenteDetail = idVenteDetail;
    }

    public String getIdTransfertDetail() {
        return idTransfertDetail;
    }

    public void setIdTransfertDetail(String idTransfertDetail) {
        this.idTransfertDetail = idTransfertDetail;
    }

    public double getEntree() {
        return entree;
    }

    public void setEntree(double entree) throws Exception {
        if(getMode().compareTo("modif")==0 && entree<0)throw new Exception("Valeur de l'entree invalide");
        this.entree = entree;
    }

    public double getSortie() {
        return sortie;
    }

    public void setSortie(double sortie) throws Exception {
        if(getMode().compareTo("modif")==0 && sortie<0)throw new Exception("Valeur de la sortie invalide");
        this.sortie = sortie;
    }



    
    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("MVTSFI", "GETSEQMVTSTOCKFILLE");
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
    
    @Override
    public void setLiaisonMere(String liaisonMere) {
        super.setLiaisonMere("idMvtStock");
    }

    @Override
    public void controlerUpdate(Connection c) throws Exception {
        super.setNomClasseMere("stock.MvtStock");
        super.controlerUpdate(c);
    }

    @Override
    public void controler(Connection c) throws Exception{
        this.controlerSaisie();
        Ingredients ing=this.getIngredient(c);
        if (ing.getTypeStock()==null || ing.getTypeStock()=="") {
            throw new Exception("Ce produit ne peut pas etre generer une sortie en stock");
        }
    }
    public As_BondeLivraisonClientFille_Cpl getBondeLivraisonClientFille(Connection c) throws Exception{
        As_BondeLivraisonClientFille_Cpl blf= new As_BondeLivraisonClientFille_Cpl();
        blf.setIdventedetail(this.getIdVenteDetail());
        As_BondeLivraisonClientFille_Cpl[] As_BondeLivraisonClientFille= (As_BondeLivraisonClientFille_Cpl[]) CGenUtil.rechercher(blf,null,null,c, "");
        if(As_BondeLivraisonClientFille.length>0 || blf!=null){
            return As_BondeLivraisonClientFille[0];
        }
        return null;
    }
    public void controllerQteLivraison(Connection c) throws Exception{
        As_BondeLivraisonClientFille_Cpl blf=getBondeLivraisonClientFille(c);
        if(this.sortie>blf.getQteResteALivrer()){
            throw new Exception( blf.getIdproduitlib()+ " : quantité supérieure au reste à livrer");
            }
    }
    @Override
    public ClassMAPTable createObject(String u, Connection c) throws Exception {
        return super.createObject(u, c);
    }
     public MvtStock getMereMvtStock(Connection c) throws Exception {
        if (c == null) {
            throw new Exception("Connection non etablie");
        }
        MvtStock m = new MvtStock();
        m.setId(this.getIdMvtStock());
        MvtStock[] listes = (MvtStock[]) CGenUtil.rechercher(m, null, null, c, "");
        if (listes.length > 0) {
            return listes[0];
        }
        return null;
    }

    public double getQuantite(Connection c, MvtStock mere) throws Exception {
        EtatStock search = new EtatStock();
        search.setId(this.getIdProduit());
        search.setIdMagasin(this.getMereMvtStock(c).getIdMagasin());
        if(mere.getIdTypeMvStock().compareTo(ConstanteStation.TYPEMVTSTOCKINVENTAIRE)==0){
            search.setNomTable("V_ETATSTOCK_ING_ALL");
        }else{
            search.setNomTable("V_ETATSTOCK_ING");
        }
        EtatStock[] retour = (EtatStock[]) CGenUtil.rechercher(search,null,null, c,"");
        if (retour.length == 0) {
            throw new Exception("Mouvement Stock avec id produit : " + this.getIdProduit() + " introuvable");
        }
        return retour[0].getReste();
    }



    public double getQuantite(Connection c) throws Exception {
        EtatStock search = new EtatStock();
        search.setId(this.getIdProduit());
        search.setIdMagasin(this.getMereMvtStock(c).getIdMagasin());
        search.setNomTable("V_ETATSTOCK_ING");

        EtatStock[] retour = (EtatStock[]) CGenUtil.rechercher(search,null,null, c,"");
        if (retour.length == 0) {
            throw new Exception("Mouvement Stock Entree avec id produit : " + this.getIdProduit() + " introuvable");
        }
        System.out.println(retour[0].getReste()+" reste en stock");
        return retour[0].getReste();
    }


    public boolean estSuffisant(Connection c, MvtStock mere) throws Exception {
        if (this.getSortie()>this.getQuantite(c, mere)) {
            return false;
        }
        return true;
     }
     

    public void updateQteIngredient(String u, Connection c)throws Exception{
        Ingredients ing = this.getIngredient(c);
        ing.setReste(ing.getReste()+this.getEntree());
        ing.setReste(ing.getReste()-this.getSortie());
        ing.updateToTableWithHisto(u, c);
    }
    public MvtStockFille[] genererNouveauMvt(MvtStockEntreeAvecReste[] stockReste) throws Exception {
        List<MvtStockFille> retourList = new ArrayList<>();
        double quantite = this.getSortie();
            for (int i = 0; i < stockReste.length && quantite>0; i++) {
                double reste = stockReste[i].getQuantite() - quantite;
                if (reste < 0) {
                    reste = 0;
                }
                double sortie= stockReste[i].getQuantite() - reste;
                MvtStockFille mvtFille = new MvtStockFille();
                mvtFille.setIdMvtStock(this.getIdMvtStock());
                mvtFille.setIdProduit(this.getIdProduit());
                mvtFille.setSortie(sortie);
                mvtFille.setMvtSrc(stockReste[i].getId());
                mvtFille.setPu(stockReste[i].getPu());
                quantite = quantite - mvtFille.getSortie();
                retourList.add(mvtFille);
            }
        return retourList.toArray(new MvtStockFille[0]);
     }
     



    public Ingredients getIngredient(Connection c) throws Exception {
        Ingredients search = new Ingredients();
        search.setId(this.getIdProduit());
        Ingredients[] result = (Ingredients[]) CGenUtil.rechercher(search,null,null,c,"");
        if (result.length == 0) {
            throw new Exception("Ingredients avec id : " + this.getIdProduit() + " introuvable");
        }
        return result[0];
    }
    public void updateQteIngredientBatch(String u, Statement st)throws Exception{
        Ingredients ing = this.getIngredient(st.getConnection());
        ing.setReste(ing.getReste()+this.getEntree());
        ing.setReste(ing.getReste()-this.getSortie());
        ing.updateToTableWithHistoBatch(u, st);
     }
     public void controlerSaisie() throws Exception{
        if(this.getEntree() <= 0 && this.getSortie() <=0){
            throw new Exception("Verifier la quantite sur entree et sortie!");
        }
     }

     public MvtStockFille(String idMvtStock, String idProduit, double entree, double pu) throws Exception{
        setNomTable("MvtStockFille");
        this.setLiaisonMere("idMvtStock");
        this.setNomClasseMere("stock.MvtStock");
        this.idMvtStock = idMvtStock;
        this.idProduit = idProduit;
        this.entree = entree;
        this.pu = pu;
     }
     
     



}
