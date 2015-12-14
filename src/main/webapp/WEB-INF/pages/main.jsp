<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page session="true"%>

<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>On-Line High Precision Currency Converter</title>
<meta name="Description" content="Get real-time stock quotes.">

<style>
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
	font-size: 10px;
	color: b80808;
	border: 1px solid b80808;
	width: 750px;
	height: 55px;
	padding: 5px;
}

.history {
	font-family: 'Tahoma', sans-serif;
	font-size: 12px;
	color: #6da021;
	border: 1px solid #6da021;
	width: 800px;
	height: 200px;
	padding-top: 5px;
	padding-left: 10px;
}
</style>
</head>
<jsp:include page="fragments/headerMain.jsp" />

<body style="margin-left: 6px; min-width: 0px;">

	<div id="logout-box">
		<table>
			<tbody>
				<tr>
					<td><c:if
							test="${pageContext.request.userPrincipal.name != null}">
							<h4>
								${pageContext.request.userPrincipal.name} | <a
									href="javascript:formSubmit()"> Logout</a>
							</h4>
						</c:if></td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div class="header">
		<label class="disclaimer">${disclaimer}</label>
	</div>

	<form:form action="main" modelAttribute="actualCurrency" method="get">
		<div class="sfe-break-top">
			<h5>
				Enter calculation precision (numbers from range 1...99):
				<form:input path="currency" name="precision" maxlength="2" size="10"
					autocomplete="off" value="5" />
			</h5>
		</div>
		<p></p>

		<div class="sfe-break-top">
			<h5>
				Enter amount. Use dot as selector for decimal numbers:
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
	<p></p>
	<h5>${result}</h5>
	<p></p>
	<p></p>

	<h5>Last 10 requests:</h5>
	<textarea rows="20" cols="150" class="history">${history}</textarea>
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

	<jsp:include page="fragments/footer.jsp" />
</body>
</html>