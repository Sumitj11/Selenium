package com.HybridFramework.sampleTest;

import java.util.Hashtable;

import com.HybridFramework.util.Xls_Reader;

public class ReadingFromExcel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String testName = "TestB";
		// Xls_Reader xls = new
		// Xls_Reader("F:\\Learning Java\\HybridFramework\\testData\\SuiteA.xls");

		Xls_Reader xls = new Xls_Reader(System.getProperty("user.dir")
				+ "//testData//SuiteA.xls");
		// find out the row number where the TESTCASE is
		int startRowNumber = 1;
		while (!xls.getCellData("Data", 0, startRowNumber).equals(testName)) {
			startRowNumber++;
		}
		System.out.println("Test Name " + testName + " @Row No. "
				+ startRowNumber);

		// find out the number of columns
		int columnStartRowNumber = startRowNumber + 1;
		int totalCols = 0;
		while (!xls.getCellData("Data", totalCols, columnStartRowNumber)
				.equals("")) {

			totalCols++;
		}
		System.out.println("Total No. of Columns = " + totalCols);

		// find out the number of rows
		int testCase_dataStartRowNumber = startRowNumber + 2;
		int totalRows = 0;
		while (!xls.getCellData("Data", 1, testCase_dataStartRowNumber).equals(
				"")) {
			totalRows++;
			testCase_dataStartRowNumber++;
		}
		System.out.println("Total No. of Rows  = " + totalRows + "  "
				+ "testCase_dataStartRowNumber " + testCase_dataStartRowNumber);

		// Read the data
		testCase_dataStartRowNumber = startRowNumber + 2;
		// finalRow is the last row of the test case
		int finalRow = testCase_dataStartRowNumber + totalRows;

		// Hastable create and initiated to null
		Hashtable<String, String> table = null;
		
		// create 2 dimensional array object
		Object[][] myData = new Object[totalRows][1];
		int i = 0;
		for (int rNum = testCase_dataStartRowNumber; rNum < finalRow; rNum++) {
			System.out.println("*****************************");
			// hashtable initialised here because there will be table in every row
			table = new Hashtable<String, String>();
			for (int cNum = 0; cNum < totalCols; cNum++) {
				String key = xls.getCellData("Data", cNum, columnStartRowNumber);
				String data = xls.getCellData("Data", cNum, rNum);
				System.out.println( key + " -- " + data + " -- " + " Test data at Row no. " + rNum);
				table.put(key, data);
			}
		}myData[i][0]=table;
		i++;

	}

}
