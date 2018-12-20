package com.vehicle.rent.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*  main Spring boot application to run the app  */

@SpringBootApplication(scanBasePackages = { "com.vehicle.rent.controller" })
public class VehicleRentalSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleRentalSpringBootApplication.class, args);
	}
}
