package proforma;

public class ProformaDetailsLib extends ProformaDetails{
    private String idProformaLib,idProduitLib;
    private double puTotal,remisemontant,montanttotal, montanttva, montantttc;
    private String uniteLib,reference, dimension;

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    private String idDevis, image;
    public ProformaDetailsLib()throws Exception {
        this.setNomTable("PROFORMADETAILS_CPL");
    }

    public double getMontantttc() {
        return montantttc;
    }

    public void setMontantttc(double montantttc) {
        this.montantttc = montantttc;
    }

    public double getMontanttva() {
        return montanttva;
    }

    public void setMontanttva(double montanttva) {
        this.montanttva = montanttva;
    }

    public double getMontanttotal() {
        return montanttotal;
    }

    public void setMontanttotal(double montanttotal) {
        this.montanttotal = montanttotal;
    }

    public double getRemisemontant() {
        return remisemontant;
    }

    public void setRemisemontant(double remisemontant) {
        this.remisemontant = remisemontant;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUniteLib() {
        return uniteLib;
    }

    public void setUniteLib(String uniteLib) {
        this.uniteLib = uniteLib;
    }

    public String getIdProformaLib() {
        return idProformaLib;
    }

    public void setIdProformaLib(String idProformaLib) {
        this.idProformaLib = idProformaLib;
    }

    public String getIdProduitLib() {
        return idProduitLib;
    }

    public void setIdProduitLib(String idProduitLib) {
        this.idProduitLib = idProduitLib;
    }

    public double getPuTotal() {
        return puTotal;
    }

    public void setPuTotal(double puTotal) {
        this.puTotal = puTotal;
    }

    public String getIdDevis() {
        return idDevis;
    }

    public void setIdDevis(String idDevis) {
        this.idDevis = idDevis;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
    
}
