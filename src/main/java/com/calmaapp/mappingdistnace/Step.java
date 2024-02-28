package com.calmaapp.mappingdistnace;



public class Step {
    private Duration duration;
    // Other fields like startLocation, endLocation, distance, instructions, etc.

    // Constructors, getters, and setters
    public Step() {}

    public Step(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
