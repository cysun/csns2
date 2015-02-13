<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

 <script>
$(function(){
    $("#major").change(function(){
        window.location.href = "setMajor?userId=${user.id}&majorId=" + $(this).val();
    });
    $("#program").change(function(){
        window.location.href = "setProgram?userId=${user.id}&programId=" + $(this).val();
    });
    $("table").tablesorter({
        sortList: [[0,0]]
    });
});
</script>

<form:form modelAttribute="user">
<table class="general autowidth">
<tr>
  <th>Major</th>
  <td>
    <form:select path="major">
      <form:option value="" label=""/>
      <form:options items="${departments}" itemValue="id" itemLabel="name"/>
    </form:select>
  </td>
</tr>  
<tr>
  <th>Program</th>
  <td>
    <form:select path="program">
      <form:option value="" label=""/>
      <form:options items="${programs}" itemValue="id" itemLabel="name"/>
    </form:select>
  </td>
</tr>
</table>
</form:form>

<c:if test="${not empty programStatus}">
${programStatus.program.description}

<h4>Required Courses</h4>
<table class="viewtable autowidth">
<thead>
<tr><th>Code</th><th>Name</th><th>Status</th><th colspan="3">Information</th></tr>
</thead>
<tbody>
<c:forEach items="${programStatus.requiredCourseStatuses}" var="courseStatus">
<tr>
  <td>${courseStatus.course.code}</td>
  <td>${courseStatus.course.name}</td>
  <td>${courseStatus.status}</td>
  <c:choose>
    <c:when test="${courseStatus.status == 'Taken'}">
    <td>${courseStatus.enrollment.section.course.code}</td>
    <td>${courseStatus.enrollment.section.quarter.shortString}</td>
    <td>${courseStatus.enrollment.grade.symbol}</td>
    </c:when>
    <c:when test="${courseStatus.status == 'Mapped'}">
    <td>${courseStatus.mappedEnrollment.section.course.code}</td>
    <td>${courseStatus.mappedEnrollment.section.quarter.shortString}</td>
    <td>${courseStatus.mappedEnrollment.grade.symbol}</td>
    </c:when>
    <c:otherwise>
      <td colspan="3"></td>
    </c:otherwise>
  </c:choose>
</tr>
</c:forEach>
</tbody>
</table>

<h4>Elective Courses</h4>
<table class="viewtable autowidth">
<thead>
<tr><th>Code</th><th>Name</th><th>Status</th><th colspan="3">Information</th></tr>
</thead>
<tbody>
<c:forEach items="${programStatus.electiveCourseStatuses}" var="courseStatus">
<tr>
  <c:if test="${not empty courseStatus.status}">
    <td>${courseStatus.course.code}</td>
    <td>${courseStatus.course.name}</td>
    <td>${courseStatus.status}</td>
  </c:if>
  <c:choose>
    <c:when test="${courseStatus.status == 'Taken'}">
    <td>${courseStatus.enrollment.section.course.code}</td>
    <td>${courseStatus.enrollment.section.quarter.shortString}</td>
    <td>${courseStatus.enrollment.grade.symbol}</td>
    </c:when>
    <c:when test="${courseStatus.status == 'Mapped'}">
    <td>${courseStatus.mappedEnrollment.section.course.code}</td>
    <td>${courseStatus.mappedEnrollment.section.quarter.shortString}</td>
    <td>${courseStatus.mappedEnrollment.grade.symbol}</td>
    </c:when>
  </c:choose>
</tr>
</c:forEach>
</tbody>
</table>

<h4>Other Courses</h4>
<table class="viewtable autowidth">
<thead>
  <tr><th>Code</th><th>Name</th><th colspan="2">Information</th></tr>
</thead>
<tbody>
  <c:forEach items="${programStatus.otherEnrollments}" var="enrollment">
  <tr>
    <td>${enrollment.section.course.code}</td>
    <td>${enrollment.section.course.name}</td>
    <td>${enrollment.section.quarter.shortString}</td>
    <td>${enrollment.grade.symbol}</td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
