package com.HybridFramework.keywords;

import com.aventstack.extentreports.Status;

public class AppKeywords extends GenericKeywords {

	public void validateLogin() throws InterruptedException {
		logger.log(Status.INFO, "Validating Login");
		String expectedTitle = testData.get(dataKey);

//write code to wait for page load
		String actualTitle = driver.getTitle();
	/*	WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.wait(2000);
	*/	if(!expectedTitle.equals(actualTitle)){
			reportFailure("Title did not match after Login. Expected -> "+ expectedTitle +" ||  Actual title found -> " + actualTitle);
	}else 
		logger.log(Status.PASS, "Actual -> " + actualTitle + " Expected -> " + expectedTitle);
	}
	public void defalutLogin(){
		
		String userName = envProp.getProperty("userid");
		String password = envProp.getProperty("password");
		System.out.println("Login with UserName : "+userName +" & Password : "+password);
	}
	
}
