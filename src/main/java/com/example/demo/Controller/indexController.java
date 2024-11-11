package com.example.demo.Controller;

import com.example.demo.Entity.*;
import com.example.demo.Repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Controller
public class indexController {
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




    private double tinhTongTienGioHang(Integer hoaDonId) {
        double tongTien = 0;
        List<HDCT> listHDCT = hdctRepository.getL(hoaDonId);

        for (HDCT h : listHDCT) {
            tongTien += h.getTongTien();
        }

        return tongTien;
    }


    @GetMapping("/ban-hang")
    public String BanHang(Model model) {
        model.addAttribute("listCTSP", ctspRepository.findAll());
        model.addAttribute("listHD", hoaDonRepository.getList());

        return "BanHang";
    }


    // Khi tạo hóa đơn, lưu vào session


    // Khi xem hóa đơn, lưu vào session
    @GetMapping("/ban-hang/xem-hoa-don/{id}")
    public String XemHoaDon(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(id);

        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            model.addAttribute("hoaDon", hoaDon);

            // Lưu hóa đơn vào session
            session.setAttribute("hoaDon", hoaDon);

            // Tính tổng tiền và lưu vào session
            double tongTien = tinhTongTienGioHang(hoaDon.getId());
            session.setAttribute("tongTien", tongTien);
        } else {
            model.addAttribute("hoaDon", new HoaDon());
            session.setAttribute("tongTien", 0.0);
        }

        model.addAttribute("listCTSP", ctspRepository.findAll());
        model.addAttribute("listHDCT", hdctRepository.getL(id));
        model.addAttribute("listHD", hoaDonRepository.getList());

        return "BanHang";
    }


    @PostMapping("/ban-hang/them-vao-gio/{id}")
    public String ThemVaoGio(@PathVariable("id") Integer id,
                             @RequestParam("soLuongMua") Integer soLuongMua,
                             HttpSession session, Model model) {
        Optional<CTSP> optionalCTSP = ctspRepository.findById(id);
        if (optionalCTSP.isPresent()) {
            CTSP chiTietSanPham = optionalCTSP.get();

            // Lấy hóa đơn từ session
            HoaDon hoaDon = (HoaDon) session.getAttribute("hoaDon");

            // Kiểm tra nếu hóa đơn tồn tại và số lượng tồn đủ đáp ứng yêu cầu mua
            if (hoaDon != null && chiTietSanPham.getSoLuongTon() >= soLuongMua) {
                // Cập nhật số lượng tồn
                chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() - soLuongMua);
                ctspRepository.save(chiTietSanPham);

                boolean existsInCart = false;

                // Tìm kiếm sản phẩm trong giỏ hàng
                for (HDCT h : hdctRepository.findAll()) {
                    if (h.getCtsp().getId().equals(chiTietSanPham.getId()) && h.getHoaDon().getId().equals(hoaDon.getId())) {
                        h.setSoLuongMua(h.getSoLuongMua() + soLuongMua);
                        h.setTongTien(h.getSoLuongMua() * h.getGiaBan());
                        hdctRepository.save(h);
                        existsInCart = true;
                        break;
                    }
                }

                // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới vào giỏ
                if (!existsInCart) {
                    HDCT hoaDonChiTiet = new HDCT(null, hoaDon, chiTietSanPham, soLuongMua, chiTietSanPham.getGiaBan(), soLuongMua * chiTietSanPham.getGiaBan(), "Da thanh toan", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()));
                    hdctRepository.save(hoaDonChiTiet);
                }

                // Cập nhật tổng tiền giỏ hàng
                double tongTien = tinhTongTienGioHang(hoaDon.getId());
                session.setAttribute("tongTien", tongTien);
                session.setAttribute("listHDCT", hdctRepository.getL(hoaDon.getId()));
            }
        }

        return "redirect:/ban-hang";
    }

    @GetMapping("/ban-hang/xoa-san-pham/{id}")
    public String XoaSanPhamGioHang(@PathVariable("id") Integer id, HttpSession session) {
        Optional<HDCT> optionalHDCT = hdctRepository.findById(id);

        if (optionalHDCT.isPresent()) {
            HDCT hoaDonChiTiet = optionalHDCT.get();
            CTSP chiTietSanPham = hoaDonChiTiet.getCtsp();

            // Cộng lại số lượng mua vào số lượng tồn của sản phẩm
            chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() + hoaDonChiTiet.getSoLuongMua());
            ctspRepository.save(chiTietSanPham);

            // Xóa sản phẩm khỏi giỏ hàng
            hdctRepository.delete(hoaDonChiTiet);

            // Lấy hóa đơn từ session
            HoaDon hoaDon = (HoaDon) session.getAttribute("hoaDon");

            // Cập nhật tổng tiền giỏ hàng
            double tongTien = tinhTongTienGioHang(hoaDon.getId());
            session.setAttribute("tongTien", tongTien);

            // Cập nhật lại danh sách sản phẩm trong giỏ hàng
            session.setAttribute("listHDCT", hdctRepository.getL(hoaDon.getId()));
        }

        return "redirect:/ban-hang";
    }


    @GetMapping("/ban-hang/tim-khach-hang")
    public String TimKhachHang(@RequestParam("sdt") String sdt, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<KhachHang> khachHang = khachHangRepository.findBySdt(sdt);
        HoaDon hoaDon = new HoaDon();

        if (khachHang.isPresent()) {
            // Nếu tìm thấy khách hàng, lưu thông tin khách hàng vào session
            hoaDon.setKhachHang(khachHang.get());
            session.setAttribute("hoaDon", hoaDon);  // Lưu hóa đơn mới vào session
            session.setAttribute("khachHang", khachHang.get());  // Lưu thông tin khách hàng vào session

        } else {
            // Nếu không tìm thấy khách hàng
            session.setAttribute("hoaDon", new HoaDon());
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy khách hàng với số điện thoại này.");
        }

        // Cập nhật model với các thông tin cần thiết để hiển thị
        model.addAttribute("listCTSP", ctspRepository.findAll());
        model.addAttribute("listHDCT", hdctRepository.getL(hoaDon.getId()));
        model.addAttribute("listHD", hoaDonRepository.getList());

        return "redirect:/ban-hang";  // Chuyển hướng lại trang bán hàng
    }

    @GetMapping("/ban-hang/tao-hoa-don")
    public String TaoHoaDon(String soDT, HttpSession session, RedirectAttributes redirectAttributes) {
        // Lấy thông tin khách hàng từ session
        KhachHang khachHang = (KhachHang) session.getAttribute("khachHang");

        if (khachHang != null) {
            HoaDon hoaDon = new HoaDon();
            hoaDon.setKhachHang(khachHang);  // Gán thông tin khách hàng vào hóa đơn
            hoaDon.setTrangThai("Cho thanh toan");
            hoaDon.setNgayTao(new java.util.Date());
            hoaDon.setNgaySua(new java.util.Date());
            hoaDon.setSoDienThoai(khachHang.getSdt());
            hoaDon.setDiaChi(khachHang.getDiaChi());

            // Lưu hóa đơn vào cơ sở dữ liệu
            hoaDonRepository.save(hoaDon);

            // Lưu hóa đơn vào session
            session.setAttribute("hoaDon", hoaDon);

            // Khởi tạo tổng tiền là 0 khi mới tạo hóa đơn
            session.setAttribute("tongTien", 0.0);

        }
        redirectAttributes.addFlashAttribute("message", "Tạo hóa đơn mới thành công");

        return "redirect:/ban-hang";  // Chuyển hướng về trang bán hàng
    }
    @PostMapping("/ban-hang/thanh-toan")
    public String ThanhToan(@RequestParam("id") Integer hoaDonId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {

        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(hoaDonId);

        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();

            // Kiểm tra nếu hóa đơn không có hoặc tổng tiền bằng 0 thì không cho thanh toán
            Double tongTien = (Double) session.getAttribute("tongTien");
            if (hoaDon.getId() == 0 || tongTien == null || tongTien == 0) {
                model.addAttribute("message", "Không thể thanh toán vì hóa đơn hoặc tổng tiền không hợp lệ.");
                return "redirect:/ban-hang";
            }

            // Cập nhật trạng thái hóa đơn thành "Đã thanh toán"
            hoaDon.setTrangThai("Da thanh toan");


            // Cập nhật ngày sửa thành ngày hiện tại
            hoaDon.setNgaySua(Date.valueOf(LocalDate.now()));

            // Lưu hóa đơn đã cập nhật
            hoaDonRepository.save(hoaDon);

            // Xóa hóa đơn và giỏ hàng khỏi session
            session.removeAttribute("hoaDon");
            session.removeAttribute("tongTien");
            session.removeAttribute("listHDCT");

            redirectAttributes.addFlashAttribute("message", "Thanh toán thành công!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy hóa đơn.");
        }

        return "redirect:/ban-hang"; // Chuyển hướng về trang bán hàng
    }



}
