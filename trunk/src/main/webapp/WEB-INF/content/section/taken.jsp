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
  <li>Sections</li>
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
    <td>
      <table class="viewtable">
        <tr><th>Assignment</th><th>Due Date</th></tr>
        <c:forEach items="${section.assignments}" var="assignment">
        <tr>
          <td>
            <a href="<c:url value='/submission/view?assignmentId=${assignment.id}' />">${assignment.name}</a>
          </td>
          <td class="duedate"><csns:dueDate assignment="${assignment}" /></td>
        </tr>
        </c:forEach>
      </table>
    </td>
  </tr>
  <tr class="rowtypeb">
    <td><a href="downloadZip.html?sectionId=${section.id}">Download All Files</a></td>
  </tr>
</table>
</c:forEach>
