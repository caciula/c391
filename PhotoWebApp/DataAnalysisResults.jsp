<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
    <title>Search Results</title> 
</head>

<body> 

<jsp:include page="resources/includes/header.jsp" />

    <div class="content">
    
        <p class="pageTitle">Report</p>
        
        <p><a href="/PhotoWebApp/DataAnalysis">Generate New Report</a></p>
        
        <hr/>
        
		<%@ page import="main.util.ReportRow" %>
		<c:forEach items="${reportRows}" var="row">
        	<tr>
            	<td>
                   <c:out value="${row.user}"/>
            	</td>
            	<td>
                   	<c:out value="${row.subject}"/>
            	</td>
            	<td>
                   	<c:out value="${row.total}"/>
            	</td>
        	</tr>
		</c:forEach>
			
	
	</div>
</body>