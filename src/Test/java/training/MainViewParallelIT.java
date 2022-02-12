package training;

import com.vaadin.testbench.IPAddress;
import com.vaadin.testbench.annotations.BrowserConfiguration;
import com.vaadin.testbench.annotations.RunOnHub;
import com.vaadin.testbench.parallel.BrowserUtil;
import com.vaadin.testbench.parallel.ParallelTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import training.components.MainViewElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunOnHub
public class MainViewParallelIT extends ParallelTest {

    private MainViewElement mainViewElement;

    @Before
    public void setup() throws Exception {
        super.setup();
        getDriver().get("http://localhost:8080/"); //+ IPAddress.findSiteLocalAddress() + ":8080/");

        mainViewElement = $(MainViewElement.class).waitForFirst();

        testBench().resizeViewPortTo(1024, 768);
    }

    @BrowserConfiguration
    public List<DesiredCapabilities> getBrowserConfiguration() {
        List<DesiredCapabilities> browsers =
                new ArrayList<>();

        // Add all the browsers you want to test
        browsers.add(BrowserUtil.firefox());
        browsers.add(BrowserUtil.chrome());

        return browsers;
    }

    @Test
    public void editPerson() {
        String uniqueFirstName = "Unique first name ";
        String uniqueLastName = "Unique last name ";

        int rowIndex = mainViewElement.createNewPerson(uniqueFirstName, uniqueLastName);
        String newFirstName = uniqueFirstName + "edited";
        String newLastName = uniqueLastName + "edited";

        mainViewElement.editRow(rowIndex, newFirstName, newLastName);

        mainViewElement.openEditorForRow(rowIndex);

        verifyWithScreenShot();

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
                    .compareScreen("editor-form"));
        } catch (IOException e) {
            Assert.fail("screen comparison failed");
        }
    }
}
