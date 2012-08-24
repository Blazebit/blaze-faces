package com.blazebit.blazefaces.showcase.integration.treetable;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

public class TreeTableScrollableIntegrationTest extends AbstractIntegrationTest {

	public static final String TEST_URL = BLAZE_SHOWCASE_UI
			+ "treeTableScrolling.xhtml";

	@BeforeClass
	public static void init() {
		driver.get(TEST_URL);
	}

	@Test
	public void shouldVerifyDOM() {

		try {

			findElementById("form");

			findElementById("form:yscroll");

			findElementById("form:xscroll");

			findElementById("form:xyscroll");

		} catch (NoSuchElementException e) {
			assertTrue("TreeTable Scrollable showcase DOM not verified.", false);
		}
	}
}
