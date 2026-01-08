
create or replace view PROFORMADETAILS_CPLIMAGE as
SELECT
    vd."ID",vd."IDPROFORMA",vd."IDPRODUIT",vd."IDORIGINE",vd."QTE",vd."PU",vd."REMISE",vd."TVA",vd."PUACHAT",vd."PUVENTE",vd."IDDEVISE",vd."TAUXDECHANGE",vd."DESIGNATION",p.DURRE as dimension,vd."COMPTE",vd."PUREVIENT",vd."IDDEMANDEPRIXFILLE",vd."UNITE",vd."NOMBRE",vd."DATEDEBUT",vd."MARGEMOINS",vd."MARGEPLUS",
    p.image
FROM PROFORMA_DETAILS vd
         LEFT JOIN AS_INGREDIENTS p ON p.ID = vd.IDPRODUIT
    /

create or replace view ST_INGREDIENTSAUTOVENTE_MIMAGE as
SELECT ai.ID,
       ai.LIBELLE,
       ai.SEUIL,
       ai.UNITE,
       ai.QUANTITEPARPACK,
       ai.PU,
       ai.ACTIF,
       ai.PHOTO,
       ai.CALORIE,
       nvl(ai.DURRE,1) as durre,
       ai.COMPOSE,
       ai.CATEGORIEINGREDIENT,
       ai.IDFOURNISSEUR,
       ai.DATY,
       ai.QTELIMITE,
       ai.PV,
       ai.LIBELLEVENTE,
       ai.SEUILMIN,
       ai.SEUILMAX,
       ai.PUACHATUSD,
       ai.PUACHATEURO,
       ai.PUACHATAUTREDEVISE,
       ai.PUVENTEUSD,
       ai.PUVENTEEURO,
       ai.PUVENTEAUTREDEVISE,
       ai.ISVENTE,
       ai.ISACHAT,
       ai.COMPTE_VENTE,
       ai.COMPTE_ACHAT,
       ai.TVA,
       ai.COMPTE_VENTE AS compte,
       ai.idvoiture,
       ai.IDMODELE,
       aimg.quantite,
       ai.reference,
       ai.image
FROM AS_INGREDIENTS ai left join as_ingredient_m_grp aimg on aimg.IDMODELE = ai.id
where ai.pv > 0
    /

create or replace view PROFORMAMONTANT2 as
SELECT v.ID,
       cast(SUM ((NVL (nvl(ing.durre,1)*vd.QTE, 0)* NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0))) as number(30,2)) AS montant,
       cast(SUM ((1-nvl(vd.remise/100,0))*(NVL (nvl(ing.durre,1)*vd.QTE, 0)* NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0))) as number(30,2)) AS montanttotal,
       cast(SUM ((NVL (nvl(ing.durre,1)*vd.QTE, 0)* NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0))) as number(30,2)) - cast(SUM ((1-nvl(vd.remise/100,0))*(nvl(ing.durre,1)*NVL (vd.QTE, 0)* NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0))) as number(30,2)) as montantremise,
       cast(SUM ( (nvl(ing.durre,1)*NVL (vd.QTE, 0)* NVL (vd.NOMBRE, 0) * NVL (vd.PuAchat, 0))) as number(30,2)) AS montanttotalachat,
       cast(SUM ((1-nvl(vd.remise/100,0))* (nvl(ing.durre,1)*NVL (vd.QTE, 0)* NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0))* (NVL (VD.TVA , 0))/100) as number(30,2)) AS montantTva,
       cast(SUM ((1-nvl(vd.remise/100,0))* (nvl(ing.durre,1)*NVL (vd.QTE, 0)* NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0))* (NVL (VD.TVA , 0))/100)+SUM ((1-nvl(vd.remise/100,0))* (nvl(ing.durre,1)*NVL (vd.QTE, 0)* NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0)))as number(30,2))  as montantTTC,
       NVL (vd.IDDEVISE,'AR') AS IDDEVISE,
       NVL(avg(vd.tauxDeChange),1 ) AS tauxDeChange,
       cast(SUM ( (1-nvl(vd.remise/100,0))*(nvl(ing.durre,1)*NVL (vd.QTE, 0)* NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0)*NVL(VD.TAUXDECHANGE,1) )* (NVL (VD.TVA , 0))/100)+SUM ((1-nvl(vd.remise/100,0))* (nvl(ing.durre,1)*NVL (vd.QTE, 0)* NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0)*NVL(VD.TAUXDECHANGE,1)))as number(30,2)) as montantTTCAR,
       cast(sum(vd.PUREVIENT*vd.QTE) as number(20,2)) as montantRevient  ,
       v.IDRESERVATION
FROM PROFORMA_DETAILS vd LEFT JOIN PROFORMA v ON v.ID = vd.IDPROFORMA LEFT JOIN AS_INGREDIENTS ing on ing.id=vd.IDPRODUIT
GROUP BY v.ID, vd.IDDEVISE,v.IDRESERVATION
    /



create or replace view PROFORMADETAILS_CPL as
SELECT
    vd.ID,vd.IDPROFORMA,vd.IDPRODUIT,vd.IDORIGINE,vd.QTE,vd.PU,vd.REMISE,vd.TVA,vd.PUACHAT,vd.PUVENTE,vd.IDDEVISE,vd.TAUXDECHANGE,vd.DESIGNATION,vd.COMPTE,vd.PUREVIENT,vd.IDDEMANDEPRIXFILLE,
    nvl(vd.nombre,0) as nombre,
    v.DESIGNATION    AS IDPROFORMALIB,
    p.LIBELLE            AS IDPRODUITLIB,
    cast((nvl(p.durre,1)*nvl(vd.nombre,0)*vd.QTE * vd.PU) as NUMBER(30,2)) AS puTotal,
    vd.unite,
    au.val AS uniteLib,
    p.image,
    cast(((nvl(p.durre,1)*nvl(vd.nombre,0)*vd.QTE * vd.PU)*NVL(vd.remise, 0))/100 as NUMBER(30,2)) AS remisemontant,
    cast((nvl(p.durre,1)*nvl(vd.nombre,0)*vd.QTE * vd.PU)-(nvl(p.durre,1)*nvl(vd.nombre,0)*(vd.QTE * vd.PU)*NVL(vd.remise, 0))/100 as NUMBER(30,2)) as montanttotal,
    (nvl(vd.TVA, 0)/100)*cast((nvl(p.durre,1)*nvl(vd.nombre,0)*vd.QTE * vd.PU)-(nvl(p.durre,1)*nvl(vd.nombre,0)*(vd.QTE * vd.PU)*NVL(vd.remise, 0))/100 as NUMBER(30,2)) as montanttva,
    cast((nvl(p.durre,1)*nvl(vd.nombre,0)*vd.QTE * vd.PU)-(nvl(p.durre,1)*nvl(vd.nombre,0)*(vd.QTE * vd.PU)*NVL(vd.remise, 0))/100 as NUMBER(30,2))+(nvl(vd.TVA, 0)/100)*cast((nvl(p.durre,1)*nvl(vd.nombre,0)*vd.QTE * vd.PU)-(nvl(p.durre,1)*nvl(vd.nombre,0)*(vd.QTE * vd.PU)*NVL(vd.remise, 0))/100 as NUMBER(30,2)) as montantttc,
    vd.datedebut,
    p.reference
--       d2.id AS idDevis
FROM PROFORMA_DETAILS vd
         LEFT JOIN PROFORMA v ON v.ID = vd.IDPROFORMA
         LEFT JOIN AS_INGREDIENTS p ON p.ID = vd.IDPRODUIT
         LEFT JOIN DMDPRIXFILLE d
                   ON
                       vd.IDDEMANDEPRIXFILLE = d.ID
         LEFT JOIN AS_UNITE au ON au.id = vd.unite
    /
create or replace view PROFORMADETAILS_CPL as
SELECT
    vd.ID,vd.IDPROFORMA,vd.IDPRODUIT,vd.IDORIGINE,vd.QTE,vd.PU,vd.REMISE,vd.TVA,vd.PUACHAT,vd.PUVENTE,vd.IDDEVISE,vd.TAUXDECHANGE,vd.DESIGNATION,vd.COMPTE,vd.PUREVIENT,vd.IDDEMANDEPRIXFILLE,
    nvl(vd.nombre,0) as nombre,
    v.DESIGNATION    AS IDPROFORMALIB,
    p.LIBELLE            AS IDPRODUITLIB,
    cast((nvl(p.durre,1)*nvl(vd.nombre,0)*vd.QTE * vd.PU) as NUMBER(30,2)) AS puTotal,
    vd.unite,
    au.val AS uniteLib,
    p.image,
    cast(((nvl(p.durre,1)*nvl(vd.nombre,0)*vd.QTE * vd.PU)*NVL(vd.remise, 0))/100 as NUMBER(30,2)) AS remisemontant,
    cast((nvl(p.durre,1)*nvl(vd.nombre,0)*vd.QTE * vd.PU)-(nvl(p.durre,1)*nvl(vd.nombre,0)*(vd.QTE * vd.PU)*NVL(vd.remise, 0))/100 as NUMBER(30,2)) as montanttotal,
    (nvl(vd.TVA, 0)/100)*cast((nvl(p.durre,1)*nvl(vd.nombre,0)*vd.QTE * vd.PU)-(nvl(p.durre,1)*nvl(vd.nombre,0)*(vd.QTE * vd.PU)*NVL(vd.remise, 0))/100 as NUMBER(30,2)) as montanttva,
    cast((nvl(p.durre,1)*nvl(vd.nombre,0)*vd.QTE * vd.PU)-(nvl(p.durre,1)*nvl(vd.nombre,0)*(vd.QTE * vd.PU)*NVL(vd.remise, 0))/100 as NUMBER(30,2))+(nvl(vd.TVA, 0)/100)*cast((nvl(p.durre,1)*nvl(vd.nombre,0)*vd.QTE * vd.PU)-(nvl(p.durre,1)*nvl(vd.nombre,0)*(vd.QTE * vd.PU)*NVL(vd.remise, 0))/100 as NUMBER(30,2)) as montantttc,
    vd.datedebut,
    p.reference
--       d2.id AS idDevis
FROM PROFORMA_DETAILS vd
         LEFT JOIN PROFORMA v ON v.ID = vd.IDPROFORMA
         LEFT JOIN AS_INGREDIENTS p ON p.ID = vd.IDPRODUIT
         LEFT JOIN DMDPRIXFILLE d
                   ON
                       vd.IDDEMANDEPRIXFILLE = d.ID
         LEFT JOIN AS_UNITE au ON au.id = vd.unite
    /


create or replace view VENTEMONTANT as
SELECT v.ID,
       cast(SUM ((1-nvl(vd.remise/100,0))*(nvl(ing.durre,1)*NVL (vd.QTE, 0)*NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0))) as number(30,2)) AS montanttotal,
       cast(SUM ( (nvl(ing.durre,1)*NVL (vd.QTE, 0)*NVL (vd.NOMBRE, 0) * NVL (vd.PuAchat, 0))) as number(30,2)) AS montanttotalachat,
       cast(SUM ((1-nvl(vd.remise/100,0))* (nvl(ing.durre,1)*NVL (vd.QTE, 0)*NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0))* (NVL (VD.TVA , 0))/100) as number(30,2)) AS montantTva,
       cast(SUM ((1-nvl(vd.remise/100,0))* (nvl(ing.durre,1)*NVL (vd.QTE, 0)*NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0))* (NVL (VD.TVA , 0))/100)+SUM ((1-nvl(vd.remise/100,0))* (nvl(ing.durre,1)*NVL (vd.QTE, 0)*NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0)))as number(30,2))  as montantTTC,
       NVL (vd.IDDEVISE,'AR') AS IDDEVISE,
       NVL(avg(vd.tauxDeChange),1 ) AS tauxDeChange,
       cast(SUM ( (1-nvl(vd.remise/100,0))*(nvl(ing.durre,1)*NVL (vd.QTE, 0)*NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0)*NVL(VD.TAUXDECHANGE,1) )* (NVL (VD.TVA , 0))/100)+SUM ((1-nvl(vd.remise/100,0))* (nvl(ing.durre,1)*NVL (vd.QTE, 0)*NVL (vd.NOMBRE, 0) * NVL (vd.PU, 0)*NVL(VD.TAUXDECHANGE,1)))as number(30,2)) as montantTTCAR,
       cast(sum(vd.PUREVIENT*nvl(ing.durre,1)*vd.QTE*NVL (vd.NOMBRE, 0)) as number(20,2)) as montantRevient  ,
       v.IDRESERVATION,
       cast(SUM( nvl(ing.durre,1)*NVL(vd.QTE,0) * NVL(vd.NOMBRE,0) * NVL(vd.PU,0) * NVL(vd.remise,0) / 100 ) as number(30,2)) AS montantRemise
FROM VENTE_DETAILS vd LEFT JOIN VENTE v ON v.ID = vd.IDVENTE LEFT JOIN AS_INGREDIENTS ing on ing.id=vd.IDPRODUIT
GROUP BY v.ID, vd.IDDEVISE,v.IDRESERVATION
    /



create or replace view VENTE_DETAILS_LIB as
SELECT vd.ID,
       vd.IDVENTE,
       v.DESIGNATION    AS IDVENTELIB,
       vd.IDPRODUIT,
       p.LIBELLE            AS IDPRODUITLIB,
       vd.IDORIGINE,
       vd.QTE,
       vd.PU,
       cast((nvl(p.durre,1)*vd.QTE * vd.PU) as NUMBER(30,2)) AS puTotal,
       vd.PuAchat AS PUACHAT,
       v.IDRESERVATION
FROM VENTE_DETAILS vd
         LEFT JOIN VENTE v ON v.ID = vd.IDVENTE
         LEFT JOIN AS_INGREDIENTS p ON p.ID = vd.IDPRODUIT
    /

create or replace view VENTE_DETAILS_CPL as
SELECT
    vd.ID,
    vd.IDVENTE,
    v.DESIGNATION AS IDVENTELIB,
    vd.IDPRODUIT,
    p.LIBELLE AS IDPRODUITLIB,
    vd.IDORIGINE,
    vd.QTE,
    vd.PU AS PU,

    CAST((NVL(vd.remise/100,0)) * (nvl(p.durre,1)*vd.QTE * NVL(vd.nombre,0) * vd.PU) AS NUMBER(30,2)) AS montantRemise,
    CAST((1 - NVL(vd.remise/100,0)) * (nvl(p.durre,1)*vd.QTE * NVL(vd.nombre,0) * vd.PU) AS NUMBER(30,2)) AS montant,
    (NVL(vd.TVA/100,0)) * CAST((1 - NVL(vd.remise/100,0)) * (nvl(p.durre,1)*vd.QTE * NVL(vd.nombre,0) * vd.PU) AS NUMBER(30,2)) AS montanttva,
    (NVL(vd.TVA/100,0)) * CAST((1 - NVL(vd.remise/100,0)) * (nvl(p.durre,1)*vd.QTE * NVL(vd.nombre,0) * vd.PU) AS NUMBER(30,2))
        + CAST((1 - NVL(vd.remise/100,0)) * (nvl(p.durre,1)*vd.QTE * NVL(vd.nombre,0) * vd.PU) AS NUMBER(30,2)) AS montantttc,

    vd.iddevise AS iddevise,
    vd.tauxDeChange AS tauxDeChange,
    vd.tva AS tva,
    v.idclient,
    v.idclientlib,
    vd.designation,
    vd.PUREVIENT,
    CAST(nvl(p.durre,1)*vd.QTE * NVL(vd.nombre,0) * vd.PUREVIENT AS NUMBER(20,2)) AS montantRevient,
    vd.DATERESERVATION,
    p.image,
    p.reference,
    p.unite,
    vd.nombre,

    CAST(((NVL(vd.nombre,0) *nvl(p.durre,1)* vd.QTE * vd.PU) * NVL(vd.remise, 0)) / 100 AS NUMBER(30,2)) AS remisemontant,
    vd.remise,
    CAST(nvl(p.durre,1)*vd.QTE * NVL(vd.nombre,0) * vd.PU AS NUMBER(30,2)) AS montantAvantRemise

FROM VENTE_DETAILS vd
         LEFT JOIN VENTE_LIB v ON v.ID = vd.IDVENTE
         LEFT JOIN AS_INGREDIENTS_LIB p ON p.ID = vd.IDPRODUIT
    /


