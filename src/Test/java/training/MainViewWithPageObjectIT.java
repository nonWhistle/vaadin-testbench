package training;

import com.vaadin.testbench.IPAddress;
import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.screenshot.ImageFileUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import training.components.MainViewElement;

import java.io.IOException;

public class MainViewWithPageObjectIT extends TestBenchTestCase {

    private MainViewElement mainViewElement;

    @Rule
    public ScreenshotOnFailureRule rule = new ScreenshotOnFailureRule(this,
            true);

    @Before
    public void setup() throws Exception {
        /**
         * To run in headless mode
         * ChromeOptions options = new ChromeOptions();
         * options.addArguments("--headless", "--disable-gpu");
         * options.addArguments("--headless", "--disable-gpu");
         * setDriver(new ChromeDriver(options));
         */
        setDriver(new ChromeDriver());
        // Open the application
        getDriver().get("http://localhost:8080/"); //+ IPAddress.findSiteLocalAddress() + ":8080/");

        // Define the directory for reference screenshots and for error files
        Parameters.setScreenshotComparisonCursorDetection(true);

        mainViewElement = $(MainViewElement.class).waitForFirst();

        testBench().resizeViewPortTo(1024, 754);
    }

    @Test
    public void editPerson() {
        String uniqueFirstName = "Unique first name";
        String uniqueLastName = "Unique last name";

        int rowIndex = mainViewElement.createNewPerson(uniqueFirstName, uniqueLastName);

        String newFirstName = uniqueFirstName + "edited";
        String newLastName = uniqueLastName + "edited";

        mainViewElement.editRow(rowIndex, newFirstName, newLastName);

        mainViewElement.openEditorForRow(rowIndex);

        //verifyWithScreenShot();
        verifyWithValues(newFirstName, newLastName);

        mainViewElement.closeEditor();

        mainViewElement.deleteRow(rowIndex);
    }

    private void verifyWithValues(String firstName, String lastName) {
        //Compare values
        Assert.assertEquals(firstName, mainViewElement.getFirstNameValue());
        Assert.assertEquals(lastName, mainViewElement.getLastNameValue());
    }

    private void verifyWithScreenShot() {
        try {
            Assert.assertTrue(mainViewElement.getEditorForm()
                    .compareScreen(ImageFileUtil.getReferenceScreenshotFile(
                            "editor-form.png")));
        } catch (IOException e) {
            Assert.fail("screen comparison failed");
        }
    }

}
