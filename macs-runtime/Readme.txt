Download your Jade jar files nad place them in the macs/jars folder.
You will need:
jade.jar
XMLCodec.jar.
These jars can be obtained from:
http://jade.tilab.com/
The current system is built with JADE 4.4.0. 
The XMLCodec is the June 2012 version. You will find this in the add-ons section of the Downloads of the jade site.

To run the system on one machine just open a terminal window for each script file.
Run each from a separate field in this order. The system is set up to run a capacitated vechile routing example
startLauncher
agent1
agent2
agent3
agent4

--Waiting time
The launcher agent waits 10 seconds once it has loaded the input files. If you want to increase this time change line 142 of the LaucherAgent.java file. Time is in milliseconds.

--Running on more than one machine
If you want to run each agent on a different machine then open an terminal on each machine.
in the agent1, agent2, agent3 and agent4 scripts change the -local-host 127.0.0.1 setting to 
-hosts %ipaddress of the machine with startlauncher script%


-Linux
If you are running on linux please change your hosts file to the example provided in this directory.
There is a bug in jade/java which means linux that jade does not read the hosts file correctly.
This fix does not affect any other program you might care to use

