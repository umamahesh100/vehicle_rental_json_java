package com.vehicle.rent.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/")
public class VehicleRentalController {
	
	/*  getAllCars() method gets executed with url http://localhost:8080/getCars which in tun calls the method readCarsJsonFromClassPath
	      andreturns ResponseEntity Map which contains the Cars JSON data
	   */

	@RequestMapping(value = "/getCars", method = RequestMethod.GET)
	public ResponseEntity<?> getAllCars() {

		Map<String, Object> carsMap = readCarsJsonFromClassPath();
		return new ResponseEntity<Object>(carsMap, HttpStatus.OK);

	}
  
	 /* with request http://localhost:8080/cars/{make}/{color}   getCarsByColor method gets executed , In this method cars were filtered by
	    Color & Make       */
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cars/{make}/{color}", method = RequestMethod.GET)
	public ResponseEntity<?> getCarsByColor(@PathVariable("make") String make, @PathVariable("color") String color) {

		Map<String, Object> carsMap = readCarsJsonFromClassPath();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> filterdCarsList = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> carsList = (List<Map<String, Object>>) carsMap.get("Cars");
		for (Map<String, Object> map : carsList) {
			if (((String) map.get("make")).equalsIgnoreCase(make)) {
				Map<String, String> medaDataMap = (Map<String, String>) map.get("metadata");
				System.out.println(medaDataMap);
				if (medaDataMap.get("Color").equalsIgnoreCase(color)) {
					filterdCarsList.add(map);
				}
			}

		}
		resultMap.put("Cars", filterdCarsList);
		return new ResponseEntity<Object>(resultMap, HttpStatus.OK);

	}

	/* readCarsJsonFromClassPath  method is used to get the json data from json file useing ObjectMapper calss from 
	   Jackson API . Data was read into objectMapper which in turn set to Map   */
	
	private Map<String, Object> readCarsJsonFromClassPath() {
		ObjectMapper objectMapper = new ObjectMapper();

		InputStream input;
		Map<String, Object> carsMap = null;
		try {
			input = this.getClass().getResourceAsStream("/carsList.json");

			carsMap = objectMapper.readValue(input, new TypeReference<Map<String, Object>>() {
			});

		} catch (Exception e) {

			e.printStackTrace();
		}
		return carsMap;
	}

}