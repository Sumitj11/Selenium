package com.zoho.project.util;

//http://relevantcodes.com/Tools/ExtentReports2/javadoc/index.html?com/relevantcodes/extentreports/ExtentReports.html

import java.io.File;
import java.util.Date;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager  {
	// private static ExtentReports extent;

	private static ExtentReports extent;

	public static ExtentReports getInstance() {
		if (extent == null) {
			Date d = new Date();
			String fileName = d.toString().replace(":", "_").replace(" ", "_")
					+ ".html";
			String filepath = "F:\\Learning Java\\DataDriven_Zoho_Project\\Reports\\" +fileName;
			extent = new ExtentReports(filepath, true, DisplayOrder.NEWEST_FIRST);
			
			
			/*extent = new ExtentReports(
					"D:\\Sumits.jain\\Desktop\\Java\\Data_Driven_Framework\\Reports\\"+ fileName, true, DisplayOrder.NEWEST_FIRST);
			*/// extent = new
			// ExtentHtmlReporter("D:\\Sumits.jain\\Desktop\\Java\\Data_Driven_Framework\\Reports\\"+fileName,
			// true, DisplayOrder.NEWEST_FIRST);

			extent.loadConfig(new File(System.getProperty("user.dir")+ "\\ReportsConfig.xml"));
			// optional
			extent.addSystemInfo("Selenium Version", "2.53.0").addSystemInfo("Environment", "QA");
		}
		return extent;
	}
}
