let sortBy = $("#sort-by");
let searchKey = $("#search-input");
let pageSizeDefalut = 3;
// lần vào trang đầu tiên tải trang 1
// Lấy phần tử '.page-item' đầu tiên gán class 'active'
$(document).ready(function () {
  callPageHomeAjax(0, pageSizeDefalut, "id");
  $(".pagination").find(".page-item").first().addClass("active");

  // lấy vị trí nút bấm và gán 'avtive' => gọi AJAX
  $("body").on("click", ".page-item", function () {
    $(".page-item").removeClass("active");
    $(this).addClass("active");
    let pageNo = Number.parseInt($(this).text() - 1);

    callPageHomeAjax(pageNo, pageSizeDefalut, sortBy.val());
  });

  // lấy vị trí nút kế tiếp và gán active. nút trước bỏ
  $(".next-page-item").click(function () {
    $(".pagination").find(".page-item.active").next().addClass("active");
    $(".pagination").find(".page-item.active").prev().removeClass("active");

    let pageNo =
      Number.parseInt($(".pagination").find(".page-item.active").text()) - 1;
    callPageHomeAjax(pageNo, pageSizeDefalut, sortBy.val());
  });

  // lấy vị trí nút trước và gán 'active' nút sau bỏ
  $(".previous-page-item").click(function () {
    $(".pagination").find(".page-item.active").prev().addClass("active");
    $(".pagination").find(".page-item.active").next().removeClass("active");

    let pageNo =
      Number.parseInt($(".pagination").find(".page-item.active").text()) - 1;

    callPageHomeAjax(pageNo, pageSizeDefalut, sortBy.val());
  });

  // khi thay đổi sort by
  $("#sort-by").change(function () {
    // $(".pagination").find(".page-item").removeClass("active");
    // $(".pagination").find(".page-item").first().addClass("active");
    if ($("#search-input").val() == "") {
      $("#pagination-id").show(this);
    }
    let pageNo =
      Number.parseInt($(".pagination").find(".page-item.active").text()) - 1;
    let searchKey = $("#search-input").val();
    callPageHomeAjax(pageNo, pageSizeDefalut, sortBy.val(), searchKey);
  });

  // khi tìm kiếm #search-btn
  $("#search-btn").click(function () {
    if ($("#search-input").val() == "") return;
    $("#pagination-id").hide(this);
    let sortBy = $("#sort-by").val();
    let searchKey = $("#search-input").val();
    callSearchAjax(sortBy, searchKey);
  });

  // khi hủy tìm kiếm
  $("#cancel-search-btn").click(function () {
    $("#pagination-id").show(this);
    $("#search-input").val("");
    let pageNo =
      Number.parseInt($(".pagination").find(".page-item.active").text()) - 1;
    callPageHomeAjax(pageNo, pageSizeDefalut, sortBy.val());
  });

  // show books by category
  $(".category").click(function () {
    $("#pagination-id").hide(this);
    let categoryId = Number.parseInt($(this).val());
    let sortBy = $("#sort-by").val();
    callCategoryAjax(sortBy, categoryId);
  });
});

////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////

// function call Ajax page defalut
function callPageHomeAjax(pageNo, pageSize, sortBy, searchKey) {
  $.ajax({
    type: "get",
    url: "/trang-chu/product-home-ajax",
    data: {
      pageNo: pageNo,
      pageSize: pageSize,
      sortBy: sortBy,
      searchKey: searchKey,
    },
    success: function (value) {
      $("#product-list").empty();
      $("#product-list").append(value);
    },
  });
}

// function call ajax khi tìm kiếm
function callSearchAjax(sortBy, searchKey) {
  $.ajax({
    type: "get",
    url: "/trang-chu/product-search-home-ajax",
    data: {
      sortBy: sortBy,
      searchKey: searchKey,
    },
    success: function (value) {
      $("#product-list").empty();
      $("#product-list").append(value);
    },
  });
}

// function lấy tất cả sách theo thể loại
function callCategoryAjax(sortBy, categoryId) {
  $.ajax({
    type: "get",
    data: { sortBy: sortBy, categoryId: categoryId },
    url: "/trang-chu/show-book-by-category-ajax",
    success: function (value) {
      $("#product-list").empty();
      $("#product-list").append(value);
    },
  });
}
