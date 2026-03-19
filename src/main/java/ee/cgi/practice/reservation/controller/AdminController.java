package ee.cgi.practice.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ee.cgi.practice.reservation.repository.TableRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final TableRepository tableRepository;

    public AdminController(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    // Table update endpoint for admin to change table position
    @PatchMapping("/tables/{id}/position")
    public ResponseEntity<?> updateTablePosition(
            @PathVariable Long id, 
            @RequestParam int x, 
            @RequestParam int y) {
        
        return tableRepository.findById(id).map(table -> {
            table.setPosX(x);
            table.setPosY(y);
            tableRepository.save(table);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}