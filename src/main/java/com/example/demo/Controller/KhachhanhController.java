package com.example.demo.Controller;

import com.example.demo.Entity.KhachHang;
import com.example.demo.Entity.MauSac;
import com.example.demo.Repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class KhachhanhController {
    @Autowired
    private KhachHangRepository khachHangRepository;

    @GetMapping("/khach-hang")
    public String getAll(Model model) {
        List<KhachHang> list = khachHangRepository.findAll();
        model.addAttribute("listKH", list);
        model.addAttribute("khachHang", new KhachHang());
        return "KhachHang";
    }

    @PostMapping("/khach-hang/save")
    public String saveDanhMuc(@ModelAttribute KhachHang khachHang, RedirectAttributes redirectAttributes) {
        Optional<KhachHang> existingDanhMuc = khachHangRepository.findById(khachHang.getId());

        if (existingDanhMuc.isPresent()) {
            // Update existing record
            KhachHang updatedDanhMuc = existingDanhMuc.get();
            updatedDanhMuc.setHoTen(khachHang.getHoTen());
            updatedDanhMuc.setDiaChi(khachHang.getDiaChi());
            updatedDanhMuc.setSdt(khachHang.getSdt());
            updatedDanhMuc.setTrangThai(khachHang.getTrangThai());
            updatedDanhMuc.setNgaySua(new Date());
            khachHangRepository.save(updatedDanhMuc);
        } else {
            // Create new record
            khachHang.setNgayTao(new Date());
            khachHang.setNgaySua(new Date());
            khachHangRepository.save(khachHang);
        }

        redirectAttributes.addFlashAttribute("message", "Lưu thành công");
        return "redirect:/khach-hang";
    }

    @GetMapping("/khach-hang/delete/{id}")
    public String deleteDanhMuc(@PathVariable int id, RedirectAttributes redirectAttributes) {
        khachHangRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công");
        return "redirect:/khach-hang";
    }

    @GetMapping("/khach-hang/edit/{id}")
    public String editDanhMuc(@PathVariable int id, Model model) {
        KhachHang khachHang = khachHangRepository.findById(id).orElse(new KhachHang());
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("listKH", khachHangRepository.findAll());
        return "KhachHang";
    }
}
