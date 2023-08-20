<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css"
	integrity="sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"
	integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
	crossorigin="anonymous"></script>
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"
	integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN"
	crossorigin="anonymous"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.min.js"
	integrity="sha384-+sLIOodYLS7CIrQpBjl+C7nPvqq+FbNUBDunl/OZv93DB7Ln/533i8e/mZXLi/P+"
	crossorigin="anonymous"></script>
<title>Send attachment mail page !</title>
</head>
<body>
	<main class="container text-center">
		<h1 class="text-warning">THIS IS SIMPLE SEND MAIL</h1>
		<h6>Number :${NumberTo}</h6>
		<form:form class="row mt-5" action="/mail/send-attachment"
			method="post" modelAttribute="mail" enctype="multipart/form-data">
			<div class="form-group col-12">
				<form:input class="form-control" placeholder="To" path="to" />
			</div>
			<div class="form-group col-12">
				<form:input class="form-control" placeholder="Title" path="subject" />
			</div>
			<div class="form-group col-12">
				<form:textarea class="form-control" rows="5" placeholder="Body"
					path="body" />
			</div>
			<div class="form-group col-12">
				<form:input type="file" class="custom-file-input" path="attachments"/>
					id="customFile" />
				<label class="custom-file-label" for="customFile">Choose
					file</label>
			</div>
			<div class="col-12 text-center">
				<form:button class="btn btn-danger">Send Mail</form:button>
			</div>
		</form:form>
	</main>
	<script>
		// Add the following code if you want the name of the file appear on select
		$(".custom-file-input").on(
				"change",
				function() {
					var fileName = $(this).val().split("\\").pop();
					$(this).siblings(".custom-file-label").addClass("selected")
							.html(fileName);
				});
	</script>
</body>
</html>