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

	/**
	 * getAllCars() method gets executed with url http://localhost:8080/getCars
	 * @return list of cars
	 */

	@RequestMapping(value = "/getCars", method = RequestMethod.GET)
	public ResponseEntity<?> getAllCars() {

		Map<String, Object> carsMap = readCarsJsonFromClassPath();
		return new ResponseEntity<Object>(carsMap, HttpStatus.OK);

	}

	/**
	 * GetCarsByColor by matched make and color passed in the request
	 * 
	 * @param make
	 * @param color
	 * @return List of Cars
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cars/{make}/{color}", method = RequestMethod.GET)
	public ResponseEntity<?> getCarsByColorMake(@PathVariable("make") String make,
			@PathVariable("color") String color) {

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

	/**
	 * GetCarsByColor by matched make and color passed in the request
	 * 
	 * @param make
	 * @param color
	 * @return List of Cars
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "cars/lesspricecars", method = RequestMethod.GET)
	public ResponseEntity<?> getCarsByLowestPrice() {

		Map<String, Object> carsMap = readCarsJsonFromClassPath();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> filterdCarsList = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> carsList = (List<Map<String, Object>>) carsMap.get("Cars");
		double lowestPrice = 0;
		int count = 0;
		for (Map<String, Object> map : carsList) {
			Map<String, Object> perdayRentMap = (Map<String, Object>) map.get("perdayrent");
			if (count == 0) {
				lowestPrice = (Double) perdayRentMap.get("Price");
				filterdCarsList.add(map);
				count++;
				continue;
			}
			if (((Double) perdayRentMap.get("Price")) == lowestPrice) {
				filterdCarsList.add(map);
			}
			if (((Double) perdayRentMap.get("Price")) < lowestPrice) {
				filterdCarsList.clear();
				filterdCarsList.add(map);
				lowestPrice = (Double) perdayRentMap.get("Price");
			}

		}
		resultMap.put("Cars", filterdCarsList);
		return new ResponseEntity<Object>(resultMap, HttpStatus.OK);

	}

	/**
	 * GetCarsByColor by matched make and color passed in the request
	 * 
	 * @param make
	 * @param color
	 * @return List of Cars
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "cars/lesspriceafterdiscount", method = RequestMethod.GET)
	public ResponseEntity<?> getCarsByLowestPriceAfterDiscount() {

		Map<String, Object> carsMap = readCarsJsonFromClassPath();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> filterdCarsList = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> carsList = (List<Map<String, Object>>) carsMap.get("Cars");
		double lowestPrice = 0;
		double discountedPrice = 0;
		boolean firstRecordFlag = true;
		for (Map<String, Object> map : carsList) {
			Map<String, Object> perdayRentMap = (Map<String, Object>) map.get("perdayrent");
			if (firstRecordFlag) {
				discountedPrice = (Double) perdayRentMap.get("Discount");
				lowestPrice = (Double) perdayRentMap.get("Price") - discountedPrice;
				filterdCarsList.add(map);
				firstRecordFlag = false;
				continue;
			}
			discountedPrice = (Double) perdayRentMap.get("Discount");
			if (((Double) perdayRentMap.get("Price")) - discountedPrice == lowestPrice) {
				filterdCarsList.add(map);
			}
			if (((Double) perdayRentMap.get("Price")) - discountedPrice < lowestPrice) {
				filterdCarsList.clear();
				filterdCarsList.add(map);
				lowestPrice = (Double) perdayRentMap.get("Price") - discountedPrice;
			}

		}
		resultMap.put("Cars", filterdCarsList);
		return new ResponseEntity<Object>(resultMap, HttpStatus.OK);

	}
	/**
	 * @return list of cars with least maintenance cost
	 */

	@RequestMapping(value = "cars/highestrevenue", method = RequestMethod.GET)
	public ResponseEntity<?> getCarsByHighestRevenue() {

		Map<String, Object> carsMap = readCarsJsonFromClassPath();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> filterdCarsList = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> carsList = (List<Map<String, Object>>) carsMap.get("Cars");
		double expences = 0;
		double yoyMaintenance = 0;
		double depreciation = 0;
		boolean firstRecordFlag = true;
		for (Map<String, Object> map : carsList) {
			Map<String, Object> highRevenueMap = (Map<String, Object>) map.get("metrics");
			if (firstRecordFlag) {
				yoyMaintenance = (Double) highRevenueMap.get("yoymaintenancecost");
				expences = (Double) highRevenueMap.get("depreciation") + yoyMaintenance;
				filterdCarsList.add(map);
				firstRecordFlag = false;
				continue;
			}
			depreciation =(Double) highRevenueMap.get("depreciation");
			if (((Double) highRevenueMap.get("depreciation")) + yoyMaintenance == expences) {
				filterdCarsList.add(map);
			}
			if (((Double) highRevenueMap.get("depreciation")) + yoyMaintenance < expences) {
				filterdCarsList.clear();
				filterdCarsList.add(map);
				expences = ((Double) highRevenueMap.get("depreciation")) + yoyMaintenance;
			}

		}
		resultMap.put("Cars", filterdCarsList);
		System.out.println("cars with lesser maintenance cost are  :  " + resultMap);
		return new ResponseEntity<Object>(resultMap, HttpStatus.OK);

	}

	/**
	 * readCarsJsonFromClassPath method is used to get the json data from json file
	 * useing ObjectMapper calss from Jackson API . Data was read into objectMapper ,which in turn set to Map
	 * @return
	 */

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