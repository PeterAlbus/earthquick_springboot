package com.peteralbus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estimate {
    @TableId(type= IdType.ASSIGN_ID)
    private Long analyzeId;
    private Long earthquakeId;
    private Double predictDeath;
    private Double predictEconomy;
    private int population;
    private Date gmt_time;
}
