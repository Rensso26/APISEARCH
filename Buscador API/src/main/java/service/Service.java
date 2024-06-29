package service;

import model.Photos;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Service {
    private static final String Key = "jAa9bUIeoNdsnYF3Qrt78blcAdZHQNgDMgZRwI8c";
    private static final String URL = "https://api.nasa.gov/mars-photos/api/v1/";

    public List<Photos> getPhotos(String rover, int sol, String camera, LocalDate startDate, LocalDate endDate) throws Exception {
        List<Photos> allPhotos = new ArrayList<>();
        boolean existPhotos = true;
        int page = 1;

        while (existPhotos) {
            String urlString = URL +"rovers/"+ rover + "/photos?sol=" + sol + "&camera=" + camera +
                    "&earth_date=" + startDate + "&end_date=" + endDate + "&page=" + page + "&api_key=" + Key;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("Error foto no encontrada: " + responseCode);
            }

            JSONObject json = new JSONObject(getResponseContent(conn));
            JSONArray jsonArray = json.getJSONArray("photos");

            if (jsonArray.length() == 0) {
                existPhotos = false;
            } else {
                allPhotos.addAll(addPhotos(jsonArray));
                page++;
            }
        }

        return allPhotos;
    }
    
    private String getResponseContent(HttpURLConnection conn) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();
        return content.toString();
    }
    
    private List<Photos> addPhotos(JSONArray jsonArray) {
        List<Photos> photos = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject photoJson = jsonArray.getJSONObject(i);

            JSONObject roverJson = photoJson.getJSONObject("rover");
            JSONObject cameraJson = photoJson.getJSONObject("camera");
            JSONArray camerasArray = roverJson.getJSONArray("cameras");
            List<String> camerasList = new ArrayList<>();

            IntStream.range(0, camerasArray.length())
                    .mapToObj(e -> camerasArray.getJSONObject(e).getString("full_name"))
                    .forEach(camerasList::add);

            photos.add(new Photos(
                    photoJson.getInt("id"),
                    photoJson.getInt("sol"),
                    cameraJson.getString("name"),
                    cameraJson.getString("full_name"),
                    photoJson.getString("img_src"),
                    LocalDate.parse(photoJson.getString("earth_date")),
                    roverJson.getString("name"),
                    roverJson.getString("status"),
                    LocalDate.parse(roverJson.getString("landing_date")),
                    LocalDate.parse(roverJson.getString("launch_date")),
                    roverJson.getInt("max_sol"),
                    LocalDate.parse(roverJson.getString("max_date")),
                    roverJson.getInt("total_photos"),
                    camerasList
            ));
        }

        return photos;
    }
    
}


