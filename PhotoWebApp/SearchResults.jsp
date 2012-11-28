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
    
        <p class="pageTitle">Search Results</p>
        
        <p><a href="/PhotoWebApp/Search">Perform New Search</a></p>
        
        <hr/>
        
		<table>
			<tbody>
				<tr>
			    	<c:forEach items="${matchingIds}" var="foundId">
			       	<a href="/PhotoWebApp/ViewImage?${foundId}"><img src ="/PhotoWebApp/GetThumbnailImage?${foundId}"></a>
			    	</c:forEach>
			    </tr>
			</tbody>
		</table>
	
	</div>
</body>