package com.system.learn.dto.card;

public class ReviewTimeDto {
    private double value;
    private String unit;

    public ReviewTimeDto(double value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public ReviewTimeDto() {}

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}