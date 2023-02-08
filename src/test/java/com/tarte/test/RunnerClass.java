package com.ritikaGupta.test;

import org.testng.annotations.*;
import testBase.BaseClass;

public class RunnerClass extends BaseClass{

	@Test
	public void launchsite() throws Exception {

		//Get URL
		driver.get("https://tartecosmetics.com");

		//Assert method for validation
		assertTrueVerification("home.topnav.makeup");
		
		//Method call to add 1st product
		addProductInBag("home.topnav.makeup", "topnav.makeup.face.foundation", "1");
		
		//Method call to add 2nd product
		addProductInBag("home.topnav.skincare", "topnav.skincare.dryness", "2");
		
		//Method call to add 3rd product
		addProductInBag("home.topnav.makeup", "topnav.makeup.eyes.eyeliner", "3");
		
		//Clicking my bag
		click("minicart_quantity");
		
		waitForPageToLoadCompletely(driver);
		//Thread.sleep(3000);
		
		//Deleting 1 product from my bag
		click("product_delete_btn");
		
		Thread.sleep(3000);
		
		//Validating count of my bag after deleting 1 product
		assertStringContainsVerification("minicart_quantity", "2");
	}

}

