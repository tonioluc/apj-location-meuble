create or replace view v_stock_par_magasin_col as
SELECT
    p.ID AS ID,
    p.LIBELLE AS idproduitLib,
    p.CATEGORIEINGREDIENT,
    tp.ID AS idTypeProduit,
    tp.DESCE AS idtypeproduitlib,
    TO_DATE('01-01-2001', 'DD-MM-YYYY') AS dateDernierMouvement,

    NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000122' THEN ms.quantite ELSE 0 END), 0) AS PNT000122,
    NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000124' THEN ms.quantite ELSE 0 END), 0) AS PNT000124,
    NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000103' THEN ms.quantite ELSE 0 END), 0) AS PNT000103,
    NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000084' THEN ms.quantite ELSE 0 END), 0) AS PNT000084,
    NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000086' THEN ms.quantite ELSE 0 END), 0) AS PNT000086,

    p.UNITE,
    u.DESCE AS idunitelib,
    CAST(NVL(p.PV, 0) AS NUMBER(30, 2)) AS PUVENTE,
    p.SEUILMIN,
    p.SEUILMAX,
    p.pu,
    p.reference,
    p.image

FROM AS_INGREDIENTS p

         LEFT JOIN (
    SELECT
        mf.IDPRODUIT,
        m.IDMAGASIN,
        SUM(NVL(mf.ENTREE, 0)) - SUM(NVL(mf.SORTIE, 0)) AS quantite
    FROM mvtStockFilleMontant mf
             JOIN MVTSTOCK m ON m.id = mf.IDMVTSTOCK
    WHERE m.ETAT >= 11
      AND mf.IDPRODUIT IS NOT NULL
      AND m.daty <= TO_DATE('03/11/2030', 'DD/MM/YYYY')
    GROUP BY mf.IDPRODUIT, m.IDMAGASIN
) ms ON ms.IDPRODUIT = p.ID

         LEFT JOIN CATEGORIEINGREDIENT tp ON p.CATEGORIEINGREDIENT = tp.ID
         LEFT JOIN AS_UNITE u ON p.UNITE = u.ID
GROUP BY
    p.ID,
    p.LIBELLE,
    p.CATEGORIEINGREDIENT,
    tp.ID,
    tp.DESCE,
    p.UNITE,
    u.DESCE,
    p.PV,
    p.SEUILMIN,
    p.SEUILMAX,
    p.pu,
    p.reference,
    p.image;


UPDATE MENUDYNAMIQUE SET LIBELLE = 'Détails', ICONE = 'fa fa-list', HREF = 'module.jsp?but=stock/etatstock/etatstock-liste-magasin.jsp', RANG = 1, NIVEAU = 3, ID_PERE = 'MNDN000000017' WHERE ID = 'MNDN0000000171';
