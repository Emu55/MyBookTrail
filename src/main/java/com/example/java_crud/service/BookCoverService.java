package com.example.java_crud.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookCoverService {
    private final String GOOGLE_BOOKS_API = "https://www.googleapis.com/books/v1/volumes?q=";

    public String getBookCover(String title, String author) {
        String url = GOOGLE_BOOKS_API + "intitle:" + title + "+inauthor:" + author;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        try {
            // Parse the JSON response
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("items") && jsonObject.getJSONArray("items").length() > 0) {
                JSONObject volumeInfo = jsonObject.getJSONArray("items")
                        .getJSONObject(0)
                        .getJSONObject("volumeInfo");

                if (volumeInfo.has("imageLinks")) {
                    return volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                }
            }
        } catch (JSONException e) {
            // If something goes wrong, log and return default cover
            System.out.println("Error fetching book cover: " + e.getMessage());
        }

        // If no cover found, return the path to the default image
        return "/img/default_cover.png";
    }
}
