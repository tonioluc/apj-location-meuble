
CREATE OR REPLACE FUNCTION ATIPIKLOCATIONEVENT.getseq_notification
   RETURN NUMBER
IS
   retour   NUMBER;
BEGIN
   SELECT seq_notification.NEXTVAL INTO retour FROM DUAL;

   RETURN retour;
END;

CREATE TABLE notification2 (
	id varchar(50) NOT NULL,
	receiver int NOT NULL,
	message varchar(250) NULL,
	daty date NULL,
	heure varchar(10) NULL,
	etat int NULL,
	ref varchar(50) NULL,
	lien varchar(100) NULL,
	CONSTRAINT notification_pkey PRIMARY KEY (id),
	CONSTRAINT notification_receiver_fkey FOREIGN KEY (receiver) REFERENCES utilisateur(refuser)
);

CREATE OR REPLACE VIEW notificationlibcplt
AS SELECT n.id,
       n.receiver,
       u.nomuser AS receiverlib,
       n.message,
       n.daty,
       n.heure,
       n.etat,
       n.ref,
       '' AS reflib,
       CASE
         WHEN n.etat = 11 THEN 'LU'
         WHEN n.etat = 1 THEN 'NON LU'
         ELSE NULL
       END AS etatlib,
       n.lien,
       CASE
         WHEN ( (SYSDATE - (TO_DATE(TO_CHAR(n.daty,'YYYY-MM-DD') || ' ' || n.heure, 'YYYY-MM-DD HH24:MI:SS'))) * 24 * 60 * 60 ) < 60
           THEN 'il y a moins d''une minute'
         WHEN ( (SYSDATE - (TO_DATE(TO_CHAR(n.daty,'YYYY-MM-DD') || ' ' || n.heure, 'YYYY-MM-DD HH24:MI:SS'))) * 24 * 60 * 60 ) < 3600
           THEN 'il y a ' || FLOOR(((SYSDATE - (TO_DATE(TO_CHAR(n.daty,'YYYY-MM-DD') || ' ' || n.heure, 'YYYY-MM-DD HH24:MI:SS'))) * 24 * 60)) || ' mn'
         WHEN ( (SYSDATE - (TO_DATE(TO_CHAR(n.daty,'YYYY-MM-DD') || ' ' || n.heure, 'YYYY-MM-DD HH24:MI:SS'))) * 24 * 60 * 60 ) < 86400
           THEN 'il y a ' || FLOOR(((SYSDATE - (TO_DATE(TO_CHAR(n.daty,'YYYY-MM-DD') || ' ' || n.heure, 'YYYY-MM-DD HH24:MI:SS'))) * 24)) || ' h'
         ELSE 'il y a ' || FLOOR((SYSDATE - (TO_DATE(TO_CHAR(n.daty,'YYYY-MM-DD') || ' ' || n.heure, 'YYYY-MM-DD HH24:MI:SS')))) || ' jrs'
       END AS ecartdate
FROM notification2 n
LEFT JOIN utilisateur u ON u.refuser = n.receiver;

CREATE OR REPLACE VIEW notificationlibnonlu
AS SELECT n.id,
       n.receiver,
       n.message,
       n.daty,
       n.heure,
       n.etat,
       n.ref,
       n.etatlib,
       n.receiverlib,
       n.lien,
       n.ecartdate,
       '<a href="XX?but=' || n.lien || '&id=' || n.ref || '">' || n.ref || '</a>' AS lien_html
FROM notificationlibcplt n
WHERE n.etat = 1;


INSERT INTO ATIPIKLOCATIONEVENT.UTILISATEUR
(REFUSER, LOGINUSER, PWDUSER, NOMUSER, ADRUSER, TELUSER, IDROLE)
VALUES(880689, 'vente', 'paop', 'Rivo', 'DIR42', '03444044044', 'vente');
