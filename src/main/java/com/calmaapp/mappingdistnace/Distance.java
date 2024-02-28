package com.calmaapp.mappingdistnace;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Distance {
    private double value;

    // Getter and setter for 'value'

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
