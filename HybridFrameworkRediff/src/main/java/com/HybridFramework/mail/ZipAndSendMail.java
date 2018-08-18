package com.HybridFramework.mail;

 
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

 

public class ZipAndSendMail
{
	Properties emailProperties;
	Session mailSession;
	MimeMessage emailMessage;
	static String[] toEmails = { "sumitj5483@gmail.com" };
	static String fromUser = "sumitj5483@gmail.com";//just the id alone without @gmail.com
	static String password = "xxxxxxxxxx";

	
    public static void main(String[] args) throws Exception
    {
    	
		//report folder - extent reports
    	Properties prop = new Properties();
    	FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//production.properties");
    	prop.load(fs);
		String reportFolder=prop.getProperty("reportPath");
    	// find latest folder

                File dir = new File(reportFolder);
        	    File[] files = dir.listFiles();
        	    File lastModified = Arrays.stream(files).filter(File::isDirectory).max(Comparator.comparing(File::lastModified)).orElse(null);
        	    System.out.println(lastModified.getName());
        	    
        	//zip
                Zip.zipDir(reportFolder+"\\"+lastModified.getName(), reportFolder+"\\"+lastModified.getName()+".zip");
                
            //mail
                Mail javaEmail = new Mail();

        		javaEmail.setMailServerProperties();
        		

        		javaEmail.createEmailMessage(
        				"Automation Test Reports", // subject
        				"Please find the reports in attachment.", // body
        				reportFolder+"\\"+lastModified.getName()+".zip", // attachment path
        				"Reports.zip", // name of attachment
        				toEmails// receivers
        				);
        		javaEmail.sendEmail(fromUser,password);
        		
        		
      }
    



 
 

}