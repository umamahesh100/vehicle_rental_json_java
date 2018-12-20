package com.vehicle.rent.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VehicleRentTest {

	private String endPointUlr = "http://localhost:8096";

	/*
	 * below testAllCars method is used to print all the Cars avaiblable in json
	 * format . runtimeException will be thrown incase of failure
	 */

	@Test
	public void testGetAllCars() {

		try {

			URL url = new URL(endPointUlr + "/getCars");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			System.out.println("All Cars:");
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = testReadCarsJsonFromClassPath(conn.getInputStream());

			/**
			 * printing the cars of Map in JSON format using Pretty print Json
			 */

			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map.get("Cars")));
			System.out.println("========================");

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	/**
	 * below printCarsbyColorModel method is used to print the cars with perticular
	 * Make and Color, runtimeException will be thrown incase of failure
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void printCarsbyColorModel() throws Exception {

		try {
			String testData_Color = "black";
			String testData_Make = "tesla";

			URL url = new URL(endPointUlr + "/cars/" + testData_Make + "/" + testData_Color);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			Map<String, Object> responseMap = testReadCarsJsonFromClassPath(conn.getInputStream());
			List<Map<String, Object>> carsList = (List<Map<String, Object>>) responseMap.get("Cars");

			for (Map<String, Object> map : carsList) {
				if (((String) map.get("make")).equalsIgnoreCase(testData_Make)) {
					Map<String, String> medaDataMap = (Map<String, String>) map.get("metadata");
					if (medaDataMap.get("Color").equalsIgnoreCase(testData_Color)) {
						System.out.println("Vin:" + map.get("vin"));
						System.out.println("Make:" + map.get("make"));
						System.out.println("Color:" + medaDataMap.get("Color"));
						System.out.println("Notes:" + medaDataMap.get("Notes"));
						System.out.println("=========================================");
					}
				}
			}
			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetCarsByLowestPriceAfterDiscount() throws Exception {

		URL url = new URL(endPointUlr + "/cars/lesspriceafterdiscount");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		Map<String, Object> carsMap = testReadCarsJsonFromClassPath(conn.getInputStream());
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
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("======================================");
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultMap));
		System.out.println("======================================");

	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Test
	public void testGetCarsByHighestRevenue() throws Exception {

		URL url = new URL(endPointUlr + "/cars/highestrevenue");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		Map<String, Object> carsMap = testReadCarsJsonFromClassPath(conn.getInputStream());
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
			depreciation = (Double) highRevenueMap.get("depreciation");
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
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("======================================");
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultMap));
		System.out.println("======================================");

	}

	/* below readCarsJsonFromClassPath */

	private Map<String, Object> testReadCarsJsonFromClassPath(InputStream input) {
		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> carsMap = null;
		try {
			carsMap = objectMapper.readValue(input, new TypeReference<Map<String, Object>>() {
			});

		} catch (Exception e) {

			e.printStackTrace();
		}
		return carsMap;
	}

}
