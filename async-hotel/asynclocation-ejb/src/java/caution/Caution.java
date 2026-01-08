package caution;

import bean.CGenUtil;
import bean.ClassMere;
import caisse.MvtCaisse;
import mg.cnaps.configuration.Configuration;
import reservation.Reservation;
import reservation.ReservationDetails;
import reservation.ReservationDetailsLib;
import reservation.ReservationLib;
import utilitaire.UtilDB;
import utils.ConstanteLocation;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

public class Caution extends ClassMere {

    String id, idreservation, idmodepaiement, referencepaiement;
    double pct_applique,montantreservation;
    Date daty, dateprevuerestitution;

    public Caution genererCaution(Connection c) throws Exception{
        Caution result = new Caution();
        boolean estOuvert = false;
        try {
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert = true;
            }
            Configuration conf = (Configuration) new Configuration().getById(ConstanteLocation.tauxcaution, "CONFIGURATION", c);
            ReservationLib r = (ReservationLib) new ReservationLib().getById(this.getIdreservation(), "RESERVATION_LIB", c);

            result.setIdreservation(this.getIdreservation());
            result.setPct_applique(Double.parseDouble(conf.getValmin()));
            result.setMontantreservation(r.getMontant());

            ReservationDetailsLib resd = new ReservationDetailsLib();
            resd.setIdmere(this.getIdreservation());
            ReservationDetailsLib[] resds = (ReservationDetailsLib[]) CGenUtil.rechercher(resd,null,null,c,"");
            ArrayList<CautionDetails> liste = new ArrayList<>();


            for(int i=0;i<resds.length;i++){
                if(resds[i].getIdproduit().equals(ConstanteLocation.id_produit_caution)||resds[i].getIdproduit().equals(ConstanteLocation.id_produit_transport_aller)||resds[i].getIdproduit().equals(ConstanteLocation.id_produit_transport_retour)||resds[i].getIdproduit().equals(ConstanteLocation.id_produit_transport_pers)) continue;
                CautionDetails detailc = new CautionDetails();
                detailc.setPct_applique(Double.parseDouble(conf.getValmin()));
                detailc.setIdreservationdetails(resds[i].getId());
                detailc.setMontantreservation(resds[i].getMontant());
                detailc.setDesignation(resds[i].getLibelleproduit());
                detailc.setIdingredient(resds[i].getIdproduit());
                detailc.setMontant((Double.parseDouble(conf.getValmin())*resds[i].getMontant())/100);
                liste.add(detailc);
            }
            CautionDetails [] det = new CautionDetails[liste.size()];
            det = liste.toArray(det);
            if(det.length>0){
                result.setFille(det);
                return result;
            }
            throw new Exception("Pas de reservation pour "+this.getIdreservation());

        }
        catch(Exception e){
            throw e;
        }
        finally {
            if(estOuvert==true && c!=null) c.close();
        }
    }

    public Caution genererCaution(Connection c, double taux, double remise) throws Exception{
        Caution result = new Caution();
        boolean estOuvert = false;
        try {
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert = true;
            }
            Configuration conf = (Configuration) new Configuration().getById(ConstanteLocation.tauxcaution, "CONFIGURATION", c);
            ReservationLib r = (ReservationLib) new ReservationLib().getById(this.getIdreservation(), "RESERVATION_LIB", c);

            result.setIdreservation(this.getIdreservation());
            result.setPct_applique(Double.parseDouble(conf.getValmin()));
            result.setMontantreservation(r.getMontant());

            ReservationDetailsLib resd = new ReservationDetailsLib();
            //resd.setIdmere(this.getIdreservation());
            ReservationDetailsLib[] resds = (ReservationDetailsLib[]) CGenUtil.rechercher(resd,null,null,c," AND IDMERE='"+this.getIdreservation()+"'");
            ArrayList<CautionDetails> liste = new ArrayList<>();


            for(int i=0;i<resds.length;i++){
                if (
                    ConstanteLocation.id_produit_caution.equalsIgnoreCase(resds[i].getIdproduit()) ||
                    ConstanteLocation.id_produit_transport_aller.equalsIgnoreCase(resds[i].getIdproduit()) ||
                    ConstanteLocation.id_produit_transport_retour.equalsIgnoreCase(resds[i].getIdproduit()) ||
                    ConstanteLocation.id_produit_transport_pers.equalsIgnoreCase(resds[i].getIdproduit())
                ) continue;
                CautionDetails detailc = new CautionDetails();
                detailc.setPct_applique(Double.parseDouble(conf.getValmin()));
                detailc.setIdreservationdetails(resds[i].getId());
                detailc.setMontantreservation(resds[i].getMontant());
                detailc.setDesignation(resds[i].getLibelleproduit());
                detailc.setIdingredient(resds[i].getIdproduit());
                detailc.setMontant(taux*resds[i].getMontantttc()/100);
                liste.add(detailc);
            }
            CautionDetails [] det = new CautionDetails[liste.size()];
            det = liste.toArray(det);
            if(det.length>0){
                result.setFille(det);
                return result;
            }
            throw new Exception("Pas de reservation pour "+this.getIdreservation());

        }
        catch(Exception e){
            throw e;
        }
        finally {
            if(estOuvert==true && c!=null) c.close();
        }
    }

    public ReservationLib getReservationAvecVerif(Connection c) throws Exception{
        ReservationLib result = null;
        boolean estOuvert = false;
        try {
            if(c==null)
            {
                c=new UtilDB().GetConn();
                estOuvert = true;
            }
            result = (ReservationLib) new ReservationLib().getById(this.getIdreservation(), "RESERVATION_LIB", c);
            ReservationVerifDetailsLib resd = new ReservationVerifDetailsLib();
            resd.setIdcaution(this.getId());
            resd.setNomTable("RETENUECAUTIONFILLE");
            ReservationVerifDetailsLib[] resds = (ReservationVerifDetailsLib[]) CGenUtil.rechercher(resd,null,null,c,"");
            result.setVerification(resds);
            if(resds.length>0){
                return result;
            }
            throw new Exception("Pas de retenue pour le caution"+this.getId());
        }
        catch(Exception e){
            throw e;
        }
        finally {
            if(estOuvert==true && c!=null) c.close();
        }
    }

    public double getMontantreservation() {
        return montantreservation;
    }

    public void setMontantreservation(double montantreservation) {
        this.montantreservation = montantreservation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdreservation() {
        return idreservation;
    }

    public void setIdreservation(String idreservation) {
        this.idreservation = idreservation;
    }

    public String getIdmodepaiement() {
        return idmodepaiement;
    }

    public void setIdmodepaiement(String idmodepaiement) {
        this.idmodepaiement = idmodepaiement;
    }

    public String getReferencepaiement() {
        return referencepaiement;
    }

    public void setReferencepaiement(String referencepaiement) {
        this.referencepaiement = referencepaiement;
    }

    public double getPct_applique() {
        return pct_applique;
    }

    public void setPct_applique(double pct_applique) {
        this.pct_applique = pct_applique;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

    public Date getDateprevuerestitution() {
        return dateprevuerestitution;
    }

    public void setDateprevuerestitution(Date dateprevuerestitution) {
        this.dateprevuerestitution = dateprevuerestitution;
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("CAU", "getSeqCaution");
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

    public Caution()throws Exception {
        super.setNomTable("caution");
        this.setNomClasseFille("caution.CautionDetails");
    }

    public String getLiaisonFille() {
        return "idcaution";
    }
    public String getNomClasseFille() {
        return "caution.CautionDetails";
    }

    public MvtCaisse getMvtCaisse() throws Exception {
        MvtCaisse mvtCaisse = new MvtCaisse();
        mvtCaisse.setIdOrigine(this.getId());
        MvtCaisse[] mvtCaisses = (MvtCaisse[]) CGenUtil.rechercher(mvtCaisse, null, null, "");
        return mvtCaisses[0];
    }

}