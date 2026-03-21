package ee.cgi.practice.reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Admin page view
@Controller
public class ViewController {

    @GetMapping("/admin") 
    public String adminPage() {
        return "admin"; 
    }
}