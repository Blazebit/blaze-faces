package com.blazebit.blazefaces.showcase.integration.menu;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;

public class MenuIntegrationTest extends AbstractIntegrationTest {

	@Before
	public void before() {
		driver.get(toShowcaseUrl("menu.xhtml"));
	}

	@Test
	public void shouldRenderTieredMenu() {
		List<WebElement> menus = driver.findElements(By.className("ui-menu"));

		WebElement tieredMenu = menus.get(0);

		assertThat(tieredMenu.getText(),
				equalTo("Ajax Menuitems\nSave\nUpdate\nNon-Ajax Menuitem\nDelete\nNavigations\nHome\nTouchFaces"));

		assertThat(tieredMenu.findElements(By.tagName("li")).get(0).getText(), equalTo("Ajax Menuitems"));
		assertThat(tieredMenu.findElements(By.tagName("li")).get(1).getText(), equalTo("Save"));
	}

	@Test
	public void shouldRenderRegularMenu() {
		driver.findElements(By.className("ui-button")).get(0).click();
		List<WebElement> menus = driver.findElements(By.className("ui-menu"));

		WebElement regularMenu = menus.get(2);

		assertThat(
				regularMenu.getText(),
				equalTo("Ajax Menuitems\nSave\nUpdate\nNon-Ajax Menuitem\nDelete\nNavigations\nHome\nTouchFaces"));
		driver.findElements(By.className("ui-button")).get(0).click();

	}

	@Test
	public void shouldRenderDynamicMenu() {
		List<WebElement> menus = driver.findElements(By.className("ui-menu"));

		WebElement dynamicMenu = menus.get(1);

		assertThat(
				dynamicMenu.getText(),
				equalTo("Dynamic Submenu 1\nDynamic Menuitem 1.1\nDynamic Submenu 2\nDynamic Menuitem 2.1\nDynamic Menuitem 2.2"));
	}

}
