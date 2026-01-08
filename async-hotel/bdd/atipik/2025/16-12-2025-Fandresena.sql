CREATE OR REPLACE VIEW VENTE_DETAIL_ANALYSE AS
SELECT
    a.id,
    i.reference,
    a.idproduitlib,
    a.duree,
    a.nbclient,
    a.ca,
    SYSDATE AS datejour
FROM (
         SELECT
             vd.IDPRODUIT AS id,
             i.LIBELLE AS idproduitlib,
             SUM(vd.QTE) AS duree,
             COUNT(DISTINCT v.IDCLIENT) AS nbclient,
             SUM(vd.QTE * vd.pu * vd.NOMBRE * NVL(i.DURRE, 1)) AS ca
         FROM VENTE_DETAILS vd
                  JOIN VENTE v ON v.ID = vd.IDVENTE
                  JOIN AS_INGREDIENTS i ON i.ID = vd.IDPRODUIT
         WHERE v.ETAT >= 11
           AND vd.IDPRODUIT NOT IN ('INGCAUT001','INGCAUT002','INGCAUT004', 'INGCAUT003')
           AND v.DATY BETWEEN DATE '2020-12-15' AND DATE '2025-12-15'
         GROUP BY
             vd.IDPRODUIT,
             i.LIBELLE
     ) a
         LEFT JOIN AS_INGREDIENTS i ON i.ID = a.id;

