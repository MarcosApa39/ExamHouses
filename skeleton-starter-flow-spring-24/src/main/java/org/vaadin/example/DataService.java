package org.vaadin.example;


import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.vaadin.flow.component.notification.Notification;

public class DataService {

    private static final String API_BASE_URL = "http://localhost:8090/api/houses";
    private final RestTemplate restTemplate;

    public DataService() {
        this.restTemplate = new RestTemplate();
    }

    public List<House> getAllHouses() {
        try {
            ResponseEntity<House[]> response = restTemplate.getForEntity(API_BASE_URL, House[].class);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            Notification.show("Error fetching houses: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            return List.of();
        }
    }

    public void addHouse(House house) {
        try {
            restTemplate.postForEntity(API_BASE_URL, house, String.class);
            Notification.show("House added successfully!", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error adding house: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    public void updateHouse(House house) {
        try {
            String url = API_BASE_URL + "/" + house.getId();
            restTemplate.put(url, house);
            Notification.show("House updated successfully!", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error updating house: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    public void deleteHouse(String houseId) {
        try {
            String url = API_BASE_URL + "/" + houseId;
            restTemplate.delete(url);
            Notification.show("House deleted successfully!", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error deleting house: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    public void generateCsv(String houseId) {
        try {
            String url = API_BASE_URL + "/generate-csv";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>("\"" + houseId + "\"", headers);
            restTemplate.postForEntity(url, request, String.class);
            Notification.show("CSV generated successfully!", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error generating CSV: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    public void exportPdf() {
        try {
            String url = API_BASE_URL + "/export-pdf";
            restTemplate.getForEntity(url, String.class);
            Notification.show("PDF exported successfully!", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error exporting PDF: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
