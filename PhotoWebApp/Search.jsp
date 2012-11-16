<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
    <title>Search</title> 
</head>

<body> 

<jsp:include page="resources/includes/header.jsp" />

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
    <th>From Date(Optional): </th>
    <td><input name="fromDate" type="textfield" size="30" ></input></td>
  </tr>
  <tr>
    <th>To Date(Optional): </th>
    <td><input name="toDate" type="textfield" size="30" ></input></td>
  </tr>
  <tr>
    <th>Sort By: </th>
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
</form>
</body> 
</html>
