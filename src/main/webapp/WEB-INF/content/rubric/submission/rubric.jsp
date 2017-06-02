<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="rubric" value="${assignment.rubric}" />
<c:set var="section" value="${assignment.section}" />

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taken#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="view?assignmentId=${assignment.id}">${assignment.name}</a></li>
<li>Rubric</li>
</ul>

<c:if test="${rubric.obsolete}"><p class="error">This rubric is obsolete.</p></c:if>

${rubric.description}

<table class="viewtable">
<thead>
  <tr>
    <c:forEach begin="1" end="${rubric.scale}" step="1" var="rank">
    <th>${rank}</th>
    </c:forEach>
  </tr>
</thead>
<tbody>
<c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
  <tr id="${indicator.id}">
    <td colspan="${rubric.scale}" class="indicator-name">
      ${indicator.name}
    </td>
  </tr>
  <tr>
  <c:forEach begin="0" end="${rubric.scale-1}" step="1" var="index">
    <td class="indicator-criterion">${indicator.criteria[index]}</td>
  </c:forEach>
  </tr>
</c:forEach>
</tbody>
</table>
