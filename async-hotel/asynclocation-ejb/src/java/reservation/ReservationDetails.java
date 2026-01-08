package reservation;

import annexe.Unite;
import bean.CGenUtil;
import bean.ClassFille;
import produits.Ingredients;
import utilitaire.UtilDB;
import utilitaire.Utilitaire;
import vente.VenteDetails;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ReservationDetails extends ClassFille
{
    String id;
    String idmere;
    String idproduit;
    String remarque;
    String heure;
    double qte, pu, distanceestimation;
    double qtearticle;
    Date daty;
    String tranche;
    int nbDemiJournee;
    String idVoiture;
    String idResadetails;
    double margemoins;
    double margeplus;
    String libelleproduit;
    double remise,tva;
    String referenceproduit;

    public String getReferenceproduit() {
        return referenceproduit;
    }

    public void setReferenceproduit(String referenceproduit) {
        this.referenceproduit = referenceproduit;
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

    public double getMontantCalcule()
    {
        return this.getQte()*this.getPu();
    }

    public  String getNomClasseMere() {
        return "reservation.Reservation";
    }
    public String getLiaisonMere() {
        return "idmere";
    }
    
    public String getLibelleproduit() {
        return libelleproduit;
    }

    public void setLibelleproduit(String libelleproduit) {
        this.libelleproduit = libelleproduit;
    }

    public String getId() {
        return id;
    }
    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
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

    public void setIdproduit(String idproduits) {
        this.idproduit = idproduits;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public double getQte() {
        return qte;
    }

    public void setQte(double qte) throws Exception {
        if(this.getMode().equals("modif")){
            if(qte <= 0){
                throw new Exception("Quantit\u00E9 insuffisante pour une ligne");
            }
        }
        this.qte = qte;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

    public double getPu() {
        return pu;
    }

    public String getIdVoiture() {
        return idVoiture;
    }

    public void setIdVoiture(String idVoiture) {
        this.idVoiture = idVoiture;
    }

    public void setPu(double pu) throws Exception {
        if(this.getMode().equals("modif")){
            if(pu <= 0){
                throw new Exception("Prix unitaire invalide pour une ligne");
            }

        }
        this.pu = pu;
    }

    public double getDistanceestimation() {
        return distanceestimation;
    }

    public void setDistanceestimation(double distanceestimation) {
        this.distanceestimation = distanceestimation;
    }

    public int getNbDemiJournee() throws Exception {
        double margetotal = this.getMargemoins()+this.getMargeplus();
        if (qte <= 0) {
            throw new IllegalStateException("Impossible de calculer nbDemiJournee à partir d'une qte non positive : " + qte);
        }
        Ingredients ingredients = (Ingredients) new Ingredients().getById(this.getIdproduit(),null,null);
        Unite unite = (Unite) new Unite().getById(ingredients.getUnite(),"AS_UNITE",null);
        System.out.println("getEquivalence=======>"+unite.getEquivalence());
        double nbDem = unite.getEquivalence() * (this.getQte()+margetotal);
        // 1 jour = 2 demi-journées
//        this.nbDemiJournee = (int) Math.round(this.qte * 2);
        this.setNbDemiJournee((int)Math.round(nbDem));
        return nbDemiJournee;
    }


    public String getIdResadetails() {
        return idResadetails;
    }

    public void setIdResadetails(String idResadetails) {
        this.idResadetails = idResadetails;
    }

    public void setNbDemiJournee(int nbDemiJournee) {
        this.nbDemiJournee = nbDemiJournee;
    }

    public String getTranche() {
        return tranche;
    }

    public void setTranche(String tranche) {
        this.tranche = tranche;
    }

    public double getQtearticle() {
        return qtearticle;
    }

    public void setQtearticle(double qtearticle) {
        this.qtearticle = qtearticle;
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

    public ReservationDetails() throws Exception {
        setNomTable("reservationdetails");
        setLiaisonMere("idmere");
        setNomClasseMere("reservation.Reservation");
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
        this.preparePk("RESADET", "GETSEQRESERVATIONDETAILS");
        this.setId(makePK(c));
    }
    public List<ReservationDetails> decomposer() throws Exception {
        List<ReservationDetails> res = new ArrayList<ReservationDetails>();
        for(int i=1;i<=this.getQte();i++) {
            ReservationDetails r = (ReservationDetails) this.dupliquerSansBase();
            r.setQte(1);
            r.setDaty(Utilitaire.ajoutJourDate(this.getDaty(),i-1));
            res.add(r);
        }
        return res;
    }
    public ReservationDetails[] decomposerEnTableau() throws Exception {
        List<ReservationDetails> res=decomposer();
        return res.toArray (new ReservationDetails[res.size()]);
    }

    public ReservationDetails[] genererAutres() throws Exception{
        int n = this.getNbDemiJournee();
        ReservationDetails[] result = new ReservationDetails[n];
        String[] tranches = { "AM", "PM" };
        Date dateDebut = Utilitaire.ajoutJourDate(this.getDaty(), (int) -margemoins);
        int jourCourant = 0;
        if(n <= 1){
            int isAfterNoon = Utilitaire.diffDeuxheures(this.heure,"12:00");
            ReservationDetails copied;
            copied = (ReservationDetails) this.dupliquerSansBase();
            copied.setDaty(dateDebut);
            copied.tranche = (this.heure != null && isAfterNoon < 0) ? "PM" : "AM";
            copied.setIdResadetails(this.getId());
            copied.setQte(this.getQtearticle());
            copied.setId(null);
            result[0] = copied;
            return result;
        } else {
            for (int i = 0; i < n; i++) {
                ReservationDetails copied;
                copied = (ReservationDetails) this.dupliquerSansBase();
                copied.setIdResadetails(this.getId());
                copied.setId(null);
                copied.tranche = tranches[i % 2]; // alterne AM / PM
                if (i % 2 == 0 && i > 0) {
                    jourCourant++;
                }
                copied.setQte(this.getQtearticle());
                copied.daty = Utilitaire.ajoutJourDate(dateDebut, jourCourant);
                result[i] = copied;
            }
        }
        return result;
    }

    /*
    public ReservationDetails[] genererAutres() throws Exception{
        int n = this.getNbDemiJournee();


        ReservationDetails[] result = new ReservationDetails[n];
        String[] tranches = { "AM", "PM" };
        Date dateDebut = Utilitaire.ajoutJourDate(this.getDaty(), (int) -this.);
        int jourCourant = 0;
        if(n <= 1){
            int isAfterNoon = Utilitaire.diffDeuxheures(this.heure,"12:00");
            ReservationDetails copie = new ReservationDetails();
            copie = (ReservationDetails) this.dupliquerSansBase();
            copie.setDaty(dateDebut);
            copie.tranche = (this.heure != null && isAfterNoon < 0) ? "PM" : "AM";
            copie.setIdResadetails(this.getId());
            copie.setId(null);
            result[0] = copie;
            return result;
        } else {
            for (int i = 0; i < n; i++) {
                ReservationDetails copie = new ReservationDetails();
                copie = (ReservationDetails) this.dupliquerSansBase();
                copie.setIdResadetails(this.getId());
                copie.setId(null);
                copie.tranche = tranches[i % 2]; // alterne AM / PM
                if (i % 2 == 0 && i > 0) {
                    jourCourant++;
                }
                copie.daty = Utilitaire.ajoutJourDate(dateDebut, jourCourant);
                result[i] = copie;
            }
        }
        return result;
    }*/



    public boolean isDisponible(Connection c) throws Exception {
        boolean estOuvert = false;
        try {
            if (c == null) {
                estOuvert = true;
                c = new UtilDB().GetConn();
            }
            ReservationDetails res = new ReservationDetails();
            res.setNomTable("reservationPlaninngVise");
            res.setIdproduit(this.getIdproduit());
            //System.out.println("TRANCHE ======= "+this.getTranche());
            res.setTranche(this.tranche);
            String[] colInt = {"daty"};
            String[] valInt = {Utilitaire.datetostring(this.daty),Utilitaire.datetostring(this.daty)};
            ReservationDetails[] reservationDetails = (ReservationDetails[]) CGenUtil.rechercher(res,colInt,valInt,c,"");
            if(reservationDetails.length > 0){
                return false;
            }else {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (c != null && estOuvert == true) {
                c.close();
            }
        }
    }


    @Override
    public String toString() {
        return "ReservationDetails{" +
                "daty=" + daty +
                ", id='" + id + '\'' +
                ", idmere='" + idmere + '\'' +
                ", idproduit='" + idproduit + '\'' +
                ", remarque='" + remarque + '\'' +
                ", heure='" + heure + '\'' +
                ", qte=" + qte +
                ", pu=" + pu +
                ", tranche='" + tranche + '\'' +
                ", nbDemiJournee=" + nbDemiJournee +
                '}';
    }

    @Override
    public void controlerDelete(Connection c) throws Exception {
        if(this.getNomTable().equals("RESERVATIONPLANNING")){
            return;
        }
        super.controlerDelete(c);
    }

    public Ingredients getProduit(Connection c) throws Exception {
        Ingredients ing = new Ingredients();
        ing.setId(this.getIdproduit());
        Ingredients [] ingredients = (Ingredients[]) CGenUtil.rechercher(ing,null,null,c,"");
        if(ingredients!=null && ingredients.length>0){
            return ingredients[0];
        }
        return null;
    }

    public VenteDetails genererVenteDetail(String idVente) throws Exception{
        VenteDetails res = new VenteDetails();
        res.setIdVente(idVente);
        res.setIdProduit(this.getIdproduit());
        res.setRemise(this.getRemise());
        res.setQte(this.getQte());
        res.setPu(this.getPu());
        res.setTva(this.getTva());
        res.setIdDevise("AR");
        res.setTauxDeChange(1);
        res.setDatereservation(this.getDaty());
        res.setNombre(this.getQtearticle());
        return res;
    }
}
