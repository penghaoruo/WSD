#mvn dependency:copy-dependencies
#mvn compile

MEMORY="-Xmx4g -XX:MaxPermSize=200m"
CP="./target/classes/:./target/dependency/*"
OPTIONS="$MEMORY -cp $CP"
MAIN="cs446.Main"
java $OPTIONS $MAIN $*
