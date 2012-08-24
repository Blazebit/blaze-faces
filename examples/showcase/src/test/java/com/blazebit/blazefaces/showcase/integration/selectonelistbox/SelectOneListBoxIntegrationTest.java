package com.blazebit.blazefaces.showcase.integration.selectonelistbox;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

public class SelectOneListBoxIntegrationTest extends AbstractIntegrationTest {

	@Before
	public void before() {
		driver.get(toShowcaseUrl("selectOneListbox.xhtml"));
	}

	@Test
	public void shouldSelectOneValue() {

		findElementById("form:basic").findElements(By.tagName("li")).get(0)
				.click();

		clickToElementById("form:submitButton");

		waitUntilElementGetsValue("form:selectedNumber", "1");
	}

	@Test
	public void shouldSelectOneValueFromListBoxWithScroll() {

		findElementById("form:scroll").findElements(By.tagName("li")).get(0)
				.click();

		clickToElementById("form:submitButton");

		waitUntilElementGetsValue("form:selectedPlayer", "Messi");
	}
}
