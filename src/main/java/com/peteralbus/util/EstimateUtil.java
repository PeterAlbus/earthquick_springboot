package com.peteralbus.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * The type Estimate util.
 * @author PeterAlbus
 */
@Component
public class EstimateUtil
{
    /**
     * death predict int.
     *
     * @param population the population
     * @param magnitude  the magnitude
     * @param earthquakeTime the time of earthquake
     * @param longitude the longitude
     * @param latitude the latitude
     * @return the double
     */
    public double deathPredict(Long earthquake_id,int population, double magnitude, double intensity, LocalDateTime earthquakeTime,Double longitude,Double latitude)
    {
        double deathPredict = 0;
        /*M为震级 I为震中强度*/
        double mCoefficient = Math.abs((magnitude-4.17)/(0.35*intensity-0.97));
        /*population 为人口密度*/
        double denCoefficient = 0.05*Math.log(population)+0.74;
        /*时间修正系数*/
        /* 1:00-5:59为2,6:00-8:59为1,9:00-19:59为5/9,20:00-00:59为5/3 */
        int hour = earthquakeTime.getHour();
        double timeCoefficient = 5.0/3.0;
        if(hour>=1 && hour<6){
            timeCoefficient = 2.0;
        }
        else if(hour>=6 && hour < 9){
            timeCoefficient = 1.0;
        }
        else if(hour >= 9 && hour < 20){
            timeCoefficient = 5.0/9.0;
        }
        /*西部强度修正系数 0.3661*/
        double temp_latitude = 0.85 * longitude -64.4;/*黑河-腾冲现模拟方程*/
        double strengthCoefficient = 0.3661;
        if(latitude < temp_latitude){
            strengthCoefficient = 0.4853;
        }
        double bdr = (intensity*17.205-101.861)*0.01;
        if(bdr <= 0)
        bdr = 0.01;
        deathPredict = 0.461*mCoefficient*denCoefficient*timeCoefficient*strengthCoefficient*Math.exp(12.285*bdr);
        return deathPredict;
//        double deathPredict = 0;
//        deathPredict = 9.6*Math.pow(10,-11)*(0.00564*population+0.1634)*Math.exp(4.19*magnitude);
//        return (int)deathPredict;
    }

    /**
     * economy lose predict double.
     *
     * @param highIntensity the high intensity
     * @return the double
     */
    public double economyPredict(double highIntensity)
    {return Math.pow(10,0.84444*highIntensity-1.831)/10000;
    }

}
