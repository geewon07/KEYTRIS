package com.example.demo.component;

import com.example.demo.enums.TfidfTrainType;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class ModelLoader {
    private final WordModel wordModel;
    private final TfidfModel tfidfModel;

    private SparkConf sparkConf;
    private JavaSparkContext javaSparkContext;

    public ModelLoader(SparkConf sparkConf, JavaSparkContext javaSparkContext, WordModel wordModel, TfidfModel tfidfModel) {
        this.sparkConf = sparkConf;
        this.javaSparkContext = javaSparkContext;
        this.wordModel = wordModel;
        this.tfidfModel = tfidfModel;
    }

    @PostConstruct
    public void loadModels() throws IOException {

        wordModel.loadWordVectorCache();
        tfidfModel.loadAndTrain(TfidfTrainType.RESTART);
    }
}
