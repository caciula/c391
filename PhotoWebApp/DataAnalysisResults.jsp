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
        <table>
        <tr>
        	<form name="DataAnalysis" method="POST" action="DataAnalysis">
			<td>
				<input type="radio" name="drillDown" value="None" checked> None
			</td>
			<td>
				<input type="radio" name="drillDown" value="Yearly"> Yearly
			</td>
			<td>
				<input type="radio" name="drillDown" value="Monthly"> Monthly
			</td>
			<td>
				<input type="radio" name="drillDown" value="Weekly"> Weekly
			</td>
			
			<input type="hidden" name="user" value="${param.user}"/>
   			<input type="hidden" name="subject" value="${param.subject}"/>
   			<input type="hidden" name="fromDate" value="${param.fromDate}"/>
   			<input type="hidden" name="toDate" value="${param.toDate}"/>
   			
			<td ALIGN=CENTER COLSPAN="2">
			<input type="submit" name=".submit" value="Rollup/Drilldown">
			</td>
		<tr>
        </table>
        
        
		<%@ page import="main.util.ReportRow" %>
		<table>
			<thead>
			<tr>
            	<th>${head1}</th>
            	<th>${head2}</th>
            	<th>${head3}</th>
            	<th>${head4}</th>
        	</tr>
			</thead>
			<tbody>
				<c:forEach items="${reportRows}" var="row">
        			<tr>
            			<td>
                   			<c:out value="${row.col1}"/>
            			</td>
            			<td>
                   			<c:out value="${row.col2}"/>
            			</td>
            			<td>
                   			<c:out value="${row.col3}"/>
            			</td>
            			<td>
                   			<c:out value="${row.col4}"/>
            			</td>
            			
        			</tr>
        		</c:forEach>
			</tbody>
		</table>
			
	
	</div>
</body>