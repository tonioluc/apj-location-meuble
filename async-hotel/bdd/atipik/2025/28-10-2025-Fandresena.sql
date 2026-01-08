CREATE OR REPLACE VIEW PROFORMADETAILS_CPLIMAGE AS
SELECT
    vd.*,
    p.image
FROM PROFORMA_DETAILS vd
         LEFT JOIN AS_INGREDIENTS p ON p.ID = vd.IDPRODUIT;

CREATE OR REPLACE VIEW ST_INGREDIENTSAUTOVENTE_MIMAGE AS
SELECT ai.ID,
       ai.LIBELLE,
       ai.SEUIL,
       ai.UNITE,
       ai.QUANTITEPARPACK,
       ai.PU,
       ai.ACTIF,
       ai.PHOTO,
       ai.CALORIE,
       ai.DURRE,
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
where ai.pv > 0 and ai.IDMODELE = ai.id;



CREATE OR REPLACE VIEW RESERVATIONDETSANSCIGROUPLIB_SANS AS
SELECT r.ID,
       r.IDMERE,
       r.IDPRODUIT,
       ai.reference AS referenceproduit,
       r.QTE,
       r.qtearticle,
       r.DATY,
       r.PU,
       r.REMARQUE,
       r.ETAT,
       r.IDCLIENT,
       ai.libelle as produitlib,
       r.IDCLIENTLIB
FROM RESERVATIONDETSANSCIGROUP_SANS r
         LEFT JOIN AS_INGREDIENTS ai ON ai.id = r.idproduit where r.IDPRODUIT not in ('INGCAUT001', 'INGCAUT002', 'INGCAUT003', 'INGCAUT004');

CREATE OR REPLACE VIEW RESERVATIONDETSANSCIGROUP_SANS AS
select max(id) as id,
       rd.IDMERE,
       rd.IDPRODUIT,
       sum(rd.QTE) as qte,
       sum(rd.qtearticle) AS qtearticle,
       min(rd.DATY) as daty,
       avg(rd.PU) as pu,
       max(rd.REMARQUE) as remarque,
       max(rd.ETAT) as etat,
       rd.IDCLIENT,
       rd.IDCLIENTLIB
from RESERVATIONDETSANSCI_SANS rd group by rd.IDMERE,rd.IDPRODUIT,rd.IDCLIENT,rd.IDCLIENTLIB;

CREATE OR REPLACE VIEW RESERVATIONDETSANSCI_SANS AS
SELECT
    "ID",
    "IDMERE",
    "IDPRODUIT",
    "QTE",
    "DATY",
    "PU",
    "REMARQUE",
    "ETAT",
    "IDCLIENT",
    "IDCHECKIN",
    qtearticle,
    "IDCLIENTLIB"
FROM
    RESERVATIONDETCI;

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
        LEFT JOIN RESERVATION_LIB_MIN_DATY rlmd ON c.numBl = rlmd.numBl;

CREATE OR REPLACE VIEW  ST_INGREDIENTSAUTO AS
SELECT
    ai."ID",ai."LIBELLE",ai."SEUIL",ai."UNITE",ai."QUANTITEPARPACK",ai."PU",ai."ACTIF",ai."PHOTO",ai."CALORIE",ai."DURRE",ai."COMPOSE",ai."CATEGORIEINGREDIENT",ai."IDFOURNISSEUR",ai."DATY",ai."QTELIMITE",ai."PV",ai."LIBELLEVENTE",ai."SEUILMIN",ai."SEUILMAX",ai."PUACHATUSD",ai."PUACHATEURO",ai."PUACHATAUTREDEVISE",ai."PUVENTEUSD",ai."PUVENTEEURO",ai."PUVENTEAUTREDEVISE",ai."ISVENTE",ai."ISACHAT",ai."COMPTE_VENTE",ai."COMPTE_ACHAT", ai.REFERENCE,
    CASE
        WHEN ai.COMPTE_VENTE IS NOT NULL THEN ai.COMPTE_VENTE
        ELSE ai.COMPTE_ACHAT
        END AS compte
FROM AS_INGREDIENTS ai;

CREATE OR REPLACE VIEW PROFORMA_CPL_NUM AS
SELECT v.ID,
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
       c.ADRESSE,
       c.TELEPHONE AS CONTACT,
       CAST(V2.montant AS NUMBER(30,2)) AS montant,
       v2.MONTANTTOTAL,
       CAST(V2.montantremise AS NUMBER(30,2)) AS MONTANTREMISE,
       CAST(V2.MONTANTTVA AS NUMBER(30,2)) AS MONTANTTVA,
       CAST(V2.MONTANTTTC AS NUMBER(30,2)) AS montantttc,
       CAST(V2.MONTANTTTCAR AS NUMBER(30,2)) AS MONTANTTTCAR,
       CAST(NVL(mv.credit,0) - NVL(ACG.MONTANTPAYE, 0) AS NUMBER(30,2)) AS montantpaye,
       CAST(V2.MONTANTTTC - NVL(mv.credit,0) - NVL(ACG.resteapayer_avr, 0) AS NUMBER(30,2)) AS montantreste,
       NVL(ACG.MONTANTTTC_avr, 0) AS avoir,
       v2.tauxDeChange AS tauxDeChange,
       v2.MONTANTREVIENT,
       CAST((V2.MONTANTTTCAR - v2.MONTANTREVIENT) AS NUMBER(20,2)) AS margeBrute,
       v.IDRESERVATION,
       v.IDORIGINE,
       v.LIEULOCATION,  -- ← AJOUTÉ v. ICI
       v.remise,
       vpd.DATEDEBUT_MIN AS datedebutmin,
       vpd.DATEFIN_MAX AS datefinmax,
       p.periode,
       v.NUMPROFORMA
FROM PROFORMA v
         LEFT JOIN CLIENT c ON c.ID = v.IDCLIENT
         LEFT JOIN MAGASIN m ON m.ID = v.IDMAGASIN
         JOIN PROFORMAMONTANT2 v2 ON v2.ID = v.ID
         LEFT JOIN MOUVEMENTCAISSEGROUPEprof mv ON v.id = mv.IDORIGINE
         LEFT JOIN AVOIRFCLIB_CPL_GRP ACG ON ACG.IDVENTE = v.ID
         LEFT JOIN V_PROFORMAMERE_DATE vpd ON vpd.IDMERE = v.ID
         LEFT JOIN periodeproforma p ON p.IDMERE = v.id;

CREATE OR REPLACE VIEW VENTE_CPL_NUM AS
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
    v.NUMFACTURE
FROM VENTE v
         LEFT JOIN CLIENT c ON c.ID = v.IDCLIENT
         LEFT JOIN MAGASIN m ON m.ID = v.IDMAGASIN
         JOIN VENTEMONTANT v2 ON v2.ID = v.ID
         LEFT JOIN MOUVEMENTCAISSEGROUPEFACTURE mv ON v.ID = mv.IDORIGINE
         LEFT JOIN AVOIRFCLIB_CPL_GRP ACG ON ACG.IDVENTE = v.ID
         LEFT JOIN RESERVATION r ON r.ID = v.IDORIGINE
         LEFT JOIN PROFORMA_CPL p ON p.ID = r.IDORIGINE;

UPDATE menudynamique SET libelle = 'État de Caisse' WHERE id = 'MENUDYN0011';
UPDATE menudynamique SET libelle = 'Catégorie produit', href = '' WHERE id = 'MENDYN1761036566234112';

DELETE FROM usermenu WHERE id = 'UM7B0ADE69'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM7B0ADEV9'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM7B0ADEA9'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UANT90F347'; -- 1 rows
DELETE FROM usermenu WHERE id = 'U90F3ANT47'; -- 1 rows
DELETE FROM usermenu WHERE id = 'U90F347ANT'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM13C39AC3'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM0475E205'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UMBF22D0C7'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UMC7925526'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM9F1F436C'; -- 1 rows
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM98EB05F3', 'ELM001104001', '*', NULL, 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM67864571', 'ELM001504001', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMC7397659', 'ELM001504001', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM7F49326C', 'ELM001504001', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UME019A40E', 'ELM001504001', NULL, 'achat', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM5856DC9C', 'ELM001504001', NULL, 'vente', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM31D1A9CB', 'ELM001504001', NULL, 'pompiste', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM3B589844', 'MNDNB000000001', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM207FC1DF', 'MNDNB000000001', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM832DC803', 'MNDNB000000001', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM8A13D4F4', 'MNDNB000000001', NULL, 'vente', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMBFBA5391', 'MNDNB000000001', NULL, 'pompiste', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM21D5648D', 'MENUDYN00171', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM20449E3C', 'MENUDYN00171', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM864C1365', 'MENUDYN00171', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM472737DB', 'MENUDYN00171', NULL, 'vente', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM03533DFF', 'MENUDYN00171', NULL, 'pompiste', 1);

DELETE FROM usermenu WHERE id = 'UM8379CF30'; -- 1 rows
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM48EE8CCD', 'MENDYN1761036401604405', '*', NULL, 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UME1D53E31', 'MENDYN176103646673053', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM1C59CFAE', 'MENDYN176103646673053', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM7EBAAB82', 'MENDYN176103646673053', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM111F9B21', 'MENDYN176103646673053', NULL, 'achat', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UME57A66D3', 'MENDYN176103646673053', NULL, 'vente', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM794B8995', 'MENDYN176103646673053', NULL, 'pompiste', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM9E558ACA', 'MENDYN1761036517570748', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM9397105A', 'MENDYN1761036517570748', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMA8CC8F2B', 'MENDYN1761036517570748', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM626B5808', 'MENDYN1761036517570748', NULL, 'achat', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMB5DAA5EE', 'MENDYN1761036517570748', NULL, 'vente', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM1D9C61F6', 'MENDYN1761036517570748', NULL, 'pompiste', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM893DD0A5', 'MENDYN1761036566234112', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM9B467875', 'MENDYN1761036566234112', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM2D2B1CC9', 'MENDYN1761036566234112', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM10B884BC', 'MENDYN1761036566234112', NULL, 'achat', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM6FD7F2BF', 'MENDYN1761036566234112', NULL, 'vente', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM609949E4', 'MENDYN1761036566234112', NULL, 'pompiste', 1);

DELETE FROM usermenu WHERE id = 'UME1D53E31'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM1C59CFAE'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM7EBAAB82'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM111F9B21'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UME57A66D3'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM794B8995'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM9E558ACA'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM9397105A'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UMA8CC8F2B'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM626B5808'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UMB5DAA5EE'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM1D9C61F6'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM893DD0A5'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM9B467875'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM2D2B1CC9'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM10B884BC'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM6FD7F2BF'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM609949E4'; -- 1 rows

ALTER TABLE CHECKOUT ADD (etat_materiel_lib VARCHAR(100))