package com.example.demo.repository.shoppingcart;

import com.example.demo.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long shoppingCartId);

    void deleteCartItemByIdAndShoppingCartId(Long id, Long shoppingCartId);
}
