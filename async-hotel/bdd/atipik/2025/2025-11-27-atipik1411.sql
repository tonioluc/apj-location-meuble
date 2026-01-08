
-- Vue 1 : Pré-calcul des MIN(daty) des détails de réservation
CREATE OR REPLACE  VIEW MV_RESERVATION_MIN_DATY
AS
SELECT 
    idmere,
    MIN(daty) AS min_daty
FROM reservationdetails
GROUP BY idmere;


-- Vue 2 : Dernière caution par réservation
CREATE OR REPLACE  VIEW MV_CAUTION_LAST
AS
SELECT 
    cl.idreservation,
    cl.id,
    cl.daty,
    cl.montantgrp
FROM CAUTIONLIB cl
WHERE (cl.idreservation, cl.daty) IN (
    SELECT idreservation, MAX(daty)
    FROM CAUTIONLIB
    GROUP BY idreservation
);

-- Vue 3 : Somme des cautions par réservation
CREATE OR REPLACE  VIEW MV_CAUTION_SUM
AS
SELECT
    idreservation,
    SUM(montantgrp) AS montantCaution
FROM CAUTIONLIB
GROUP BY idreservation;


-- Vue 4 : Dernière équipe responsable par réservation
CREATE OR REPLACE  VIEW MV_EQUIPERESP_LAST
AS
SELECT 
    e.idreservation,
    e.equiperesp,
    e.description,
    e.daty
FROM equiperesp e
WHERE (e.idreservation, e.daty) IN (
    SELECT idreservation, MAX(daty)
    FROM equiperesp
    GROUP BY idreservation
);

CREATE OR REPLACE  VIEW RESERVATION_LIB_MIN_DATYF (ID, IDCLIENT, IDCLIENTLIB, DATY, DATEPREVISIONDEPART, REMARQUE, ETAT, ETATLIB, MONTANT, MONTANTREMISE, MONTANTTOTAL, MONTANTTVA, MONTANTTTC, PAYE, RESTEAPAYER, MONTANTCAUTION, DATYCAUTION, DEBITCAUTION, REVIENT, MARGE, IDORIGINE, LIEULOCATION, MAGASIN, DATEPREVISIONRETOUR, EQUIPERESP, DESCEEQUIPERESP, NUMBL) AS
SELECT
    r.id,
    r.idClient,
    c.NOM AS idclientlib,
    NVL(rd.min_daty, r.daty) AS daty,
    TO_CHAR(NVL(rd.min_daty, r.daty) - 1, 'DD/MM/YYYY') AS datePrevisionDepart,
    r.remarque,
    r.etat,
    CASE
        WHEN r.ETAT = 0 THEN 'ANNULE(E)'
        WHEN r.ETAT = 1 THEN 'CREE(E)'
        WHEN r.ETAT = 11 THEN 'VISE(E)'
    END AS etatlib,
    rm.montanttotal AS montant,
    rm.montantRemise,
    rm.montanttotal,
    rm.MONTANTTVA,
    rm.MONTANTTTC,
    rm.MONTANTPAYE AS paye,
    rm.MONTANTRESTE AS resteAPayer,
    NVL(cau_sum.montantCaution, 0) AS montantCaution,
    cau_last.daty AS datyCaution,
    mc.DEBIT AS debitCaution,
    0 AS revient,
    0 AS marge,
    r.IDORIGINE,
    r.LIEULOCATION,
    m.val AS magasin,
    TO_CHAR(NVL(rd.min_daty, r.daty) + s.nbJour + 1, 'DD/MM/YYYY') AS datePrevisionRetour,
    er.equiperesp,
    er.description AS desceequiperesp,
    r.numBl
FROM RESERVATION r
LEFT JOIN CLIENT c ON c.id = r.idClient
LEFT JOIN MV_RESERVATION_MIN_DATY rd ON rd.idmere = r.id
LEFT JOIN VENTE_CPL rm ON rm.idreservation = r.id
LEFT JOIN MOUVEMENTCAISSEGROUPERESA mvt ON mvt.IDORIGINE = r.ID
LEFT JOIN magasin m ON m.id = r.IDMAGASIN
LEFT JOIN sommeNombreJourReservation s ON r.id = s.idMere
LEFT JOIN MV_CAUTION_SUM cau_sum ON cau_sum.idreservation = r.id
LEFT JOIN MV_CAUTION_LAST cau_last ON cau_last.idreservation = r.id
LEFT JOIN MOUVEMENTCAISSE mc ON mc.IDORIGINE = cau_last.id
LEFT JOIN MV_EQUIPERESP_LAST er ON er.idreservation = r.id;
