package reservation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckInLib extends Check{
     
    String client,compteVente;
    String etatlib;
    String idclientlib, unite, image;
    double qteUtil;

    public double getQteUtil() {
        return qteUtil;
    }

    public void setQteUtil(double qteUtil) {
        this.qteUtil = qteUtil;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIdclientlib() {
        return idclientlib;
    }

    public void setIdclientlib(String idclientlib) {
        this.idclientlib = idclientlib;
    }

    public String getEtatlib() {
        return etatlib;
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

    public CheckInLib()
    {
        super.setNomTable("CHECKINLIBELLE");
    }



}
