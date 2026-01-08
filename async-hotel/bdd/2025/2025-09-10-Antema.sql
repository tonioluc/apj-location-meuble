CREATE OR REPLACE VIEW V_ETATSTOCK_ING AS
SELECT
    p.ID AS ID,
    p.LIBELLE AS idproduitLib,
    p.CATEGORIEINGREDIENT,
    tp.ID AS idTypeProduit,
    tp.DESCE AS idtypeproduitlib,
    ms.IDMAGASIN,
    mag.VAL AS idmagasinlib,
    TO_DATE('01-01-2001', 'DD-MM-YYYY') AS dateDernierMouvement,
    CAST(ms.quantite AS NUMBER(30,
    2) ) AS QUANTITE,
    CAST(NVL( ms.entree, 0) AS NUMBER(30,
    2)) AS ENTREE,
    CAST(NVL( ms.sortie, 0) AS NUMBER(30,
    2)) AS SORTIE,
    CAST(NVL( ms.quantite, 0) AS NUMBER(30,
    2)) AS reste,
    p.UNITE,
    u.DESCE AS idunitelib,
    CAST(NVL(p.PV, 0) AS NUMBER(30,
    2)) AS PUVENTE,
    mag.IDPOINT,
    mag.IDTYPEMAGASIN,
    p.SEUILMIN,
    p.SEUILMAX,
    CAST(ms.montantEntree AS NUMBER(30,
    2)) AS montantEntree,
    CAST(ms.montantSortie AS NUMBER(30,
    2)) AS montantSortie,
    CAST(p.pu AS NUMBER(30,
    2)) AS pu,
    CAST(ms.montant AS NUMBER(30,
    2)) AS montantReste
FROM
	AS_INGREDIENTS p
LEFT JOIN MONTANT_STOCK ms ON
	ms.IDPRODUIT = p.ID
LEFT JOIN CATEGORIEINGREDIENT tp ON
	p.CATEGORIEINGREDIENT = tp.ID
LEFT JOIN MAGASINPOINT mag ON
	ms.IDMAGASIN = mag.ID
LEFT JOIN AS_UNITE u ON
	p.UNITE = u.ID
WHERE
	NVL(ms.ENTREE, 0)>0
	OR NVL(ms.SORTIE, 0)>0;



CREATE OR REPLACE VIEW FACTUREFOURNISSEURCPL AS
SELECT
    f.ID,
    f.IDFOURNISSEUR,
    f2.NOM  AS IDFOURNISSEURLIB,
    f.IDMODEPAIEMENT,
    m.VAL   AS IDMODEPAIEMENTLIB,
    f.DATY,
    f.DESIGNATION,
    f.DATEECHEANCEPAIEMENT,
    f.ETAT,
    CASE
    WHEN f.ETAT = 1
    THEN 'CR&Eacute;&Eacute;(E)'
    WHEN f.ETAT = 0
    THEN 'ANNUL&Eacute;(E)'
    WHEN f.ETAT = 11
    THEN 'VIS&Eacute;(E)'
    END AS ETATLIB,
    f.REFERENCE,
    f.IDBC,
    f.IDMAGASIN,
    f.DEVISE,
    case
    when f.TAUX=0 then 1
    else f.TAUX
    end as taux,
    p.VAL   AS idMagasinLib,
    f3.IDDEVISE,
    cast(f3.MONTANTTVA as number(30,2)) as MONTANTTVA,
    CAST(f3.MONTANTTTC-f3.MONTANTTVA as number(30,2)) AS MONTANTHT,
    cast(f3.MONTANTTTC as number(30,2)) as montantttc,
    cast(f3.MONTANTTTC*f3.tauxdechange as number(30,2)) as MONTANTTTCAR,
    cast(nvl(mv.debit,0) AS NUMBER(30,2)) AS montantpaye,
    cast(f3.MONTANTTTC-nvl(mv.debit,0) AS NUMBER(30,2)) AS montantreste,
    cast(nvl(f3.tauxdechange,0) AS  NUMBER(30,2)) AS tauxdechange,
    prev.id as idPrevision,
    f.DATYPREVU
FROM FACTUREFOURNISSEUR f
    LEFT JOIN FOURNISSEUR f2 ON f2.ID = f.IDFOURNISSEUR
    LEFT JOIN MODEPAIEMENT m ON m.ID = f.IDMODEPAIEMENT
    LEFT JOIN MAGASIN p ON p.ID = f.IDMAGASIN
    JOIN FACTUREFOURNISSEURMONTANT f3 ON f.ID = f3.id
    LEFT JOIN mouvementcaisseGroupeFacture mv ON f.id=mv.IDORIGINE
    LEFT JOIN prevision prev ON prev.idfacture=f.id;



INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM7B0ADE69', 'MENUDYN0204006', NULL, 'dg', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM7B0ADEV9', 'MENUDYN0204006', NULL, 'vente', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM7B0ADEA9', 'MENUDYN0204006', NULL, 'achat', 0);

DELETE FROM USERMENU WHERE ID='USRMEN01104001';



INSERT INTO usermenu (ID, REFUSER, IDMENU, IDROLE, CODESERVICE, CODEDIR, INTERDIT)
VALUES('UANT90F347', '*', 'MNDNB0000000014', NULL, NULL, NULL, 1);

INSERT INTO usermenu (ID, REFUSER, IDMENU, IDROLE, CODESERVICE, CODEDIR, INTERDIT)
VALUES('U90F3ANT47', '*', 'MNDNB000000001', NULL, NULL, NULL, 1);

INSERT INTO usermenu (ID, REFUSER, IDMENU, IDROLE, CODESERVICE, CODEDIR, INTERDIT)
VALUES('U90F347ANT', '*', 'ELM001504001', NULL, NULL, NULL, 1);
