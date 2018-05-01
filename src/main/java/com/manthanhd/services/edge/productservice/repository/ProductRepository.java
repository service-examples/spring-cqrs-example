package com.manthanhd.services.edge.productservice.repository;

import com.manthanhd.services.edge.productservice.models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {
}
