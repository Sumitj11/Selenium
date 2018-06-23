package com.zoho.project.testcase;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.baseclass.BaseClass;
import com.relevantcodes.extentreports.LogStatus;
import com.zoho.project.util.DataUtil;
import com.zoho.project.util.Xls_Reader;

public class PotentialTest extends BaseClass {

	// Variables
	Xls_Reader xls;
	SoftAssert softAssert;

	// test is not complete because Potential Link does not exists in  WEB SITE.
	@Test(priority = 1,dataProvider = "getPotentialData")
	public void createPotentialTest(Hashtable<String, String> data) {
		logger = reports.startTest("CreatePotentialTest");
		logger.log(LogStatus.INFO, data.toString());
		if (DataUtil.isRunnable("CreatePotentialTest", xls)
				|| data.get("Runmode").equals("N")) {
			logger.log(LogStatus.SKIP, "Skipping the Test as Runmode is N");
			throw new SkipException("Skipping the Test as Runmode is N");
		
		/*	 openBrowser("Firefox"); navigate("appurl");
			 doLogin(prop.getProperty("userid"), prop.getProperty("password"));
			 click("crmlink_xpath"); 
		*/
		
		
		}
	
	}

	@Test(priority = 2,dependsOnMethods={"deletePotentialTest"})
	public void deletePotentialAccountTest(Hashtable<String, String> data) {
		logger = reports.startTest("DeletePotentialAccountTest");
		logger.log(LogStatus.INFO, data.toString());
		if (DataUtil.isRunnable("DeletePotentialAccountTest", xls)
				|| data.get("Runmode").equals("N")) {
			logger.log(LogStatus.SKIP, "Skipping the Test as Runmode is N");
			throw new SkipException("Skipping the Test as Runmode is N");
		}
	
	}
	
	
	@DataProvider
	// Using hash table so as to support multiple
	public Object[][] getPotentialData() {   // getDataFromExcelUsingHashTable
		// super is used to access function from parent class . super because
		// same function also exists in current class
		super.init();
		xls = new Xls_Reader(prop.getProperty("TestData_xls"));
		return DataUtil.getTestData(xls, "CreatePotentialTest");
	}
	
	public Object[][] deletePotentialTest() {   // getDataFromExcelUsingHashTable
		// super is used to access function from parent class . super because
		// same function also exists in current class
		super.init();
		xls = new Xls_Reader(prop.getProperty("TestData_xls"));
		return DataUtil.getTestData(xls, "DeletePotentialAccountTest");
	}
	
	@BeforeTest
	public void init() {
		softAssert = new SoftAssert();
	}

	@AfterTest
	public void quit() {
		try {
			softAssert.assertAll();
		} catch (Error e) {
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		if (reports != null) {
			reports.endTest(logger);
			reports.flush();
		}
		//if (driver != null)
			//driver.quit();
	}
}
