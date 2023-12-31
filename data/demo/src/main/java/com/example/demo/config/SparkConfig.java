package com.example.demo.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

    @Value("${spring.hdfs.baseUrl}")
    private String hdfsBaseUrl;

    @Bean
    public SparkConf sparkConf() {
        SparkConf conf = new SparkConf()
                .setAppName("yarn")
                .set("spark.master", "yarn")
                .set("spark.submit.deployMode", "client")
                .set("spark.yarn.jars", hdfsBaseUrl+"/spark/jars/*.jar")
//                .set("spark.executor.extraJavaOptions","-Dlog4jspark.root.logger=ERROR,console")
//                .set("spark.driver.extraJavaOptions", "-Dorg.slf4j.simpleLogger.defaultLogLevel=ERROR,console")
                .set("spark.dynamicAllocation.enabled", "true");
//                .set("spark.dynamicAllocation.minExecutors", "2")
//                .set("spark.dynamicAllocation.maxExecutors", "4");

//        SparkConf conf = new SparkConf()
//                .setAppName("yarn")
//                .setMaster("local[*]")
//                .set("spark.master", "local")
//                .set("spark.submit.deployMode", "client")
//                .set("spark.yarn.jars", hdfsBaseUrl+"/spark/jars/*.jar")
//                .set("spark.executor.extraJavaOptions","-Dlog4jspark.root.logger=ERROR,console")
//                .set("spark.driver.extraJavaOptions", "-Dorg.slf4j.simpleLogger.defaultLogLevel=ERROR,console");
        return conf;
    }

    @Bean
    public JavaSparkContext javaSparkContext(SparkConf sparkConf) {
        return new JavaSparkContext(sparkConf);
    }

    @Bean
    public SparkSession sparkSession(SparkConf sparkConf) {
        return SparkSession.builder().config(sparkConf).getOrCreate();
    }
}
