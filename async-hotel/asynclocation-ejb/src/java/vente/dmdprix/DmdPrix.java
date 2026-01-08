package vente.dmdprix;

import bean.CGenUtil;
import bean.ClassFille;
import bean.ClassMere;
import produits.Ingredients;
import proforma.Proforma;
import proforma.ProformaDetails;
import utilitaire.UtilDB;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DmdPrix extends ClassMere {

    private String id;

    private Date daty;

    private String client;

    private DmdPrixFille[] dmdPrixFilles;

    public DmdPrix()throws Exception{
        this.setNomTable("DMDPRIX");
        setLiaisonFille("idMere");
        setNomClasseFille("vente.dmdprix.DmdPrixFille");
    }

    @Override
    public void construirePK(Connection c) throws Exception {
        this.preparePk("DMDP", "getseqDmdPrix");
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

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Override
    public String getTuppleID() {
        return this.id;
    }

    @Override
    public String getAttributIDName() {
        return "id";
    }

    public DmdPrixFille[] getDmdPrixFilles() {
        return dmdPrixFilles;
    }

    public void setDmdPrixFilles(DmdPrixFille[] dmdPrixFilles) {
        this.dmdPrixFilles = dmdPrixFilles;
    }

//    public String bonDeCommandeLiee(String idDmd)throws Exception{
//        BonDeCommande bonDeCommande = new BonDeCommande();
//        bonDeCommande.setNomTable("BONDECOMMANDE_CLIENT");
//        bonDeCommande.setIdDmdPrix(idDmd);
//
//        BonDeCommande[] bonDeCommandes = (BonDeCommande[]) CGenUtil.rechercher(bonDeCommande, null, null, "");
//        if(bonDeCommandes.length==0){
//            return null;
//        }
//        return  bonDeCommandes[0].getId();
//    }

    public Proforma genererProforma(String id, Connection c) throws Exception {
        boolean estOuvert = false;

        try {
            if (c == null) {
                estOuvert = true;
                c = (new UtilDB()).GetConn();
            }

            DmdPrix demandePrix = new DmdPrix();
            demandePrix = (DmdPrix)demandePrix.getById(id, "DmdPrix", c);
            if (demandePrix == null) {
                throw new Exception("Demande de prix non trouvÃ©e pour l'ID: " + id);
            }

            DmdPrixFilleLib demandePrixFille = new DmdPrixFilleLib();
            demandePrixFille.setNomTable("DMDPRIXFILLELIB");
            demandePrixFille.setIdMere(demandePrix.getId());
            DmdPrixFilleLib[] demandePrixFilleLibs = (DmdPrixFilleLib[])CGenUtil.rechercher(demandePrixFille, (String[])null, (String[])null, c, "");
            Proforma proforma = new Proforma();
            proforma.setDaty(demandePrix.getDaty());
            proforma.setIdClient(demandePrix.getClient());
            proforma.setDesignation("Proforma pour la demande de prix " + demandePrix.getId());
            proforma.setRemarque("Proforma de la demande de prix " + demandePrix.getId());
            proforma.setIdOrigine(demandePrix.getId());
            if (demandePrixFilleLibs != null) {
                List<ProformaDetails> details = new ArrayList();

                for(int i = 0; i < demandePrixFilleLibs.length; ++i) {
                    Ingredients produit = demandePrixFilleLibs[i].getProduit(c);
                    if (produit != null && produit.getCategorieIngredient().equals("CATING0029")) {
                        ProformaDetails proformaDetails = new ProformaDetails();
                        proformaDetails.setIdProduit(demandePrixFilleLibs[i].getProduit());
                        proformaDetails.setQte(1);
                        proformaDetails.setUnite(produit.getUnite());
                        proformaDetails.setIdDemandePrixFille(demandePrixFilleLibs[i].getId());
                        proformaDetails.setDesignation(demandePrixFilleLibs[i].getProduitLib());
                        proformaDetails.setPu(produit.getPu());
                        details.add(proformaDetails);
                    }
                }

                proforma.setFille((ClassFille[])details.toArray(new ProformaDetails[0]));
            }

            Proforma var18 = proforma;
            return var18;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null && estOuvert) {
                c.close();
            }

        }

        return null;
    }

    public ProformaDetails[] genererProformaDetails(String idDmd, Connection c) throws Exception {
        DmdPrix dm = (DmdPrix)(new DmdPrix()).getById(idDmd, "DMDPRIX", c);
        DmdPrixFille[] fille = (DmdPrixFille[])dm.getFille("DMDPRIXFILLE", c, "");
        List<ProformaDetails> det = new ArrayList();

        for(DmdPrixFille demandePrixFille : fille) {
            Ingredients produit = demandePrixFille.getProduit(c);
            if (demandePrixFille.getProduit() != null) {
                ProformaDetails proformaDetails = new ProformaDetails();
                proformaDetails.setIdProduit(demandePrixFille.getProduit());
                proformaDetails.setDesignation(demandePrixFille.getDesce());
                proformaDetails.setQte(1);
                proformaDetails.setUnite(produit.getUnite());
                proformaDetails.setIdDemandePrixFille(demandePrixFille.getId());
                proformaDetails.setDesignation(produit.getLibelle());
                proformaDetails.setPu(produit.getPu());
                det.add(proformaDetails);
            }
        }

        return (ProformaDetails[])det.toArray(new ProformaDetails[0]);
    }
}
