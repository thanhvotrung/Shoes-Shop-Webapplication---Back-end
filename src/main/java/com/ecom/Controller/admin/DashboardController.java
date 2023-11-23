package com.ecom.Controller.admin;

import com.ecom.Model.dto.ChartDTO;
import com.ecom.Model.dto.StatisticDTO;
import com.ecom.Model.request.FilterDayByDay;
import com.ecom.Repository.*;
import com.ecom.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class DashboardController {
    @Autowired
    private PostService postService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrdersService orderService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private StatisticRepository statisticRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/admin/count/posts")
    public ResponseEntity<Object> getCountPost(){
        long countPosts = postService.getCountPost();
        return ResponseEntity.ok(countPosts);
    }

    @GetMapping("/api/admin/count/products")
    public ResponseEntity<Object> getCountProduct(){
        long countProducts = productService.getCountProduct();
        return ResponseEntity.ok(countProducts);
    }

    @GetMapping("/api/admin/count/orders")
    public ResponseEntity<Object> getCountOrders(){
        long countOrders = orderService.getCountOrder();
        return ResponseEntity.ok(countOrders);
    }

    @GetMapping("/api/admin/count/categories")
    public ResponseEntity<Object> getCountCategories(){
        long countCategories = categoryService.getCountCategories();
        return ResponseEntity.ok(countCategories);
    }

    @GetMapping("/api/admin/count/brands")
    public ResponseEntity<Object> getCountBrands(){
        long countBrands = brandService.getCountBrands();
        return ResponseEntity.ok(countBrands);
    }

    @GetMapping("/api/admin/count/users")
    public ResponseEntity<Object> getCountUsers(){
        long countUsers = userRepository.count();
        return ResponseEntity.ok(countUsers);
    }

    @GetMapping("/api/admin/statistics")
    public ResponseEntity<Object> getStatistic30Day(){
        List<StatisticDTO> statistics = statisticRepository.getStatistic30Day();
        return ResponseEntity.ok(statistics);
    }

    @PostMapping("/api/admin/statistics")
    public ResponseEntity<Object> getStatisticDayByDay(@RequestBody FilterDayByDay filterDayByDay){
        List<StatisticDTO> statisticDTOS = statisticRepository.getStatisticDayByDay(filterDayByDay.getToDate(),filterDayByDay.getFromDate());
        return ResponseEntity.ok(statisticDTOS);
    }

    @GetMapping("/api/admin/product-order-categories")
    public ResponseEntity<Object> getListProductOrderCategories(){
        List<ChartDTO> chartDTOS = categoryRepository.getListProductOrderCategories();
        return ResponseEntity.ok(chartDTOS);
    }

    @GetMapping("/api/admin/product-order-brands")
    public ResponseEntity<Object> getProductOrderBrands(){
        List<ChartDTO> chartDTOS = brandRepository.getProductOrderBrands();
        return ResponseEntity.ok(chartDTOS);
    }

    @GetMapping("/api/admin/product-order")
    public ResponseEntity<Object> getProductOrder(){
        Pageable pageable = PageRequest.of(0,10);
        Date date = new Date();
        List<ChartDTO> chartDTOS = productRepository.getProductOrders(pageable, date.getMonth() +1, date.getYear() + 1900);
        return ResponseEntity.ok(chartDTOS);
    }
}
