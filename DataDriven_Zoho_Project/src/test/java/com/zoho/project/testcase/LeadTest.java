package com.zoho.project.testcase;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/*import util.DataUtil;
import util.Xls_Reader;
*/
import com.baseclass.BaseClass;
import com.relevantcodes.extentreports.LogStatus;
import com.zoho.project.util.DataUtil;
import com.zoho.project.util.Xls_Reader;

public class LeadTest extends BaseClass {
	// Variables
	Xls_Reader xls;
	//String testCaseName = "DeleteLeadTest";
	SoftAssert softAssert;

	@Test(priority = 1, dataProvider = "createLeadTest")
	public void createLeadTest(Hashtable<String, String> data)
			throws InterruptedException {
		logger = reports.startTest("CreateLeadTest");

//		init();
		if (DataUtil.isRunnable("CreateLeadTest", xls)
				|| data.get("Runmode").equals("N")) {
			logger.log(LogStatus.SKIP, "Skipping the Test as Runmode is N");
			throw new SkipException("Skipping the Test as Runmode is N");
		}
		
		 openBrowser("Firefox"); navigate("appurl");
		 doLogin(prop.getProperty("userid"), prop.getProperty("password"));
		 click("crmlink_xpath"); 
		 click("leadsTab_xpath");
		 click("createLead_xpath"); 
		 type2("leadCompany_xpath", data.get("LeadCompany"));
		 type2("leadLastName_xpath", data.get("LeadLastName")); 
		 click("leadSaveButton_xpath");
		 clickAndWait("leadsTab_xpath","leadsTab_xpath");
		 
		 int rowNum =  getLeadRowNum(data.get("LeadLastName"));
		if(rowNum==-1){
			reportFail("Lead not found in Lead table " + data.get("LeadLastName"));
		}else
			reportPass("Lead - "  + data.get("LeadLastName") +  " found in Lead table ");
		takeScreenShot();
	}

	@Test(priority = 2,dataProvider = "ConvertLeadTest") // dependsOnMethods = { "createLeadTest" }
	public void convertLeadTest(Hashtable<String, String> data) throws InterruptedException {

		logger = reports.startTest("ConvertLeadTest");
		openBrowser("Firefox"); navigate("appurl");
		 doLogin(prop.getProperty("userid"), prop.getProperty("password"));
		 click("crmlink_xpath"); 
		 click("leadsTab_xpath");
		 clickOnLead(data.get("LeadLastName"));
		 click("convertLeadButton_xpath");
		 click("convertLeadSaveButton_xpath");
		 clickAndWait("convertLeadSaveButton_xpath", "goToLeadTestButton_xpath");
	}

	
	@Test(priority = 3, dataProvider = "getDataDeleteLead")//,dependsOnMethods = { "createLeadTest","convertLeadTest"}
	public void deleteLeadTest(Hashtable<String, String> data) throws InterruptedException {
		logger = reports.startTest("DeleteLeadTest");
		
		if (DataUtil.isRunnable("DeleteLeadTest", xls)
				|| data.get("Runmode").equals("N")) {
			logger.log(LogStatus.SKIP, "Skipping the Test as Runmode is N");
			throw new SkipException("Skipping the Test as Runmode is N");
		}
		
		openBrowser("Firefox"); navigate("appurl");
		 doLogin(prop.getProperty("userid"), prop.getProperty("password"));
		 click("crmlink_xpath"); 
		 click("leadsTab_xpath");
		 clickOnLead(data.get("LeadLastName"));
		 click("customTools_xpath");
		 click("deleteLead_xpath");
		// acceptAlert();
		 click("confirmDeleteLead_xpath");
		 wait(5);
		 click("leadsTab_xpath");
		int rowNum = getLeadRowNum(data.get("LeadLastName"));
		if(rowNum!=-1)
			reportFail("Could not delte the Lead");
		else 
			reportPass("Lead deleted succesfully ");
		takeScreenShot();
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
		if (driver != null)
			driver.quit();
	}

/*	@DataProvider
	// Using hash table so as to support multiple
	public Object[][] getDataFromExcelUsingHashTable() {
		// super is used to access function from parent class . super because
		// same function also exists in current class
		super.init();
		xls = new Xls_Reader(prop.getProperty("TestData_xls"));
		return DataUtil.getTestData(xls, testCaseName);
	}*/
	
	@DataProvider(parallel=true)
	// Using hash table so as to support multiple
	public Object[][] createLeadTest() {   // getDataFromExcelUsingHashTable
		// super is used to access function from parent class . super because
		// same function also exists in current class
		super.init();
		xls = new Xls_Reader(prop.getProperty("TestData_xls"));
		return DataUtil.getTestData(xls, "CreateLeadTest");
	}
	@DataProvider(parallel=true)
	// Using hash table so as to support multiple
	public Object[][] ConvertLeadTest() {   // getDataFromExcelUsingHashTable
		// super is used to access function from parent class . super because
		// same function also exists in current class
		super.init();
		xls = new Xls_Reader(prop.getProperty("TestData_xls"));
		return DataUtil.getTestData(xls, "ConvertLeadTest");
	}
	@DataProvider(parallel=true)
	// Using hash table so as to support multiple
	public Object[][] getDataDeleteLead() {   // getDataFromExcelUsingHashTable
		// super is used to access function from parent class . super because
		// same function also exists in current class
		super.init();
		xls = new Xls_Reader(prop.getProperty("TestData_xls"));
		return DataUtil.getTestData(xls, "DeleteLeadTest");
	}
	

}