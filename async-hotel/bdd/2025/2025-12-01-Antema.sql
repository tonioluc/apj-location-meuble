ALTER TABLE POINT
ADD RANG NUMBER;

create OR REPLACE VIEW MAGASIN as
select p.ID,
    p.VAL,
    p.DESCE,
    p.id as idPoint,
    P.rang,
    '-' as idProduit,
    '' as idtypemagasin,
    11 as etat
    from point p;

UPDATE ATIPIK.POINT SET VAL = 'RZ_GCM', DESCE = 'Bel''air', RANG = 2 WHERE ID = 'PNT000122';
UPDATE ATIPIK.POINT SET VAL = 'ILAFY', DESCE = 'Ankadikely Ilafy', RANG = 2 WHERE ID = 'PNT000124';
UPDATE ATIPIK.POINT SET VAL = 'ANKADIVATO', DESCE = 'Neurones Ankadivato', RANG = 2 WHERE ID = 'PNT000103';
UPDATE ATIPIK.POINT SET VAL = 'DSB', DESCE = 'Domaine sous bois antanambao', RANG = 2 WHERE ID = 'PNT000084';
UPDATE ATIPIK.POINT SET VAL = 'ATIPIK', DESCE = 'Ankorahotra', RANG = 1 WHERE ID = 'PNT000086';
UPDATE ATIPIK.POINT SET VAL = 'ATELIER', DESCE = 'ATELIER', RANG = 2 WHERE ID = 'PNT000085';