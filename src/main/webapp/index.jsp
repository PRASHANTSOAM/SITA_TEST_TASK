<!DOCTYPE html>
<html lang="en">
<head>
<title>File Processing Utility Application</title>
</head>
<body>
	<center>
		<h1>Welcome to File Processing Utility</h1>
		<div>
			<h2>
				File Processing Utility Status : <font color="white"
					style="background-color: green">RUNNING</font>
			</h2>
		</div>
	</center>

	<div>
		<hr />
		<h1>File Processing Utility</h1>

		<h2>Task Detail:</h2>
		There will be a series of files placed into the directory (<i>C:\SITA_TEST_TASK\IN</i>)
		with a number on each line. The application is responsible for:

		<ol>
			<li>Application to poll input folder for new files every 5
				seconds and process them.
			<li>To process the file sum all the numbers in the file and
				create a new file containing the resulting value in the directory (<i>C:\SITA_TEST_TASK\OUT</i>)
				with same name as input file but <b>.OUTPUT</b> appended to it.
			<li>When the input file is successfully processed it should be
				moved to the following directory (<i>C:\SITA_TEST_TASK\PROCESSED</i>)
				with <b>.PROCESSED</b> appended to the end of the file name.
			<li>If an error occurs while processing the input file then the
				input file should be moved into the following directory (<i>C:\SITA_TEST_TASK\ERROR</i>)
				with <b>.ERROR</b> appended to the end of the filename.
			<li>Only process files with a .txt extension.
		</ol>

		<h2>Dependencies</h2>
		<ul>
			<li>spring integration framework 4.3.10.RELEASE</li>
			<li>spring framework 4.3.9.RELEASE</li>
			<li>log4j 1.2.17</li>
			<li>junit 4.10</li>
			<li>apache commons 1.3.2</li>
		</ul>

		<b><i>*JDK 1.6 and above is required.</i></b><br /> <b><i>*Application
				is tested on Tomcat and Jetty.</i></b>

		<h2>Maven repository to download dependencies</h2>
		<a href="http://central.maven.org/maven2">http://central.maven.org/maven2</a>

		<h2>Generated Artefact</h2>
		<b><i>.war</i></b> file deployable on Glassfish/Tomcat/Jetty

		<h2>Build the application</h2>
		<ol>
			<li>From the command prompt <b><code>run mvn clean
						install</code></b></li>
		</ol>

		<h2>Assumptions</h2>
		<ol>
			<li>It is assumed that the input files will be placed under <i>C:\SITA_TEST_TASK\IN</i>,
				however we can configure this value in <i>application.properties</i>
				file which is available at <i>src/main/resources</i></li>, same can be
			placed anywhere in classpath say
			<i>%CATALINA_HOME%\lib*</i>.
		</ol>

		<h2>Testing the application.</h2>
		<ol>
			<li>Optionally, input files may be placed under <i>C:\SITA_TEST_TASK\IN</i>
				before deploying the application or file(s) may also be placed after
				application is deployed on any servlet container like tomcat.
			</li>
			<li>To run the application on Tomcat place the generated war
				file in <i>%CATALINA_HOME%\webapps</i> and start Tomcat server.
			</li>
			<li>You may also run the application on provided embedded jetty
				server through maven.</li>
			<li>To run the application on Tomcat from maven, run <b>mvn
					clean install tomcat:run-war</b> command from command prompt.
			</li>
			<li>To run the application on jetty from maven, run <b>mvn
					clean install jetty:run</b> command from command prompt.
			</li>
			<li>To check if application is up and running hit following URL:
				<a href="http://localhost:8080/sita-test-task/">http://localhost:8080/sita-test-task/</a>
			</li>

			<li>Verify the results in <i>C:\SITA_TEST_TASK\OUT</i>, <i>C:\SITA_TEST_TASK\PROCESSED</i>
				and <i>C:\SITA_TEST_TASK\ERROR</i>.
			</li>
		</ol>
		<li>Application Log location
			C:\SITA_TEST_TASK\logs\sitaTestTask.log</li>

		<h2>Process Flow</h2>
		<ol>
			<li>When Tomcat/Glassfish start-up and application is deployed,
				the <i>inbound-channel-adapter</i> will start automatically since we
				have configured <i>auto-startup</i> value to <i>true</i>.
			</li>

			<li>Since the poller is configured it will poll for messages
				from the given input directory with configured interval say 5 sec.</li>

			<li>All the messages one by one sent to <code>processFileChannel</code>
				where we have <i>service-activator</i> and the bean referred for
				this channel is responsible to process the file and generate the
				file content (sum of numbers), it will also put original message in
				the header of newly created message (so that original file may be
				placed in configured folder as <i>PROCESSED</i> after successful
				processing) and will send to <code>messageSplitterInputChannel</code>,
				if there is any error in processing file message will be forwarded
				as is.
			</li>

			<li>On the <code>messageSplitterInputChannel</code>, bean
				referred for this splitter will split the message into new and
				original message and set appropriate header attributes to mark them
				as <code>status=OUTPUT</code> <i>(for New)</i> and <code>status=PROCESSED</code>
				<i>(for Original)</i> and send it to <code>headerValueRouterChannel</code>,
				if original message doesn't exists in the received message header
				then message is considered error message and header attribute as <code>status=ERROR</code>
				is set and sent it to <code>headerValueRouterChannel</code>.
			</li>

			<li>At <code>headerValueRouterChannel</code> it will redirect
				messages with header attributes <code>status=OUTPUT</code> to <code>outputFileChannel</code>,
				with header attributes <code>status=PROCESSED</code> to <code>processedFileChannel</code>
				and with header attributes <code>status=ERROR</code> to <code>errorFileChannel</code>.
			</li>

			<li><code>outputFileChannel</code>, <code>processedFileChannel</code>
				and <code>errorFileChannel</code> are <i>outbound-channel-adapter</i>
				which are responsible to generate the output in the configured
				directory say <i>C:\SITA_TEST_TASK\OUT</i>, <i>C:\SITA_TEST_TASK\PROCESSED</i>
				and <i>C:\SITA_TEST_TASK\ERROR</i> respectively. Since these <i>outbound-channel-adapter</i>
				are configured with <i>file-name-generators</i> these generators add
				<b>.OUTPUT</b>/<b>.PROCESSED</b>/<b>.ERROR</b> at the end of the <i>Output
					file name</i>, <i>Original file name</i> or <i>Error file name</i> as
				the case may be.</li>
		</ol>
	</div>
</body>
</html>
