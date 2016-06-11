export MACS=$HOME/macs-runtime
export JARS=$MACS/jars
export JARFILES=$JARS/jade.jar:$JARS/XMLCodec.jar:$JARS/macs.jar


export CLASSPATH=$CLASSPATH:$JARFILES


java -Xms512m -Xmx2048m jade.Boot -local-host 127.0.0.1 -container  agent3:macs.agents.HAgent"(cws, noname, 500, 7, geo,cws,0.3, true,0.25,0.1,2222222)"
