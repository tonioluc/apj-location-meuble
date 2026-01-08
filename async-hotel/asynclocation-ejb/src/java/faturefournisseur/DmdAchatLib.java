package faturefournisseur;

public class DmdAchatLib extends DmdAchat{

    String fournisseurLib;
    String etatlib;

    public String getEtatlib() {
        return etatlib;
    }

    public void setEtatlib(String etatlib) {
        this.etatlib = etatlib;
    }

    public String getFournisseurLib() {
        return fournisseurLib;
    }

    public void setFournisseurLib(String fournisseurLib) {
        this.fournisseurLib = fournisseurLib;
    }

    public DmdAchatLib() throws Exception {
        this.setNomTable("DMDACHATLIB");
    }
}
