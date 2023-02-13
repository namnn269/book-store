$(document).ready(function () {
  $("body").on("click", ".confirm-btn", function () {
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

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////

function callConfirmOrderAjax(orderId) {
  $.ajax({
    type: "post",
    url: domain + "/admin/confirm-order-ajax",
    data: { orderId: orderId },
    success: (value) => {
      $("#alert-confirm").empty();
      $("#alert-confirm").append(value);
      $(".two-buttons").empty();
    },
    error: (error) => {
      $("#alert-confirm").empty();
      $("#alert-confirm").append(error.responseText);
    },
  });
}

function callCancelOrderAjax(orderId) {
  $.ajax({
    type: "post",
    url: domain + "/admin/cancel-order-ajax",
    data: { orderId: orderId },
    success: (value) => {
      $("#alert-confirm").empty();
      $("#alert-confirm").append(value);
      $(".two-buttons").empty();
    },
  });
}
