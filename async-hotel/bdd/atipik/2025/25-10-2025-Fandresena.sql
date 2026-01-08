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
    TO_CHAR(r.daty + s.nbJour + 1, 'DD/MM/YYYY') AS datePrevisionRetour,
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

CREATE OR REPLACE VIEW CHECKOUT_DATERETOUR AS
SELECT
    c.ID,
    c.RESERVATION,
    NVL(rd.IDMERE, c.reservation) AS idReservationMere,
    NVL(TO_DATE(rlmd.datePrevisionRetour, 'DD/MM/YYYY'), TO_DATE(c.DATY, 'DD/MM/YYYY')) AS DATY,
    c.HEURE,
    c.REMARQUE,
    c.CLIENT,
    c.IDPRODUIT,
    c.ETAT,
    c.IDCLIENT,
    c.qte,
    ai.libelle AS produitLibelle,
    c.refproduit
FROM
    CHECKIN c
        LEFT JOIN RESERVATIONDETAILS rd ON c.RESERVATION = rd.ID
        LEFT JOIN AS_INGREDIENTS ai ON ai.id = c.idproduit
        LEFT JOIN RESERVATION_LIB_MIN_DATY rlmd ON c.numBl = rlmd.numBl  -- Jointure par numBl
WHERE
    c.id NOT IN (
        SELECT o.reservation
        FROM CHECKOUT o
        WHERE o.ETAT >= 11
    );