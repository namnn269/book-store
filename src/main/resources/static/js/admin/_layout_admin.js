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
});
