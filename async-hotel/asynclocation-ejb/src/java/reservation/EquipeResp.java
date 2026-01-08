package reservation;

import bean.ClassMAPTable;

import java.sql.Connection;
import java.sql.Date;

public class EquipeResp extends ClassMAPTable {
    private String id;
    private String equiperesp;
    private String description;
    private String idreservation;
    private Date daty;

    public EquipeResp(){
        this.setNomTable("equiperesp");
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("EQR", "GETSEQEQUIPERESP");
        this.setId(makePK(c));
    }

    @Override
    public ClassMAPTable createObject(String u, Connection c) throws Exception {
        ClassMAPTable obj = super.createObject(u, c);
        obj.setTuppleId(this.getIdreservation());
        return obj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquiperesp() {
        return equiperesp;
    }

    public void setEquiperesp(String equiperesp) {
        this.equiperesp = equiperesp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdreservation() {
        return idreservation;
    }

    public void setIdreservation(String idreservation) {
        this.idreservation = idreservation;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }
}
