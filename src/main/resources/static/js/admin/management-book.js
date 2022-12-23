$(".nav-sidebar").removeClass("active");
$(".book-nav-sidebar").addClass("active");

$(document).ready(function() {
	// xóa nhiều phần tử đã chọn
	$("#delete-many-btn").click(function() {
		let values = [];
		$.each($("input[class='delete-many-input']:checked"), function() {
			values.push(Number.parseInt($(this).val()));
		});
		if (confirm("Xóa " + values.length + " cuốn sách đã chọn????"))
			values.forEach((id) => callAjaxDelele(id));
	});

	// lần vào trang đầu tiên tải trang 1
	callAjax(0);

	$("body").on("click", ".delete", function() {
		let delId = $(this).val();
		if (confirm("Xóa 1 cuốn sách ??")) {
			callAjaxDelele(delId);
		}
	});

	// lấy vị trí nút bấm và gán 'active' => gọi AJAX
	$("body").on("click", ".page-item", function() {
		$(".page-item").removeClass("active");
		$(this).addClass("active");
		let pageNo = Number.parseInt($(this).text() - 1);
		callAjax(pageNo);
	});

	// lấy vị trí nút kế tiếp và gán active. nút trước bỏ
	$("body").on("click", ".next-page-item", function() {
		$(".pagination")
			.find(".page-item.active")
			.next()
			.addClass("active");

		$(".pagination")
			.find(".page-item.active")
			.prev()
			.removeClass("active");

		callAjax(getCurrentPageIndex());
	});

	// lấy vị trí nút trước và gán 'active' nút sau bỏ
	$("body").on("click", ".previous-page-item", function() {
		$(".pagination")
			.find(".page-item.active")
			.prev()
			.addClass("active");

		$(".pagination")
			.find(".page-item.active")
			.next()
			.removeClass("active");

		callAjax(getCurrentPageIndex());
	});

	// accept confirm btn
	$("body").on("click", "#confirm-btn", function() {
		callAjax(0);
	});

	// search category
	$("body").on("click", "#admin-search-btn", function() {
		callAjax(0);
	});

	// chọn category để lọc
	$("#categoryId").select2();
});

////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////

// hàm gọi AJAX - get author
function callAjax(pageNo) {
	$.ajax({
		url: "/admin/management-book-ajax",
		data: {
			pageNo: pageNo,
			pageSize: getPageSize(),
			searchFor: getSearchFor(),
			categoryId: getCategoryId(),
			sortbyPrice: getSortByPrice(),
		},
		success: (value) => {
			let tbody = $("#table").find("tbody");
			tbody.empty();
			tbody.append(value[0]);
			$(".clearfix").empty();
			$(".clearfix").append(value[1]);
		},
	});
}

// hàm gọi Ajax - delete category
function callAjaxDelele(delId) {
	$.ajax({
		method: "get",
		url: "/admin/delete-book",
		data: { id: delId },
		success: (value) => {
			callAjax(getCurrentPageIndex());
			$(".alert-server").empty();
			$(".alert-server").removeClass();
			$("#message").addClass("alert alert-warning");
			$("#message").empty();
			$("#message").append(value);
		},
	});
}

// lấy chỉ số trang hiện tại
function getCurrentPageIndex() {
	let currentPage =
		Number.parseInt($(".pagination").find(".page-item.active").text()) -
		1;
	return currentPage < 0 ? 0 : currentPage;
}

// lấy số phần tử 1 trang
function getPageSize() {
	return Number.parseInt($("#quantity").val());
}

// lấy search word
function getSearchFor() {
	return $("#admin-search-input").val();
}

// lấy id thể loại filter
function getCategoryId() {
	return Number.parseInt($("#categoryId").val());
}

// lấy sort by price
function getSortByPrice() {
	return $("#sort-by-price").val();
}
