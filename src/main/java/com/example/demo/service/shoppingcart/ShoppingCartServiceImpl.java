package com.example.demo.service.shoppingcart;

import com.example.demo.dto.shoppingcart.CartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.dto.shoppingcart.UpdateCartItemRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.shoppingcart.CartItemRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartResponseDto getCartByUserId(Long userId) {
        ShoppingCart cart = findShoppingCart(userId);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto addBookToCart(
            Long userId, CartItemRequestDto cartItemRequestDto) {
        ShoppingCart cart = findShoppingCart(userId);
        Book book = bookRepository.findById(cartItemRequestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id: " + cartItemRequestDto.bookId()));
        cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(book.getId()))
                .findFirst()
                .ifPresentOrElse(item -> item.setQuantity(item.getQuantity()
                                + cartItemRequestDto.quantity()),
                        () -> addCartItem(cart, book, cartItemRequestDto.quantity()));
        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateCartItem(Long itemId, User user,
                                                      UpdateCartItemRequestDto cartItemDto) {
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(itemId, user.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find item with id " + itemId));
        cartItem.setQuantity(cartItemDto.quantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(findShoppingCart(user.getId()));
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto removeItemFromCart(User user, Long itemId) {
        cartItemRepository.deleteCartItemByIdAndShoppingCartId(itemId, user.getId());
        return shoppingCartMapper.toDto(findShoppingCart(user.getId()));
    }

    private void addCartItem(ShoppingCart shoppingCart, Book book, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItem.setQuantity(quantity);
        shoppingCart.getCartItems().add(cartItem);
    }

    private ShoppingCart findShoppingCart(Long id) {
        return shoppingCartRepository.findByUserId(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find shopping cart with id " + id));
    }
}
