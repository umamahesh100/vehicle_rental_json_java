package com.vehicle.rent.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
			printResponseInJSONFormat(conn);
			System.out.println("========================");
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void printResponseInJSONFormat(HttpURLConnection conn) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map;
		try {
			map = testReadCarsJsonFromClassPath(conn.getInputStream());
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map.get("Cars")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

	@Test
	public void testGetCarsByLowestPrice() throws Exception {

		URL url = new URL(endPointUlr + "/cars/lesspricecars");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}
		System.out.println("Response-Cars by lowest price:");
		printResponseInJSONFormat(conn);
		System.out.println("======================================");
		conn.disconnect();
	}

	@Test
	public void testGetCarsByLowestPriceAfterDiscount() throws Exception {

		URL url = new URL(endPointUlr + "/cars/lesspriceafterdiscount");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		System.out.println("Response-Cars by lowest price after Discount:");
		printResponseInJSONFormat(conn);
		System.out.println("======================================");
		conn.disconnect();
	}

	@Test
	public void testGetCarsByHighestRevenue() throws Exception {

		URL url = new URL(endPointUlr + "/cars/highestrevenue");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		System.out.println("Response-Cars by highest revenue:");
		printResponseInJSONFormat(conn);
		System.out.println("======================================");
		conn.disconnect();
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
