package com.example.demo.Repository;

import com.example.demo.Entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {
    Optional<KhachHang> findBySdt(String sdt);
}
