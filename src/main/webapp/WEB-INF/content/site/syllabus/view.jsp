<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${isInstructor}">
<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li>Syllabus</li>
<li class="align_right"><a href="./syllabus/edit"><img title="Edit Syllabus"
  alt="[Edit Syllabus]" src="<c:url value='/img/icons/script_edit.png' />" /></a></li>
</ul>
</c:if>

${syllabus.text}
