<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

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
<tr><th>Course</th><th>Status</th></tr>
</thead>
<tbody>
<c:forEach items="${programStatus.requiredCourseStatuses}" var="courseStatus">
<tr>
  <td>
    <c:forEach items="${courseStatus.courses}" var="course">
    <div class="pstat">
      <div class="pstat-course-code">${course.code}</div>
      <div><csns:truncate value="${course.name}" length="55" /></div>
    </div>
    </c:forEach>
  </td>
  <td class="nowrap">
    <c:forEach items="${courseStatus.enrollments}" var="enrollment">
    <div class="pstat">
      <div class="pstat-course-code">${enrollment.section.course.code}</div>
      <div class="pstat-quarter">${enrollment.section.quarter.shortString}</div>
      <div>${enrollment.grade.symbol}</div>
    </div>
    </c:forEach>
    <c:forEach items="${courseStatus.courseSubstitutions}" var="substitution">
    <div class="pstat">
      <div class="pstat-course-code">${substitution.original.code}</div>
      <div>Substituted by ${substitution.substitute.code}</div>
    </div>
    </c:forEach>
    <c:forEach items="${courseStatus.courseTransfers}" var="transfer">
    <div class="pstat">
      <div class="pstat-course-code">${transfer.course.code}</div>
      <div>Transferred</div>
    </div>
    </c:forEach>
    <c:forEach items="${courseStatus.courseWaivers}" var="waiver">
    <div class="pstat">
      <div class="pstat-course-code">${waiver.course.code}</div>
      <div>Waived</div>
    </div>
    </c:forEach>
  </td>
</tr>
</c:forEach>
</tbody>
</table>

<h4>Elective Courses</h4>
<table class="viewtable autowidth">
<thead>
<tr><th>Course</th><th>Status</th></tr>
</thead>
<tbody>
<c:forEach items="${programStatus.electiveCourseStatuses}" var="courseStatus">
<tr>
  <td>
    <c:forEach items="${courseStatus.courses}" var="course">
    <div class="pstat">
      <div class="pstat-course-code">${course.code}</div>
      <div><csns:truncate value="${course.name}" length="55" /></div>
    </div>
    </c:forEach>
  </td>
  <td class="nowrap">
    <c:forEach items="${courseStatus.enrollments}" var="enrollment">
    <div class="pstat">
      <div class="pstat-course-code">${enrollment.section.course.code}</div>
      <div class="pstat-quarter">${enrollment.section.quarter.shortString}</div>
      <div>${enrollment.grade.symbol}</div>
    </div>
    </c:forEach>
    <c:forEach items="${courseStatus.courseSubstitutions}" var="substitution">
    <div class="pstat">
      <div class="pstat-course-code">${substitution.original.code}</div>
      <div>Substituted by ${substitution.substitute.code}</div>
    </div>
    </c:forEach>
    <c:forEach items="${courseStatus.courseTransfers}" var="transfer">
    <div class="pstat">
      <div class="pstat-course-code">${transfer.course.code}</div>
      <div>Transferred</div>
    </div>
    </c:forEach>
    <c:forEach items="${courseStatus.courseWaivers}" var="waiver">
    <div class="pstat">
      <div class="pstat-course-code">${waiver.course.code}</div>
      <div>Waived</div>
    </div>
    </c:forEach>
  </td>
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
