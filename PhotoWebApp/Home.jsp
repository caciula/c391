<!DOCTYPE html PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
    <title>Home</title> 
</head>

<body> 

    <jsp:include page="resources/includes/header.jsp" />
    
    <div class="content">
	    <p class="pageTitle">Home</p>
	    
	    <hr/>
	    
        Home screen - to be updated.
        
        <p>Display an upload link if a user is logged in? (<a href="/PhotoWebApp/UploadImage" >Upload Image</a>)</p>
        
        <p>Search</p>
        
        <p>Also need to display the top 5 images here?</p>

    </div>

	<form method="link" action="CreateGroup">
		<input type="submit" value="Click here to create a group">
	</form>

        <form method="link" action="AddUserToGroup">
                <input type="submit" value="Click here to add a user to a group">
        </form>
</body>
</html>
