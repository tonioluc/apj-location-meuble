package vente;

import bean.CGenUtil;
import bean.ClassMAPTable;
import bean.ResultatEtSomme;
import utilitaire.Utilitaire;
import utils.ConstanteLocation;

import java.sql.Connection;
import java.sql.Date;

public class VenteDetailAnalyseCl extends ClassMAPTable {
    String idClient,idClientLib;
    double nbarticle, ca;
    Date daty;

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getIdClientLib() {
        return idClientLib;
    }

    public void setIdClientLib(String idClientLib) {
        this.idClientLib = idClientLib;
    }

    public double getNbarticle() {
        return nbarticle;
    }

    public void setNbarticle(double nbarticle) {
        this.nbarticle = nbarticle;
    }

    public double getCa() {
        return ca;
    }

    public void setCa(double ca) {
        this.ca = ca;
    }

    public Date getDaty() {
        return daty;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }

    public VenteDetailAnalyseCl() {
        this.setNomTable("VENTE_DETAIL_ANALYSECL");
    }

    @Override
    public String getTuppleID() {
        return this.getDaty().toString();
    }

    @Override
    public String getAttributIDName() {
        return "daty";
    }

    public String generateQueryCore(Date datemin, Date datemax ) {
        return "select\n" +
                "    v.DATY,v.IDCLIENT,cl.NOM as idclientlib, count(distinct vd.IDPRODUIT) as nbarticle, sum(vd.QTE*vd.pu*vd.NOMBRE*nvl(i.DURRE,1)) as ca\n" +
                "FROM VENTE_DETAILS vd\n" +
                "         LEFT JOIN VENTE v ON v.ID = vd.IDVENTE\n" +
                "         LEFT JOIN AS_INGREDIENTS i ON i.ID = vd.IDPRODUIT LEFT JOIN CLIENT cl on cl.id=v.IDCLIENT\n" +
                "WHERE v.ETAT >= 11 AND vd.IDPRODUIT!='"+ ConstanteLocation.id_produit_caution +"' AND vd.IDPRODUIT!='"+ ConstanteLocation.id_produit_transport_aller +"' AND vd.IDPRODUIT!='"+ ConstanteLocation.id_produit_transport_retour +"' AND vd.IDPRODUIT!='"+ ConstanteLocation.id_produit_transport_pers +"' AND v.DATY >= DATE '"+datemin+"' AND v.DATY <=  DATE '"+datemax+"'\n" +
                "group by v.DATY,v.IDCLIENT,cl.NOM";
    }

    public ResultatEtSomme rechercherPage(String[] colInt, String[]valInt, int numPage, String apresWhere, String[]nomColSomme, Connection c, int npp) throws Exception {
        String daty= Utilitaire.dateDuJour();
        String daty2=Utilitaire.dateDuJour();
        if(valInt!=null&&valInt.length>1) {
            daty=valInt[0].toString();
            daty2=valInt[1].toString();
        }
        Date datemin = Utilitaire.stringDate(daty);
        Date datemax = Utilitaire.stringDate(daty2);
        String query = this.generateQueryCore(datemin, datemax);
        return CGenUtil.rechercherPage(this,query,numPage,nomColSomme,apresWhere,c,npp);
    }
}
