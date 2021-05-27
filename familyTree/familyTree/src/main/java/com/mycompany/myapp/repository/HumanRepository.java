package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Human;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Human entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HumanRepository extends JpaRepository<Human, Long> {}
