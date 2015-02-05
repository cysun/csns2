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
        window.location.href = "evaluated?quarter=" + quarter;
    });
    $(".viewtable").tablesorter({
        headers: { 3: {sorter: false} }
    });
});
</script>

<ul id="title">
  <li>Evaluator's Home</li>
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
    <td>${section.course.code} ${section.course.name} - ${section.number}</td>
  </tr>
  <tr> 
    <td colspan="2">
      <table class="viewtable">
        <thead>
        <tr>
          <th>Assignment</th><th class="datetime">Due Date</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${section.rubricAssignments}" var="assignment">
        <c:if test="${assignment.isExternalEvaluator(user)}">
        <tr>
          <td><a href="<c:url value='/rubric/submission/evaluator/list?assignmentId=${assignment.id}' />">${assignment.name}</a></td>
          <td class="datetime"><csns:dueDate date="${assignment.dueDate.time}"
              datePast="${assignment.pastDue}" /></td>
        </tr>
        </c:if>
        </c:forEach>
        </tbody>
      </table>
    </td>
  </tr>
</table>
</c:forEach>
