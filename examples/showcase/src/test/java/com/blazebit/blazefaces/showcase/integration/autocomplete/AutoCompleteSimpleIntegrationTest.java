/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazebit.blazefaces.showcase.integration.autocomplete;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

/**
 * 
 * @author yigitdarcin
 */
public class AutoCompleteSimpleIntegrationTest extends AbstractAutoCompleteTest {

	@Before
	public void before() {
		String testUrl = toShowcaseUrl("autoCompleteBasic.xhtml");
		driver.get(testUrl);
	}

	@Test
	public void shouldSelectElementFromList() {
		doTestOnAutoComplete("form:acSimple", "Blaze");
	}

	@Test
	public void shouldNotSelectElementFromListIfSizeSmallerThan3() {
		String text = "12";
		WebElement autocomplete = findElementById("form:acMinLength_input");
		autocomplete.sendKeys(text);
		waitUntilAjaxRequestCompletes();
		waitUntilAllAnimationsComplete();
		selectAutoCompleteListElement("form:acMinLength", 2, 1);
		assertThat(autocomplete.getAttribute("value"), equalTo(text));

		doTestOnAutoComplete("form:acMinLength", "Blaze");

	}

	@Test
	public void shouldWaitForDelay() {
		String text = "text";
		WebElement autocomplete = findElementById("form:acDelay_input");
		autocomplete.sendKeys(text);
		waitUntilAjaxRequestCompletes();
		waitUntilAllAnimationsComplete();
		selectAutoCompleteListElement("form:acDelay", 5, 0);
		assertThat(autocomplete.getAttribute("value"), equalTo(text));

		doTestOnAutoComplete("form:acDelay", "Blaze");

	}

	@Test
	public void shouldGetMaximum5Results() {
		String text = "Blaze";
		WebElement autocomplete = findElementById("form:acMaxResults_input");
		autocomplete.sendKeys(text);
		waitUntilAjaxRequestCompletes();
		waitUntilAjaxRequestCompletes();
		try {
			selectAutoCompleteListElement("form:acMaxResults", 6, 1);
			assertThat(autocomplete.getAttribute("value"), equalTo(text));
			fail();
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		selectAutoCompleteListElement("form:acMaxResults", 2, 1);
		assertThat(autocomplete.getAttribute("value"), equalTo(text + "2"));
	}

	@Test
	public void shouldForceSelectionOfResult() {
		String text = "Blaze";
		WebElement autocomplete = findElementById("form:acForce_input");
		autocomplete.sendKeys(text);
		waitUntilAjaxRequestCompletes();
		waitUntilAjaxRequestCompletes();
		findElementById("form:panel_header").click();
		assertThat(autocomplete.getAttribute("value"), equalTo(""));

		doTestOnAutoComplete("form:acForce", text);
	}

	@Test
	public void shouldDropdownListResults() {
		WebElement autocompleteButton = findElementByXpath("//button[@type='button']");
		autocompleteButton.click();
		waitUntilAjaxRequestCompletes();
		waitUntilAjaxRequestCompletes();
		selectAutoCompleteListElement("form:dd", 5, 1);
		WebElement autocomplete = findElementById("form:dd_input");
		assertThat(autocomplete.getAttribute("value"), equalTo("5"));
	}

	@Test
	public void shoulSubmitValues() {
		shouldSelectElementFromList();
		shouldNotSelectElementFromListIfSizeSmallerThan3();
		shouldWaitForDelay();
		shouldGetMaximum5Results();
		shouldForceSelectionOfResult();
		shouldDropdownListResults();

		WebElement submit = findElementById("form:submit");
		submit.click();
		waitUntilAjaxRequestCompletes();
		waitUntilAllAnimationsComplete();

		assertThat(findElementById("form:txt1").getText(), equalTo("Blaze2"));
		assertThat(findElementById("form:txt2").getText(), equalTo("Blaze2"));
		assertThat(findElementById("form:txt3").getText(), equalTo("Blaze2"));
		assertThat(findElementById("form:txt4").getText(), equalTo("Blaze2"));
		assertThat(findElementById("form:txt5").getText(), equalTo("Blaze2"));
		assertThat(findElementById("form:txt6").getText(), equalTo("5"));

	}

}
