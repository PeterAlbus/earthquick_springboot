package com.peteralbus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Distance {
    private double startLon;
    private double startLat;
    private double endLon;
    private double endLat;
    private double distance;
    private String endName;
    private String endAddress;
}
