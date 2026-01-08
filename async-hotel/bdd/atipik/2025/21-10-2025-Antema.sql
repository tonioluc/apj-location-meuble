CREATE TABLE equiperesp (
    id VARCHAR(50) PRIMARY KEY,
    equiperesp VARCHAR(500),
    description VARCHAR(500),
    idreservation VARCHAR(100),
    daty DATE,
    FOREIGN KEY (idreservation) REFERENCES RESERVATION(ID)
);

CREATE SEQUENCE SEQ_EQUIPERESP INCREMENT BY 1 MINVALUE 1 MAXVALUE 999999999999999999999999999 NOCYCLE CACHE 20 NOORDER ;

CREATE OR REPLACE FUNCTION GETSEQEQUIPERESP
    RETURN NUMBER IS
    retour NUMBER;
BEGIN
    SELECT SEQ_EQUIPERESP.nextval INTO retour FROM dual;
    return retour;
END;

CREATE OR REPLACE VIEW RESERVATION_LIB AS
SELECT r.id,
    r.idClient,
    c.NOM AS idclientlib,
    r.daty,
    r.remarque,
    r.etat,
    CASE
       WHEN r.ETAT = 0
           THEN 'ANNUL&Eacute;(E)'
       WHEN r.ETAT = 1
           THEN 'CR&Eacute;&Eacute;(E)'
       WHEN r.ETAT = 11
           THEN 'VIS&Eacute;(E)'
       END AS etatlib,
    rm.montant,
    rm.montantremise,
    rm.montanttotal,
    rm.MONTANTTVA,
    rm.MONTANTTTC,
    nvl(mvt.CREDIT, 0)                                        as paye,
    cast(rm.MONTANTTTC - nvl(mvt.CREDIT, 0) as number(20, 2)) as resteAPayer,
    0 as revient,
    0 as marge,
    r.IDORIGINE,
    r.LIEULOCATION,
    m.val as magasin,
    TO_CHAR(r.daty + s.nbJour, 'DD/MM/YYYY') AS datePrevisionRetour,
    er.equiperesp,
    er.description as desceequiperesp
FROM RESERVATION r
    LEFT JOIN CLIENT c ON c.id = r.idClient
    LEFT JOIN RESERVATIONMONTANT2 rm ON rm.id = r.id
    left join MOUVEMENTCAISSEGROUPERESA mvt on mvt.IDORIGINE = r.ID
    left join magasin m on m.id = r.IDMAGASIN
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
    er.description as desceequiperesp
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