#!/bin/bash

cd /Users/hsandeep/Desktop/gitRepos/astroDataGen/astroDataGen/target
/Users/hsandeep/Downloads/spark-2.1.0-bin-hadoop2.7/bin/spark-submit --class org.siemens.spark.PulseSparkHandler --master local[2] /Users/hsandeep/Desktop/gitRepos/astroDataGen/astroDataGen/target/PULSE-0.0.1-SNAPSHOT-jar-with-dependencies.jar