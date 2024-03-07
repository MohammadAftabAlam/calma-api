package com.calmaapp.mappingdistnace;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistanceMatrixResponse {
    private String status;
    private DistanceMatrixElement[] rows;

    // Getters and setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DistanceMatrixElement[] getRows() {
        return rows;
    }

    public void setRows(DistanceMatrixElement[] rows) {
        this.rows = rows;
    }
}
