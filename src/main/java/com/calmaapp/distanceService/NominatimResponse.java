package com.calmaapp.distanceService;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NominatimResponse {
 private double lat;
 private double lon;

 
}
