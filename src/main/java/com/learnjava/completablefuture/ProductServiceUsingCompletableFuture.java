package com.learnjava.completablefuture;

import com.learnjava.domain.*;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.CommonUtil.stopWatchReset;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingCompletableFuture {
    private final ProductInfoService productInfoService;
    private final ReviewService reviewService;
    private final InventoryService inventoryService;

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService,
                                                InventoryService inventoryService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
        this.inventoryService = inventoryService;
    }

    public Product retrieveProductDetails(String productId) {
        stopWatch.start();

        Product product = retrieveProductDetails_approach2(productId)
                .join(); // block the thread

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        stopWatchReset();
        return product;
    }

    public CompletableFuture<Product> retrieveProductDetails_approach2(String productId) {
//        stopWatch.start();

        CompletableFuture<ProductInfo> cfProductInfo =
                CompletableFuture.supplyAsync(()->productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> cfReview =
                CompletableFuture.supplyAsync(()->reviewService.retrieveReviews(productId));

        CompletableFuture<Product> product = cfProductInfo
                .thenCombine(cfReview, (productInfo, review) -> new Product(productId, productInfo, review));
//        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
//        stopWatchReset();
        return product;
    }

    public Product retrieveProductDetailsWithInventory(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> cfProductInfo = CompletableFuture
                .supplyAsync(()->productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    productInfo.setProductOptions(updateInventory(productInfo));
                    return productInfo;
                });

        CompletableFuture<Review> cfReview =
                CompletableFuture.supplyAsync(()->reviewService.retrieveReviews(productId));

        Product product = cfProductInfo
                .thenCombine(cfReview, (productInfo, review) -> new Product(productId, productInfo, review))
                .join();

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        stopWatchReset();
        return product;
    }

    private List<ProductOption> updateInventory(ProductInfo productInfo) {
        return productInfo.getProductOptions()
                .stream()
                .peek(productOption -> {
                    Inventory inventory = inventoryService.retrieveInventory(productOption);
                    productOption.setInventory(inventory);
                })
                .collect(Collectors.toList());
    }

    public Product retrieveProductDetailsWithInventory_approach2(String productId) {
//        stopWatch.start();

        CompletableFuture<ProductInfo> cfProductInfo = CompletableFuture
                .supplyAsync(()->productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    productInfo.setProductOptions(
                            updateInventory_approach2(productInfo));
                    return productInfo;
                });

        CompletableFuture<Review> cfReview = CompletableFuture
                .supplyAsync(()->reviewService.retrieveReviews(productId))
                .exceptionally((e) -> {
                    log("Handled the exception in reviewService: " + e.getMessage());
                    return Review.builder()
                            .noOfReviews(0)
                            .overallRating(0.0)
                            .build();
                });

        Product product = cfProductInfo
                .thenCombine(cfReview, (productInfo, review) -> new Product(productId, productInfo, review))
                .whenComplete((product1, ex) -> {
                  log("Inside WhenComplete: " + product1 + " and the exception is "
                  + ex);
//                  stopWatchReset();
                })
                .join();

//        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
//        stopWatchReset();
        return product;
    }

    private List<ProductOption> updateInventory_approach2(ProductInfo productInfo) {
         List<CompletableFuture<ProductOption>> productOptionList = productInfo.getProductOptions()
                .stream()
                .map(productOption ->
                        CompletableFuture.supplyAsync(
                                ()-> inventoryService.retrieveInventory(productOption))
                                .exceptionally((e) -> {
                                    log("Handled the exception in retrieveInventory: " + e.getMessage());
                                    return Inventory.builder()
                                            .count(1)
                                            .build();
                                })
                        .thenApply(inventory->{
                            productOption.setInventory(inventory);
                            return productOption;
                        }))
                .collect(Collectors.toList());
         return productOptionList.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public static void main(String[] args) {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        InventoryService inventoryService = new InventoryService();
        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService, inventoryService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

    }
}
