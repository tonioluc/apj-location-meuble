CREATE OR REPLACE VIEW CA_ANNEE_VIEW AS
SELECT
    EXTRACT(YEAR  FROM daty)  AS annee,
    EXTRACT(MONTH FROM daty) AS mois,
    SUM(putotal)             AS montant
FROM vente_details_cpl_2_visee
GROUP BY
    EXTRACT(YEAR  FROM daty),
    EXTRACT(MONTH FROM daty);

CREATE OR REPLACE VIEW CA_ANNEE_MOIS AS
SELECT
    annee,
    SUM(CASE WHEN mois = 1 THEN montant ELSE 0 END) AS janvier,
    SUM(CASE WHEN mois = 2 THEN montant ELSE 0 END) AS fevrier,
    SUM(CASE WHEN mois = 3 THEN montant ELSE 0 END) AS mars,
    SUM(CASE WHEN mois = 4 THEN montant ELSE 0 END) AS avril,
    SUM(CASE WHEN mois = 5 THEN montant ELSE 0 END) AS mai,
    SUM(CASE WHEN mois = 6 THEN montant ELSE 0 END) AS juin,
    SUM(CASE WHEN mois = 7 THEN montant ELSE 0 END) AS juillet,
    SUM(CASE WHEN mois = 8 THEN montant ELSE 0 END) AS aout,
    SUM(CASE WHEN mois = 9 THEN montant ELSE 0 END) AS septembre,
    SUM(CASE WHEN mois = 10 THEN montant ELSE 0 END) AS octobre,
    SUM(CASE WHEN mois = 11 THEN montant ELSE 0 END) AS novembre,
    SUM(CASE WHEN mois = 12 THEN montant ELSE 0 END) AS decembre
FROM CA_ANNEE_VIEW
GROUP BY annee
ORDER BY annee;

CREATE OR REPLACE FORCE VIEW CA_MENSUEL AS
WITH agg AS (
    SELECT
        TRUNC(daty) AS jour,
        SUM(putotal) AS montant
    FROM vente_details_cpl_2_visee
    GROUP BY TRUNC(daty)
),
agg_mois AS (
    SELECT
        EXTRACT(YEAR FROM jour)  AS annee,
        EXTRACT(MONTH FROM jour) AS mois,
        INITCAP(TO_CHAR(jour, 'Month', 'NLS_DATE_LANGUAGE=FRENCH')) AS moislib,
        SUM(montant) AS montant
    FROM agg
    GROUP BY
        EXTRACT(YEAR FROM jour),
        EXTRACT(MONTH FROM jour),
        INITCAP(TO_CHAR(jour, 'Month', 'NLS_DATE_LANGUAGE=FRENCH'))
)
SELECT annee, mois, moislib, montant
FROM agg_mois;

INSERT INTO MENUDYNAMIQUE
(ID, LIBELLE, ICONE, HREF, RANG, NIVEAU, ID_PERE)
VALUES('MNDN000000111', 'Chiffre d''affaire Mensuel', 'fa fa-bar-chart', 'module.jsp?but=vente/analyse/graphe-ca-mensuel.jsp', 2, 3, 'MNDN000000010');
INSERT INTO MENUDYNAMIQUE
(ID, LIBELLE, ICONE, HREF, RANG, NIVEAU, ID_PERE)
VALUES('MNDN000000112', 'Chiffre d''affaire Annuel', 'fa fa-line-chart', 'module.jsp?but=vente/analyse/graphe-ca-annuel.jsp', 3, 3, 'MNDN000000010');