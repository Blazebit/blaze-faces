package com.blazebit.blazefaces.showcase.integration.progressbar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

public class ProgressBarIntegrationTest extends AbstractIntegrationTest {

	@Before
	public void init() {
		String testUrl = toShowcaseUrl("progressBar.xhtml");
		driver.get(testUrl);
	}

	@Test
	public void shouldDisplayClientSideProgressBar()
			throws InterruptedException {

		WebElement progressBar = findElementById("form:progressBarClient");
		assertThat(progressBar.getAttribute("aria-valuenow"), equalTo("0"));
		findElementById("form:start").click();
		Thread.sleep(11 * 1000l);
		assertThat(progressBar.getAttribute("aria-valuenow"), equalTo("100"));
	}

	@Test
	public void shouldBeAbleToCancelProgressBar() throws InterruptedException {

		WebElement progressBar = findElementById("form:progressBarClient");
		assertThat(progressBar.getAttribute("aria-valuenow"), equalTo("0"));
		findElementById("form:start").click();
		Thread.sleep(5 * 1000l);
		findElementById("form:cancel").click();
		assertThat(progressBar.getAttribute("aria-valuenow"), equalTo("0"));
	}
}
