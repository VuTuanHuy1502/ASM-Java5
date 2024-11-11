package com.example.demo.Controller;

import com.example.demo.Entity.*;
import com.example.demo.Repository.CTSPRepository;
import com.example.demo.Repository.KichThuocRepository;
import com.example.demo.Repository.MauSacRepository;
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


public class CTSPController {
    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private CTSPRepository ctspRepository;
    @Autowired
    private MauSacRepository mauSacRepository;
    @Autowired
    private KichThuocRepository kichThuocRepository;

    @GetMapping("/ctsp")
    public String getAllSP(Model model) {
        List<CTSP> listCTSP = ctspRepository.findAll();
        model.addAttribute("listCTSP", listCTSP);
        model.addAttribute("CTSP", new CTSP());
        List<SanPham> listSP = sanPhamRepository.findAll();
        model.addAttribute("listSP", listSP);
        List<MauSac> listMS = mauSacRepository.findAll();
        model.addAttribute("listMS", listMS);
        List<Size> listSize = kichThuocRepository.findAll();
        model.addAttribute("listSize", listSize);
        return "TaoCTSP";
    }



    @PostMapping("/ctsp/save")
    public String saveSanPham(@ModelAttribute CTSP ctsp, RedirectAttributes redirectAttributes) {
        if (ctsp.getId() != null && ctsp.getId() > 0) {
            // Nếu id tồn tại, thực hiện cập nhật.
            Optional<CTSP> existingCTSP = ctspRepository.findById(ctsp.getId());
            if (existingCTSP.isPresent()) {
                CTSP updateCTSP = existingCTSP.get();
                updateCTSP.setSanPham(ctsp.getSanPham());
                updateCTSP.setMauSac(ctsp.getMauSac());
                updateCTSP.setSize(ctsp.getSize());
                updateCTSP.setGiaBan(ctsp.getGiaBan());
                updateCTSP.setSoLuongTon(ctsp.getSoLuongTon());
                updateCTSP.setTrangThai(ctsp.getTrangThai());
                updateCTSP.setNgaySua(new Date());
                ctspRepository.save(updateCTSP);
            }
        } else {
            // Nếu không có id, thực hiện thêm mới.
            ctsp.setNgayTao(new Date());
            ctsp.setNgaySua(new Date());
            ctspRepository.save(ctsp);
        }

        redirectAttributes.addFlashAttribute("message", "Lưu CTSP thành công");
        return "redirect:/ctsp";
    }

    @GetMapping("/ctsp/delete/{id}")
    public String deleteSP(@PathVariable int id, RedirectAttributes redirectAttributes) {
        ctspRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công");
        return "redirect:/ctsp";
    }

    @GetMapping("/ctsp/edit/{id}")
    public String editSP(@PathVariable int id, Model model) {

        CTSP ctsp = ctspRepository.findById(id).orElse(new CTSP());
        model.addAttribute("CTSP", ctsp);

        List<SanPham> listSP = sanPhamRepository.findAll();
        model.addAttribute("listSP", listSP);

        List<MauSac> listMS = mauSacRepository.findAll();
        model.addAttribute("listMS", listMS);

        List<Size> listSize = kichThuocRepository.findAll();
        model.addAttribute("listSize", listSize);
        model.addAttribute("listCTSP", ctspRepository.findAll());

        return "TaoCTSP";
    }

}
