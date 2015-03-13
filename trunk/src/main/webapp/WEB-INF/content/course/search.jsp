<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
    $("#search").autocomplete({
        source: "<c:url value='/autocomplete/course' />",
        select: function(event, ui) {
            if( ui.item )
                window.location.href = "view?id=" + ui.item.id;
        }
    });
});
</script>

<ul id="title">
<li>Courses</li>
<security:authorize access="authenticated and principal.admin">
<li class="align_right"><a href="create"><img title="Create Course" alt="[Create Course]"
    src="<c:url value='/img/icons/table_add.png' />" /></a></li>
</security:authorize>
</ul>

<form action="search" method="get">
<p><input id="search" name="term" type="text" class="forminput" size="40"
  value="${param.term}" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>

<c:if test="${not empty courses}">
<table class="viewtable autowidth">
<tr><th>Code</th><th>Name</th><th>Coordinator</th></tr>
<c:forEach items="${courses}" var="course">
<tr>
  <td>${course.code}</td>
  <td><a href="view?id=${course.id}">${course.name}</a></td>
  <td class="center">${course.coordinator.name}</td>
</tr>
</c:forEach>
</table>
</c:if>
