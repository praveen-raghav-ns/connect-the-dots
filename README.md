# connect-the-dots
EXECUTION:
appletviewer -J-Djava.security.policy=socket.policy GameNow.java //server
appletviewer -J-Djava.security.policy=socket.policy GameNowC.java //client
execute the server code before client code
new Socket("localhost",3333); //line 34 of gameNowC.java. replace "localhost" with the MAC address of the system that acts as a server to play the game in two sytems
