package ee.cgi.practice.reservation.service;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ee.cgi.practice.reservation.model.RestaurantTable;
import ee.cgi.practice.reservation.model.TableFeature;
import ee.cgi.practice.reservation.model.Zone;
import ee.cgi.practice.reservation.repository.TableRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TableRepository tableRepository;

    public DataInitializer(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public void run(String... args) {
        tableRepository.deleteAll(); // clear existing data before seeding

        // Zone 1: Main Hall
        tableRepository.save(new RestaurantTable(2, Zone.MAIN_HALL, Set.of(TableFeature.WINDOW, TableFeature.SOFA), 50, 50));
        tableRepository.save(new RestaurantTable(2, Zone.MAIN_HALL, Set.of(TableFeature.WINDOW), 100, 50));
        tableRepository.save(new RestaurantTable(6, Zone.MAIN_HALL, Set.of(TableFeature.SOFA), 250, 50));
        tableRepository.save(new RestaurantTable(8, Zone.MAIN_HALL, Set.of(TableFeature.SOFA), 50, 150));

        // Zone 2: Private Rooms (here is no sofa but are private)
        tableRepository.save(new RestaurantTable(4, Zone.PRIVATE_ROOMS, Set.of(TableFeature.CORNER), 50, 100));
        tableRepository.save(new RestaurantTable(2, Zone.PRIVATE_ROOMS, Set.of(), 300, 100));

        // Zone 3: Terrace
        tableRepository.save(new RestaurantTable(4, Zone.TERRACE, Set.of(TableFeature.WINDOW), 50, 50));
        tableRepository.save(new RestaurantTable(8, Zone.TERRACE, Set.of(), 150, 50));

        System.out.println("Restorant layout initialized");
    }
}