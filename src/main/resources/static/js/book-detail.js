
$(document).ready(function () {
	  // show books by category
  $(".category").click(function () {
    $("#pagination-id").hide(this);
    let categoryId = Number.parseInt($(this).val());
    let sortBy = $("#sort-by").val();
    callCategoryAjax(sortBy, categoryId);
  });
})



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