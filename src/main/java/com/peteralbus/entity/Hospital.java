package com.peteralbus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hospital {
    private int id;
    private double lon;
    private double lat;
    private String name;
    private String address;
    private String pname;
    private String cityname;
    private String type;
}
