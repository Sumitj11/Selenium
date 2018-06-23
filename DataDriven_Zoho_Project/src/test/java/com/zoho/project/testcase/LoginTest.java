package com.zoho.project.testcase;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.baseclass.BaseClass;
import com.relevantcodes.extentreports.LogStatus;
import com.zoho.project.util.DataUtil;
import com.zoho.project.util.Xls_Reader;

public class LoginTest extends BaseClass {
	
	String testCaseName = "LoginTest";
	Xls_Reader xls;
	

	@Test
	public void login(){
		logger = reports.startTest("Login Test Initiated");
		logger.log(LogStatus.INFO, "Login Test is Running ");
	
		System.out.println(" Environment " + prop.getProperty("env"));
		System.out.println("Properties file called = " + envProp.getProperty("environment"));
	}
	
	/*@Test(dataProvider="getDataFromExcelUsingHashTable")
	public void doLogin(Hashtable<String, String> data) throws InterruptedException {
		logger = reports.startTest("Login Test Initiated");
		logger.log(LogStatus.INFO, "Login Test is Running ");
	
		if (DataUtil.isRunnable(testCaseName, xls)  || data.get("Runmode").equals("N")) {
			logger.log(LogStatus.SKIP, "Skipping the Test as Runmode is N");
			throw new SkipException("Skipping the Test as Runmode is N");
		}
		System.out.println("Browser opening");
		openBrowser(data.get("Browser"));
		navigate("appurl");
		doLogin(data.get("Username"),data.get("Password"));
		
	}
*/	
	@BeforeTest
	public void init() {
		super.init();
		softAssert = new SoftAssert();
	}

	@AfterTest
	public void quit() {
		try {
			softAssert.assertAll();
		} catch (Error e) {
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		reports.endTest(logger);
		reports.flush();
		/*if(driver!=null)
			driver.quit();*/
	}

	@DataProvider(parallel=true)  
	// parallel=true means that all the data sets will be invoked parallel 
	// Using hash table so as to support multiple
	public Object[][] getDataFromExcelUsingHashTable() {
		//super is used to access function from parent class . super because same function also exists in current class
		super.init();
		xls = new Xls_Reader(prop.getProperty("TestData_xls"));
		return DataUtil.getTestData(xls, testCaseName);
	}
	
}
