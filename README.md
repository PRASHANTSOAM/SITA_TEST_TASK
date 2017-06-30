# SITA_TEST_TASK

## Task Detail: 
There will be a series of files placed into the directory (C:\SITA_TEST_TASK\IN) with a number on each line.
The application is responsible for 
1. Application to poll input folder for new files every 5 seconds and process them.
2. To process the file, sum all the numbers in the file and create a new file containing the resulting value in the directory (*C:\SITA_TEST_TASK\OUT*) with same name as input file but **.OUTPUT** appended to it.
3. When the input file is successfully processed it should be moved to the following directory (*C:\SITA_TEST_TASK\PROCESSED*) with **.PROCESSED** appended to the end of the file name. 
4. If an error occurs while processing the input file then the input file should be moved into the following directory (*C:\SITA_TEST_TASK\ERROR*) with **.ERROR** appended to the end of the filename.
5. Only process files with a .txt extension. 

## Dependencies
* spring integration framework 4.3.10.RELEASE
* spring framework 4.3.9.RELEASE
* log4j 1.2.17
* junit 4.10
* apache commons 1.3.2

JDK 1.6 and above is required.

Application is tested on Tomcat and Jetty.

## Maven repository to download dependencies
http://central.maven.org/maven2

## Generated Artefact
*.war* file deployable on Glassfish/Tomcat/Jetty

## Build the application
1. From the command prompt run mvn clean install

## Note
1. It is assumed that the input files will be placed under *C:\SITA_TEST_TASK\IN*, however we can configure this value in *application.properties* file which is available at *src/main/resources*

## Testing the application.
1. Optionally, input files may be placed under *C:\SITA_TEST_TASK\IN* before deploying the application or file(s) may also be placed after application is deployed on any servlet container like tomcat.
2. To run the application on Tomcat place the generated war file in *%CATALINA_HOME%\webapps* and start Tomcat server. 
3. You may also run the application on provided embedded jetty server through maven.
5. To run the application on Tomcat from maven, run **mvn clean install tomcat:run-war** command from command prompt.
5. To run the application on jetty from maven, run **mvn clean install jetty:run** command from command prompt.
7. To check if application is up and running hit following URL:
http://localhost:8080/sita-test-task/

6. Verify the results in *C:\SITA_TEST_TASK\OUT*, *C:\SITA_TEST_TASK\PROCESSED* and *C:\SITA_TEST_TASK\ERROR*.

## Process Flow
1. When Tomcat/Glassfish start-up and application is deployed, the *inbound-channel-adapter* will start automatically since we have configured auto-startup value to true.

2. Since the poller is configured it will poll for messages from the given input directory with configured interval say 5 sec.

3. All the messages one by one sent to `processFileChannel` where we have *service-activator* and the bean referred for this channel is responsible to process the  file and generate the file content (sum of numbers), it will also put original message in the header of newly created message (so that original file may be placed in configured folder as *PROCESSED* after successful processing) and will send to `messageSplitterInputChannel`, if there  is any error in processing file message will be forwarded as is.

4. On the `messageSplitterInputChannel`, bean referred for this splitter will split the message into new and original message and set appropriate header attributes to mark them as `status=OUTPUT` *(for New)* and `status=PROCESSED` *(for Original)* and send it to `headerValueRouterChannel`, if original message doesn't exists in the received message  header then message is considered error  message and header attribute as `status=ERROR` is set and sent it to `headerValueRouterChannel`.

5. At `headerValueRouterChannel` it will redirect messages with header attributes `status=OUTPUT` to `outputFileChannel`, with header attributes `status=PROCESSED` to `processedFileChannel` and with header attributes `status=ERROR` to `errorFileChannel`.

6. `outputFileChannel`, `processedFileChannel` and `errorFileChannel` are *outbound-channel-adapter* which are responsible to generate the output in the configured directory say *C:\SITA_TEST_TASK\OUT*, *C:\SITA_TEST_TASK\PROCESSED* and *C:\SITA_TEST_TASK\ERROR* respectively.
Since these *outbound-channel-adapter* are configured with *file-name-generators* these generators add **.OUTPUT**/**.PROCESSED**/**.ERROR** at the end of the *Output file name*, *Original file name* or *Error file name* as the case may be.
