create or replace view CHECKOUTAVECRESERVATION as
select c.RESERVATION ,o.id,o.DATY,o.ETAT,  o.HEURE,o.REMARQUE, o.RESERVATION as checkOut,
       case when o.ETAT = 1 then 'CREE'
            when o.ETAT = 11 then 'VALIDEE'
           end as etatlib,
    r.id as idreservationmere, o.QUANTITE
from CHECKOUT o left join CHECKIN c on c.id=o.RESERVATION
    left join RESERVATIONDETAILS rd on rd.id = c.RESERVATION
left join reservation r on r.id = rd.IDMERE
/



select * from CHECKOUTAVECRESERVATION;

select * from CHECKINAVECCHEKOUTCOMPLET;

create or replace view CHECKINAVECCHEKOUTCOMPLET as
select c.RESERVATION,
       o.id,
       o.DATY,
       o.ETAT,
       o.HEURE,
       o.REMARQUE,
       o.RESERVATION as checkOut,
       o.id as idcheckout,
       c.id as idcheckin,
       r.id as idreservationmere,
       c.QTE as qtecheckin,
       o.QUANTITE as qtecheckout,
       rd.QTEARTICLE
from CHECKIN c
         left join CHECKOUT o on c.id = o.RESERVATION
left join RESERVATIONDETAILS rd on rd.id = c.RESERVATION
left join reservation r on r.id = rd.IDMERE
/

create or replace view situationreservationlogistique as
select idreservationmere,
       case when sum(nvl(QTEARTICLE, 0)) > sum(nvl(qtecheckin, 0)) then 0 else sum(nvl(qtecheckin, 0)) end as qtecheckin,
       case when sum(nvl(QTEARTICLE, 0)) > sum(nvl(qtecheckout, 0)) then 0 else sum(nvl(qtecheckout, 0)) end as qtecheckout
from CHECKINAVECCHEKOUTCOMPLET
group by idreservationmere;

create or replace view reservation_etatlogistique as
select rlmd.*,
       c.qtecheckin,
       c.qtecheckout,
       case when c.qtecheckin > 0 and c.qtecheckout > 0 then 14 when c.qtecheckin > 0 and c.qtecheckout = 0 then 13 else 12 end as etatlogistique
from RESERVATION_LIB_MIN_DATY rlmd
         left join situationreservationlogistique c on c.idreservationmere = rlmd.id;

create or replace view reservation_etatlogistiquelib as
select id,
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
            WHEN re.etatlogistique = 12 THEN '<span class= "badge bg-warning fw-normal" >A PREPARER</span>'
            WHEN re.etatlogistique = 13 THEN '<span class= "badge bg-success fw-normal" >EXPEDIEE</span>'
            WHEN re.etatlogistique = 14 THEN '<span class= "badge bg-danger  fw-normal" >BOUCLEE</span>'
            ELSE ''
        END AS etatlogistiquelib
from reservation_etatlogistique re;

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
    re.ETATLOGISTIQUELIB
FROM VENTE v
         LEFT JOIN CLIENT c ON c.ID = v.IDCLIENT
         LEFT JOIN MAGASIN m ON m.ID = v.IDMAGASIN
         JOIN VENTEMONTANT v2 ON v2.ID = v.ID
         LEFT JOIN MOUVEMENTCAISSEGROUPEFACTURE mv ON v.ID = mv.IDORIGINE
         LEFT JOIN AVOIRFCLIB_CPL_GRP ACG ON ACG.IDVENTE = v.ID
         LEFT JOIN RESERVATION r ON r.ID = v.IDORIGINE
         LEFT JOIN PROFORMA_CPL p ON p.ID = r.IDORIGINE
left join reservation_etatlogistiquelib re on re.id = v.IDRESERVATION;

create or replace view RESERVATIONCALENDRIER_MIN_DATY as
SELECT r.ID,
       r.IDCLIENT,
       r.IDCLIENTLIB,
       r.DATY,
       r.REMARQUE,
       r.ETAT,
       r.ETATLIB,
       r.MONTANT,
       r.MONTANTTOTAL,
       r.MONTANTREMISE,
       r.MONTANTTTC,
       r.MONTANTTVA,
       r.PAYE,
       r.RESTEAPAYER,
       r.REVIENT,
       r.MARGE,
       r.IDORIGINE,
       r.LIEULOCATION,
       r.MAGASIN,
       r.ETATLOGISTIQUE,
       r.ETATLOGISTIQUELIB
FROM RESERVATION_ETATLOGISTIQUELIB r
WHERE ETAT >= 11;