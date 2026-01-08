# Etape 1 - Construction avec Ant
FROM frekele/ant:1.10-jdk8 AS builder

# Définir le répertoire de travail où se trouve le build.xml
WORKDIR /app

# Copier l'ensemble du projet (afin que les chemins relatifs fonctionnent correctement)
COPY . /app

# Exécuter la construction Ant (la cible deploy construit et copie le war explosé)
RUN ant -f build.xml deploy

# Etape 2 - WildFly avec WAR construit
FROM jboss/wildfly:10.1.0.Final

# Répertoire des déploiements WildFly
ENV DEPLOY_DIR=/opt/jboss/wildfly/standalone/deployments

# Copier le WAR explosé depuis l'étape de construction
COPY --from=builder /opt/jboss/wildfly/standalone/deployments/asynclocation.war ${DEPLOY_DIR}/asynclocation.war

# Créer un marqueur afin que WildFly déploie au démarrage
RUN touch ${DEPLOY_DIR}/asynclocation.war.dodeploy

# Exposer les ports nécessaires
EXPOSE 8080 9990

# Démarrer WildFly
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]