ALTER TABLE DMDACHAT
ADD ETAT NUMBER(*,0);

CREATE OR REPLACE VIEW DMDACHATLIB AS
SELECT
da.id,
da.daty,
da.fournisseur,
f.nom AS fournisseurlib,
da.remarque,
da.etat,
CASE
   WHEN da.ETAT = 0
       THEN 'ANNUL&Eacute;(E)'
   WHEN da.ETAT = 1
       THEN 'CR&Eacute;&Eacute;(E)'
   WHEN da.ETAT = 11
       THEN 'VIS&Eacute;(E)'
   END AS etatlib
FROM DMDACHAT da
LEFT JOIN FOURNISSEUR f ON f.ID = da.fournisseur;

