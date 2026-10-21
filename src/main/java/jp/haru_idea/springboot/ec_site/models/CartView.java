package jp.haru_idea.springboot.ec_site.models;

import java.util.ArrayList;
import java.util.Collection;

public class CartView {
    public Collection<CartDetailView> cartDetailViews = new ArrayList<>();

    public int totalPrice;

    public Collection<CartDetailView> getCartDetailViews() {
        return cartDetailViews;
    }

    public void setCartDetailViews(Collection<CartDetailView> cartDetailViews) {
        this.cartDetailViews = cartDetailViews;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

}
