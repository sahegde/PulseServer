#!/bin/bash

cd C:/SwathiFinalDemo/AstroPulseMachineLearning/target
..\spark-2.1.0-bin-hadoop2.7\bin\spark-submit.cmd --class org.siemens.spark.PulseSparkHandler --master local[2] PULSE-0.0.1-SNAPSHOT-jar-with-dependencies.jar