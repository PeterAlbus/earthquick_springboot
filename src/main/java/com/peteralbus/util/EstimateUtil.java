package com.peteralbus.util;

import org.springframework.stereotype.Component;

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
     * @return the double
     */
    public double deathPredict(int population,double magnitude,double intensity)
    {
        double deathPredict = 0;
        /*M为震级 I为震中强度*/
        double mCoefficient = Math.abs((magnitude-4.17)/0.35*intensity-0.97);
        /*population 为人口密度*/
        double denCoefficient = 0.05*Math.log(population)+0.74;
        /*白天为1，夜晚为0.75*/
        double timeCoefficient = 0.75;
        /*西部强度修正系数 0.3661*/
        double strengthCoefficient = 0.4853;
        double bdr = 0.5;
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
    {
        return Math.pow(10,0.84444*highIntensity-1.831)/10000;
    }

}
