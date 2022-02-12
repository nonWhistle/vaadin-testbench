package training.components;

import com.vaadin.flow.component.confirmdialog.testbench.ConfirmDialogElement;
import com.vaadin.flow.component.crud.testbench.CrudElement;
import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.annotations.Attribute;
import com.vaadin.testbench.commands.CanCompareScreenshots;
import org.junit.Assert;

@Attribute(name = "id", value = "main-view")
public class MainViewElement extends TestBenchElement {

    public int createNewPerson(String firstName, String lastName){
        openEditorForNew();

        savePerson(firstName, lastName);

        GridElement gridElement = getCrudElement().getGrid();
        gridElement.scrollToRow(gridElement.getRowCount());
        return waitUntil(wd -> gridElement.getCell(firstName)).getRow();
    }

    private void openEditorForNew() {
        TestBenchElement newButton = getCrudElement().getNewItemButton().get();
        newButton.click();
        Assert.assertTrue(getCrudElement().isEditorOpen());
    }

    private void savePerson(String firstName, String lastName) {
        TextFieldElement firstNameField = getCrudElement().getEditor().$(TextFieldElement.class).id("first-name");
        TextFieldElement lastNameField = getCrudElement().getEditor().$(TextFieldElement.class).id("last-name");

        firstNameField.setValue(firstName);
        lastNameField.setValue(lastName);

        getCrudElement().getEditorSaveButton().click();
        Assert.assertFalse(getCrudElement().isEditorOpen());
    }

    public void editRow(int rowIndex, String newFirstName, String newLastName){
        openEditorForRow(rowIndex);
        savePerson(newFirstName, newLastName);
    }

    public void deleteRow(int rowIndex){
        openEditorForRow(rowIndex);
        getCrudElement().getEditorDeleteButton().click();
        getCrudElement().$(ConfirmDialogElement.class).last().getConfirmButton().click();
    }

    public void openEditorForRow(int rowIndex) {
        $(CrudElement.class).first().getGrid().getCell(rowIndex, 0).$(TestBenchElement.class).first().click();
        Assert.assertTrue(getCrudElement().isEditorOpen());
    }

    private CrudElement getCrudElement(){
        return $(CrudElement.class).first();
    }

    public CanCompareScreenshots getEditorForm() {
        return getCrudElement().getEditor().$(FormLayoutElement.class).first();
    }

    public String getFirstNameValue() {
        return getCrudElement().getEditor().$(TextFieldElement.class).id("first-name").getValue();
    }

    public String getLastNameValue() {
        return getCrudElement().getEditor().$(TextFieldElement.class).id("last-name").getValue();
    }

    public void closeEditor() {
        getCrudElement().getEditorCancelButton().click();
    }
}
