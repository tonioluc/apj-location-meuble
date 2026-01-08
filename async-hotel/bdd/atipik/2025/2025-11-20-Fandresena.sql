CREATE OR REPLACE VIEW RESERVATION_LIB_MIN_DATYF AS
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
    rm.montanttotal AS montant,
    rm.montantRemise,
    rm.montanttotal,
    rm.MONTANTTVA,
    rm.MONTANTTTC,
    rm.MONTANTPAYE AS paye,
    rm.MONTANTRESTE AS resteAPayer,
    NVL(cau.montantCaution, 0) AS montantCaution,
    cl_last.daty AS datyCaution,
    mc.DEBIT AS debitCaution,
    0 AS revient,
    0 AS marge,
    r.IDORIGINE,
    r.LIEULOCATION,
    m.val AS magasin,
    TO_CHAR(r.daty + s.nbJour + 1, 'DD/MM/YYYY') AS datePrevisionRetour,
    er.equiperesp,
    er.description AS desceequiperesp,
    r.numBl
FROM RESERVATION r
         LEFT JOIN CLIENT c ON c.id = r.idClient
         LEFT JOIN VENTE_CPL rm ON rm.idreservation = r.id
         LEFT JOIN MOUVEMENTCAISSEGROUPERESA mvt ON mvt.IDORIGINE = r.ID
         LEFT JOIN magasin m ON m.id = r.IDMAGASIN
         LEFT JOIN sommeNombreJourReservation s ON r.id = s.idMere
         LEFT JOIN (
    SELECT
        idreservation,
        SUM(montantgrp) AS montantCaution
    FROM CAUTIONLIB
    GROUP BY idreservation
) cau ON cau.idreservation = r.id
         LEFT JOIN (
    SELECT cl.*
    FROM CAUTIONLIB cl
             JOIN (
        SELECT idreservation, MAX(daty) AS max_daty
        FROM CAUTIONLIB
        GROUP BY idreservation
    ) last_cl ON cl.idreservation = last_cl.idreservation
        AND cl.daty = last_cl.max_daty
) cl_last ON cl_last.idreservation = r.id
         LEFT JOIN MOUVEMENTCAISSE mc
                   ON mc.IDORIGINE = cl_last.id
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


CREATE OR REPLACE VIEW PROFORMA_INSERT AS
SELECT
    p.*,
    '' AS idmagasinlib
FROM PROFORMA p;

CREATE OR REPLACE VIEW CLIENTLIB AS
SELECT
    c."ID",c."NOM",c."TELEPHONE",c."MAIL",c."ADRESSE",c."REMARQUE",c."COMPTE",c."IDNATIONALITE",c."CIN",c."DATECIN",c."CINPATH",c."PERMIS",c."DATEPERMIS",c."PERMISPATH",c."PHOTOPROFLEPATH",c."PASSEPORT",c."REPRESENTANT",c."IDTYPE",c."IDTYPECLIENT",c."NIF",c."STAT",
    t.VAL AS TYPECLIENTLIB
FROM CLIENT c
         LEFT JOIN TYPECLIENT t
                   ON c.IDTYPECLIENT = t.ID;

CREATE OR REPLACE VIEW VENTEMONTANT AS
SELECT v.ID,
       cast(SUM ((1-nvl(vd.remise/100,0))*(NVL (vd.QTE, 0)*NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0))) as number(30,2)) AS montanttotal,
       cast(SUM ( (NVL (vd.QTE, 0)*NVL (vd.NOMBRE, 0) * NVL (vd.PuAchat, 0))) as number(30,2)) AS montanttotalachat,
       cast(SUM ((1-nvl(vd.remise/100,0))* (NVL (vd.QTE, 0)*NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0))* (NVL (VD.TVA , 0))/100) as number(30,2)) AS montantTva,
       cast(SUM ((1-nvl(vd.remise/100,0))* (NVL (vd.QTE, 0)*NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0))* (NVL (VD.TVA , 0))/100)+SUM ((1-nvl(vd.remise/100,0))* (NVL (vd.QTE, 0)*NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0)))as number(30,2))  as montantTTC,
       NVL (vd.IDDEVISE,'AR') AS IDDEVISE,
       NVL(avg(vd.tauxDeChange),1 ) AS tauxDeChange,
       cast(SUM ( (1-nvl(vd.remise/100,0))*(NVL (vd.QTE, 0)*NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0)*NVL(VD.TAUXDECHANGE,1) )* (NVL (VD.TVA , 0))/100)+SUM ((1-nvl(vd.remise/100,0))* (NVL (vd.QTE, 0)*NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0)*NVL(VD.TAUXDECHANGE,1)))as number(30,2)) as montantTTCAR,
       cast(sum(vd.PUREVIENT*vd.QTE*NVL (vd.NOMBRE, 0)) as number(20,2)) as montantRevient  ,
       v.IDRESERVATION,
       cast(SUM( NVL(vd.QTE,0) * NVL(vd.NOMBRE,0) * NVL(vd.PU,0) * NVL(vd.remise,0) / 100 ) as number(30,2)) AS montantRemise
FROM VENTE_DETAILS vd LEFT JOIN VENTE v ON v.ID = vd.IDVENTE
GROUP BY v.ID, vd.IDDEVISE,v.IDRESERVATION;

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
    v2.IDDEVISE,
    v.IDCLIENT,
    c.NOM AS IDCLIENTLIB,
    c.telephone,
    v2.MONTANTTOTAL,
    CAST(v2.MONTANTTVA AS NUMBER(30,2)) AS MONTANTTVA,
    CAST(v2.MONTANTTTC AS NUMBER(30,2)) AS MONTANTTTC,
    CAST(v2.MONTANTTTCAR AS NUMBER(30,2)) AS MONTANTTTCAR,
    CAST(NVL(mv.CREDIT,0) - NVL(ACG.MONTANTPAYE, 0) AS NUMBER(30,2)) AS montantpaye,
    CAST(v2.MONTANTTTC - NVL(mv.CREDIT,0) - NVL(ACG.resteapayer_avr, 0) AS NUMBER(30,2)) AS montantreste,
    NVL(ACG.MONTANTTTC_avr, 0) AS avoir,
    v2.tauxDeChange AS tauxDeChange,
    v2.MONTANTREVIENT,
    CAST((v2.MONTANTTTCAR - v2.MONTANTREVIENT) AS NUMBER(20,2)) AS margeBrute,
    v.DATYPREVU,
    p.PERIODE AS PERIODE,
    v.IDRESERVATION,
    re.ETATLOGISTIQUE,
    re.ETATLOGISTIQUELIB,
    v2.MONTANTREMISE
FROM VENTE v
         LEFT JOIN CLIENT c ON c.ID = v.IDCLIENT
         LEFT JOIN MAGASIN m ON m.ID = v.IDMAGASIN
         JOIN VENTEMONTANT v2 ON v2.ID = v.ID
         LEFT JOIN MOUVEMENTCAISSEGROUPEFACTURE mv ON v.ID = mv.IDORIGINE
         LEFT JOIN AVOIRFCLIB_CPL_GRP ACG ON ACG.IDVENTE = v.ID
         LEFT JOIN RESERVATION r ON r.ID = v.IDORIGINE
         LEFT JOIN PROFORMA_CPL p ON p.ID = r.IDORIGINE
         left join reservation_etatlogistiquelib re on re.id = v.IDRESERVATION;

INSERT INTO CAISSE
(ID, VAL, DESCE, IDTYPECAISSE, IDPOINT, IDPOMPISTE, ETAT, IDCATEGORIECAISSE, COMPTE, IDMAGASIN, IDDEVISE)
VALUES('CAI000284', 'Caisse Airtel Money', 'Caisse Airtel Money', 'TCA002', 'PNT000086', , 11  , 'CTC004', 'COC007913', 'PNT000086', 'AR');

INSERT INTO CAISSE
(ID, VAL, DESCE, IDTYPECAISSE, IDPOINT, IDPOMPISTE, ETAT, IDCATEGORIECAISSE, COMPTE, IDMAGASIN, IDDEVISE)
VALUES('CAI000282', 'Caisse cheque', 'Caisse cheque', 'TCA001', 'PNT000086', , 11  , 'CTC004', 'COC007913', 'PNT000086', 'AR');

INSERT INTO CAISSE
(ID, VAL, DESCE, IDTYPECAISSE, IDPOINT, IDPOMPISTE, ETAT, IDCATEGORIECAISSE, COMPTE, IDMAGASIN, IDDEVISE)
VALUES('CAI000283', 'Caisse Orange Money', 'Caisse Orange Money', 'TCA002', 'PNT000086', , 11  , 'CTC004', 'COC007913', 'PNT000086', 'AR');

CREATE OR REPLACE VIEW reservation_lib_f AS
select rlmd."ID",rlmd."IDCLIENT",rlmd."IDCLIENTLIB",rlmd."DATY",rlmd."DATEPREVISIONDEPART",rlmd."REMARQUE",rlmd."ETAT",rlmd."ETATLIB",rlmd."MONTANT",rlmd."MONTANTREMISE",rlmd."MONTANTTOTAL",rlmd."MONTANTTVA",rlmd."MONTANTTTC",rlmd."PAYE",rlmd."RESTEAPAYER",rlmd."REVIENT",rlmd."MARGE",rlmd."IDORIGINE",rlmd."LIEULOCATION",rlmd."MAGASIN",rlmd."DATEPREVISIONRETOUR",rlmd."EQUIPERESP",rlmd."DESCEEQUIPERESP",rlmd."NUMBL",
       c.qtecheckin,
       c.qtecheckout,
       case when c.qtecheckin > 0 and c.qtecheckout > 0 then 14 when c.qtecheckin > 0 and c.qtecheckout = 0 then 13 else 12 end as etatlogistique
from RESERVATION_LIB_MIN_DATYF rlmd
         left join situationreservationlogistique c on c.idreservationmere = rlmd.id;

CREATE OR REPLACE VIEW RESERVATION_ETATLIB_F AS
SELECT
    id,
    idClient,
    idclientlib,
    daty,
    datePrevisionDepart,
    remarque,
    etat,
    etatlib,
    montant,
    montantremise,
    montanttotal,
    MONTANTTVA,
    MONTANTTTC,
    paye,
    resteAPayer,
    revient,
    marge,
    IDORIGINE,
    LIEULOCATION,
    magasin,
    datePrevisionRetour,
    equiperesp,
    desceequiperesp,
    numBl,
    qtecheckin,
    qtecheckout,
    etatlogistique,
    CASE
        WHEN re.etatlogistique = 12 THEN '<span class="badge bg-warning fw-normal">A PREPARER</span>'
        WHEN re.etatlogistique = 13 THEN '<span class="badge bg-success fw-normal">EXPEDIEE</span>'
        WHEN re.etatlogistique = 14 THEN '<span class="badge bg-danger fw-normal">BOUCLEE</span>'
        ELSE ''
        END AS etatlogistiquelib,
    CASE
        WHEN re.resteAPayer = 0 THEN 16
        ELSE 17
        END AS etatpayment,
    CASE
        WHEN re.resteAPayer = 0 THEN '<span class="badge bg-success fw-normal">PAYE TOTALITE</span>'
        ELSE '<span class="badge bg-warning fw-normal">ACOMPTE</span>'
        END AS etatpaymentlib
FROM reservation_lib_f re;