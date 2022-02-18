package com.peteralbus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("fire_center")
public class FireCenter {
    @TableId(type= IdType.ASSIGN_ID)
    private Integer fireId;
    private Double fireLon;
    private Double fireLat;
    private String fireName;
    private String fireAddress;
    private String fireProvince;
    private String fireCityName;
    private String fireType;
}
