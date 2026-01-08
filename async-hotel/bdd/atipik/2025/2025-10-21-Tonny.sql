alter table DMDACHAT add (idCommande varchar(100));
alter table DMDACHAT add (idbu varchar(100));
alter table AS_BONDECOMMANDE add (idProformaAchat varchar(100));
alter table AS_BONDECOMMANDE add (idDmdAchat varchar(100));
alter table FACTUREFOURNISSEURFILLE add (mois integer);
alter table FACTUREFOURNISSEURFILLE add (annee integer);
alter table FACTUREFOURNISSEURFILLE add (MONTANTPERTEGAIN NUMBER);





create table BU
(
    ID    VARCHAR2(255) not null
        constraint BU_PK
            primary key,
    VAL   VARCHAR2(255),
    DESCE VARCHAR2(255)
)
/

INSERT INTO BU (ID, VAL, DESCE) VALUES ('BU001', 'Valeur Ajoutée', 'Valeur Ajoutée');
INSERT INTO BU (ID, VAL, DESCE) VALUES ('BU002', 'Confection', 'Confection');
INSERT INTO BU (ID, VAL, DESCE) VALUES ('BU003', 'Raphia', 'Raphia');

-- auto-generated definition
create table PROFORMAACHAT
(
    ID            VARCHAR2(255) not null
        constraint PROFORMAACHAT_PK
            primary key,
    DATY          DATE,
    REMARQUE      VARCHAR2(500),
    IDDMDACHAT    VARCHAR2(255),
    IDFOURNISSEUR VARCHAR2(255),
    ETAT          NUMBER
)
/

-- auto-generated definition
create table PROFORMAACHATDETAIL
(
    ID              VARCHAR2(255) not null
        constraint PROFORMAACHATDETAIL_PK
            primary key,
    IDMERE          VARCHAR2(255)
        constraint PROFORMAACHAT_MEREFILLE_FK
            references PROFORMAACHAT,
    IDDMDACHATFILLE VARCHAR2(255),
    DESIGNATION     VARCHAR2(500),
    QTE             NUMBER,
    PU              NUMBER(30, 5)
)
/

create view PROFORMAACHATDETAILIB as
SELECT
    p.id ,
    d.IDMERE AS iddmdachat,
    p.idmere ,
    d.IDPRODUIT ,
    ai.libelle AS idproduitlib,
    p.qte,
    p.pu,
    p.qte* p.pu AS montant ,
    pm.idfournisseur ,
    f.NOM AS idfournisseurlib
FROM PROFORMAACHATDETAIL p
         LEFT JOIN DMDACHATFILLE d ON d.id = p.iddmdachatfille
         LEFT JOIN PROFORMAACHAT pm ON pm.id = p.idmere
         LEFT JOIN FOURNISSEUR f ON f.id = pm.idfournisseur
         LEFT JOIN AS_INGREDIENTS ai ON ai.ID  = d.IDPRODUIT
/


create view PROFORMAACHATLIB as
SELECT
    p."ID",p."DATY",p."REMARQUE",p."IDDMDACHAT",p."IDFOURNISSEUR",p."ETAT",
    f.nom AS idfournisseurlib
FROM PROFORMAACHAT p
         LEFT JOIN  FOURNISSEUR f ON f.id = p.idfournisseur
/

CREATE SEQUENCE SEQ_achat INCREMENT BY 1 MINVALUE 1 MAXVALUE 999999999999999999999999999 NOCYCLE CACHE 20 NOORDER ;

CREATE OR REPLACE FUNCTION get_seq_dmd_achat_proforma
    RETURN NUMBER IS
    retour NUMBER;
BEGIN
    SELECT SEQ_achat.nextval INTO retour FROM dual;
    return retour;
END;

CREATE SEQUENCE SEQ_achatf INCREMENT BY 1 MINVALUE 1 MAXVALUE 999999999999999999999999999 NOCYCLE CACHE 20 NOORDER ;

CREATE OR REPLACE FUNCTION get_seq_dmd_achat_proforma_fl
    RETURN NUMBER IS
    retour NUMBER;
BEGIN
    SELECT SEQ_achatf.nextval INTO retour FROM dual;
    return retour;
END;

ALTER TABLE AS_BONDECOMMANDE_FILLE
    DROP CONSTRAINT BCF_PRODUIT;

ALTER TABLE AS_BONDECOMMANDE_FILLE
    ADD CONSTRAINT BCF_PRODUIT
        FOREIGN KEY (PRODUIT)
            REFERENCES AS_INGREDIENTS(ID);


create view ST_INGREDIENTSAUTOACHAT_CPL as
select st."ID",st."LIBELLE",st."SEUIL",st."UNITE",st."QUANTITEPARPACK",st."PU",st."ACTIF",st."PHOTO",st."CALORIE",st."DURRE",st."COMPOSE",st."CATEGORIEINGREDIENT",st."IDFOURNISSEUR",st."DATY",st."QTELIMITE",st."PV",st."LIBELLEVENTE",st."SEUILMIN",st."SEUILMAX",st."PUACHATUSD",st."PUACHATEURO",st."PUACHATAUTREDEVISE",st."PUVENTEUSD",st."PUVENTEEURO",st."PUVENTEAUTREDEVISE",st."ISVENTE",st."ISACHAT",st."COMPTE_VENTE",st."COMPTE_ACHAT",st."COMPTE", 1 as taux from ST_INGREDIENTSAUTO st
/

create or replace view AS_BONDELIVRAISON_LIB as
select bl.ID,
       bl.REMARQUE,
       bl.IDBC,
       bl.DATY,
       bl.ETAT,
       bl.MAGASIN,
       m.VAL as MAGASINlib,
       case
           when bl.etat = 1 then 'CR&Eacute;&Eacute;(E)'
           when bl.etat = 11 then 'VIS&Eacute;(E)'
           end as etatlib,
       bl.idfournisseur,
       f.nom AS idfournisseurlib,
       bl.IDFACTUREFOURNISSEUR
from AS_BONDELIVRAISON bl
         left join MAGASIN m on bl.MAGASIN = m.ID
         LEFT JOIN fournisseur f ON f.id=bl.idfournisseur
/

-- auto-generated definition
create table LIAISONFACTUREFOURNISSEURS
(
    ID      VARCHAR2(500) not null
        primary key,
    ID1     VARCHAR2(500),
    ID2     VARCHAR2(500),
    MONTANT NUMBER(30, 2),
    ETAT    NUMBER
)
/


-- auto-generated definition
create table REFERENCEFACTURE
(
    ID        VARCHAR2(255) not null
        constraint REFERENCEFACTURE_PK
            primary key,
    IDFACTURE VARCHAR2(255),
    REFERENCE VARCHAR2(255)
)
/




create view FACTUREFOURNISSEURCPL_LIER as
SELECT
    f.ID,
    f.IDFOURNISSEUR,
    f.IDFOURNISSEURLIB,
    f.IDMODEPAIEMENT,
    f.IDMODEPAIEMENTLIB,
    f.DATY,
    f.DESIGNATION,
    f.DATEECHEANCEPAIEMENT,
    f.ETAT,
    f.ETATLIB,
    f.REFERENCE,
    f.IDBC,
    f.IDMAGASIN,
    f.DEVISE,
    f.TAUX,
    f.IDMAGASINLIB,
    f.IDDEVISE,
    f.MONTANTTVA,
    f.MONTANTHT,
    f.MONTANTTTC,
    f.MONTANTTTCAR,
    f.MONTANTPAYE,
    f.MONTANTRESTE,
    f.TAUXDECHANGE,
    f.IDPREVISION,
    l.id1
FROM
    liaisonfacturefournisseurs l
        LEFT JOIN FACTUREFOURNISSEURCPL f ON
        l.id2 = f.id
/


CREATE SEQUENCE SEQ_REFERENCEFACTURE INCREMENT BY 1 MINVALUE 1 MAXVALUE 999999999999999999999999999 NOCYCLE CACHE 20 NOORDER ;

CREATE OR REPLACE FUNCTION GETSEQREFERENCEFACTURE
    RETURN NUMBER IS
    retour NUMBER;
BEGIN
    SELECT SEQ_REFERENCEFACTURE.nextval INTO retour FROM dual;
    return retour;
END;



CREATE SEQUENCE SEQ_liaison INCREMENT BY 1 MINVALUE 1 MAXVALUE 999999999999999999999999999 NOCYCLE CACHE 20 NOORDER ;

CREATE OR REPLACE FUNCTION getseqliaisonfacturef
    RETURN NUMBER IS
    retour NUMBER;
BEGIN
    SELECT SEQ_liaison.nextval INTO retour FROM dual;
    return retour;
END;

create view FACTUREFOURNISSEUR_MF as
SELECT ff.ID,
       ff.IDFACTUREFOURNISSEUR,
       ff.IDPRODUIT,
       ff.QTE,
       ff.PU,
       ff.REMISES,
       ff.IDBCDETAIL,
       ff.TVA,
       ff.DEVISE,
       ff.TAUXDECHANGE,
       ff.IDDEVISE,
       ff.DESIGNATION,
       ff.COMPTE,
       ff.MONTANTPERTEGAIN,
       f.IDFOURNISSEUR,
       f.ETAT,
       f.DATY
FROM FACTUREFOURNISSEURFILLE ff
         JOIN FACTUREFOURNISSEUR f ON ff.IDFACTUREFOURNISSEUR = f.ID
/

create view PERTE_GRPORIGINE_FACT as
select lf.id1 as idorigine, sum(ffc.MONTANTHT) as montant, sum(ffc.montantHT * ffc.TAUXDECHANGE) as montantar
from LIAISONFACTUREFOURNISSEURS lf
         LEFT JOIN FACTUREFOURNISSEURCPL FFC on FFC.id = lf.id2
group by lf.id1
/

create view FF_GRPMERE as
SELECT
    IDFACTUREFOURNISSEUR,
    sum(qte*pu) AS montanttotal,
    sum(MONTANTPERTEGAIN) AS MONTANTPERTEGAIN
FROM facturefournisseurfille
GROUP BY idfacturefournisseur
/




create view FFAVECPERTE_FF as
SELECT
    ff.ID,
    ff.IDFACTUREFOURNISSEUR,
    ff.IDPRODUIT,
    ff.QTE,
    ff.PU,
    ff.REMISES,
    ff.IDBCDETAIL,
    ff.TVA,
    ff.DEVISE,
    ff.TAUXDECHANGE,
    ff.IDDEVISE,
    ail.LIBELLE as DESIGNATION,
    ff.COMPTE,
    CAST( nvl((CASE WHEN fg.MONTANTPERTEGAIN =0 THEN NULL ELSE  ff.MONTANTPERTEGAIN end),(p.montant*qte*TAUXDECHANGE*ff.pu/fg.montanttotal)) AS NUMBER(30,2) ) AS MONTANTPERTEGAIN ,
    ff.IDFOURNISSEUR,
    ff.ETAT,ff.DATY,
    CAST( (qte*ff.pu*TAUXDECHANGE+nvl((CASE WHEN fg.MONTANTPERTEGAIN =0 THEN NULL ELSE  ff.MONTANTPERTEGAIN end),0))/qte AS NUMBER(30,2) ) AS pupertegain ,
    CAST( (qte*ff.pu*TAUXDECHANGE+nvl((CASE WHEN fg.MONTANTPERTEGAIN =0 THEN NULL ELSE  ff.MONTANTPERTEGAIN end),0)) AS NUMBER(30,2) ) AS montantavecpertegain
FROM
    FACTUREFOURNISSEUR_mf ff
        left JOIN perte_grporigine_fact p ON p.idorigine=ff.idfacturefournisseur
        LEFT JOIN ff_grpmere fg ON fg.idfacturefournisseur=ff.idfacturefournisseur
        left join AS_INGREDIENTS_LIB ail on ail.ID = ff.IDPRODUIT
/

create view FACTUREFOURNISSEUR_PERTE as
SELECT f.ID,
       f.IDFOURNISSEUR,
       f.IDMODEPAIEMENT,
       f.DATY,
       f.DESIGNATION,
       f.DATEECHEANCEPAIEMENT,
       f.ETAT,
       f.REFERENCE,
       f.IDBC,
       f.DEVISE,
       f.TAUX,
       f.IDMAGASIN,
       f.IDDEVISE,
       f.ESTPREVU,
       f.DATYPREVU,
       nvl(p.MONTANTAR, 0) AS MONTANTPERTEGAIN
FROM FACTUREFOURNISSEUR f
         left JOIN perte_grporigine_fact p ON p.idorigine = f.id
/






