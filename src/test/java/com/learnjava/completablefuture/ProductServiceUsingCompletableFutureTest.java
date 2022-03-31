package com.learnjava.completablefuture;

import com.learnjava.domain.Product;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductServiceUsingCompletableFutureTest {

    private final ProductInfoService pis = new ProductInfoService();
    private final ReviewService rs = new ReviewService();

    ProductServiceUsingCompletableFuture pscf = new ProductServiceUsingCompletableFuture(pis, rs);

    @Test
    void retrieveProductDetails() {
        // given

        String productId = "ABC123";

        //when
        Product product = pscf.retrieveProductDetails(productId);

        //then
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        assertNotNull(product.getReview());
    }
    @Test
    void retrieveProductDetails_approach2() {
        // given

        String productId = "ABC123";

        //when
        Product product = pscf.retrieveProductDetails_approach2(productId).join();

        //then
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        assertNotNull(product.getReview());
    }
}
