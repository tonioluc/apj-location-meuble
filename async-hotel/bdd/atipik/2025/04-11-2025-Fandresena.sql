CREATE OR REPLACE VIEW RESERVATION_ETATLOGISTIQUELIB AS
SELECT
    id,
    idClient,
    idclientlib,
    daty,
    datePrevisionDepart,
    remarque,
    etat,
    etatlib,
    montant,
    montantremise,
    montanttotal,
    MONTANTTVA,
    MONTANTTTC,
    paye,
    resteAPayer,
    revient,
    marge,
    IDORIGINE,
    LIEULOCATION,
    magasin,
    datePrevisionRetour,
    equiperesp,
    desceequiperesp,
    numBl,
    qtecheckin,
    qtecheckout,
    etatlogistique,
    CASE
        WHEN re.etatlogistique = 12 THEN '<span class="badge bg-warning fw-normal">A PREPARER</span>'
        WHEN re.etatlogistique = 13 THEN '<span class="badge bg-success fw-normal">EXPEDIEE</span>'
        WHEN re.etatlogistique = 14 THEN '<span class="badge bg-danger fw-normal">BOUCLEE</span>'
        ELSE ''
        END AS etatlogistiquelib,
    CASE
        WHEN re.resteAPayer = 0 THEN 16
        ELSE 17
        END AS etatpayment,
    CASE
        WHEN re.resteAPayer = 0 THEN '<span class="badge bg-success fw-normal">PAYE TOTALITE</span>'
        ELSE '<span class="badge bg-warning fw-normal">ACOMPTE</span>'
        END AS etatpaymentlib
FROM reservation_etatlogistique re;
