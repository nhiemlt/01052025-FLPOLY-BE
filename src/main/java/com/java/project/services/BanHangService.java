package com.java.project.services;

import com.java.project.entities.*;
import com.java.project.helper.HoaDonHelper;
import com.java.project.repositories.*;
import com.java.project.request.HoaDonChiTietRequest;
import com.java.project.request.HoaDonRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BanHangService {
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    KhachHangRepository khachHangRepository;
    @Autowired
    NhanVienRepository nhanVienRepository;
    @Autowired
    PhieuGiamGiaRepository phieuGiamGiaRepository;
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    public HoaDon createHoaDon(HoaDonRequest hoaDonRequest){
        KhachHang khachHang = hoaDonRequest.getIdKhachHang() != null
                ? khachHangRepository.findById(hoaDonRequest.getIdKhachHang())
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy khách hàng với id "+ hoaDonRequest.getIdKhachHang()))
                :null;

        NhanVien nhanVien = hoaDonRequest.getIdNhanVien() != null
                ? nhanVienRepository.findById(hoaDonRequest.getIdNhanVien())
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy nhân viên với id "+ hoaDonRequest.getIdNhanVien()))
                :null;

        PhieuGiamGia phieuGiamGia = hoaDonRequest.getIdPhieuGiamGia() != null
                ? phieuGiamGiaRepository.findById(hoaDonRequest.getIdPhieuGiamGia())
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy phiếu giảm giá với id "+ hoaDonRequest.getIdPhieuGiamGia()))
                :null;

        HoaDon hoaDon = new HoaDon();
        BeanUtils.copyProperties(hoaDonRequest, hoaDon);
        hoaDon.setKhachHang(khachHang);
        hoaDon.setNhanVien(nhanVien);
        hoaDon.setPhieuGiamGia(phieuGiamGia);
        hoaDon.setMaHoaDon(HoaDonHelper.createHoaDonHelper());
        hoaDon.setLoaiDon(1);
        hoaDon.setTrangThaiGiaoHang(8);
        hoaDon.setNgayTao(LocalDateTime.now());
        hoaDon.setTrangThai(0);

        return hoaDonRepository.save(hoaDon);
    }

    public HoaDon updateKHOfHoaDon(Integer idHD, Integer idKH){
        KhachHang khachHang = khachHangRepository.findById(idKH)
                .orElseThrow(()->new EntityNotFoundException("Không tìm thấy khách hàng với Id: " + idKH));
        HoaDon hoaDon = hoaDonRepository.findById(idHD).get();
        hoaDon.setKhachHang(khachHang);
        return hoaDonRepository.save(hoaDon);
    }

    public HoaDon upDateTrangThaiHoaDon(Integer idHD){
        HoaDon hoaDon = hoaDonRepository.findById(idHD).get();
        hoaDon.setTrangThai(1);
        hoaDon.setTrangThaiGiaoHang(9);
        return hoaDonRepository.save(hoaDon);
    }

    public HoaDonChiTiet AddOrUpdateHoaDonChiTiet(HoaDonChiTietRequest hdctRequest){
        HoaDon hoaDon = hdctRequest.getIdHoaDon() != null
                ?hoaDonRepository.findById(hdctRequest.getIdHoaDon())
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy hóa đơn với id:"+ hdctRequest.getIdHoaDon()))
                : null;
        SanPhamChiTiet sanPhamChiTiet = hdctRequest.getIdSPCT() != null
                ?sanPhamChiTietRepository.findById(hdctRequest.getIdSPCT())
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy sản phẩm chi tiết với id:"+ hdctRequest.getIdSPCT()))
                :null;
        HoaDonChiTiet hoaDonChiTiet ;

        Optional<HoaDonChiTiet>getIsPresent = hoaDonChiTietRepository
                .findByHoaDon_IdAndSanPhamChiTiet_Id(hdctRequest.getIdHoaDon(),hdctRequest.getIdSPCT());

        if(getIsPresent.isPresent()){
            hoaDonChiTiet = getIsPresent.get();
            hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + hdctRequest.getSoLuong());
        }else {
            hoaDonChiTiet = new HoaDonChiTiet();
            BeanUtils.copyProperties(hdctRequest, hoaDonChiTiet);
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTiet.setTrangThai(1);
        }

        if(hdctRequest.getSoLuong() != null && sanPhamChiTiet != null){
            BigDecimal donGia = sanPhamChiTiet.getDonGia();
            BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(hoaDonChiTiet.getSoLuong()));

            hoaDonChiTiet.setThanhTien(thanhTien.doubleValue());
        }else {
            throw  new IllegalArgumentException("Số lượng sản phẩm không hợp lệ");
        }
        updateSoLuongSPCT(sanPhamChiTiet.getId(),hdctRequest.getSoLuong());
        return hoaDonChiTietRepository.save(hoaDonChiTiet);
    }

    public void updateSoLuongSPCT(Integer idSPCT, Integer soLuongMua){
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(idSPCT)
                    .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy idSPct"));
            Integer soLuongCu = sanPhamChiTiet.getSoLuong();
            Integer soLuongCon = soLuongMua != null ? soLuongCu - soLuongMua : soLuongCu;

            if(soLuongCon <= 0){
                sanPhamChiTiet.setTrangThai(false);
            }else {
                sanPhamChiTiet.setTrangThai(true);
            }

            sanPhamChiTiet.setSoLuong(soLuongCon);

            sanPhamChiTietRepository.save(sanPhamChiTiet);
    }

    public void updateTongTienHoaDon(Integer idHoaDon){
        List<HoaDonChiTiet>listHDCTByIdHD = hoaDonChiTietRepository.findByHoaDon_Id(idHoaDon);

        BigDecimal tongTien = listHDCTByIdHD.stream()
                .map(hdct -> BigDecimal.valueOf(hdct.getThanhTien()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        HoaDon hoaDon =  hoaDonRepository.findById(idHoaDon).orElseThrow();
        hoaDon.setTongTien(tongTien.doubleValue());
        hoaDonRepository.save(hoaDon);
    }

    public HoaDonChiTiet updateQuatityHDCT(Integer id, Integer newQuantity){
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id).
                orElseThrow(()->new EntityNotFoundException("Không tìm thấy hóa đơn chi tiết với id " + id));

        if(hoaDonChiTiet.getSoLuong() != newQuantity){
            Integer khoangSoLuongThayDoi = Math.abs(hoaDonChiTiet.getSoLuong() - newQuantity);
            if(hoaDonChiTiet.getSoLuong() > newQuantity){
                updateSoLuongSPCT(hoaDonChiTiet.getSanPhamChiTiet().getId(), - khoangSoLuongThayDoi);
            }else {
                updateSoLuongSPCT(hoaDonChiTiet.getSanPhamChiTiet().getId(), khoangSoLuongThayDoi);
            }

            hoaDonChiTiet.setSoLuong(newQuantity);
            BigDecimal donGia = hoaDonChiTiet.getSanPhamChiTiet().getDonGia();
            BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(newQuantity));

            hoaDonChiTiet.setThanhTien(thanhTien.doubleValue());
            updateTongTienHoaDon(hoaDonChiTiet.getHoaDon().getId());

            return hoaDonChiTietRepository.save(hoaDonChiTiet);
        }

        return hoaDonChiTiet;
    }
    @Transactional
    public void deleteHoaDonChiTiet(Integer id){
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                        .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy hóa đơn chi tiết với id: "+ id));

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(hoaDonChiTiet.getSanPhamChiTiet().getId())
                .orElseThrow(()->new EntityNotFoundException("Không thể tìm thấy spct"));
        Integer soLuongTraVe = hoaDonChiTiet.getSoLuong();
        if(soLuongTraVe != 0){
            updateSoLuongSPCT(sanPhamChiTiet.getId(), - soLuongTraVe);
        }

        hoaDonChiTietRepository.deleteById(id);
        updateTongTienHoaDon(hoaDonChiTiet.getHoaDon().getId());
    }

    public Map<Integer, Long>getHoaDonChiTietCount(){
        List<Object[]>result = hoaDonChiTietRepository.getHoaDonChiTietCount();
        Map<Integer, Long>hoaDonChiTietMap = new HashMap<>();
        for (Object[] row : result){
            Integer hoaDonId = (Integer) row[0];
            Long soLuong = (Long) row[1];
            hoaDonChiTietMap.put(hoaDonId, soLuong);
        }
        return hoaDonChiTietMap;
    }
}
