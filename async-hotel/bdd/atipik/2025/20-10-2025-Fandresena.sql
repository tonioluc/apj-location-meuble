CREATE OR REPLACE VIEW RESERVATION_LIB_MIN_DATY AS
SELECT
    r.id,
    r.idClient,
    c.NOM AS idclientlib,
    NVL(
            (SELECT MIN(d2.daty)
             FROM reservationdetails d2
             WHERE d2.idmere = r.id),
            r.daty
    ) AS daty,
    TO_CHAR(
            NVL(
                    (SELECT MIN(d2.daty)
                     FROM reservationdetails d2
                     WHERE d2.idmere = r.id),
                    r.daty
            ) - 1,
            'DD/MM/YYYY'
    ) AS datePrevisionDepart,
    r.remarque,
    r.etat,
    CASE
        WHEN r.ETAT = 0 THEN 'ANNULE(E)'
        WHEN r.ETAT = 1 THEN 'CREE(E)'
        WHEN r.ETAT = 11 THEN 'VISE(E)'
        END AS etatlib,
    rm.montant,
    rm.montantremise,
    rm.montanttotal,
    rm.MONTANTTVA,
    rm.MONTANTTTC,
    NVL(mvt.CREDIT, 0) AS paye,
    CAST(rm.MONTANTTTC - NVL(mvt.CREDIT, 0) AS NUMBER(20, 2)) AS resteAPayer,
    0 AS revient,
    0 AS marge,
    r.IDORIGINE,
    r.LIEULOCATION,
    m.val AS magasin,
    TO_CHAR(r.daty + s.nbJour, 'DD/MM/YYYY') AS datePrevisionRetour
FROM
    RESERVATION r
        LEFT JOIN CLIENT c ON c.id = r.idClient
        LEFT JOIN RESERVATIONMONTANT2 rm ON rm.id = r.id
        LEFT JOIN MOUVEMENTCAISSEGROUPERESA mvt ON mvt.IDORIGINE = r.ID
        LEFT JOIN magasin m ON m.id = r.IDMAGASIN
        LEFT JOIN sommeNombreJourReservation s ON r.id = s.idMere;


CREATE OR REPLACE VIEW VENTE_CPL AS
SELECT
    v.ID,
    v.DESIGNATION,
    v.IDMAGASIN,
    m.VAL AS IDMAGASINLIB,
    v.DATY,
    v.REMARQUE,
    v.ETAT,
    CASE
        WHEN v.ETAT = 1 THEN 'CREE'
        WHEN v.ETAT = 11 THEN 'VISEE'
        WHEN v.ETAT = 0 THEN 'ANNULEE'
        END AS ETATLIB,
    v2.MONTANTTOTAL,
    v2.IDDEVISE,
    v.IDCLIENT,
    c.NOM AS IDCLIENTLIB,
    CAST(v2.MONTANTTVA AS NUMBER(30,2)) AS MONTANTTVA,
    CAST(v2.MONTANTTTC AS NUMBER(30,2)) AS MONTANTTTC,
    CAST(v2.MONTANTTTCAR AS NUMBER(30,2)) AS MONTANTTTCAR,
    CAST(NVL(mv.CREDIT,0) - NVL(ACG.MONTANTPAYE, 0) AS NUMBER(30,2)) AS montantpaye,
    CAST(v2.MONTANTTTC - NVL(mv.CREDIT,0) - NVL(ACG.resteapayer_avr, 0) AS NUMBER(30,2)) AS montantreste,
    NVL(ACG.MONTANTTTC_avr, 0) AS avoir,
    v2.tauxDeChange AS tauxDeChange,
    v2.MONTANTREVIENT,
    CAST((v2.MONTANTTTCAR - v2.MONTANTREVIENT) AS NUMBER(20,2)) AS margeBrute,
    v.IDRESERVATION,
    v.DATYPREVU,
    p.PERIODE AS PERIODE
FROM VENTE v
         LEFT JOIN CLIENT c ON c.ID = v.IDCLIENT
         LEFT JOIN MAGASIN m ON m.ID = v.IDMAGASIN
         JOIN VENTEMONTANT v2 ON v2.ID = v.ID
         LEFT JOIN MOUVEMENTCAISSEGROUPEFACTURE mv ON v.ID = mv.IDORIGINE
         LEFT JOIN AVOIRFCLIB_CPL_GRP ACG ON ACG.IDVENTE = v.ID
         LEFT JOIN RESERVATION r ON r.ID = v.IDORIGINE
         LEFT JOIN PROFORMA_CPL p ON p.ID = r.IDORIGINE;

DELETE FROM usermenu WHERE id = 'USRMEN09'; -- 1 rows
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMA0DAA6B8', 'MNDN000000003', NULL, 'caisse', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM5D36B46B', 'MNDN000000003', NULL, 'dg', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UME002E07D', 'MNDN000000003', NULL, 'gestionnaire', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM5EB90965', 'MNDN000000003', NULL, 'achat', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMAD205D07', 'MNDN000000003', NULL, 'pompiste', 0);
