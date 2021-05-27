package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TypeOfRelationship;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypeOfRelationship entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeOfRelationshipRepository extends JpaRepository<TypeOfRelationship, Long> {}
