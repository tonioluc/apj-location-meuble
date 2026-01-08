package reservation;

import bean.CGenUtil;
import bean.ClassEtat;
import bean.ClassMAPTable;
import org.joda.time.DateTime;


import java.sql.Connection;
import java.sql.Date;
import java.util.*;

import produits.Acte;
import produits.Ingredients;
import produits.IngredientsLib;
import stock.MvtStock;
import stock.MvtStockFille;
import stock.MvtStockFilleLib;
import utilitaire.UtilDB;
import utilitaire.Utilitaire;
import utils.ConstanteStation;

public class Check extends bean.ClassEtat {
    private String id;
    /**
     * Mivadika ho idChekIn ity rehefa checkOut
     */
    private String reservation,idClient,idReservationMere;
    private java.sql.Date daty;
    private String heure,checkOut,compteVente;
    private String remarque,idProduit,produitLibelle, client,etatlib;
    private String idResDetail,idmagasin;
    private String responsable,refproduit,idtypelivraison,idtypelivraisonlib, numBl;
    double pu,tva,qte,kilometragecheckin,kilometrage;
    int equivalence;

    public String getNumBl() {
        return numBl;
    }

    public void setNumBl(String numBl) {
        this.numBl = numBl;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getRefproduit() {
        return refproduit;
    }

    public void setRefproduit(String refproduit) {
        this.refproduit = refproduit;
    }

    public String getIdtypelivraison() {
        return idtypelivraison;
    }

    public void setIdtypelivraison(String idtypelivraison) {
        this.idtypelivraison = idtypelivraison;
    }

    public String getIdtypelivraisonlib() {
        return idtypelivraisonlib;
    }

    public void setIdtypelivraisonlib(String idtypelivraisonlib) {
        this.idtypelivraisonlib = idtypelivraisonlib;
    }

    public String getIdmagasin() {
        return idmagasin;
    }

    public void setIdmagasin(String idmagasin) {
        this.idmagasin = idmagasin;
    }

    public int getEquivalence() {
        return equivalence;
    }

    public void setEquivalence(int equivalence) {
        this.equivalence = equivalence;
    }

    public String getIdReservationMere() {
        return idReservationMere;
    }

    public void setIdReservationMere(String idReservationMere) {
        this.idReservationMere = idReservationMere;
    }

    public double getKilometragecheckin() {
        return kilometragecheckin;
    }

    public void setKilometragecheckin(double kilometragecheckin){
        this.kilometragecheckin = kilometragecheckin;
    }

    public String getEtatlib() {
        return this.etatlib;
    }

    public void setEtatlib(String etatlib) {
        this.etatlib = etatlib;
    }
    
    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCompteVente() {
        return compteVente;
    }

    public void setCompteVente(String compteVente) {
        this.compteVente = compteVente;
    }

    public double getTva() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva = tva;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getProduitLibelle() {
        return produitLibelle;
    }

    public void setProduitLibelle(String produitLibelle) {
        this.produitLibelle = produitLibelle;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(String idProduit) {
        this.idProduit = idProduit;
    }

    public double getPu() {
        return pu;
    }

    public void setPu(double pu) throws Exception {
        if(this.getMode().equals("modif")){
            if(pu < 0){
                throw new Exception("Prix unitaire invalide pour une ligne");
            }
        }
        this.pu = pu;
    }

    public Check(String nT) {
        super.setNomTable(nT);
    }
    public Check() {
        super.setNomTable("checkIn");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReservation() {
        return reservation;
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("CIN", "get_seqChekIn");
        if(this.getNomTable().compareTo("checkOut") == 0) this.preparePk("COT", "get_seqCheckOut");
        this.setId(makePK(c));
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    public java.sql.Date getDaty() {
        return daty;
    }

    public void setDaty(java.sql.Date daty) throws Exception {
        if (this.getMode().compareTo("modif") == 0) {
            if (daty == null) {
                daty = utilitaire.Utilitaire.dateDuJourSql();
            }
        }
        this.daty = daty;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }
    public reservation.Reservation[] getReservations(String nT,Connection c)throws Exception {
        return null;
    }
    @Override
    public Object validerObject(String u, Connection c) throws Exception {
        ClassMAPTable o = (ClassMAPTable)super.validerObject(u, c);
        if (this.getClass() != Check.class) {
            return o;
        }
        genererMvtStockCheckin(u, c);
        return o;
    }

    public Reservation createReservation(String u, Connection c) throws Exception{
        Reservation r= new Reservation();
        r.setIdclient(this.getIdClient());
        r.setDaty(this.getDaty());
        r.setRemarque("Reservation sur place");
        produits.Ingredients i = (produits.Ingredients)new produits.Ingredients().getById(this.getIdProduit(),null,c) ;
        if(i==null)throw new Exception("Chambre non existante");
        ReservationDetails [] rd= new ReservationDetails[1];
        rd[0] = new ReservationDetails();
        rd[0].setDaty(this.getDaty());
        // rd[0].setHeure(this.getHeure());
        rd[0].setIdproduit(this.getIdProduit());
        rd[0].setRemarque(this.getProduitLibelle());
        rd[0].setQte(this.getQte());
        rd[0].setPu(i.getPu());
        r.setFille(rd);
        r.createObject(u, c);
        this.setReservation(r.getId());
        return r;
    }

    @Override
    public ClassMAPTable createObject(String u, Connection c) throws Exception {
        boolean estOuvert = false;
        Reservation retour = null;
        try {
            if (c == null) {
                estOuvert = true;
                c = new UtilDB().GetConn();
                c.setAutoCommit(false);
            }
            Check arg = new Check();
            arg.setReservation(this.getReservation());
            //Check [] liste = (Check[]) CGenUtil.rechercher(arg, null, null, c, "");
            //if (liste.length > 0){
                //this.setId(liste[0].getId());
                //this.updateToTableWithHisto(u, c);
                //return liste[0];
//                throw new Exception("Impossible d' ajouter checkin existant");
            //}
            if (this.getReservation().isEmpty() || this.getReservation().equalsIgnoreCase("null") || this.getReservation()==null) {
                retour = createReservation(u, c);
            }
//            int maxSeqBL = Utilitaire.getMaxSeq("GETSEQBONDELIVRAISON", c);
//            this.setNumBl("BL"+maxSeqBL);

            this.construirePK(c);
            Check checkin = (Check) super.createObject(u, c);

            checkin.validerObject(u, c);
            if (estOuvert) {
                c.commit();
            }
            return retour;
        }
        catch (Exception e) {
            c.rollback();
            throw e;
        }
        finally {
            if(estOuvert==true&&c!=null){c.close();}
        }
    }

    public vente.VenteDetails[] genereVenteDetails(String nTableActe,Connection c)throws Exception {
        Acte[] listeActe=getActe(nTableActe,c);
        vente.VenteDetails[] retour=new vente.VenteDetails[listeActe.length];
        for(int i=0;i<listeActe.length;i++)
        {
            retour[i]=listeActe[i].genererVenteDetails();
        }
        return retour;
    }
    public reservation.CheckOut getCheckOut(String nT, Connection c)throws Exception {
        reservation.CheckOut checkOut = new reservation.CheckOut();
        checkOut.setReservation (this.getId());
        if(nT!=null&&nT.compareToIgnoreCase("")!=0)checkOut.setNomTable(nT);
        CheckOut[] retour=(CheckOut[]) CGenUtil.rechercher(checkOut,null,null,c,"");
        if(retour.length==0){return null;}
        return retour[0];
    }
    public Acte[] getActeAvecSimulation(String nTActe,Connection c)throws Exception {
        reservation.CheckOut listeCheckOut=getCheckOut("checkOutVise",c);
        String dateMax=Utilitaire.dateDuJour();
        Acte[] listeGen=null;
        if(listeCheckOut==null) {
            listeGen= CheckOut.genererActe(this.getId(), dateMax,c);
        }
        Acte[] listeInsere=this.getActe(nTActe,c);

        List<Acte> retour=new ArrayList<>();
        if(listeGen!=null) retour.addAll(Arrays.asList(listeGen));
        retour.addAll(Arrays.asList(listeInsere));
        return retour.toArray(new Acte[retour.size()]);

    }
    public produits.Acte[] getActe(String nT, Connection c) throws Exception
    {
        boolean estOuvert = false;
        try {
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert = true;
            }
            Acte crt = new Acte();
            if (nT != null && nT.compareTo("") != 0) crt.setNomTable(nT);
            crt.setIdreservation(this.getId());
            return (Acte[]) CGenUtil.rechercher(crt,null,null,c,"");
        }
        catch(Exception e){
            throw e;
        }
        finally {
            if(estOuvert==true && c!=null) c.close();
        }
    }

    public double getQte() {
        return qte;
    }

    public void setQte(double qte) throws Exception {
        int quantiteALivrer = 0;
        int quantiteDejaLivrer = 0;
        if (this.getMode().equals("modif")){
            ReservationDetails reservationDetails = new ReservationDetails();
            reservationDetails.setId(this.reservation);
            ReservationDetails[] res = (ReservationDetails[]) CGenUtil.rechercher(reservationDetails, null, null,null, "");
            if (res.length > 0) {
                quantiteALivrer = (int) res[0].getQtearticle();
                Check check = new Check();
                check.setReservation(this.getReservation());
                Check[] checks = (Check[]) CGenUtil.rechercher(check,null,null,null, "");
                if (checks.length > 0) {
                    for (int i = 0; i < checks.length; i++) {
                        quantiteDejaLivrer += checks[i].getQte();
                    }
                }
            }
            if (quantiteALivrer < (quantiteDejaLivrer + qte)) {
                Ingredients ingredientsLib = new Ingredients();
                ingredientsLib.setId(this.getIdProduit());
                Ingredients[] ingredientsLibs = (Ingredients[])  CGenUtil.rechercher(ingredientsLib,null,null,null, "");
                throw new Exception("Quantité du produit : " + ingredientsLibs[0].getLibelle() + " deja livrer atteint!");
            }
        }
        this.qte = quantiteDejaLivrer + qte;
    }

    public double getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(double kilometrage) throws Exception {
        this.kilometrage = kilometrage;
    }

    public String getIdResDetail() {
        return idResDetail;
    }

    public void setIdResDetail(String idResDetail) {
        this.idResDetail = idResDetail;
    }

    public void genererMvtStockCheckin(String u,Connection c)throws Exception
    {
        boolean estOuvert=false;
        try
        {
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert=true;
            }
            MvtStock mv=new MvtStock();
            mv.setDaty(this.getDaty());
            mv.setDesignation("Sortie produit "+Utilitaire.champNull(this.getRemarque()));
            mv.setIdTypeMvStock(ConstanteStation.TYPEMVTSTOCKSORTIE);
            mv.setIdobjet(this.getId());
            mv.setIdMagasin(this.getIdmagasin());
            mv.setIdPoint(this.getIdmagasin());
            mv.createObject(u,c);
            Check chk = new Check();
            chk.setNomTable("checkIn");
            chk.setId(this.getId());
            Check[] listeF=(Check[]) CGenUtil.rechercher(chk, null, null, c, "");
            MvtStockFille[] lf = new MvtStockFille[listeF.length];
            for(int i=0;i<listeF.length;i++)
            {
                lf[i]=listeF[i].genererMouvementStockFille(c);
                lf[i].setIdMvtStock(mv.getId());
                lf[i].createObject(u,c);
                lf[i].validerObject(u,c);
            }
            mv.validerObject(u,c);
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            if(c!=null&&estOuvert==true)c.close();
        }
    }


    public MvtStockFille genererMouvementStockFille(Connection c)throws Exception
    {
        MvtStockFille mf=new MvtStockFille();
        mf.setIdProduit(this.getIdProduit());
        mf.setSortie(this.getQte());
        mf.setPu(this.getPu());
        return mf;
    }

    public ReservationDetails getReservationDetail() throws Exception {
        ReservationDetails rd = new ReservationDetails();
        String awhere = " AND id='"+this.getReservation()+"'";
        System.err.println(awhere);
        ReservationDetails[] res = (ReservationDetails[]) CGenUtil.rechercher(rd, null, null, null,awhere );
        return res[0];
    }

    public Check[] regrouper(Check[] fille) throws Exception {
        if (fille == null) return new Check[0];
        Map<String, Check> map = new HashMap<>();
        for (Check c : fille) {
            String key = c.getIdProduit() + "|" + c.getClient();
            if (map.containsKey(key)) {
                Check existant = map.get(key);
                existant.setQte(existant.getQte() + c.getQte());
            } else {
                Check check = (Check) c.dupliquerSansBase();
                map.put(key, check);
            }
        }
        return map.values().toArray(new Check[0]);
    }


}
