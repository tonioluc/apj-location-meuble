package caution;



import bean.ClassMere;

import java.sql.Connection;
import java.sql.Date;

public class ReservationVerification extends ClassMere {

    private String id,idreservation,observation;
    private Date daty;

    public ReservationVerification() {
        this.setNomTable("reservation_verification");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdreservation() {
        return idreservation;
    }

    public void setIdreservation(String idreservation) {
        this.idreservation = idreservation;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

    @Override
    public String getTuppleID() {
        return this.getId();
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }
    ///getSeqReservationVerification

    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("RV", "getSeqReservationVerification");
        this.setId(makePK(c));
    }

    public String getLiaisonFille() {
        return "idreservationverif";
    }
    public String getNomClasseFille() {
        return "caution.ReservationVerifDetails";
    }

}

