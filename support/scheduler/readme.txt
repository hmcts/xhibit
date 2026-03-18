This project builds a scheduler, deployable as a web application that makes use of the 
java.util.Timer class. The javadoc should give sufficient detail to make more sophisticated use of 
it, but to get started.

1/ Deploy the WAR file

2/ Implement JavaTask with a class with a no args constructor OR Extend RemoteJavaTask in a Session 
bean remote interface and implement the doTask() method.

3/ Edit the scheduler.properties file to add your new task and set it's schedule.

4/ Ensure that your new JavaTask or the client jar for your Session Bean is on the web-app's class 
path.

5/ Restart the web-app.

Have fun!