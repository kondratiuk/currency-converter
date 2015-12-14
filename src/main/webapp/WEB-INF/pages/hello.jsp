<html lang="en">

<head>
<title>On-Line High Precision Currency Converter</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="Description" content="Welcome page">
</head>

<jsp:include page="fragments/headerMain.jsp" />
<body>

	<div class="navbar-header">
		<div class="col-sm-10">
			<a class="navbar-brand" href="${userActionUrl}/main"> Login</a>
		</div>

		<div class="col-sm-10">
			<a class="navbar-brand" href="${userActionUrl}/users/add"> Register</a>
		</div>

		<div class="col-sm-10">
			<a class="navbar-brand" href="${userActionUrl}/users"> User Management</a>
		</div>

		<div class="col-sm-10">
			<jsp:include page="fragments/footer.jsp" />
		</div>
	</div>

</body>
</html>