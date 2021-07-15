#!/bin/sh

if [[ $# -ge 1 ]]; then
    day=${1}
else
    day=`date +%Y-%m-%d -d "-1 day"`
fi

check_exist()
{
    echo "check if path: "$1" exist..."
    while [[ 1 == 1 ]]
    do
        /usr/local/service/hadoop/bin/hadoop fs -test -e $1/000000_0
        if [[ $? == 0 ]]
        then
            echo "Path "$1" exist, continue"
            break
        fi
        echo "Path "$1" does not exist, wait"
        exit
    done
}

dspBillingPath="/usr/hive/warehouse/dsp_billing/dt=${day}"

echo "dspBillingPath: ${dspBillingPath}"

check_exist ${dspBillingPath}

/usr/local/service/spark/bin/spark-submit \
--master yarn \
--deploy-mode cluster \
--class com.coolpad.bigdata.bi.billing.GenDspBilling \
--driver-memory 1g \
--num-executors 2 \
--executor-memory 1g \
--executor-cores 1 \
bi-spark-1.0-SNAPSHOT.jar \
"--dspBillingPath" ${dspBillingPath}  \
"--date" ${day}
