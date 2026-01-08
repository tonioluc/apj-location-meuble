CREATE OR REPLACE VIEW CHECKOUTLIB AS
SELECT
    ch.ID,
    ci.ID AS RESERVATION,
    ci.IDRESERVATIONMERE,
    ch.DATY,
    ch.HEURE,
    ch.REMARQUE,
    ch.ETAT,
    ci.PRODUITLIBELLE,
    CASE
        WHEN ch.ETAT = 1 THEN 'CREE'
        WHEN ch.ETAT = 11 THEN 'VALIDEE'
        END AS etatlib,
    ci.kilometragecheckin,
    ch.KILOMETRAGE AS kilometragecheckout,
    ci.distancereelle,
    ch.quantite,
    ci.pu,
    ci.idproduit,
    ch.idmagasin,
    mag.val,
    ch.responsable,
    ch.refproduit,
    ch.etat_materiel_lib,
    ing.image AS image
FROM CHECKOUT ch
         JOIN CHECKINLIBELLE ci ON ci.ID = ch.RESERVATION
         LEFT JOIN magasin mag ON ch.idmagasin = mag.id
         LEFT JOIN As_ingredients ing ON ing.id = ci.idproduit;

DELETE FROM REPORTCAISSE
WHERE IDCAISSE = 'CAI000262';

DELETE FROM CAISSE
WHERE ID = 'CAI000262';


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
where ai.pv > 0;

CREATE OR REPLACE VIEW CHECKINLIBELLE AS
SELECT
    c.ID,
    c.RESERVATION,
    nvl(r2.ID,c.reservation) AS idReservationMere,
    c.DATY,
    c.HEURE,
    c.REMARQUE,
    cl.nom AS CLIENT,
    c.IDPRODUIT,
    c.ETAT,
    c.IDCLIENT,
    c.CHECKOUT,
    i.LIBELLE AS produitLibelle,
    i.pu,
    i.tva,
    CASE
        WHEN c.ETAT = 0
            THEN 'ANNULEE'
        WHEN c.ETAT = 1
            THEN 'CREE'
        WHEN c.ETAT = 11
            THEN 'VISEE'
        END AS ETATLIB,
    c.kilometragecheckin,
    c.kilometragecheckout,
    c.distancereelle,
    c.quantite,
    u.EQUIVALENCE,
    c.qte,
    c.responsable,
    c.refproduit,
    c.idtypelivraisonlib,
    i.image
FROM
    checkInAvecChekOut c
        LEFT JOIN RESERVATIONDETAILS r ON r.ID = c.RESERVATION
        LEFT JOIN RESERVATION r2 ON r.IDMERE = r2.id
        LEFT JOIN AS_INGREDIENTS i ON c.IDPRODUIT = i.id
        LEFT JOIN CLIENT cl ON c.IDCLIENT=cl.id
        left join AS_UNITE u on i.UNITE=u.ID;

CREATE OR REPLACE VIEW CHECKOUTLIB_CLIENT AS
SELECT
    ch.ID,
    ci.ID AS RESERVATION,
    ci.IDRESERVATIONMERE,
    ch.DATY,
    ch.HEURE,
    ch.REMARQUE,
    ch.ETAT,
    ci.PRODUITLIBELLE,
    CASE
        WHEN ch.ETAT = 1 THEN 'CREE'
        WHEN ch.ETAT = 11 THEN 'VALIDEE'
        END AS etatlib,
    ci.kilometragecheckin,
    ch.KILOMETRAGE AS kilometragecheckout,
    ci.distancereelle,
    ch.quantite,
    ci.pu,
    ci.idproduit,
    ch.idmagasin,
    mag.val,
    ch.responsable,
    ch.refproduit,
    ch.etat_materiel_lib,
    ing.image AS image,
    ci.client
FROM CHECKOUT ch
         JOIN CHECKINLIBELLE ci ON ci.ID = ch.RESERVATION
         LEFT JOIN magasin mag ON ch.idmagasin = mag.id
         LEFT JOIN As_ingredients ing ON ing.id = ci.idproduit;