package com.learnjava.completablefuture;

import com.learnjava.domain.Product;
import com.learnjava.domain.ProductInfo;
import com.learnjava.domain.Review;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.CommonUtil.stopWatchReset;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingCompletableFuture {
    private final ProductInfoService productInfoService;
    private final ReviewService reviewService;

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public Product retrieveProductDetails(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> cfProductInfo =
                CompletableFuture.supplyAsync(()->productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> cfReview =
                CompletableFuture.supplyAsync(()->reviewService.retrieveReviews(productId));

        Product product = cfProductInfo
                .thenCombine(cfReview, (productInfo, review) -> new Product(productId, productInfo, review))
                .join(); // block the thread

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        stopWatchReset();
        return product;
    }

    public static void main(String[] args) {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

    }
}
