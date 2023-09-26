package com.example.veterinaria;

public class Raza {
    private int value;
    private String text;

    public Raza(int value, String text) {
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
