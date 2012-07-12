<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script>
function deleteCourse( courseId )
{
    var message = "Do you want to mark this course as obsolete?";
    if( confirm(message) )
        window.location.href = "delete?id=" + courseId;
}
</script>

<ul id="title">
<li><a class="bc" href="search">Courses</a></li>
<li>${course.code}</li>
<li class="align_right"><a href="javascript:deleteCourse(${course.id})"><img title="Delete"
    alt="[Delete]" src="<c:url value='/img/icons/table_delete.png' />" /></a></li>
<li class="align_right"><a href="edit?id=${course.id}"><img title="Edit" alt="[Edit]"
    src="<c:url value='/img/icons/table_edit.png' />" /></a></li>
</ul>

<table class="general">
  <tr>
    <th>Code</th>
    <td>${course.code}</td>
  </tr>
  <tr>
    <th>Name</th>
    <td>${course.name}</td>
  </tr>
  <tr>
    <th>Coordinator</th>
    <td>${course.coordinator.name}</td>
  </tr>
</table>
