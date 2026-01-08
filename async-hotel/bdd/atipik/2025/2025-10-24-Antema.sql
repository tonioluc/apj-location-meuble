CREATE OR REPLACE VIEW CHECKINFORMU as
SELECT
    c.*,
    '' AS idclientlib
FROM checkin c;


CREATE OR REPLACE VIEW RESERVATIONDETCI AS
SELECT
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
    rd.qtearticle,
    cl.nom as idclientlib
FROM reservationDetails rd
left JOIN RESERVATION r	ON rd.IDMERE = r.ID
left join checkInVise ci on ci.RESERVATION=rd.id
left join client cl on cl.id = r.idclient;

CREATE OR REPLACE VIEW RESERVATIONDETSANSCI AS
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
	qtearticle,
	"IDCLIENTLIB"
FROM
	RESERVATIONDETCI
WHERE
	idCheckIn IS NULL;


CREATE OR REPLACE VIEW RESERVATIONDETSANSCIGROUP AS
select max(id) as id,
    rd.IDMERE,
    rd.IDPRODUIT,
    sum(rd.QTE) as qte,
    sum(rd.qtearticle) AS qtearticle,
    min(rd.DATY) as daty,
    avg(rd.PU) as pu,
    max(rd.REMARQUE) as remarque,
    max(rd.ETAT) as etat,
    rd.IDCLIENT,
    rd.IDCLIENTLIB
from RESERVATIONDETSANSCI rd group by rd.IDMERE,rd.IDPRODUIT,rd.IDCLIENT,rd.IDCLIENTLIB;

CREATE OR REPLACE VIEW RESERVATIONDETSANSCIGROUPLIB AS
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
   r.IDCLIENTLIB
FROM RESERVATIONDETSANSCIGROUP r
LEFT JOIN AS_INGREDIENTS ai ON ai.id = r.idproduit where r.IDPRODUIT not in ('INGCAUT001', 'INGCAUT002', 'INGCAUT003', 'INGCAUT004');

CREATE OR REPLACE VIEW CHECKINSANSCHEKOUT AS
  SELECT
	c.ID,
	c.RESERVATION,
	nvl(rd.IDMERE, c.reservation) AS idReservationMere,
	c.DATY,
	c.HEURE,
	c.REMARQUE,
	c.CLIENT,
	c.IDPRODUIT,
	c.ETAT,
	c.IDCLIENT,
	c.qte,
	ai.libelle AS produitLibelle,
	c.refproduit
FROM
	CHECKIN c
LEFT JOIN RESERVATIONDETAILS rd ON
	c.RESERVATION = rd.ID
LEFT JOIN AS_INGREDIENTS ai ON ai.id = c.idproduit
WHERE
	c.id NOT IN (
	SELECT
		o.reservation
	FROM
		CHECKOUT o
	WHERE
		o.ETAT >= 11);





