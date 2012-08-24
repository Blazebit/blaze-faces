package com.blazebit.blazefaces.showcase.integration.media;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

public class MediaIntegrationTest extends AbstractIntegrationTest {

	@Before
	public void init() {
		String testUrl = toShowcaseUrl("media.xhtml");
		driver.get(testUrl);
	}

	@Test
	public void shouldDisplayMedias() {
		List<WebElement> elements = findElementsByTag("embed");
		assertTrue(elements.size() == 3);

		assertTrue(elements.get(0).getAttribute("src")
				.startsWith("http://www.youtube.com/v/KZnUr8lcqjo"));
		assertTrue(elements
				.get(1)
				.getAttribute("src")
				.startsWith(
						"http://service.real.com/learnnav/testrams/realvideo10_56.ram"));
		assertTrue(elements
				.get(2)
				.getAttribute("src")
				.startsWith(
						"http://www.tulumba.com/mp3/mogollar/yurudukdurmadan/track%2002.mp3"));
	}
}
