package com.vehicle.rent.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 
 * @author umamahesh.rd
 *
 */

@SpringBootApplication(scanBasePackages = { "com.vehicle.rent.controller" })
public class VehicleRentalSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleRentalSpringBootApplication.class, args);
	}
}
