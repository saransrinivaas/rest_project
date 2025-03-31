package com.examly.springapp.controllers.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Tag(name = "Product Controller", description = "APIs for managing products")
public class ProductController {

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Fetch a product based on its ID")
    public String getProduct(@PathVariable Long id) {
        return "Product " + id;
    }

    @PostMapping
    @Operation(summary = "Add new product", description = "Creates a new product")
    public String addProduct(@RequestBody String product) {
        return "Product added: " + product;
    }
}