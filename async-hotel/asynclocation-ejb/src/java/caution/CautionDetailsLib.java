package caution;

public class CautionDetailsLib extends CautionDetails{
    String idcautionlib, idreservationdetailslib,idingredientlib,produit;

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public String getIdcautionlib() {
        return idcautionlib;
    }

    public void setIdcautionlib(String idcautionlib) {
        this.idcautionlib = idcautionlib;
    }

    public String getIdreservationdetailslib() {
        return idreservationdetailslib;
    }

    public void setIdreservationdetailslib(String idreservationdetailslib) {
        this.idreservationdetailslib = idreservationdetailslib;
    }

    public String getIdingredientlib() {
        return idingredientlib;
    }

    public void setIdingredientlib(String idingredientlib) {
        this.idingredientlib = idingredientlib;
    }

    public CautionDetailsLib() {
        setNomTable("CautionDetailsLib");
    }
}
