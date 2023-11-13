package com.ecom.Model.mapper;

import com.ecom.Entity.Brand;
import com.ecom.Entity.Category;
import com.ecom.Entity.Product;
import com.ecom.Model.dto.ProductDTO;
import com.ecom.Model.request.CreateProductRequest;
import com.github.slugify.Slugify;
import java.util.ArrayList;

public class ProductMapper {

    public static ProductDTO toProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setSalePrice(product.getSalePrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setImages(product.getImages());
        productDTO.setFeedBackImages(product.getImageFeedBack());
        productDTO.setTotalSold(product.getTotalSold());
        productDTO.setStatus(product.getStatus());

        return productDTO;
    }

    public static Product toProduct(CreateProductRequest createProductRequest) {
        Product product = new Product();
        product.setName(createProductRequest.getName());
        product.setDescription(createProductRequest.getDescription());
        product.setPrice(createProductRequest.getPrice());
        product.setSalePrice(createProductRequest.getSalePrice());
        product.setImages(createProductRequest.getImages());
        product.setImageFeedBack(createProductRequest.getFeedBackImages());
        product.setStatus(createProductRequest.getStatus());
        //Gen slug
        Slugify slug = new Slugify();
        product.setSlug(slug.slugify(createProductRequest.getName()));
        //Brand
        Brand brand = new Brand();
        brand.setId(createProductRequest.getBrandId());
        product.setBrand(brand);
        //Category
        ArrayList<Category> categories = new ArrayList<>();
        for (Integer id : createProductRequest.getCategoryIds()) {
            Category category = new Category();
            category.setId(id);
            categories.add(category);
        }
        product.setCategories(categories);

        return product;
    }

    public static Product toUpdateProduct(CreateProductRequest createProductRequest, Product product2) {
        product2.setName(createProductRequest.getName());
        product2.setDescription(createProductRequest.getDescription());
        product2.setPrice(createProductRequest.getPrice());
        product2.setSalePrice(createProductRequest.getSalePrice());
        product2.setImages(createProductRequest.getImages());
        product2.setImageFeedBack(createProductRequest.getFeedBackImages());
        product2.setStatus(createProductRequest.getStatus());
        //Gen slug
        Slugify slug = new Slugify();
        product2.setSlug(slug.slugify(createProductRequest.getName()));
        //Brand
        Brand brand = new Brand();
        brand.setId(createProductRequest.getBrandId());
        product2.setBrand(brand);
        //Category
        ArrayList<Category> categories = new ArrayList<>();
        for (Integer id : createProductRequest.getCategoryIds()) {
            Category category = new Category();
            category.setId(id);
            categories.add(category);
        }
        product2.setCategories(categories);

        return product2;
    }
}
