package com.zoho.project.util;

import java.util.Hashtable;

public class DataUtil {

	public static Object[][] getTestData(Xls_Reader xls, String testCaseName) {
		int testStartRowNum = 1;

		String sheetName = "Data";

		while (!xls.getCellData(sheetName, 0, testStartRowNum).equals(
				testCaseName)) {
			testStartRowNum++;
		}
		System.out.println("Name of the Test Case - " + testCaseName);
		System.out.println("Test Starts from row - " + testStartRowNum);
		int columnStartNum = testStartRowNum + 1;
		int DataStartNum = testStartRowNum + 2;

		// calculate number of rows of data are there for a particular test case
		int rows = 0;
		while (!xls.getCellData(sheetName, 0, DataStartNum + rows).equals("")) {

			rows++;
		}
		System.out.println("Number of rows in Test - " + rows);

		// Calc number of columns
		int cols = 0;
		while (!xls.getCellData(sheetName, cols, columnStartNum).equals("")) {
			cols++;
		}
		System.out.println("Number of columns in Test - " + cols);
		// store data in an object array
		Object[][] data = new Object[rows][1];
		// read the data
		// Finding row location to store data
		int dataRow = 0;
		Hashtable<String, String> hashTable = null;
		for (int rowNum = DataStartNum; rowNum < DataStartNum + rows; rowNum++) {
			hashTable = new Hashtable<String, String>();
			for (int colNum = 0; colNum < cols; colNum++) {
				String key = xls.getCellData(sheetName, colNum, columnStartNum);
				String value = xls.getCellData(sheetName, colNum, rowNum);
				hashTable.put(key, value);
				System.out.print(key + " - " + value + " || ");
			}
			System.out.println();
			data[dataRow][0] = hashTable;
			dataRow++;

		}

		return data;
	}

	public static boolean isRunnable(String testCaseName, Xls_Reader xls) {
		String sheetName = "TestCases";
		String tName = null;
		int r;
		int rows = xls.getRowCount(sheetName);
		for (r = 2; r < rows; r++)
			tName = xls.getCellData(sheetName, "TCID", r);
		if (tName.equals(testCaseName)) {
			String runmode = xls.getCellData(sheetName, "Runmode", r);
			if (runmode.equals("Y"))
				return true;
			else
				return false;

		}
		return false;
	}
}