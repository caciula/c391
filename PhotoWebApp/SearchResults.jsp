<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
    <title>SearchResults</title> 
</head>

<body> 

<jsp:include page="resources/includes/header.jsp" />
	<table>
		<tbody>
			<tr>
		    	<c:forEach items="${matchingIds}" var="foundId">
		       	<a href="/PhotoWebApp/ViewImage?${foundId}"><img src ="/PhotoWebApp/GetThumbnailImage?${foundId}"></a>
		    	</c:forEach>
		    </tr>
		</tbody>
	</table>
	
</body>