package com.HybridFramework.keywords;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;

public class AppKeywords extends GenericKeywords {

	public void validateLogin() throws InterruptedException   {
		Thread.sleep(5000);
		logger.log(Status.INFO, "Validating Login");
		boolean result = isElementPresent("rename_xpath");
		logger.log(Status.INFO, "Validating Login Results = " + result);
		}
	
	public void login() throws InterruptedException{
		logger.log(Status.INFO, "Login into the site");
		getObject("money_xpath").click();
		logger.log(Status.INFO, "Click on Signin Link");
		getObject("signin_xpath").click();
			String userName="";
			String password="";
		
			if(testData.get("UserName")==null){
			userName = envProp.getProperty("defaultuserid");
			password = envProp.getProperty("defalutpassword");
		}else {
			
			userName = testData.get("UserName");
			password = testData.get("Password");
		
		}	
		getObject("useremail_xpath").sendKeys(userName);
		getObject("emailsumbmitButton_xpath").click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		// wait for visiblity of the object
		wait.until(ExpectedConditions.visibilityOf(getObject("userpass_xpath")));
		// check the state of the object
		wait.until(ExpectedConditions.elementToBeClickable(getObject("userpass_xpath")));
			
		getObject("userpass_xpath").sendKeys(password);
		getObject("loginsubmitButton_xpath").click();
		logger.log(Status.INFO, "Login button clicked " + envProp.getProperty("loginsubmitButton_xpath"));
		Thread.sleep(5000);
		acceptAlert();
	}
	
	public void validatePortfolio() {
		logger.log(Status.INFO,"Searching in dropdown -> " + testData.get(dataKey));
		// validate whether the value is present in dropdown
		List<WebElement> options = new Select(getObject(objectKey)).getOptions();
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).getText().equals(testData.get(dataKey)))
				break;
			if (i == options.size() - 1)
				logger.log(Status.INFO,"Option Not found in Dropdown -> "+testData.get(dataKey) );
					
			//	reportFailure("Option not found in Dropdown -> "+ testData.get(dataKey));
			
			//logger.log(Status.INFO, "Value Not found in Dropdown -> "+ testData.get(dataKey));
				//reportFailure("Option not found in Dropdown -> "+ testData.get(dataKey));
		}
		logger.log(Status.INFO, "Value found in Dropdown -> "+ testData.get(dataKey));
		new Select(getObject(objectKey)).selectByVisibleText(testData.get(dataKey));
	}
	
	public void validateDeletion() {
		logger.log(Status.INFO,"Searching in dropdown -> " + testData.get(dataKey));
		// validate whether the value is present in dropdown
		List<WebElement> options = new Select(getObject(objectKey)).getOptions();
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).getText().equals(testData.get(dataKey)))
				break;
			if (i == options.size() - 1)
				logger.log(Status.INFO,"Value Not found in Dropdown - Deletion successful-> "+testData.get(dataKey) );
					
			//	reportFailure("Option not found in Dropdown -> "+ testData.get(dataKey));
			
			//logger.log(Status.INFO, "Value Not found in Dropdown -> "+ testData.get(dataKey));
				//reportFailure("Option not found in Dropdown -> "+ testData.get(dataKey));
		}
		logger.log(Status.INFO, "Value found in Dropdown Deletion was not successfull -> "+ testData.get(dataKey));
	}
	
	public void addStock() throws InterruptedException{
		logger.log(Status.INFO,"Adding Stock -> " + testData.get(dataKey));
		click("addStock_xpath");
		waitForPageToLoad();
		type("addStockName_xpath","StockName");
		click("firstStockInList_xpath");
		//click("addStockCalender_xpath");
		//selectDate(testData.get("Date"));
		type("stockAddDate_xpath","Date");
		type("addStockQty_xpath","Quantity");
		type("addStockPrice_xpath","PurchasePrice");
		click("addStockButton_xpath");
		logger.log(Status.INFO, "Stock Added -> " + testData.get("StockName") );
		waitForPageToLoad();
		int rNum=getRowWithCellData(testData.get("StockName"));
		if (rNum==-1)
			reportFailure("Could not find the Stock in Portfolio -> " + testData.get("StockName"));
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
	
	public void selectDate(String d){
		// day, month , year
		Date current = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date selected = sd.parse(d);
			String day = new SimpleDateFormat("d").format(selected);
			String month = new SimpleDateFormat("MMMM").format(selected);
			String year = new SimpleDateFormat("yyyy").format(selected);
			System.out.println(day+" --- "+month+" --- "+ year);
			String desiredMonthYear=month+" "+year;
			System.out.println("desiredMonthYear ->" + desiredMonthYear);
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
	

}
	
