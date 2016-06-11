set myhome=F:\macs-runtime  
set jars=%myhome%\jars
set jars=%jars%\jade.jar;%jars%\XMLCodec.jar;%jars%\macs.jar

set classpath=%classpath%;%jars%

java -Xms256m -Xmx1025m jade.Boot -gui  launcher:macs.agents.LaunchAgent"(vrp, $MACS/config/testfile1VRP.dat, false,false)"
