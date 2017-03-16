<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
function testLocalStorage(){
    try
    {
        localStorage.setItem("test", "test");
        localStorage.removeItem("test");
        return true;
    }
    catch(e) {
        return false;
    }
}
function saveCourseIds(){
    if( ! testLocalStorage() ) return;

    var courseIds = [];
    $(".course").each(function(){
        courseIds.push($(this).attr("data-course-id"));
    });
    localStorage.setItem( "searchText", "${param.text}" );
    localStorage.setItem( "courseIds", courseIds.join() );
}
function removeCourseIds(){
    if( ! testLocalStorage() ) return;
    localStorage.removeItem( "searchText" );
    localStorage.removeItem( "courseIds" );
}
$(function(){
    $("table").tablesorter({
        sortList: [[0,0]]
    });
    $("table").bind("sortEnd", function(){
        saveCourseIds();
    });
    $("#search").autocomplete({
        source: "<c:url value='/autocomplete/course' />",
        select: function(event, ui) {
            if( ui.item )
                window.location.href = "view?id=" + ui.item.id;
        }
    });
    $(".course").each(function(){
        $(this).html( splitCode($(this).html()) );
    });
    if( $(".course").length > 0 )
        saveCourseIds();
    else
        removeCourseIds();
});
function splitCode( code )
{
    var parts = code.match("^([a-zA-Z]+)([0-9].*)$");
    return parts ? parts[1] + " " + parts[2] : code;
}
</script>

<ul id="title">
<li>Courses</li>
<security:authorize access="authenticated and principal.faculty">
<li class="align_right"><a href="create"><img title="Create Course" alt="[Create Course]"
    src="<c:url value='/img/icons/table_add.png' />" /></a></li>
</security:authorize>
</ul>

<form action="search" method="get">
<p><input id="search" name="text" type="text" class="forminput" size="40"
  value="${param.text}" />
<input name="search" type="submit" class="subbutton" value="Search" /></p>
</form>

<c:if test="${not empty courses}">
<table class="viewtable autowidth">
<thead><tr><th>Code</th><th>Name</th><th>Units</th><th>Unit Factor</th><th>Coordinator</th></tr></thead>
<tbody>
<c:forEach items="${courses}" var="course">
<tr>
  <td class="course" data-course-id="${course.id}">${course.code}</td>
  <td><a href="view?id=${course.id}">${course.name}</a></td>
  <td class="center">${course.units}</td>
  <td class="center">${course.unitFactor}</td>
  <td class="center">${course.coordinator.name}</td>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
