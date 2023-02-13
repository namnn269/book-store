//const domain = "/bookstore";
const domain = "";

$(document).ready(function () {
  // khi hover cart
  $(".top-cart-info").hover(function () {
    cartHoverhAjax();
  });
});

// function ajax khi hover
function cartHoverhAjax() {
  $.ajax({
    type: "get",
    url: domain + "/cart/hover-cart-ajax",
    success: function (value) {
      $(".scroller").empty();
      $(".scroller").append(value);
    },
  });
}

// let working = false;
// let x = 3;

// $(document).ready(function () {
// khi cuộn trang
// $(window).scroll(function () {
//   if (
//     $(this).scrollTop() + 300 >
//     $("body").height() - $(window).height()
//   ) {
//     callAjax(x, 1, "id");
//     x++;
//   }
// });
// });

// function ajax khi cuộn
// function callAjax(pageNo, pageSize, sortBy) {
//   $.ajax({
//     type: "get",
//     url: "/trang-chu/product-home-ajax",
//     data: { pageNo: pageNo, pageSize: pageSize, sortBy: sortBy },
//     success: function (value) {
//       $("#product-list").append(value);
//     },
//   });
// }
