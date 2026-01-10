docker exec -it oracle-db bash

sqlplus system/oracle

CREATE USER atipik IDENTIFIED BY atipik;
GRANT CONNECT, RESOURCE, DBA TO atipik;
ALTER USER atipik DEFAULT TABLESPACE USERS;
ALTER USER atipik TEMPORARY TABLESPACE TEMP;
exit
exit




docker cp /home/antonio/ITU/S5/mr-tahina/apj-location-meuble/atipik0601.dmp oracle-db:/home/oracle/
docker exec -it oracle-db bash
imp atipik/atipik file=/home/oracle/atipik0601.dmp full=yes ignore=yes
