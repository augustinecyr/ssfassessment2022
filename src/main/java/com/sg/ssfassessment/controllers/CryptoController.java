package com.sg.ssfassessment.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sg.ssfassessment.models.Crypto;
import com.sg.ssfassessment.services.CryptoService;


@Controller
@RequestMapping("/price")
public class CryptoController {

    @Autowired
    private CryptoService cryptoSvc;

 
    @GetMapping
    public String getPrice(Model model, @RequestParam String coin, @RequestParam String currency) {

        cryptoSvc.getPrice(coin.toUpperCase(), currency.toUpperCase());
        List<Crypto> list = cryptoSvc.getPrice(coin, currency); // must be same from service function
        model.addAttribute("coin", coin);
        model.addAttribute("currency", currency);
        model.addAttribute("list", list);

        return "price";

    }

    
  


}
