package com.example.productmanagement.controller;

import com.example.productmanagement.model.Manufacturer;
import com.example.productmanagement.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;

    @GetMapping
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manufacturer> getManufacturerById(@PathVariable Long id) {
        return manufacturerService.findById(id)
                .map(manufacturer -> ResponseEntity.ok().body(manufacturer))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Manufacturer createManufacturer(@RequestBody Manufacturer manufacturer) {
        return manufacturerService.save(manufacturer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manufacturer> updateManufacturer(@PathVariable Long id, @RequestBody Manufacturer manufacturerDetails) {
        return manufacturerService.findById(id)
                .map(manufacturer -> {
                    manufacturer.setName(manufacturerDetails.getName());
                    manufacturer.setOrigin(manufacturerDetails.getOrigin());
                    manufacturer.setDescription(manufacturerDetails.getDescription());
                    Manufacturer updatedManufacturer = manufacturerService.save(manufacturer);
                    return ResponseEntity.ok().body(updatedManufacturer);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable Long id) {
        if (manufacturerService.findById(id).isPresent() && manufacturerService.findById(id).get().getProducts().isEmpty()) {
            manufacturerService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
