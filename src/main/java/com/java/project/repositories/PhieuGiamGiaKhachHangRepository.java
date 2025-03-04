package com.java.project.repositories;

import com.java.project.entities.PhieuGiamGiaKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhieuGiamGiaKhachHangRepository extends JpaRepository<PhieuGiamGiaKhachHang, Integer> {
    @Query("SELECT p FROM PhieuGiamGiaKhachHang p WHERE p.idKhachHang.id = :khachHangId")
    List<PhieuGiamGiaKhachHang> findByKhachHangId(Integer khachHangId);

    @Query("SELECT p FROM PhieuGiamGiaKhachHang p WHERE p.idVoucher.id = :phieuGiamGiaId")
    List<PhieuGiamGiaKhachHang> findByVoucherId(Integer phieuGiamGiaId);
}