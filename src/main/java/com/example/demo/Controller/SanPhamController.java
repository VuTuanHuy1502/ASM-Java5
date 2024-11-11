package com.example.demo.Controller;

import com.example.demo.Entity.DanhMuc;
import com.example.demo.Entity.SanPham;
import com.example.demo.Repository.DanhMucRepository;
import com.example.demo.Repository.SanPhamRepository;
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
public class SanPhamController {
    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private DanhMucRepository danhMucRepository;

    @GetMapping("/san-pham")
    public String getAllSP(Model model) {
        List<DanhMuc> listDM1 = danhMucRepository.findAll();
        model.addAttribute("listDM1", listDM1);
        model.addAttribute("danhMuc", new DanhMuc());
        List<SanPham> listSP = sanPhamRepository.findAll();
        model.addAttribute("listSP",listSP);
        return "TaoSanPham";
    }
    @PostMapping("/san-pham/save")
    public String saveSanPham(@ModelAttribute SanPham sanPham, RedirectAttributes redirectAttributes) {
        if (sanPham.getId() == null || sanPham.getId() == 0) {
            // Xử lý trường hợp sản phẩm mới
            sanPham.setNgayTao(new Date());
            sanPham.setNgaySua(new Date());
            sanPhamRepository.save(sanPham);
        } else {
            // Xử lý trường hợp cập nhật sản phẩm
            Optional<SanPham> existingSP = sanPhamRepository.findById(sanPham.getId());
            if (existingSP.isPresent()) {
                SanPham updateSP = existingSP.get();
                updateSP.setTenSanPham(sanPham.getTenSanPham());
                updateSP.setMaSanPham(sanPham.getMaSanPham());
                updateSP.setDanhMuc(sanPham.getDanhMuc());
                updateSP.setTrangThai(sanPham.getTrangThai());
                updateSP.setNgaySua(new Date());
                sanPhamRepository.save(updateSP);
            }
        }

        redirectAttributes.addFlashAttribute("message", "Lưu sản phẩm thành công");
        return "redirect:/san-pham";
    }

    @GetMapping("/san-pham/delete/{id}")
    public String deleteSP(@PathVariable int id, RedirectAttributes redirectAttributes) {
        sanPhamRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công");
        return "redirect:/san-pham";
    }

    @GetMapping("/san-pham/edit/{id}")
    public String editSP(@PathVariable int id, Model model) {

        SanPham sanPham = sanPhamRepository.findById(id).orElse(new SanPham());
        model.addAttribute("sanPham", sanPham);


        List<DanhMuc> listDM1 = danhMucRepository.findAll();
        model.addAttribute("listDM1", listDM1);

        model.addAttribute("listSP", sanPhamRepository.findAll());

        return "TaoSanPham";
    }

}
