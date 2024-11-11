package com.example.demo.Controller;

import com.example.demo.Entity.DanhMuc;
import com.example.demo.Entity.MauSac;
import com.example.demo.Repository.MauSacRepository;
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
public class MauSacController {
    @Autowired
    private MauSacRepository mauSacRepository;

    @GetMapping("/mau-sac")
    public String getAll(Model model) {
        List<MauSac> list = mauSacRepository.findAll();
        model.addAttribute("listMS", list);
        model.addAttribute("mauSac", new MauSac());
        return "MauSac";
    }

    @PostMapping("/mau-sac/save")
    public String saveDanhMuc(@ModelAttribute MauSac mauSac, RedirectAttributes redirectAttributes) {
        Optional<MauSac> existingDanhMuc = mauSacRepository.findById(mauSac.getId());

        if (existingDanhMuc.isPresent()) {
            // Update existing record
            MauSac updatedDanhMuc = existingDanhMuc.get();
            updatedDanhMuc.setTenMau(mauSac.getTenMau());
            updatedDanhMuc.setMaMau(mauSac.getMaMau());
            updatedDanhMuc.setTrangThai(mauSac.getTrangThai());
            updatedDanhMuc.setNgaySua(new Date());
            mauSacRepository.save(updatedDanhMuc);
        } else {
            // Create new record
            mauSac.setNgayTao(new Date());
            mauSac.setNgaySua(new Date());
            mauSacRepository.save(mauSac);
        }

        redirectAttributes.addFlashAttribute("message", "Lưu thành công");
        return "redirect:/mau-sac";
    }

    @GetMapping("/mau-sac/delete/{id}")
    public String deleteDanhMuc(@PathVariable int id, RedirectAttributes redirectAttributes) {
        mauSacRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công");
        return "redirect:/mau-sac";
    }

    @GetMapping("/mau-sac/edit/{id}")
    public String editDanhMuc(@PathVariable int id, Model model) {
        MauSac mauSac = mauSacRepository.findById(id).orElse(new MauSac());
        model.addAttribute("mauSac", mauSac);
        model.addAttribute("listMS", mauSacRepository.findAll());
        return "MauSac";
    }
}
