ALTER TABLE RESERVATION ADD (numBL VARCHAR(100));
ALTER TABLE CHECKIN ADD (numBL VARCHAR(100));

CREATE OR REPLACE VIEW CHECKINLIBELLE_PDF AS
SELECT
    c.ID,
    c.RESERVATION,
    nvl(r2.ID,c.reservation) AS idReservationMere,
    c.DATY,
    c.HEURE,
    c.REMARQUE,
    cl.nom AS CLIENT,
    c.IDPRODUIT,
    c.ETAT,
    c.IDCLIENT,
    i.LIBELLE AS produitLibelle,
    i.pu,
    i.tva,
    CASE
        WHEN c.ETAT = 0
            THEN 'ANNULEE'
        WHEN c.ETAT = 1
            THEN 'CREE'
        WHEN c.ETAT = 11
            THEN 'VISEE'
        END AS ETATLIB,
    c.qte,
    c.responsable,
    i.reference as refproduit,
    i.unite,
    c.numBL
FROM
    checkIn c
        LEFT JOIN RESERVATIONDETAILS r ON r.ID = c.RESERVATION
        LEFT JOIN RESERVATION r2 ON r.IDMERE = r2.id
        LEFT JOIN AS_INGREDIENTS_LIB i ON c.IDPRODUIT = i.id
        LEFT JOIN CLIENT cl ON c.IDCLIENT=cl.id
        left join AS_UNITE u on i.UNITE=u.ID;

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
    TO_CHAR(r.daty + s.nbJour, 'DD/MM/YYYY') AS datePrevisionRetour,
    er.equiperesp,
    er.description as desceequiperesp,
    r.numBl
FROM
    RESERVATION r
        LEFT JOIN CLIENT c ON c.id = r.idClient
        LEFT JOIN RESERVATIONMONTANT2 rm ON rm.id = r.id
        LEFT JOIN MOUVEMENTCAISSEGROUPERESA mvt ON mvt.IDORIGINE = r.ID
        LEFT JOIN magasin m ON m.id = r.IDMAGASIN
        LEFT JOIN sommeNombreJourReservation s ON r.id = s.idMere
        LEFT JOIN (
        SELECT e.*
        FROM equiperesp e
                 JOIN (
            SELECT idreservation, MAX(daty) AS max_daty
            FROM equiperesp
            GROUP BY idreservation
        ) last_e ON e.idreservation = last_e.idreservation
            AND e.daty = last_e.max_daty
    ) er ON er.idreservation = r.id;