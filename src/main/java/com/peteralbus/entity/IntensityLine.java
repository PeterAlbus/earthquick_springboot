package com.peteralbus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * The type Intensity line.
 *
 * @author PeterAlbus
 */
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntensityLine implements Serializable
{
    @TableId(type= IdType.ASSIGN_ID)
    private Long lineId;
    private Double longRadius;
    private Double shortRadius;
    private Double angle;
    private Integer intensity;
    private Long earthquakeId;

    public IntensityLine(Double longRadius, Double shortRadius, Double angle, Integer intensity, Long earthquakeId)
    {
        this.longRadius = longRadius;
        this.shortRadius = shortRadius;
        this.angle = angle;
        this.intensity = intensity;
        this.earthquakeId = earthquakeId;
    }
}
