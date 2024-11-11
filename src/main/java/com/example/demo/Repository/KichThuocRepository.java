package com.example.demo.Repository;

import com.example.demo.Entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KichThuocRepository extends JpaRepository<Size,Integer> {
}
