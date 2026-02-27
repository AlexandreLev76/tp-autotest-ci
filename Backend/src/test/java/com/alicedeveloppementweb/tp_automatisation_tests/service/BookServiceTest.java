package com.alicedeveloppementweb.tp_automatisation_tests.service;

import com.alicedeveloppementweb.tp_automatisation_tests.model.Book;
import com.alicedeveloppementweb.tp_automatisation_tests.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService service;

    @Test
    void should_create_book() {
        // ARRANGE
        Book book = new Book(null, "1984", "Orwell", 1948);

        // ACT
        when(repository.save(book)).thenReturn(
                new Book(1L, "1984", "Orwell", 1948)
        );

        Book saved = service.create(book);

        // ASSERT
        assertNotNull(saved.getId());
        assertEquals("1984", saved.getTitre());
        verify(repository).save(book);
    }

    @Test
    void should_get_all_books() {
        // ARRANGE
        List<Book> books = List.of(
                new Book(1L, "1984", "Orwell", 1948),
                new Book(2L, "Dune", "Frank Herbert", 1965)
        );
        when(repository.findAll()).thenReturn(books);

        // ACT
        List<Book> result = service.getAll();

        // ASSERT
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1984", result.get(0).getTitre());
        assertEquals("Dune", result.get(1).getTitre());
        verify(repository).findAll();
    }

    @Test
    void should_get_book_by_id() {
        // ARRANGE
        Long id = 1L;
        Book book = new Book(id, "1984", "Orwell", 1948);
        when(repository.findById(id)).thenReturn(Optional.of(book));

        // ACT
        Book result = service.getById(id);

        // ASSERT
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("1984", result.getTitre());
        verify(repository).findById(id);
    }

    @Test
    void should_return_null_when_get_by_id_not_found() {
        // ARRANGE
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // ACT
        Book result = service.getById(999L);

        // ASSERT
        assertNull(result);
        verify(repository).findById(999L);
    }

    @Test
    void should_update_book() {
        // ARRANGE
        Long id = 1L;
        Book updates = new Book(null, "1984 (édition révisée)", "George Orwell", 1949);
        Book saved = new Book(id, "1984 (édition révisée)", "George Orwell", 1949);
        when(repository.save(any(Book.class))).thenReturn(saved);

        // ACT
        Book result = service.update(id, updates);

        // ASSERT
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("1984 (édition révisée)", result.getTitre());
        assertEquals("George Orwell", result.getAuteur());
        assertEquals(1949, result.getAnnee());
        verify(repository).save(argThat(b -> id.equals(b.getId())));
    }

    @Test
    void should_delete_book() {
        // ARRANGE
        Long id = 1L;

        // ACT
        service.delete(id);

        // ASSERT
        verify(repository).deleteById(id);
    }

    @Test
    void should_return_empty_list_when_no_books() {
        when(repository.findAll()).thenReturn(List.of());

        List<Book> result = service.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findAll();
    }
}
