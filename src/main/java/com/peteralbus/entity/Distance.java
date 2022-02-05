package com.peteralbus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Distance.
 * @author PeterAlbus
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Distance {
    private Double startLon;
    private Double startLat;
    private Double endLon;
    private Double endLat;
    private Double distance;
    private String endName;
    private String endAddress;
}
