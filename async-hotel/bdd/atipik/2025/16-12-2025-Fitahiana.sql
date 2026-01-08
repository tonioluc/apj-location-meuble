INSERT INTO MENUDYNAMIQUE (ID, LIBELLE, ICONE, HREF, RANG, NIVEAU, ID_PERE) VALUES ('MNDNTABLEANA01', 'Par article', 'fa fa-bar-chart', 'module.jsp?but=vente/tableau-vente-analyse.jsp', 1, 3, 'MNDN000000010');
INSERT INTO MENUDYNAMIQUE (ID, LIBELLE, ICONE, HREF, RANG, NIVEAU, ID_PERE) VALUES ('MNDNTABLEANA02', 'Par client', 'fa fa-bar-chart', 'module.jsp?but=vente/tableau-vente-analyse-cl.jsp', 1, 3, 'MNDN000000010');

create or replace view VENTE_DETAIL_ANALYSE as
select
    vd.IDPRODUIT,i.LIBELLE as idproduitlib,sum(QTE) as duree, count(distinct v.IDCLIENT) as nbclient, sum(vd.QTE*vd.pu*vd.NOMBRE*nvl(i.DURRE,1)) as ca, sysdate as datejour
FROM VENTE_DETAILS vd
         LEFT JOIN VENTE v ON v.ID = vd.IDVENTE
         LEFT JOIN MAGASIN m ON m.ID = v.IDMAGASIN
         LEFT JOIN AS_INGREDIENTS i ON i.ID = vd.IDPRODUIT
         LEFT JOIN POINT p1 ON p1.ID = m.IDPOINT
WHERE v.ETAT >= 11 AND vd.IDPRODUIT!='INGCAUT004' AND vd.IDPRODUIT!='INGCAUT001' AND vd.IDPRODUIT!='INGCAUT002' AND vd.IDPRODUIT!='INGCAUT004' AND v.DATY >= DATE '2020-12-15' AND v.DATY <=  DATE '2025-12-15'
group by vd.IDPRODUIT, i.LIBELLE;


create or replace view VENTE_DETAIL_ANALYSECL as
select
    v.DATY,v.IDCLIENT,cl.NOM as idclientlib, count(distinct vd.IDPRODUIT) as nbarticle, sum(vd.QTE*vd.pu*vd.NOMBRE*nvl(i.DURRE,1)) as ca, sysdate as datejour
FROM VENTE_DETAILS vd
         LEFT JOIN VENTE v ON v.ID = vd.IDVENTE
         LEFT JOIN AS_INGREDIENTS i ON i.ID = vd.IDPRODUIT
         LEFT JOIN CLIENT cl on cl.id=v.IDCLIENT
WHERE v.ETAT >= 11 AND vd.IDPRODUIT!='INGCAUT004' AND vd.IDPRODUIT!='INGCAUT001' AND vd.IDPRODUIT!='INGCAUT002' AND vd.IDPRODUIT!='INGCAUT004' AND v.DATY >= DATE '2020-12-16' AND v.DATY <=  DATE '2025-12-16'
group by v.DATY,v.IDCLIENT,cl.NOM;


create or replace view RESERVATION_LIB_MIN_DATYF as
SELECT
    r.id,
    r.idClient,
    c.NOM AS idclientlib,
    NVL(rd.min_daty, r.daty) AS daty,
    TO_CHAR(NVL(rd.min_daty, r.daty) - 1, 'DD/MM/YYYY') AS datePrevisionDepart,
    r.remarque,
    r.etat,
    CASE
        WHEN r.ETAT = 0 THEN 'ANNULE(E)'
        WHEN r.ETAT = 1 THEN 'CREE(E)'
        WHEN r.ETAT = 11 THEN 'VISE(E)'
        END AS etatlib,
    rm.montanttotal AS montant,
    rm.montantRemise,
    rm.montanttotal,
    rm.MONTANTTVA,
    rm.MONTANTTTC,
    rm.MONTANTPAYE AS paye,
    rm.MONTANTRESTE AS resteAPayer,
    NVL(cau_sum.montantCaution, 0) AS montantCaution,
    cau_last.daty AS datyCaution,
    mc.DEBIT AS debitCaution,
    0 AS revient,
    0 AS marge,
    r.IDORIGINE,
    r.LIEULOCATION,
    m.val AS magasin,
    TO_CHAR(
            NVL(rd.min_daty, r.daty)
                + (s.nbJour-1)
                + CASE WHEN s.nbJour > 1 THEN 1 ELSE 0 END,
            'DD/MM/YYYY'
    ) AS datePrevisionRetour,
    rd.min_daty,
    er.equiperesp,
    er.description AS desceequiperesp,
    p.PERIODE,
    r.numBl,
    '' as modelivraison
FROM RESERVATION r
         LEFT JOIN CLIENT c ON c.id = r.idClient
         LEFT JOIN MV_RESERVATION_MIN_DATY rd ON rd.idmere = r.id
         LEFT JOIN VENTE_CPL rm ON rm.idreservation = r.id
         LEFT JOIN MOUVEMENTCAISSEGROUPERESA mvt ON mvt.IDORIGINE = r.ID
         LEFT JOIN magasin m ON m.id = r.IDMAGASIN
         LEFT JOIN sommeNombreJourReservation s ON r.id = s.idMere
         LEFT JOIN MV_CAUTION_SUM cau_sum ON cau_sum.idreservation = r.id
         LEFT JOIN MV_CAUTION_LAST cau_last ON cau_last.idreservation = r.id
         LEFT JOIN MOUVEMENTCAISSE mc ON mc.IDORIGINE = cau_last.id
         LEFT JOIN MV_EQUIPERESP_LAST er ON er.idreservation = r.id
         LEFT JOIN periodeproforma p ON p.IDMERE = r.IDORIGINE
    /


create or replace view StatistiqueTop as
WITH ventes_agg AS (
    SELECT
        RANG,
        IDCLIENT,
        IDCLIENTLIB,
        CA
    FROM (SELECT ROW_NUMBER() OVER (ORDER BY SUM(MONTANTTOTAL) DESC) AS RANG,
                 IDCLIENT,
                 IDCLIENTLIB,
                 SUM(MONTANTTOTAL)                                   AS CA
          FROM VENTE_CPL
          WHERE ETAT >= 11
            AND (DATY >= to_char(sysdate) AND DATY <= to_char(sysdate))
          GROUP BY IDCLIENT, IDCLIENTLIB
          order by SUM(NVL(MONTANTTOTAL, 0)) desc)
    where RANG<3
),
ventesd_agg AS (
    SELECT
        RANG,
        IDPRODUIT,
        IDPRODUITLIB,
        qteJour
    FROM ( SELECT
            ROW_NUMBER() OVER (ORDER BY SUM(NVL(MONTANT, 0)) DESC) AS RANG,
            IDPRODUIT,
            IDPRODUITLIB,
            SUM(NVL(MONTANT, 0)) AS qteJour
        FROM
            VENTE_DETAILS_CPL
        WHERE
            DATERESERVATION IS NOT NULL
            AND UPPER(TRIM(IDPRODUITLIB)) NOT IN (
                'TRANSPORT ALLER',
                'TRANSPORT PERS',
                'CAUTION',
                'TRANSPORT RETOUR'
            )
        AND (DATERESERVATION>= to_char(sysdate) AND DATERESERVATION<= to_char(sysdate))
        GROUP BY
            IDPRODUITLIB,
            IDPRODUIT
        order by SUM(NVL(MONTANT, 0)) desc)
    where RANG<3
)
select
    vd.IDPRODUIT,vd.IDPRODUITLIB,vd.qteJour as caproduit, v.IDCLIENT,v.IDCLIENTLIB,v.CA as caclient, sysdate as datejour,3 as top
from
    ventes_agg v full join ventesd_agg vd on v.rang=vd.RANG
order by v.RANG asc, vd.RANG asc
;


INSERT INTO MENUDYNAMIQUE (ID, LIBELLE, ICONE, HREF, RANG, NIVEAU, ID_PERE) VALUES ('MENDYNSTAT0001', 'Statistique TOP', 'fa fa-chart-pie', 'module.jsp?but=vente/analyse/analyse-tableau.jsp', 4, 2, 'MENDYN1762162582299338');

create or replace view PROFORMA_CPL_FIL as
SELECT v.ID,
       v.DESIGNATION,
       v.IDMAGASIN,
       m.VAL AS IDMAGASINLIB,
       v.DATY,
       v.REMARQUE,
       v.ETAT,
       CASE
           WHEN v.ETAT = 1 THEN 'CREE'
           WHEN v.ETAT = 11 THEN 'VISEE'
           WHEN v.ETAT = 0 THEN 'ANNULEE'
           END
             AS ETATLIB,
       'AR' as IDDEVISE,
       v.IDCLIENT,
       c.NOM AS IDCLIENTLIB,
       c.ADRESSE,
       c.TELEPHONE AS CONTACT,
       res.MONTANTTTC,
       res.MONTANTPAYE as montantPaye,
       res.MONTANTRESTE as montantreste,
       v.IDRESERVATION,
       v.IDORIGINE,
       v.LIEULOCATION,
       v.remise,
       vpd.DATEDEBUT_MIN as datedebutmin,
       vpd.DATEFIN_MAX as datefinmax,
       p.periode,
       CASE
           WHEN res.MONTANTRESTE = 0 THEN '<span class="badge bg-success fw-normal">PAYE TOTALITE</span>'
           ELSE '<span class="badge bg-danger fw-normal">ACOMPTE</span>'
           END AS etatpaymentlib
FROM PROFORMA v
         LEFT JOIN CLIENT c ON c.ID = v.IDCLIENT
         LEFT JOIN MAGASIN m ON m.ID = v.IDMAGASIN
         left join V_PROFORMAMERE_DATE vpd on vpd.IDMERE = v.ID
         LEFT JOIN RESERVATION r on r.IDORIGINE=v.id
         LEFT JOIN VENTE_CPL res on res.IDRESERVATION=r.id
         left join periodeproforma p on p.IDMERE = v.id;