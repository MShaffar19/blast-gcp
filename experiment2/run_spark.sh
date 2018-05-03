#!/bin/bash

#
# in another terminal: 'nc -lk 10011' to see the log-output
# in another terminal: 'ncat -lk 10012' to trigger jobs
#
# ports can be changed via test.ini
#
# obtain ncat via 'sudo apt-get install nmap'
#
# on the master-node: 'hadoop fs -ls results' to see produced rdd's
#

EXP_MASTER="--master yarn"
EXP_CLASS="--class gov.nih.nlm.ncbi.exp.EXP_MAIN"
EXP_JAR="target/exp-2-jar-with-dependencies.jar"
EXP_INI="ini.json"

#
# on google-cluster:
#   --num-executers X   : X should match the number or worker-nodes
#   --executor-cores Y  : Y should match the number of vCPU's per worker-node 
#

spark-submit --deploy-mode client $EXP_MASTER $EXP_CLASS $EXP_JAR $EXP_INI

