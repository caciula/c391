<!DOCTYPE html PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
    <title>Error</title> 
</head>

<body> 

    <jsp:include page="resources/includes/header.jsp" />
    
    <div class="content">
	    <p class="pageTitle">Error</p>
	    
	    <hr/>
	    <font color="red">${errorMessage}</font>
	    
	    <br><br>
	    
	    <a href="${errorBackLink}">Back</a>
    </div>
</body>
</html>
