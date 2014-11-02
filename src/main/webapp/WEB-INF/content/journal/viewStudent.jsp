<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="section" value="${enrollment.section}" />

<ul id="title">
<li><a href="list" class="bc">Course Journals</a></li>
<li><a href="view?id=${section.journal.id}#students"
       class="bc">${section.course.code}</a></li>
<li>${enrollment.student.name}</li>
</ul>

<table class="viewtable autowidth">
<thead><tr><th>Assignment</th><th>Grade</th><th>Total</th></tr></thead>
<tbody>
  <c:forEach items="${submissions}" var="submission">
  <tr>
    <td><a href="viewSubmission?id=${submission.id}&amp;enrollmentId=${enrollment.id}">${submission.assignment.name}</a></td>
    <td class="center">${submission.grade}</td>
    <td class="center">${submission.assignment.totalPoints}</td>
  </tr>
  </c:forEach>
</tbody>
</table>

<h4>Grade</h4>
<div style="margin-left: 20px;">${enrollment.grade.symbol}</div>

<h4>Comments</h4>
<pre id="comments"><c:out value="${enrollment.comments}" escapeXml="true" /></pre>
