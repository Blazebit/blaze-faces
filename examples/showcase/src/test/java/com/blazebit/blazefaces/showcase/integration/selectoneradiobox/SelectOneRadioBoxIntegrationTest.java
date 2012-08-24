package com.blazebit.blazefaces.showcase.integration.selectoneradiobox;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

public class SelectOneRadioBoxIntegrationTest extends AbstractIntegrationTest {

	@Before
	public void before() {
		driver.get(toShowcaseUrl("selectOneRadio.xhtml"));
	}

	@Test
	public void shouldSelectAValue() {

		findElementById("form:options").findElement(
				By.className("ui-radiobutton-box")).click();

		clickToElementById("form:submitButton");

		waitUntilElementGetsValue("form:value1", "1");
	}

	@Test
	public void shouldSelectAValueFromCustomRadioBox() {

		findElementById("form:customPanel").findElement(By.tagName("label"))
				.click();

		clickToElementById("form:submitButton");

		waitUntilElementGetsValue("form:value2", "1");
	}
}
