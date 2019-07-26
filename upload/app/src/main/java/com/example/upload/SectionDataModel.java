package com.example.upload;

import java.util.ArrayList;

public class SectionDataModel {
    String time;
    ArrayList<SingleDataModel> item = new ArrayList<>();
    SectionDataModel(String time){
        this.time = time;
    }
}
