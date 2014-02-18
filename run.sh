#!/bin/bash
#java -cp . cs455.overlay.node.Registry
#sleep 10
#java -cp . cs455.overlay.node.MessagingNode
#!/bin/bash
#bash script to start up terminals and ssh to computers.
#These computers’ names are read from a file named comps.

for i in `cat ./comps`
do
 echo 'sshing to '${i}
 gnome-terminal --title="${i}"-x bash -c "ssh -t ${i} 'echo 'hello world!'; bash'" &
done
