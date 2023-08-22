package com.api.oderapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.oderapi.domain.model.Product;
import com.api.oderapi.exceptions.GeneralServiceException;
import com.api.oderapi.exceptions.NoDataFoundException;
import com.api.oderapi.exceptions.ValidateServiceException;
import com.api.oderapi.repository.ProductRepository;
import com.api.oderapi.validators.ProductValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAll(Pageable page) {
        log.debug("getAll =>"); // DISPONIBLE EN MODO DEBUGGER
        try {
            // CONVIERTE PAGE<PRODUCT> A LIST<PRODUCT>
            List<Product> products = productRepository.findAll(page).toList();
            return products;

        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    public Product getProductById(Long id) {
        try {
            Product productById = productRepository.findById(id)
                    .orElseThrow(() -> new NoDataFoundException("No existe el producto con id: " + id));
            return productById;

        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    @Transactional
    public Product postUpdateProduct(Long id, Product product) {
        try {
            ProductValidator.save(product);

            if (id == null) {
                return productRepository.save(product);
            }
            Product productById = productRepository.findById(id)
                    .orElseThrow(() -> new NoDataFoundException("No existe el producto con id: " + id));
            productById.setName(product.getName());
            productById.setPrice(product.getPrice());
            productRepository.save(productById);
            return productById;

        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }

    }

    @Transactional
    public void deleteProduct(Long id) {
        try {
            Product productById = productRepository.findById(id)
                    .orElseThrow(() -> new NoDataFoundException("No existe el producto con id: " + id));
            productRepository.delete(productById);

        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

}
