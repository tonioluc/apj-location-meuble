package proforma;

import java.sql.Connection;
import java.sql.Date;

import bean.CGenUtil;
import constante.ConstanteEtat;
import reservation.Reservation;
import utilitaire.UtilDB;
import caisse.MvtCaisse;
import vente.VenteLib;

public class ProformaLib extends Proforma{
    private String idMagasinLib,etatLib,idClientLib,adresse,contact,idDevise, periode;
    private double montantTotal,montantTva,montantTtc,montantTtcAr,montantPaye,montantreste,avoir,tauxDechange,montantRevient,margeBrute,montantremise,montant;
    private Date datedebut,daterelance, datedebutmin, datefinmax;
    protected String etatpaymentlib;

    public String getEtatpaymentlib() {
        return etatpaymentlib;
    }

    public VenteLib getVenteLib() throws Exception {
        Connection c = null;
        boolean estOuvert=false;
        try{
            if(c==null){
                c=new UtilDB().GetConn();
                estOuvert=true;
            }
            Reservation [] res = (Reservation[]) CGenUtil.rechercher(new Reservation(),null,null,c," AND IDORIGINE='"+this.getId()+"'");
            if(res.length==0){
                return null;
            }
            VenteLib vl = new VenteLib();
            VenteLib [] vente = (VenteLib[]) CGenUtil.rechercher(new VenteLib(),null,null,c," AND IDRESERVATION='"+res[0].getId()+"'");
            if(vente.length==0){
                return null;
            }
            return vente[0];
        }
        catch(Exception e){
            throw e;
        }
        finally{
            if(c!=null&&estOuvert==true)c.close();
        }
    }

    public void setEtatpaymentlib(String etatpaymentlib) {
        if(this.getMontantreste()>0){
            this.etatpaymentlib = "<span class=\"badge bg-danger fw-normal\">ACOMPTE</span>";
        }
        else if(this.getMontantreste()==0){
            this.etatpaymentlib = "<span class=\"badge bg-success fw-normal\">PAYE TOTALITE</span>";
        }
        //this.etatpaymentlib = etatpaymentlib;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Date getDatedebutmin() {
        return datedebutmin;
    }

    public void setDatedebutmin(Date datedebutmin) {
        this.datedebutmin = datedebutmin;
    }

    public Date getDatefinmax() {
        return datefinmax;
    }

    public void setDatefinmax(Date datefinmax) {
        this.datefinmax = datefinmax;
    }

    public Date getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(Date datedebut) {
        this.datedebut = datedebut;
    }

    public Date getDaterelance() {
        return daterelance;
    }

    public void setDaterelance(Date daterelance) {
        this.daterelance = daterelance;
    }
    
    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getMontantremise() {
        return montantremise;
    }

    public void setMontantremise(double montantremise) {
        this.montantremise = montantremise;
    }

    public ProformaLib()throws Exception{
        this.setNomTable("PROFORMA_CPL");
    }

    public void setAvoir(double avoir) {
        this.avoir = avoir;
    }

    public void setIdMagasinLib(String idMagasinLib) {
        this.idMagasinLib = idMagasinLib;
    }

    public void setEtatLib(String etatLib) {
        this.etatLib = etatLib;
    }

    public void setIdClientLib(String idClientLib) {
        this.idClientLib = idClientLib;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setMontantTva(double montantTva) {
        this.montantTva = montantTva;
    }

    public void setMontantTtc(double montantTtc) {
        this.montantTtc = montantTtc;
    }

    public void setMontantTtcAr(double montantTtcAr) {
        this.montantTtcAr = montantTtcAr;
    }

    public void setMontantPaye(double montantPaye) throws Exception{
        VenteLib vl = this.getVenteLib();
        if(vl==null){
            this.montantPaye = 0;
        }else{
            this.montantPaye = vl.getMontantpaye();
        }
        //this.montantPaye = montantPaye;
    }

    public void setMontantreste(double montantreste) throws Exception {
        VenteLib vl = this.getVenteLib();
        if(vl==null){
            this.montantreste = montantreste;
        }else{
            this.montantreste = vl.getMontantreste();
        }
        //this.montantreste = montantreste;
    }

    public void setTauxDechange(double tauxDechange) {
        this.tauxDechange = tauxDechange;
    }

    public void setMontantRevient(double montantRevient) {
        this.montantRevient = montantRevient;
    }

    public void setMargeBrute(double margeBrute) {
        this.margeBrute = margeBrute;
    }

    public double getMontantPaye() {
        return montantPaye;
    }

    public String getIdMagasinLib() {
        return idMagasinLib;
    }

    public String getEtatLib() {
        return etatLib;
    }

    public String getIdClientLib() {
        return idClientLib;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getContact() {
        return contact;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public double getMontantTva() {
        return montantTva;
    }

    public double getMontantTtc() {
        return montantTtc;
    }

    public double getMontantTtcAr() {
        return montantTtcAr;
    }

    public double getMontantreste() {
        return montantreste;
    }

    public double getAvoir() {
        return avoir;
    }

    public double getTauxDechange() {
        return tauxDechange;
    }

    public double getMontantRevient() {
        return montantRevient;
    }

    public double getMargeBrute() {
        return margeBrute;
    }

    public String getIdDevise() {
        return idDevise;
    }

    public void setIdDevise(String idDevise) {
        this.idDevise = idDevise;
    }
      public MvtCaisse genererMvtCaisseEntree(Connection c)throws Exception{
	    boolean estOuvert=false;   
        try{
            if(c==null){
                c=new UtilDB().GetConn();
                estOuvert=true;
            }
        ProformaLib blc=(ProformaLib)this.getById(this.getId(),null,c);
	    MvtCaisse mvt=blc.genererMvtCaisseEntreeProforma(c);
        mvt.setEtat(ConstanteEtat.getEtatCreer());
	    return mvt;
	    }
	    catch(Exception e){
            throw e;
        }
        finally{
            if(c!=null&&estOuvert==true)c.close();
        }
    }
    public MvtCaisse genererMvtCaisseEntreeProforma(Connection c)throws Exception
    {
        boolean estOuvert=false;
        try
        {
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert=true;
            }
            
            MvtCaisse mv=new MvtCaisse();
            mv.setDesignation("Paiement par proforma "+this.getDaty());
            //mv.setIdOrigine(this.getId());
            mv.setIdproforma(this.getId());
            mv.setIdTiers(this.getIdClient());
            mv.setDaty(this.getDaty());
            mv.setCredit(this.getMontantreste());
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
    
}
