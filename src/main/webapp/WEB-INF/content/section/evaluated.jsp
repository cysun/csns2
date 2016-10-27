<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("select[name='term'] option").each(function(){
       if( $(this).val() == ${term.code}) 
           $(this).attr('selected', true);
    });
    $("select[name='term']").change(function(){
        var term = $("select[name='term'] option:selected").val();
        window.location.href = "evaluated?term=" + term;
    });
    $(".viewtable").tablesorter({
        headers: { 3: {sorter: false} }
    });
    $(".course-code").each(function(){
        $(this).html( splitCode($(this).html()) );
    });
});
function splitCode( code )
{
    var parts = code.match("^([a-zA-Z]+)([0-9].*)$");
    return parts ? parts[1] + " " + parts[2] : code;
}
</script>

<ul id="title">
  <li>Evaluator's Home</li>
  <li class="align_right">
    <select class="formselect" name="term">
      <c:forEach var="q" items="${terms}"><option value="${q.code}">${q}</option></c:forEach>
    </select>
  </li>
</ul>

<c:forEach var="section" items="${sections}">
<a id="section-${section.id}"></a>
<table class="outer_viewtable">
  <tr class="rowtypea">
    <td><span class="course-code">${section.course.code}</span>
      ${section.course.name} - ${section.number}</td>
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
