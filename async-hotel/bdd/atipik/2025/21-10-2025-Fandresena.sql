ALTER TABLE CATEGORIEINGREDIENT ADD (Prefix VARCHAR(50));

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'FAU'
WHERE ID='CAT0019';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'CPT'
WHERE ID='CAT001';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'MQ'
WHERE ID='CAT0011';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'CBU'
WHERE ID='CAT0013';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'DEC'
WHERE ID='CAT007';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'TSI'
WHERE ID='CAT0010';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'TB'
WHERE ID='CAT0017';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'ML'
WHERE ID='CAT006';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'BC'
WHERE ID='CAT008';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'CNP'
WHERE ID='CAT005';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'CH'
WHERE ID='CAT0015';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'CNP'
WHERE ID='CAT0020';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'TBR'
WHERE ID='CAT0016';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'PB'
WHERE ID='CAT004';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'MEXT'
WHERE ID='CAT009';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'RAG'
WHERE ID='CAT0012';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'CNP'
WHERE ID='CAT0021';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'CHSCA'
WHERE ID='CAT0014';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'SV'
WHERE ID='CAT002';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'TBH'
WHERE ID='CAT0018';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'MEXT'
WHERE ID='CAT0022';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'MQ'
WHERE ID='CAT0023';

UPDATE CATEGORIEINGREDIENT
SET Prefix = 'VT'
WHERE ID='CAT003';

CREATE OR REPLACE VIEW CATEGORIEINGREDIENTPREFIX AS
SELECT
    c.ID,
    c.VAL,
    c.PREFIX,
    COUNT(i.ID) + 1 AS NEXTNUMBER
FROM
    CATEGORIEINGREDIENT c
        LEFT JOIN AS_INGREDIENTS i ON i.CATEGORIEINGREDIENT = c.ID
GROUP BY
    c.ID, c.VAL, c.PREFIX;

INSERT INTO menudynamique (id, libelle, icone, href, rang, niveau, id_pere) VALUES ('MENDYN1761036401604405', 'Produit', 'fa fa-tags', '', 12, 1, NULL);
INSERT INTO menudynamique (id, libelle, icone, href, rang, niveau, id_pere) VALUES ('MENDYN176103646673053', 'Saisie', 'fa fa-plus', 'module.jsp?but=produits/as-ingredients-saisie.jsp', 1, 2, 'MENDYN1761036401604405');
INSERT INTO menudynamique (id, libelle, icone, href, rang, niveau, id_pere) VALUES ('MENDYN1761036517570748', 'Liste', 'fa fa-list', 'module.jsp?but=produits/as-ingredients-liste.jsp', 2, 2, 'MENDYN1761036401604405');
INSERT INTO menudynamique (id, libelle, icone, href, rang, niveau, id_pere) VALUES ('MENDYN1761036566234112', 'Categorie ingredient', 'fa fa-cogs', '', 3, 2, 'MENDYN1761036401604405');
INSERT INTO menudynamique (id, libelle, icone, href, rang, niveau, id_pere) VALUES ('MENDYN1761036588562151', 'Saisie', 'fa fa-plus', 'module.jsp?but=categorieingredient/categorie-ingredient-saisie.jsp', 1, 3, 'MENDYN1761036566234112');
INSERT INTO menudynamique (id, libelle, icone, href, rang, niveau, id_pere) VALUES ('MENDYN1761036620954511', 'Liste', 'fa fa-list', 'module.jsp?but=categorieingredient/categorie-ingredient-liste.jsp', 2, 3, 'MENDYN1761036566234112');

-- script pour l'usermenu

INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM552E001C', 'MENDYN1761036401604405', '*', NULL, 0);


CREATE OR REPLACE VIEW RESERVATIONDETAILS_LIB_MARGE AS
SELECT r.ID,
       r.IDMERE,
       r.QTE,
       r.QTEARTICLE,
       r.remise,
       (((nvl(r.QTEARTICLE,0)*r.QTE * r.PU)*r.remise)/100) as montantremise,
       r.DATY,
       r.IDPRODUIT,
       ai.reference AS referenceproduit,
       ai.LIBELLE                                                                      AS libelleproduit,
       ai.idCATEGORIEINGREDIENT                                                        AS categorieproduit,
       r.PU,
       nvl(r.QTEARTICLE,0)*r.QTE * r.PU  - (((nvl(r.QTEARTICLE,0)*r.QTE * r.PU)*r.remise)/100)                                                                  AS montant,
       ai.tva                                                                          as tva,
       cast(nvl(r.QTEARTICLE,0)*r.qte * r.pu * (nvl(ai.tva, 0) / 100) as number(20, 2))                    as montantTva,
       cast(((nvl(r.QTEARTICLE,0)*r.QTE * r.PU)-nvl(r.QTEARTICLE,0)*r.QTE * r.PU  - (((nvl(r.QTEARTICLE,0)*r.QTE * r.PU)*r.remise)/100)) + (nvl(r.QTEARTICLE,0)*r.QTE * r.PU  - (((nvl(r.QTEARTICLE,0)*r.QTE * r.PU)*r.remise)/100) * (nvl(ai.tva, 0) / 100)) as number(20, 2)) as montantttc,
       ai.CATEGORIEINGREDIENT                                                          AS categorieproduitlib,
       r.heure                                                                         AS heure,
       r.DISTANCEESTIMATION,
       cac.KILOMETRAGECHECKIN,
       cac.KILOMETRAGECHECKOUT,
       nvl(cac.KILOMETRAGECHECKOUT - cac.KILOMETRAGECHECKIN, 0) as distancereelle,
       v.CHARGE_PER_KILOMETRE,
       v.VALEUR_ACTUELLE,
       cast (greatest(nvl(cac.KILOMETRAGECHECKOUT - cac.KILOMETRAGECHECKIN, r.DISTANCEESTIMATION) * v.CHARGE_PER_KILOMETRE,0) as number(30,2)) as revient,
       cast((nvl(r.QTEARTICLE,0)*r.QTE * r.PU) - greatest(nvl(cac.KILOMETRAGECHECKOUT - cac.KILOMETRAGECHECKIN, r.DISTANCEESTIMATION) * v.CHARGE_PER_KILOMETRE,0)as number(30,2)) as marge,
       cac.id as idcheckin,
       cac.CHECKOUT as idcheckout,
       ai.IDVOITURE,
       rm.ETAT,
       r.margemoins,
       r.margeplus,
       ai.image,
       rm.idorigine
FROM RESERVATIONDETAILS r
         LEFT JOIN AS_INGREDIENTS_LIB ai ON ai.id = r.IDPRODUIT
         left join CHECKINAVECCHEKOUT cac on cac.reservation = r.id
         left join voiture v on v.id = ai.idvoiture
         left join reservation rm on rm.id = r.IDMERE;


ALTER TABLE AS_INGREDIENTS ADD (Classification VARCHAR(100));

