ALTER TABLE reservation
  ADD (
    margemoins NUMBER(10,2),
    margeplus  NUMBER(10,2)
  );
  
 ALTER TABLE reservationdetails
  ADD (
    qtearticle NUMBER(38,2)
  );
  
 -----------------------
-- ATIPIKLOCATIONEVENT.RESERVATIONDETCI source

CREATE OR REPLACE VIEW RESERVATIONDETCI as  SELECT
	rd."ID",
	rd."IDMERE",
	rd."IDPRODUIT",
	rd."QTE",
	rd."DATY",
	rd."PU",
	rd."REMARQUE",
	rd."ETAT",
	r."IDCLIENT",
	ci.ID as idCheckIn,
	rd.qtearticle
FROM
	reservationDetails rd
	left JOIN RESERVATION r
	ON rd.IDMERE = r.ID
left join checkInVise ci on ci.RESERVATION=rd.id;

CREATE OR REPLACE
VIEW RESERVATIONDETSANSCI AS 
SELECT
	"ID",
	"IDMERE",
	"IDPRODUIT",
	"QTE",
	"DATY",
	"PU",
	"REMARQUE",
	"ETAT",
	"IDCLIENT",
	"IDCHECKIN",
	qtearticle
FROM
	RESERVATIONDETCI
WHERE
	idCheckIn IS NULL;
 
 CREATE OR REPLACE VIEW RESERVATIONDETSANSCIGROUP as 
 select max(id) as id,
       rd.IDMERE,
	rd.IDPRODUIT,
	sum(rd.QTE) as qte,
	sum(rd.qtearticle) AS qtearticle,
	min(rd.DATY) as daty,
	avg(rd.PU) as pu,
	max(rd.REMARQUE) as remarque,
	max(rd.ETAT) as etat,
	rd.IDCLIENT
from RESERVATIONDETSANSCI rd group by rd.IDMERE,rd.IDPRODUIT,rd.IDCLIENT;
 
 
 CREATE OR REPLACE VIEW RESERVATIONDETSANSCIGROUPLIB as  SELECT 
r."ID",r."IDMERE",r."IDPRODUIT",r."QTE",r.qtearticle,r."DATY",r."PU",r."REMARQUE",r."ETAT",r."IDCLIENT",
ai.libelle as produitlib
FROM RESERVATIONDETSANSCIGROUP r 
LEFT JOIN AS_INGREDIENTS ai ON ai.id = r.idproduit;