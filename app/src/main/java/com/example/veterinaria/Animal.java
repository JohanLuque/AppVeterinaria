package com.example.veterinaria;

public class Animal {
    private int value;
    private String text;

    public Animal(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }

}
