CREATE OR REPLACE  VIEW MOUVEMENTCAISSEGROUPEprof (IDORIGINE, CREDIT, DEBIT, IDDEVISE) AS 
  select idproforma, sum(credit) as credit,sum(debit) as debit, C.IDDEVISE from mouvementcaisse m
LEFT JOIN caisse c on  c.id=idcaisse
  where m.etat>=11 and idproforma is not null
group by idproforma,c.iddevise
;

-- ATIPIK2509.PROFORMA_CPL source

CREATE OR REPLACE  VIEW PROFORMA_CPL (ID, DESIGNATION, IDMAGASIN, IDMAGASINLIB, DATY, REMARQUE, ETAT, ETATLIB, IDDEVISE, IDCLIENT, IDCLIENTLIB, ADRESSE, CONTACT, MONTANT, MONTANTTOTAL, MONTANTREMISE, MONTANTTVA, MONTANTTTC, MONTANTTTCAR, MONTANTPAYE, MONTANTRESTE, AVOIR, TAUXDECHANGE, MONTANTREVIENT, MARGEBRUTE, IDRESERVATION, IDORIGINE, LIEULOCATION, REMISE, DATEDEBUTMIN, DATEFINMAX, PERIODE) AS 
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
       v.remise,
       vpd.DATEDEBUT_MIN as datedebutmin,
       vpd.DATEFIN_MAX as datefinmax,
       p.periode
FROM PROFORMA v
         LEFT JOIN CLIENT c ON c.ID = v.IDCLIENT
         LEFT JOIN MAGASIN m ON m.ID = v.IDMAGASIN
         JOIN PROFORMAMONTANT2 v2 ON v2.ID = v.ID
         LEFT JOIN MOUVEMENTCAISSEGROUPEprof mv ON v.id=mv.IDORIGINE
         LEFT JOIN AVOIRFCLIB_CPL_GRP ACG on ACG.IDVENTE = v.ID
    left join V_PROFORMAMERE_DATE vpd on vpd.IDMERE = v.ID
left join periodeproforma p on p.IDMERE = v.id;


-- ATIPIK2509.MOUVEMENTCAISSECPL source

ALTER TABLE mouvementcaisse  ADD idmodepaiement varchar2(100);

CREATE OR REPLACE  VIEW MOUVEMENTCAISSECPL (ID, DESIGNATION, IDCAISSE, IDCAISSELIB, IDVENTEDETAIL, IDVIREMENT, DEBIT, CREDIT, DATY, ETAT, ETATLIB, IDVENTE, IDORIGINE, IDTIERS, TIERS, IDPREVISION, IDOP, TAUX, COMPTE, IDDEVISE,IDMODEPAIEMENT,IDMODEPAIEMENTLIB) AS 
  SELECT m.ID,
       m.DESIGNATION,
       m.IDCAISSE,
       c.VAL   AS IDCAISSELIB,
       m.IDVENTEDETAIL,
       m.IDVIREMENT,
       m.DEBIT,
       m.CREDIT,
       m.DATY,
       m.ETAT,
       CASE
           WHEN m.ETAT = 0
               THEN 'Annul&eacute;(e)'
           WHEN m.ETAT = 1
               THEN 'Cr&eacute;&eacute;(e)'
           WHEN m.ETAT = 11
               THEN 'Vis&eacute;(e)'
           END AS ETATLIB,
       vd.IDVENTE,
       m.IDORIGINE,
       m.idtiers,
       t.NOM AS tiers,
       m.idPrevision,
       m.idOP,
       m.taux,
       m.COMPTE,
       m.IDDEVISE,
       m.idmodepaiement,
       mdp.val
FROM MOUVEMENTCAISSE m
         LEFT JOIN CAISSE c ON c.ID = m.IDCAISSE
         LEFT JOIN VENTE_DETAILS vd ON vd.ID = m.IDVENTEDETAIL
         LEFT JOIN tiers t ON t.ID = m.idtiers
		LEFT JOIN modepaiement mdp ON m.idmodepaiement=mdp.id;

-- ATIPIK2509.MOUVEMENTCAISSECPL source

