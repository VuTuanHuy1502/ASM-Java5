package com.example.demo.Controller;


import com.example.demo.Entity.HDCT;
import com.example.demo.Entity.HoaDon;
import com.example.demo.Repository.*;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HoaDonController {
    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private CTSPRepository ctspRepository;
    @Autowired
    private MauSacRepository mauSacRepository;
    @Autowired
    private KichThuocRepository kichThuocRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private HDCTRepository hdctRepository;

    @GetMapping("/hoa-don")
    public String getAll(Model model) {
        List<HoaDon> hoaDonList = hoaDonRepository.findAll();

        // Tạo một Map để lưu tổng tiền của từng hóa đơn theo ID
        Map<Integer, Double> totalAmountMap = new HashMap<>();
        for (HoaDon hoaDon : hoaDonList) {
            double totalAmount = 0.0;
            // Tính tổng tiền của tất cả các HDCT trong hóa đơn
            for (HDCT hdct : hoaDon.getHoaDonChiTiet()) {
                totalAmount += hdct.getTongTien();
            }
            totalAmountMap.put(hoaDon.getId(), totalAmount);
        }

        model.addAttribute("hoaDonList", hoaDonList);
        model.addAttribute("totalAmountMap", totalAmountMap);

        return "HoaDon";
    }
    @PostMapping("/hoa-don/in-hoa-don/{id}")
    public ResponseEntity<byte[]> printInvoice(@PathVariable("id") Integer id) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + id));

        // Tạo nội dung file PDF từ thông tin hóa đơn
        byte[] pdfData = createInvoicePDF(hoaDon);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "HoaDon_" + id + ".pdf");

        return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
    }

    public byte[] createInvoicePDF(HoaDon hoaDon) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            document.setFontSize(12);

            // Tiêu đề
            Paragraph title = new Paragraph("HÓA DON BÁN HÀNG")
                    .setBold()
                    .setFontSize(18);

            document.add(title);

            document.add(new Paragraph("Ma hoa don:" + hoaDon.getId()));
            document.add(new Paragraph("Ten khach hang: " + hoaDon.getKhachHang().getHoTen()));
            document.add(new Paragraph("Dia chi: " + hoaDon.getDiaChi()));
            document.add(new Paragraph("So dien thoai: " + hoaDon.getSoDienThoai()));
            document.add(new Paragraph("Trang thai: " + hoaDon.getTrangThai()));
            document.add(new Paragraph("Ngay tao: " + hoaDon.getNgayTao()));
            document.add(new Paragraph("Ngay sua: " + hoaDon.getNgaySua()));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Chi tiet san pham:"));

            for (HDCT hdct : hoaDon.getHoaDonChiTiet()) {
                document.add(new Paragraph("San pham: " + hdct.getCtsp().getSanPham().getTenSanPham()));
                document.add(new Paragraph("So luong: " + hdct.getSoLuongMua()));
                document.add(new Paragraph("Don gia: " + hdct.getGiaBan()));
                document.add(new Paragraph(" "));
            }

            // Tổng cộng
            double tongTien = hoaDon.getHoaDonChiTiet().stream()
                    .mapToDouble(HDCT::getTongTien)
                    .sum();
            document.add(new Paragraph("Tong Tien: " + String.format("%.2f", tongTien))
                    .setBold()
                    .setMarginTop(20));

            document.add(new Paragraph("Xin cam on quy khach da mua hang!")
                    .setItalic()
                    .setMarginTop(20));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}
