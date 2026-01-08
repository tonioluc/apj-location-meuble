create or replace view V_STOCK_PAR_MAGASIN_COL as
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
    NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000085' THEN ms.quantite ELSE 0 END), 0) AS PNT000085,
    (
        CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000122' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) +
        CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000124' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) +
        CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000103' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) +
        CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000084' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) +
        CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000085' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2)) +
        CAST(NVL(SUM(CASE WHEN ms.IDMAGASIN = 'PNT000086' THEN ms.quantite ELSE 0 END), 0) AS NUMBER(30,2))
        ) AS TOTAL,
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
    p.image
        /



create or replace view CA_PAR_MOIS as
SELECT
    '' AS id,
    INITCAP(TO_CHAR(DATY, 'Month YYYY', 'NLS_DATE_LANGUAGE=FRENCH')) AS MOIS,
    TRUNC(DATY, 'MM') AS DEBUT,
    EXTRACT(YEAR FROM DATY) AS ANNEE,
    EXTRACT(MONTH FROM DATY) AS moisInt,
    SUM(MONTANTTOTAL) AS CA
FROM
    VENTE_CPL
WHERE
    ETAT >= 11
GROUP BY
    TO_CHAR(DATY, 'Month YYYY', 'NLS_DATE_LANGUAGE=FRENCH'),
    TRUNC(DATY, 'MM'),
    EXTRACT(YEAR FROM DATY),
    EXTRACT(MONTH FROM DATY)
ORDER BY
    DEBUT
        /


        INSERT INTO menudynamique (id, libelle, icone, href, rang, niveau, id_pere) VALUES ('MENDYN176277627248485', 'Statistique', 'fa fa-chart-pie', 'module.jsp?but=vente/analyse/analyse.jsp', 4, 2, 'MENDYN1762162582299338');

-- script pour l'usermenu

INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UME77CDB13', 'MENDYN1762163047592789', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMA26D4078', 'MENDYN1762163047592789', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM11084030', 'MENDYN1762163047592789', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM8045F8F9', 'MENDYN1762163047592789', NULL, 'achat', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UME7C62B25', 'MENDYN1762163047592789', NULL, 'vente', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMA7511B68', 'MENDYN1762163047592789', NULL, 'pompiste', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM3A9A9772', 'MENDYN1762163146592896', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMAFD35FC3', 'MENDYN1762163146592896', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM7FCD57E8', 'MENDYN1762163146592896', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM262164CA', 'MENDYN1762163146592896', NULL, 'achat', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM6A011B0B', 'MENDYN1762163146592896', NULL, 'vente', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM543324EE', 'MENDYN1762163146592896', NULL, 'pompiste', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM5C3031D5', 'MENDYN1762163186640835', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM3F139285', 'MENDYN1762163186640835', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMB96D4280', 'MENDYN1762163186640835', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM5402D493', 'MENDYN1762163186640835', NULL, 'achat', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMA19942C3', 'MENDYN1762163186640835', NULL, 'vente', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM82FF455D', 'MENDYN1762163186640835', NULL, 'pompiste', 1);

