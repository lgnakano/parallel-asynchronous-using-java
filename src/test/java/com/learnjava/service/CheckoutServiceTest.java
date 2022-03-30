package com.learnjava.service;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;
import com.learnjava.util.DataSet;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {

    PriceValidatorService priceValidatorService = new PriceValidatorService();
    CheckoutService checkoutService = new CheckoutService(priceValidatorService);

    @Test
    void checkout_6_items() {


        // ginev

        Cart cart = DataSet.createCart(6);

        // when

        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);

        // then

        assertEquals(CheckoutStatus.SUCCESS, checkoutResponse.getCheckoutStatus());
        assertTrue(checkoutResponse.getFinalRate()>0);
    }

    @Test
    void no_of_cores(){
        //given

        //when
        System.out.println("no of cores: " + Runtime.getRuntime().availableProcessors());

        //then
    }

    @Test
    void parallelism(){
        //given

        //when
        System.out.println("parallelism: " + ForkJoinPool.getCommonPoolParallelism());

        //then
    }

    @Test
    void checkout_49_items() {

        // ginev

        Cart cart = DataSet.createCart(49);

        // when

        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);

        // then

        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
    }

    @Test
    void checkout_25_items() {

        // ginev

        Cart cart = DataSet.createCart(25);

        // when

        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);

        // then

        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
    }
}
