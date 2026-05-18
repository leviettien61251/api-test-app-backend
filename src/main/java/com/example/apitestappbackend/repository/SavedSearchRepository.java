package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.SavedSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedSearchRepository extends JpaRepository<SavedSearch, Integer> {
}
