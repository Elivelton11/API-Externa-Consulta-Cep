package com.example.demo.controller;

import org.springframework.ui.Model;
import com.example.demo.DTO.ApplicationCepResultDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class ApplicationController {

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/consulta-cep")
    public String CepResult(@RequestParam String cep, Model model){
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://viacep.com.br/ws/%s/json", cep);

        try {
            ApplicationCepResultDTO cepData = restTemplate.getForObject(url, ApplicationCepResultDTO.class);
            model.addAttribute("cepData",cepData);
        }catch (Exception e){
            model.addAttribute("erro: cepData não encontrado");
        }

        return "resultado";

    }

}
