package com.example.demo.service;


import com.example.demo.enums.ListType;

import java.io.IOException;
import java.util.List;

public interface DataService {

    int train() throws IOException;
    List<List<Object>> guessWord(List<String> currentList, String guessWord);
    List<String> getWords(ListType listType, String category, int amount);
}
