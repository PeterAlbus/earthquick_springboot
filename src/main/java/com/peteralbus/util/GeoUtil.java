package com.peteralbus.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * The type Geo util.
 *
 * @author PeterAlbus
 */
public class GeoUtil
{
    private final static double EARTH_RADIUS = 6378137;
    private final static double EARTH_SEA = 1.852;

    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public static String getCountry(Double longitude, Double latitude)
    {
        Double[] chinaLon={93.003,86.373,73.680,71.947,80.416,94.0975,97.0312,106.2194,108.0356,115.0892,121.001,124.256,122.910,123.501,132.322,136.144,123.47,117.27,103.71};
        Double[] chinaLat={46.33,50.38,43.12,36.56,28.37,25.95,25.24,18.50,1.79,2.56,11.53,24.36,33.58,37.35,43.32,48.08,54.98,50.57,44.65};
        if(isInPolygon(longitude,latitude,chinaLon,chinaLat))
        {
            return "china";
        }
        return "unknown";
        /*
        *  93.00308916641167 46.33557176688004
 86.37301672326446 50.38751980406519
 73.6806350895496 43.12858377050971
 71.94791968712168 36.56117850620303
 80.4163170843829 28.375889044457423
 94.0975257616471 25.95490015099189
 97.03126325207795 25.2466929117808
 106.21940281130414 18.50325287154198
 108.03561240396455 1.7904217579092063
 115.08923186524542 2.5672899629108006
 121.00126550556921 11.536691305966007
 124.25692768695644 24.364585545502848
 122.9104187262468 33.58930644637152
 123.50186921321631 37.35578847985431
 132.32258204239037 43.32300574916736
 136.11484957724633 48.08471692521181
 123.4737832533535 54.98049753905599
 114.27833315710531 50.57789552466974
 103.71424276778035 44.6553865942794
 * */
    }

    /**
     * 判断是否在多边形区域内
     *
     * @param pointLon 要判断的点的横坐标 经度
     * @param pointLat 要判断的点的纵坐标 维度
     * @param lon      区域各顶点的横坐标数组
     * @param lat      区域各顶点的纵坐标数组
     */
    public static boolean isInPolygon(double pointLon, double pointLat, Double[] lon, Double[] lat)
    {
        // 将要判断的横纵坐标组成一个点
        Point2D.Double point = new Point2D.Double(pointLon, pointLat);
        // 将区域各顶点的横纵坐标放到一个点集合里面
        List<Point2D.Double> pointList = new ArrayList<Point2D.Double>();
        double polygonPointX = 0.0, polygonPointY = 0.0;
        for (int i = 0; i < lon.length; i++)
        {
            polygonPointX = lon[i];
            polygonPointY = lat[i];
            Point2D.Double polygonPoint = new Point2D.Double(polygonPointX, polygonPointY);
            pointList.add(polygonPoint);
        }
        return check(point, pointList);
    }

    /**
     * @param point   要判断的点的横纵坐标
     * @param polygon 组成的顶点坐标集合
     */
    private static boolean check(Point2D.Double point, List<Point2D.Double> polygon)
    {
        java.awt.geom.GeneralPath peneralPath = new java.awt.geom.GeneralPath();

        Point2D.Double first = polygon.get(0);
        // 通过移动到指定坐标（以双精度指定），将一个点添加到路径中
        peneralPath.moveTo(first.x, first.y);
        polygon.remove(0);
        for (Point2D.Double d : polygon)
        {
            // 通过绘制一条从当前坐标到新指定坐标（以双精度指定）的直线，将一个点添加到路径中。
            peneralPath.lineTo(d.x, d.y);
        }
        // 将几何多边形封闭
        peneralPath.lineTo(first.x, first.y);
        peneralPath.closePath();
        // 测试指定的 Point2D 是否在 Shape 的边界内。
        return peneralPath.contains(point);
    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1 纬度1
     * @param lng1 经度1
     * @param lat2 纬度2
     * @param lng2 经度2
     * @return 距离
     */
    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        //double len=s/EARTH_SEA;
        //s = s / 1000;
        return s;
    }

}
