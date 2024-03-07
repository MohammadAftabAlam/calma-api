package com.calmaapp.mappingdistnace;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class DistanceMatrixElement {
    private DistanceMatrixValue[] elements;

    // Getter and setter for 'elements'

    public DistanceMatrixValue[] getElements() {
        return elements;
    }

    public void setElements(DistanceMatrixValue[] elements) {
        this.elements = elements;
    }
}
