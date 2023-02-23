package com.disl.starter.features.cms.repository;

import com.disl.starter.features.cms.entity.Section;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findAllByPageId(Long pageId);

    List<Section> findAllByPageIdOrderByCreationDateDesc(Long pageId);

    Section findTopByIdAndPageId(Long id, Long pageId);

    @Transactional
    void deleteAllByPageId(Long pageId);
}
