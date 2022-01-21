package com.peteralbus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Earthquake info.
 *
 * @author PeterAlbus
 */
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EarthquakeInfo implements Serializable
{
    @TableId(type= IdType.ASSIGN_ID)
    private Long earthquakeId;
    private String earthquakeName;
    private Double magnitude;
    private Double highIntensity;
    private Double longitude;
    private Double latitude;
    private Double depth;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime earthquakeTime;
    @TableField(exist = false)
    private List<IntensityLine> intensityLineList=new ArrayList<>();

    /**
     * Gets intensity.
     *
     * @param x1 The first regression constant of the regression equation(for long radius)
     * @param y1 the second regression constant of the regression equation(for long radius)
     * @param z1 the third regression constant of the regression equation(for long radius)
     * @param b1 the fourth regression constant of the regression equation(for long radius)
     * @param e1 the fifth regression constant of the regression equation(for long radius)
     * @param x2 The first regression constant of the regression equation(for short radius)
     * @param y2 the second regression constant of the regression equation(for short radius)
     * @param z2 the third regression constant of the regression equation(for short radius)
     * @param b2 the fourth regression constant of the regression equation(for short radius)
     * @param e2 the fifth regression constant of the regression equation(for short radius)
     */
    public void getIntensity(double x1,double y1,double z1,double b1,double e1,double x2,double y2,double z2,double b2,double e2)
    {
        double longRadius =0,shortRadius = 0;
        double ia=x1+y1*magnitude-z1*Math.log10(longRadius+b1)+e1;
        double ib=x2+y2*magnitude-z2*Math.log10(shortRadius+b2)+e2;
        System.out.println("震中烈度:"+ia+"/"+ib);
        setHighIntensity(ia);
        int intensity=(int)ia;
        intensityLineList.clear();
        int count=0;
        while (intensity>3)
        {
            longRadius=Math.pow(10,-(intensity-x1-e1-y1*magnitude)/z1)-b1;
            shortRadius=Math.pow(10,-(intensity-x2-e2-y2*magnitude)/z2)-b2;
            IntensityLine intensityLine=new IntensityLine(longRadius,shortRadius,-20.0,intensity,earthquakeId);
            intensityLineList.add(intensityLine);
            intensity--;
            count++;
        }
    }

    /**
     * Gets intensity , use ln.
     *
     * @param x1 The first regression constant of the regression equation(for long radius)
     * @param y1 the second regression constant of the regression equation(for long radius)
     * @param z1 the third regression constant of the regression equation(for long radius)
     * @param b1 the fourth regression constant of the regression equation(for long radius)
     * @param x2 The first regression constant of the regression equation(for short radius)
     * @param y2 the second regression constant of the regression equation(for short radius)
     * @param z2 the third regression constant of the regression equation(for short radius)
     * @param b2 the fourth regression constant of the regression equation(for short radius)
     */
    public void getIntensityLn(double x1,double y1,double z1,double b1,double x2,double y2,double z2,double b2)
    {
        double longRadius =0,shortRadius = 0;
        double ia=x1+y1*magnitude-z1*Math.log10(longRadius+b1);
        double ib=x2+y2*magnitude-z2*Math.log10(shortRadius+b2);
        System.out.println("震中烈度:"+ia+"/"+ib);
        setHighIntensity(ia);
        int intensity=(int)ia;
        intensityLineList.clear();
        while (intensity>1)
        {
            longRadius=Math.exp(-(intensity-x1-y1*magnitude)/z1)-b1;
            shortRadius=Math.exp(-(intensity-x2-y2*magnitude)/z2)-b2;
            IntensityLine intensityLine=new IntensityLine(longRadius,shortRadius,-20.0,intensity,earthquakeId);
            intensityLineList.add(intensityLine);
            intensity--;
        }
    }

    /**
     * Gets formatted time.
     *
     * @return the formatted time
     */
    public String getFormattedTime()
    {
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.format(earthquakeTime);
    }
}
