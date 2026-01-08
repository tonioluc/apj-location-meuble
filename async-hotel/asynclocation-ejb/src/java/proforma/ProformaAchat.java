package proforma;

import bean.CGenUtil;
import bean.ClassMere;
import faturefournisseur.As_BonDeCommande;

import java.sql.Connection;
import java.sql.Date;

public class ProformaAchat extends ClassMere {

    private String id;
    private Date daty;
    private String remarque;
    private String idDmdAchat;
    private String idFournisseur;

    public ProformaAchat() throws Exception {
        this.setNomTable("ProformaAchat");
        this.setLiaisonFille("idMere");
    }

    @Override
    public void setNomClasseFille(String nomClasseFill) throws Exception {
        super.setNomClasseFille("proforma.ProformaAchatDetail");
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("PFRMA", "get_seq_dmd_achat_proforma");
        this.setId(makePK(c));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public String getIdDmdAchat() {
        return idDmdAchat;
    }

    public void setIdDmdAchat(String idDmdAchat) {
        this.idDmdAchat = idDmdAchat;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    @Override
    public String getTuppleID() {
        return id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }
    
    public ProformaAchatDetail[] getFilleProformaAchat()throws Exception{
        ProformaAchatDetail profD = new ProformaAchatDetail();
        profD.setIdMere(this.getId());
        ProformaAchatDetail[] val = (ProformaAchatDetail[]) CGenUtil.rechercher(profD, null, null, "");
        return val;
    }

    public As_BonDeCommande genereBc()throws Exception{
        try {
            ProformaAchat proformaAchat = new ProformaAchat();
            proformaAchat.setId(this.getId());
            ProformaAchat[] resultats = (ProformaAchat[]) CGenUtil.rechercher(proformaAchat, null, null, "");
            if(resultats.length > 0) {
                proformaAchat = resultats[0];
                As_BonDeCommande bd = new As_BonDeCommande();
                if (proformaAchat.getIdFournisseur() != null) {
                    bd.setFournisseur(proformaAchat.getIdFournisseur());
                }
                bd.setIdProformaAchat(proformaAchat.getId());
                return bd;
            }else {
                throw new Exception("La proforma achat n'existe pas");
            }
        }catch(Exception e){
            throw e;
        }
    }
}
