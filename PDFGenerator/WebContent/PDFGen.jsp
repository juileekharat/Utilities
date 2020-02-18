<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">.

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>PDG Generator</title>
	</head>
	<body>
		<div align="center">
			<h1>PDF Generator</h1>
			<form action="GeneratePDF" method="POST">
				PMMDY No : <input type="text" placeholder="Enter PMMDY No here" name="pmmdyNumber"/>
				<input type="submit" value="Generate PDF" /><br>
			  	<c:if test="${not empty responseMsg}">
				    <h3>${responseMsg}</h3>
				</c:if>
				<br>
				<c:if test="${not empty pdfDisplayPath}">
				    <embed src="${pdfDisplayPath}" width="700px" height="800px" />
				</c:if>
			</form>
		</div>
	</body>
</html>