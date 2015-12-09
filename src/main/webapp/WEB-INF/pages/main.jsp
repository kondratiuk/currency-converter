<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page session="true"%>

<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Currency Converter Application</title>
<meta name="Description" content="Get real-time stock quotes.">

<style>
h6 {
	background-color: #e0dede;
}

.error {
	color: #ff0000;
}

.errorblock {
	color: #000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding: 8px;
	margin: 16px;
}

.disclaimer {
	font-family: 'Tahoma', sans-serif;
	font-size: 12px;
	color: b80808;
	border: 1px solid b80808;
	width: 1050px;
	height: 55px;
	padding-top: 5px;
	padding-left: 10px;
}

.history {
	font-family: 'Tahoma', sans-serif;
	font-size: 15px;
	color: #6da021;
	border: 1px solid #6da021;
	width: 1050px;
	height: 250px;
	padding-top: 5px;
	padding-left: 10px;
}
</style>
</head>

<body style="margin-left: 6px; min-width: 0px;">
	<div id="logout-box">
		<table>
			<tbody>
				<tr>
					<td><c:if
							test="${pageContext.request.userPrincipal.name != null}">
							<h3>
								Welcome : ${pageContext.request.userPrincipal.name} | <a
									href="javascript:formSubmit()"> Logout</a>
							</h3>
						</c:if>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<textarea rows="4" cols="150" class="disclaimer">${disclaimer}</textarea>
	<p></p>
	<p></p>

	<h3>${title}</h3>
	<p></p>

	<form:form action="main" modelAttribute="actualCurrency" method="get">
		<div class="sfe-break-top">
			<h5>
				Please, select calculation precision. It's numbers quantity after
				dot. It should be integer in recommended range 1...20):  
				<form:input path="currency" name="precision" maxlength="2" size="1"
					autocomplete="off" value="5" />
			</h5>
		</div>
		<p></p>
		<p></p>

		<div class="sfe-break-top">
			<h5>
				Please, use dot to input decimal number:
				<form:input path="currency" name="num" maxlength="20" size="10"
					autocomplete="off" value="1" />
			</h5>
		</div>
		<p></p>

		<div class="sfe-break-top">
			<form:select path="currency" name="from">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${allCurrency}" />
			</form:select>
		</div>

		<div style="padding: 6px 8px">to</div>
		<div class="sfe-break-top">
			<form:select path="currency" name="to">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${allCurrency}" />
			</form:select>
			<div style="padding: 6px 8px"></div>
		</div>
		<input type="submit" value="Convert" />
		<p></p>
	</form:form>

	<h5>${result}</h5>
	<p></p>

	<h5>Last 10 requests:</h5>
	<textarea rows="20" cols="150" class="history">${history}</textarea>
	<p></p>

	<p></p>
	<h6>Copyright (c) 2015 O.K., alikkond@gmail.com</h6>
	<p></p>

	<c:url value="/j_spring_security_logout" var="logoutUrl" />
	<form action="${logoutUrl}" method="post" id="logoutForm">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
	</form>
	<script>
		function formSubmit() {
			document.getElementById("logoutForm").submit();
		}
	</script>

</body>
</html>