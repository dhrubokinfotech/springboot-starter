package com.disl.starter.features.db_file.repository;

import com.disl.starter.features.db_file.entity.DbFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbFileRepository extends JpaRepository<DbFile, Long> {

}
