set myhome=F:\macs-runtime  
set jars=%myhome%\jars
set jars=%jars%\jade.jar;%jars%\XMLCodec.jar;%jars%\macs.jar

set classpath=%classpath%;%jars%

java -Xms512m -Xmx2048m jade.Boot -local-host 127.0.0.1  -container agent2:macs.agents.HAgent"(cws, noname, 500, 7, geo, cws, 0.3,true,0.2,0.07,41611944)"