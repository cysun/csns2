<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
$(function(){
    $("#add").click(function(){
        window.location.href = "add";
    });
    $("#search").autocomplete({
        source: "<c:url value='/course/autocomplete' />",
        select: function(event, ui) {
            if( ui.item )
                window.location.href = "view?id=" + ui.item.id;
        }
    });
});
</script>

<ul id="title">
<li>Courses</li>
<li class="align_right"><a href="add"><img title="Add" alt="[Add]"
    src="<c:url value='/img/icons/table_add.png' />" /></a></li>
</ul>

<form action="search" method="get">
<p><input id="search" name="term" type="text" class="forminput" size="40" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>

<c:if test="${not empty courses}">
<table class="viewtable">
<tr><th>Code</th><th>Name</th><th>Coordinator</th><th></th></tr>
<c:forEach items="${courses}" var="course">
<tr>
  <td>${course.code}</td>
  <td><a href="view?id=${course.id}">${course.name}</a></td>
  <td class="center">${course.coordinator.name}</td>
  <td class="center">
    <a href="edit?id=${course.id}"><img title="Edit" alt="[Edit]"
       src="<c:url value='/img/icons/table_edit.png' />" /></a>
  </td>
</tr>
</c:forEach>
</table>
</c:if>
