let oldPassword = $("#old-password");
let newPassword1 = $("#new-password-1");
let newPassword2 = $("#new-password-2");
let errorServer = $("#server-errormsg");

function checkCooincident() {
	if (newPassword1.val() !== newPassword2.val()) {
		$("#errormsg").html("Mật khẩu không trùng khớp");
		errorServer.html("");
	} else $("#errormsg").html("");
}

function checkForm(errorMsg) {
	errorServer.html("");
	$("#errormsg").html(errorMsg);
}

$("#new-password-1").keyup(function() {
	checkCooincident();
});

$("#new-password-2").keyup(function() {
	checkCooincident();
});

function checkFormChangePassword() {
	if (!oldPassword.val() || !newPassword1.val() || !newPassword2.val()) {
		checkForm("Không đươc để trống");
		return false;
	}
	if (newPassword1.val() !== newPassword2.val()) {
		checkForm("Mật khẩu không trùng khớp");
		return false;
	}
	if (newPassword1.val().length < 8 || newPassword2.val().length < 8) {
		checkForm("Mật khẩu mới tối thiếu 8 ký tự");
		return false;
	}
	return true;
}