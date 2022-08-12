package com.sg.ssfassessment.services;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sg.ssfassessment.models.News;
import com.sg.ssfassessment.repositories.NewsRepository;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class NewsService {
    private static final String URL = "https://min-api.cryptocompare.com/data/v2/news";

    @Value("${API_KEY}")
    private String key;

    @Autowired
    private NewsRepository newsRepo;

    public List<News> getArticles(String categories ) {

        News n = new News();

        Optional<String> opt = newsRepo.getArticle(categories);
        String payloadArticle;

        System.out.println("------------");
        System.out.printf(">>> category: %s\n", categories.toUpperCase());

        if (opt.isEmpty()) {

            System.out.println("Getting latest articles from CryptoCompare.com");

            try {

                String url = UriComponentsBuilder.fromUriString(URL)
                        .queryParam("categories", categories)
                        .queryParam("api_key", key)
                        .toUriString();

                RequestEntity<Void> req = RequestEntity.get(url).build();

                RestTemplate template = new RestTemplate();
                ResponseEntity<String> resp;

                resp = template.exchange(req, String.class);

                payloadArticle = resp.getBody();
                System.out.println(">>> Article: " + payloadArticle);
                // getting error , path does not exist

                newsRepo.saveArticle(categories, payloadArticle);

            } catch (Exception ex) {
                System.err.printf("Error: %s\n", ex.getMessage());
                return Collections.emptyList();

            }

        } else {
            payloadArticle = opt.get();
            System.out.printf(">>> Cached Article: %s\n", payloadArticle);

        }

       
 
         // return null;
          return Collections.emptyList(); // prevents prompt from repeating
         // return getPrice(coin, currency); // this will result in never ending prompts.

    }

}
