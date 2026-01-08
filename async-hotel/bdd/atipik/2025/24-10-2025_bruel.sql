UPDATE MAGASIN2 SET VAL = 'Bureau Centrale', DESCE = 'Bureau Centrale', IDPOINT = 'PNT000103', IDPRODUIT = null, IDTYPEMAGASIN = null, ETAT = 11 WHERE ID = 'PHARM004';
UPDATE MAGASIN2 SET VAL = 'Fabrication 1', DESCE = 'Fabrication 1', IDPOINT = 'PNT000103', IDPRODUIT = null, IDTYPEMAGASIN = null, ETAT = 11 WHERE ID = 'PHARM0044';

delete from POINT where id = 'PNT000123';

UPDATE POINT SET VAL = 'Bel''air', DESCE = 'Bel''air' WHERE ID = 'PNT000122';
UPDATE POINT SET VAL = 'Ankadikely Ilafy', DESCE = 'Ankadikely Ilafy' WHERE ID = 'PNT000124';
UPDATE POINT SET VAL = 'Neurones Ankadivato', DESCE = 'Neurones Ankadivato' WHERE ID = 'PNT000103';
UPDATE POINT SET VAL = 'Domaine sous bois antanambao', DESCE = 'Domaine sous bois antanambao' WHERE ID = 'PNT000084';

INSERT INTO POINT (ID, VAL, DESCE) VALUES ('PNT000086', 'Ankorahotra', 'Ankorahotra');