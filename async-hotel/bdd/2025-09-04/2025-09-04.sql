ALTER TABLE VENTE_DETAILS  ADD nombre NUMBER(30,2);
ALTER TABLE VENTE_DETAILS ADD idorigine varchar2(100);



CREATE OR REPLACE  VIEW VENTE_DETAILS_SAISIE (ID, IDVENTE, IDPRODUIT, DESIGNATION, IDORIGINE, QTE, PU, REMISE, TVA, PUACHAT, PUVENTE, IDDEVISE, TAUXDECHANGE, COMPTE, datereservation,nombre) AS 
  SELECT 
         id,
         IDVENTE ,
         IDPRODUIT ,
         DESIGNATION ,
         IDORIGINE ,
         qte,
         pu,
         REMISE ,
         TVA ,
         PUACHAT ,
         PUVENTE ,
         IDDEVISE ,
         TAUXDECHANGE ,
         compte,
         DATERESERVATION,
         nombre
         FROM VENTE_DETAILS vd ;

 -- ATIPIKLOCATIONEVENT.CHECKINSANSCHEKOUT source

CREATE OR REPLACE  VIEW CHECKINSANSCHEKOUT  AS 
  SELECT
	c.ID,
	c.RESERVATION,
	nvl(rd.IDMERE, c.reservation) AS idReservationMere,
	c.DATY,
	c.HEURE,
	c.REMARQUE,
	c.CLIENT,
	c.IDPRODUIT,
	c.ETAT,
	c.IDCLIENT,
	c.qte,
	ai.libelle AS produitLibelle
FROM
	CHECKIN c
LEFT JOIN RESERVATIONDETAILS rd ON
	c.RESERVATION = rd.ID
LEFT JOIN AS_INGREDIENTS ai ON ai.id = c.idproduit
WHERE
	c.id NOT IN (
	SELECT
		o.reservation
	FROM
		CHECKOUT o
	WHERE
		o.ETAT >= 11);
		
	
ALTER TABLE CHECKOUT ADD  idmagasin varchar2(100);


-- ATIPIKLOCATIONEVENT.CHECKOUTLIB source

CREATE OR REPLACE  VIEW CHECKOUTLIB (ID, RESERVATION, IDRESERVATIONMERE, DATY, HEURE, REMARQUE, ETAT, PRODUITLIBELLE, ETATLIB, KILOMETRAGECHECKIN, KILOMETRAGECHECKOUT, DISTANCEREELLE, QUANTITE, PU, IDPRODUIT,idmagasin,idmagasinlib) AS 
  select
    ch.ID,ci.ID as RESERVATION,ci.IDRESERVATIONMERE,ch.DATY,ch.HEURE,ch.REMARQUE,ch.ETAT,
    ci.PRODUITLIBELLE,
    case when ch.ETAT = 1 then 'CREE'
    when ch.ETAT = 11 then 'VALIDEE'
    END as etatlib,
    ci.kilometragecheckin,
    ch.KILOMETRAGE as kilometragecheckout,
    ci.distancereelle,
    ch.quantite,
    ci.pu,
    ci.idproduit,
    ch.idmagasin,
    mag.val
from CHECKOUT ch
join CHECKINLIBELLE ci on ci.ID = ch.RESERVATION
LEFT JOIN magasin mag ON ch.idmagasin = mag.id;


CREATE OR REPLACE  VIEW PROFORMADETAILS_CPL (ID, IDPROFORMA, IDPRODUIT, IDORIGINE, QTE, PU, REMISE, TVA, PUACHAT, PUVENTE, IDDEVISE, TAUXDECHANGE, DESIGNATION, COMPTE, PUREVIENT, IDDEMANDEPRIXFILLE, NOMBRE, IDPROFORMALIB, IDPRODUITLIB, PUTOTAL, UNITE, UNITELIB, IMAGE, REMISEMONTANT, MONTANTTOTAL, DATEDEBUT,REFERENCE) AS 
  SELECT
  		vd.ID,vd.IDPROFORMA,vd.IDPRODUIT,vd.IDORIGINE,vd.QTE,vd.PU,vd.REMISE,vd.TVA,vd.PUACHAT,vd.PUVENTE,vd.IDDEVISE,vd.TAUXDECHANGE,vd.DESIGNATION,vd.COMPTE,vd.PUREVIENT,vd.IDDEMANDEPRIXFILLE,
       nvl(vd.nombre,0) as nombre,
  		v.DESIGNATION    AS IDPROFORMALIB,
       p.LIBELLE            AS IDPRODUITLIB,
       cast((nvl(vd.nombre,0)*vd.QTE * vd.PU) as NUMBER(30,2)) AS puTotal,
       vd.unite,
       au.val AS uniteLib,
       p.image,
       cast(((nvl(vd.nombre,0)*vd.QTE * vd.PU)*NVL(vd.remise, 0))/100 as NUMBER(30,2)) AS remisemontant,
       cast((nvl(vd.nombre,0)*vd.QTE * vd.PU)-(nvl(vd.nombre,0)*(vd.QTE * vd.PU)*NVL(vd.remise, 0))/100 as NUMBER(30,2)) as montanttotal,
       vd.datedebut,
       p.reference
--       d2.id AS idDevis
FROM PROFORMA_DETAILS vd
         LEFT JOIN PROFORMA v ON v.ID = vd.IDPROFORMA
         LEFT JOIN AS_INGREDIENTS p ON p.ID = vd.IDPRODUIT
         LEFT JOIN DMDPRIXFILLE d
ON
	vd.IDDEMANDEPRIXFILLE = d.ID
	LEFT JOIN AS_UNITE au ON au.id = vd.unite;

         
CREATE OR REPLACE  VIEW AS_INGREDIENTS_LIB (ID, LIBELLE, SEUIL, UNITE, QUANTITEPARPACK, PU, ACTIF, PHOTO, CALORIE, DURRE, COMPOSE, IDCATEGORIEINGREDIENT, CATEGORIEINGREDIENT, IDCATEGORIE, IDFOURNISSEUR, DATY, IDVOITURE, IDVOITURELIB, IMMATRICULATION, BIENOUSERV, ETATLIB, COMPTE_VENTE, COMPTE_ACHAT, PV, TVA, FILEPATH, IMAGE,REFERENCE) AS 
  SELECT ing.id,
          ing.LIBELLE,
          ing.SEUIL,
          au.VAL AS unite,
          ing.QUANTITEPARPACK,
          ing.pu,
          ing.ACTIF,
          ing.PHOTO,
          ing.CALORIE,
          ing.DURRE,
          ing.COMPOSE,
          cating.id AS IDCATEGORIEINGREDIENT ,
          catIng.VAL AS CATEGORIEINGREDIENT,
          ing.CATEGORIEINGREDIENT AS idcategorie,
          ing.idfournisseur,
          ing.daty,
          ing.idVoiture,
          v.NOM AS idVoiturelib,
          v.NUMERO AS immatriculation,
          catIng.desce as bienOuServ,
	      CASE WHEN ing.id IN (SELECT idproduit FROM INDISPONIBILITE i )
	      THEN 'INDISPONIBLE'
	      WHEN ing.id NOT IN (SELECT idproduit FROM INDISPONIBILITE i )
	      THEN 'DISPONIBLE'
	      END AS etatlib,
        ing.COMPTE_VENTE,
        ing.COMPTE_ACHAT,
        ing.pv,
        ing.tva,
        ing.filepath,
        ing.image,
        ing.reference
     FROM as_ingredients ing
          JOIN AS_UNITE AU ON ing.UNITE = AU.ID
          JOIN CATEGORIEINGREDIENTLIB catIng
             ON catIng.id = ing.CATEGORIEINGREDIENT
            LEFT JOIN VOITURE v ON v.ID = ing.IDVOITURE;

        

            
-- ATIPIKLOCATIONEVENT.PROFORMA_CPL source

-- ATIPIKLOCATIONEVENT.VENTE_DETAILS_CPL source

CREATE OR REPLACE  VIEW VENTE_DETAILS_CPL (ID, IDVENTE, IDVENTELIB, IDPRODUIT, IDPRODUITLIB, IDORIGINE, QTE, PU, MONTANTREMISE, MONTANT, MONTANTTVA, MONTANTTTC, IDDEVISE, TAUXDECHANGE, TVA, IDCLIENT, IDCLIENTLIB, DESIGNATION, PUREVIENT, MONTANTREVIENT, DATERESERVATION, IMAGE, REFERENCE, UNITE, NOMBRE, REMISEMONTANT,REMISE) AS 
  SELECT vd.ID,
          vd.IDVENTE,
          v.DESIGNATION AS IDVENTELIB,
          vd.IDPRODUIT,
          p.LIBELLE AS IDPRODUITLIB,
          vd.IDORIGINE,
          vd.QTE,
          VD.pu
             AS PU,
         	CAST((nvl(vd.remise/100,0))*(vd.QTE*nvl(vd.nombre,0) * vd.PU) AS NUMBER(30,2)) AS montantRemise,
            CAST((1-nvl(vd.remise/100,0))*(vd.QTE*nvl(vd.nombre,0) * vd.PU) AS NUMBER(30,2)) AS montant,
            (nvl(vd.TVA/100,0))*CAST((1-nvl(vd.remise/100,0))*(vd.QTE*nvl(vd.nombre,0) * vd.PU) AS NUMBER(30,2)) AS montanttva,
            (nvl(vd.TVA/100,0))*CAST((1-nvl(vd.remise/100,0))*(vd.QTE*nvl(vd.nombre,0) * vd.PU) AS NUMBER(30,2))+CAST((1-nvl(vd.remise/100,0))*(vd.QTE*nvl(vd.nombre,0) * vd.PU) AS NUMBER(30,2)) as montantttc,
          vd.iddevise AS iddevise,
          vd.tauxDeChange AS tauxDeChange,
          vd.tva AS tva,
          v.idclient,
          v.idclientlib,
          vd.designation,
          vd.PUREVIENT,
          cast(vd.QTE*nvl(vd.nombre,0)*vd.PUREVIENT as NUMBER(20,2)) as montantRevient,
          vd.DATERESERVATION,
          p.image,
          P.reference,
          p.unite,
          vd.nombre,
         cast(((nvl(vd.nombre,0)*vd.QTE * vd.PU)*NVL(vd.remise, 0))/100 as NUMBER(30,2)) AS remisemontant,
         vd.remise
     FROM VENTE_DETAILS vd
          LEFT JOIN VENTE_LIB v ON v.ID = vd.IDVENTE
          LEFT JOIN AS_INGREDIENTS_LIB p ON p.ID = vd.IDPRODUIT;
         
        CREATE OR REPLACE  VIEW PROFORMA_CPL (ID, DESIGNATION, IDMAGASIN, IDMAGASINLIB, DATY, REMARQUE, ETAT, ETATLIB, IDDEVISE, IDCLIENT, IDCLIENTLIB, ADRESSE, CONTACT, MONTANT, MONTANTTOTAL, MONTANTREMISE, MONTANTTVA, MONTANTTTC, MONTANTTTCAR, MONTANTPAYE, MONTANTRESTE, AVOIR, TAUXDECHANGE, MONTANTREVIENT, MARGEBRUTE, IDRESERVATION, IDORIGINE, LIEULOCATION,REMISE) AS 
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
           END
             AS ETATLIB,
       v2.IDDEVISE,
       v.IDCLIENT,
       c.NOM AS IDCLIENTLIB,
       c.ADRESSE,
       c.TELEPHONE AS CONTACT,
       cast(V2.montant as number(30,2)) as montant,
       v2.MONTANTTOTAL,
       cast(V2.montantremise as number(30,2)) as MONTANTREMISE,
       cast(V2.MONTANTTVA as number(30,2)) as MONTANTTVA,
       cast(V2.MONTANTTTC as number(30,2)) as montantttc,
       cast(V2.MONTANTTTCAR as number(30,2)) as MONTANTTTCAR,
       cast(nvl(mv.credit,0)-nvl(ACG.MONTANTPAYE, 0) AS NUMBER(30,2)) AS montantpaye,
       cast(V2.MONTANTTTC-nvl(mv.credit,0)-nvl(ACG.resteapayer_avr, 0) AS NUMBER(30,2)) AS montantreste,
       nvl(ACG.MONTANTTTC_avr, 0)  as avoir,
       v2.tauxDeChange AS tauxDeChange,v2.MONTANTREVIENT,cast((V2.MONTANTTTCAR-v2.MONTANTREVIENT) as number(20,2))  as margeBrute,
       v.IDRESERVATION,
       v.IDORIGINE,
       LIEULOCATION,
       v.remise
FROM PROFORMA v
         LEFT JOIN CLIENT c ON c.ID = v.IDCLIENT
         LEFT JOIN MAGASIN m ON m.ID = v.IDMAGASIN
         JOIN PROFORMAMONTANT2 v2 ON v2.ID = v.ID
         LEFT JOIN mouvementcaisseGroupeFacture mv ON v.id=mv.IDORIGINE
         LEFT JOIN AVOIRFCLIB_CPL_GRP ACG on ACG.IDVENTE = v.ID;