<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
<%-- view syllabus on class site --%>
<c:when test="${view == 'site' and isInstructor}">
<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li>Syllabus</li>
<li class="align_right"><a href="./syllabus/edit"><img title="Edit Syllabus"
  alt="[Edit Syllabus]" src="<c:url value='/img/icons/script_edit.png' />" /></a></li>
</ul>
</c:when>

<%-- view syllabus in course journal as instructor --%>
<c:when test="${view == 'journal1'}">
<ul id="title">
<li><a href="<c:url value='/section/taught?quarter=${section.quarter.code}#section-${section.id}' />"
       class="bc" >${section.course.code} - ${section.number}</a></li>
<li><a href="<c:url value='/section/journal/view?sectionId=${section.id}' />"
      class="bc">Course Journal</a></li>
<li>Syllabus</li>
<li class="align_right"><a href="./syllabus/edit"><img title="Edit Syllabus"
  alt="[Edit Syllabus]" src="<c:url value='/img/icons/script_edit.png' />" /></a></li>
</ul>
</c:when>

<%-- view syllabus in course journal as a reviewer or department chair --%>
<c:when test="${view == 'journal2'}">
<ul id="title">
<li><a href="list" class="bc">Course Journals</a></li>
<li><a href="view?id=${section.journal.id}" class="bc">${section.course.code}</a></li>
<li>Syllabus</li>
</ul>
</c:when>
</c:choose>

${syllabus.text}
