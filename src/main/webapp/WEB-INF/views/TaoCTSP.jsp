<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <title>Document</title>
</head>
<body class="container">
<div>
    <nav class="navbar navbar-expand-lg bg-body-tertiary">
        <div class="container-fluid">
            <a class="navbar-brand"><img src="https://270349597.e.cdneverest.net/image/responsive/header/logo.svg" alt="Bootstrap" width="80" height="80"
                                         style=" margin-left: 40px;"></a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarScroll" aria-controls="navbarScroll" aria-expanded="false"
                    aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarScroll">
                <ul class="navbar-nav me-auto my-2 my-lg-0 navbar-nav-scroll"
                    style="--bs-scroll-height: 100px;">
                    <li class="nav-item">
                        <form class="d-flex " role="search">
                            <input class="form-control " type="search" placeholder="Freeship đến 30k"
                                   aria-label="Search" style=" margin-left: 30px; width: 700px;">
                            <button style="width: 100px;" class="btn text-primary ">Tìm kiếm</button>
                        </form>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/ban-hang"><i
                                class="fa-solid fa-house"></i>Bán Hàng</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
                           aria-expanded="false" style=" margin-left: 0px;">
                            <i class="fa-regular fa-face-smile-wink"></i> Danh Mục
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="/danh-muc">Danh mục sản phẩm</a></li>
                            <li><a class="dropdown-item" href="/hoa-don">Danh sách hóa đơn</a></li>
                            <li><a class="dropdown-item" href="/san-pham">Sản phẩm</a></li>
                            <li><a class="dropdown-item" href="/ctsp">Chi tiết sản phẩm</a></li>
                            <li><a class="dropdown-item" href="/khach-hang">Khách hàng</a></li>
                            <li><a class="dropdown-item" href="/mau-sac">Màu sắc</a></li>
                            <li><a class="dropdown-item" href="/size">Kích Thước</a></li>


                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li><a class="dropdown-item" href="#!ctsp">Đăng xuất</a></li>
                        </ul>
                    </li>
                    <div class="vertical-line"></div>
                    <li class="nav-item ">
                        <a class="nav-link " href="../Views/GioHang.html"><i class="fa-solid fa-cart-shopping"></i><span
                                class="position-absolute  translate-middle badge rounded-pill bg-danger">
                                    0
                                    <span class="visually-hidden">unread messages</span>
                                </span></a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    <nav class="navbar text-bg-warning">
        <div class="container-fluid d-flex justify-content-center">
            <h5 class="text-center">
                <i class="fa-solid fa-people-carry-box fa-bounce" style="color: #74C0FC;"></i>
                30 ngày đổi ý & miễn phí trả hàng
            </h5>
        </div>
    </nav>
</div>
<div class="d-flex justify-content-center mt-3">
    <div class="col-md-8 account-info" style="background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); height: 800px;">
        <h2 class="text-center">Thêm chi tiết sản phẩm</h2>
        <!-- Form to submit CTSP data -->
        <form action="${pageContext.request.contextPath}/ctsp/save" method="post">
            <input type="hidden" name="id" value="${CTSP.id}"/>
            <div class="mb-3">
                <label class="form-label">Tên sản phẩm</label>
                <select class="form-select" name="sanPham.id">
                    <c:forEach var="sp" items="${listSP}">
                        <option value="${sp.id}" ${CTSP.sanPham.id == sp.id ? 'selected' : ''}>${sp.tenSanPham}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-3">
                <label  class="form-label">Giá bán</label>
                <input type="number" name="giaBan" class="form-control" min="0" value="${CTSP.giaBan}">
            </div>

            <div class="mb-3">
                <label  class="form-label">Số lượng tồn</label>
                <input type="number" name="soLuongTon" class="form-control" min="0" value="${CTSP.soLuongTon}">
            </div>

            <div class="mb-3 d-flex">
                <div class="flex-fill me-2">
                    <label class="form-label">Màu sắc</label>
                    <select class="form-select" name="mauSac.id">
                        <c:forEach var="ms" items="${listMS}">
                            <option value="${ms.id}" ${CTSP.mauSac != null && CTSP.mauSac.id == ms.id ? 'selected' : ''}>
                                    ${ms.tenMau}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="flex-fill ms-2">
                    <label class="form-label">Kích thước</label>
                    <select class="form-select" name="size.id">
                        <c:forEach var="sz" items="${listSize}">
                            <option value="${sz.id}" ${CTSP.size != null && CTSP.size.id == sz.id ? 'selected' : ''}>
                                    ${sz.tenSize}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">Trạng thái</label>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="trangThai" value="Active" ${CTSP.trangThai == 'Active' ? 'checked' : ''}>
                    <label class="form-check-label">Active</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="trangThai" value="UnActive" ${CTSP.trangThai == 'UnActive' ? 'checked' : ''}>
                    <label class="form-check-label">UnActive</label>
                </div>
            </div>


            <hr>
            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            <div class="text-center">
                <button type="submit" class="btn btn-outline-primary w-25 my-2" style="border-radius: 8px;">Lưu</button>
            </div>
        </form>
    </div>
</div>

<div class="row mt-5">
    <h2 class="m-2">Danh mục chi tiết sản phẩm</h2>
    <table class="table" style="background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); height: 400px;">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Tên sản phẩm</th>
            <th scope="col">Giá bán</th>
            <th scope="col">Số lượng tồn</th>
            <th scope="col">Màu sắc</th>
            <th scope="col">Kích thước</th>
            <th scope="col">Trạng thái</th>
            <th scope="col">Ngày tạo</th>
            <th scope="col">Ngày sửa</th>
            <th scope="col">Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="ctsp" items="${listCTSP}" >
            <tr>
                <th scope="row">${ctsp.id}</th>
                <td>${ctsp.sanPham.tenSanPham}</td>
                <td>${ctsp.giaBan}</td>
                <td>${ctsp.soLuongTon}</td>
                <td>${ctsp.mauSac.tenMau}</td>
                <td>${ctsp.size.tenSize}</td>
                <td>${ctsp.trangThai}</td>
                <td>${ctsp.ngayTao}</td>
                <td>${ctsp.ngaySua}</td>
                <td>

                    <a href="${pageContext.request.contextPath}/ctsp/edit/${ctsp.id}" class="btn btn-primary btn-sm">Sửa</a>
                    <a href="${pageContext.request.contextPath}/ctsp/delete/${ctsp.id}" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc muốn xóa danh mục này?');">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>


<div class="" style="margin-top: 30px;">
    <footer class="py-5">

        <div class="row">
            <div class="col-3">
                <h6>Hỗ trợ khách hàng</h6>
                <ul class="nav flex-column">
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Hotline: 1900-6035</a></li>

                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Các câu hỏi thường gặp</a>
                    </li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Gửi yêu cầu hỗ trợ</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Hướng dẫn đặt hàng</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Phương thức vận chuyển</a>
                    </li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Chính sách đổi trả</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Hướng dẫn trả góp</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Chính sách hàng nhập
                        khẩu</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Hỗ trợ khách hàng:
                        hotro@tiki.vn</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Báo lỗi bảo mật:
                        security@tiki.vn</a></li>
                </ul>
            </div>

            <div class="col-3">
                <h6>Về Tiki</h6>
                <ul class="nav flex-column">
                    <li class="nav-item mb-2"><a href="GioiThieu.html" class="nav-link p-0 text-body-secondary">Giới thiệu
                        Tiki</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Tiki Blog</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Tuyển dụng</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Chính sách bảo mật thanh
                        toán</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Chính sách bảo mật thông
                        tin cá nhân</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Chính sách giải quyết khiếu
                        nại</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Điều khoản sử dụng</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Giới thiệu Tiki Xu</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Gói hội viên VIP</a></li>

                </ul>
            </div>

            <div class="col-2">
                <h6>Hợp tác và liên kết</h6>
                <ul class="nav flex-column">
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Quy chế hoạt động Sàn
                        GDTMĐT</a></li>
                    <li class="nav-item mb-2"><a href="#" class="nav-link p-0 text-body-secondary">Bán hàng cùng Tiki</a></li>
                </ul>
                <h6>Chứng nhận bởi</h6>
                <img src="" style="width: 150px;" alt="">
            </div>

            <div class="col-2">
                <h6>Phương thức thanh toán</h6>
                <img src="" alt="">
                <h6>Dịch vụ giao hàng</h6>
                <img src="" alt="">
            </div>
            <div class="col-2">
                <h6>Kết nối với chúng tôi</h6>
                <img src="" alt="">
                <h6>Tải ứng dụng trên điện thoại</h6>
                <img src="" style="width: 180px;" alt="">
            </div>
        </div>
    </footer>
</div>
</body>
</html>