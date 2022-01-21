package com.peteralbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The type Earthquick application.
 * @author PeterAlbus
 */
@SpringBootApplication
@EnableScheduling
public class EarthquickApplication
{

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args)
    {
        SpringApplication.run(EarthquickApplication.class, args);
    }

}
