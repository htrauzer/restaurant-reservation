package ee.cgi.practice.reservation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.cgi.practice.reservation.model.RestaurantTable;
import ee.cgi.practice.reservation.repository.TableRepository;

@RestController
@RequestMapping("/api/tables") // Саме цей шлях шукає ваш JS: fetch('/api/tables')
public class TableController {

    private final TableRepository tableRepository;

    public TableController(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @GetMapping
    public List<RestaurantTable> getAllTables() {
        return tableRepository.findAll();
    }
}