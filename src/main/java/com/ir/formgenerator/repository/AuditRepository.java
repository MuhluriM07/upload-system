package com.ir.formgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ir.formgenerator.model.*;

public interface AuditRepository extends JpaRepository<AuditLog, Long> {
}

