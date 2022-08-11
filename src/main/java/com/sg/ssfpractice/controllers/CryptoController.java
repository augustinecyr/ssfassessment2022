package com.sg.ssfpractice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sg.ssfpractice.models.Crypto;
import com.sg.ssfpractice.services.CryptoService;

@Controller
@RequestMapping("/price")
public class CryptoController {

    @Autowired
    private CryptoService cryptoSvc;

    @GetMapping
    public String getPrice(Model model, @RequestParam String coin, @RequestParam String currency) {

        cryptoSvc.getPrice(coin.toUpperCase(), currency.toUpperCase());
        List<Crypto> price = cryptoSvc.getPrice(coin, currency);
        model.addAttribute("coin", coin);
        model.addAttribute("currency", currency);
        model.addAttribute("price", price);

        return "price";

    }
}
