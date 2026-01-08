CREATE OR REPLACE VIEW PROFORMADETAILS_CPL AS
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
    p.reference,
    p.durre AS dimension
--       d2.id AS idDevis
FROM PROFORMA_DETAILS vd
         LEFT JOIN PROFORMA v ON v.ID = vd.IDPROFORMA
         LEFT JOIN AS_INGREDIENTS p ON p.ID = vd.IDPRODUIT
         LEFT JOIN DMDPRIXFILLE d
                   ON
                       vd.IDDEMANDEPRIXFILLE = d.ID
         LEFT JOIN AS_UNITE au ON au.id = vd.unite;

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
    CAST(nvl(p.durre,1)*vd.QTE * NVL(vd.nombre,0) * vd.PU AS NUMBER(30,2)) AS montantAvantRemise,
    p.durre AS dimension
FROM VENTE_DETAILS vd
         LEFT JOIN VENTE_LIB v ON v.ID = vd.IDVENTE
         LEFT JOIN AS_INGREDIENTS_LIB p ON p.ID = vd.IDPRODUIT;

-- clé pour le mois de décembre 2025
INSERT INTO CLEREFERENCE ("ID", "NOMTAB", "NEXTVAL")
VALUES ('202512', 'PROFORMA', 5);
INSERT INTO CLEREFERENCE ("ID", "NOMTAB", "NEXTVAL")
VALUES ('202512', 'VENTE', 3);
