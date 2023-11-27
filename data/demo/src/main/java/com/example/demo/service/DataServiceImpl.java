package com.example.demo.service;


import java.io.IOException;
import java.util.*;

import com.example.demo.component.TfidfModel;
import com.example.demo.component.WordModel;
import com.example.demo.enums.ListType;
import com.example.demo.enums.TfidfTrainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DataServiceImpl implements DataService {

    private final WordModel wordModel;
    private final TfidfModel tfidfModel;

    @Override
    public int train() throws IOException {
        int resultTime = wordModel.train();
        tfidfModel.loadAndTrain(TfidfTrainType.RETRAIN);
        return resultTime;
    }

    @Override
    public List<List<Object>> guessWord(List<String> currentList, String guessWord) {
        return wordModel.sortedSimilarities(currentList,guessWord);
    }


    @Override
    public List<String> getWords(ListType listType,String category, int amount) {
        return tfidfModel.giveWords(listType,category,amount);

    }
}
