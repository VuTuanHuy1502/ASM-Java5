package com.example.demo.Repository;

import com.example.demo.Entity.CTSP;
import com.example.demo.Entity.HDCT;
import com.example.demo.Entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HDCTRepository extends JpaRepository<HDCT,Integer> {
    @Query("SELECT h FROM HDCT h WHERE h.hoaDon.id = :idhd")
    List<HDCT> getL(Integer idhd);
    List<HDCT> findByHoaDon(HoaDon hoaDon);
}
