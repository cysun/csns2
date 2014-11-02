<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${submission.assignment.section}" />

<ul id="title">
<li><a href="list" class="bc">Course Journals</a></li>
<li><a href="view?id=${section.journal.id}#students"
       class="bc">${section.course.code}</a></li>
<li><a href="viewStudent?enrollmentId=${param.enrollmentId}"
       class="bc">${submission.student.name}</a></li>
<li>${submission.assignment.name}</li>
</ul>

<c:if test="${fn:length(submission.files) > 0}">
<table class="viewtable">
<thead><tr><th>Name</th><th class="shrink">Size</th><th>Date</th></tr></thead>
<tbody>
  <c:forEach items="${submission.files}" var="file">
  <tr>
    <td><a href="<c:url value='/download?fileId=${file.id}' />">${file.name}</a></td>
    <td class="shrink"><csns:fileSize value="${file.size}" /></td>
    <td class="datetime"><fmt:formatDate value="${file.date}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>

<h4>Grade</h4>
<div style="margin-left: 20px;">${submission.grade}</div>

<h4>Comments</h4>
<pre><c:out value="${submission.comments}" escapeXml="true" /></pre>
