package stock;

import bean.CGenUtil;
import bean.ResultatEtSomme;
import utilitaire.Utilitaire;

import java.sql.Connection;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Angela
 */
public class EtatStockModele extends EtatStock {

    public ResultatEtSomme rechercherPage(String[] colInt, String[]valInt, int numPage, String apresWhere, String[]nomColSomme, Connection c, int npp) throws Exception {
        String daty=Utilitaire.dateDuJour();
        if(valInt!=null&&valInt.length>1) {daty=valInt[1].toString();}

        String req= "SELECT\n" +
                "    asing.IDMODELE as id,\n" +
                "    asing2.LIBELLE as idProduitLib,\n" +
                "    p.CATEGORIEINGREDIENT,\n" +
                "    tp.ID AS idTypeProduit,\n" +
                "    tp.DESCE AS idtypeproduitlib,\n" +
                "    ms.IDMAGASIN,\n" +
                "    mag.DESCE AS idmagasinlib,\n" +
                "    TO_DATE('01-01-2001', 'DD-MM-YYYY') AS dateDernierMouvement,\n" +
                "    sum(ms.quantite) AS QUANTITE,\n" +
                "    sum(ms.entree) AS ENTREE,\n" +
                "    sum(ms.sortie) AS SORTIE,\n" +
                "    sum(ms.quantite) AS reste,\n" +
                "    p.UNITE,\n" +
                "    u.DESCE AS idunitelib,\n" +
                "    mag.IDPOINT,\n" +
                "    mag.IDTYPEMAGASIN,\n" +
                "    sum(ms.montantEntree) as montantEntree,\n" +
                "    sum(ms.montantSortie) as montantSortie,\n" +
                "    sum(ms.montant) as montantReste\n" +
                "FROM AS_INGREDIENTS p\n" +
                "         LEFT JOIN (SELECT\n" +
                "                        mf.IDPRODUIT,\n" +
                "                        SUM(NVL(mf.ENTREE,0)) AS ENTREE,\n" +
                "                        SUM(NVL(mf.SORTIE,0)) AS SORTIE,\n" +
                "                        SUM(NVL(mf.ENTREE,0)) - SUM(NVL(mf.SORTIE,0)) AS quantite,\n" +
                "                        cast(sum(mf.montantEntree) as number(30,2))  AS montantEntree,\n" +
                "                        cast(sum(mf.montantSortie) as number(30,2))  AS montantSortie,\n" +
                "                        CAST(NVL(ai.PU, 0) * (SUM(NVL(mf.ENTREE,0)) - SUM(NVL(mf.SORTIE,0))) AS NUMBER(30,2)) AS montant,\n" +
                "                        m.IDMAGASIN\n" +
                "                    FROM\n" +
                "                        mvtStockFilleMontant mf\n" +
                "                            JOIN MVTSTOCK m ON m.id = mf.IDMVTSTOCK\n" +
                "                            JOIN AS_INGREDIENTS ai ON ai.ID = mf.IDPRODUIT\n" +
                "                    WHERE\n" +
                "                        m.ETAT >= 11\n" +
                "                      AND mf.IDPRODUIT IS NOT NULL\n" +
                "                      AND m.daty<='"+daty+"'\n" +
                "                    GROUP BY\n" +
                "                        mf.IDPRODUIT,\n" +
                "                        ai.PU,m.IDMAGASIN) ms ON ms.IDPRODUIT = p.ID\n" +
                "         LEFT JOIN CATEGORIEINGREDIENT tp ON p.CATEGORIEINGREDIENT = tp.ID\n" +
                "         LEFT JOIN magasin mag ON ms.IDMAGASIN = mag.ID\n" +
                "         LEFT JOIN AS_UNITE u ON p.UNITE = u.ID\n" +
                "         left join AS_INGREDIENTS asing on p.id = asing.id\n" +
                "         left join AS_INGREDIENTS asing2 on asing.IDMODELE=asing2.id\n" +
                "where NVL(ms.ENTREE, 0)>0 or NVL(ms.SORTIE, 0)>0\n" +
                "Group by\n" +
                "    asing.IDMODELE,\n" +
                "    asing2.LIBELLE,\n" +
                "    p.CATEGORIEINGREDIENT,\n" +
                "    tp.ID,\n" +
                "    tp.DESCE,\n" +
                "    ms.IDMAGASIN,\n" +
                "    mag.DESCE,\n" +
                "    TO_DATE('01-01-2001', 'DD-MM-YYYY'),\n" +
                "    p.UNITE,\n" +
                "    u.DESCE,\n" +
                "    mag.IDPOINT,\n" +
                "    mag.IDTYPEMAGASIN\n";
        System.err.println(req);
        ResultatEtSomme rs= CGenUtil.rechercherPage(this,req,numPage,nomColSomme,apresWhere,c,npp);
        return rs;
    }

}
