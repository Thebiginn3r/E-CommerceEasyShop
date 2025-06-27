package org.yearup.data;

import org.yearup.models.ShoppingCart;

import java.util.List;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    ShoppingCart updateProductAmount(int userId, int productId, int quantity);
    ShoppingCart clearCart(int userId);
    ShoppingCart addProduct(int userId, int productId, int quantity);
    ShoppingCart addProductWithout(int userId, int productId);

    // add additional method signatures here
}
