package com.example.productservice.service;

import com.example.productservice.entity.Image;
import com.example.productservice.entity.Product;
import com.example.productservice.model.ImageView;
import com.example.productservice.model.ProductView;
import com.example.productservice.repository.ImageRepo;
import com.example.productservice.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepo productRepo;
    @Autowired
    ImageRepo imageRepo;
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> findAll() {
        return productRepo.findAll();
    }

    public List<Product> findAll(Sort sort) {
        return productRepo.findAll(sort);
    }

    public List<Product> findAllById(Iterable<Long> longs) {
        return productRepo.findAllById(longs);
    }

    public <S extends Product> S save(S entity) {
        return productRepo.save(entity);
    }

    public Optional<Product> findById(Long aLong) {
        return productRepo.findById(aLong);
    }

    public long count() {
        return productRepo.count();
    }

    @Query(value = "SELECT * FROM product WHERE id=:id", nativeQuery = true)
    public Product getProductById(Long id) {
        return productRepo.getProductById(id);
    }

    public List<ProductView> getAll(){
        List<ProductView> list = new ArrayList<>();
        List<Product> listProduct = findAll();
        for(Product pr: listProduct){
            ProductView product = new ProductView();
            product.setId(pr.getId());
            product.setName(pr.getName());
            product.setDescription(pr.getDescription());
            product.setPrice(pr.getPrice());
            product.setCreateDate(pr.getCreateDate());
            List<Image> listImage = imageRepo.findImageByProductId(pr.getId());
            List<ImageView> imageViewList = new ArrayList<>();

            for(Image image:listImage){
                ImageView imageView =new ImageView(image.getId(), image.getImage());
                imageViewList.add(imageView);
            }

            product.setImages(imageViewList);
            list.add(product);
        }
        return list;
    }

    public ProductView getById(Long id){
        ProductView product = new ProductView();
        Product pr = getProductById(id);
        if (pr==null)
            return new ProductView();
        product.setId(pr.getId());
        product.setName(pr.getName());
        product.setDescription(pr.getDescription());
        product.setPrice(pr.getPrice());
        product.setCreateDate(pr.getCreateDate());
        List<Image> listImage = imageRepo.findImageByProductId(pr.getId());
        List<ImageView> imageViewList = new ArrayList<>();

        for(Image image:listImage){
            ImageView imageView =new ImageView(image.getId(), image.getImage());
            imageViewList.add(imageView);
        }

        product.setImages(imageViewList);
        return product;
    }
}
