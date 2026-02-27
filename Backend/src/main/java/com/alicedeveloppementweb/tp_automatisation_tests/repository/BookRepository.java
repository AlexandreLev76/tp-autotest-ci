package com.alicedeveloppementweb.tp_automatisation_tests.repository;

import com.alicedeveloppementweb.tp_automatisation_tests.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
