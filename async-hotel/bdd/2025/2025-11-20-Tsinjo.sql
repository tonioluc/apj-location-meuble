CREATE OR REPLACE VIEW PROFORMAARELANCER AS
select
    p.ID,
    p.DESIGNATION,
    p.IDMAGASIN,
    p.DATY,
    p.REMARQUE,
    p.ETAT,
    p.IDORIGINE,
    p.IDCLIENT,
    c.NOM as idclientlib,
    c.TELEPHONE as contact,
    c.MAIL,
    p.ESTPREVU,
    p.DATYPREVU,
    p.IDRESERVATION,
    p.TVA,p.ECHEANCE,
    p.REGLEMENT,
    p.REMISE,
    p.LIEULOCATION,
    p.CAUTION,
    pd.DATEDEBUT,
    pr.daterelance
from PROFORMA p
         join PROFORMADETAILMAX pd on p.id=pd.IDPROFORMA
         left join ProformaRelanceMax pr on pr.IDPROFORMA=p.id
         left join client c on c.id = p.idclient
         LEFT JOIN BONDECOMMANDE_CLIENT bc ON p.id = bc.idProforma
WHERE SYSDATE > pd.DATEDEBUT - 5 AND p.etat < 11 AND bc.id IS NULL
;

CREATE OR REPLACE VIEW VENTE_DETAILS_CPL AS
SELECT
    vd.ID,
    vd.IDVENTE,
    v.DESIGNATION AS IDVENTELIB,
    vd.IDPRODUIT,
    p.LIBELLE AS IDPRODUITLIB,
    vd.IDORIGINE,
    vd.QTE,
    vd.PU AS PU,

    CAST((NVL(vd.remise/100,0)) * (vd.QTE * NVL(vd.nombre,0) * vd.PU) AS NUMBER(30,2)) AS montantRemise,
    CAST((1 - NVL(vd.remise/100,0)) * (vd.QTE * NVL(vd.nombre,0) * vd.PU) AS NUMBER(30,2)) AS montant,
    (NVL(vd.TVA/100,0)) * CAST((1 - NVL(vd.remise/100,0)) * (vd.QTE * NVL(vd.nombre,0) * vd.PU) AS NUMBER(30,2)) AS montanttva,
    (NVL(vd.TVA/100,0)) * CAST((1 - NVL(vd.remise/100,0)) * (vd.QTE * NVL(vd.nombre,0) * vd.PU) AS NUMBER(30,2))
        + CAST((1 - NVL(vd.remise/100,0)) * (vd.QTE * NVL(vd.nombre,0) * vd.PU) AS NUMBER(30,2)) AS montantttc,

    vd.iddevise AS iddevise,
    vd.tauxDeChange AS tauxDeChange,
    vd.tva AS tva,
    v.idclient,
    v.idclientlib,
    vd.designation,
    vd.PUREVIENT,
    CAST(vd.QTE * NVL(vd.nombre,0) * vd.PUREVIENT AS NUMBER(20,2)) AS montantRevient,
    vd.DATERESERVATION,
    p.image,
    p.reference,
    p.unite,
    vd.nombre,

    CAST(((NVL(vd.nombre,0) * vd.QTE * vd.PU) * NVL(vd.remise, 0)) / 100 AS NUMBER(30,2)) AS remisemontant,
    vd.remise,
    CAST(vd.QTE * NVL(vd.nombre,0) * vd.PU AS NUMBER(30,2)) AS montantAvantRemise

FROM VENTE_DETAILS vd
         LEFT JOIN VENTE_LIB v ON v.ID = vd.IDVENTE
         LEFT JOIN AS_INGREDIENTS_LIB p ON p.ID = vd.IDPRODUIT;

CREATE OR REPLACE VIEW CheckOutReservation AS
SELECT
    c.* ,
    r.ID AS idReservation
FROM CHECKOUT c
         LEFT JOIN CHECKIN c2
                   ON c.RESERVATION = c2.ID
         LEFT JOIN RESERVATIONDETAILS r2
                   ON c2.RESERVATION = r2.ID
         LEFT JOIN RESERVATION r
                   ON r2.IDMERE = r.ID ;