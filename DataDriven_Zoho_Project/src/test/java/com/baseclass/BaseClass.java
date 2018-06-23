package com.baseclass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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


//import util.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.zoho.project.util.ExtentManager;

public class BaseClass {
	public WebDriver driver;
	public java.util.Properties prop;
	public java.util.Properties envProp;

	/*
	 * Extent Report Initialize the extent report first . Verify if the object
	 * is present or not if not than create the object. Initiate the test object
	 * ExtentTest
	 */
	public ExtentReports reports = ExtentManager.getInstance();
	public ExtentTest logger;
	public SoftAssert softAssert;
	
	
	public void init() {
		// Initialise the properties obj
		if (prop == null) {
			prop = new java.util.Properties();
			envProp = new java.util.Properties();
			try {
				FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+ "//src//test//resources//projectConfig.properties");
				prop.load(fs);
				/*here environment is intialized and same needs to pass to new properties variable which is envProp so that entire
				file is loaded in envProp	
				*/
				String env= prop.getProperty("env");
				fs = new FileInputStream(System.getProperty("user.dir")+ "//src//test//resources//"+env+ ".properties");
				envProp.load(fs);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	public void openBrowser(String browserTypeKey) {
		init();
		logger.log(LogStatus.INFO, "Opening Borwser - " + browserTypeKey);

		if (browserTypeKey.equals("Chrome"))
			driver = new ChromeDriver();
		if (browserTypeKey.equals("Firefox"))
			driver = new FirefoxDriver();
		if (browserTypeKey.equals("IE"))
			driver = new InternetExplorerDriver();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		logger.log(LogStatus.INFO, "Borwser Opened");

	}

	public void navigate(String urlKey) {
		logger.log(LogStatus.INFO,
				"Naviagating to URL - " + prop.getProperty(urlKey));
		driver.get(envProp.getProperty(urlKey));

	}

	// Function where xpath and data key is passed and actual data needs to be
	// fetched from properties file
	public void type(String locatorKey, String dataKey) {
		logger.log(LogStatus.INFO, "Typing in - " + dataKey);

		getElement(locatorKey).sendKeys(prop.getProperty(dataKey));

		// driver.findElement(By.xpath(prop.getProperty(xPathEleKey))).sendKeys(prop.getProperty(dataKey));
	}

	// Function where xpath and actual data is passed
	public void type2(String locatorKey, String dataKey) {
		logger.log(LogStatus.INFO, "Typing in - " + dataKey);

		getElement(locatorKey).sendKeys(dataKey);

		// driver.findElement(By.xpath(prop.getProperty(xPathEleKey))).sendKeys(prop.getProperty(dataKey));
	}

	public void clickAndWait(String locatorKey_clicked,String locatorKey_IsPresent) {
		int count=5;
		for(int i=0;i<count;i++){
			getElement(locatorKey_clicked).click();
			wait(2);
			if(isElementPresent(locatorKey_IsPresent))
				break;
			
		}
	}
	
	public void click(String locatorKey) {
		logger.log(LogStatus.INFO, "Clicking on - " + locatorKey);
		getElement(locatorKey).click();
		logger.log(LogStatus.INFO, "Clicked Successfully - " + locatorKey);
		waitForPageLoad();
	}

	// Finding Element on the page and returning the same
	public WebElement getElement(String locatorKey) {

		WebElement e = null;
		try {
			if (locatorKey.endsWith("_id"))
				e = driver.findElement(By.id(prop.getProperty(locatorKey)));

			else if (locatorKey.endsWith("_name"))
				e = driver.findElement(By.name(prop.getProperty(locatorKey)));

			else if (locatorKey.endsWith("_xpath"))
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			else
				reportFail("Locator not correct - " + locatorKey);

		} catch (Exception ex) {
			reportFail(ex.getMessage());
			ex.printStackTrace();
			Assert.fail("Failed the Test " + ex.getMessage());
			System.out.println("");
		}
		return e;

	}

	// -------------------Validation Functions---------------------------------
	public void verifyTitle() {

	}

	public boolean isElementPresent(String locatorKey) {
		List<WebElement> elementList = null;

		if (locatorKey.endsWith("_id"))
			elementList = driver.findElements(By.id(prop.getProperty(locatorKey)));
		else if (locatorKey.endsWith("_name"))
			elementList = driver.findElements(By.name(prop.getProperty(locatorKey)));
		else if (locatorKey.endsWith("_xpath"))
			elementList = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		else {
			reportFail("Locator not correct - " + locatorKey);
			Assert.fail("Locator not correct - " + locatorKey);
		}
		if (elementList.size() == 0)
			return false;
		else
			return true;

	}

	public boolean verifyText(String locatorKey, String expectedTextKey) {
		String actualText = getElement(locatorKey).getText().trim();
		String expectedText = prop.getProperty(expectedTextKey);

		System.out.println(actualText);
		System.out.println(expectedText);
		if (actualText.equals(expectedText))
			return true;
		else
			return false;

	}

	// -------------------Reporting Functions---------------------------------
	public void reportPass(String message ) {
		logger.log(LogStatus.PASS, message + "Pass" );
	}

	public void reportFail(String error) {
		logger.log(LogStatus.FAIL, "Fail");
		takeScreenShot();
		Assert.fail(error);
	}

	public String takeScreenShot() {
		Date d = new Date();
		String fileName = d.toString().replace(":", "_").replace(" ", "_")
				+ ".png";
		String fileName1 = (System.getProperty("user.dir") + "\\Reports")
				+ fileName;
		File screeshot = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(screeshot,
					new File(System.getProperty("user.dir") + fileName));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Screenshot Taken Successfuly for \"" + fileName
				+ "\" - Check the Path " + fileName1);
		System.out.println("");
		return fileName1;

	}

	public void acceptAlert(){
		
		WebDriverWait wait = new WebDriverWait(driver,5);
		wait.until(ExpectedConditions.alertIsPresent());
		logger.log(LogStatus.INFO, "Accepting Alert");
		driver.switchTo().alert().accept();
		driver.switchTo().defaultContent();
	}
	// Using javascript to verify the page load state.
	// https://developer.mozilla.org/en-US/docs/Web/API/Document/readyState
	public void waitForPageLoad() {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		String state = (String) js.executeScript("return document.readyState");
		while (!state.equals("complete")) {
			wait(3);
			state = (String) js.executeScript("return document.readyState");
		}
	}

	public void wait(int timeToWaitInSec) {
		try {
			Thread.sleep(timeToWaitInSec * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ******************************* App Functions
	// ***************************************

	public boolean doLogin(String userId, String password)
			throws InterruptedException {
		click("loginlink_xpath");
		System.out.println("login link clicked");
		logger.log(LogStatus.INFO, "Login page clicked ");
		// To find the iframe on the page
		// System.out.println(driver.findElements(By.tagName("iframe")).size());

		waitForPageLoad();
		type2("loginID_xpath", userId);
		logger.log(LogStatus.INFO, "UserId entered - " + userId);
		type2("password_xpath", password);
		logger.log(LogStatus.INFO, "Password entered - " + password);
		click("submit_xpath");
		logger.log(LogStatus.INFO, "Submit Button clicked");
        waitForPageLoad();
        wait(5);
		if (isElementPresent("crmlink_xpath")) {
			logger.log(LogStatus.INFO, "Login Successfull");
			return true;

		} else
			logger.log(LogStatus.FAIL, "Login Failed");

		return false;

	}

	public int getLeadRowNum(String leadLastName) {
		logger.log(LogStatus.INFO, "Finding the Lead Name -  " + leadLastName);

		List<WebElement> leadNames = driver.findElements(By.xpath(prop.getProperty("leadNameCol_xpath")));
		for (int i = 0; i < leadNames.size(); i++) {
			//System.out.println("Lead Name - " + leadNames.get(i).getText());
			if (leadNames.get(i).getText().trim().equals(leadLastName)) {
				logger.log(LogStatus.INFO, "Lead Name - " + leadLastName + " found on Row No. " + i);
				return (i + 1);
			}
		}
		logger.log(LogStatus.INFO, "Lead not found ");
		return -1;

	}

	public void clickOnLead(String leadLastName) {
		// TODO Auto-generated method stub
		logger.log(LogStatus.INFO, "Clicking the Lead Checkbox for " + leadLastName);
		int rowNum = getLeadRowNum(leadLastName);
		driver.findElement(By.xpath(prop.getProperty("leadCheckBoxPart1_xpath") + rowNum
						+ prop.getProperty("leadCheckBoxPart2_xpath"))).click();

	}
	
/*	public void selectDate(String d){
		1. Find the month and the year in date given in excel
		2. Compare it with current month and year given in calendar
		3. Accordingly navigate front or back
		4. select the date
		

		
		logger.log(LogStatus.INFO, "Selecting the date "+d);
		// convert the string date(input) in date object
		click("dateTextField_xpath");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date dateTobeSelected = sdf.parse(d);
			Date currentDate = new Date();
			sdf = new SimpleDateFormat("MMMM");
			String monthToBeSelected=sdf.format(dateTobeSelected);
			sdf = new SimpleDateFormat("yyyy");
			String yearToBeSelected=sdf.format(dateTobeSelected);
			sdf = new SimpleDateFormat("d");
			String dayToBeSelected=sdf.format(dateTobeSelected);
			//June 2016
			String monthYearToBeSelected=monthToBeSelected+" "+yearToBeSelected;
			
			while(true){
				if(currentDate.compareTo(dateTobeSelected)==1){
					//back
					click("back_xpath");
				}else if(currentDate.compareTo(dateTobeSelected)==-1){
					//front
					click("forward_xpath");
				}
				
				if(monthYearToBeSelected.equals(getText("monthYearDisplayed_xpath"))){
					break;
				}
				
				
			}
			driver.findElement(By.xpath("//td[text()='"+dayToBeSelected+"']")).click();
			logger.log(LogStatus.INFO, "Date Selection Successful "+d);

			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
			

}
