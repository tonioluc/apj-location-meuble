UPDATE menudynamique SET rang = 1 WHERE id = 'MNDNAN001';
UPDATE menudynamique SET rang = 2 WHERE id = 'MNDN0000000061';
UPDATE menudynamique SET rang = 3 WHERE id = 'MNDN000000010';
UPDATE menudynamique SET rang = 4 WHERE id = 'MENUDYN14071007';

-- script pour l'usermenu

DELETE FROM usermenu WHERE id = 'USRMEN7624'; -- 1 rows
DELETE FROM usermenu WHERE id = 'USRMEN0904001'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM6352C9BC'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UME5532723'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM54FFB738'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM57D1D1FF'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UMCE90F347'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM2930CC18'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM51D5D3B8'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UMD67972E7'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UMF29B596C'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM4DD063EF'; -- 1 rows
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM7786649A', 'MNDN000000002', '*', NULL, 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM78624850', 'MENUDYN002', '*', NULL, 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMBB682384', 'ELM000904001', '*', NULL, 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM66EF41A5', 'MENUDYN14071007', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM267AB29C', 'MENUDYN14071007', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMB95ACCF3', 'MENUDYN14071007', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM7808FBD4', 'MENUDYN14071007', NULL, 'vente', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM3D2C58C9', 'MENUDYN14071007', NULL, 'pompiste', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMC5458AF6', 'MNDN000000021', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM5CD025C6', 'MNDN000000021', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM6C24AEE6', 'MNDN000000021', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMCC4814C2', 'MNDN000000021', NULL, 'achat', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM42A045C1', 'MNDN000000021', NULL, 'pompiste', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM13C39AC3', 'MNDN0000000211', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM0475E205', 'MNDN0000000211', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMBF22D0C7', 'MNDN0000000211', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMC7925526', 'MNDN0000000211', NULL, 'achat', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM9F1F436C', 'MNDN0000000211', NULL, 'pompiste', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMEB2F6F5E', 'MNDN00000002111', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM15F380EF', 'MNDN00000002111', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMD379EDC6', 'MNDN00000002111', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMB059A208', 'MNDN00000002111', NULL, 'achat', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM02647822', 'MNDN00000002111', NULL, 'pompiste', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMC831A74F', 'MENUDYN0013', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM08A030C6', 'MENUDYN0013', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM257207B3', 'MENUDYN0013', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMD93C8A72', 'MENUDYN0013', NULL, 'achat', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM20EC7573', 'MENUDYN0013', NULL, 'vente', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMBF178435', 'MENUDYN0013', NULL, 'pompiste', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMCFBADDF9', 'MENUDYN0015', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM99A2430B', 'MENUDYN0015', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM1CAAEB2A', 'MENUDYN0015', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM32E9307A', 'MENUDYN0015', NULL, 'achat', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM52111105', 'MENUDYN0015', NULL, 'vente', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM86F1B8C2', 'MENUDYN0015', NULL, 'pompiste', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMC8E6F212', 'MENUDYN0016', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMAA8C3F91', 'MENUDYN0016', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM45A9D886', 'MENUDYN0016', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMD3984C31', 'MENUDYN0016', NULL, 'achat', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM97FA62D8', 'MENUDYN0016', NULL, 'vente', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM251C07F9', 'MENUDYN0016', NULL, 'pompiste', 1);

DELETE FROM usermenu WHERE id = 'USRMEN01'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UMD93C8A72'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UM32E9307A'; -- 1 rows
DELETE FROM usermenu WHERE id = 'UMD3984C31'; -- 1 rows
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM14D4A8C0', 'MENUDYN001', NULL, 'caisse', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMC298E21F', 'MENUDYN001', NULL, 'dg', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMF608A966', 'MENUDYN001', NULL, 'gestionnaire', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM84B3F4EB', 'MENUDYN001', NULL, 'vente', 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM7FEDE814', 'MENUDYN001', NULL, 'pompiste', 0);

DELETE FROM usermenu WHERE id = 'UM552E001C'; -- 1 rows
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM8379CF30', 'MENDYN1761036401604405', '*', NULL, 1);