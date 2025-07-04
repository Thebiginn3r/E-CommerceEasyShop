package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    private final DataSource dataSource;

    //private ShoppingCart shoppingCart;

    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;

    }


    @Override
    public ShoppingCart getByUserId(int userId) {
        String sql = "SELECT product_id, quantity FROM shopping_cart WHERE user_id = ?";
        ShoppingCart cart = new ShoppingCart();

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");

                ShoppingCartItem item = new ShoppingCartItem(productId, quantity);
                cart.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving cart", e);
        }

        return cart;
    }

    @Override
    public ShoppingCart addProductWithout(int userId, int productId) {
        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?) ";

        ShoppingCart cart = new ShoppingCart();
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            //stmt.setInt(3, quantity);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding item to cart", e);
        }
        return cart;

    }

    @Override
    public ShoppingCart  updateProductAmount(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        ShoppingCart cart = new ShoppingCart();
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, userId);
            stmt.setInt(3, productId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating item in cart", e);
        }
        return cart;
    }

    @Override
    public ShoppingCart clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";
        ShoppingCart cart = new ShoppingCart();
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing cart", e);
        }
        return cart;
    }

    @Override
    public ShoppingCart addProduct(int userId, int productId, int quantity) {
        return null;
    }


}
