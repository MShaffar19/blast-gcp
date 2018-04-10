# Starting a cluster

From your google cloud shell (the >_) box in upper right, paste
(ctrl-shift-v in my browser, very finicky)
```shell
# Copied from lib_builder/make_cluster.sh
gcloud dataproc --region us-east4 \
    clusters create cluster-$USER-$(date +%Y%m%d) \
    --master-machine-type n1-standard-8 \
        --master-boot-disk-size 100 \
    --num-workers 2 \
    --worker-machine-type n1-standard-32 \
        --worker-boot-disk-size 100 \
    --num-preemptible-workers 2 \
        --preemptible-worker-boot-disk-size 100 \
    --scopes 'https://www.googleapis.com/auth/cloud-platform' \
    --project ncbi-sandbox-blast \
    --labels owner=$USER \
    --region us-east4 \
    --zone   us-east4-b \
    --image-version 1.2 \
    --initialization-action-timeout 30m \
    --initialization-actions \
    "gs://blastgcp-pipeline-test/scripts/cluster_initialize.sh" \
    --tags ${USER}-dataproc-cluster-$(date +%Y%m%d-%H%M%S) \
    --bucket dataproc-3bd9289a-e273-42db-9248-bd33fb5aee33-us-east4
```

* Once cluster has begun, click on [ DataProc ->  Clusters ](https://console.cloud.google.com/dataproc/clusters?project=ncbi-sandbox-blast)
* you should see your cluster "Provisioning" (takes about 10 minutes to copy Blast databases from Google Cloud Storage), and then "Running."
* **DATA LOSS WARNING:** Cluster filesystems, including /home and HDFS, are not persistent, and clusters automatically terminate after 8 hours.

# SSH'ing to your cluster
Click on the cluster name, and then "VM instances", and then click on your
master node and open an SSH session on it. If using another ssh client, note that you want the "External IP", marked "(ephemeral)."

# Checkout git repository
```shell
git clone https://github.com/ncbi/blast-gcp.git
cd blast-gcp
git checkout engineering
```

# Start SPARK
```shell
cd ~/blast-gcp/pipeline
./make_jar.sh
```

* Edit the file 'test.ini' to adjust the settings.
* Edit the script run_spark.sh to adjust local/yarn, number of executors/cores
* In another terminal: ```ncat -lk 10011``` to see the log-output
* In another terminal: ```ncat -lk 10012``` to trigger jobs
* on google-cluster:  --num-executers X   : X should match the number or worker-nodes
  --executor-cores Y  : Y should match the number of vCPU's per worker-node 

```shell
./run_spark.sh
```

In the terminal with "ncat -lk 10012", type in a query ("T1" as an test)

# Viewing results
```console
$ hadoop fs -ls -R results
drwxr-xr-x   - vartanianmh hadoop          0 2018-04-09 21:54 results/1341
-rw-r--r--   2 vartanianmh hadoop          0 2018-04-09 21:54 results/1341/_SUCCESS
-rw-r--r--   2 vartanianmh hadoop          0 2018-04-09 21:54 results/1341/part-00000
-rw-r--r--   2 vartanianmh hadoop          0 2018-04-09 21:54 results/1341/part-00001
-rw-r--r--   2 vartanianmh hadoop         11 2018-04-09 21:54 results/1341/part-00002
-rw-r--r--   2 vartanianmh hadoop          0 2018-04-09 21:54 results/1341/part-00003
drwxr-xr-x   - vartanianmh hadoop          0 2018-04-09 21:53 results/208

$ hadoop fs -cat results/1341/part-00002
( T2,4499)
```

# Shutdown your cluster
**All data not stored in a Google Cloud Storage bucket will be lost.**
```console
gcloud dataproc --region us-east4 clusters list
gcloud dataproc --region us-east4 clusters delete cluster-$USER-...
```
or [ select your cluster ](https://console.cloud.google.com/dataproc/clusters?project=ncbi-sandbox-blast) and press Delete.

