package com.hizmet.bluewhaleventures.classes;

public class Question {
    private String question;
    private String answer;
    private String notes;
    private int questionNumber;

    public Question(int index, String name) {
        this.questionNumber = index;
        this.question = name;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public int getIndex() {
        return questionNumber;
    }

    public void setIndex(int index) {
        this.questionNumber = index;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }
}