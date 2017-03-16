<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
$(function(){
    $(".prev,.next").hide();
    if( testLocalStorage() && localStorage.getItem("courseIds") != null )
    {
        var courseIds = localStorage.getItem("courseIds").split(",");
        var currentIndex = courseIds.indexOf("${course.id}");
        if( currentIndex > 0 )
          $(".prev").show().click(function(){
              window.location.href = "view?id=" + courseIds[currentIndex-1];
          });
        if( currentIndex >=0 && currentIndex < courseIds.length-1 )
          $(".next").show().click(function(){
              window.location.href = "view?id=" + courseIds[currentIndex+1];
          });
    }
});
</script>
<ul id="title">
<li><a class="bc" href="<c:url value='/course/search' />">Courses</a></li>
<c:if test="${not empty dept}">
  <li><a class="bc" href="<c:url value='/department/${dept}/courses' />">${fn:toUpperCase(dept)}</a></li>
</c:if>
<li>${course.code}</li>
<security:authorize access="authenticated and principal.faculty">
<li class="align_right"><a href="edit?id=${course.id}"><img title="Edit Course" alt="[Edit Course]"
    src="<c:url value='/img/icons/table_edit.png' />" /></a></li>
</security:authorize>
</ul>

<p>
<c:if test="${course.obsolete}"><span class="error">This course is obsolete.</span></c:if>
<span style="float: right; margin-bottom: 5px;">
<button class="prev subbutton">Prev</button>
<button class="next subbutton">Next</button>
</span> 
</p>
<table class="general autowidth">
  <tr>
    <th>Department</th>
    <td colspan="3">${course.department.name}</td>
  <tr>
    <th>Code</th>
    <td>${course.code}</td>
    <th class="shrink">Name</th>
    <td>${course.name}</td>
  </tr>
  <tr>
    <th>Units</th>
    <td>${course.units}</td>
    <th class="shrink">Unit Factor</th>
    <td>${course.unitFactor}</td>
  </tr>
  <tr>
    <th>Coordinator</th>
    <td>${course.coordinator.name}</td>
    <th class="shrink">Description</th>
    <td>
      <c:if test="${course.description != null}">
      <a href="<c:url value='/download?fileId=${course.description.id}' />">View</a>
      </c:if>
    </td>
  </tr>
  <tr>
    <th>Prerequisites</th>
    <td colspan="3">
      <c:forEach items="${course.prerequisites}" var="prerequisite" varStatus="status">
        <a href="view?id=${prerequisite.id}">${prerequisite.code}</a><c:if
          test="${not status.last}">,</c:if>
      </c:forEach>
    </td>
  </tr>
  <tr>
    <th>Catalog Description</th>
    <td colspan="3">${course.catalogDescription}</td>
  </tr>
</table>
