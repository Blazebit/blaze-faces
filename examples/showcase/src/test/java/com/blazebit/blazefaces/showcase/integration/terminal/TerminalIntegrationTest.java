package com.blazebit.blazefaces.showcase.integration.terminal;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

public class TerminalIntegrationTest extends AbstractIntegrationTest {

	public static final String TEST_URL = BLAZE_SHOWCASE_UI + "terminal.xhtml";

	@BeforeClass
	public static void init() {
		driver.get(TEST_URL);
	}

	@Test
	public void shouldVerifyDOM() {

		try {

			findElementBySelector("form#form "
					+ escapeClientId("form:terminal")
					+ " form input[type='text']");

		} catch (NoSuchElementException e) {
			assertTrue("Terminal basic showcase not verified.", false);
		}
	}

	@Test
	public void shouldTerminalGreet() throws InterruptedException {

		WebElement commandLine = findElementBySelector("form#form "
				+ escapeClientId("form:terminal") + " form input[type='text']");
		commandLine.sendKeys("greet Blaze");

		commandLine.submit();

		waitUntilAjaxRequestCompletes();

		WebElement response = findElementByXpath("//form[@id='form']//div[@id='form:terminal']/div[contains(@class, 'ui-terminal-content')]//div[contains(text(), 'Hello Blaze')]");

		assertTrue("Should response displayed.", response.isDisplayed());

	}
}
