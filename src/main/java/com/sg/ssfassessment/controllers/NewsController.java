package com.sg.ssfassessment.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sg.ssfassessment.models.News;
import com.sg.ssfassessment.services.NewsService;

@Controller
@RequestMapping("/text")
public class NewsController {
    @Autowired
    private NewsService newsSvc;

    @GetMapping
    public String getArticles(Model model, @RequestParam String categories) {

        newsSvc.getArticles(categories);
        List<News> newslist = newsSvc.getArticles(categories);
        model.addAttribute("categories", categories);
        model.addAttribute("newslist", newslist);

        return "text";
    }

}
