CREATE TABLE mailrapport(
    id VARCHAR2(100) PRIMARY KEY,  
    val VARCHAR2(255),
    desce VARCHAR2(255)
);

create sequence seqMailRapport
    minvalue 1
    maxvalue 999999999999
    start with 1
    increment by 1
    cache 20;

CREATE OR REPLACE FUNCTION getseqMailRapport
   RETURN NUMBER
IS
   retour   NUMBER;
BEGIN
SELECT seqMailRapport.NEXTVAL INTO retour FROM DUAL;
RETURN retour;
END;