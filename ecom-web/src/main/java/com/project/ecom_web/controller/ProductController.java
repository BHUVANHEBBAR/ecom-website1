package com.project.ecom_web.controller;

import com.project.ecom_web.model.Product;
import com.project.ecom_web.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {
    @Autowired
    ProductService service;


    @RequestMapping("/")
    public String greet(){
        return "Hello world";
    }

    @GetMapping("/products")

    public ResponseEntity<List<Product>> getProd(){
        return new ResponseEntity<>(service.getAllProds(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity< Product> getOneProd(@PathVariable int id){

        Product p = service.getOneProd(id);

        if(p!=null)
        return new ResponseEntity<>(service.getOneProd(id),HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile){
        try {
            Product p = service.addProduct(product, imageFile);
            return new ResponseEntity<>(p,HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte []> getImageByProductId(@PathVariable int productId){
        Product p =service.getOneProd(productId);
        byte[] imageFile = p.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(p.getImageType()))
                .body(imageFile);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product,@RequestPart MultipartFile imageFile){
        Product p1 = null;
        try {
            p1 = service.updateProduct(id,product,imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(p1!=null){
           return new ResponseEntity<>("Updated",HttpStatus.OK);
       }
       else{
           return new ResponseEntity<>("Failed",HttpStatus.BAD_REQUEST);
       }


    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product p1 = service.getOneProd(id);
        if(p1!=null) {
            service.deleteProduct(id);
            return new ResponseEntity<>("Product Deleted", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Product Not found",HttpStatus.NOT_FOUND);

    }


    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        System.out.println("Searching with "+keyword);
        List<Product> ls = service.searchProducts(keyword);
        return new ResponseEntity<>(ls,HttpStatus.OK);
    }
}
