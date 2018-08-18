package com.HybridFramework.sampleTest;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTestSAMPLE {

	WebDriver driver = null;

	@Test
	public void loginTest(){		
		String browser = "Chrome";// xls, xml
		// script
		if(browser.equals("Mozilla")){
			driver = new FirefoxDriver();
		}else if(browser.equals("Chrome")){
			driver = new ChromeDriver();
		}else if(browser.equals("IE")){
			driver = new InternetExplorerDriver();
		}else if(browser.equals("Edge")){
			driver = new EdgeDriver();
		}
		
		driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(20,TimeUnit.SECONDS);
		driver.get("http://in.rediff.com/");
		driver.findElement(By.xpath("//*[@id='homewrapper']/div[5]/a[3]/div/u")).click();
		driver.findElement(By.xpath("//*[@id='signin_info']/a[1]")).click();
		driver.findElement(By.id("useremail")).sendKeys("ashishthakur1983");
		driver.findElement(By.id("emailsubmit")).click();
		// wait 
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("userpass"))));
		driver.findElement(By.id("userpass")).sendKeys("pass@1234");
		driver.findElement(By.id("userpass")).sendKeys(Keys.ENTER);
		
		boolean result = isElementPresent("//*[@id='username']/a");
		Assert.assertTrue(result, "Not able to login");		
	}
	
	public boolean isElementPresent(String xpathExp){
		int s = driver.findElements(By.xpath(xpathExp)).size();
		if(s==0)
			return false;
		else
			return true;
	}
}
