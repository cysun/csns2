<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("#resources tbody").sortable({
        update: function(event, ui) {
        	$.ajax({
                type: "POST",
                url:  "resource/reorder",
                data: {
                    "projectId" : "${project.id}",
                    "resourceId" : ui.item.attr("id"),
                    "newIndex" : ui.item.index()
                }
            });
        }
    }).disableSelection();
    $("#publish").click(function(){
        var msg = "Do you want to publish this project?";
        if( confirm(msg) )
            window.location.href = "publish?id=${project.id}";
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/project/search' />">Projects</a></li>
<li><a class="bc" href="../projects?year=${project.year}">${fn:toUpperCase(dept)}</a></li>
<li><csns:truncate value="${project.title}" length="80" /></li>
<c:if test="${project.isMember(user) or user.isFaculty(dept)}">
<li class="align_right"><a href="edit?id=${project.id}"><img alt="[Edit Project]"
  title="Edit Project" src="<c:url value='/img/icons/brick_edit.png' />" /></a></li>
</c:if>
</ul>

<div class="project-title">${project.title}</div>

<c:if test="${not empty project.sponsor}">
<div class="project-sponsor">Sponsored by ${project.sponsor}</div>
</c:if>

<div class="project-members">
  <c:forEach items="${project.students}" var="student" varStatus="status">
    ${student.name}<c:if test="${not status.last}">, </c:if>
  </c:forEach>
</div>

<div class="project-advisors"> Advisor: 
  <c:forEach items="${project.advisors}" var="advisor" varStatus="status">
    ${advisor.name}<c:if test="${not status.last}">, </c:if>
  </c:forEach>
</div>

<c:if test="${fn:length(project.liaisons) > 0}">
<div class="project-liaisons"> Liaison: 
  <c:forEach items="${project.liaisons}" var="liaison" varStatus="status">
    ${liaison.name}<c:if test="${not status.last}">, </c:if>
  </c:forEach>
</div>
</c:if>

${project.description}

<div class="project-resource-title">Resources
<c:if test="${project.isMember(user) or user.isFaculty(dept)}">
  <span style="float: right;"><a href="resource/add?projectId=${project.id}"><img alt="[Add Resource]"
    title="Add Resource" src="<c:url value='/img/icons/plugin_add.png' />" /></a></span>
</c:if>
</div>

<c:if test="${fn:length(project.resources) > 0}">
<table id="resources" class="viewtable autowidth">
<tbody>
  <c:forEach items="${project.resources}" var="resource">
  <tr id="${resource.id}">
    <td>
      <c:if test="${resource.isPrivate()}"><img border="0" alt="[Private Resource]"
          title="Private Resource" src="<c:url value='/img/icons/lock.png' />" /></c:if>
      <a href="resource/view?projectId=${project.id}&amp;resourceId=${resource.id}">${resource.name}</a>
    </td>
    <c:if test="${project.isMember(user) or user.isFaculty(dept)}">
    <td class="center">
      <a href="resource/edit?projectId=${project.id}&amp;resourceId=${resource.id}"><img alt="[Edit Resource]"
           title="Edit Resource" src="<c:url value='/img/icons/plugin_edit.png' />" /></a>
    </td>
    </c:if>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>

<c:if test="${not project.published}">
<p><button id="publish" class="subbutton">Publish</button></p>
</c:if>
