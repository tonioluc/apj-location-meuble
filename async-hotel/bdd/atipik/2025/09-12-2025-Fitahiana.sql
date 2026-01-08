
create or replace view RESERVATIONDETAILS_LIB as
SELECT r.ID,
       r.IDMERE,
       r.QTE,
       r.QTEARTICLE,
       r.DATY,
       r.IDVOITURE,
       v.NOM                                                                           AS idVoiturelib,
       r.IDPRODUIT,
       ai.LIBELLE                                                                      AS libelleproduit,
       ai.idCATEGORIEINGREDIENT                                                        AS categorieproduit,
       r.PU,
       nvl(ai.DURRE,1) * r.QTEARTICLE * r.QTE * r.PU                                                                   AS montant,
       nvl(ai.DURRE,1) * r.QTEARTICLE * r.QTE * r.PU * (1-nvl(r.remise/100,0))                                                                    AS montanttotal,
       (nvl(ai.DURRE,1) * r.QTEARTICLE * r.QTE * r.PU) - (nvl(ai.DURRE,1) * r.QTEARTICLE * r.QTE * r.PU * (1-nvl(r.remise/100,0))) as montantremise,
       r.tva                                                                          as tva,
       cast(  (nvl(ai.DURRE,1) * r.QTEARTICLE * r.qte * r.pu)*(1-nvl(r.remise/100,0)) * (nvl(r.tva, 0) / 100) as number(20, 2)  )                    as montantTva,
       cast((nvl(ai.DURRE,1) * r.QTEARTICLE * r.QTE * r.PU)*(1-nvl(r.remise/100,0)) + (nvl(ai.DURRE,1) * r.QTEARTICLE * r.qte * r.pu * (1-nvl(r.remise/100,0)) * (nvl(r.tva, 0) / 100)) as number(20, 2)) as montantttc,
       ai.CATEGORIEINGREDIENT                                                          AS categorieproduitlib,
       r.heure                                                                         AS heure,
       r.remarque,
       r.distanceestimation,
       r.etat
FROM RESERVATIONDETAILS r
         LEFT JOIN AS_INGREDIENTS_LIB ai ON ai.id = r.IDPRODUIT
         LEFT JOIN VOITURE v ON v.ID = r.IDVOITURE
    /



