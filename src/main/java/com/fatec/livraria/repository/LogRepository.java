package com.fatec.livraria.repository;

import com.fatec.livraria.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {
}
