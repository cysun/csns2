<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="section" value="${gradeSheet.section}"/>

<script>
$(function(){
   $("table").tablesorter({
      sortList: [[0,0]]
   });
   $("select").each(function(){
       $(this).change(function(event){
           $("#span-"+event.target.id).load("grade?enrollmentId=" + event.target.id + "&gradeId=" + $(this).val());
           $("#span-"+event.target.id).addClass("bold");
       });
   });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught' />">${section.quarter}</a></li>
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li>Students</li>
</ul>

<table class="outer_viewtable">
<tr><td>
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
  <c:set var="enrollment" value="${studentGrade.key}" />
    <tr>
      <td><a href="grade?enrollmentId=${enrollment.id}">${enrollment.student.lastName},
        ${enrollment.student.firstName}</a></td>
      <td class="center">
        <span id="span-${enrollment.id}">
          <c:if test="${empty enrollment.grade}">
            <select id="${enrollment.id}">
              <option value="-">-</option>
              <c:forEach items="${grades}" var="grade">
                <option value="${grade.id}">${grade.symbol}</option>
              </c:forEach>
            </select>
          </c:if>
          <c:if test="${not empty enrollment.grade and enrollment.gradeMailed}">${enrollment.grade.symbol}</c:if>
          <c:if test="${not empty enrollment.grade and not enrollment.gradeMailed}"><b>${enrollment.grade.symbol}</b></c:if>
        </span>
      </td>
      <c:forEach items="${studentGrade.value}" var="grade">
        <td class="center">${grade}</td>
      </c:forEach>
    </tr>
  </c:forEach>
</tbody>
</table>
</td></tr>
<tr class="rowtypeb">
  <td>
    <a href="email?sectionId=${section.id}">Email Grades</a> |
    <a href="export?sectionId=${section.id}&amp;format=excel">Export to Excel</a>
  </td>
</tr>
</table>
