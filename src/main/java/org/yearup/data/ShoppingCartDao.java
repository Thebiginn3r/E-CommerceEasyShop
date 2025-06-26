package org.yearup.data;

import org.yearup.models.ShoppingCart;

import java.util.List;

public interface ShoppingCartDao
{
    List<ShoppingCart> getAllCarts();
    ShoppingCart getByUserId(int userId);
    void updateProductAmount(int userId, int productId, int quantity);
    void clearCart(int userId);
    void addProduct(int userId, int productId, int quantity);
    // add additional method signatures here
}
