#!/bin/bash
#java -cp . cs455.overlay.node.Registry
#sleep 10
#java -cp . cs455.overlay.node.MessagingNode
#!/bin/bash
#bash script to start up terminals and ssh to computers.
#These computers’ names are read from a file named comps.


echo 'sshing to pikes'
gnome-terminal --title=pikes -x bash -c "ssh -t pikes 'echo 'hello registry!'; cd CS455/HW1; java -cp . cs455.overlay.node.Registry'" &


sleep 2

for i in `cat ./comps`
do
 echo 'sshing to '${i}
 gnome-terminal --title="${i}" -x bash -c "ssh -t ${i} 'echo 'hello messaging node!'; cd CS455/HW1; java -cp . cs455.overlay.node.MessagingNode'" &
done
