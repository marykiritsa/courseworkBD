package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Relatives;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Relatives entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RelativesRepository extends JpaRepository<Relatives, Long> {}
