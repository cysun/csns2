<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("#prev").click(function(){
        window.location.href = "results?id=${survey.id}&sectionIndex=${sectionIndex-1}"; 
    });
    $("#next").click(function(){
        window.location.href = "results?id=${survey.id}&sectionIndex=${sectionIndex+1}"; 
    });
    $("#ok").click(function(){
        window.location.href = "list"; 
    });
});
</script>

<ul id="title">
<li><a class="bc" href="list">Rubrics</a></li>
<li><a class="bc" href="view?id=${rubric.id}"><csns:truncate
  value="${rubric.name}" length="50" /></a></li>
<li>Result Summary</li>
</ul>

<c:if test="${fn:length(mappedSections) == 0}">
<p>No results yet.</p>
</c:if>

<c:if test="${fn:length(mappedSections) > 0}">
<table class="viewtable autowidth">
<tr><th>Course</th><th>Sections</th></tr>
<c:forEach items="${mappedSections}" var="mappedSection">
<tr>
  <td class="center">${mappedSection.key.code}</td>
  <td>
  <c:forEach items="${mappedSection.value}" var="section">
    ${section.quarter.shortString}
  </c:forEach>
  </td>
</tr>
</c:forEach>
</table>
</c:if>
