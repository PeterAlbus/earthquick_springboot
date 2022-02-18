package com.peteralbus.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Hospital.
 * @author PeterAlbus
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("hospital_center")
public class Hospital {
    @TableId
    private Integer id;
    private Double lon;
    private Double lat;
    private String name;
    private String address;
    private String pname;
    private String cityname;
    private String type;
}
