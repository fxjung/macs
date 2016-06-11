export MACS=$HOME/macs-runtime
export JARS=$MACS/jars
export JARFILES=$JARS/jade.jar:$JARS/XMLCodec.jar:$JARS/macs.jar


export CLASSPATH=$CLASSPATH:$JARFILES

java -Xms256m -Xmx1025m jade.Boot -gui  launcher:macs.agents.LaunchAgent"(vrp, $MACS/config/testfile1VRP.dat, false,false)"
