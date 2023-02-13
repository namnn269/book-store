$(document).ready(
  $(".btn-down").click(function () {
    callChangeAjax(false, $(this).val());
  }),

  $(".btn-up").click(function () {
    callChangeAjax(true, $(this).val());
  }),

  $(".delete").click(function () {
    if (confirm("Bạn chắc chắn muốn xóa??")) {
      callDeleteAjax($(this).val());
      $(this).parents("tr").hide();
    }
  }),

  $(".delete-many").click(function () {
    let values = [];
    $.each($("input[class='delete-many-input']:checked"), function () {
      values.push(Number.parseInt($(this).val()));
    });
    if (!values.length) return;
    if (confirm("Bạn chắc chắn muốn xóa??")) {
      $.each($("input[class='delete-many-input']:checked"), function () {
        $(this).parents("tr").hide();
        callDeleteAjax($(this).val());
      });
    }
  })
);

function callDeleteAjax(id) {
  $.ajax({
    type: "post",
    data: { id: id },
    url: domain + "/cart/delete-order-detail-incart",
    success: function () {
      $("#message").addClass("alert alert-success");
      $("#message").text("Xóa thành công!");
    },
  });
}

function callChangeAjax(increase, id) {
  $.ajax({
    type: "post",
    data: { increase: increase, id: id },
    url: domain + "/cart/change-amount-in-cart",
    success: function (value) {
      if (value) {
        console.log("value: ", value);
        $("#message").addClass("alert alert-warning");
        $("#message").text(value);
      }
    },
  });
}
