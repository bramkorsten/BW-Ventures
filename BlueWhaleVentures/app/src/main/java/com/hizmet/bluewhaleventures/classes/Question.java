package com.hizmet.bluewhaleventures.classes;

public class Question {
    private String name;
    private int questionNumber;

    public Question(int index, String name) {
        this.questionNumber = index;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return questionNumber;
    }
}