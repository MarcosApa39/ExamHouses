package org.vaadin.example;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    private final DataService dataService;
    private final Grid<House> grid;
    private final TextField addressField;
    private final TextField ownerField;
    private final TextField valueField;
    private final TextField squareMetresField;
    private final Button saveButton;

    public MainView() {
        this.dataService = new DataService();
        this.grid = new Grid<>(House.class, false);

        // Configuración del Grid
        grid.addColumn(House::getAddress).setHeader("Address");
        grid.addColumn(House::getOwner).setHeader("Owner");
        grid.addColumn(House::getValue).setHeader("Value");
        grid.addColumn(House::getSquareMetres).setHeader("Square Metres");
        grid.setSelectionMode(SelectionMode.SINGLE);

        grid.addComponentColumn(house -> {
            Button generateCsvButton = new Button("Generate CSV");
            generateCsvButton.addClickListener(e -> dataService.generateCsv(house.getId()));
            return generateCsvButton;
        }).setHeader("Actions");

        grid.addSelectionListener(e -> {
            e.getFirstSelectedItem().ifPresent(this::populateForm);
        });

        // Campos del formulario
        addressField = new TextField("Address");
        ownerField = new TextField("Owner");
        valueField = new TextField("Value");
        squareMetresField = new TextField("Square Metres");

        saveButton = new Button("Save", e -> saveHouse());

        HorizontalLayout formLayout = new HorizontalLayout(addressField, ownerField, valueField, squareMetresField, saveButton);

        Button exportPdfButton = new Button("Export PDF", e -> dataService.exportPdf());

        add(grid, formLayout, exportPdfButton);
        refreshGrid();
    }

    private void refreshGrid() {
        List<House> houses = dataService.getAllHouses();
        grid.setItems(houses);
    }

    private void populateForm(House house) {
        addressField.setValue(house.getAddress());
        ownerField.setValue(house.getOwner());
        valueField.setValue(String.valueOf(house.getValue()));
        squareMetresField.setValue(String.valueOf(house.getSquareMetres()));
    }

    private void saveHouse() {
        try {
            House house = new House();
            house.setAddress(addressField.getValue());
            house.setOwner(ownerField.getValue());
            house.setValue(Double.parseDouble(valueField.getValue()));
            house.setSquareMetres(Integer.parseInt(squareMetresField.getValue()));

            dataService.addHouse(house);
            refreshGrid();
        } catch (Exception e) {
            Notification.show("Error saving house: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}

