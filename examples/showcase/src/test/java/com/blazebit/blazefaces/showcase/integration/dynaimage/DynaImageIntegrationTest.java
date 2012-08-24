package com.blazebit.blazefaces.showcase.integration.dynaimage;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

public class DynaImageIntegrationTest extends AbstractIntegrationTest {

	protected static final String TEST_URL = BLAZE_SHOWCASE_UI
			+ "dynamicImage.xhtml";

	@BeforeClass
	public static void init() {
		driver.get(TEST_URL);
	}

	@Test
	public void shouldVerifyDOM() {

		try {

			String chart = findElementBySelector("img#chart").getAttribute(
					"src");
			String barcode = findElementBySelector("img#barcode").getAttribute(
					"src");
			String text = findElementBySelector("img#text").getAttribute("src");

			assertTrue("Images should be sourced.", chart != null
					&& barcode != null && text != null
					&& !chart.trim().isEmpty() && !barcode.trim().isEmpty()
					&& !text.trim().isEmpty());

		} catch (NoSuchElementException e) {
			assertTrue("Should render images.", false);
		}
	}
}
