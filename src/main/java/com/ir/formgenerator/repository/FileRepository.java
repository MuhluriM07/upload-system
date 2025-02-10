package com.ir.formgenerator.repository;

import com.ir.formgenerator.model.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileRecord, Long> {
}
