package com.blazebit.blazefaces.showcase.integration.gmaps;

import com.blazebit.blazefaces.showcase.integration.AbstractIntegrationTest;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.openqa.selenium.NoSuchElementException;

public class GMapPolylinesIntegrationTest extends AbstractIntegrationTest {
    
    protected static final String TEST_URL = BLAZE_SHOWCASE_UI + "gmapPolyline.jsf";
    
    @BeforeClass
    public static void init(){
        driver.get(TEST_URL);
    }
    
    @Test
    public void shouldVerifyDOM(){
        
        try{
            
           assertTrue("Should map displayed.", findElementBySelector("div#gmap").isDisplayed());
           
        }
        catch(NoSuchElementException e){
            assertTrue("GMap Polyline showcase DOM not verified.", false);
        }
    }
}
