package training;

import com.vaadin.flow.component.confirmdialog.testbench.ConfirmDialogElement;
import com.vaadin.flow.component.crud.testbench.CrudElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.IPAddress;
import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.TestBenchTestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;

public class MainViewIT extends TestBenchTestCase {

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
        getDriver().get("http://" + IPAddress.findSiteLocalAddress() + ":8080/");

        // Define the directory for reference screenshots and for error files
        Parameters.setScreenshotReferenceDirectory("src/test/screenshots");
        Parameters.setScreenshotErrorDirectory("target/screenshot_errors");
        Parameters.setScreenshotComparisonCursorDetection(true);
    }

    @Test
    public void editPerson() {
        //1. Create a new Person with unique names by clicking the 'New Item' button and input the values in the popup editor
        String uniqueFirstName = "Unique first name";
        String uniqueLastName = "Unique last name";
        int rowIndex = createPerson(uniqueFirstName, uniqueLastName);

        //2. Click the edit button of the new row in Grid to edit the new record
        openRowForEditing(rowIndex);

        //3. Verify that the editor is open
        Assert.assertTrue(getCrudElement().isEditorOpen());

        //4. Verify that the values in the first name and last name fields are the same value you've defined.
        TextFieldElement firstNameField = $(TextFieldElement.class).id("first-name");
        TextFieldElement lastNameField = $(TextFieldElement.class).id("last-name");
        Assert.assertEquals(uniqueFirstName, firstNameField.getValue());
        Assert.assertEquals(uniqueLastName, lastNameField.getValue());

        //5. Update the first/last name to some new values
        String editedFirstName = uniqueFirstName + "edited";
        String editedLastName = uniqueLastName + "edited";
        firstNameField.setValue(editedFirstName);
        lastNameField.setValue(editedLastName);

        //6. Click the save button in the editor
        getCrudElement().getEditorSaveButton().click();

        //7. Verify the editor is closed
        Assert.assertFalse(getCrudElement().isEditorOpen());

        //8. Click the edit button of the new row in Grid to edit the new record
        openRowForEditing(rowIndex);

        //9. Verify that the values in the first name and last name fields are the same value you've updated.
        Assert.assertEquals(editedFirstName, firstNameField.getValue());
        Assert.assertEquals(editedLastName, lastNameField.getValue());

        //10. Close the popup editor
        getCrudElement().getEditorCancelButton().click();

        //11. Delete the record
        openRowForEditing(rowIndex);
        getCrudElement().getEditorDeleteButton().click();
        getCrudElement().$(ConfirmDialogElement.class).last().getConfirmButton().click();
    }

    private void openRowForEditing(int rowIndex) {
        getCrudElement().getGrid().getCell(rowIndex, 0).$("vaadin-crud-edit").first().click();
    }

    private int createPerson(String firstName, String lastName) {
        TestBenchElement newButton = getCrudElement().getNewItemButton().get();
        newButton.click();

        Assert.assertTrue(getCrudElement().isEditorOpen());

        savePerson(firstName, lastName);

        Assert.assertFalse(getCrudElement().isEditorOpen());

        GridElement gridElement = getCrudElement().getGrid();
        gridElement.scrollToRow(gridElement.getRowCount());
        return waitUntil(wd -> gridElement.getCell(firstName)).getRow();
    }

    private void savePerson(String firstName, String lastName) {
        TextFieldElement firstNameField = $(TextFieldElement.class).id("first-name");
        TextFieldElement lastNameField = $(TextFieldElement.class).id("last-name");

        firstNameField.setValue(firstName);
        lastNameField.setValue(lastName);

        getCrudElement().getEditorSaveButton().click();
    }

    private CrudElement getCrudElement(){
        return $(CrudElement.class).first();
    }

    @After
    public void tearDown(){
        getDriver().quit();
    }
}
