package DEPRECATED.com.HybridFramework.Test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PortFolioTest {
	WebDriver driver;
	@Test(priority=1)
	public void createPortfolioTest() throws InterruptedException{

		String browser = "Mozilla";// xls, xml
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
		driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
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
		//waitForPageToLoad();
		clickAndWait("//*[@id='createPortfolio']/img","//*[@id='createPortfolioButton']",10);
		driver.findElement(By.id("create")).clear();
		driver.findElement(By.id("create")).sendKeys("AshishThakur1");
		driver.findElement(By.id("createPortfolioButton")).click();
		// verify selected value
		waitForPageToLoad();
		waitTillSelectionToBe("AshishThakur1");

	
	}
	
	

	@Test(priority=2,dependsOnMethods={"createPortfolioTest"})
	public void deletePortFolioTest(){
		driver.findElement(By.id("deletePortfolio")).click();
		driver.switchTo().alert().accept();
		driver.switchTo().defaultContent();
	}
	
	
	public void clickAndWait(String xpathExpTarget, String xpathExpWait, int maxTime){
		for(int i=0;i<maxTime;i++){
			// click
			driver.findElement(By.xpath(xpathExpTarget)).click();
			// check presence of other ele
			if(isElementPresent(xpathExpWait) && driver.findElement(By.xpath(xpathExpWait)).isDisplayed()){
				// if present - success.. return
				return;
			}else{
				// else wait for 1 sec
				wait(1);
			}
			
		}
		// 10 seconds over - for loop - comes here
		Assert.fail("Target element coming after clicking on "+xpathExpTarget );
	}
	

	public boolean isElementPresent(String xpathExp){
		int s = driver.findElements(By.xpath(xpathExp)).size();
		if(s==0)
			return false;
		else
			return true;
	}

	public void wait(int time){
		try {
			Thread.sleep(time*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void waitForPageToLoad(){
		JavascriptExecutor js = (JavascriptExecutor)driver;
		int i=0;
		
		while(i!=10){
		String state = (String)js.executeScript("return document.readyState;");
		System.out.println(state);

		if(state.equals("complete"))
			break;
		else
			wait(2);

		i++;
		}
		// check for jquery status
		i=0;
		while(i!=10){
	
			Long d= (Long) js.executeScript("return jQuery.active;");
			System.out.println(d);
			if(d.longValue() == 0 )
			 	break;
			else
				 wait(2);
			 i++;
				
			}
		
		}
	public void waitTillSelectionToBe(String expected) {
		int i=0;
		while(i!=10){
			WebElement e = driver.findElement(By.id("portfolioid"));
			Select s = new Select(e);
			String actual = s.getFirstSelectedOption().getText();
			System.out.println(actual);
			if(actual.equals(expected))
				return;
			else
				wait(1);			
				i++;	
		}
		// reach here
		Assert.fail("Value never changed in Select box");
		
		
	}
}
