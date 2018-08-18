package com.HybridFramework.baseTest;

import java.io.FileInputStream;
import java.util.Properties;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import com.HybridFramework.driver.DriverClass;
import com.HybridFramework.util.DataUtil;
import com.HybridFramework.util.ExtentManager;
import com.HybridFramework.util.Xls_Reader;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class BaseTest {
	public Properties prop;		//represents properties.properties FILE
	public Properties envProp;	//represents production.properties FILE
	public Xls_Reader xls;
	public String testCaseName;
	public DriverClass ds;  // object of DriverClass
	public ExtentReports rep;
	// this will createtest will create a report . rep object is pointing towards extent.html file. in that you can 
	// create a test and this will return you object of the class known as ExtentTest
	public ExtentTest logger;

	
	@BeforeTest
	public void init() {
		// Ensure to keep the test case name same as Class name. because below testCaseName is 
		// intialized with class name that calls it 
	testCaseName=this.getClass().getSimpleName();
	System.out.println("Test case name to run - " + testCaseName);
	
	System.out.println("Package name - " + this.getClass().getPackage().getName());
	// get the name of the package . Ensure that name of the package and name of the test data excel name is same for below to work
	String arr[] = this.getClass().getPackage().getName().split("\\.");
	
	//extract the name of the package from the path
	System.out.println("Suite Name is - " + arr[arr.length-1]);
	String suiteName = arr[arr.length-1];
	// INITIALIZE THE PROPERTIES FILES 
		if (prop == null) {
			prop = new Properties();
			envProp = new Properties();

			try {
				FileInputStream fs = new FileInputStream(
						(System.getProperty("user.dir"))
								+ "//src//test//resources//properties.properties");
				prop.load(fs);
				String environment = prop.getProperty("env");
System.out.println("Environment = " +environment);
				fs = new FileInputStream((System.getProperty("user.dir"))
						+ "//src//test//resources//" + environment
						+ ".properties");
				envProp.load(fs);

			} catch (Exception e) {
				e.printStackTrace();
			}
			// here extracted package name is used to run the suite
			System.out.println(envProp.getProperty(suiteName + "_xls"));
			xls = new Xls_Reader(envProp.getProperty(suiteName + "_xls"));	
			// intialize the object of DriverClass
			
			// Here we are passing teh envProp object to DriverClass's setter method 
			// so that we can use envProp in Driver class
			ds = new DriverClass(); 
			ds.setEnvProp(envProp);	
			ds.setProp(prop);
		}

	}
	
	@DataProvider
	public Object[][] getData(){
		
		/*String testCaseName = "TestB";

		Xls_Reader xls = new Xls_Reader(System.getProperty("user.dir")
				+ "//testData//SuiteA.xls");
		*/return DataUtil.getTestData(testCaseName, xls);
	}
	

	
	@BeforeMethod  // called before executing each testcase 
	public void initTest(){
		// intialize the extent report object this will help logger object to be accessed through driverclass
				rep = ExtentManager.getInstance(envProp.getProperty("reportPath"));
				logger = rep.createTest(testCaseName);
				ds.setExtentTest(logger);
	}
	
	@AfterMethod  // called after executing each testcase 
	public void quit(){
		if (rep!=null)
			rep.flush();
		if(ds!=null)
		ds.quit();
	}
}
