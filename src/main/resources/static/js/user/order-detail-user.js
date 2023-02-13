$(document).ready(function () {
  $("body").on("click", ".cancel-btn", function () {
    if (confirm("Hủy đơn hàng")) {
      callCancelOrderAjax(Number.parseInt($(this).val()));
    }
  });
});

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////

function callCancelOrderAjax(orderId) {
  $.ajax({
    type: "post",
    url: domain + "/orders/cancel-order-ajax",
    data: { orderId: orderId },
    success: (value) => {
      $("#alert-confirm").empty();
      $("#alert-confirm").append(value);
      $(".two-buttons").empty();
    },
  });
}
