package com.example.demo.Controller;

import com.example.demo.Entity.DanhMuc;
import com.example.demo.Entity.SanPham;
import com.example.demo.Repository.DanhMucRepository;
import com.example.demo.Repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class DanhMucController {
    @Autowired
    private DanhMucRepository danhMucRepository;
    @Autowired
    private SanPhamRepository sanPhamRepository;

    @GetMapping("/danh-muc")
    public String getAll(Model model) {
        List<DanhMuc> list = danhMucRepository.findAll();
        model.addAttribute("listDM", list);
        model.addAttribute("danhMuc", new DanhMuc());
        return "DanhMucSanPham";
    }

    @PostMapping("/danh-muc/save")
    public String saveDanhMuc(@ModelAttribute DanhMuc danhMuc, RedirectAttributes redirectAttributes) {
        Optional<DanhMuc> existingDanhMuc = danhMucRepository.findById(danhMuc.getId());

        if (existingDanhMuc.isPresent()) {
            // Update existing record
            DanhMuc updatedDanhMuc = existingDanhMuc.get();
            updatedDanhMuc.setTenDanhMuc(danhMuc.getTenDanhMuc());
            updatedDanhMuc.setMaDanhMuc(danhMuc.getMaDanhMuc());
            updatedDanhMuc.setTrangThai(danhMuc.getTrangThai());
            updatedDanhMuc.setNgaySua(new Date());
            danhMucRepository.save(updatedDanhMuc);
        } else {
            // Create new record
            danhMuc.setNgayTao(new Date());
            danhMuc.setNgaySua(new Date());
            danhMucRepository.save(danhMuc);
        }

        redirectAttributes.addFlashAttribute("message", "Lưu danh mục thành công");
        return "redirect:/danh-muc";
    }

    @GetMapping("/danh-muc/delete/{id}")
    public String deleteDanhMuc(@PathVariable int id, RedirectAttributes redirectAttributes) {
        danhMucRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa danh mục thành công");
        return "redirect:/danh-muc";
    }

    @GetMapping("/danh-muc/edit/{id}")
    public String editDanhMuc(@PathVariable int id, Model model) {
        DanhMuc danhMuc = danhMucRepository.findById(id).orElse(new DanhMuc());
        model.addAttribute("danhMuc", danhMuc);
        model.addAttribute("listDM", danhMucRepository.findAll());
        return "DanhMucSanPham";
    }


}
