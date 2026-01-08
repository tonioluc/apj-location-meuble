package vente.dmdprix;

public class DmdPrixFilleLib extends DmdPrixFille{
    private String produitLib;

    private String idPersonneLib, image;

    public DmdPrixFilleLib() throws Exception {
        this.setNomTable("DMDPRIXFILLELIB");
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProduitLib() {
        return produitLib;
    }

    public void setProduitLib(String produitLib) {
        this.produitLib = produitLib;
    }

    public String getIdPersonneLib() {
        return idPersonneLib;
    }

    public void setIdPersonneLib(String idPersonneLib) {
        this.idPersonneLib = idPersonneLib;
    }
}
