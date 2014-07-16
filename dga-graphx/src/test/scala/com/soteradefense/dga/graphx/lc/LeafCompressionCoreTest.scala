package com.soteradefense.dga.graphx.lc

import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.specs2.mutable._

class LeafCompressionCoreTest extends SpecificationWithJUnit {
  sequential
  "Test" should {
    "Test Leaf Compression Core Compress To None" ! {
      val conf = new SparkConf().setMaster("local").setAppName("WCCTest")
      val data = Array("1,2", "2,3", "3,4", "4,5", "5,6")
      val sc = new SparkContext(conf)
      val rdd = sc.parallelize(data.toSeq)
      val edgeRDD: RDD[Edge[Long]] = rdd.map(f => {
        val tokens = f.split(",")
        new Edge(tokens(0).toLong, tokens(1).toLong)
      })
      val graph = Graph.fromEdges(edgeRDD, None)
      val result = LeafCompressionCore.lc(graph)
      result.vertices.count() == 0
    }
    "Test Leaf Compression Core Compress To Three" ! {
      val conf = new SparkConf().setMaster("local").setAppName("WCCTest")
      val data = Array("1,2","1,3","1,4","1,5","2,3","2,4","2,1", "2,3","3,1", "3,2")
      val sc = new SparkContext(conf)
      val rdd = sc.parallelize(data.toSeq)
      val edgeRDD: RDD[Edge[Long]] = rdd.map(f => {
        val tokens = f.split(",")
        new Edge(tokens(0).toLong, tokens(1).toLong)
      })
      val graph = Graph.fromEdges(edgeRDD, None)
      val result = LeafCompressionCore.lc(graph)
      result.vertices.count() == 3
    }
  }
}
