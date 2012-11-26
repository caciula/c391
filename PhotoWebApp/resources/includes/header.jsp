
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="header">
    <a href="Home" class="headerTitle">PhotoWeb</a>
    <span class="headerAlignRight">
        <c:if test='${username != null && username != ""}'>
            You are logged in as <a href="/PhotoWebApp/ViewProfile?${username}">${username}</a> |
        </c:if>
        <a href="resources/help/index.html" target="_blank">Help</a>
    </span>
</div>
