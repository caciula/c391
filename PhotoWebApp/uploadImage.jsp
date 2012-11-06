<!DOCTYPE html PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <title>Upload Image</title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.1/jquery.js"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.min.js"></script>
    <link rel="stylesheet" type="text/css" media="screen" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/themes/base/jquery-ui.css">
    <script type="text/javascript">
    $(function() {
        $('.date-picker').datepicker( {
            changeMonth: true,
            changeYear: true,
        });
    });
    </script>
</head>

<body>
    <p><b>Upload Image</b></p>
        
    <hr>
    
    <form name="uploadImage" method="POST" enctype="multipart/form-data" action="/PhotoWebApp/UploadImage">
        <table>
            <tr>
                <th>File path: </th>
                <td><input name="imagePath" size="30" type="file"></td>
            </tr>
            <tr>
                <th>Subject: </th>
                <td><input name="subject" size="50" maxlength="128" type="text"></td>
            </tr>
            <tr>
                <th>Place: </th>
                <td><input name="place" size="50" maxlength="128" type="text"></td>
            </tr>
            <tr>
                <th>Date: </th>
                <td><input name="date" id="date" class="date-picker" maxlength="10"/></td>
            </tr>
            <tr>
                <th>Description: </th>
                <td><textarea name="description" rows="4" cols="57" maxlength="2048"></textarea></td>
            </tr>
            <tr>
                <td colspan="2" align="CENTER"><input name=".submit" value="Upload" type="submit"></td>
            </tr>
            </tbody>
         </table>
    </form>
    
</body>
</html>
