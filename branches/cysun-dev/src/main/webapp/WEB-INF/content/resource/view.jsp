<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
<%-- view assignment in course journal --%>
<c:when test="${view == 'CourseJournal'}">
<ul id="title">
<li><a href="list" class="bc">Course Journals</a></li>
<li><a href="view?id=${assignment.section.journal.id}#assignments"
       class="bc">${assignment.section.course.code}</a></li>
<li>${assignment.name}</li>
</ul>
</c:when>
</c:choose>

${resource.text}
