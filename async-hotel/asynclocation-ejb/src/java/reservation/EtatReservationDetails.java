package reservation;

import bean.CGenUtil;
import utilitaire.UtilDB;
import utilitaire.Utilitaire;
import utils.CalendarUtil;

import java.sql.Connection;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Vector;

public class EtatReservationDetails {
    String[] listeDate;
    HashMap<String, Vector> reservations;

    public EtatReservationDetails(String dtMin,String dtMax) throws Exception {
        Connection c=null;
        try {
            c = new UtilDB().GetConn();
            this.setListeDate(dtMin,dtMax);
            this.setReservations(dtMin,dtMax,c);
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            if(c!=null)c.close();
        }
    }

    public String[] getListeDate() {
        return listeDate;
    }

    public void setListeDate(String dtMin,String dtMax) {
//        if(Utilitaire.diffJourDaty(dtMax,dtMin)<0)throw new Exception("Date sup inferieur a date Inf");
        int day = Utilitaire.diffJourDaty(dtMax, dtMin);
        String liste[]=new String[day];
        for (int i = 0; i < day; i++) {
            liste[i]=Utilitaire.formatterDaty(Utilitaire.ajoutJourDate(dtMin,i))  ;
//            System.out.println(liste[i]);
        }
        this.listeDate = liste;
    }

    public HashMap<String, Vector> getReservations() {
        return reservations;
    }

    public void setReservations(String dMin,String dMax,Connection c) throws Exception {
        ReservationLib res = new ReservationLib();
        res.setNomTable("RESERVATIONCALENDRIER_MIN_DATY");
        if(dMin==null||dMin.compareToIgnoreCase("")==0)dMin=Utilitaire.formatterDaty(Utilitaire.getDebutSemaine(Utilitaire.dateDuJourSql())) ;
        String[] colInt={"daty"};
        String[] valInt={dMin,dMax};
        this.reservations= CGenUtil.rechercher2D(res,colInt,valInt,"daty",c,"");
    }

    public String getCodeCouleur(ReservationLib reservationLib){
        String codeCouleur="";
        if (reservationLib!=null){
            if (reservationLib.getEtatlogistique()==12){
                codeCouleur="background-color:var(--Warning-light);border-color: var(--Warning-dark);";
            }
            else if (reservationLib.getEtatlogistique()==13){
                codeCouleur="background:var(--Success-300);border-color: var(--Success-500);";
            }else if (reservationLib.getEtatlogistique()==14){
                codeCouleur="background-color:var(--Error-light);border-color: var(--Error-dark);";
            }
        }
        return codeCouleur;
    }

    public ReservationLib [] getByDate(String date) throws Exception {
        if (this.getReservations().get(date)!=null){
            return (ReservationLib[]) this.getReservations().get(date).toArray(new ReservationLib[]{});
        }
        return new ReservationLib[0];
    }

}
