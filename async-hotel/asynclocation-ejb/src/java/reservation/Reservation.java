package reservation;

import bean.CGenUtil;
import bean.ClassFille;
import bean.ClassMAPTable;
import bean.ClassMere;
import caution.Caution;
import caution.CautionDetails;
import caution.CautionLib;
import caution.ReservationVerifDetails;
import magasin.Magasin;
import produits.Acte;
import produits.Ingredients;
import proforma.Proforma;
import proforma.ProformaDetails;
import stock.MvtStock;
import stock.MvtStockFille;
import stock.MvtStockFilleLib;
import user.UserEJB;
import utilitaire.UtilDB;
import utilitaire.Utilitaire;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import caisse.MvtCaisse;
import prevision.Prevision;
import utils.CleReference;
import utils.ConstanteLocation;
import utils.ConstanteStation;
import vente.Vente;
import vente.VenteDetails;
import constante.ConstanteEtat;
import utilitaire.UtilDB;
import bean.CGenUtil;
import javax.validation.constraints.Null;

public class Reservation extends ClassMere
{
    String id;
    String idclient;
    Date daty;
    String remarque;
    double margemoins;
    double margeplus;
    String idorigine;
    double remise,tva,caution;
    String idMagasin;
    String lieulocation;
    String numBl;
    Vente vente;
    String responsable;

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getNumBl() {
        return numBl;
    }

    public void setNumBl(String numBl) {
        this.numBl = numBl;
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

    public String getIdMagasin() {
        return idMagasin;
    }

    public void setIdMagasin(String idMagasin) {
        this.idMagasin = idMagasin;
    }

    public double getTva() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva = tva;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public String getIdorigine() {
        return idorigine;
    }

    public void setIdorigine(String idorigine) {
        this.idorigine = idorigine;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdclient() {
        return idclient;
    }

    @Override
    public String getLiaisonFille() {
        return "idmere";
    }
    @Override
    public  String getNomClasseFille() {
        return "reservation.ReservationDetails";
    }

    public void setIdclient(String idclient) throws Exception {
        if(getMode().compareToIgnoreCase("modif") == 0){
            if(idclient == null || idclient.compareToIgnoreCase("") == 0) throw new Exception("Client vide");
        }
        this.idclient = idclient;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) throws Exception {
        if(getMode().compareToIgnoreCase("modif") == 0){
            if(daty == null){
                this.daty = Utilitaire.dateDuJourSql();
                return;
            }
        }
        this.daty = daty;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }
    
    
    public double getMargemoins() {
        return margemoins;
    }

    public void setMargemoins(double margemoins) {
        this.margemoins = margemoins;
    }

    public double getMargeplus() {
        return margeplus;
    }

    public void setMargeplus(double margeplus) {
        this.margeplus = margeplus;
    }


    public Reservation () throws Exception {
        setNomTable("reservation");
        setLiaisonFille("idmere");
        setNomClasseFille("reservation.ReservationDetails");
    }

    @Override
    public boolean getEstIndexable() {
        return true;
    }

    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    public void construirePK(Connection c) throws Exception {
        this.preparePk("RESA", "GETSEQRESERVATION");
        this.setId(makePK(c));
    }

    @Override
    public void annulerVisa(String u, Connection c) throws Exception {
        // Annuler les ReservationPlanning liés à la réservation
        ReservationDetails reservationDetails = new ReservationDetails();
        reservationDetails.setIdmere(this.getId());
        reservationDetails.setNomTable("reservationplanning");
        ReservationDetails[] planningreservation = (ReservationDetails[]) CGenUtil.rechercher(reservationDetails, null, null, c, "");

        Check check = new Check();
        check.setNomTable("checkinlibelle");
        check.setIdReservationMere(this.getId());
        Check[] checkins = (Check[]) CGenUtil.rechercher(check, null, null, c, "");
        if(checkins != null && checkins.length > 0){
            throw new Exception("Impossible d annuler la reservation car des check-in ont ete effectues.");
        }
        if(planningreservation != null && planningreservation.length > 0){
            for (ReservationDetails planning : planningreservation) {
                planning.setNomTable("reservationplanning");
                planning.deleteToTable(c);
            }
        }
        super.annulerVisa(u, c);
    }

    public void effectif(String u, Connection c) throws Exception
    {
        boolean isOuvert = false;
        try
        {
            if (c == null)
            {
                c = new UtilDB().GetConn();
                isOuvert = true;
            }
            ReservationDetails r = new ReservationDetails();
            r.setIdmere(this.getId());
            ReservationDetails[] res = (ReservationDetails[]) CGenUtil.rechercher(r, null, null, c, " ");
            for (int j = 0; j < res.length; j++)
            {
                Ingredients ing = new Ingredients();
                ing.setId(res[j].getIdproduit());
                Ingredients[] ings = (Ingredients[]) CGenUtil.rechercher(ing, null, null, c, " ");
                for (int i = 0; i < res[i].getQte(); i++)
                {
                    Acte acte = new Acte();
                    acte.setIdclient(this.getIdclient());
                    acte.setIdreservation(this.getId());
                    acte.setIdproduit(res[j].getIdproduit());
                    acte.setQte(res[i].getQte());
                    acte.setLibelle("Location de/du " + this.getId());
                    acte.setPu(ings[0].getPu());
                    LocalDate ld = this.getDaty().toLocalDate().plusDays(i);
                    Date dt = Date.valueOf(ld);
                    acte.setDaty(dt);
                    acte.createObject(u, c);
                }
            }
        }
        catch (Exception e) {
            c.rollback();
            e.printStackTrace();
            throw new Exception("Rendre effectif réservation non abouti");
        }
        finally {
            if (isOuvert) {
                c.close();
            }
        }
    }


    public ReservationVerifDetails[] genererResaVerifDetails(Connection c) throws Exception{
        boolean estOuvert = false;
        try {
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert = true;
            }
            ReservationDetails reservationDetails = new ReservationDetails();
            reservationDetails.setNomTable("RESERVATIONDETAILS_LIB_sser");
            reservationDetails.setIdmere(this.getId());
            ReservationDetails[] resadetails = (ReservationDetails[]) CGenUtil.rechercher(reservationDetails, null, null, c, "");
            if(resadetails.length>0){
                ReservationVerifDetails [] d = new ReservationVerifDetails[resadetails.length];
                for (int i = 0; i < resadetails.length; i++){
                    d[i] = new ReservationVerifDetails();
                    d[i].setIdreservationdetails(resadetails[i].getId());
                    d[i].setIdreservationverif(this.getId());
                    d[i].setDesignation(resadetails[i].getLibelleproduit());
                }
                return d;
            }
            throw new Exception("Pas de reservation pour "+this.getId());
        }
        catch(Exception e){
            throw e;
        }
        finally {
            if(estOuvert==true && c!=null) c.close();
        }
    }

    public Acte[] getActes(Connection c) throws Exception
    {
        Acte[] actes;
        boolean isOuvert = false;
        try
        {
            if (c == null)
            {
                c = new UtilDB().GetConn();
                isOuvert = true;
            }
            Acte acte = new Acte();
            acte.setIdreservation(this.getId());
            actes = (Acte[]) CGenUtil.rechercher(acte, null, null, c, " ");
        }
        catch (Exception e)
        {
            c.rollback();
            e.printStackTrace();
            throw new Exception("Erreur lors de la recuperation des actes dans la reservation");
        }
        finally {
            if (isOuvert) {
                c.close();
            }
        }
        return actes;
    }
    public List<reservation.ReservationDetails> decomposer(String nT,Connection c) throws Exception{
        List<reservation.ReservationDetails> res = new ArrayList<reservation.ReservationDetails>();
        reservation.ReservationDetails[] listeFille =(reservation.ReservationDetails[])this.getFille();
        if(listeFille==null)listeFille=(reservation.ReservationDetails[])this.getFille(nT,c,"");
        for(reservation.ReservationDetails fille : listeFille)
        {
            res.addAll(fille.decomposer());
        }
        return res;
    }
    public Acte[] getActeAvecSimulation(String nTActe,Connection c)throws Exception {
        boolean estOuvert = false;
        try {
            if(c==null){
                c = new UtilDB().GetConn();
                estOuvert = true;
            }

            List<Acte> retour=new ArrayList<>();
            Check[] listeCheckIn=this.getListeCheckIn(null,c);
            for (int i = 0; i < listeCheckIn.length; i++)
            {
                retour.addAll(Arrays.asList(listeCheckIn[i].getActeAvecSimulation(nTActe,c)));
            }

            return retour.toArray(new Acte[retour.size()]);
        }catch (Exception e) {
            e.printStackTrace();
            throw  e;
        }finally {
            if(estOuvert) c.close();
        }
    }
    public vente.VenteDetails[] genereVenteDetails(String nTableChekIn,Connection c)throws Exception {
        List<vente.VenteDetails> retour=new ArrayList<>();
        Check[] listeCheckIn=this.getListeCheckIn("CHECKINLIBELLE",c);
        for (int i = 0; i < listeCheckIn.length; i++)
        {
            retour.addAll(Arrays.asList(listeCheckIn[i].genereVenteDetails("ACTE_LIB",c)));
        }
        VenteDetails[] venteDetails = retour.toArray(new vente.VenteDetails[retour.size()]);

        if(venteDetails == null || venteDetails.length <= 0){
            venteDetails = genereVente(c);
        }
        return venteDetails;
    }

    public CheckOut[] getListeCheckOut(String nTableChekOut,Connection c)throws Exception {
        CheckOut crt = new CheckOut();
        crt.setNomTable("CHECKOUTAVECRESERVATION");
        if (nTableChekOut != null && nTableChekOut.compareTo("") != 0) crt.setNomTable(nTableChekOut);
        crt.setReservation (this.getId());
        return (CheckOut[]) CGenUtil.rechercher(crt,null,null,c,"");
    }

    public Check[] getListeCheckIn(String nT, Connection c) throws Exception{
        boolean estOuvert = false;
        try {
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert = true;
            }
            Check crt = new Check();
            crt.setNomTable("CHECKINLIBELLE");
            if (nT != null && nT.compareTo("") != 0) crt.setNomTable(nT);
            crt.setIdReservationMere (this.getId());
            return (Check[]) CGenUtil.rechercher(crt,null,null,c,"");
        }
        catch(Exception e){
            throw e;
        }
        finally {
            if(estOuvert==true && c!=null) c.close();
        }
    }

    public Prevision genererPrevision(String u, Connection c) throws Exception{
        Prevision mere = new Prevision();

        ReservationLibAvecDateMax reservationComplet = this.getReservationWithMontant(c);
        mere.setIdOrigine(this.getId());
        Prevision [] previsions = (Prevision[]) CGenUtil.rechercher(mere,null,null,c,"");

        if(previsions.length > 0){
            previsions[0].setCredit(reservationComplet.getResteAPayer());
            previsions[0].updateToTableWithHisto(u, c);
            return previsions[0];
        }

        Date datyPrevu = reservationComplet.getDatyfinpotentiel();
        mere.setDaty(datyPrevu);
        mere.setCredit(reservationComplet.getResteAPayer());
        mere.setIdCaisse(ConstanteStation.idCaisse);
        mere.setIdDevise("AR");
        mere.setDesignation("Prevision rattach&eacute;e au Reservation N : "+this.getId());
        mere.setIdTiers(this.getIdclient());
        return ( Prevision ) mere.createObject(u, c);
    }

    public ReservationLibAvecDateMax getReservationWithMontant(Connection c) throws Exception{
        return (ReservationLibAvecDateMax)new reservation.ReservationLibAvecDateMax().getById(this.getId(), "reservation_lib_avecmaxdate", c);
    }
    

    public Vente[] getFactureClient(String nT, Connection c) throws Exception{
        boolean estOuvert = false;
        try {
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert = true;
            }
            Vente crt = new Vente();
            if (nT != null && nT.compareTo("") != 0) crt.setNomTable(nT);
            crt.setIdReservation(this.getId());
            System.out.println("ID RESA "+crt.getIdReservation());
            return (Vente[]) CGenUtil.rechercher(crt,null,null,c,"");
        }
        catch(Exception e){
            throw e;
        }
        finally {
            if(estOuvert==true && c!=null) c.close();
        }
    }
    public reservation.ReservationDetails[] decomposerEnTableau(String nT,Connection c) throws Exception {
        List<reservation.ReservationDetails> res=decomposer(nT,c);
        return res.toArray (new reservation.ReservationDetails[res.size()]);
    }
    public MvtCaisse[] getAcompte(String nT,Connection c) throws Exception {
        MvtCaisse crt=new MvtCaisse();
        if(nT!=null&&nT.compareTo("") != 0) crt.setNomTable(nT);
        crt.setIdOp(this.getId());
        return (MvtCaisse[]) CGenUtil.rechercher(crt,null,null,c,"");
    }

    public Magasin[] getStockMagasin(Connection c,ReservationDetailsCheck reservation) throws Exception{
        List<Magasin> res=new ArrayList<>();
        Magasin magasin = new Magasin();
        Magasin [] magasins = (Magasin[]) CGenUtil.rechercher(magasin,null,null,c," ORDER BY RANG");
        for(Magasin m : magasins){
            if(m.getQuantiteProduit(c,reservation.getIdproduit())>0){
                res.add(m);
            }
        }
        return res.toArray(new Magasin[res.size()]);
    }

    public boolean isLivraison(Connection c) throws Exception{
        boolean estOuvert = false;
        try {
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert = true;
            }
            ReservationDetailsCheck crt = new ReservationDetailsCheck();
            crt.setNomTable("RESERVATIONDETSANSCI");
            ReservationDetailsCheck[] resultats =  (ReservationDetailsCheck[]) CGenUtil.rechercher(crt,null,null,c," AND IDMERE = '"+this.getId()+"'");
            for(ReservationDetailsCheck r : resultats){
                if(r.getIdproduit().compareToIgnoreCase(ConstanteLocation.id_produit_transport_pers)==0 || r.getIdproduit().compareToIgnoreCase(ConstanteLocation.id_produit_transport_aller)==0){
                    return true;
                }
            }
            return false;
        }
        catch(Exception e){
            throw e;
        }
        finally {
            if(estOuvert==true && c!=null) c.close();
        }
    }

    public ReservationDetailsCheck[] getListeSansCheckIn(String nT, Connection c) throws Exception{
        boolean estOuvert = false;
        try {
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert = true;
            }
            ReservationDetailsCheck crt = new ReservationDetailsCheck();
            if (nT != null && nT.compareTo("") != 0) crt.setNomTable(nT);
            crt.setIdmere (this.getId());
            ReservationDetailsCheck[] resultats =  (ReservationDetailsCheck[]) CGenUtil.rechercher(crt,null,null,c,"");
            String idtype = this.isLivraison(c)?ConstanteLocation.idmodelivraison:ConstanteLocation.idmoderecuperation;
            if (resultats != null && resultats.length > 0) {
                Date dateMin = null;
                for (ReservationDetailsCheck r : resultats) {
                    if (r.getDaty() != null) {
                        if (dateMin == null || r.getDaty().before(dateMin)) {
                            dateMin = r.getDaty();
                        }
                    }
                }

                if (dateMin != null) {
                    System.out.println("Misy ny daty min: " +  dateMin);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateMin);
                    cal.add(Calendar.DATE, -1);
                    Date dateMinMoinsUn = new java.sql.Date(cal.getTimeInMillis());

                    for (ReservationDetailsCheck r : resultats) {
                        r.setDaty(dateMinMoinsUn);
                        System.out.println("Date chaque reservation: " + r.getDaty());
                    }
                }
            }
            List<ReservationDetailsCheck> retour=new ArrayList<>();
            for(ReservationDetailsCheck r : resultats){
                double qteUtil = r.getQtearticle();
                r.setQteUtil(qteUtil);
                Magasin [] tab = getStockMagasin(c,r);
                for(Magasin m : tab){
                    ReservationDetailsCheck r2 = (ReservationDetailsCheck) r.dupliquerSansBase();
                    double qteMag = m.getQuantiteProduit(c,r.getIdproduit());
                    if(qteMag<qteUtil){
                        r2.setQtearticle(qteMag);
                        qteUtil = qteUtil - qteMag;
                    }else if(qteMag>=qteUtil){
                        r2.setQtearticle(qteUtil);
                        qteUtil = 0;
                    }else{
                        r2.setQtearticle(0);
                    }
                    r2.setMagasin(m.getId());
                    r2.setType(idtype);
                    retour.add(r2);
                }
            }
            return retour.toArray(new ReservationDetailsCheck[retour.size()]);
        }
        catch(Exception e){
            throw e;
        }
        finally {
            if(estOuvert==true && c!=null) c.close();
        }
    }

    public ReservationDetails getCautionDetail(ReservationDetails prof)throws Exception{
        ReservationDetails res = null;
        System.err.println(this.getCaution()+" "+prof.getDaty()+" "+prof.getTranche());
        if(this.getCaution()>0 && prof!=null){
            res = new ReservationDetails();
            res.setRemarque("Caution");
            res.setDaty(prof.getDaty());
            res.setQte(1);
            res.setQtearticle(1);
            res.setRemise(0);
            res.setMargemoins(1);
            res.setMargeplus(1);
            res.setIdproduit(ConstanteLocation.id_produit_caution);
        }
        return res;
    }

    @Override
    public ClassMAPTable createObject(String u, Connection c) throws Exception {
        try {
            if(this.getIdorigine()==null || this.getIdorigine().isEmpty() || !this.getIdorigine().contains("PROF")){
                ReservationDetails[] fillestemp = (ReservationDetails[]) this.getFille();
                ReservationDetails[] fillesnew = null;
                ReservationDetails det = this.getCautionDetail(fillestemp[0]);
                if(det!=null && fillestemp.length>0) {
                    fillesnew= new ReservationDetails[fillestemp.length+1];
                    fillesnew[fillestemp.length] = det;
                }else{
                    fillesnew= new ReservationDetails[fillestemp.length];
                }
                double caution = 0.0;
                for (int i = 0; i < fillestemp.length; i++) {
                    fillesnew[i] = fillestemp[i];
                    fillesnew[i].setTva(this.getTva());
                    Ingredients temp = fillestemp[i].getProduit(c);
                    if(temp!=null && temp.getCategorieIngredient().compareToIgnoreCase(ConstanteLocation.type_produit_service)!=0) {
                        Ingredients ing = new Ingredients();
                        ing.setId(fillestemp[i].getIdproduit());
                        Date [] ds = new Date[(int) fillestemp[i].getQte()+1];
                        for (int j = 0; j < ds.length; j++) {
                            ds[j] = Utilitaire.ajoutJourDate(fillestemp[i].getDaty(),j);
                        }
                        Date datyTss = ing.estDispo(ds,(int)fillestemp[i].getQtearticle(),null,null);
                        if(datyTss!=null) {
                            throw new Exception("Le produit "+temp.getLibelle()+" n'est pas disponible pour le date "+datyTss.toString());
                        }
                        fillesnew[i].setRemise(this.getRemise());
                        double dim = 1;
                        if(temp.getDurre()>0) {
                            dim = temp.getDurre();
                        }
                        double montantsansremise = (fillestemp[i].getQtearticle()*fillestemp[i].getPu()*fillestemp[i].getQte()*dim);
                        double montantavecremise = montantsansremise - (fillestemp[i].getQtearticle()*fillestemp[i].getPu()*fillestemp[i].getQte()*fillestemp[i].getRemise())/100;
                        double montanttva = (montantavecremise*fillestemp[i].getTva())/100;
                        double montantttc = montantavecremise + montanttva;
                        caution += montantttc;
                    }else{
                        fillesnew[i].setRemise(0);
                        fillesnew[i].setTva(0);
                    }
                }
                if(det!=null){
                    fillesnew[fillestemp.length].setPu((caution*this.getCaution())/100);
                }

                this.setFille(fillesnew);
            }
            return super.createObject(u, c);
        } catch (Exception e) {
            throw e;
        }

//        reservation.ReservationDetails[] fille=(reservation.ReservationDetails[])this.getFille();
//        ArrayList<reservation.ReservationDetails> retour=new ArrayList<>();
//        for(int i=0;i<fille.length;i++)
//        {
//            retour.addAll(fille[i].decomposer());
//        }
//        reservation.ReservationDetails[] filleVrai=retour.toArray (new reservation.ReservationDetails[retour.size()]);
//        this.setFille(filleVrai);
        /*
        Reservation res = (Reservation) super.createObject(u, c);
        ReservationLib temp = (ReservationLib) new ReservationLib().getById(res.getId(), "reservation_lib", c);
        Caution caution = new Caution();
        caution.setIdreservation(res.getId());
        caution.setPct_applique(ConstanteLocation.caution);
        caution.setDaty(res.getDaty());
        caution.setIdmodepaiement(ConstanteLocation.modepaiementespece);
        caution.setDateprevuerestitution(res.getDaty());
        caution.setMontantreservation(temp.getMontant());
        ReservationDetailsLib det = new ReservationDetailsLib();
        det.setIdmere(temp.getId());
        ReservationDetailsLib [] filles = (ReservationDetailsLib[]) CGenUtil.rechercher(det,null,null,c,"");
        CautionDetails[] details = new CautionDetails[filles.length];
        for (int i = 0; i < filles.length; i++) {
            details[i] = new CautionDetails();
            details[i].setIdreservationdetails(filles[i].getId());
            details[i].setPct_applique(ConstanteLocation.caution);
            details[i].setIdingredient(filles[i].getIdproduit());
            details[i].setMontantreservation(temp.getMontant());
            details[i].setDesignation(filles[i].getLibelleproduit());
            details[i].setMontant(filles[i].getMontant()-((filles[i].getMontant()*ConstanteLocation.caution)/100));
        }
        caution.setFille(details);
        caution.createObject(u, c);
        return res;
         */
    }
    
    @Override
    public Object validerObject(String u, Connection c) throws Exception {
        boolean estOuvert = false;
        try {
            if (c == null) {
                estOuvert = true;
                c = new UtilDB().GetConn();
                c.setAutoCommit(false);
            }
            genererPrevision(u, c);

            traiterReservationDetails(u,c);
            super.validerObject(u, c);

            Vente vente = this.genererVente(u,c);
            vente.validerObject(u,c);
            this.setVente(vente);

            if(estOuvert) c.commit();
            return this;
        } catch (Exception e) {
            if (c != null) {
                c.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }
    }

    /**
     * Traite les détails de réservation et crée les réservations planifiées
     */
    private void traiterReservationDetails(String utilisateur, Connection connexion) throws Exception {

        ReservationDetails[] reservationDetails = (ReservationDetails[]) this.getFille("reservationdetails_lib", connexion, "");
        for (ReservationDetails detail : reservationDetails) {
            if(detail.getProduit(connexion).getCategorieIngredient().compareToIgnoreCase(ConstanteLocation.type_produit_service)==0) continue;
            ReservationDetails[] planifications = detail.genererAutres();

            for (ReservationDetails planification : planifications) {
                planification.setNomTable("reservationplanning");
                verifierDisponibilite(planification, connexion);
                planification.createObject(utilisateur, connexion);
            }
        }
    }

    /**
     * Vérifie la disponibilité d'une réservation planifiée
     */
    private void verifierDisponibilite(ReservationDetails planification, Connection connexion) throws Exception {
        if (!planification.isDisponible(connexion)) {
            //throw new Exception("L'article => "+ planification.getLibelleproduit() +" est non disponible pour " + planification.getDaty() + " " + planification.getTranche());
        }
    }


    public VenteDetails[] genereVente(Connection c)throws  Exception {
        ReservationDetails[] reservationDetails = (ReservationDetails[]) this.getFille(null, c, "");
        VenteDetails[] venteDetails = new VenteDetails[reservationDetails.length];
        boolean estOuvert = false;
        try {
            if (c == null) {
                estOuvert = true;
                c = new UtilDB().GetConn();
                c.setAutoCommit(false);
            }
            for (int i = 0; i < venteDetails.length; i++) {
                venteDetails[i] = new VenteDetails();
                venteDetails[i].setIdProduit(reservationDetails[i].getIdproduit());
                Ingredients ingredients = (Ingredients) new Ingredients().getById(reservationDetails[i].getIdproduit(), "AS_INGREDIENTS_LIB", c);
                venteDetails[i].setDesignation(ingredients.getLibelle());
                venteDetails[i].setCompte(ingredients.getCompte_vente());
                venteDetails[i].setTva(ingredients.getTva());
                venteDetails[i].setQte(reservationDetails[i].getQte());
                venteDetails[i].setPu(reservationDetails[i].getPu());
                venteDetails[i].setIdDevise("AR");
                venteDetails[i].setDatereservation(Utilitaire.stringDate(Utilitaire.datetostring(reservationDetails[i].getDaty())));
            }
        } catch (Exception e) {
            if (c != null) {
                c.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }
        return venteDetails;
    }
    public MvtStock genererMvtStockSortie(Connection c)throws Exception{
        boolean estOuvert=false;
        try{
            if(c==null){
                c=new UtilDB().GetConn();
                estOuvert=true;
            }
        Reservation blc=(Reservation)this.getById(this.getId(),null,c);
            MvtStock mvt=blc.genererMvtStock(c);
        mvt.setEtat(ConstanteEtat.getEtatCreer());
            return mvt;
        } catch(Exception e){
            throw e;
        }
        finally{
            if(c!=null&&estOuvert==true)c.close();
        }
    }
    public MvtStock genererMvtStock(Connection c)throws Exception
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
            mv.setDesignation(this.getRemarque());
            mv.setIdTypeMvStock(ConstanteStation.TYPEMVTSTOCKSORTIE);
            mv.setIdobjet(this.getId());
            Check chk = new Check();
            chk.setNomTable("CHECKINLIBELLE");
            chk.setIdReservationMere(this.getId());
            Check[] listeF=(Check[]) CGenUtil.rechercher(chk, null, null, c, " and etat=11");
            MvtStockFille[]lf=new MvtStockFille[listeF.length];
            for(int i=0;i<listeF.length;i++)
            {   
                lf[i]=listeF[i].genererMouvementStockFille(c);
                lf[i].setIdMvtStock(mv.getId());
            }
            mv.setFille(lf);
            return mv;
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
    public MvtStock genererMvtStockEntrer(Connection c)throws Exception{
        boolean estOuvert=false;
        try{
            if(c==null){
                c=new UtilDB().GetConn();
                estOuvert=true;
            }
        Reservation blc=(Reservation)this.getById(this.getId(),null,c);
            MvtStock mvt=blc.genererMvtStockCheckout(c);
        mvt.setEtat(ConstanteEtat.getEtatCreer());
            return mvt;
        } catch(Exception e){
            throw e;
        }
        finally{
            if(c!=null&&estOuvert==true)c.close();
        }
    }
    public MvtStock genererMvtStockCheckout(Connection c)throws Exception
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
            mv.setDesignation(this.getRemarque());
            mv.setIdTypeMvStock(ConstanteStation.TYPEMVTSTOCKENTREE);
            mv.setIdobjet(this.getId());
            CheckOut chk = new CheckOut();
            chk.setNomTable("checkoutlib");
            chk.setIdReservationMere(this.getId());
            CheckOut[] listeF=(CheckOut[]) CGenUtil.rechercher(chk, null, null, c, " and etat=11");
            MvtStockFille[]lf=new MvtStockFille[listeF.length];
            for(int i=0;i<listeF.length;i++)
            {   
                lf[i]=listeF[i].genererMouvementStockFille(c);
                lf[i].setIdMvtStock(mv.getId());
            }
            mv.setFille(lf);
            return mv;
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

    public Vente reservationVente()throws Exception{
        Vente vente=new Vente();
        vente.setIdReservation(this.getId());
        vente.setIdClient(this.getIdclient());
        vente.setDatyPrevu(this.getDaty());
        vente.setIdMagasin(this.getIdMagasin());
        vente.setDaty(Utilitaire.dateDuJourSql());
        vente.setDesignation("Facture du reservation "+this.getId());
        vente.setIdOrigine(this.getId());
        vente.setLieulocation(this.getLieulocation());
        vente.setIdReservation(this.getId());
        vente.setRemarque(this.getRemarque());
        vente.setCaution(this.getCaution());
        return vente;
    }

    public Proforma reservationProforma(Proforma prof)throws Exception{
        Proforma proforma= prof;
        proforma.setIdReservation(this.getId());
        proforma.setIdClient(this.getIdclient());
        proforma.setDatyPrevu(this.getDaty());
        proforma.setIdMagasin(this.getIdMagasin());
        proforma.setDaty(Utilitaire.dateDuJourSql());
        proforma.setLieulocation(this.getLieulocation());
        proforma.setRemarque(this.getRemarque());
        proforma.setCaution(this.getCaution());
        return proforma;
    }

    public Vente genererVente(String u, Connection c)throws  Exception {
        //Reservation blc=(Reservation)this.getById(this.getId(),null,c);
        ReservationDetails[] reservationDetails = (ReservationDetails[]) this.getFille(null, c, "");
        Vente res= this.reservationVente();
        VenteDetails[] venteDetails = new VenteDetails[reservationDetails.length];
        boolean estOuvert = false;
        try {
            if (c == null) {
                estOuvert = true;
                c = new UtilDB().GetConn();
                c.setAutoCommit(false);
            }
            for (int i = 0; i < venteDetails.length; i++) {
                venteDetails[i] = new VenteDetails();
                venteDetails[i].setIdProduit(reservationDetails[i].getIdproduit());
                Ingredients ingredients = (Ingredients) new Ingredients().getById(reservationDetails[i].getIdproduit(), "AS_INGREDIENTS_LIB", c);
                venteDetails[i].setDesignation(ingredients.getLibelle());
                venteDetails[i].setCompte(ingredients.getCompte_vente());
                venteDetails[i].setTva(reservationDetails[i].getTva());
                venteDetails[i].setQte(reservationDetails[i].getQte());
                venteDetails[i].setPu(reservationDetails[i].getPu());
                venteDetails[i].setIdDevise("AR");
                venteDetails[i].setTauxDeChange(1);
                venteDetails[i].setNombre(reservationDetails[i].getQtearticle());
                venteDetails[i].setRemise(reservationDetails[i].getRemise());
                venteDetails[i].setDatereservation(Utilitaire.stringDate(Utilitaire.datetostring(reservationDetails[i].getDaty())));
            }
            res.setFille(venteDetails);
            return (Vente)res.createObject(u,c);
        } catch (Exception e) {
            if (c != null) {
                c.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }

    }


    public Vente getVente() {
        return vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public Vente getVente(Connection c) throws Exception {
        boolean estOuvert = false;
        try {
            if (c == null) {
                estOuvert = true;
                c = new UtilDB().GetConn();
            }
            Vente temp = new Vente();
            temp.setIdReservation(this.getId());
            Vente[] liste=(Vente[]) CGenUtil.rechercher(temp, null, null, c, "");
            if (liste.length>0){
                return liste[0];
            }
            throw new Exception("Pas de facture pour ce reservation");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }

    }

    public ReservationDetails [] getFilleReservation(Connection c) throws Exception {
        boolean estOuvert = false;
        try {
            if (c == null) {
                estOuvert = true;
                c = new UtilDB().GetConn();
            }
            ReservationDetails temp = new ReservationDetails();
            temp.setIdmere(this.getId());
            ReservationDetails[] liste=(ReservationDetails[]) CGenUtil.rechercher(temp, null, null, c, "");
            if (liste.length>0){
                return liste;
            }
            //throw new Exception("Pas de reservation detail pour ce reservation");
            return new ReservationDetails[0];
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }

    }

    public VenteDetails[] genererVenteDetail(Connection c, ReservationDetails[] filles)throws  Exception {
        ReservationDetails[] reservationDetails = filles;
        VenteDetails[] venteDetails = new VenteDetails[reservationDetails.length];
        boolean estOuvert = false;
        try {
            if (c == null) {
                estOuvert = true;
                c = new UtilDB().GetConn();
            }
            for (int i = 0; i < venteDetails.length; i++) {
                venteDetails[i] = new VenteDetails();
                venteDetails[i].setIdProduit(reservationDetails[i].getIdproduit());
                Ingredients ingredients = (Ingredients) new Ingredients().getById(reservationDetails[i].getIdproduit(), "AS_INGREDIENTS_LIB", c);
                venteDetails[i].setDesignation(ingredients.getLibelle());
                venteDetails[i].setCompte(ingredients.getCompte_vente());
                venteDetails[i].setTva(reservationDetails[i].getTva());
                venteDetails[i].setQte(reservationDetails[i].getQte());
                venteDetails[i].setPu(reservationDetails[i].getPu());
                venteDetails[i].setIdDevise("AR");
                venteDetails[i].setTauxDeChange(1);
                venteDetails[i].setNombre(reservationDetails[i].getQtearticle());
                venteDetails[i].setRemise(reservationDetails[i].getRemise());
                venteDetails[i].setDatereservation(Utilitaire.stringDate(Utilitaire.datetostring(reservationDetails[i].getDaty())));
            }
            return venteDetails;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }

    }

    public ProformaDetails[] genererProformaDetail(Connection c, ReservationDetails[] filles)throws  Exception {
        ReservationDetails[] reservationDetails = filles;
        ProformaDetails[] proformaDetails = new ProformaDetails[reservationDetails.length];
        boolean estOuvert = false;
        try {
            if (c == null) {
                estOuvert = true;
                c = new UtilDB().GetConn();
            }
            for (int i = 0; i < proformaDetails.length; i++) {
                proformaDetails[i] = new ProformaDetails();
                proformaDetails[i].setIdProduit(reservationDetails[i].getIdproduit());
                Ingredients ingredients = (Ingredients) new Ingredients().getById(reservationDetails[i].getIdproduit(), "AS_INGREDIENTS", c);
                proformaDetails[i].setIdProforma(this.getIdorigine());
                proformaDetails[i].setQte((int)reservationDetails[i].getQte());
                proformaDetails[i].setNombre((int)reservationDetails[i].getQtearticle());
                proformaDetails[i].setPu(reservationDetails[i].getPu());
                proformaDetails[i].setRemise(reservationDetails[i].getRemise());
                proformaDetails[i].setTva(reservationDetails[i].getTva());
                proformaDetails[i].setIdDevise("AR");
                proformaDetails[i].setTauxDeChange(1);
                proformaDetails[i].setPuAchat(0);
                proformaDetails[i].setPuVente(0);
                proformaDetails[i].setPuRevient(0);
                proformaDetails[i].setDesignation(ingredients.getLibelle());
                proformaDetails[i].setUnite(ingredients.getUnite());
                proformaDetails[i].setDatedebut(Utilitaire.stringDate(Utilitaire.datetostring(reservationDetails[i].getDaty())));
                proformaDetails[i].setMargemoins(reservationDetails[i].getMargemoins());
                proformaDetails[i].setMargeplus(reservationDetails[i].getMargeplus());
            }
            return proformaDetails;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }

    }

    public ReservationDetails[] enleverCaution(ReservationDetails[] fillestemp)throws Exception{
        List<ReservationDetails> liste=new ArrayList<>();
        for(ReservationDetails fille:fillestemp){
            if(fille.getIdproduit().compareToIgnoreCase(ConstanteLocation.id_produit_caution)!=0){
                liste.add(fille);
            }
        }
        return liste.toArray(new ReservationDetails[liste.size()]);
    }

    public String remplacant(UserEJB u,ReservationDetails [] filles) throws Exception{
        boolean estOuvert = false;
        Connection c = null;
        ReservationDetails[] listePlaA = null;
        try {
            if (c == null) {
                estOuvert = true;
                c = new UtilDB().GetConn();
                c.setAutoCommit(false);
            }
            System.err.println("1");
            ReservationDetails [] olds = getFilleReservation(c);
            //this.updateToTableWithHisto(u.getUser().getTuppleID(),c);

            Proforma[] proformas = (Proforma[]) CGenUtil.rechercher(new Proforma(), null, null, c, " AND ID='" + this.getIdorigine() + "'");
            if(proformas.length==0){
                throw new Exception("Pas de proforma pour cette reservation");
            }

            ProformaDetails[] proformaDetails = (ProformaDetails[]) CGenUtil.rechercher(new ProformaDetails(), null, null, c, " AND IDPROFORMA='" + proformas[0].getId() + "'");


            for (int i = 0; i < olds.length; i++) {
                ReservationDetails temp = new ReservationDetails();
                temp.setNomTable("RESERVATIONPLANNING");
                ReservationDetails[] listePla = (ReservationDetails[]) CGenUtil.rechercher(temp, null, null, c, " AND IDRESADETAILS='" + olds[i].getId() + "'");
                listePlaA = new ReservationDetails[listePla.length];
                for (int j = 0; j < listePla.length; j++) {
                    listePlaA[j] = listePla[j];
                    int a = listePla[j].deleteToTable(c);
                }
            }

            ReservationDetails[] fillestemp = enleverCaution(filles);

            ReservationDetails[] fillesnew = null;
            ReservationDetails det = this.getCautionDetail(fillestemp[0]);
            if(det!=null && fillestemp.length>0) {
                fillesnew= new ReservationDetails[fillestemp.length+1];
                fillesnew[fillestemp.length] = det;
                fillesnew[fillestemp.length].setId("");
            }else{
                fillesnew= new ReservationDetails[fillestemp.length];
            }
            double caution = 0.0;
            for (int i = 0; i < fillestemp.length; i++) {
                fillesnew[i] = fillestemp[i];
                fillesnew[i].setTva(this.getTva());
                fillesnew[i].setEtat(11);
                Ingredients temp = fillestemp[i].getProduit(c);
                if(temp!=null && temp.getCategorieIngredient().compareToIgnoreCase(ConstanteLocation.type_produit_service)!=0) {
                    Ingredients ing = new Ingredients();
                    Ingredients [] all = ((Ingredients[]) CGenUtil.rechercherPrecis(ing,null,null,c," AND ID='"+fillestemp[i].getIdproduit()+"'"));
                    if(all==null || all.length<=0) {
                        throw new Exception("Produit non valide");
                    }
                    ing = all[0];
                    Date [] ds = new Date[(int) fillestemp[i].getQte()+1];
                    String message ="Liste des articles disponibles : ";
                    for (int j = 0; j < ds.length; j++) {
                        ds[j] = Utilitaire.ajoutJourDate(fillestemp[i].getDaty(),j);
                        double dispo = ing.checkDisponibilite(c,ds[j]);
                        if(fillestemp[i].getQtearticle()>dispo){
                            message += dispo + " "+temp.getLibelle()+" date "+ds[j].toString()+" //";
                        }
                    }
                    if(message.compareToIgnoreCase("Liste des articles disponibles : ")!=0){
                        throw new Exception(message);
                    }
                    fillesnew[i].setRemise(this.getRemise());
                    double dim = 1;
                    if(temp.getDurre()>0) {
                        dim = temp.getDurre();
                        System.err.println("==========DMMMMMMMMMMm======================"+dim);
                    }
                    double montantsansremise = (fillestemp[i].getQtearticle()*fillestemp[i].getPu()*fillestemp[i].getQte()*dim);
                    double montantavecremise = montantsansremise - (fillestemp[i].getQtearticle()*fillestemp[i].getPu()*fillestemp[i].getQte()*fillestemp[i].getRemise())/100;
                    double montanttva = (montantavecremise*fillestemp[i].getTva())/100;
                    double montantttc = montantavecremise + montanttva;
                    caution += montantttc;
                }else{
                    fillesnew[i].setRemise(0);
                    fillesnew[i].setTva(0);
                }
            }
            this.setEtat(11);
            this.setFille(fillesnew);


            for (int i = 0; i < olds.length; i++) {
                ReservationDetails temp = new ReservationDetails();
                temp.setNomTable("RESERVATIONPLANNING");
                ReservationDetails[] listePla=(ReservationDetails[]) CGenUtil.rechercher(temp, null, null, c, " AND IDRESADETAILS='"+olds[i].getId()+"'");
                for (int j = 0; j < listePla.length; j++) {
                    int a = listePla[j].deleteToTable(c);
                }
                ReservationVerifDetails dete = new ReservationVerifDetails();
                ReservationVerifDetails[] listeVer=(ReservationVerifDetails[]) CGenUtil.rechercher(dete, null, null, c, " AND IDRESERVATIONDETAILS='"+olds[i].getId()+"'");
                for (int j = 0; j < listeVer.length; j++) {
                    int a = listeVer[j].deleteToTable(c);
                }
                olds[i].setNomTable("reservationdetails");
                int b = olds[i].deleteToTable(c);
            }

            Caution cauOlds = new Caution();
            cauOlds.setIdreservation(this.getId());
            Caution[] cautions = (Caution[]) CGenUtil.rechercher(cauOlds, null, null, c, "");
            for (int i = 0; i < cautions.length; i++) {
                cautions[i].deleteToTable(c);
            }


            for (int i = 0; i < fillesnew.length; i++) {
                fillesnew[i].setIdmere(this.getId());
                fillesnew[i].setNomTable("reservationdetails");
                //fillesnew[i].updateToTableWithHisto(u.getUser().getTuppleID(), c);
                ReservationDetails r = (ReservationDetails) fillesnew[i].createObject(u.getUser().getTuppleID(), c);
            }
            if(det!=null){
                System.err.println("===============================+CAAAAAAAA"+(caution*this.getCaution())/100);
                fillesnew[fillestemp.length].setPu((caution*this.getCaution())/100);
                Caution cau = new Caution();
                cau.setIdreservation(this.getId());
                cau = cau.genererCaution(c, this.getCaution(), this.getRemise());
                cau.createObject(u.getUser().getTuppleID(),c);
                cau.validerObject(u.getUser().getTuppleID(), c);
            }
            this.traiterReservationDetails(u.getUser().getTuppleID(),c);
            Vente v = this.getVente(c);
            v.setNomTable("VENTE");

            Vente vnew = this.reservationVente();
            vnew.setId(v.getId());
            vnew.setNumfacture(v.getNumfacture());

            VenteDetails [] vd = v.getFilleVente(c);
            for (int i = 0; i < vd.length; i++) {
                int zaz = vd[i].deleteToTable(c);
            }
            VenteDetails [] vdn = this.genererVenteDetail(c,fillesnew);
            v.setFille(vdn);
            for (int i = 0; i < vdn.length; i++) {
                vdn[i].setNomTable("VENTE_DETAILS");
                vdn[i].setIdVente(v.getId());
                vdn[i].createObject(u.getUser().getTuppleID(),c);
            }


            Proforma pnew = this.reservationProforma(proformas[0]);
            pnew.setId(proformas[0].getId());

            for (int i = 0; i < proformaDetails.length; i++) {
                int zaz = proformaDetails[i].deleteToTable(c);
            }
            ProformaDetails [] pdn = this.genererProformaDetail(c,fillesnew);
            pnew.setFille(pdn);
            for (int i = 0; i < pdn.length; i++) {
                pdn[i].setNomTable("PROFORMA_DETAILS");
                pdn[i].setIdProforma(pnew.getId());
                pdn[i].createObject(u.getUser().getTuppleID(),c);
            }

            this.updateToTableWithHisto(u.getUser().getTuppleID(),c);
            vnew.updateToTableWithHisto(u.getUser().getTuppleID(), c);
            pnew.updateToTableWithHisto(u.getUser().getTuppleID(),c);
            vnew.validerObject(u.getUser().getTuppleID(),c);
            c.commit();
            return v.getId();
        } catch (Exception e) {
            try {
                if(listePlaA!=null)for (int i = 0; i < listePlaA.length; i++) {
                    listePlaA[i].insertToTable();
                }
                if (c != null) c.rollback();
            } catch (Exception ex) {
                System.err.println("ECHEC ROLLBACK : " + ex.getMessage());
            }
            throw e;
        } finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }
    }



    public void controleQteTotal(Check[] cfille) throws Exception {
        Map<String, Double> qteSortieParProduit = new HashMap<>();
        Map<String, Double> qteDemandeeParProduit = new HashMap<>();
        Map<String, Check> refParProduit = new HashMap<>();

        for (Check c : cfille) {
            String idProduit = c.getIdProduit();

            double qteSortie = c.getQte();
            double qteDemandee = c.getReservationDetail().getQtearticle();

            // Somme des sorties
            qteSortieParProduit.merge(idProduit, qteSortie, Double::sum);

            // Prendre UNE SEULE FOIS la quantité demandée
            qteDemandeeParProduit.putIfAbsent(idProduit, qteDemandee);

            refParProduit.putIfAbsent(idProduit, c);
        }

        for (String idProduit : qteSortieParProduit.keySet()) {
            double sortieTotale = qteSortieParProduit.get(idProduit);
            double demandee = qteDemandeeParProduit.get(idProduit);

            if (Double.compare(sortieTotale, demandee) != 0) {
                Check ref = refParProduit.get(idProduit);
                String msg = new String("Quantit insuffisante pour le produit "
                        + ref.getProduitLibelle()
                        + " : sortie = " + sortieTotale
                        + ", demandee = " + demandee
                        + ". Verifiez les autres magasins.");
                throw new Exception(
                        msg
                );
            }

        }
    }


    public void controleQteMagasin(Connection c, String magasin, String idproduit, double qte)throws Exception{
        Magasin [] mags = (Magasin[]) CGenUtil.rechercher(new Magasin(), null,null,c," and id='"+magasin+"'");
        if(mags==null || mags.length<0){
            throw new Exception("Le magasin "+magasin+" n'existe pas");
        }
        double qteDisp = mags[0].getQuantiteProduit(c,idproduit);
        if(qteDisp<qte){
            throw new Exception("La quantite du produit "+idproduit+" dans le magasin "+mags[0].getVal()+" est insuffisante. Quantite disponible "+qteDisp);
        }
    }


    public void createObjectFilleMultipleSansMere(UserEJB u, ClassMAPTable[] cfille) throws Exception{
        Connection c = null;
        try{
            c = new UtilDB().GetConn();
            c.setAutoCommit(false);
            if (cfille.length > 0 ){
                controleQteTotal((Check[])cfille);
                Check check = (Check) cfille[0];
                Date dateMin = check.getDaty();
                for (int i=1; i<cfille.length; i++){
                    Check ch = (Check) cfille[i];
                    controleQteMagasin(c,ch.getIdmagasin(),ch.getIdProduit(),ch.getQte());
                    if (ch.getDaty().before(dateMin)){
                        dateMin = ch.getDaty();
                    }
                }
                ReservationDetails rcheck = new ReservationDetails();
                rcheck.setId(check.getReservation());
                ReservationDetails[] rchecklist = (ReservationDetails[]) CGenUtil.rechercher(rcheck,null,null,c,"");
                if (rchecklist.length>0){
                    Reservation mere = (Reservation) new Reservation().getById(rchecklist[0].getIdmere(),null,c);
                    if (mere.getNumBl() == null){
                        CleReference cle = new CleReference();
                        String datyMin = dateMin.toString().replace("-", "");
                        cle.setId(datyMin);
                        cle.setNomTab("RESERVATION");
                        cle.createObject(u.getUser().getTuppleID(),c);
                        mere.setNumBl(datyMin+"-"+cle.getNextVal());
                        for (int i=0; i<cfille.length; i++){
                            Check ch = (Check) cfille[i];
                            ch.setNumBl(mere.getNumBl());
                        }
                        mere.updateToTableWithHisto(u.getUser().getTuppleID(),c);
                    }
                }
            }

            u.createObjectMultipleSansMere(cfille,c);
            c.commit();
        }
        catch(Exception e){
            if(c!=null) c.rollback();
            throw e;
        }
        finally{
            if(c!=null) c.close();
        }

    }

    public CheckOut[] getCheckOut(Connection c)throws Exception{
        if(c==null){
            c = new UtilDB().GetConn();
        }
        CheckOut checkOut = new CheckOut();
        checkOut.setNomTable("CheckOutReservation");
        CheckOut[] checkOuts = (CheckOut[])CGenUtil.rechercher(checkOut, null, null, c, " and idReservation = '"+this.getId()+"'");
        return checkOuts;
    }

    public double getMontantRetenue(Connection c, CautionLib caution)throws Exception{
        if(c==null){
            c = new UtilDB().GetConn();
        }
        CheckOut[] checkOuts = this.getCheckOut(c);
        double total = 0;
        for (int i = 0; i < checkOuts.length; i++) {
            total += (caution.getMontantgrp()*(checkOuts[i].getRetenue()/100));
//            Check check = checkOuts[i].getCheckIn(c);
//            if(check!=null){
//                ReservationDetails[] reservationDetails = (ReservationDetails[]) CGenUtil.rechercher(new ReservationDetails(), null, null, c, " and id = '"+check.getReservation()+"'");
//                if(reservationDetails.length!=0){
//                    System.out.println(total);
//                }
//            }
        }
        return total;
    }

    public double getMontantRetenue(CautionLib caution)throws Exception{
        return this.getMontantRetenue(null, caution);
    }

    public CautionLib getCautions() throws Exception {
        CautionLib cautionLib = new CautionLib();
        cautionLib.setIdreservation(this.getId());
        CautionLib[] cautionLibs = (CautionLib[]) CGenUtil.rechercher(cautionLib, null, null, null, "");
        if(cautionLibs.length>0){
            return cautionLibs[0];
        }
        return new CautionLib();
    }
}
