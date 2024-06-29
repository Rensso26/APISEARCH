package controller;

import model.Photos;
import service.Service;

import java.time.LocalDate;
import java.util.List;

public class Controller {
    private final Service apiService;

    public Controller(Service apiService) {
        this.apiService = apiService;
    }

    public List<Photos> fetchPhotos(String rover, int sol, String camera, LocalDate startDate, LocalDate endDate) throws Exception {
        return apiService.getPhotos(rover, sol, camera, startDate, endDate);
    }
}
