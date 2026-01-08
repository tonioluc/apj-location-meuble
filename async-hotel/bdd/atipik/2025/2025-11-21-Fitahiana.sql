
create view RESERVATIONPLANNING_SOM as
select IDPRODUIT,DATY,sum(qte) as qte from RESERVATIONPLANNING group by IDPRODUIT, DATY;

create or replace view VENTE_CPL as
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
    v2.MONTANTREMISE,
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
    re.ETATLOGISTIQUELIB
FROM VENTE v
         LEFT JOIN CLIENT c ON c.ID = v.IDCLIENT
         LEFT JOIN MAGASIN m ON m.ID = v.IDMAGASIN
         JOIN VENTEMONTANT v2 ON v2.ID = v.ID
         LEFT JOIN MOUVEMENTCAISSEGROUPEFACTURE mv ON v.ID = mv.IDORIGINE
         LEFT JOIN AVOIRFCLIB_CPL_GRP ACG ON ACG.IDVENTE = v.ID
         LEFT JOIN RESERVATION r ON r.ID = v.IDORIGINE
         LEFT JOIN PROFORMA_CPL p ON p.ID = r.IDORIGINE
         left join reservation_etatlogistiquelib re on re.id = v.IDRESERVATION
    /

create or replace view CAUTIONLIB as
SELECT
    c."ID",c."IDRESERVATION",c."IDMODEPAIEMENT",c."PCT_APPLIQUE",c."REFERENCEPAIEMENT",c."DATY",c."DATEPREVUERESTITUTION",c."ETAT",c."MONTANTRESERVATION",
    m.val AS modepaiement,
    cm.montant AS montantgrp,
    sum(nvl(mc.CREDIT,0)) as credit,
    sum(nvl(mc.DEBIT,0))-sum(nvl(mc.CREDIT,0)) as debit
FROM CAUTION c
         LEFT JOIN MODEPAIEMENT m ON m.id = c.idmodepaiement
         LEFT JOIN caution_montantgrp cm ON cm.idcaution = c.id
         LEFT JOIN MOUVEMENTCAISSE mc on mc.IDORIGINE=c.ID
group by c."ID",c."IDRESERVATION",c."IDMODEPAIEMENT",c."PCT_APPLIQUE",c."REFERENCEPAIEMENT",c."DATY",c."DATEPREVUERESTITUTION",c."ETAT",c."MONTANTRESERVATION",
         m.val ,cm.montant

create or replace view CHECKOUTLIB as
SELECT
    ch.ID,
    ci.ID AS RESERVATION,
    ci.IDRESERVATIONMERE,
    ch.DATY,
    ch.HEURE,
    ch.REMARQUE,
    ch.ETAT,
    ci.PRODUITLIBELLE,
    CASE
        WHEN ch.ETAT = 1 THEN 'CREE'
        WHEN ch.ETAT = 11 THEN 'VALIDEE'
        END AS etatlib,
    ci.kilometragecheckin,
    ch.KILOMETRAGE AS kilometragecheckout,
    ci.distancereelle,
    ch.quantite,
    ci.pu,
    ci.idproduit,
    ch.idmagasin,
    mag.val,
    mag.val as idmagasinlib,
    ch.responsable,
    ch.refproduit,
    ch.etat_materiel_lib,
    ing.image AS image
FROM CHECKOUT ch
         JOIN CHECKINLIBELLE ci ON ci.ID = ch.RESERVATION
         LEFT JOIN magasin mag ON ch.idmagasin = mag.id
         LEFT JOIN As_ingredients ing ON ing.id = ci.idproduit
    /