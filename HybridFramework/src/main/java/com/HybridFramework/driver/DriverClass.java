package com.HybridFramework.driver;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Properties;

import com.HybridFramework.keywords.AppKeywords;
import com.HybridFramework.util.Constants;
import com.HybridFramework.util.Xls_Reader;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class DriverClass {

	public Properties envProp;
	public Properties prop;
	AppKeywords app;
	public ExtentTest logger;

	public void executeKeywords(String testCaseName, Xls_Reader xls,
			Hashtable<String, String> testData) throws Exception {
//		get row counts in the keyword sheet
		app = new AppKeywords();
		
		/* OLD CODE
		Comment: Below is the old way to name the sheet. 
		int rows = xls.getRowCount("Keywords");
		Comment: According to new  hardcoding of sheet name "Keywords" is replaced with constant
		*/
		int rows =xls.getRowCount(Constants.KEYWORDS_SHEET);
		System.out.println("No. of Rows in Keywords sheet = " + rows);

//		send properties to Keywords class (AppKeywords and GenericKeywords)
		app.setEnvProp(envProp);
		app.setProp(prop);
//		send the data as well from hashtable
		app.setData(testData);
		System.out.println("---------------------------------------------");
		
		for (int rNum = 2; rNum < rows; rNum++) {
			String tcid = xls.getCellData(Constants.KEYWORDS_SHEET,Constants.TCID_COLUMN, rNum);
			if (tcid.equals(testCaseName)) {
				//System.out.println("---------------------------------------------");
				String keyword = xls.getCellData(Constants.KEYWORDS_SHEET,Constants.KEYWORD_COLUMN, rNum);
				String object = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.OBJECT_COLUMN, rNum);
				String dataKey = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.DATA_COLUMN, rNum);
				String proceedOnFail = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.PROCEEDONFAIL_COLUMN, rNum);
				// below command is to get the data from the hashtable using the key value. in dataKey we have the
				// key using which we can fetch value
			 	String data = testData.get(dataKey);

			 	logger.log(Status.INFO, "Test Case Data -> " +  tcid + " | " + keyword + " | " + envProp.getProperty(object) + " | " + dataKey + " | "+ data);
 	
			//System.out.println(tcid + " | " + keyword + " | " + envProp.getProperty(object) + " | " + dataKey + " | "+ data);

				app.setDataKey(dataKey);
				app.setObjectKey(object);
				app.setExtentTest(logger);
				app.setProceedOnFail(proceedOnFail);
			/*	
				COMMENT - it is difficult to write ifelse statement for all the objects as written below hence we use 
			    the concept of REFLECTIONS API in java
				if (keyword.equals("openBrowser"))
					app.openBrowser();
				else if (keyword.equals("navigate"))
					app.navigate();
				else if (keyword.equals("type"))
					app.type();
				else if (keyword.equals("type2"))
					app.type2();
				else if (keyword.equals("click"))
					app.click();
				else if (keyword.equals("validateLogin"))
					app.validateLogin();
			*/
			/*	/**********************REFLECTIONS API******************************//*
				COMMENT - since keyword name from Keywords sheet and funtions names are similar we use reflections API
				below code will replace all of the above if else section
			*/
			
				/*try {
					logger.log(Status.INFO, "Calling method -> " + keyword);
					method = app.getClass().getMethod(keyword);
					method.invoke(app);

				} catch (Exception e) {
					logger.log(Status.ERROR, "Error occured while executing method -> " + keyword);
					logger.log(Status.ERROR, " Error ->" + e.getMessage());
					e.printStackTrace();
				}
				 Here due to above try catch block even though element was not found it was catching the exception
				and countinuing the exectuion with rest of the test case. to stop the execcution if element is not found
				removing the try catch block */
				Method method;
				logger.log(Status.INFO, "Calling method -> " + keyword);
				method = app.getClass().getMethod(keyword);
				method.invoke(app);

				} 
			
		}app.softAssert.assertAll();
		
		System.out.println("---------------------------------------------");

	}
	public void quit(){
		if(app!=null)
		app.quit();
	}
	/************************Getter & Setter functions*********************************/
	public Properties getEnvProp() {
		return envProp;
	}

	public void setEnvProp(Properties envProp) {
		this.envProp = envProp;
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}
	public void setExtentTest(ExtentTest logger) {
		this.logger  = logger;
	}
}
