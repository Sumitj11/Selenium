package com.HybridFramework.keywords;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.HybridFramework.util.ExtentManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class GenericKeywords {
	
	public Properties envProp;
	public Properties prop;
	public String objectKey;
	public String dataKey;
	public Hashtable<String,String> testData;
	public WebDriver driver;
	public ExtentTest logger;
	private String proceedOnFail;
	public SoftAssert softAssert = new SoftAssert();
	public String screenshotFolderPath;
	
	/**********************Setter Functions **************************/
	public void setEnvProp(Properties envProp) {
		this.envProp = envProp;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public void setData(Hashtable<String, String> testData) {
		this.testData = testData;
	}
	public void setExtentTest(ExtentTest logger) {
		this.logger  = logger;
	}
	public void setScreenshotFolderPath(String screenshotFolderPath) {
		this.screenshotFolderPath  = screenshotFolderPath;
	}
	public void setProceedOnFail(String proceedOnFail) {
		this.proceedOnFail  = proceedOnFail;
	}
	
	/**********************Setter Functions **************************/
	public void openBrowser() {
		logger.log(Status.INFO, "Open Browser -> " + testData.get(dataKey) );
		if(testData.get(dataKey).equals("Firefox"))
		
			// here all setting should be taken care such PROFILING 
			driver = new FirefoxDriver();
		if(testData.get(dataKey).equals("Chrome"))
			driver = new ChromeDriver();
		if(testData.get(dataKey).equals("IE"))
			driver = new InternetExplorerDriver();
		
		// set implicit wait
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	public void navigate() {
		logger.log(Status.INFO,"Navigate to URL -> " + envProp.getProperty(objectKey));
		//System.out.println("Navigate to URL -> " + envProp.getProperty(objectKey));
	//	find element and pass the object. envProp is used because value of the objectkey is in Properties file
		driver.get(envProp.getProperty(objectKey));
		
	}

	public void type() {
		logger.log(Status.INFO,"Type text -> "  + objectKey + "  ->  " + envProp.getProperty(objectKey));
		//System.out.println("Type text-> " + envProp.getProperty(objectKey));

		/*    OLD CODE replaced with common function 
		 	testData is used because the value of the datakey is in Excel file
		    driver.findElement(By.xpath(envProp.getProperty(objectKey))).sendKeys(testData.get(dataKey));
		*/ 
		// Use common method to get object replace below with driver.findElement(By.xpath(envProp.getProperty(objectKey)))
		getObject(objectKey).sendKeys(testData.get(dataKey));
	}

	public void type2() {
		logger.log(Status.INFO,"Type2 -> " + objectKey + "  ->  "+envProp.getProperty(objectKey));
		//System.out.println("Type2 -> " + envProp.getProperty(objectKey));
		getObject(objectKey).sendKeys(testData.get(dataKey));
	}

	public void click() {
		logger.log(Status.INFO,"Clicking button -> "+ objectKey + "  ->  "+envProp.getProperty(objectKey));
//		System.out.println("Clicking button -> "+ envProp.getProperty(objectKey));
		
		/*
		 * OLD CODE repalced with common function
		 * driver.findElement(By.xpath(envProp.getProperty(objectKey))).click();
		 */
		getObject(objectKey).click();
	}

	public void validateTitle(){
		logger.log(Status.INFO,"Validating Title -> " + objectKey + "  ->  "+ envProp.getProperty(objectKey));
//		System.out.println("Validating Title -> " + envProp.getProperty(objectKey));
		String expectedTitle = envProp.getProperty(objectKey);

// write code to wait for page load
		String actualTitle = driver.getTitle();
		if(!expectedTitle.equals(actualTitle)){
			reportFailure("Title did not match. Expected -> "+ expectedTitle +" ||  Actual title found -> " + actualTitle);
		}
	}
	
	public void validateElementPresent(){
		logger.log(Status.INFO,"Vlaidating Login link is Present -> " + objectKey + "  ->  "+ envProp.getProperty(objectKey));
//		System.out.println("Vlaidating Login link is Present " + envProp.getProperty(objectKey));
		if(!isElementPresent(objectKey))
			//logger.log(Status.ERROR,"Element not present " +  objectKey + "  ->  "+envProp.getProperty(objectKey));
			reportFailure("Element not present "  +  objectKey + "  ->  "+envProp.getProperty(objectKey));
}
	// Common function to find the object using xpath/css/id
	public WebElement getObject(String objectKey) {
		WebElement element = null;
		try {
			if (objectKey.endsWith("_xpath"))
				element = driver.findElement(By.xpath(envProp
						.getProperty(objectKey)));
			else if (objectKey.endsWith("_id"))
				element = driver.findElement(By.id(envProp
						.getProperty(objectKey)));
			else if (objectKey.endsWith("_css"))
				element = driver.findElement(By.cssSelector(envProp.getProperty(objectKey)));
			else if (objectKey.endsWith("_name"))
				element = driver.findElement(By.name(envProp.getProperty(objectKey)));

			WebDriverWait wait = new WebDriverWait(driver, 20);
			// wait for visiblity of the object
			wait.until(ExpectedConditions.visibilityOf(element));
			// check the state of the object
			wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Object Not Found ->" + objectKey);
		}
		return element;
	}
	/*******************************Validation Functions************************************/
	// Function to check if the element is present or not 
	public boolean isElementPresent(String objectKey) {
		
		java.util.List<WebElement> elementList = null;
		
			if (objectKey.endsWith("_xpath"))
				elementList = driver.findElements(By.xpath(envProp
						.getProperty(objectKey)));
			else if (objectKey.endsWith("_id"))
				elementList = driver.findElements(By.id(envProp
						.getProperty(objectKey)));
			else if (objectKey.endsWith("_css"))
				elementList = driver.findElements(By.cssSelector(envProp.getProperty(objectKey)));
			else if (objectKey.endsWith("_name"))
				elementList = driver.findElements(By.name(envProp.getProperty(objectKey)));

			/*WebDriverWait wait = new WebDriverWait(driver, 20);
			// wait for visiblity of the object
			wait.until(ExpectedConditions.visibilityOfAllElements(elementList));
			*/			
		if(elementList.size()==0){
			logger.log(Status.ERROR, "Object not found ->" + objectKey);
			return false;}
		return true;
	}
/*******************************REPORTING FUNCTIONS*************************************/		
	public void reportFailure(String failureMessage){
		
		// fail the test case
		// take screenshot
		// embed it in log file
		if(proceedOnFail.equals("Y")){
			logger.log(Status.ERROR,"SoftAlert Program will Continue-> " + failureMessage); 
	takeScreenshot();
			softAssert.fail(failureMessage);
		}else{ 
			// below code is equivalent to Assert.fail();
			logger.log(Status.FAIL,"Alert Program Stopped -> " + failureMessage);
			takeScreenshot();
			softAssert.fail(failureMessage);
			softAssert.assertAll();
		}
			//	Assert.fail(failureMessage);
	}

public void takeScreenshot(){
	
	//screenshotFolderPath
	Date d = new Date();
	String screenShotFileName = d.toString().replace(":", "_").replace(" ", "_")+ ".png";
	//String fileName1 = (System.getProperty("user.dir") + "\\Report\\")+ fileName;
//	take screenshot
	File screeshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	try {
		//FileUtils.copyFile(screeshot, new File(System.getProperty("user.dir") +"//Report//" + fileName));
		FileUtils.copyFile(screeshot,new File(ExtentManager.screenshotFolderPath+screenShotFileName));
		logger.log(Status.INFO,"Screenshot -> " +logger.addScreenCaptureFromPath(ExtentManager.screenshotFolderPath + screenShotFileName));
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//put screenshot file in the report
	
	/*System.out.println("Screenshot Taken Successfuly for \"" + fileName + "\" - Check the Path " + fileName1);
	System.out.println("");
	*///return fileName1;
	
	
}
/*******************************FINISHING TEST*************************************/		


//if sometimes driver is not initialized or null than call quit
public void quit(){
	
	if(driver!=null)
		logger.log(Status.INFO, "Driver is not null, hence quiting the driver");
		//driver.quit();
}
}
