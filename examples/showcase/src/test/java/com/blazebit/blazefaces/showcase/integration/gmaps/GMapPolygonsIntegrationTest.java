package com.blazebit.blazefaces.showcase.integration.gmaps;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

public class GMapPolygonsIntegrationTest extends AbstractIntegrationTest {

	protected static final String TEST_URL = BLAZE_SHOWCASE_UI
			+ "gmapPolygon.xhtml";

	@BeforeClass
	public static void init() {
		driver.get(TEST_URL);
	}

	@Test
	public void shouldVerifyDOM() {

		try {

			assertTrue("Should map displayed.",
					findElementBySelector("div#gmap").isDisplayed());

		} catch (NoSuchElementException e) {
			assertTrue("GMap Polygon showcase DOM not verified.", false);
		}
	}
}
