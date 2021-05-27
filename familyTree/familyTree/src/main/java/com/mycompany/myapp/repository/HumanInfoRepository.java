package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.HumanInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HumanInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HumanInfoRepository extends JpaRepository<HumanInfo, Long> {}
