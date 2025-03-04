package com.java.project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "hoa_don_phuong_thuc_thanh_toan")
public class HoaDonPhuongThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hoa_don_id")
    private HoaDon hoaDon;

    @Column(name = "so_tien_thanh_thoan", precision = 10, scale = 2)
    private BigDecimal soTienThanhThoan;

    @ColumnDefault("CONVERT([date],getdate())")
    @Column(name = "ngay_thuc_hien_thanh_toan")
    private LocalDate ngayThucHienThanhToan;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ghi_chu")
    private String ghiChu;

}