//const domain = "/bookstore";
const domain = "";

$(document).ready(function () {
  $("body").on("click", ".nav-sidebar", () => {
    $(".nav-sidebar").removeClass("active");
    $(this).addClass("active");
  });

  $("body").click(function () {
    $(".alert-server").empty();
    $("#message").empty();
    $(".alert-server").removeClass();
    $("#message").removeClass();
  });

  $("#cancel-search-btn").click(function () {
    $("#pagination-id").show(this);
    $("#admin-search-input").val("");
    let pageNo =
      Number.parseInt($(".pagination").find(".page-item.active").text()) - 1;
    callAjax(pageNo);
  });
});
