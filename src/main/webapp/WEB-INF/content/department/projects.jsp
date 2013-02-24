<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
    $("select[name='year'] option").each(function(){
       if( $(this).val() == ${year}) 
           $(this).attr('selected', true);
    });
    $("select[name='year']").change(function(){
        var year = $("select[name='year'] option:selected").val();
        window.location.href = "projects?year=" + year;
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/project/search' />">Projects</a></li>
<li>${department.name}</li>
<li class="align_right">
  <select class="formselect" name="year">
    <c:forEach var="y" items="${years}"><option value="${y}">${y}</option></c:forEach>
  </select>
</li>
<security:authorize access="authenticated and principal.isFaculty('${dept}')">
<li class="align_right"><a href="project/add?year=${year}"><img alt="[Add Project]"
  title="Add Project" src="<c:url value='/img/icons/brick_add.png' />" /></a></li>
</security:authorize>
</ul>

<c:if test="${fn:length(projects) > 0}">
<table class="viewtable">
<tr>
  <th>Project</th><th>Students</th><th>Advisors</th>
</tr>
<c:forEach items="${projects}" var="project">
<tr <c:if test="${not project.published}">style="color: gray;"</c:if>>
  <td><a href="project/view?id=${project.id}">${project.name}</a></td>
  <td style="width: 250px;">
    <c:forEach items="${project.students}" var="student" varStatus="status">
      ${student.name}<c:if test="${not status.last}">, </c:if>
    </c:forEach>
  </td>
  <td style="width: 100px;">
    <c:forEach items="${project.advisors}" var="advisor" varStatus="status">
      ${advisor.name}<c:if test="${not status.last}">, </c:if>
    </c:forEach>
  </td>
</tr>
</c:forEach>
</table>
</c:if>
