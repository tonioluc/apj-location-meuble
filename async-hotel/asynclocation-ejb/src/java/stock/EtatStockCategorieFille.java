package stock;

import bean.ClassMAPTable;

import java.sql.Date;

public class EtatStockCategorieFille extends ClassMAPTable {
    protected String id;
    protected String idProduitLib;
    protected String idTypeProduit;
    protected String idTypeProduitLib;
    protected String idMagasin;
    protected String idMagasinLib;
    protected Date dateDernierInventaire,dateDernierMouvement, daty;
    protected double quantite;
    protected double entree;
    protected double sortie;
    protected double reste,montantReste,montantSortie,montantEntree;
    protected double puVente;
    protected String idUnite;
    protected String idUniteLib;
    protected String idPoint;
    protected String idPointLib;
    protected  double pu;
    protected String unite;
    protected String reference;
    private String image;

    public EtatStockCategorieFille() {
        this.setNomTable("V_ETATSTOCK_ING");
    }

    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProduitLib() {
        return idProduitLib;
    }

    public void setIdProduitLib(String idProduitLib) {
        this.idProduitLib = idProduitLib;
    }

    public String getIdTypeProduit() {
        return idTypeProduit;
    }

    public void setIdTypeProduit(String idTypeProduit) {
        this.idTypeProduit = idTypeProduit;
    }

    public String getIdTypeProduitLib() {
        return idTypeProduitLib;
    }

    public void setIdTypeProduitLib(String idTypeProduitLib) {
        this.idTypeProduitLib = idTypeProduitLib;
    }

    public String getIdMagasin() {
        return idMagasin;
    }

    public void setIdMagasin(String idMagasin) {
        this.idMagasin = idMagasin;
    }

    public String getIdMagasinLib() {
        return idMagasinLib;
    }

    public void setIdMagasinLib(String idMagasinLib) {
        this.idMagasinLib = idMagasinLib;
    }

    public Date getDateDernierInventaire() {
        return dateDernierInventaire;
    }

    public void setDateDernierInventaire(Date dateDernierInventaire) {
        this.dateDernierInventaire = dateDernierInventaire;
    }

    public Date getDateDernierMouvement() {
        return dateDernierMouvement;
    }

    public void setDateDernierMouvement(Date dateDernierMouvement) {
        this.dateDernierMouvement = dateDernierMouvement;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getEntree() {
        return entree;
    }

    public void setEntree(double entree) {
        this.entree = entree;
    }

    public double getSortie() {
        return sortie;
    }

    public void setSortie(double sortie) {
        this.sortie = sortie;
    }

    public double getReste() {
        return reste;
    }

    public void setReste(double reste) {
        this.reste = reste;
    }

    public double getMontantReste() {
        return montantReste;
    }

    public void setMontantReste(double montantReste) {
        this.montantReste = montantReste;
    }

    public double getMontantSortie() {
        return montantSortie;
    }

    public void setMontantSortie(double montantSortie) {
        this.montantSortie = montantSortie;
    }

    public double getMontantEntree() {
        return montantEntree;
    }

    public void setMontantEntree(double montantEntree) {
        this.montantEntree = montantEntree;
    }

    public double getPuVente() {
        return puVente;
    }

    public void setPuVente(double puVente) {
        this.puVente = puVente;
    }

    public String getIdUnite() {
        return idUnite;
    }

    public void setIdUnite(String idUnite) {
        this.idUnite = idUnite;
    }

    public String getIdUniteLib() {
        return idUniteLib;
    }

    public void setIdUniteLib(String idUniteLib) {
        this.idUniteLib = idUniteLib;
    }

    public String getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(String idPoint) {
        this.idPoint = idPoint;
    }

    public String getIdPointLib() {
        return idPointLib;
    }

    public void setIdPointLib(String idPointLib) {
        this.idPointLib = idPointLib;
    }

    public double getPu() {
        return pu;
    }

    public void setPu(double pu) {
        this.pu = pu;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
