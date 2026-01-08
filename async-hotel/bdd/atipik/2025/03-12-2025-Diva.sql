create or replace view V_ETATSTOCK_CATEG as
SELECT
    tp.ID AS id,
    tp.ID AS idTypeProduit,
    tp.DESCE AS idtypeproduitlib,
    ms.IDMAGASIN,
    mag.VAL AS idmagasinlib,
    TO_DATE('01-01-2001', 'DD-MM-YYYY') AS daty,
    SUM(CAST(ms.quantite AS NUMBER(30,
        2) )) AS QUANTITE,
    SUM(CAST(NVL( ms.entree, 0) AS NUMBER(30,
        2))) AS ENTREE,
    SUM(CAST(NVL( ms.sortie, 0) AS NUMBER(30,
        2))) AS SORTIE,
    SUM(CAST(NVL( ms.quantite, 0) AS NUMBER(30,
        2))) AS reste
FROM
    AS_INGREDIENTS p
        LEFT JOIN MONTANT_STOCK ms ON
        ms.IDPRODUIT = p.ID
        LEFT JOIN CATEGORIEINGREDIENT tp ON
        p.CATEGORIEINGREDIENT = tp.ID
        LEFT JOIN MAGASINPOINT mag ON
        ms.IDMAGASIN = mag.ID
WHERE
    NVL(ms.ENTREE, 0)>0
   OR NVL(ms.SORTIE, 0)>0
GROUP BY tp.ID, tp.DESCE, ms.IDMAGASIN, mag.VAL, TO_DATE('01-01-2001', 'DD-MM-YYYY');

INSERT INTO MENUDYNAMIQUE (ID, LIBELLE, ICONE, HREF, RANG, NIVEAU, ID_PERE) VALUES
    ('MNLIVRE','Détails par catégorie','fa fa-list','module.jsp?but=stock/etatstock/etatstock-categorie.jsp',3,3,'MNDN000000017');
