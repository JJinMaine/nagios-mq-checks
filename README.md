# nagios-mq-checks

Let's be 1000% clear: I'm not a java developer, I'm a sysadmin that uses Nagios and needed to monitor MQ queue depths.

Back in 2013, I found this [MQ Depth Nagios service check](https://exchange.nagios.org/directory/Plugins/Operating-Systems/Linux/check_mq-IBM-WebSphere-MQSeries-Queues-Monitor/details) and I started to use it. Almost immediately the MQ admins started yelling at me for causing a ton of errors in their logs because the service check wasn't properly closing out from the queue / queue manager and simply disconnecting which was a no-no. I was annoyed and embarrased so I tried finding the source code somewhere online but I couldn't. So, I found a java decompiler and decided to try and decompile and fix what I could.  

I was able to get this working properly with a lot of google. I had always meant to put this up in github but I never got around to it until this week. I found the soure code and compiled jar and I'm making it available.

I am not the original author of this service check and I'm not affiliated with Traiana. I just fixed a buggy MQ API call.
  
Here's how you use the mq-utils.jar file: 

1) copy the mq-utils.jar file to your nagios libexec directory.  
2) define a command at checkcommands.cfg:  

`define command{`  
`command_name check_mq`  
`command_line java -jar $USER1$/mq-utils.jar $ARG1$ $ARG2$ $HOSTADDRESS$ $ARG3$ $ARG4$ -w $ARG5$ -c $ARG6$ -debug`  
`}`  

3) the service check command should be:  
  
`check_command check_mq!QMGR!PORT#!CHANNEL!QUEUENAME!WARNING#!CRITICAL#`
  
As with most things on the internet, "it works on my computer" but YMMV. I'm not planning on making any future releases, this was more of a one-time thing. However, feel free to add / cleanup / make a PR if you want - I'm not opposed to putting out new releases.  

Thanks for reading and hope it works for you!



