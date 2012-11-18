<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
    <title>Search</title> 
</head>

<body> 

<jsp:include page="resources/includes/header.jsp" />

    <div class="content">
    
        <p class="pageTitle">Search</p>
        
        <hr/>

<form name="Search" method="POST" action="Search">
<table>
	<tr>
		<th>Please define your search parameters:</th>
	</tr>
  <tr>
    <th>Keyword(s): </th>
    <td><input name="keywords" type="textfield" size="30" ></input></td>
  </tr>
  <tr>
    <th>From Date: </th>
    <td><input name="fromDate" type="textfield" size="30" ></input></td>
  </tr>
  <tr>
    <th>To Date: </th>
    <td><input name="toDate" type="textfield" size="30" ></input></td>
  </tr>
  <tr>
    <th>Sort By: <span class="requiredField">*</span></th>
    <td>
        <select name="SortBy">
        <option value="Rank">Relevance</option>
        <option value="Old">Time(oldest first)</option>
        <option value="New">Time(newest first)</option>
        </select>
    </td>
  </tr>
  <tr>
    <td ALIGN=CENTER COLSPAN="2"><input type="submit" name=".submit" value="Search"></td>
  </tr>
</table>
    </div>
</form>
</body> 
</html>
