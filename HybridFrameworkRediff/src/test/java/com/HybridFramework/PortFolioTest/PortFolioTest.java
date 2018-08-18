package com.HybridFramework.PortFolioTest;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.HybridFramework.baseTest.BaseTest;
import com.HybridFramework.util.Constants;
import com.HybridFramework.util.DataUtil;
import com.aventstack.extentreports.Status;

public class PortFolioTest extends BaseTest{
/*
Here catch is that name of the class is not same as Testcase in excel file.
becuse name of our test case is createPortfolioTest where in our class's name is PortFolioTest.
To solve this problem we use REFLECTION API 

*/
	@Test(dataProvider = "getData",priority=1)
	public void createPortfolioTest(Hashtable<String, String> data) throws Exception {
		if (data.get(Constants.RUNMODE_COLUMN).equals(Constants.RUNMODE_NO)|| DataUtil.isSkip(testCaseName, xls)) {
			logger.log(Status.SKIP, "Runmode is set to NO ");

			throw new SkipException("Runmode is set to NO ");

		}
		System.out.println("Running " + testCaseName);
		ds.executeKeywords(testCaseName, xls, data);
		//logger.log(Status.INFO, "Portfolio Created successfully");
	}
	
	@Test(dataProvider = "getData",priority=2,dependsOnMethods={"createPortfolioTest"})
	public void deletePortfolioTest(Hashtable<String, String> data) throws Exception {
		if (data.get(Constants.RUNMODE_COLUMN).equals(Constants.RUNMODE_NO)|| DataUtil.isSkip(testCaseName, xls)) {
			logger.log(Status.SKIP, "Runmode is set to NO ");

			throw new SkipException("Runmode is set to NO ");

		}
		System.out.println("Running " + testCaseName);
		ds.executeKeywords(testCaseName, xls, data);
		
		
	}}
