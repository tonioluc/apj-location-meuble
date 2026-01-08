package proforma;

public class ProformaAchatLib extends ProformaAchat {

    private String idFournisseurlib;

    public String getIdFournisseurlib() {
        return idFournisseurlib;
    }

    public void setIdFournisseurlib(String idFournisseurlib) {
        this.idFournisseurlib = idFournisseurlib;
    }

    public ProformaAchatLib() throws Exception {
        this.setNomTable("PROFORMAACHATLIB");
    }

}