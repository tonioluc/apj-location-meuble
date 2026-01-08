UPDATE menudynamique SET libelle = 'Détails' WHERE id = 'MNDN0000000171';
UPDATE menudynamique SET libelle = 'Détails par modèle' WHERE id = 'MNDN0023061001';
UPDATE menudynamique SET libelle = 'Catégorie de Produit', href = '' WHERE id = 'MNDN00000002111113';
UPDATE menudynamique SET libelle = 'Sous-Catégorie', href = '' WHERE id = 'MNDN000000021111133';
UPDATE menudynamique SET libelle = 'Unité', href = '' WHERE id = 'MNDN00000002111114';
UPDATE menudynamique SET libelle = 'Prévision', href = '' WHERE id = 'MENUDYN002';

-- script pour l'usermenu

DELETE FROM usermenu WHERE id = 'USRMEN0409001'; -- 1 rows
DELETE FROM usermenu WHERE id = 'USRMEN0409002'; -- 1 rows
DELETE FROM usermenu WHERE id = 'USRMEN01404001'; -- 1 rows
DELETE FROM usermenu WHERE id = 'USRMEN0204001'; -- 1 rows
DELETE FROM usermenu WHERE id = 'USRMEN0204006'; -- 1 rows
DELETE FROM usermenu WHERE id = 'USRMEN02'; -- 1 rows
DELETE FROM usermenu WHERE id = 'USRMEN03'; -- 1 rows
DELETE FROM usermenu WHERE id = 'USRMEN04'; -- 1 rows
DELETE FROM usermenu WHERE id = 'USRMEN05'; -- 1 rows
DELETE FROM usermenu WHERE id = 'USRMEN06'; -- 1 rows
DELETE FROM usermenu WHERE id = 'USRMEN07'; -- 1 rows
DELETE FROM usermenu WHERE id = 'USRMEN10'; -- 1 rows
DELETE FROM usermenu WHERE id = 'USRMEN11'; -- 1 rows
DELETE FROM usermenu WHERE id = 'USRMEN0304001'; -- 1 rows
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM7B0ADE6C', 'MNDN000000001', NULL, 'caisse', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM79F2B1E4', 'MNDN000000001', NULL, 'dg', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM3409BAF6', 'MNDN000000001', NULL, 'gestionnaire', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMAAC932FC', 'MNDN000000001', NULL, 'vente', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM8C261F37', 'MNDN000000001', NULL, 'pompiste', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM6352C9BC', 'MNDN000000002', NULL, 'caisse', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UME5532723', 'MNDN000000002', NULL, 'dg', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM54FFB738', 'MNDN000000002', NULL, 'gestionnaire', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM57D1D1FF', 'MNDN000000002', NULL, 'achat', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMCE90F347', 'MNDN000000002', NULL, 'pompiste', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM6A753642', 'MNDN00001', '*', NULL, 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMF55D027D', 'MENUDYN00304001', '*', NULL, 1);
