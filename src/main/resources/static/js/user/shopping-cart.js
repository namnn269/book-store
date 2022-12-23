$(document).ready(
	$(".btn-down").click(function() {
		callChangeAjax(false, $(this).val());
	}),
	$(".btn-up").click(function() {
		callChangeAjax(true, $(this).val());
	}),
	$(".delete").click(function() {
		callDeleteAjax($(this).val());
		$(this).parents("tr").hide();
	})
);

function callDeleteAjax(id) {
	$.ajax({
		type: "post",
		data: { id: id },
		url: "/cart/delete-order-detail-incart",
		success: function() {
			console.log("delete ", id);
		},
	});
}

function callChangeAjax(increase, id) {
	$.ajax({
		type: "post",
		data: { increase: increase, id: id },
		url: "/cart/change-amount-in-cart",
		success: function() {
			console.log("change ", id);
		},
	});
}