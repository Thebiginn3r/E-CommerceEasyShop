package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("cart")


// convert this class to a REST controller
// only logged in users should have access to these actions
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;
    private ShoppingCart ShoppingCart;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // each method in this controller requires a Principal object as a parameter
    @GetMapping("")
    //@PreAuthorize("isAuthenticated()")
    public ShoppingCart getCart(Principal principal) {
        try {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            ShoppingCart cart = shoppingCartDao.getByUserId(user.getId());
            return cart != null ? cart : new ShoppingCart();

            // use the shoppingcartDao to get all items in the cart and return the cart
            //return shoppingCartDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("/products/{productId}")
    //@PreAuthorize("isAuthenticated()")
    public ShoppingCart addToCart(@PathVariable int productId, Principal principal){
        String username = principal.getName();
        int userId = userDao.getIdByUsername(username);
        if(userId <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
       // int quantity = 1;
        shoppingCartDao.addProduct(userId, productId, 1);
        return ShoppingCart;
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("/products/{productId}")
   // @PreAuthorize("isAuthenticated()")
    public ShoppingCart updateAmount(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal){
        String username = principal.getName();
        int userId = userDao.getIdByUsername(username);
        shoppingCartDao.updateProductAmount(userId, productId, item.getQuantity());
        return ShoppingCart;
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping
    //@PreAuthorize("isAuthenticated()")
    public ShoppingCart clearCart(Principal principal){
        String userName = principal.getName();
        int userId = userDao.getIdByUsername(userName);
        shoppingCartDao.clearCart(userId);
        return ShoppingCart;
    }

}
