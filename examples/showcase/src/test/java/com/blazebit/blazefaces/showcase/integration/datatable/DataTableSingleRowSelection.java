package com.blazebit.blazefaces.showcase.integration.datatable;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DataTableSingleRowSelection extends
		AbstractDataTableIntegrationTest {

	@Before
	public void before() {
		driver.get(toShowcaseUrl("datatableRowSelectionSingle.xhtml"));
	}

	@Test
	public void shouldDisplaySelectedRows() {
		scrollByOffset(0, 200);
		List<WebElement> rows = findElementById("form:cars_data").findElements(
				By.cssSelector(ROW_CLASS));
		Integer rowIndex = new Random().nextInt(rows.size());
		List<String> aRow = getRowByRowIndex(rows, rowIndex, FIRST_COLUMN,
				END_COLUMN);
		rows.get(rowIndex).findElement(By.xpath("td/div")).click();
		clickToElementById("form:cars:viewButton");
		assertTrue(isTextsPresent(findElementById("form:dialog"), aRow));
	}

}
