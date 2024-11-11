package com.example.demo.Controller;

import com.example.demo.Entity.MauSac;
import com.example.demo.Entity.Size;
import com.example.demo.Repository.KichThuocRepository;
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
public class SizeController {
    @Autowired
    private KichThuocRepository kichThuocRepository;
    @GetMapping("/size")
    public String getAll(Model model) {
        List<Size> list = kichThuocRepository.findAll();
        model.addAttribute("listSize", list);
        model.addAttribute("size", new Size());
        return "Size";
    }

    @PostMapping("/size/save")
    public String saveDanhMuc(@ModelAttribute Size size, RedirectAttributes redirectAttributes) {
        Optional<Size> existingDanhMuc = kichThuocRepository.findById(size.getId());

        if (existingDanhMuc.isPresent()) {

            Size updatedDanhMuc = existingDanhMuc.get();
            updatedDanhMuc.setTenSize(size.getTenSize());
            updatedDanhMuc.setMaSize(size.getMaSize());
            updatedDanhMuc.setTrangThai(size.getTrangThai());
            updatedDanhMuc.setNgaySua(new Date());
            kichThuocRepository.save(updatedDanhMuc);
        } else {
            // Create new record
            size.setNgayTao(new Date());
            size.setNgaySua(new Date());
            kichThuocRepository.save(size);
        }

        redirectAttributes.addFlashAttribute("message", "Lưu thành công");
        return "redirect:/size";
    }

    @GetMapping("/size/delete/{id}")
    public String deleteDanhMuc(@PathVariable int id, RedirectAttributes redirectAttributes) {
        kichThuocRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công");
        return "redirect:/size";
    }

    @GetMapping("/size/edit/{id}")
    public String editDanhMuc(@PathVariable int id, Model model) {
        Size size = kichThuocRepository.findById(id).orElse(new Size());
        model.addAttribute("size", size);
        model.addAttribute("listSize", kichThuocRepository.findAll());
        return "Size";
    }
}
