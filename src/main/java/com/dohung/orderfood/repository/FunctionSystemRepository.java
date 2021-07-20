package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.FunctionSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionSystemRepository extends JpaRepository<FunctionSystem, Integer> {}
