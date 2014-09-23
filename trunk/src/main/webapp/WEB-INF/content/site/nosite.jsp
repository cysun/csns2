<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty section}">
<p>The class website for <em>${section.course.code} Section ${section.number},
${section.quarter}</em> does not exist.
<c:if test="${isInstructor}">
Do you want to create this site?
<a href="<c:url value='/site/create?sectionId=${section.id}&new=true' />">Yes</a> |
<a href="<c:url value='/section/taught/' />">No</a>
</c:if>
</p>
</c:if>

<c:if test="${empty section}">
<p>Cannot find the class section you are looking for. Please check if the URL
is correct.</p>
</c:if>
