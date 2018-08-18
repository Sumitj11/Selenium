package com.HybridFramework.util;

import java.util.Hashtable;

public class DataUtil {

	public static Object[][]  getTestData(String testCaseName,Xls_Reader xls){
		
		// find out the row number where the TESTCASE is
		int startRowNumber = 0;
		while (!xls.getCellData(Constants.DATA_SHEET, 0, startRowNumber).equalsIgnoreCase(testCaseName)) {
			startRowNumber++;
		}
		System.out.println("Test Name " + testCaseName + " @Row No. "
				+ startRowNumber);

		// find out the number of columns
		int columnStartRowNumber = startRowNumber + 1;
		int totalCols = 0;
		while (!xls.getCellData(Constants.DATA_SHEET, totalCols, columnStartRowNumber)
				.equals("")) {

			totalCols++;
		}
		System.out.println("Total No. of Columns = " + totalCols);

		// find out the number of rows
		int testCase_dataStartRowNumber = startRowNumber + 2;
		int totalRows = 0;
		while (!xls.getCellData(Constants.DATA_SHEET, 1, testCase_dataStartRowNumber).equals(
				"")) {
			totalRows++;
			testCase_dataStartRowNumber++;
		}
		System.out.println("Total No. of TestCases for "+ testCaseName + " is -> " + totalRows );
		System.out.println("TestCase " +testCaseName +" completes at Row No. -> " + testCase_dataStartRowNumber);

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
				String key = xls.getCellData(Constants.DATA_SHEET, cNum, columnStartRowNumber);
				String data = xls.getCellData(Constants.DATA_SHEET, cNum, rNum);
				
				//This line shows test case is at rowno. 
				//System.out.println( " Testcase No. -> " + rNum + " -- " + key + " -- " + data);
				//This line shows test case No.
				System.out.println( " Testcase No. -> " + i + " -- " + key + " -- " + data);
				table.put(key, data);
			}
		myData[i][0]=table;
		i++;
		}
	return myData;
	}
	// function to checkif the runmode is Y or N
	public static boolean isSkip(String testCaseName, Xls_Reader xls) {
//		get the numbers of rows in the testcases sheet
		int rows = xls.getRowCount(Constants.TESTCASES_SHEET);
//		navigate to that row where test case is and get the test case name and than check runmode.
//		if runmode is NO than return true as the test case has been skipped
		
		for (int rNum = 2; rNum <= rows; rNum++) {
			String tcid = xls.getCellData(Constants.TESTCASES_SHEET,
					Constants.TCID_COLUMN, rNum);
			if (tcid.equalsIgnoreCase(testCaseName)) {
				String runmode = xls.getCellData(Constants.TESTCASES_SHEET,
						Constants.RUNMODE_COLUMN, rNum);
				if (runmode.equals(Constants.RUNMODE_NO))
					return true;
				else
					return false;
			}
		}
		// in case if testcases is not found at all it will retrun true meaning test is skipped
		return true;
	}	
}
