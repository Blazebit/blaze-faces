package com.blazebit.blazefaces.showcase.integration.datatable;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DataTableBasicRowSelectionIntegrationTest extends
		AbstractDataTableIntegrationTest {
	private static int repeat = 0;

	@Before
	public void before() {
		driver.get(toShowcaseUrl("datatableRowSelectionByColumn.xhtml"));
	}

	@Test
	public void shouldDisplaySelectedRows() {
		scrollByOffset(0, 200);
		List<WebElement> rows = findElementById("form:cars_data").findElements(
				By.cssSelector(ROW_CLASS));
		Integer rowIndex = new Random().nextInt(rows.size());
		List<String> aRow = getRowByRowIndex(rows, rowIndex, FIRST_COLUMN,
				END_COLUMN);
		clickToElementById("form:cars:" + rowIndex + ":selectButton");
		waitUntilAjaxRequestCompletes();
		assertTrue(isTextsPresent(findElementById("form:carDlg"), aRow));
		findElementBySelector(findElementById("form:carDlg"),
				".ui-dialog-titlebar-close").click();
		waitUntilAllAnimationsComplete();

		if (repeat < 3) {
			repeat++;
			scrollByOffset(0, -200);
			shouldDisplaySelectedRows();
		}
	}

}
