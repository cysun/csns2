<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="section" value="${journal.section}" />
<c:set var="course" value="${section.course}" />

<script>
function approveJournal()
{
    var msg = "Do you want to approve this course journal?";
    if( confirm(msg) )
        window.location.href = "approve?id=${journal.id}";
}
function removeJournal()
{
    var msg = "Do you want to remove this course journal?";
    if( confirm(msg) )
        window.location.href = "reject?id=${journal.id}";
}
</script>

<ul id="title">
<li><a href="list" class="bc">Course Journals</a></li>
<li>${course.code}</li>
<c:if test="${not journal.approved}">
<li class="align_right"><a href="javascript:approveJournal()"><img title="Approve Course Journal"
  alt="[Approve Course Journal]" src="<c:url value='/img/icons/accept.png' />" /></a></li>
<li class="align_right"><a href="javascript:removeJournal()"><img title="Remove Course Journal"
  alt="[Remove Course Journal]" src="<c:url value='/img/icons/cross.png' />" /></a></li>
<li class="align_right"><a href="<c:url value='/email/compose?userId=${section.instructors[0].id}' />"><img
  title="Email Instructor" alt="[Email Instructor]" src="<c:url value='/img/icons/email_to_friend.png' />" /></a></li>
</c:if>
</ul>

<div class="journal-title">${course.code} ${course.name}</div>
<div class="journal-quarter">${section.quarter}</div>
<div class="journal-instructor">${section.instructors[0].name}</div>

<a id="description"></a>
<div class="journal-section-title">1.
 <c:if test="${not empty course.description}">
   <a href="<c:url value='/download?fileId=${course.description.id}' />">Course Description</a>
 </c:if>
 <c:if test="${empty course.description}">Course Description</c:if>
</div>

<a id="syllabus"></a>
<div class="journal-section-title">2.
 <c:if test="${not empty section.syllabus}">
   <a href="viewSyllabus?sectionId=${section.id}">Syllabus</a>
 </c:if>
 <c:if test="${empty section.syllabus}">Syllabus</c:if>
</div>

<a id="handouts"></a>
<div class="journal-section-title">3. Handouts</div>
<div class="journal-section-content">
<c:if test="${fn:length(journal.handouts) > 0}">
<ul>
  <c:forEach items="${journal.handouts}" var="handout">
  <li><a href="<c:url value='/resource/view?id=${handout.id}' />">${handout.name}</a></li>
  </c:forEach>
</ul>
</c:if>
</div>

<a id="assignments"></a>
<div class="journal-section-title">4. Assignments</div>
<div class="journal-section-content">
<c:if test="${fn:length(journal.assignments) > 0}">
<ul>
  <c:forEach items="${journal.assignments}" var="assignment">
  <li>
  <c:choose>
    <c:when test="${assignment.online}">
      <a href="viewOnlineAssignment?assignmentId=${assignment.id}">${assignment.name}</a>
    </c:when>
    <c:when test="${not assignment.online and assignment.description != null}">
      <a href="viewAssignment?assignmentId=${assignment.id}">${assignment.name}</a>
    </c:when>
    <c:otherwise>${assignment.name}</c:otherwise>
  </c:choose>
  </li>
  </c:forEach>
</ul>
</c:if>
</div>

<a id="students"></a>
<div class="journal-section-title">5. Student Samples</div>
<div class="journal-section-content">
<c:if test="${fn:length(journal.studentSamples) > 0}">
<ul>
  <c:forEach items="${journal.studentSamples}" var="enrollment">
  <li><a href="viewStudent?enrollmentId=${enrollment.id}">${enrollment.student.name}<c:if
    test="${not empty enrollment.grade}"> (${enrollment.grade.symbol})</c:if></a></li>
  </c:forEach>
</ul>
</c:if>
</div>
