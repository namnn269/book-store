$(document).ready(function () {
  $(".nav-sidebar").removeClass("active");
  $(".order-nav-sidebar").addClass("active");
  callAjax(0);

  $("body").on("click", ".confirm-btn", function () {
    console.log($(this).val());
    if (confirm("Xác nhận đơn hàng")) {
      callConfirmOrderAjax(Number.parseInt($(this).val()));
    }
  });
  $("body").on("click", ".cancel-btn", function () {
    if (confirm("Hủy đơn hàng")) {
      callCancelOrderAjax(Number.parseInt($(this).val()));
    }
  });
});

// accept confirm btn
$("body").on("click", "#confirm-btn", function () {
  callAjax(0);
});

// lấy vị trí nút bấm và gán 'active' => gọi AJAX
$("body").on("click", ".page-item", function () {
  $(".page-item").removeClass("active");
  $(this).addClass("active");
  let pageNo = Number.parseInt($(this).text() - 1);
  callAjax(pageNo);
});

// lấy vị trí nút kế tiếp và gán active. nút trước bỏ
$("body").on("click", ".next-page-item", function () {
  $(".pagination").find(".page-item.active").next().addClass("active");

  $(".pagination").find(".page-item.active").prev().removeClass("active");

  callAjax(getCurrentPageIndex());
});

// lấy vị trí nút trước và gán 'active' nút sau bỏ
$("body").on("click", ".previous-page-item", function () {
  $(".pagination").find(".page-item.active").prev().addClass("active");

  $(".pagination").find(".page-item.active").next().removeClass("active");

  callAjax(getCurrentPageIndex());
});
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////

function callConfirmOrderAjax(orderId) {
  $.ajax({
    type: "post",
    url: domain + "/admin/confirm-order-ajax",
    data: { orderId: orderId },
    success: (value) => {
      callAjax(getCurrentPageIndex());
      $("#alert-confirm").empty();
      $("#alert-confirm").append(value);
    },
  });
}

function callCancelOrderAjax(orderId) {
  $.ajax({
    type: "post",
    url: domain + "/admin/cancel-order-ajax",
    data: { orderId: orderId },
    success: (value) => {
      callAjax(getCurrentPageIndex());
      $("#alert-confirm").empty();
      $("#alert-confirm").append(value);
    },
  });
}

// hàm gọi AJAX - get orders
function callAjax(pageNo) {
  $.ajax({
    url: domain + "/admin/management-order-ajax",
    data: {
      pageNo: pageNo,
      pageSize: getPageSize(),
      sortBy: getSortByDate(),
    },
    success: (value) => {
      let tbody = $("#table").find("tbody");
      tbody.empty();
      tbody.append(value[0]);
      $(".clearfix").empty();
      $(".clearfix").append(value[1]);
    },
  });
}

// lấy chỉ số trang hiện tại
function getCurrentPageIndex() {
  let currentPage =
    Number.parseInt($(".pagination").find(".page-item.active").text()) - 1;
  return currentPage < 0 ? 0 : currentPage;
}

// lấy số phần tử 1 trang
function getPageSize() {
  return Number.parseInt($("#quantity").val());
}

// lấy sort word
function getSortByDate() {
  return Number.parseInt($("#sortByDate").val());
}
