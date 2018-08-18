package com.HybridFramework.suiteA;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.HybridFramework.baseTest.BaseTest;
import com.HybridFramework.util.Constants;
import com.HybridFramework.util.DataUtil;
import com.aventstack.extentreports.Status;

public class TestB extends BaseTest {

	@Test(dataProvider = "getData")
	public void testB(Hashtable<String, String> data) throws Exception {

		logger.log(Status.INFO, "Starting " + testCaseName);

		if (data.get(Constants.RUNMODE_COLUMN).equals(Constants.RUNMODE_NO)|| DataUtil.isSkip(testCaseName, xls)) {
			logger.log(Status.SKIP, "Runmode is set to NO ");

			throw new SkipException("Runmode is set to NO ");
		}
		System.out.println("Running " + testCaseName);
		ds.executeKeywords(testCaseName, xls, data);

	}

}