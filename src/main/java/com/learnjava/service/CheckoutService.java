package com.learnjava.service;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CartItem;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;

import java.util.List;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.*;

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
        else {
            return new CheckoutResponse(CheckoutStatus.SUCCESS);
        }
    }
}
