package com.disl.starter.features.cms.repository;

import com.disl.starter.features.cms.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    Page findTopByTag(String tag);

    boolean existsByTag(String tag);

    boolean existsByTagAndIdNot(String tag, long id);
}
