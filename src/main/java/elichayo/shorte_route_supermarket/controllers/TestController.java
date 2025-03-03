package elichayo.shorte_route_supermarket.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ILAN PERETZ | 25.02.2025
 */
@RestController
public class TestController 
{
   @GetMapping("/test")   
   public String test()
   {
      return "RestController HTTP-GET (http://localhost:8080/test) OK!";
   }
}
