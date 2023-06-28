package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.gestioneclubdellibro.GenereService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Genere;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/generi")
public class GenereController {
    private final GenereService genereService;

    @GetMapping(value = "")
    @ResponseBody
    @CrossOrigin
    public Set<Genere> visualizzaGeneri() {
       return genereService.getAllGeneri();
    }
}
