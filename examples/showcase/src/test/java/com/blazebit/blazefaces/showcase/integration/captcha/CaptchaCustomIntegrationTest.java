package com.blazebit.blazefaces.showcase.integration.captcha;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

public class CaptchaCustomIntegrationTest extends AbstractIntegrationTest {

	protected static final String TEST_URL = BLAZE_SHOWCASE_UI
			+ "captchaCustom.xhtml";

	@BeforeClass
	public static void shouldInit() {
		driver.get(TEST_URL);
	}

	@Test
	public void shouldVerifyDOM() {
		try {

			// captcha image
			findElementBySelector("form#form div#recaptcha_widget_div div#recaptcha_image");

			// captcha input
			findElementBySelector("form#form div#recaptcha_widget_div input#recaptcha_response_field");

			// submit button
			findElementBySelector(escapeClientId("form:btn"));

		} catch (NoSuchElementException e) {
			assertTrue(false);
		}
	}

	@Test
	public void shouldNotPassCaptcha() {

		// write sth wrong
		WebElement input = findElementById("recaptcha_response_field");
		input.sendKeys("somethingWrong");

		// submit
		WebElement check = findElementBySelector(escapeClientId("form:btn"));
		check.submit();

		// look for returning message
		try {
			findElementBySelector("form#form .ui-messages .ui-messages-error .ui-messages-error-summary");
		} catch (NoSuchElementException e) {
			assertTrue(false);
		}

	}

}
