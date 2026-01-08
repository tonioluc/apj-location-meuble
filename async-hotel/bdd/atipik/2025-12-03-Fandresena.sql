UPDATE AS_INGREDIENTS
SET UNITE = 'UNT000016'
WHERE LIBELLE LIKE '%Tapi%'
   OR LIBELLE LIKE '%Moquette%';

CREATE OR REPLACE VIEW MOUVEMENTCAISSECPL AS
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
       TRIM(REPLACE(c.VAL, 'Caisse', '')) AS idmodepaiementlib
FROM MOUVEMENTCAISSE m
         LEFT JOIN CAISSE c ON c.ID = m.IDCAISSE
         LEFT JOIN VENTE_DETAILS vd ON vd.ID = m.IDVENTEDETAIL
         LEFT JOIN tiers t ON t.ID = m.idtiers
         LEFT JOIN modepaiement mdp ON m.idmodepaiement=mdp.id;