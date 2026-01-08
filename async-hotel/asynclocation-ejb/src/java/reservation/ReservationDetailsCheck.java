package reservation;

import magasin.Magasin;

public class ReservationDetailsCheck extends ReservationDetails{

    private String idclient;
    private String produitlib;
    private String idclientlib;
    private String image;
    String magasin;
    double qteUtil;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getQteUtil() {
        return qteUtil;
    }

    public void setQteUtil(double qteUtil) {
        this.qteUtil = qteUtil;
    }

    public String getMagasin() {
        return magasin;
    }

    public void setMagasin(String magasin) {
        this.magasin = magasin;
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

    public String getProduitlib() {
        return produitlib;
    }

    public void setProduitlib(String produitlib) {
        this.produitlib = produitlib;
    }

    public ReservationDetailsCheck() throws Exception {
       setNomTable("RESERVATIONDETSANSCI");
    }

    public String getIdclient() {
        return idclient;
    }

    public void setIdclient(String idclient) {
        this.idclient = idclient;
    }

}
