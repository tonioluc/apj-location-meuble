CREATE OR REPLACE VIEW CHECKINFORMU_BL AS
SELECT
    c."ID",
    c."RESERVATION",
    c."DATY",
    c."HEURE",
    c."REMARQUE",
    c."CLIENT",
    c."IDPRODUIT",
    c."ETAT",
    c."IDCLIENT",
    c."QTE",
    c."KILOMETRAGE",
    c."IDRESDETAIL",
    c."IDMAGASIN",
    c."RESPONSABLE",
    c."REFPRODUIT",
    c."IDTYPELIVRAISON",
    '' AS idclientlib,
    c."NUMBL",
    ai."IMAGE" AS image
FROM CHECKIN c
         LEFT JOIN AS_INGREDIENTS ai
                   ON c."IDPRODUIT" = ai."ID";

CREATE OR REPLACE VIEW RESTSANSCIGROUPLIB_SANS AS
SELECT r.ID,
       r.IDMERE,
       r.IDPRODUIT,
       ai.reference AS referenceproduit,
       r.QTE,
       r.qtearticle,
       r.DATY,
       r.PU,
       r.REMARQUE,
       r.ETAT,
       r.IDCLIENT,
       ai.libelle as produitlib,
       r.IDCLIENTLIB,
       ai.Image
FROM RESERVATIONDETSANSCIGROUP_SANS r
         LEFT JOIN AS_INGREDIENTS ai ON ai.id = r.idproduit where r.IDPRODUIT not in ('INGCAUT001', 'INGCAUT002', 'INGCAUT003', 'INGCAUT004');