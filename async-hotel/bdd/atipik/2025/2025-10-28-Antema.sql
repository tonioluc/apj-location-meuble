CREATE OR REPLACE VIEW CHECKOUTLIB AS
  select
    ch.ID,ci.ID as RESERVATION,ci.IDRESERVATIONMERE,ch.DATY,ch.HEURE,ch.REMARQUE,ch.ETAT,
    ci.PRODUITLIBELLE,
    case when ch.ETAT = 1 then 'CREE'
    when ch.ETAT = 11 then 'VALIDEE'
    END as etatlib,
    ci.kilometragecheckin,
    ch.KILOMETRAGE as kilometragecheckout,
    ci.distancereelle,
    ch.quantite,
    ci.pu,
    ci.idproduit,
    ch.idmagasin,
    mag.val,
    ch.responsable,
    ch.refproduit,
    ch.etat_materiel_lib
from CHECKOUT ch
join CHECKINLIBELLE ci on ci.ID = ch.RESERVATION
LEFT JOIN magasin mag ON ch.idmagasin = mag.id;