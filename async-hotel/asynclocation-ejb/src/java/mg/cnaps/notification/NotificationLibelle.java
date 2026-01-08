package mg.cnaps.notification;

import java.sql.Date;

import bean.ClassMAPTable;

public class NotificationLibelle extends ClassMAPTable {

    String id;
    int receiver;
    String message;
    Date daty;
    String heure;
    int etat;
    String ref;
    String etatlib;
    String receiverlib;
    String lien;
    String ecartdate;
    String lien_html;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getEtatlib() {
        return etatlib;
    }

    public void setEtatlib(String etatlib) {
        this.etatlib = etatlib;
    }

    public String getReceiverlib() {
        return receiverlib;
    }

    public void setReceiverlib(String receiverlib) {
        this.receiverlib = receiverlib;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public String getEcartdate() {
        return ecartdate;
    }

    public void setEcartdate(String ecartdate) {
        this.ecartdate = ecartdate;
    }

    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    public NotificationLibelle() {
        this.setNomTable("notificationlibnonlu");
    }

    public String getLien_html() {
        return lien_html;
    }

    public void setLien_html(String lien_html) {
        this.lien_html = lien_html;
    }

}
