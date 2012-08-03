<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="section" value="${gradeSheet.section}"/>

<script>
$(function(){
   $("table").tablesorter({
      sortList: [[0,0]]
   });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught' />">${section.quarter}</a></li>
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li>Students</li>
</ul>

<table class="viewtable">
  <thead>
  <tr>
    <th>Name</th><th>Grade</th>
    <c:forEach items="${section.assignments}" var="assignment">
      <th>${assignment.alias}</th>
    </c:forEach>
  </tr>
  </thead>
  <tbody>
  <c:forEach items="${gradeSheet.studentGrades}" var="studentGrade">
    <tr>
      <td>${studentGrade.key.student.lastName}, ${studentGrade.key.student.firstName}</td>
      <td>${studentGrade.key.grade.symbol}</td>
      <c:forEach items="${studentGrade.value}" var="grades">
        <td>${grade}</td>
      </c:forEach>
    </tr>
  </c:forEach>
  </tbody>
</table>
