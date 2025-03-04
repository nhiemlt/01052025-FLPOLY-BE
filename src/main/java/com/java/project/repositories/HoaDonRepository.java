package com.java.project.repositories;

import com.java.project.dtos.HoaDonBanHangResponse;
import com.java.project.dtos.HoaDonHomNayResponse;
import com.java.project.dtos.HoaDonResponse;
import com.java.project.entities.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    @Query("""
        select new com.java.project.dtos.HoaDonResponse(
        hd.id,
        hd.khachHang.tenKhachHang,
        hd.nhanVien.maNhanVien,
        hd.maHoaDon,
        hd.loaiDon,
        hd.ghiChu,
        hd.hoTenNguoiNhan,
        hd.soDienThoai,
        hd.email,
        hd.diaChiNhanHang,
        hd.ngayNhanMongMuon,
        hd.ngayDuKienNhan,
        hd.trangThaiGiaoHang,
        hd.phiShip,
        hd.tongTien,
        hd.ngayTao,
        hd.trangThai
        )

        from HoaDon hd left join KhachHang kh on kh.id = hd.khachHang.id
        where hd.ngayTao >= :startDate AND hd.ngayTao < :endDate
                and (:loaiDon IS NULL OR hd.loaiDon = :loaiDon)
        order by hd.ngayTao ASC 
""")
    List<HoaDonResponse> getAll(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("loaiDon")Integer loaiDon);

    @Query("""
                    select new com.java.project.dtos.HoaDonResponse(
                hd.id,
                hd.khachHang.tenKhachHang,
                hd.nhanVien.maNhanVien,
                hd.maHoaDon,
                hd.loaiDon,
                hd.ghiChu,
                hd.hoTenNguoiNhan,
                hd.soDienThoai,
                hd.email,
                hd.diaChiNhanHang,
                hd.ngayNhanMongMuon,
                hd.ngayDuKienNhan,
                hd.trangThaiGiaoHang,
                hd.phiShip,
                hd.tongTien,
                hd.ngayTao,
                hd.trangThai
                    ) 
                from HoaDon hd left join KhachHang kh on kh.id = hd.khachHang.id
                where CAST(hd.ngayTao AS DATE ) = CURRENT_DATE 
                and (:loaiDon IS NULL OR hd.loaiDon = :loaiDon)
                order by hd.ngayTao ASC 
                    """)
    Page<HoaDonResponse>getPhanTrang(Pageable pageable,
                                     @Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate,
                                     @Param("loaiDon") Integer loaiDon);

    @Query("""
                    select new com.java.project.dtos.HoaDonResponse(
                hd.id,
                 hd.khachHang.tenKhachHang,
                hd.nhanVien.maNhanVien,
                hd.maHoaDon,
                hd.loaiDon,
                hd.ghiChu,
                hd.hoTenNguoiNhan,
                hd.soDienThoai,
                hd.email,
                hd.diaChiNhanHang,
                hd.ngayNhanMongMuon,
                hd.ngayDuKienNhan,
                hd.trangThaiGiaoHang,
                hd.phiShip,
                hd.tongTien,
                hd.ngayTao,
                hd.trangThai
                    ) 
                from HoaDon hd left join KhachHang kh on kh.id = hd.khachHang.id
                where (:trangThaiGiaoHang IS NULL OR hd.trangThaiGiaoHang = :trangThaiGiaoHang)
                and (:keyword IS NULL OR LOWER(hd.maHoaDon) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(kh.tenKhachHang) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(hd.nhanVien.maNhanVien) LIKE LOWER(CONCAT('%', :keyword, '%')))
                and (:ngayBatDau IS NULL OR CAST(hd.ngayTao AS DATE ) >=  :ngayBatDau)
                and (:ngayKetThuc IS NULL OR  CAST(hd.ngayTao AS DATE ) <=  :ngayKetThuc)
                and (:loaiDon IS NULL OR hd.loaiDon = :loaiDon)
                order by hd.ngayTao ASC 
                    """)
    Page<HoaDonResponse>getPhanTrangSearch(Pageable pageable,
                                  @Param("trangThaiGiaoHang") Integer trangThaiGiaoHang,
                                  @Param("keyword") String keyword,
                                  @Param("ngayBatDau") LocalDate ngayBatDau,
                                  @Param("ngayKetThuc") LocalDate ngayKetThuc,
                                  @Param("loaiDon") Integer loaiDon);

    @Query("""
                    select new com.java.project.dtos.HoaDonResponse(
                hd.id,
                hd.khachHang.tenKhachHang,
                hd.nhanVien.maNhanVien,
                hd.maHoaDon,
                hd.loaiDon,
                hd.ghiChu,
                hd.hoTenNguoiNhan,
                hd.soDienThoai,
                hd.email,
                hd.diaChiNhanHang,
                hd.ngayNhanMongMuon,
                hd.ngayDuKienNhan,
                hd.trangThaiGiaoHang,
                hd.phiShip,
                hd.tongTien,
                hd.ngayTao,
                hd.trangThai
                    ) 
                from HoaDon hd
                where (:trangThaiGiaoHang IS NULL OR hd.trangThaiGiaoHang = :trangThaiGiaoHang)
                and (:keyword IS NULL OR LOWER(hd.maHoaDon) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(hd.khachHang.tenKhachHang) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(hd.nhanVien.maNhanVien) LIKE LOWER(CONCAT('%', :keyword, '%')))
                and (:ngayBatDau IS NULL OR CAST(hd.ngayTao AS DATE ) >=  :ngayBatDau)
                and (:ngayKetThuc IS NULL OR CAST(hd.ngayTao AS DATE ) <=  :ngayKetThuc)
                and (:loaiDon IS NULL OR hd.loaiDon = :loaiDon)
                order by hd.ngayTao ASC 
                    """)
    List<HoaDonResponse>getSearchAll(
                                    @Param("trangThaiGiaoHang") Integer trangThaiGiaoHang,
                                    @Param("keyword") String keyword,
                                    @Param("ngayBatDau") LocalDate ngayBatDau,
                                    @Param("ngayKetThuc") LocalDate ngayKetThuc,
                                    @Param("loaiDon") Integer loaiDon);

    @Query("""
    SELECT hd.trangThaiGiaoHang, COUNT(hd) 
    FROM HoaDon hd 
    WHERE
    (:ngayBatDau IS NULL OR CAST(hd.ngayTao AS DATE ) >=  :ngayBatDau)
    and (:ngayKetThuc IS NULL OR CAST(hd.ngayTao AS DATE ) <=  :ngayKetThuc)
    AND (:loaiDon IS NULL OR hd.loaiDon = :loaiDon)
    GROUP BY hd.trangThaiGiaoHang
""")
    List<Object[]> countOrdersByStatus(
            @Param("ngayBatDau") LocalDate ngayBatDau,
            @Param("ngayKetThuc") LocalDate ngayKetThuc,
            @Param("loaiDon") Integer loaiDon
    );

    @Query("""
            select new com.java.project.dtos.HoaDonHomNayResponse(
            hd.id,
            kh.id,
            pgg.id,
            hd.maHoaDon,
            hd.ngayTao,
            kh.tenKhachHang,
            kh.ngaySinh,
            kh.email,
            kh.soDienThoai,
            pgg.maPhieuGiamGia,
            pgg.loaiGiam,
            pgg.giaTriGiam,
            hd.tongTien
            )from HoaDon hd left join KhachHang kh on hd.khachHang.id = kh.id
            left join PhieuGiamGia pgg on hd.phieuGiamGia.id = pgg.id
            where hd.ngayTao >= :start and hd.ngayTao <= :end 
            and hd.trangThaiGiaoHang =8
            """)
    List<HoaDonHomNayResponse>getHoaDonHomNay(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);

    @Query("""
                    select new com.java.project.dtos.HoaDonBanHangResponse(
                hd.id,
                hd.khachHang.tenKhachHang,
                hd.nhanVien.maNhanVien,
                hd.maHoaDon,
                pgg.loaiGiam,
                pgg.giaTriGiam,
                hd.loaiDon,
                hd.ghiChu,
                hd.hoTenNguoiNhan,
                hd.soDienThoai,
                hd.email,
                hd.diaChiNhanHang,
                hd.ngayNhanMongMuon,
                hd.ngayDuKienNhan,
                hd.trangThaiGiaoHang,
                hd.phiShip,
                hd.tongTien,
                hd.ngayTao,
                hd.trangThai
                ) 
                from HoaDon hd left join KhachHang kh on hd.khachHang.id = kh.id
                left join PhieuGiamGia pgg on hd.phieuGiamGia.id = pgg.id
                where hd.maHoaDon = :maHoaDon
                """)
    Optional<HoaDonBanHangResponse>getHoaDonByMaHoaDon(@Param("maHoaDon") String maHoaDon);
}


