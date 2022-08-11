package com.sg.ssfpractice.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sg.ssfpractice.models.Crypto;
import com.sg.ssfpractice.repositories.CryptoRepository;

@Service
public class CryptoService {
    private static final String URL = "https://min-api.cryptocompare.com/data/price";

    @Value("${API_KEY}")
    private String key;

    @Autowired
    private CryptoRepository cryptoRepo;

    public List<Crypto> getPrice(String fsym, String tsyms) {

        Optional<String> opt = cryptoRepo.get(fsym, tsyms);
        String payload;
        System.out.println("--------------------");
        System.out.printf(">>> coin: %s\n", fsym.toUpperCase());
        System.out.printf(">>> currency: %s\n", tsyms.toUpperCase());

        if (opt.isEmpty()) {

            System.out.println("Getting prices from CryptoCompare.com");

            try {

                String url = UriComponentsBuilder.fromUriString(URL)
                        .queryParam("fsym", fsym)
                        .queryParam("tsyms", tsyms)
                        .queryParam("api_key", key)
                        .toUriString();

                RequestEntity<Void> req = RequestEntity.get(url).build();

                RestTemplate template = new RestTemplate();
                ResponseEntity<String> resp;

                resp = template.exchange(req, String.class);

                payload = resp.getBody();
                System.out.println(">>> latest price: " + payload);

                cryptoRepo.save(fsym, tsyms, payload);
            } catch (Exception ex) {
                System.err.printf("Error: %s\n", ex.getMessage());
                return Collections.emptyList();
            }

        } else {
            payload = opt.get();
            System.out.printf(">>>> last cached price: %s\n", payload);

        } // cache is working fine, expires on redis after 1 minute.

        // problem at payload to json

        return Collections.emptyList();

    }

}