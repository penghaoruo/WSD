CP="./target/classes/:./target/dependency/*"
OPTIONS="-cp $CP"
MAIN="cs446.dataReader"
java $OPTIONS $MAIN $*
