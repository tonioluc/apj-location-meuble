ALTER TABLE as_ingredients ADD  filepath varchar2(255);
-- ATIPIKLOCATIONEVENT.AS_INGREDIENTS_LIB source

CREATE OR REPLACE  VIEW AS_INGREDIENTS_LIB (ID, LIBELLE, SEUIL, UNITE, QUANTITEPARPACK, PU, ACTIF, PHOTO, CALORIE, DURRE, COMPOSE, IDCATEGORIEINGREDIENT, CATEGORIEINGREDIENT, IDCATEGORIE, IDFOURNISSEUR, DATY, IDVOITURE, IDVOITURELIB, IMMATRICULATION, BIENOUSERV, ETATLIB, COMPTE_VENTE, COMPTE_ACHAT, PV, TVA,filepath) AS 
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
        ing.filepath
     FROM as_ingredients ing
          JOIN AS_UNITE AU ON ing.UNITE = AU.ID
          JOIN CATEGORIEINGREDIENTLIB catIng
             ON catIng.id = ing.CATEGORIEINGREDIENT
            LEFT JOIN VOITURE v ON v.ID = ing.IDVOITURE;



;