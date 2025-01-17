package es.ufv.dis.houseExam2025.mab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HouseControllerTests {

    @InjectMocks
    private HouseController houseController;

    @Mock
    private File mockFile;

    private House house1;
    private House house2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        house1 = new House();
        house1.setId(UUID.randomUUID().toString());
        house1.setAddress("123 Malibu Point, Malibu, CA");
        house1.setOwner("Tony Stark");
        house1.setValue(25000000.00);
        house1.setSquareMetres(1000);

        house2 = new House();
        house2.setId(UUID.randomUUID().toString());
        house2.setAddress("456 Beachfront Ave, Malibu, CA");
        house2.setOwner("Bruce Wayne");
        house2.setValue(32000000.00);
        house2.setSquareMetres(1500);

        // Inicializar el controlador con casas de prueba
        houseController = new HouseController();
        houseController.addHouse(house1);
        houseController.addHouse(house2);
    }

    @Test
    public void testGetAllHouses() {
        // Crear lista ficticia de casas
        List<House> mockHouses = new ArrayList<>();
        
        House house1 = new House();
        house1.setId(UUID.randomUUID().toString());
        house1.setAddress("123 Malibu Point, Malibu, CA");
        house1.setOwner("Tony Stark");
        house1.setValue(25000000.00);
        house1.setSquareMetres(1000);

        House house2 = new House();
        house2.setId(UUID.randomUUID().toString());
        house2.setAddress("456 Beachfront Ave, Malibu, CA");
        house2.setOwner("Bruce Wayne");
        house2.setValue(32000000.00);
        house2.setSquareMetres(1500);

        mockHouses.add(house1);
        mockHouses.add(house2);

        // Usar reflexión para asignar la lista ficticia al controlador
        try {
            var housesField = HouseController.class.getDeclaredField("houses");
            housesField.setAccessible(true);
            housesField.set(houseController, mockHouses);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set mock houses: " + e.getMessage());
        }

        // Ejecutar prueba
        List<House> houses = houseController.getAllHouses();
        assertEquals(2, houses.size(), "The number of houses should be 2.");
        assertEquals("Tony Stark", houses.get(0).getOwner(), "The first house's owner should be Tony Stark.");
        assertEquals("Bruce Wayne", houses.get(1).getOwner(), "The second house's owner should be Bruce Wayne.");
    }


    @Test
    public void testAddHouse() {
        // Crear lista ficticia de casas
        List<House> mockHouses = new ArrayList<>();

        House house1 = new House();
        house1.setId(UUID.randomUUID().toString());
        house1.setAddress("123 Malibu Point, Malibu, CA");
        house1.setOwner("Tony Stark");
        house1.setValue(25000000.00);
        house1.setSquareMetres(1000);

        House house2 = new House();
        house2.setId(UUID.randomUUID().toString());
        house2.setAddress("456 Beachfront Ave, Malibu, CA");
        house2.setOwner("Bruce Wayne");
        house2.setValue(32000000.00);
        house2.setSquareMetres(1500);

        mockHouses.add(house1);
        mockHouses.add(house2);

        // Usar reflexión para asignar la lista ficticia al controlador
        try {
            var housesField = HouseController.class.getDeclaredField("houses");
            housesField.setAccessible(true);
            housesField.set(houseController, mockHouses);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set mock houses: " + e.getMessage());
        }

        // Crear una nueva casa
        House newHouse = new House();
        newHouse.setAddress("789 Ocean Drive, Malibu, CA");
        newHouse.setOwner("Clark Kent");
        newHouse.setValue(18000000.00);
        newHouse.setSquareMetres(800);

        // Añadir la nueva casa
        houseController.addHouse(newHouse);

        // Verificar resultados
        List<House> houses = houseController.getAllHouses();
        assertEquals(3, houses.size(), "The number of houses should be 3 after adding a new house.");
        assertTrue(houses.contains(newHouse), "The new house should be present in the list.");
    }


    @Test
    public void testUpdateHouse() {
        House updatedHouse = new House();
        updatedHouse.setAddress("Updated Address");
        updatedHouse.setOwner("Updated Owner");
        updatedHouse.setValue(999999.99);
        updatedHouse.setSquareMetres(500);

        String response = houseController.updateHouse(house1.getId(), updatedHouse);
        assertEquals("House updated successfully.", response);

        House updated = houseController.getAllHouses().stream()
                .filter(h -> h.getId().equals(house1.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(updated, "Updated house should exist.");
        assertEquals("Updated Address", updated.getAddress(), "The address should be updated.");
        assertEquals("Updated Owner", updated.getOwner(), "The owner should be updated.");
        assertEquals(999999.99, updated.getValue(), "The value should be updated.");
        assertEquals(500, updated.getSquareMetres(), "The square metres should be updated.");
    }

    @Test
    public void testDeleteHouse() {
        // Crear lista ficticia de casas
        List<House> mockHouses = new ArrayList<>();

        House house1 = new House();
        house1.setId(UUID.randomUUID().toString());
        house1.setAddress("123 Malibu Point, Malibu, CA");
        house1.setOwner("Tony Stark");
        house1.setValue(25000000.00);
        house1.setSquareMetres(1000);

        House house2 = new House();
        house2.setId(UUID.randomUUID().toString());
        house2.setAddress("456 Beachfront Ave, Malibu, CA");
        house2.setOwner("Bruce Wayne");
        house2.setValue(32000000.00);
        house2.setSquareMetres(1500);

        mockHouses.add(house1);
        mockHouses.add(house2);

        // Usar reflexión para asignar la lista ficticia al controlador
        try {
            var housesField = HouseController.class.getDeclaredField("houses");
            housesField.setAccessible(true);
            housesField.set(houseController, mockHouses);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set mock houses: " + e.getMessage());
        }

        // Eliminar una casa
        String response = houseController.deleteHouse(house2.getId());

        // Verificar resultados
        assertEquals("House deleted successfully.", response);

        List<House> houses = houseController.getAllHouses();
        assertEquals(1, houses.size(), "The number of houses should be 1 after deletion.");
        assertFalse(houses.contains(house2), "The deleted house should not be present in the list.");
    }


    @Test
    public void testGenerateCsv() {
        String response = houseController.generateCsv(house1.getId());
        assertEquals("CSV generated successfully for house ID: " + house1.getId(), response);

        File csvFile = new File(System.getProperty("user.dir") + "/src/main/resources/exports/" + house1.getId() + ".csv");
        assertTrue(csvFile.exists(), "The CSV file should be generated.");
    }

    @Test
    public void testExportToPdf() {
        String response = houseController.exportToPdf();
        assertEquals("PDF exported successfully.", response);

        File pdfFile = new File(System.getProperty("user.dir") + "/src/main/resources/exports/houses.pdf");
        assertTrue(pdfFile.exists(), "The PDF file should be generated.");
    }

    @Test
    public void testUpdateNonexistentHouse() {
        House updatedHouse = new House();
        updatedHouse.setAddress("Nonexistent Address");
        updatedHouse.setOwner("Nonexistent Owner");
        updatedHouse.setValue(12345.67);
        updatedHouse.setSquareMetres(200);

        String response = houseController.updateHouse("nonexistent-id", updatedHouse);
        assertEquals("House not found.", response, "The response should indicate the house was not found.");
    }

    @Test
    public void testDeleteNonexistentHouse() {
        String response = houseController.deleteHouse("nonexistent-id");
        assertEquals("House not found.", response, "The response should indicate the house was not found.");
    }
}
