CREATE OR REPLACE VIEW sommeNombreJourReservation as
SELECT IDMERE, MAX(qte) AS nbjour, MIN(daty) AS datyMin FROM RESERVATIONDETAILS r GROUP BY IDMERE; 

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
       rm.montanttotal,
       rm.montantremise,
       rm.MONTANTTTC,
       rm.MONTANTTVA,
       nvl(mvt.CREDIT, 0)                                        as paye,
       cast(rm.MONTANTTTC - nvl(mvt.CREDIT, 0) as number(20, 2)) as resteAPayer,
       0 as revient,
       0 as marge,
       r.IDORIGINE,
       r.LIEULOCATION,
       m.val as magasin,
       TO_CHAR(s.datyMin + s.nbJour, 'DD/MM/YYYY') AS datePrevisionRetour
FROM RESERVATION r
         LEFT JOIN CLIENT c ON c.id = r.idClient
         LEFT JOIN RESERVATIONMONTANT2 rm ON rm.id = r.id
         left join MOUVEMENTCAISSEGROUPERESA mvt on mvt.IDORIGINE = r.ID
        left join magasin m on m.id = r.IDMAGASIN
       LEFT JOIN sommeNombreJourReservation s ON r.id = s.idMere;

CREATE OR REPLACE VIEW RESERVATION_VERIF_DETAILS_LIB AS 
  SELECT 
  	rv.ID,
	rv.IDRESERVATIONVERIF,
	rv.IDRESERVATIONDETAILS,rv.DATERETOUR,rv.JOUR_RETARD,rv.ETAT_MATERIEL,rv.OBSERVATION,rv.RETENUE ,rl.LIBELLEPRODUIT ,rl.PU ,rl.MONTANT  FROM reservation_verif_details rv left JOIN RESERVATIONDETAILS_LIB rl ON rl.id = rv.IDRESERVATIONDETAILS  
;