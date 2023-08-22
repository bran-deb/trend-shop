package com.api.oderapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.logging.Logger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.oderapi.converter.ProductConverter;
import com.api.oderapi.domain.model.Product;
import com.api.oderapi.dto.ProductDTO;
import com.api.oderapi.service.ProductService;
import com.api.oderapi.utilities.WrapperResponse;

// @Slf4j       //LOGGER
@RequestMapping("products")
@RestController
public class ProductController {

        @Autowired
        private ProductService productService;

        @Autowired
        private ProductConverter converter;

        private static final Logger log = Logger.getLogger(ProductController.class.getCanonicalName());

        // localhost:8080/api/v1/?pageNumber=0&pageZise=3
        @GetMapping({ "", "/" })
        public ResponseEntity<WrapperResponse<List<ProductDTO>>> getAll(
                        // PAGINACION DE LA RESPONSE
                        @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
                        @RequestParam(value = "pageZise", required = false, defaultValue = "5") int pageZise) {
                Pageable page = PageRequest.of(pageNumber, pageZise); // response paginable

                log.info("getAll =>");
                List<Product> products = productService.getAll(page);
                List<ProductDTO> productDTOs;
                // CONVIERTE ENTITY A DTO PARA USO DEL CONTROLLER
                productDTOs = converter.fromEntity(products);
                // CONVIERTE A UN WRAPPERRESPONSE PARA ESTANDARIZAR FORMATO DE RESPONSE
                WrapperResponse<List<ProductDTO>> response = new WrapperResponse<List<ProductDTO>>(true, "success",
                                productDTOs);

                // USA EL METODO WRAPER PARA RETORNAR RESPONSEENTITY
                return response.createResponse(HttpStatus.OK);
                // return new ResponseEntity<WrapperResponse<List<ProductDTO>>>(
                // response,
                // HttpStatusCode.valueOf(200));
        }

        @GetMapping("/{id}")
        public ResponseEntity<WrapperResponse<ProductDTO>> getProductById(@PathVariable Long id) {
                log.info("getProductByI =>");
                Product productById = productService.getProductById(id);
                // CONVIERTE ENTITY A DTO PARA USO DEL CONTROLLER
                ProductDTO productDTO = converter.fromEntity(productById);
                // CONVIERTE A UN WRAPPERRESPONSE PARA ESTANDARIZAR FORMATO DE RESPONSE
                WrapperResponse<ProductDTO> response = new WrapperResponse<ProductDTO>(true, "successs", productDTO);

                // USA EL METODO WRAPER PARA RETORNAR RESPONSEENTITY
                return response.createResponse(HttpStatus.OK);
                // return new ResponseEntity<WrapperResponse<ProductDTO>>(
                // response,
                // HttpStatusCode.valueOf(200));
        }

        // CONTROLLERS SOLO TRABAJAN CON DTOS PARA DESACOPLAR
        // SERVICES SOLO TRABAJAN CON ENTITIES PARA DESACOPLAR
        @PostMapping("/")
        public ResponseEntity<WrapperResponse<ProductDTO>> postProduct(@RequestBody ProductDTO product) {
                log.info("postProduct =>");
                // CONVIERTE DTO A ENTITY PARA USO DEL SERVICE
                Product addedProduct = productService.postUpdateProduct(converter.fromDTO(product).getId(),
                                converter.fromDTO(product));
                // CONVIERTE ENTITY A DTO PARA USO DEL CONTROLLER
                ProductDTO productDTO = converter.fromEntity(addedProduct);
                // CONVIERTE A UN WRAPPERRESPONSE PARA ESTANDARIZAR FORMATO DE RESPONSE
                WrapperResponse<ProductDTO> response = new WrapperResponse<ProductDTO>(true, "success", productDTO);

                // USA EL METODO WRAPER PARA RETORNAR RESPONSEENTITY
                return response.createResponse(HttpStatus.CREATED);
                // return new ResponseEntity<WrapperResponse<ProductDTO>>(response,
                // HttpStatus.OK);
        }

        @PutMapping("/{id}")
        public ResponseEntity<WrapperResponse<ProductDTO>> putProduct(
                        @PathVariable Long id,
                        @RequestBody ProductDTO product) {
                log.info("putProduct =>");
                // CONVIERTE DTO A ENTITY PARA USO DEL SERVICE
                Product updaProduct = productService.postUpdateProduct(id, converter.fromDTO(product));
                // CONVIERTE ENTITY A DTO PARA USO DEL CONTROLLER
                ProductDTO productDTO = converter.fromEntity(updaProduct);
                // CONVIERTE A UN WRAPPERRESPONSE PARA ESTANDARIZAR FORMATO DE RESPONSE
                WrapperResponse<ProductDTO> response = new WrapperResponse<ProductDTO>(true, "success", productDTO);

                // USA EL METODO WRAPER PARA RETORNAR RESPONSEENTITY
                return response.createResponse(HttpStatus.OK);
                // return new ResponseEntity<WrapperResponse<ProductDTO>>(
                // response,
                // HttpStatusCode.valueOf(200));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
                log.info("deleteProduct =>");
                productService.deleteProduct(id);
                // SI ES VOID NO SE PONE EL TIPO DE DATO
                WrapperResponse<?> response = new WrapperResponse<>(true, "success", null);

                // USA EL METODO WRAPER PARA RETORNAR RESPONSEENTITY
                return response.createResponse(HttpStatus.OK);
                // return new ResponseEntity<List<ProductDTO>>(
                // productDTOs,
                // HttpStatusCode.valueOf(200));
        }
}
