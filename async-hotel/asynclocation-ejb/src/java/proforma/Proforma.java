package proforma;

import bean.CGenUtil;
import bean.ClassMAPTable;
import bean.ClassMere;
import caution.Caution;
import caution.ReservationVerifDetails;
import produits.Historique;
import produits.Ingredients;
import reservation.Reservation;
import reservation.ReservationDetails;
import user.UserEJB;
import utilitaire.UtilDB;
import utilitaire.Utilitaire;
import utils.CleReference;
import utils.ConstanteLocation;
import vente.BonDeCommande;
import historique.MapUtilisateur;
import utils.Notification;
import vente.Vente;
import vente.VenteDetails;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Proforma extends ClassMere{
    private String id,designation,idMagasin,remarque,idOrigine,idClient,idReservation,echeance,reglement;
    private Date daty,datyPrevu, dateprevres;
    private int etat,estPrevu;
    private double tva,remise,caution;
    private String lieulocation;
    private Reservation reservationPF;
    private String numproforma;
    private String idmagasinlib;

    public String getNumproforma() {
        return numproforma;
    }
    public void setNumproforma(String numproforma) {
        this.numproforma = numproforma;
    }
    public double getCaution() {
        return caution;
    }
    public void setCaution(double caution) {
        this.caution = caution;
    }

    public String getLieulocation() {
        return lieulocation;
    }

    public void setLieulocation(String lieulocation) {
        this.lieulocation = lieulocation;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public Proforma getProforma(Connection c) throws Exception{
        Proforma proforma = (Proforma) this.getById(this.getId(), "PROFORMA", c);
        return proforma;
    }

    public Date getDateprevres() {
        return dateprevres;
    }

    public void setDateprevres(Date dateprevres) {
        this.dateprevres = dateprevres;
    }

    public Proforma()throws Exception{
        this.setNomTable("PROFORMA");
        this.setLiaisonFille("idProforma");
        setNomClasseFille("proforma.ProformaDetails");
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
        this.preparePk("PROF", "getSeqProforma");
        this.setId(makePK(c));
    }

    @Override
    public String getNomClasseFille(){
        return "proforma.ProformaDetails";
    }

    @Override
    public String getLiaisonFille(){
        return "idProforma";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getIdMagasin() {
        return idMagasin;
    }

    public void setIdMagasin(String idMagasin) {
        this.idMagasin = idMagasin;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public String getIdOrigine() {
        return idOrigine;
    }

    public void setIdOrigine(String idOrigine) {
        this.idOrigine = idOrigine;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) throws Exception {
        if(this.getMode().compareToIgnoreCase("modif")==0){
            if(idClient==null || idClient.compareToIgnoreCase("")==0){
                throw new Exception("Client obligatoire");
            }
        }
        this.idClient = idClient;
    }

    public String getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(String idReservation) {
        this.idReservation = idReservation;
    }

    public String getEcheance() {
        return echeance;
    }

    public void setEcheance(String echeance) {
        this.echeance = echeance;
    }

    public String getReglement() {
        return reglement;
    }

    public void setReglement(String reglement) {
        this.reglement = reglement;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

    public Date getDatyPrevu() {
        return datyPrevu;
    }

    public void setDatyPrevu(Date datyPrevu) {
        this.datyPrevu = datyPrevu;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public int getEstPrevu() {
        return estPrevu;
    }

    public void setEstPrevu(int estPrevu) {
        this.estPrevu = estPrevu;
    }

    public String getIdmagasinlib() {
        return idmagasinlib;
    }

    public void setIdmagasinlib(String idmagasinlib) {
        this.idmagasinlib = idmagasinlib;
    }

    public double getTva() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva = tva;
    }
    public ProformaDetails[] getFilleProforma()throws Exception{
        ProformaDetails profD = new ProformaDetails();
        profD.setIdProforma(this.getId());
        ProformaDetails[] val= (ProformaDetails[])CGenUtil.rechercher(profD, null, null, "");
        return val;
    }

    public BonDeCommande createBonDeCommande()throws Exception{
        try {
            Proforma proforma = new Proforma();
            proforma.setId(this.getId());
            Proforma[] resultats = (Proforma[]) CGenUtil.rechercher(proforma, null, null, "");
            if(resultats.length > 0) {
                proforma = resultats[0];
                BonDeCommande bd = new BonDeCommande();
                if (proforma.getIdClient() != null) {
                    bd.setIdClient(proforma.getIdClient());
                }
                if (proforma.getIdMagasin() != null) {
                    bd.setIdMagasin(proforma.getIdMagasin());
                }
                bd.setIdProforma(proforma.getId());
                return bd;
            }else {
                throw new Exception("La proforma n'existe pas");
            }
        }catch(Exception e){
            throw e;
        }
    }

    public Reservation createReservation()throws Exception{
        boolean canClose=false;
        Connection c = null;
        try{
            if(c==null){
                c=new UtilDB().GetConn();
                canClose=true;
            }
            Proforma proforma = new Proforma();
            proforma.setId(this.getId());
            Proforma[] resultats = (Proforma[]) CGenUtil.rechercher(proforma, null, null, c,"");
            if(resultats.length > 0) {
                proforma = resultats[0];
                ProformaDetails temp = new ProformaDetails();
                temp.setIdProforma(proforma.getId());
                ProformaDetails[] filles = (ProformaDetails[]) CGenUtil.rechercher(temp, null, null, c,"");
                Reservation bd = new Reservation();
                if (proforma.getIdClient() != null) {
                    bd.setIdclient(proforma.getIdClient());
                }
                bd.setRemarque("Reservation du proforma "+proforma.getId());
                bd.setMargemoins(1);
                bd.setMargeplus(1);
                ReservationDetails [] fils = new ReservationDetails[filles.length];
                for (int i = 0; i < filles.length; i++) {
                    fils[i] = filles[i].createReservationDetail();
                    fils[i].setMargemoins(1);
                    fils[i].setMargeplus(1);
                    fils[i].setHeure(Utilitaire.heureCouranteHM());
                }
                bd.setFille(fils);
                return bd;
            }else {
                throw new Exception("La proforma "+this.getId()+" n'existe pas");
            }
        } catch(Exception e){
            throw e;
        } finally {
            if(canClose){
                c.close();
            }
        }
    }

    public Reservation genererReservation(String u, Connection c)throws Exception{
        boolean canClose=false;
        try{
            if(c==null){
                c=new UtilDB().GetConn();
                canClose=true;
            }
            Proforma proforma = new Proforma();
            proforma.setId(this.getId());
            Proforma[] resultats = (Proforma[]) CGenUtil.rechercher(proforma, null, null, c,"");
            if(resultats.length > 0) {
                proforma = resultats[0];
                ProformaDetails temp = new ProformaDetails();
                temp.setIdProforma(proforma.getId());
                ProformaDetails[] filles = (ProformaDetails[]) CGenUtil.rechercher(temp, null, null, c,"");
                Reservation bd = new Reservation();
                bd.setIdorigine(proforma.getId());
                if (proforma.getIdClient() != null) {
                    bd.setIdclient(proforma.getIdClient());
                }
                bd.setRemarque("Reservation du proforma "+proforma.getId());
                bd.setDaty(proforma.getDateprevres());
                bd.setMargemoins(1);
                bd.setMargeplus(1);
                bd.setTva(proforma.getTva());
                bd.setIdMagasin(proforma.getIdMagasin());
                bd.setRemise(proforma.getRemise());
                bd.setLieulocation(proforma.getLieulocation());
                bd.setCaution(proforma.getCaution());
                ReservationDetails [] fils = new ReservationDetails[filles.length];
                for (int i = 0; i < filles.length; i++) {
                    fils[i] = filles[i].createReservationDetail();
                    fils[i].setMargemoins(filles[i].getMargemoins());
                    fils[i].setMargeplus(filles[i].getMargeplus());
                    fils[i].setTva(filles[i].getTva());
                    fils[i].setRemise(filles[i].getRemise());
                    fils[i].setHeure(Utilitaire.heureCouranteHM());
                    fils[i].setQtearticle(filles[i].getNombre());
                    fils[i].setQte(filles[i].getQte());
                }
                bd.setFille(fils);
                Reservation res =(Reservation) bd.createObject(u,c);
                if(proforma.getCaution()>0) {
                    Caution cau = new Caution();
                    cau.setIdreservation(res.getId());
                    cau = cau.genererCaution(c, this.getCaution(), this.getRemise());
                    cau.createObject(u,c);
                    cau.validerObject(u, c);
                }
                return res;
            }else {
                throw new Exception("La proforma "+this.getId()+" n'existe pas");
            }
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        } finally {
            if(canClose){
                c.close();
            }
        }
    }

    public ProformaDetails getCautionDetail(ProformaDetails prof)throws Exception{
        ProformaDetails res = null;
        if(this.getCaution()>0 && prof!=null){
            res = new ProformaDetails();
            //res.setPu((caution*ConstanteLocation.caution)/100);
            res.setIdDevise(prof.getIdDevise());
            res.setCompte(prof.getCompte());
            res.setUnite(prof.getUnite());
            res.setDesignation("Caution du proforma ");
            res.setDatedebut(prof.getDatedebut());
            res.setQte(1);
            res.setNombre(1);
            res.setRemise(0);
            res.setIdProforma(this.getId());
            res.setIdProduit(ConstanteLocation.id_produit_caution);
        }
        return res;
    }

    @Override
    public ClassMAPTable createObject(String u, Connection c) throws Exception {
        try {
            ProformaDetails[] fillestemp = (ProformaDetails[]) this.getFille();
            ProformaDetails[] fillesnew = null;
            ProformaDetails det = this.getCautionDetail(fillestemp[0]);
            if(det!=null && fillestemp.length>0) {
                fillesnew= new ProformaDetails[fillestemp.length+1];
                fillesnew[fillestemp.length] = det;
            }else{
                fillesnew= new ProformaDetails[fillestemp.length];
            }
            //double caution = 0.0;
            for (int i = 0; i < fillestemp.length; i++) {
                fillesnew[i] = fillestemp[i];
                fillesnew[i].setTva(this.getTva());
                Ingredients temp = fillestemp[i].getProduit(c);
                if(temp!=null && temp.getCategorieIngredient().compareToIgnoreCase(ConstanteLocation.type_produit_service)!=0) {
                    Ingredients ing = new Ingredients();
                    ing.setId(fillestemp[i].getIdProduit());
                    Ingredients [] all = ((Ingredients[]) CGenUtil.rechercherPrecis(ing,null,null,c," AND ID='"+fillestemp[i].getIdProduit()+"'"));
                    if(all==null || all.length<=0) {
                        throw new Exception("Produit non valide");
                    }
                    ing = all[0];
                    Date [] ds = new Date[fillestemp[i].getQte()+1];
                    String message ="Liste des articles disponibles : ";
                    for (int j = 0; j < ds.length; j++) {
                        ds[j] = Utilitaire.ajoutJourDate(fillestemp[i].getDatedebut(),j);
                        double dispo = ing.checkDisponibilite(c,ds[j]);
                        if(fillestemp[i].getNombre()>dispo){
                            message += dispo + " "+temp.getLibelle()+" date "+ds[j].toString()+" //";
                        }
                    }
                    if(message.compareToIgnoreCase("Liste des articles disponibles : ")!=0){
                        throw new Exception(message);
                    }
                    fillesnew[i].setRemise(this.getRemise());
                }else{
                    fillesnew[i].setRemise(0);
                    fillesnew[i].setTva(0);
                }
            }
            if(det!=null){
                fillesnew[fillestemp.length].setPu(calculCaution(c,fillestemp,this.getRemise()));
            }

            this.setFille(fillesnew);
            CleReference cle = new CleReference();
            String keyForSequence = this.getDaty()
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("yyyyMM"));

            String dateForDisplay = this.getDaty()
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

//            String daty = this.getDaty().toString().replace("-", "");
            cle.setId(keyForSequence);
            cle.setNomTab("PROFORMA");
            cle.createObject(u,c);
            this.setNumproforma(dateForDisplay +"-"+cle.getNextVal());
            return super.createObject(u, c);
        } catch (Exception e) {
            throw e;
        }
    }

    public double calculCaution(Connection c,ProformaDetails [] proformaDetails, double remise) throws Exception {
        double caution = 0.0;
        for (int i = 0; i < proformaDetails.length; i++) {
            Ingredients temp = proformaDetails[i].getProduit(c);
            if(temp!=null && temp.getCategorieIngredient().compareToIgnoreCase(ConstanteLocation.type_produit_service)!=0) {
                Ingredients ing = new Ingredients();
                ing.setId(proformaDetails[i].getIdProduit());
                double dim = 1;
                if(temp.getDurre()>0) {
                    dim = temp.getDurre();
                }
                double montantsansremise = (proformaDetails[i].getNombre()*proformaDetails[i].getPu()*proformaDetails[i].getQte()*dim);
                double montantavecremise = montantsansremise - (montantsansremise*(remise/100));
                double montantttc = montantavecremise;
                caution += montantttc;
            }
        }
        System.err.println(caution+"caution");
        return (caution*this.getCaution())/100;
    }

    public Object validerObject(String u) throws Exception {
        Connection c = null;
        try{
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            Proforma proforma = (Proforma) new Proforma().getById(this.getId(),this.getNomTable(),c);

            ProformaDetails[] fillestemp = (ProformaDetails[]) proforma.getFilleProforma();
            for (int i = 0; i < fillestemp.length; i++) {
                Ingredients temp = fillestemp[i].getProduit(c);
                if(temp!=null && temp.getCategorieIngredient().compareToIgnoreCase(ConstanteLocation.type_produit_service)!=0) {
                    Ingredients ing = new Ingredients();
                    String awhere = " AND ID='"+fillestemp[i].getIdProduit()+"'";
                    Ingredients [] all = ((Ingredients[]) CGenUtil.rechercher(ing,null,null,c,awhere));
                    if(all==null || all.length<=0) {
                        throw new Exception("Produit non valide");
                    }
                    ing = all[0];
                    Date [] ds = new Date[fillestemp[i].getQte()+1];
                    String message ="Liste des articles disponibles : ";
                    for (int j = 0; j < ds.length; j++) {
                        ds[j] = Utilitaire.ajoutJourDate(fillestemp[i].getDatedebut(),j);
                        double dispo = ing.checkDisponibilite(c,ds[j]);
                        if(fillestemp[i].getNombre()>dispo){
                            message += dispo + " "+temp.getLibelle()+" date "+ds[j].toString()+" //";
                        }
                    }
                    if(message.compareToIgnoreCase("Liste des articles disponibles : ")!=0){
                        throw new Exception(message);
                    }
                }
            }

            proforma.validerObject(u,c);
            return proforma;
        }
        catch(Exception e){
            if(c!=null) c.rollback();
            throw e;
        }
        finally{
            if(c!=null) c.close();
        }

    }


    @Override
    public Object validerObject(String u, Connection c) throws Exception {
        Reservation resa = this.genererReservation(u,c);
        resa.validerObject(u,c);
        this.setReservationPF(resa);


        Object r= null;
        Notification notif = new Notification();
        MapUtilisateur[] u2 = (MapUtilisateur[]) (CGenUtil.rechercher(new MapUtilisateur(), null, null, c, " and idrole in('dg','vente')"));
        int[] receiver = new int[u2.length];
        for (int i = 0; i < u2.length; i++) {
            receiver[i] = u2[i].getRefuser();
        }
        r = super.validerObject(u, c);
        //if(r!=null) notif.creerNotificationMultiple(receiver,"Validation du proforma "+this.getId(),this.getId(),"vente/proforma/proforma-fiche.jsp",c);
        return r;
    }


    public Reservation getReservation() throws Exception {
        Reservation res = new Reservation();
        res.setIdorigine(this.getId());
        Reservation[] val= (Reservation[])CGenUtil.rechercher(res, null, null, "");
        if(val.length>0) {
            return val[0];
        }
        throw new Exception("Aucun reservation");
    }
    public Reservation getReservationPF(){
        return reservationPF;
    }
     public void setReservationPF(Reservation reservationPF){
        this.reservationPF=reservationPF;
    }

    public ProformaDetails[] getProformaDetail(Connection c) throws Exception{
        ProformaDetails[] profs = null;
        try{
            String awhere = " and IDPROFORMA = '"+this.getId()+"'";
            ProformaDetails pd = new ProformaDetails();
            profs = (ProformaDetails[]) CGenUtil.rechercher(pd, null, null, c, awhere);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        return profs;
    }

    public void deleteLigneCaution(Connection c,ProformaDetails [] olds ) throws Exception{
        for(int i=0;i<olds.length;i++) {
            Ingredients ing = new Ingredients();
            ing.setId(olds[i].getIdProduit());
            ing = ((Ingredients[]) CGenUtil.rechercherPrecis(ing,null,null,c," AND ID='"+olds[i].getIdProduit()+"'"))[0];
            //if(ing.getCategorieIngredient().compareToIgnoreCase(ConstanteLocation.type_produit_service)==0) {
                olds[i].deleteToTable(c);
            //}
        }
    }

    public String modifier(UserEJB u, Connection c, ProformaDetails[] filles) throws Exception{
        boolean estOuvert = false;
        try {
            if (c == null) {
                estOuvert = true;
                c = new UtilDB().GetConn();
                c.setAutoCommit(false);
            }
            ProformaDetails [] olds = getProformaDetail(c);
            deleteLigneCaution(c,olds);
            double cau  = calculCaution(c,filles,this.getRemise());
            ProformaDetails det = this.getCautionDetail(filles[0]);
            ProformaDetails [] new2 = null;

            if(det!=null && filles.length>0) {
                 new2 = new ProformaDetails[filles.length+1];
                 new2[filles.length] = det;
            }else if(det==null && filles.length>0){
                new2 = new ProformaDetails[filles.length];
            }else{
                throw new Exception("Produit non valide");
            }
            int test = 0;
            for (int i = 0; i < filles.length; i++) {
                Ingredients [] ing = (Ingredients []) CGenUtil.rechercherPrecis(new Ingredients(),null,null,c," AND ID='"+filles[i].getIdProduit()+"'");
                if(ing.length>0 && ing[0].getCategorieIngredient().compareToIgnoreCase(ConstanteLocation.type_produit_service)==0) {
                    if (ing[0].getId().compareToIgnoreCase(ConstanteLocation.id_produit_caution)!=0){
                        test++;
                        new2[i] = filles[i];
                        new2[i].setRemise(0);
                        new2[i].setIdProforma(this.getId());
                        new2[i].createObject(u.getUser().getTuppleID(), c);
                    }
                    continue;
                }
                if(ing.length<0) {
                    throw new Exception("Produit non valide");
                }
                test++;
                new2[i] = filles[i];
                new2[i].setRemise(this.getRemise());
                new2[i].setIdProforma(this.getId());
                new2[i].createObject(u.getUser().getTuppleID(), c);
            }
            if(test==0) {
                throw new Exception("Produit non valide");
            }
            if(det!=null) {
                new2[filles.length].setPu(cau);
                new2[filles.length].setRemise(0);
                det.setPu(cau);
                det.createObject(u.getUser().getTuppleID(), c);
            }
            super.updateToTableWithHisto(u.getUser().getTuppleID(), c);

            c.commit();
            return this.getId();
        } catch (Exception e) {
            c.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }
    }
}
