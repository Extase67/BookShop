package com.example.demo.repository;

import com.example.demo.exception.DataProcessingException;
import com.example.demo.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookRepositoryImpl implements BookRepository {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Book save(Book book) {
        EntityTransaction entityTransaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(book);
            entityTransaction.commit();
            return book;
        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            throw new DataProcessingException("Unable to save book in DB " + book, e);
        }

    }

    @Override
    public Optional<Book> findBookById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Book book = entityManager.find(Book.class, id);
            return Optional.ofNullable(book);
        } catch (Exception e) {
            throw new DataProcessingException("Unable to find book with id " + id, e);
        }
    }

    @Override
    public List<Book> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT b from Book b", Book.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Unable to find all books in DB", e);
        }
    }
}
