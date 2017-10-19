/**
 * Created by dan on 03/07/15.
 */

package com.activestandards.spark

import java.io.{File, FileWriter}

import org.apache.spark.{Logging, SparkConf, SparkContext}

object DomainScanner extends App with Logging {
  logInfo("Starting")
  val startTime = System.currentTimeMillis/1000
  val clientName = "airbus"
  val ZONEFILESDIR = "/home/dan/zonefiles"
  val comZonefile = new File(ZONEFILESDIR + "/verisign/com.zone")
  val netZonefile = new File(ZONEFILESDIR + "/verisign/net.zone")
  val allZoneFiles: List[File] = new File(ZONEFILESDIR + "/icann/").listFiles.toList :+ comZonefile :+ netZonefile

  val sparkConf = new SparkConf().setAppName("DomainScanner").setMaster("local[10]")
  val sparkContext = new SparkContext(sparkConf)
  for (file <- allZoneFiles) {
    logInfo("Filename: " + file.getAbsoluteFile)
    val rdd = sparkContext.textFile(file.getAbsolutePath)
      .filter(line => line.toLowerCase.contains(clientName))
      .filter(line => line.toLowerCase.contains(" ns "))
      .map(
      line => {
        val arr = line.split("\\s+") //whitespace
        if (arr(1) == "NS") { // it's .com or .net
          val extn = if (file.getName == "com.zone") arr(0)+".com" else arr(0) + ".net"
          (extn.toLowerCase, arr(2).toLowerCase)
        } else {
          val extn = arr(0).substring(0,arr(0).length-1)
          (extn.toLowerCase, arr(4).toLowerCase)
        }
      }
    ).reduceByKey((a,b) => b) //just one NS is enough
      .collect
      .foreach({ line =>
        println(line)
        val fw = new FileWriter("results.txt", true)
        try {
          val (a,b) = line
          fw.write(a + " " +  b + "\n")
        }
        finally fw.close()
      }
    )
  }

  sparkContext.stop()

  val endTime = System.currentTimeMillis/1000
  var elapsed_s = (endTime - startTime)

  val residual_s = elapsed_s % 60
  val residual_m = (elapsed_s/60) % 60

  val elapsed_h = (elapsed_s/60/60)

  // display hours as absolute, minutes & seconds as residuals
  println("Elapsed time: " + "%02d:%02d:%02d".format(elapsed_h, residual_m, residual_s))

  logInfo("Done.")
}


