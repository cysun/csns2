<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<li><a href="list" class="bc">Program Assessment</a></li>
<li><a href="view?id=${program.id}" class="bc"><csns:truncate value="${program.name}" length="50" /></a></li>
<li>Educational Objectives</li>
<li class="align_right"><a href="addObjective?programId=${program.id}"><img alt="[Add Objective]"
  title="Add Objective" src="<c:url value='/img/icons/page_add.png' />" /></a></li>
</ul>

<table class="general autowidth" style="margin-left: 10px">
<tbody id="sortables">
<c:forEach items="${program.objectives}" var="objective" varStatus="status">
<tr>
  <th>${status.index+1}</th>
  <td>${objective.text}</td>
  <td class="center">
    <a href="editObjective?id=${objective.id}"><img title="Edit" alt="[Edit]" border="0"
       src="<c:url value='/img/icons/page_edit.png' />" /></a>
  </td>
</tr>
</c:forEach>
</tbody>
</table>
