INSERT INTO menudynamique (id, libelle, icone, href, rang, niveau, id_pere) VALUES ('MENDYN1762162582299338', 'Analyse', 'fa fa-chart-bar', '', 14, 1, NULL);
INSERT INTO menudynamique (id, libelle, icone, href, rang, niveau, id_pere) VALUES ('MENDYN1762163047592789', 'Produit', 'fa fa-chart-line', 'module.jsp?but=vente/analyse/analyse-article.jsp', 1, 2, 'MENDYN1762162582299338');
INSERT INTO menudynamique (id, libelle, icone, href, rang, niveau, id_pere) VALUES ('MENDYN1762163146592896', 'Chiffre d''affaires', 'fa fa-chart-line', 'module.jsp?but=vente/analyse/analyse-ca.jsp', 2, 2, 'MENDYN1762162582299338');
INSERT INTO menudynamique (id, libelle, icone, href, rang, niveau, id_pere) VALUES ('MENDYN1762163186640835', 'Client', 'fa fa-chart-line', 'module.jsp?but=vente/analyse/analyse-client.jsp', 3, 2, 'MENDYN1762162582299338');

-- script pour l'usermenu

INSERT INTO usermenu (id, idmenu, refuser, idrole, interdit) VALUES ('UM4CFE204F', 'MENDYN1762162582299338', '*', NULL, 0);
