<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>On-Line High Precision Currency Converter</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="Description" content="Show user">
</head>

<jsp:include page="../fragments/headerMain.jsp" />
<a class="navbar-brand" href="${userActionUrl}/users">Back</a>
<br />

<div class="container">


	<c:if test="${not empty msg}">
		<div class="alert alert-${css} alert-dismissible" role="alert">
			<button type="button" class="close" data-dismiss="alert"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<strong>${msg}</strong>
		</div>
	</c:if>

	<h1>User Detail</h1>
	<br />

	<div class="row">
		<label class="col-sm-2">ID</label>
		<div class="col-sm-10">${user.id}</div>
	</div>

	<div class="row">
		<label class="col-sm-2">Name</label>
		<div class="col-sm-10">${user.name}</div>
	</div>

	<div class="row">
		<label class="col-sm-2">Email</label>
		<div class="col-sm-10">${user.email}</div>
	</div>

	<div class="row">
		<label class="col-sm-2">Country</label>
		<div class="col-sm-10">${user.country}</div>
	</div>

	<div class="row">
		<label class="col-sm-2">Zip Code</label>
		<div class="col-sm-10">${user.zipcode}</div>
	</div>

	<div class="row">
		<label class="col-sm-2">City</label>
		<div class="col-sm-10">${user.city}</div>
	</div>

	<div class="row">
		<label class="col-sm-2">Street</label>
		<div class="col-sm-10">${user.street}</div>
	</div>

	<div class="row">
		<label class="col-sm-2">Day</label>
		<div class="col-sm-10">${user.day}</div>
	</div>

	<div class="row">
		<label class="col-sm-2">Month</label>
		<div class="col-sm-10">${user.month}</div>
	</div>

	<div class="row">
		<label class="col-sm-2">Year</label>
		<div class="col-sm-10">${user.year}</div>
	</div>

</div>

<jsp:include page="../fragments/footer.jsp" />

</body>
</html>