package com.blazebit.blazefaces.showcase.integration.terminal;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

public class TerminalDialogIntegrationTest extends AbstractIntegrationTest {

	public static final String TEST_URL = BLAZE_SHOWCASE_UI
			+ "terminalDialog.xhtml";

	@BeforeClass
	public static void init() {
		driver.get(TEST_URL);
	}

	@Test
	public void shouldVerifyDOM() {

		try {

			findElementBySelector("form#form "
					+ escapeClientId("form:terminalImage"));
			findElementBySelector("form#form "
					+ escapeClientId("form:terminalDialog") + " "
					+ escapeClientId("form:terminal")
					+ " form input[type='text']");

		} catch (NoSuchElementException e) {
			assertTrue("Terminal Dialog showcase not verified.", false);
		}
	}

	@Test
	public void shouldManageDialogTerminal() {
		WebElement dialog = findElementBySelector("form#form "
				+ escapeClientId("form:terminalDialog"));

		assertTrue("Dialog should not displayed on startup.",
				!dialog.isDisplayed());

		findElementBySelector(
				"form#form " + escapeClientId("form:terminalImage")).click();

		waitUntilAllAnimationsComplete();

		assertTrue("Dialog should displayed on show.", dialog.isDisplayed());

		WebElement terminal = findElementBySelector("form#form "
				+ escapeClientId("form:terminalDialog") + " "
				+ escapeClientId("form:terminal"));

		assertTrue("Terminal should be displayed in dialog.",
				terminal.isDisplayed());

		WebElement commandLine = findElementBySelector(terminal,
				"form input[type='text']");

		commandLine.sendKeys("greet Blaze");

		commandLine.submit();

		waitUntilAjaxRequestCompletes();

		WebElement response = findElementByXpath(
				terminal,
				"//div[contains(@class, 'ui-terminal-content')]//div[contains(text(), 'Hello Blaze')]");

		assertTrue("Should response displayed.", response.isDisplayed());

	}
}
