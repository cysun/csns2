<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="site" value="${section.site}" />

<script>
$(function(){
    document.title = "${section.course.code}-${section.number} ${section.quarter.shortString}";
});
</script>

<c:if test="${isInstructor}">
<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li>${section.course.code}-${section.number}</li>
<li class="align_right"><a href="<c:url value='${section.siteUrl}/block/list' />"><img title="Edit Blocks"
  alt="[Edit Blocks]" src="<c:url value='/img/icons/bricks.png' />" /></a></li>
<li class="align_right"><a href="<c:url value='${section.siteUrl}/info/list' />"><img title="Edit Class Info"
  alt="[Edit Class Info]" src="<c:url value='/img/icons/table.png' />" /></a></li>
<li class="align_right"><a href="<c:url value='${section.siteUrl}/syllabus/edit' />"><img title="Edit Syllabus"
  alt="[Edit Syllabus]" src="<c:url value='/img/icons/script_edit.png' />" /></a></li>
<li class="align_right"><a href="<c:url value='${section.siteUrl}/files/' />"><img title="Files"
  alt="[Files]" src="<c:url value='/img/icons/file_manager.png' />" /></a></li>
<li class="align_right"><a href="<c:url value='${section.siteUrl}/settings/' />"><img title="Settings"
  alt="[Settings]" src="<c:url value='/img/icons/setting_tools.png' />" /></a></li>
</ul>
</c:if>

<div class="site-title">${section.course.code} ${section.course.name}</div>
<div class="site-quarter">${section.quarter}</div>

<table id="site" style="width: 100%;">
<tr>
<!-- sidebar -->
<td id="site-sidebar" colspan="1" rowspan="2" class="shrink">
<ul>
  <c:if test="${not empty section.syllabus}">
  <li><a href="<c:url value='${section.siteUrl}/syllabus' />">Syllabus</a></li>
  </c:if>
  <c:if test="${not empty dept}">
  <li><a href="<c:url value='/department/${dept}/forum/view?id=${section.course.forum.id}' />">Class Forum</a></li>
  </c:if>
  <c:forEach items="${site.blocks}" var="block">
    <c:if test="${not block.hidden}">
    <li><a href="#b${block.id}">${block.name}</a></li>
    </c:if>
  </c:forEach>
</ul>
</td> <!-- end of sidebar -->

<!-- class info -->
<td>
<c:if test="${fn:length(site.infoEntries) > 0}">
<table id="site-class-info" class="general">
  <c:forEach items="${site.infoEntries}" var="infoEntry">
  <tr>
    <th>${infoEntry.name}</th>
    <td>${infoEntry.value}</td>
  </tr>
  </c:forEach>
</table>
</c:if>
</td> <!-- end of class info -->
</tr>

<tr>
<!-- blocks -->
<td id="site-content">
<c:forEach items="${site.blocks}" var="block">
<c:if test="${not block.hidden}">
<a id="b${block.id}"></a>
<div class="site-block">
<div class="site-block-title">${block.name}</div>
<div class="site-block-content">
  <%-- Regular Block --%>
  <c:if test="${block.type == 'REGULAR'}">
  <ul>
    <c:forEach items="${block.items}" var="item">
      <c:if test="${not item.hidden}">
      <li><a href="<c:url value='${section.siteUrl}/item/${item.id}' />">${item.name}</a></li>
      </c:if>
    </c:forEach>
  </ul>
  </c:if>
  <%-- Assignment Block --%>
  <c:if test="${block.type == 'ASSIGNMENTS'}">
  <ul>
    <c:choose>
      <c:when test="${isInstructor}">
        <c:forEach items="${section.assignments}" var="assignment">
          <li><a href="<c:url value='/section/taught#section-${section.id}' />">${assignment.name}</a>,
            Due: <csns:dueDate date="${assignment.dueDate.time}" datePast="${assignment.pastDue}"
            datePattern="EEEE, MMMM dd" /></li>
        </c:forEach>
        <c:forEach items="${section.rubricAssignments}" var="assignment">
          <li><a href="<c:url value='/section/taught#section-${section.id}' />">${assignment.name}</a>,
            Due: <csns:dueDate date="${assignment.dueDate.time}" datePast="${assignment.pastDue}"
            datePattern="EEEE, MMMM dd" /></li>
        </c:forEach>
      </c:when>
      <c:when test="${isStudent}">
        <c:forEach items="${section.assignments}" var="assignment">
          <c:if test="${assignment.published}">
          <li><a href="<c:url value='/section/taken#section-${section.id}' />">${assignment.name}</a>,
            Due: <csns:dueDate date="${assignment.dueDate.time}" datePast="${assignment.pastDue}"
            datePattern="EEEE, MMMM dd" /></li>
          </c:if>
        </c:forEach>
        <c:forEach items="${section.rubricAssignments}" var="assignment">
          <c:if test="${assignment.evaluatedByStudents and assignment.published}">
          <li><a href="<c:url value='/section/taught#section-${section.id}' />">${assignment.name}</a>,
            Due: <csns:dueDate date="${assignment.dueDate.time}" datePast="${assignment.pastDue}"
            datePattern="EEEE, MMMM dd" /></li>
          </c:if>
        </c:forEach>
      </c:when>
      <c:otherwise>
        <c:forEach items="${section.assignments}" var="assignment">
          <c:if test="${assignment.published}">
          <li>${assignment.name}, Due: <csns:dueDate date="${assignment.dueDate.time}"
            datePast="${assignment.pastDue}" datePattern="EEEE, MMMM dd" /></li>
          </c:if>
        </c:forEach>
      </c:otherwise>
    </c:choose>
  </ul>
  </c:if>
  <%-- Announcement Block --%>
  <c:if test="${block.type == 'ANNOUNCEMENTS'}">
  <table>
    <c:forEach items="${site.announcements}" var="announcement">
    <tr>
      <td style="padding: 0 0.5em 0 1em">
        <p><fmt:formatDate value="${announcement.date}" pattern="M/dd" /></p>
      </td>
      <td style="padding: 0 0.5em 0 1em">${announcement.content}</td>
    </tr>
    </c:forEach>
  </table>
  </c:if>
</div>
</div>
</c:if>
</c:forEach>
</td> <!-- end of blocks -->
</tr>
</table>
