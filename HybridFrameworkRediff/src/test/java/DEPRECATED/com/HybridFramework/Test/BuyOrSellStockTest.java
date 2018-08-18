package DEPRECATED.com.HybridFramework.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

public class BuyOrSellStockTest {
	WebDriver driver;
	
	@Test
	public void buyOrSellStockTest() throws InterruptedException{
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
		driver.manage().timeouts().pageLoadTimeout(60,TimeUnit.SECONDS);
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
		//wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.id("userpass"))));
		waitForPageToLoad();
		// additional delay
		//Thread.sleep(10000);
	    WebElement e = driver.findElement(By.id("portfolioid"));
	    Select s = new Select(e);
		s.selectByVisibleText("Ashi_29");
		waitForPageToLoad();
		int rNum=getRowWithCellData("Tata Steel");
		System.out.println("Row "+ rNum);
		driver.findElement(By.xpath("//table[@id='stock']/tbody/tr["+rNum+"]/td[1]")).click();
		driver.findElements(By.xpath("//input[@class='buySell']")).get(rNum-1).click();
		driver.findElement(By.id("buySellCalendar")).click();
		selectDate("11/02/2018");
		driver.findElement(By.name("buysellqty")).sendKeys("200");
		driver.findElement(By.name("buysellprice")).sendKeys("500");
		driver.findElement(By.id("buySellStockButton")).click();
		waitForPageToLoad();
		// verify if new stock quantity is updated

	}
	
	@Test
	public void checkTransactionHistoryTest(){
		
		
		int rNum=getRowWithCellData("Tata Steel");
		driver.findElement(By.xpath("//table[@id='stock']/tbody/tr["+rNum+"]/td[1]")).click();
		driver.findElements(By.xpath("//input[@class='equityTransaction']")).get(rNum-1).click();
		String actual=driver.findElement(By.xpath("//table[@id='stock']/tbody/tr["+rNum+"]/td[5]")).getText();
		List<WebElement> shares = driver.findElements(By.xpath("//table[@class='dataTable']/tbody/tr/td[3]"));
		List<WebElement> prices = driver.findElements(By.xpath("//table[@class='dataTable']/tbody/tr/td[4]"));
		
		int totalShares=0;
		int totalAmount=0;
		for(int i=0;i<prices.size();i++){
			int share = Integer.parseInt(shares.get(i).getText());
			int price = Integer.parseInt(prices.get(i).getText());
			totalShares = share + totalShares;
			totalAmount=totalAmount + (share*price);
		}
		System.out.println("Total shares - "+totalShares );
		System.out.println("Total Amount spent "+totalAmount );
		double average = Double.valueOf(totalAmount)/Double.valueOf(totalShares);
		System.out.println("Average - "+average );
		Assert.assertEquals(actual, average);
		// fix the decimals - Math
		
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
		wait(2);// wait of 2 sec between page status and jquery
		// check for jquery status
		i=0;
		while(i!=10){
	
			Boolean result= (Boolean) js.executeScript("return window.jQuery != undefined && jQuery.active == 0;");
			System.out.println(result);
			if(result )
			 	break;
			else
				 wait(2);
			 i++;
				
			}
		
		}
	
	public void selectDate(String d){
		// day, month , year
		Date current = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date selected = sd.parse(d);
			String day = new SimpleDateFormat("dd").format(selected);
			String month = new SimpleDateFormat("MMMM").format(selected);
			String year = new SimpleDateFormat("yyyy").format(selected);
			System.out.println(day+" --- "+month+" --- "+ year);
			String desiredMonthYear=month+" "+year;
			
			while(true){
				String displayedMonthYear=driver.findElement(By.cssSelector(".dpTitleText")).getText();
				if(desiredMonthYear.equals(displayedMonthYear)){
					// select the day
					driver.findElement(By.xpath("//td[text()='"+day+"']")).click();
					break;
				}else{
					
					if(selected.compareTo(current) > 0)
						driver.findElement(By.xpath("//*[@id='datepicker']/table/tbody/tr[1]/td[4]/button")).click();
					else if(selected.compareTo(current) < 0)
						driver.findElement(By.xpath("//*[@id='datepicker']/table/tbody/tr[1]/td[2]/button")).click();

				}
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int getRowWithCellData(String data){
		List<WebElement> rows = driver.findElements(By.xpath("//table[@id='stock']/tbody/tr"));
		for(int rNum=0;rNum<rows.size();rNum++){
			WebElement row = rows.get(rNum);
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for(int cNum=0;cNum<cells.size();cNum++){
				WebElement cell = cells.get(cNum);
				if(!cell.getText().trim().equals("") && data.contains(cell.getText()))
					return ++rNum;
			}
		}
		
		return -1;// not found
	}

	
}
