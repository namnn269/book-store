$(document).ready(function () {
  // khi hủy tìm kiếm
  $("#cancel-search-btn").click(function () {
    $("#pagination-id").show(this);
    $("#home-search-input").val("");
    let pageNo =
      Number.parseInt($(".pagination").find(".page-item.active").text()) - 1;
    callAjax(pageNo);
  });

  // show books by category
  // $(".category").click(function () {
  //   $("#pagination-id").hide(this);
  //   let categoryId = Number.parseInt($(this).val());
  //   let sortBy = $("#sort-by").val();
  //   callCategoryAjax(sortBy, categoryId);
  // });

  // lần vào trang đầu tiên tải trang 1
  callAjax(0);

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

  // accept confirm btn
  $("body").on("click", "#confirm-btn", function () {
    callAjax(0);
  });

  // search category
  $("body").on("click", "#home-search-btn", function () {
    callAjax(0);
  });

  // chọn category để lọc
  $("#categoryId").select2();
});

////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////

// hàm gọi AJAX - get author
function callAjax(pageNo) {
  $.ajax({
    url: domain + "/trang-chu/product-home-ajax",
    data: {
      pageNo: pageNo,
      pageSize: getPageSize(),
      searchFor: getSearchFor(),
      categoryId: getCategoryId(),
      sortbyPrice: getSortByPrice(),
    },
    success: (value) => {
      let tbody = $("body").find("#product-list");
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

// lấy search word
function getSearchFor() {
  return $("#home-search-input").val();
}

// lấy id thể loại filter
function getCategoryId() {
  return Number.parseInt($("#categoryId").val());
}

// lấy sort by price
function getSortByPrice() {
  return $("#sort-by-price").val();
}
