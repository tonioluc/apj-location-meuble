/*
 * To change this license header, choose License Headers in Project Prhistoerties.
 * To change this template file, choose Tools | Templates
 * and histoen the template in the editor.
 */
package produits;

import bean.AdminGen;
import bean.CGenUtil;
import bean.ClassMAPTable;
import historique.MapHistorique;
import inventaire.Inventaire;
import inventaire.InventaireFille;
import magasin.Magasin;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLOutput;

import mg.cnaps.compta.ComptaCompte;
import prevision.PrevisionComplet;
import proforma.ProformaDetails;
import reservation.ReservationDetails;
import stock.MvtStock;
import stock.MvtStockFille;
import utilitaire.ConstanteEtat;
import utilitaire.UtilDB;
import utilitaire.Utilitaire;
import utils.ConstanteStation;

/**
 *
 * @author Joe
 */
public class Ingredients extends ClassMAPTable {
    private String id;
    private String libelle;
    private double seuil;
    private String unite;
    private double quantiteParPack;
    private double pu; // Prix unitaire
    private int actif;
    private String photo;
    private double calorie;
    private int durre;
    private int compose;
    private String categorieIngredient;
    private String idFournisseur;
    private Date daty;
    private double qteLimite;
    private double pv; // Prix de vente
    private String libelleVente, idVoiture;
    String bienOuServ;
    double revient;
    String compte_vente;
    String compte_achat;
    String etatlib;
    double ancienPu;
    double ancienPV;
    double tva;
    String filepath;
    private String idModele;
    private double reste;
    String typeStock, image, reference, classification;
    String idmagasin;
    double quantite;

    public String getIdmagasin() {
        return idmagasin;
    }

    public void setIdmagasin(String idmagasin) {
        this.idmagasin = idmagasin;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public String getTypeStock() {
        return typeStock;
    }

    public void setTypeStock(String typeStock) {
        this.typeStock = typeStock;
    }

    public double getReste() {
        return reste;
    }

    public void setReste(double reste) throws Exception {
        if (this.getMode().equals("modif") && reste < 0) {
            throw new Exception("Reste ne peut pas etre negatif");
        }
        this.reste = reste;
    }

    public double checkDisponibilite(Connection c,Date date) throws Exception {
        double temp= reserver(c,date);
        return this.getQuantiteParPack()-temp;
    }

    public double reserver(Connection c,Date date) throws Exception {
        ReservationDetails temp = new ReservationDetails();
        temp.setNomTable("RESERVATIONPLANNING_SOM");
        String awhere = " AND IDPRODUIT='"+this.getId()+"' AND DATY=TO_DATE('"+date+"','YYYY-MM-DD')";
        ReservationDetails[] listePla = (ReservationDetails[]) CGenUtil.rechercher(temp, null, null, c, awhere);
        if(listePla.length>0){
            return listePla[0].getQte();
        }
        return 0;
    }

    public Date estDispo(Date [] daty, int qte,String nomTable, Connection conn) throws Exception {
        try {
            CheckDispo checkDispo = new CheckDispo();
            checkDispo.setNomTable("checkdisponibilite");
            String [] datys = new String[daty.length];
            for (int i = 0; i < daty.length; i++) {
                datys[i] = "to_date('"+daty[i].toString()+"', 'YYYY-MM-DD')";
            }
            if(nomTable != null) {
                checkDispo.setNomTable(nomTable);
            }
            String apresWhere = " and idproduit='"+this.getId()+"' and daty in ("+Utilitaire.tabToString(datys, "", ",")+" ) ORDER BY DATY ASC ";
            //String apresWhere = " and idproduit='"+this.getId()+"' ORDER BY DATY ASC ";
            System.err.println(apresWhere);
            CheckDispo[] bls = (CheckDispo[]) CGenUtil.rechercher(checkDispo, null, null,conn, apresWhere);
            for (int i = 0; i < bls.length; i++) {
                System.err.println(bls[i].getQtedispo());
                if (bls[i].getQtedispo()<qte) {
                    return bls[i].getDaty();
                }
            }
            return null;
        } catch (Exception e) {
            throw e;
        }

    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) throws Exception {
        System.err.println("================================"+reference);
        System.err.println("===================this.getReference()============="+this.getReference()+"=============="+this.getId());
        Ingredients ing = new Ingredients();
        if (this.getMode().equals("modif") && this.getId()==null) {
            System.err.println("=================MIDITRAAAAAAAAAA==============="+reference);
            CategorieIngredientPrefix c = new CategorieIngredientPrefix();
            c.setId(this.getCategorieIngredient());
            CategorieIngredientPrefix[] categories = (CategorieIngredientPrefix[]) CGenUtil.rechercher(c, null, null,null, "");
            if(categories != null && categories.length > 0) {
                this.reference = categories[0].getPrefix() + categories[0].getNextnumber();
                return;
            }
        }
        System.err.println("=============TSY MIDITRA AAAAAA==================="+reference);
        this.reference = reference;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIdModele() {
        return idModele;
    }

    public void setIdModele(String idModele) {
        this.idModele = idModele;
    }

    public double getTva() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva = tva;
    }

    public double getAncienPu() {
        return ancienPu;
    }

    public void setAncienPu(double ancienPu) {
        this.ancienPu = ancienPu;
    }

    public double getAncienPV() {
        return ancienPV;
    }

    public void setAncienPV(double ancienPV) {
        this.ancienPV = ancienPV;
    }

    public String getEtatlib() {
        return etatlib;
    }

    public void setEtatlib(String etatlib) {
        this.etatlib = etatlib;
    }

    public String getCompte_vente() {
        return compte_vente;
    }

    public void setCompte_vente(String compte_vente) {
        this.compte_vente = compte_vente;
    }

    public String getCompte_achat() {
        return compte_achat;
    }

    public void setCompte_achat(String compte_achat) {
        this.compte_achat = compte_achat;
    }

    public double getRevient() {
        if(this.revient == 0)return this.getPu();
        return revient;
    }

    public void setRevient(double revient) {
        this.revient = revient;
    }

    public String getBienOuServ() {
        return bienOuServ;
    }

    public void setBienOuServ(String bienOuServ) {
        this.bienOuServ = bienOuServ;
    }

    public Ingredients(String id) {
        this.setNomTable("as_ingredients");
        this.setId(id);
    }

//    @Override
//    public void controler(Connection c) throws Exception {
//        Ingredients ing = new Ingredients();
//        ing.setUnite(this.getUnite());
//        ing.setIdVoiture(this.getIdVoiture());
//        Ingredients[] liste = (Ingredients[]) CGenUtil.rechercher(ing, null, null, c, "");
//        if(liste != null && liste.length > 0) {throw new Exception("Un Tarif avec la meme unite et Voiture existe deja! Il suffit de le modifier");}
//        super.controler(c);
//    }

    public Ingredients() {
        this.setNomTable("AS_INGREDIENTS");
    }


    public void construirePK(Connection c) throws Exception {
        this.preparePk("CHB", "getSeqIngredients");
        this.setId(makePK(c));
    }

    public String getTuppleID() {
        return id;
    }

    public String getAttributIDName() {
        return "id";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getSeuil() {
        return seuil;
    }

    public void setSeuil(double seuil) {
        this.seuil = seuil;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public double getQuantiteParPack() {
        return quantiteParPack;
    }

    public void setQuantiteParPack(double quantiteParPack) {
        this.quantiteParPack = quantiteParPack;
    }

    public double getPu() {
        return pu;
    }

    public void setPu(double pu) throws Exception {
        if(this.getMode().compareToIgnoreCase("modif")==0&&pu==0) throw new Exception("PU non valide");
        if(this.getMode().compareToIgnoreCase("modif")==0&&(this.getPu()>0||this.getTuppleID()!=null||this.getTuppleID().compareToIgnoreCase("")!=0))this.setAncienPu(this.getPu());
        this.pu = pu;
    }

    public int getActif() {
        return actif;
    }

    public void setActif(int actif) {
        this.actif = actif;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public int getDurre() {
        return durre;
    }

    public void setDurre(int durre) {
        this.durre = durre;
    }

    public int getCompose() {
        return compose;
    }

    public void setCompose(int compose) {
        this.compose = compose;
    }

    public String getCategorieIngredient() {
        return categorieIngredient;
    }

    public void setCategorieIngredient(String categorieIngredient) {
        this.categorieIngredient = categorieIngredient;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

    public double getQteLimite() {
        return qteLimite;
    }

    public void setQteLimite(double qteLimite) {
        this.qteLimite = qteLimite;
    }

    public double getPv() {
        return pv;
    }

    public void setPv(double pv) {
        if(this.getMode().compareToIgnoreCase("modif")==0&&(this.getPv()>0||this.getTuppleID()!=null||this.getTuppleID().compareToIgnoreCase("")!=0))this.setAncienPV(this.getPv());
        this.pv = pv;
    }

    public String getIdVoiture() {
        return idVoiture;
    }

    public void setIdVoiture(String idVoiture) {
        this.idVoiture = idVoiture;
    }

    public String getLibelleVente() {
        return libelleVente;
    }

    public void setLibelleVente(String libelleVente) {
        this.libelleVente = libelleVente;
    }
    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
    @Override
    public int updateToTableWithHisto(String refUser, Connection c) throws Exception {
        Ingredients ing=new Ingredients();
        ing=(Ingredients) new Ingredients().getById(this.getId(),null,c);
        System.out.println("Pv AVY ANY ANATY BASE "+ing.getPv()+" pv modifie "+this.getPv());
        if(ing.getPu()!=this.getPu())
        {
            HistoriquePrixIng hp=new HistoriquePrixIng();
            hp.setPu(ing.getPu());
            hp.setIdIngredients(this.getId());
            hp.setDaty(Utilitaire.dateDuJourSql());
            hp.insertToTableWithHisto(refUser,c);
        }
        if(ing.getPv()!=this.getPv())
        {
            HistoriquePrixIng hp=new HistoriquePrixIng();
            hp.setNomTable("HISTORIQUEPVING");
            hp.setPu(ing.getPv());
            hp.setIdIngredients(this.getId());
            hp.setDaty(Utilitaire.dateDuJourSql());
            hp.insertToTableWithHisto(refUser,c);
        }
        return super.updateToTableWithHisto(refUser,c);
    }
    public String[] getMotCles() {
        return new String[]{"id","libelle"};
    }

    /**
     * Fonction utilisée pour decomposer plusieurs ligne au cas ou
     * @param nTRecette
     * @param c
     * @return
     * @throws Exception
     */
    public Recette[] decomposerBase(String nTRecette,Connection c) throws Exception {
        boolean estOuvert = false;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                estOuvert = true;
            }
            /*String req = "select ing.pu as qteav,cast(0 as number(10,2)) as qtetotal ,ing.unite as idproduits, ing.LIBELLE as idingredients,sum(rec.quantite*cast (nvl(to_number(SUBSTR((SUBSTR(SYS_CONNECT_BY_PATH(quantite, '/'),0, (INSTR(SYS_CONNECT_BY_PATH(quantite, '/'), '/',-1)-1))),"
                    +
                    "(INSTR(SUBSTR(SYS_CONNECT_BY_PATH(quantite, '/'),0, (INSTR(SYS_CONNECT_BY_PATH(quantite, '/'), '/',-1)-1)), '/', -1))+1)),1) as number(10,2))) as quantite"
                    +
                    " from "+nTRecette +" rec,AS_INGREDIENTS_LIB ing  where rec.compose=0 and rec.IDINGREDIENTS=ing.id"
                    +
                    "  start with idproduits ='" + this.getId() + "'" +
                    "  connect by prior idingredients = idproduits and prior rec.compose = 1" +
                    "  group by ing.unite, ing.libelle,ing.pu";*/
            String req="SELECT\n" +
                    "    ing.pu AS qteAv,\n" +
                    "    CAST(0 AS NUMBER(10,2)) AS qteTotal,\n" +
                    "    ing.unite AS unite,\n" +
                    "    ing.libelle AS libIngredients,ing.id as idIngredients,\n" +
                    "    cast(SUM(\n" +
                    "        (\n" +
                    "            SELECT\n" +
                    "                EXP(SUM(LN(ROUND(TO_NUMBER(REGEXP_SUBSTR(path, '[^/]+', 1, LEVEL)),2))))\n" +
                    "            FROM\n" +
                    "                dual\n" +
                    "            CONNECT BY\n" +
                    "                REGEXP_SUBSTR(path, '[^/]+', 1, LEVEL) IS NOT NULL\n" +
                    "                AND PRIOR dbms_random.value IS NOT NULL\n" +
                    "        )\n" +
                    "    ) as number(20,2)) AS quantite\n" +
                    "FROM (\n" +
                    "    SELECT\n" +
                    "        rec.*,\n" +
                    "        SYS_CONNECT_BY_PATH(quantite, '/') AS path\n" +
                    "    FROM\n" +
                    "        "+nTRecette +" rec\n" +
                    "    START WITH\n" +
                    "        idproduits = '"+this.getId()+"'\n" +
                    "    CONNECT BY\n" +
                    "        PRIOR idingredients = idproduits\n" +
                    "        AND PRIOR rec.compose = 1\n" +
                    ") rec\n" +
                    "JOIN AS_INGREDIENTS_LIB ing\n" +
                    "    ON rec.idingredients = ing.id\n" +
                    "WHERE rec.compose = 0\n" +
                    "GROUP BY\n" +
                    "    ing.unite,\n" +
                    "    ing.libelle,\n" +
                    "    ing.pu,ing.id";

            Recette rec = new Recette();
            rec.setNomTable("recettemontant");
            return (Recette[]) CGenUtil.rechercher(rec, req, c);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (estOuvert == true && c != null)
                c.close();
        }
    }
    public double calculerRevient(Connection c) throws Exception
    {
        Recette [] liste=this.decomposerBase(c);
        this.setRevient(AdminGen.calculSommeDouble(liste,"revient"));
        return this.getRevient();
    }
    public Recette[] decomposerBase(Connection c) throws Exception {
        return decomposerBase("as_recettecompose",c);
    }
    public RecetteLib[] getRecette(String table, Connection c) throws Exception {
        boolean estOuvert = false;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                estOuvert = true;
            }
            RecetteLib crt = new RecetteLib();
            if (table != null && table.compareToIgnoreCase("") != 0)
                crt.setNomTable(table);
            crt.setIdproduits (this.getId());
            return (RecetteLib[]) CGenUtil.rechercher(crt, null, null, c,
                    "");
        } catch (Exception e) {
            if (c != null) {
                c.rollback();
            }
            throw e;
        } finally {
            if (c != null && estOuvert == true)
                c.close();
        }
    }
    public RecetteLib[] getRecetteIngredient(String table, Connection c) throws Exception {
        boolean estOuvert = false;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                estOuvert = true;
            }
            RecetteLib crt = new RecetteLib();
            if (table != null && table.compareToIgnoreCase("") != 0) {
                crt.setNomTable(table);
            }
            crt.setIdingredients(this.getId());
            return (RecetteLib[]) CGenUtil.rechercher(crt, null, null, c, "");
        } catch (Exception e) {
            throw e;
        } finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }
    }
    /*

    public Ingredients[] decomposer(Connection c) throws Exception {
        Ingredients[] retour = null;
        int verif = 0;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                verif = 1;
            }
            if (this.getCompose() == 0) {
                return retour;
            } else {
                Recette recette = new Recette();
                recette.setIdingredients(this.getId());
                Recette[] listef = (Recette[]) CGenUtil.rechercher(recette, null, null, "");
                String[] tab = new String[listef.length];
                for (int i = 0; i < listef.length; i++) {
                    tab[i] = listef[i].getIdproduits();
                }
                retour = (Ingredients[]) CGenUtil.rechercher(new Ingredients(), " select * from ingredients where id in(" + Utilitaire.tabToString(tab, "'", ",") + ")", c);

                return retour;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (c != null && verif == 1) {
                c.close();
            }
        }
    }

    public RecetteLib[] getRecette(String table, Connection c) throws Exception {
        boolean estOuvert = false;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                estOuvert = true;
            }
            RecetteLib crt = new RecetteLib();
            if (table != null && table.compareToIgnoreCase("") != 0) {
                crt.setNomTable(table);
            }
            return (RecetteLib[]) CGenUtil.rechercher(new RecetteLib(), null, null, c, " and idproduits = '" + this.getId() + "'");
        } catch (Exception e) {
            throw e;
        } finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }
    }

    public RecetteLib[] getRecetteIngredient(String table, Connection c) throws Exception {
        boolean estOuvert = false;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                estOuvert = true;
            }
            RecetteLib crt = new RecetteLib();
            if (table != null && table.compareToIgnoreCase("") != 0) {
                crt.setNomTable(table);
            }
            return (RecetteLib[]) CGenUtil.rechercher(new RecetteLib(), null, null, c, " and IDINGREDIENTS = '" + this.getId() + "'");
        } catch (Exception e) {
            throw e;
        } finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }
    }*/

    public Ingredients getIngredient(Connection c) throws Exception {
        boolean estOuvert = false;
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                estOuvert = true;
            }
            Ingredients[] lsing = (Ingredients[]) CGenUtil.rechercher(new Ingredients(), null, null, c, " and id = '" + this.getId() + "'");
            if (lsing == null || lsing.length == 0) {
                throw new Exception("ingredient introuvable");
            }
            return lsing[0];
        } catch (Exception e) {
            throw e;
        } finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }
    }

    public String genererCompte(Connection c) throws Exception {
        boolean estOuvert = false;
        String valiny = "";
        try {
            if (c == null) {
                c = new UtilDB().GetConn();
                estOuvert = true;
            }
            Ingredients[] liste = (Ingredients[]) CGenUtil.rechercher(new Ingredients(), "select * from as_ingredients where photo=(select max(photo) from as_ingredients where categorieingredient='" + this.getCategorieIngredient() + "')", c);
            if (liste.length > 0) {
                int compte = 0;//Utilitaire.stringToInt(liste[0].getPhoto()) + 1;
                valiny = "" + compte;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (c != null && estOuvert) {
                c.close();
            }
        }
        return valiny;
    }



    public double getLastPu(Connection c)throws Exception{
        double retour=0.0;

        return retour;
    }
    public void produitDisponible(String idProduit, String isDispo, String refUser) throws Exception {
        Connection c = null;
        try {
            c = new UtilDB().GetConn();
            c.setAutoCommit(false); 
            String point=ConstanteStation.point_par_defaut;
            Indisponibilite indisp = new Indisponibilite(idProduit, point);
            Indisponibilite[] indispo = (Indisponibilite[]) CGenUtil.rechercher(indisp, null, null, c, " and idproduit like '" + idProduit + "' and idpoint like '" + point + "'");
            if (isDispo.compareToIgnoreCase("false") == 0) {
                //manao insert anaty indisponibilite
                if (indispo.length == 0) {
                    indisp.insertToTableWithHisto("" + refUser, c);
                }
            } else {
                // delete
                if (indispo.length != 0) {
                    indispo[0].deleteToTableWithHisto("" + refUser, c);
                }

            }
            c.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (c != null) {
                c.rollback();
            }
            throw new Exception(e.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public HistoriquePrixIng[] getHistoriquePu(Connection c, String typepu,String nT) throws Exception {
        boolean estOuvert = false;
        try {
            if (c == null) {
                estOuvert = true;
                c = new UtilDB().GetConn();
            }
            HistoriquePrixIng histo = new HistoriquePrixIng();
            if(nT!=null&& nT.compareToIgnoreCase("")!=0) histo.setNomTable(nT);
            else if (typepu.compareToIgnoreCase("pv")==0) {
                histo.setNomTable("HISTORIQUEPVING");
            }

            histo.setIdIngredients(this.getId());
            HistoriquePrixIng[] histos = (HistoriquePrixIng[]) CGenUtil.rechercher(histo, null, null, c, " ");
            if (histos.length > 0) {
                return histos;
            }
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }

    }
        @Override        
    public ClassMAPTable createObject(String u,Connection c)throws Exception{
        this.setQuantiteParPack(0);
        Ingredients article = (Ingredients) super.createObject(u, c);
        InventaireFille[] listeInventaires = (InventaireFille[]) CGenUtil.rechercher(new InventaireFille(), null, null, c, " and ID = '" + article.getId()+ "'");
        if(listeInventaires==null||listeInventaires.length==0){
            Inventaire inventairesVide = article.creerInventairevide();
            inventairesVide.createObject(u, c);
			inventairesVide.validerObject(u, c);
        }
        MvtStock mvt = article.creerMouvmentStockEntree();
        mvt.insertToTableWithHisto(u, c);
        MvtStockFille mvtfille=article.creerMouvmentStockEntreeFille(mvt);
        mvtfille.insertToTableWithHisto(u, c);
		mvt.validerObject(u, c);
        return article;
    }
    public Inventaire creerInventairevide() throws Exception {
        Inventaire result = new Inventaire();
        result = new Inventaire(Utilitaire.ajoutJourDate(Utilitaire.dateDuJourSql(), -1) , "Inventaire Initiale ",
        this.getIdmagasin(), "", ConstanteEtat.getEtatValider());
            // ajouter fille inventaire
        InventaireFille[] fille =  { new InventaireFille(0, null, this.getId()) };
        result.setFille(fille);
        return result;
    }
    public MvtStock creerMouvmentStockEntree() throws Exception {
        MvtStock result = new MvtStock();
        result.setDesignation("Mouvement entree " + this.getLibelle());
        result.setIdMagasin(this.getIdmagasin());
        result.setIdTypeMvStock(ConstanteStation.TYPEMVTSTOCKENTREE);
        result.setIdPoint(this.getIdmagasin());
        result.setIdobjet(this.getId());
        result.setDaty(Utilitaire.dateDuJourSql());
        result.setEtat(ConstanteEtat.getEtatValider());
        //MvtStockFille[] fille =  { new MvtStockFille( result.getTuppleID(), this.getId(), this.getQuantite(),  this.getPu()) };
        //result.setFille(fille);
        return result;
    }    
    public MvtStockFille creerMouvmentStockEntreeFille(MvtStock mvtStock) throws Exception {
        MvtStockFille fille =  new MvtStockFille(mvtStock.getTuppleID(), this.getId(), this.getQuantiteParPack(),  this.getPu()) ;
        return fille;
    }    

}
