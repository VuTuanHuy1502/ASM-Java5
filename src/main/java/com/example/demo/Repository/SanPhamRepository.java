package com.example.demo.Repository;

import com.example.demo.Entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SanPhamRepository extends JpaRepository <SanPham,Integer > {
}
