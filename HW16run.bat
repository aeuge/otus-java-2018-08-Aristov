set CATALINA_HOME=c:/apps/apache-tomcat-8.5.37
cd HW16 Socket message server
call mvn clean package
call mvn clean install
cd ../HW16 Socket DBServer
call mvn clean package
cd ../HW16 Socket frontend server
call mvn clean package
cd ../HW16 Socket message server
java -jar target/sms-jar-with-dependencies.jar c:/apps/apache-tomcat-8.5.37