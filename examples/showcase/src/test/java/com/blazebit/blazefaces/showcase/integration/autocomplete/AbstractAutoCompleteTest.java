package com.blazebit.blazefaces.showcase.integration.autocomplete;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

public class AbstractAutoCompleteTest extends AbstractIntegrationTest {

	protected void selectAutoCompleteListElement(String id, int index,
			int waitSecond) {
		WebElement panel = findElementById(id + "_panel");
		WebElement list = waitUntilElementExistsAndGet(panel, By.tagName("ul"),
				waitSecond);
		if (list != null) {
			WebElement element = list.findElements(By.tagName("li")).get(index);
			element.click();
			waitUntilAjaxRequestCompletes();
		}
	}

	protected void doTestOnAutoComplete(String id, String value) {
		WebElement autocomplete = findElementById(id + "_input");
		autocomplete.clear();
		autocomplete.sendKeys(value);
		waitUntilAjaxRequestCompletes();
		waitUntilAjaxRequestCompletes();
		selectAutoCompleteListElement(id, 2, 1);
		assertThat(autocomplete.getAttribute("value"), equalTo(value + "2"));
	}

}
