<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
    $("table").tablesorter();
    $("select[name='quarter'] option").each(function(){
       if( $(this).val() == ${quarter.code}) 
           $(this).attr('selected', true);
    });
    $("select[name='quarter']").change(function(){
        var quarter = $("select[name='quarter'] option:selected").val();
        window.location.href = "sections?quarter=" + quarter;
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/search' />">Sections</a></li>
<li>${department.name}</li>
<li class="align_right">
  <select class="formselect" name="quarter">
    <c:forEach var="q" items="${quarters}"><option value="${q.code}">${q}</option></c:forEach>
  </select>
</li>
<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<li class="align_right"><a href="section/import?quarter=${quarter.code}"><img alt="[Import Section]"
  title="Import Section" src="<c:url value='/img/icons/table_import.png' />" /></a></li>
</security:authorize>
</ul>

<c:if test="${fn:length(sections) > 0}">
<table class="viewtable">
<thead>
<tr>
  <th>Code</th><th>Name</th><th>Instructor</th>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
    <th></th>
  </security:authorize>
</tr>
</thead>
<tbody>
<c:forEach items="${sections}" var="section">
<tr>
  <td>
    ${section.course.code}
    <c:if test="${section.number != 1}">(${section.number})</c:if>
  </td>
  <td>
    <c:choose>
      <c:when test="${empty section.site}">${section.course.name}</c:when>
      <c:otherwise>
        <a href="<c:url value='${section.siteUrl}' />">${section.course.name}</a>
      </c:otherwise>
    </c:choose>
  </td>
  <td>
    <c:forEach items="${section.instructors}" var="instructor" varStatus="status">
      ${instructor.name}<c:if test="${not status.last}">, </c:if>
    </c:forEach>
  </td>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <td class="center"><a href="section?id=${section.id}"><img
    title="Grade Sheet" alt="[Grade Sheet]" src="<c:url value='/img/icons/table.png' />" /></a></td>
  </security:authorize>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
