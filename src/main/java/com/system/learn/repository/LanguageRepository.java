package com.system.learn.repository;

import com.system.learn.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language> findAllByOrderByNameAsc(); // по алфавиту
    List<Language> findAllByOrderByIdAsc(); // по id
}