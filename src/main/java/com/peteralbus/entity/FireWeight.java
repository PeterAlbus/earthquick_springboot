package com.peteralbus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lakefish
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FireWeight {
    private Integer fireId;
    private Double fireLon;
    private Double fireLat;
    private String fireName;
    private String fireAddress;
    private Double fireCenterWeight;
}
