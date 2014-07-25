<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<li><a class="bc" href="list">Rubrics</a></li>
<li><a class="bc" href="view?id=${rubric.id}"><csns:truncate
  value="${rubric.name}" length="50" /></a></li>
<li>Results</li>
</ul>

<c:if test="${fn:length(mappedSections) == 0}">
<p>No results yet.</p>
</c:if>

<c:if test="${fn:length(mappedSections) > 0}">
<table class="viewtable autowidth">
<tr><th>Course</th><th>Sections</th></tr>
<c:forEach items="${mappedSections}" var="mappedSection">
<tr>
  <td class="center">
    <a href="results.html?rubricId=${rubric.id}&amp;courseId=${mappedSection.key.id}">${mappedSection.key.code}</a>
  </td>
  <td>
  <c:forEach items="${mappedSection.value}" var="section">
    <a href="results.html?rubricId=${rubric.id}&amp;sectionId=${section.id}">${section.quarter.shortString}</a>
  </c:forEach>
  </td>
</tr>
</c:forEach>
</table>
</c:if>
