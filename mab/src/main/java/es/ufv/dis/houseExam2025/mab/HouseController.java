package es.ufv.dis.houseExam2025.mab;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@RestController
@RequestMapping("/api/houses")
public class HouseController {

    private static final Logger logger = LoggerFactory.getLogger(HouseController.class);

    private static final String BASE_PATH = System.getProperty("user.dir") + "/src/main/resources/";
    private static final String JSON_PATH = BASE_PATH + "houses.json";
    private static final String EXPORTS_FOLDER = BASE_PATH + "exports/";
    private static final String SUMMARY_FILE = EXPORTS_FOLDER + "csv_summary.json";

    private Map<String, Integer> csvSummary = new HashMap<>();
    private List<House> houses = new ArrayList<>();

    public HouseController() {
        // Crear directorio de exportaciones si no existe
        new File(EXPORTS_FOLDER).mkdirs();

        // Cargar casas desde el archivo JSON
        File jsonFile = new File(JSON_PATH);
        if (jsonFile.exists()) {
            try (Reader reader = new FileReader(JSON_PATH)) {
                houses = new Gson().fromJson(reader, new TypeToken<List<House>>() {}.getType());
            } catch (IOException e) {
                logger.error("Error reading houses from JSON", e);
            }
        } else {
            initializeJsonFile();
        }

        // Cargar o inicializar el resumen de CSV
        File summaryFile = new File(SUMMARY_FILE);
        if (summaryFile.exists()) {
            try (Reader reader = new FileReader(SUMMARY_FILE)) {
                csvSummary = new Gson().fromJson(reader, new TypeToken<Map<String, Integer>>() {}.getType());
            } catch (IOException e) {
                logger.error("Error reading summary.json", e);
            }
        }
    }

    @GetMapping
    public List<House> getAllHouses() {
        return houses;
    }

    @PostMapping
    public String addHouse(@RequestBody House house) {
        house.setId(UUID.randomUUID().toString());
        houses.add(house);
        saveHousesToFile();
        return "House added successfully.";
    }

    @PutMapping("/{id}")
    public String updateHouse(@PathVariable String id, @RequestBody House updatedHouse) {
        for (House house : houses) {
            if (house.getId().equals(id)) {
                house.setAddress(updatedHouse.getAddress());
                house.setOwner(updatedHouse.getOwner());
                house.setValue(updatedHouse.getValue());
                house.setSquareMetres(updatedHouse.getSquareMetres());
                saveHousesToFile();
                return "House updated successfully.";
            }
        }
        return "House not found.";
    }

    @DeleteMapping("/{id}")
    public String deleteHouse(@PathVariable String id) {
        boolean removed = houses.removeIf(house -> house.getId().equals(id));
        if (removed) {
            saveHousesToFile();
            return "House deleted successfully.";
        } else {
            return "House not found.";
        }
    }

    @PostMapping("/generate-csv")
    public String generateCsv(@RequestBody String houseId) {
        String cleanedId = houseId.trim().replace("\"", "");
        House house = houses.stream()
                .filter(h -> h.getId().equals(cleanedId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("House not found"));

        File csvFile = new File(EXPORTS_FOLDER, cleanedId + ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write("ID,Address,Owner,Value,SquareMetres\n");
            writer.write(String.format("%s,%s,%s,%.2f,%d\n",
                    house.getId(),
                    house.getAddress(),
                    house.getOwner(),
                    house.getValue(),
                    house.getSquareMetres()));
        } catch (IOException e) {
            logger.error("Error generating CSV for '{}'", cleanedId, e);
            return "Error generating CSV.";
        }

        csvSummary.put(cleanedId, csvSummary.getOrDefault(cleanedId, 0) + 1);
        saveSummaryToFile();

        return "CSV generated successfully for house ID: " + cleanedId;
    }

    @GetMapping("/export-pdf")
    public String exportToPdf() {
        File pdfFile = new File(EXPORTS_FOLDER, "houses.pdf");
        try (Document document = new Document(PageSize.A4)) {
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            document.add(new Paragraph("Houses Data"));
            for (House house : houses) {
                document.add(new Paragraph(String.format("ID: %s, Address: %s, Owner: %s, Value: %.2f, Square Metres: %d",
                        house.getId(),
                        house.getAddress(),
                        house.getOwner(),
                        house.getValue(),
                        house.getSquareMetres())));
            }
            document.close();
        } catch (Exception e) {
            logger.error("Error exporting houses to PDF", e);
            return "Error exporting PDF.";
        }
        return "PDF exported successfully.";
    }

    private void saveHousesToFile() {
        try (Writer writer = new FileWriter(JSON_PATH)) {
            new Gson().toJson(houses, writer);
        } catch (IOException e) {
            logger.error("Error saving houses to JSON", e);
        }
    }

    private void saveSummaryToFile() {
        try (Writer writer = new FileWriter(SUMMARY_FILE)) {
            new Gson().toJson(csvSummary, writer);
        } catch (IOException e) {
            logger.error("Error saving summary to JSON", e);
        }
    }

    private void initializeJsonFile() {
        try (Writer writer = new FileWriter(JSON_PATH)) {
            new Gson().toJson(new ArrayList<>(), writer);
        } catch (IOException e) {
            logger.error("Error initializing houses.json", e);
        }
    }
}
