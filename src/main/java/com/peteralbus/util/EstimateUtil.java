package com.peteralbus.util;

import com.peteralbus.entity.FireWeight;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The type Estimate util.
 *
 * @author PeterAlbus
 */
@Component
public class EstimateUtil
{
    /**
     * death predict int.
     *
     * @param earthquakeId   the earthquake id
     * @param population     the population
     * @param magnitude      the magnitude
     * @param intensity      the intensity
     * @param earthquakeTime the time of earthquake
     * @param longitude      the longitude
     * @param latitude       the latitude
     * @return the double
     */
    public double deathPredict(Long earthquakeId,int population, double magnitude, double intensity, LocalDateTime earthquakeTime,Double longitude,Double latitude)
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
        {
            bdr = 0.01;
        }
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

    /**
     * 使用熵值法计算每个点的权重
     *
     * @param data                    二维数组，pointCount个点的parameterCount个参考指标
     * @param pointCount              救援点数量
     * @param parameterCount          参考指标数量
     * @param positiveIndicatorsIndex 参考指标中为正向指标的索引
     * @return 权重列表Double[pointCount], 依次对应data中的每个点
     */
    public Double[] entropyMethod(double[][] data, int pointCount, int parameterCount, List<Integer> positiveIndicatorsIndex) {
        Double[] max = new Double[parameterCount];
        Double[] min = new Double[parameterCount];
        Double[] sum = new Double[parameterCount];
        // 求出每个属性的最大值、最小值
        for(int i = 0; i < parameterCount; i++) {
            max[i] = Double.MIN_VALUE;
            min[i] = Double.MAX_VALUE;
            sum[i] = 0.0;
            for(int j=0;j<pointCount;j++) {
                if(data[j][i] > max[i]) {
                    max[i] = data[j][i];
                }
                if(data[j][i] < min[i]) {
                    min[i] = data[j][i];
                }
            }
        }
        // 归一化
        Double[][] normalizedData = new Double[pointCount][parameterCount];
        for(int i = 0; i < parameterCount; i++) {
            for(int j=0;j < pointCount;j++) {
                if(positiveIndicatorsIndex.contains(i)) {
                    normalizedData[j][i] = (data[j][i] - min[i]) / (max[i] - min[i]);
                } else {
                    normalizedData[j][i] = (max[i] - data[j][i]) / (max[i] - min[i]);
                }
                sum[i] += normalizedData[j][i];
            }
        }
        // 每个属性指标值在该属性所有数值中的比例
        Double[][] percentage = new Double[pointCount][parameterCount];
        for(int i = 0; i < parameterCount; i++) {
            for(int j=0;j<pointCount;j++) {
                percentage[j][i] = normalizedData[j][i] / sum[i] + 0.000000000001;
            }
        }
        // 熵值
        Double[] entropy = new Double[parameterCount];
        // 变异系数的总和
        double sumG = 0.0;
        for(int i = 0; i < parameterCount; i++) {
            double sumJ = 0.0;
            for(int j=0;j< pointCount;j++) {
                sumJ += percentage[j][i] * Math.log(percentage[j][i]);
            }
            entropy[i] = (-1/Math.log(10))*sumJ;
            sumG += 1 - entropy[i];
        }
        // 每个系数的权重
        Double[] parameterWeight = new Double[parameterCount];
        for(int i = 0; i < parameterCount; i++) {
            parameterWeight[i] = (1 - entropy[i]) / sumG;
        }
        // 计算每个点的权重
        Double[] weight = new Double[pointCount];
        for(int i = 0; i < pointCount; i++) {
            weight[i] = 0.0;
            for(int j = 0; j < parameterCount; j++) {
                weight[i] += percentage[i][j] * parameterWeight[j];
            }
        }
        return weight;
    }

}
