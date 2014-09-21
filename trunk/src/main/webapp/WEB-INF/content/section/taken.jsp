<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("select[name='quarter'] option").each(function(){
       if( $(this).val() == ${quarter.code}) 
           $(this).attr('selected', true);
    });
    $("select[name='quarter']").change(function(){
        var quarter = $("select[name='quarter'] option:selected").val();
        window.location.href = "taken?quarter=" + quarter;
    });
});
</script>

<ul id="title">
  <li>Student's Home</li>
  <li class="align_right">
    <select class="formselect" name="quarter">
      <c:forEach var="q" items="${quarters}"><option value="${q.code}">${q}</option></c:forEach>
    </select>
  </li>
</ul>

<c:forEach var="section" items="${sections}">
<a id="section-${section.id}"></a>
<table class="outer_viewtable">
  <tr class="rowtypea">
    <td>
      <a href="<c:url value='${section.siteUrl}' />">${section.course.code}
         ${section.course.name} - ${section.number}</a>
    </td>
  </tr>
  <tr> 
    <td>
      <table class="viewtable">
        <tr><th>Assignment</th><th class="datetime">Due Date</th></tr>
<c:forEach items="${section.assignments}" var="assignment">
<c:if test="${assignment.published}">
  <c:choose>
    <c:when test="${not assignment.online}">
      <c:url var="link" value="/submission/view?assignmentId=${assignment.id}" />
    </c:when>
    <c:when test="${assignment.online and assignment.pastDue}">
      <c:url  var="link" value="/submission/online/view?assignmentId=${assignment.id}"/>
    </c:when>
    <c:otherwise>
      <c:url  var="link" value="/submission/online/edit?assignmentId=${assignment.id}"/>
    </c:otherwise>
  </c:choose>
        <tr>
          <td><a href="${link}">${assignment.name}</a></td>
          <td class="datetime"><csns:dueDate date="${assignment.dueDate.time}"
              datePast="${assignment.pastDue}" /></td>
        </tr>
</c:if>
</c:forEach>
        <c:forEach items="${section.rubricAssignments}" var="assignment">
        <c:if test="${assignment.evaluatedByStudents and assignment.published}">
        <tr>
          <td><a href="<c:url value='/rubric/submission/student/list?assignmentId=${assignment.id}' />">${assignment.name}</a></td>
          <td class="datetime"><csns:dueDate date="${assignment.dueDate.time}"
              datePast="${assignment.pastDue}" /></td>
        </tr>
        </c:if>
        </c:forEach>
      </table>
    </td>
  </tr>
  <tr class="rowtypeb">
    <td><a href="<c:url value='/download?sectionId=${section.id}' />">Download All Files</a></td>
  </tr>
</table>
</c:forEach>
