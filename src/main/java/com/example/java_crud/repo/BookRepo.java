package com.example.java_crud.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.java_crud.model.Book;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

}
