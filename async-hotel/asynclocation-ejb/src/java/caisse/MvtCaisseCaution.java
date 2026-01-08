package caisse;

import bean.ClassMAPTable;
import caution.Caution;
import caution.CautionLib;
import caution.ReservationVerifDetailsLib;
import reservation.ReservationLib;
import utils.ConstanteLocation;

import java.sql.Connection;

public class MvtCaisseCaution extends MvtCaisse{
    String type_mvt;

    public MvtCaisseCaution() {
        this.setNomTable("MOUVEMENTCAISSE");
    }

    public String getType_mvt() {
        return type_mvt;
    }

    public void setType_mvt(String type_mvt) {
        this.type_mvt = type_mvt;
    }

    @Override
    public MvtCaisseCaution clone() {
        try {
            MvtCaisseCaution copy = new MvtCaisseCaution();
            copy.setId(this.getId());
            copy.setIdOrigine(this.getIdOrigine());
            copy.setDesignation(this.getDesignation());
            copy.setType_mvt(this.getType_mvt());
            copy.setDebit(this.getDebit());
            copy.setCredit(this.getCredit());
            copy.setIdDevise(this.getIdDevise());
            copy.setIdTiers(this.getIdTiers());
            copy.setDaty(this.getDaty());
            copy.setIdCaisse(this.getIdCaisse());
            copy.setCompte(this.getCompte());
            return copy;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



    @Override
    public ClassMAPTable createObject(String u, Connection c) throws Exception {
        return super.createObject(u,c);
    }
}
