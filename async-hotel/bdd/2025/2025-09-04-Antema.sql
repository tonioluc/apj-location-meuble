ALTER TABLE CHECKIN
ADD idmagasin VARCHAR(255);


CREATE OR REPLACE VIEW RESERVATION_LIB AS
  SELECT r.id,
       r.idClient,
       c.NOM                                                     AS idclientlib,
       r.daty,
       r.remarque,
       r.etat,
       CASE
           WHEN r.ETAT = 0
               THEN 'ANNUL&Eacute;(E)'
           WHEN r.ETAT = 1
               THEN 'CR&Eacute;&Eacute;(E)'
           WHEN r.ETAT = 11
               THEN 'VIS&Eacute;(E)'
           END AS etatlib,
       rm.montant,
       rm.MONTANTTTC,
       rm.MONTANTTVA,
       nvl(mvt.CREDIT, 0)                                        as paye,
       cast(rm.MONTANTTTC - nvl(mvt.CREDIT, 0) as number(20, 2)) as resteAPayer,
       0 as revient,
       0 as marge,
       r.IDORIGINE,
       r.LIEULOCATION
FROM RESERVATION r
         LEFT JOIN CLIENT c ON c.id = r.idClient
         LEFT JOIN RESERVATIONMONTANT2 rm ON rm.id = r.id
         left join MOUVEMENTCAISSEGROUPERESA mvt on mvt.IDORIGINE = r.ID;



CREATE OR REPLACE VIEW RESERVATION_VERIFICATIONLIB AS
SELECT
    rv.ID,
    rv.IDRESERVATION,
    rv.DATY,
    rv.OBSERVATION,
    rv.ETAT,
    CASE
        WHEN rv.ETAT = 0
            THEN 'ANNUL&Eacute;(E)'
        WHEN rv.ETAT = 1
            THEN 'CR&Eacute;&Eacute;(E)'
        WHEN rv.ETAT = 11
            THEN 'VIS&Eacute;(E)'
        END AS etatlib,
    rl.IDCLIENT ,
    rl.IDCLIENTLIB ,
    rl.daty AS datereservation,
    rl.MONTANT
FROM RESERVATION_VERIFICATION rv
JOIN RESERVATION_LIB rl ON rl.ID = rv.IDRESERVATION;


CREATE OR REPLACE VIEW FACTUREFOURNISSEURCPL AS
  SELECT f.ID,
       f.IDFOURNISSEUR,
       f2.NOM  AS IDFOURNISSEURLIB,
       f.IDMODEPAIEMENT,
       m.VAL   AS IDMODEPAIEMENTLIB,
       f.DATY,
       f.DESIGNATION,
       f.DATEECHEANCEPAIEMENT,
       f.ETAT,
       CASE
           WHEN f.ETAT = 1
               THEN 'CR&Eacute;&Eacute;(E)'
           WHEN f.ETAT = 0
               THEN 'ANNUL&Eacute;(E)'
           WHEN f.ETAT = 11
               THEN 'VIS&Eacute;(E)'
           END AS ETATLIB,
       f.REFERENCE,
       f.IDBC,
       f.IDMAGASIN,
       f.DEVISE,
       case
           when f.TAUX=0 then 1
           else f.TAUX
        end as taux,
       p.VAL   AS idMagasinLib,
       f3.IDDEVISE,
       cast(f3.MONTANTTVA as number(30,2)) as MONTANTTVA,
       	CAST(f3.MONTANTTTC-f3.MONTANTTVA as number(30,2)) AS MONTANTHT,
          cast(f3.MONTANTTTC as number(30,2)) as montantttc,
          cast(f3.MONTANTTTC*f3.tauxdechange as number(30,2)) as MONTANTTTCAR,
          cast(nvl(mv.debit,0) AS NUMBER(30,2)) AS montantpaye,
          cast(f3.MONTANTTTC-nvl(mv.debit,0) AS NUMBER(30,2)) AS montantreste,
           cast(nvl(f3.tauxdechange,0) AS  NUMBER(30,2)) AS tauxdechange,
           prev.id as idPrevision
FROM FACTUREFOURNISSEUR f
         LEFT JOIN FOURNISSEUR f2 ON f2.ID = f.IDFOURNISSEUR
         LEFT JOIN MODEPAIEMENT m ON m.ID = f.IDMODEPAIEMENT
         LEFT JOIN MAGASIN p ON p.ID = f.IDMAGASIN
         JOIN FACTUREFOURNISSEURMONTANT f3 ON f.ID = f3.id
         LEFT JOIN mouvementcaisseGroupeFacture mv ON f.id=mv.IDORIGINE
         LEFT JOIN prevision prev ON prev.idfacture=f.id;