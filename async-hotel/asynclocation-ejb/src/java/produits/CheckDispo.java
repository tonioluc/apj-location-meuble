package produits;

import bean.ClassMAPTable;

import java.sql.Date;

public class CheckDispo extends ClassMAPTable {
    private String idproduit;
    private Date daty;
    private int qtenondispo;
    private int qtehananana;
    private int qtedispo;

    public CheckDispo() {
        this.setNomTable("checkdisponibilite");
    }

    @Override
    public String getTuppleID() {
        return idproduit;
    }

    @Override
    public String getAttributIDName() {
        return "idproduit";
    }

    public String getIdproduit() {
        return idproduit;
    }

    public void setIdproduit(String idproduit) {
        this.idproduit = idproduit;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

    public int getQtenondispo() {
        return qtenondispo;
    }

    public void setQtenondispo(int qtenondispo) {
        this.qtenondispo = qtenondispo;
    }

    public int getQtehananana() {
        return qtehananana;
    }

    public void setQtehananana(int qtehananana) {
        this.qtehananana = qtehananana;
    }

    public int getQtedispo() {
        return qtedispo;
    }

    public void setQtedispo(int qtedispo) {
        this.qtedispo = qtedispo;
    }
}
