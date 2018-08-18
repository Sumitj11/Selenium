package com.HybridFramework.sampleTest;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class SoftAssertTest {

	@Test
	public void softAssert(){
		SoftAssert softAssertion = new SoftAssert();
		System.out.println("SoftAssert method started :");
		softAssertion.fail("Fail 1 ");
		softAssertion.fail("Fail 2 ");
		softAssertion.fail("Fail 3 ");
		System.out.println("SoftAssert method completed:");
		softAssertion.assertAll();

	}
}
