<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
$(function(){
    $(".assignment-toggle").change(function(){
        var url = "toggleAssignment";
        if($(this).attr("data-assignment-type") == "rubric")
            url = "toggleRubricAssignment";
        $.ajax({
           "url": url,
           data: {
               "journalId": ${section.journal.id},
               "assignmentId": $(this).attr("data-assignment-id")
           },
           cache: false
        });
    });
});
</script>

<ul id="title">
<li><a href="<c:url value='/section/taught?term=${section.term.code}#section-${section.id}' />"
       class="bc" >${section.course.code} - ${section.number}</a></li>
<li><a href="view?sectionId=${section.id}" class="bc">Course Journal</a></li>
<li>Assignments</li>
</ul>

<table class="viewtable autowidth">
  <tbody class="sortableElements">
  <c:forEach items="${section.assignments}" var="assignment">
  <tr data-assignment-id="${assignment.id}">
    <td>
      <input type="checkbox" class="assignment-toggle" data-assignment-id="${assignment.id}"
        <c:if test="${section.journal.assignments.contains(assignment)}">checked="checked"</c:if> />
    </td>
    <td>
    <c:choose>
      <c:when test="${assignment.online}">
        <a href="<c:url value='/assignment/online/view?id=${assignment.id}' />">${assignment.name}</a>
      </c:when>
      <c:when test="${not assignment.online and assignment.description != null}">
        <a href="<c:url value='/assignment/view?id=${assignment.id}' />">${assignment.name}</a>
      </c:when>
      <c:otherwise>${assignment.name}</c:otherwise>
    </c:choose>
    </td>
  </tr>
  </c:forEach>
  </tbody>
  <tbody>
  <c:forEach items="${section.rubricAssignments}" var="assignment">
  <tr data-assignment-id="${assignment.id}">
    <td>
      <input type="checkbox" class="assignment-toggle" data-assignment-id="${assignment.id}" data-assignment-type="rubric"
        <c:if test="${section.journal.rubricAssignments.contains(assignment)}">checked="checked"</c:if> />
    </td>
    <td>
      <a href="<c:url value='/department/${dept}/rubric/view?id=${assignment.rubric.id}' />">${assignment.name}</a>
    </td>
  </tr>
  </c:forEach>
  </tbody>
</table>
