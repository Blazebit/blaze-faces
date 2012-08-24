package com.blazebit.blazefaces.showcase.integration.requestcontext;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

public class RequestContextIntegrationTest extends AbstractIntegrationTest {

	@Before
	public void before() {
		String testUrl = toShowcaseUrl("requestContext.xhtml");
		driver.get(testUrl);
	}

	@Test
	public void shouldExecuteJsFromRequestContextAndUpdate()
			throws InterruptedException {
		driver.findElement(By.id("form:firstname")).sendKeys("Cagatay");
		driver.findElement(By.id("form:surname")).sendKeys("Civici");

		driver.findElement(By.className("ui-button")).click();

		waitUntilAjaxRequestCompletes();
		waitUntilAllAnimationsComplete();

		Long top = (Long) executeJS("return $(window).scrollTop();");

		Assert.assertTrue("Should request context scroll.", top > 0);

	}

}
