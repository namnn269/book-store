$(document).ready(function() { });
function checkFormUpdate() {
	let phone = $("#phone").val();
	let address = $("#address").val();
	let date = $("#date").val();
	if (isNaN(phone) || phone.length < 9 || phone.length > 11) {
		$("#message").addClass("alert alert-danger");
		$("#message").text("Định dạng SĐT không đúng!");
		return false;
	} else if (!date || !phone || !address) {
		$("#message").text("Nhập thiếu thông tin!");
		return false;
	}
	return true;
}