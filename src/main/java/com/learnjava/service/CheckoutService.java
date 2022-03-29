package com.learnjava.service;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CartItem;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;

import java.util.List;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;
import static java.util.stream.Collectors.summingDouble;

public class CheckoutService {

    private final PriceValidatorService priceValidatorService;

    public CheckoutService(PriceValidatorService priceValidatorService) {
        this.priceValidatorService = priceValidatorService;
    }
    public CheckoutResponse checkout(Cart cart) {
        startTimer();

        List<CartItem> priceValidationList = cart.getCartItemList()
                .parallelStream()
                .peek(cartItem -> {
                    boolean isPriceInvalid = priceValidatorService.isCartItemInvalid(cartItem);
                    cartItem.setExpired(isPriceInvalid);
                })
                .filter(CartItem::isExpired)
                .collect(Collectors.toList());
        timeTaken();
        stopWatchReset();
        
        if (!priceValidationList.isEmpty()) {
            return new CheckoutResponse(CheckoutStatus.FAILURE, priceValidationList);
        }
   
        double finalPrice = calculateFinalPrice_reduce(cart);
        log("Checkout complete and the final price is " + finalPrice);
        
        return new CheckoutResponse(CheckoutStatus.SUCCESS, finalPrice);
    }

    private double calculateFinalPrice(Cart cart) {

        return cart.getCartItemList()
                .parallelStream()
//                .map(cartItem -> cartItem.getQuantity() * cartItem.getRate())
//                .collect(summingDouble(Double::doubleValue));
                .mapToDouble(cartItem -> cartItem.getQuantity() * cartItem.getRate())
                .sum();
    }

    private double calculateFinalPrice_reduce(Cart cart) {

        return cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> cartItem.getQuantity() * cartItem.getRate())
//                .reduce(0.0, (x,y) -> x+y);
                .reduce(0.0, Double::sum);

    }
}
