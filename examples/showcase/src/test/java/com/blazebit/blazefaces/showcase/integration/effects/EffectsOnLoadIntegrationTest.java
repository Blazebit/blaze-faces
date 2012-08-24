package com.blazebit.blazefaces.showcase.integration.effects;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

public class EffectsOnLoadIntegrationTest extends AbstractIntegrationTest {

	protected static final String TEST_URL = BLAZE_SHOWCASE_UI
			+ "effectMessages.xhtml";

	@BeforeClass
	public static void init() {
		driver.get(TEST_URL);
	}

	@Test
	public void shouldVerifyDOM() {

		try {

			findElementById("messages");

			findElementById("text");

			findElementById("submit");

		} catch (NoSuchElementException e) {
			assertTrue("Effects On Load showcase DOM not verified.", false);
		}
	}

	@Test
	public void shouldAnimationWorksOnLoad() throws InterruptedException {

		findElementById("text").sendKeys("something");

		findElementById("submit").click();

		waitUntilAjaxRequestCompletes();

		WebElement m = findElementById("messages");

		assertTrue("Animation should start.",
				shouldElementAnimating(m, "opacity", DECREASING, 1000));

		assertTrue("Animation should start.",
				anyAnimationInProgress("#messages"));

		waitUntilAnimationCompletes("#messages");

		assertTrue("Animation should end correct.", m.isDisplayed());

	}

}
