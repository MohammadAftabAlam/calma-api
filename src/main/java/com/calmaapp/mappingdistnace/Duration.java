package com.calmaapp.mappingdistnace;



public class Duration {
    private String text;
    private int value;

    // Constructors, getters, and setters
    public Duration() {}

    public Duration(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
