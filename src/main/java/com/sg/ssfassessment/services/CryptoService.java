package com.sg.ssfassessment.services;

import java.io.Reader;
import java.io.StringReader;
import java.net.URLEncoder;
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

import com.sg.ssfassessment.models.Crypto;
import com.sg.ssfassessment.repositories.CryptoRepository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class CryptoService {
    private static final String URL = "https://min-api.cryptocompare.com/data/price";

    @Value("${API_KEY}")
    private String key;

    @Autowired
    private CryptoRepository cryptoRepo;

    public List<Crypto> getPrice(String coin, String currency) {

        Crypto c = new Crypto(); // create a model
        Optional<String> opt = cryptoRepo.get(coin, currency);
        String payload;
        System.out.println("--------------------");
        System.out.printf(">>> coin: %s\n", coin.toUpperCase());
        System.out.printf(">>> currency: %s\n", currency.toUpperCase());

        if (opt.isEmpty()) {

            System.out.println("Getting prices from CryptoCompare.com");

            try {

                String url = UriComponentsBuilder.fromUriString(URL)
                        .queryParam("fsym", URLEncoder.encode(coin, "UTF-8"))
                        .queryParam("tsyms", URLEncoder.encode(currency, "UTF-8"))
                        .queryParam("api_key", key)
                        .toUriString();

                RequestEntity<Void> req = RequestEntity.get(url).build();

                RestTemplate template = new RestTemplate();
                ResponseEntity<String> resp;

                resp = template.exchange(req, String.class);

                payload = resp.getBody();
                System.out.println(">>> latest price: " + payload);

                cryptoRepo.save(coin, currency, payload);
            } catch (Exception ex) {
                System.err.printf("Error: %s\n", ex.getMessage());
                return Collections.emptyList();

            }

        } else {
            payload = opt.get();
            System.out.printf(">>>> last cached price: %s\n", payload); // cache expires in 20 seconds

        } // cache is working fine, expires on redis after 1 minute.

        // problem at payload to json *SOLVED*
        Reader strReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(strReader);
        JsonObject jsonObject = jsonReader.readObject();

        Float price = Float.parseFloat(jsonObject.get(currency).toString());

        List<Crypto> list = new LinkedList<>(); // make a list to store payload

        c.setCoin(coin); // set current load to variable
        c.setCurrency(currency);
        c.setPrice(price);

        list.add(c); // add object c to list

        return list; // return back all the data
        // return null;
        // return Collections.emptyList(); - prevents prompt from repeating
        // return getPrice(coin, currency); - this will result in never ending prompts.

    }

}