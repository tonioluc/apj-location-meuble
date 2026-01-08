package reservation;

public class CheckOutLib extends CheckOut
{
    String produitlibelle, etatlib, idreservation,val, image;

    public String getProduitlibelle() {
        return produitlibelle;
    }

    public void setProduitlibelle(String produitlibelle) {
        this.produitlibelle = produitlibelle;
    }

    public String getEtatlib() {
        return etatlib;
    }

    public void setEtatlib(String etatlib) {
        this.etatlib = etatlib;
    }

    public String getIdreservation() {
        return idreservation;
    }

    public void setIdreservation(String idreservation) {
        this.idreservation = idreservation;
    }
    
    public CheckOutLib() {
        setNomTable("CheckOutLib");
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
