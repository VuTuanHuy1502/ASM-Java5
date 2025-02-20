package com.example.demo.Repository;

import com.example.demo.Entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon,Integer> {
    @Query(value = "SELECT * from hoa_don where trang_thai='Cho thanh toan'", nativeQuery = true)
    List<HoaDon> getList();
}
