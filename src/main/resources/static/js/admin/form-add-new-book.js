// chọn thể loại
$(document).ready(function() {
	// select option
	$("#selUser2").select2();

	// check box
	$("#authorSelection").multiselect({
		columns: 1,
		placeholder: "-- Chọn tác giả --",
		search: true,
	});

	$(".nav-sidebar").removeClass("active");
	$(".book-nav-sidebar").addClass("active");
});

function checkFormNewBook() {
	let price = $("#price").val();
	let price2 = $("#price2").val();
	let year = $("#year").val();
	let quantity = $("#quantity").val();
	if (
		isNaN(price) ||
		isNaN(price2) ||
		isNaN(year) ||
		isNaN(quantity) ||
		Number.parseFloat(price) < 0 ||
		Number.parseFloat(price2) < 0
	) {
		$("#message").addClass("alert alert-danger ");
		$("#message").text("Dữ liệu không hợp lệ");
		return false;
	}
	return true;
}