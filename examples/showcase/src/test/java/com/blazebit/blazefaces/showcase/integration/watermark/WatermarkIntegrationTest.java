package com.blazebit.blazefaces.showcase.integration.watermark;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class WatermarkIntegrationTest extends AbstractIntegrationTest {

	@Before
	public void init() {
		String testUrl = toShowcaseUrl("watermark.jsf");
		driver.get(testUrl);
	}

	@Test
	public void shouldDisplayWatermarkInitially() throws InterruptedException {
		
		WebElement input = findElementById("form:keyword");
		assertThat(input.getAttribute("placeholder"),equalTo("Search with a keyword"));
	}
	
	@Test
	public void shouldSubmitEmptyTextIfInputIsEmptyWithAjax() throws InterruptedException {
		
		findElementById("form:ajax").click();
		waitUntilAjaxRequestCompletes();
		assertThat(findElementBySelector("span.ui-messages-error-detail").getText(),equalTo("Keyword: Validation Error: Value is required."));
	}
	
	@Test
	public void shouldSubmitVsssalueWithAjax() throws InterruptedException {
		
		WebElement input = findElementById("form:keyword");
		input.sendKeys("BlazeFaces");
		findElementById("form:ajax").click();
		waitUntilAjaxRequestCompletes();
		assertTrue(findElementBySelector("span.ui-messages-info-detail").getText().contains("BlazeFaces"));
	}
	
	@Test
	public void shouldSubmitEmptyTextIfInputIsEmptyWitRegular() throws InterruptedException {
		
		findElementById("form:regular").click();
		waitUntilAjaxRequestCompletes();
		assertThat(findElementBySelector("span.ui-messages-error-detail").getText(),equalTo("Keyword: Validation Error: Value is required."));
	}
	
	@Test
	public void shouldSubmitValueWithRegular() throws InterruptedException {
		
		WebElement input = findElementById("form:keyword");
		input.sendKeys("BlazeFaces");
		findElementById("form:ajax").click();
		waitUntilAjaxRequestCompletes();
		assertTrue(findElementBySelector("span.ui-messages-info-detail").getText().contains("BlazeFaces"));
	}
}
