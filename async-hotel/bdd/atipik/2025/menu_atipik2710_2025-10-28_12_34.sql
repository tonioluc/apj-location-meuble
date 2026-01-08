INSERT INTO menudynamique (id, libelle, icone, href, rang, niveau, id_pere) VALUES ('MENDYN1761644032777620', 'Logistique', 'fa fa-toolbox', '', 13, 1, NULL);
UPDATE menudynamique SET rang = 8 WHERE id = 'MNDN00001';
UPDATE menudynamique SET rang = 9 WHERE id = 'MENDYN1761036401604405';
UPDATE menudynamique SET rang = 10 WHERE id = 'MENUDYN002';
UPDATE menudynamique SET rang = 11 WHERE id = 'MENUDYN00304001';
UPDATE menudynamique SET rang = 12 WHERE id = 'ELM000904001';
UPDATE menudynamique SET rang = 13 WHERE id = 'MNDN000000028';
INSERT INTO menudynamique (id, libelle, icone, href, rang, niveau, id_pere) VALUES ('MENDYN1761644074320852', 'Liste matériel retour', 'fa fa-list', 'module.jsp?but=check/checkout-liste.jsp', 1, 2, 'MENDYN1761644032777620');

-- script pour l'usermenu

DELETE FROM usermenu WHERE id = 'UM48EE8CCD1'; -- 1 rows
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM6A037D8B', 'MENDYN1761644032777620', '*', NULL, 0);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UMD26D3EE7', 'MNDN000000024', NULL, 'caisse', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM4A259C77', 'MNDN000000024', NULL, 'dg', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM8139BC08', 'MNDN000000024', NULL, 'gestionnaire', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM28F5465A', 'MNDN000000024', NULL, 'achat', 1);
INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM282566EC', 'MNDN000000024', NULL, 'pompiste', 1);


UPDATE menudynamique SET icone = 'fa fa-book', href = '#' WHERE id = 'MENDYN1761644032777620';
