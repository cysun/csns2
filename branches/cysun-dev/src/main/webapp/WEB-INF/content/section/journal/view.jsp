<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="course" value="${section.course}" />
<c:set var="journal" value="${section.journal}" />

<script>
function submitJournal()
{
    var msg = "Do you want to submit this course journal for review?";
    if( confirm(msg) )
        window.location.href = "submit?sectionId=${section.id}";
}
</script>

<ul id="title">
<li><a href="../taught?quarter=${section.quarter.code}#section-${section.id}"
       class="bc" >${section.course.code} - ${section.number}</a></li>
<li>Course Journal</li>
<c:if test="${not journal.submitted}">
<li class="align_right"><a href="javascript:submitJournal()"><img title="Submit Course Journal"
  alt="[Submit Course Journal]" src="<c:url value='/img/icons/accept.png' />" /></a></li>
</c:if>
</ul>

<div class="journal-title">${course.code} ${course.name}</div>
<div class="journal-quarter">${section.quarter}</div>
<div class="journal-instructor">${section.instructors[0].name}</div>

<div class="journal-section-title">1.
 <c:if test="${not empty course.description}">
   <a href="<c:url value='/download?fileId=${course.description.id}' />">Course Description</a>
 </c:if>
 <c:if test="${empty course.description}">Course Description</c:if>
 <c:if test="${isCoordinator}">
 <div class="journal-section-operations">
   <a href="<c:url value='/course/edit?id=${course.id}' />"><img title="Upload Course Description"
      alt="[Upload Course Description]" src="<c:url value='/img/icons/upload.png' />" /></a>
 </div>
 </c:if>
</div>
<div class="journal-section-content">
<p>It is the responsibility of the course coordinator to maintain the course description
(a.k.a. ABET Syllabus).
<c:if test="${isCoordinator}">
As the coordinator of this course, you can click on <a
href="<c:url value='/course/edit?id=${course.id}' />"><img title="Upload Course Description"
alt="[Upload Course Description]" src="<c:url value='/img/icons/upload.png' />" /></a> to
upload a new course description.</c:if>
<c:if test="${not isCoordinator}"> 
Since you are not the coordinator of this course, you don't need to worry about this
section.</c:if></p>
</div>

<div class="journal-section-title">2.
 <c:if test="${not empty section.syllabus}">
   <a href="viewSyllabus?sectionId=${section.id}">Syllabus</a>
 </c:if>
 <c:if test="${empty section.syllabus}">Syllabus</c:if>
 <div class="journal-section-operations">
   <a href="<c:url value='editSyllabus?sectionId=${section.id}' />"><img title="Edit Syllabus"
      alt="[Edit Syllabus]" src="<c:url value='/img/icons/script_edit.png' />" /></a>
 </div>
</div>
<div class="journal-section-content">
<p>You can click on <a href="<c:url value='editSyllabus?sectionId=${section.id}' />"><img
title="Edit Syllabus" alt="[Edit Syllabus]" src="<c:url value='/img/icons/script_edit.png' />" /></a>
to create or update your syllabus.</p>
</div>

<div class="journal-section-title">3. Handouts
  <div class="journal-section-operations">
    <a href="handouts?sectionId=${section.id}"><img title="Edit Handouts" alt="[Edit Handouts]"
       src="<c:url value='/img/icons/table_edit.png' />" /></a>
  </div>
</div>
<div class="journal-section-content">
<p>Handouts typically consist of teaching materials such as lecture notes. You can click on
<a href="handouts?sectionId=${section.id}"><img title="Edit Handouts" alt="[Edit Handouts]"
src="<c:url value='/img/icons/table_edit.png' />" /></a> to add or edit your handouts.</p>

<c:if test="${fn:length(journal.handouts) > 0}">
<ul>
  <c:forEach items="${journal.handouts}" var="handout">
  <li><a href="<c:url value='/resource/view?id=${handout.id}' />">${handout.name}</a></li>
  </c:forEach>
</ul>
</c:if>
</div>

<div class="journal-section-title">4. Assignments
  <div class="journal-section-operations">
    <a href="assignments?sectionId=${section.id}"><img title="Edit Assignments" alt="[Edit Assignments]"
       src="<c:url value='/img/icons/table_edit.png' />" /></a>
  </div>
</div>
<div class="journal-section-content">
<p>You can click on
<a href="assignments?sectionId=${section.id}"><img title="Edit Assignments" alt="[Edit Assignments]"
src="<c:url value='/img/icons/table_edit.png' />" /></a> to select which assignments should
be included or excluded. By default all the assignments will be included.</p>
<c:if test="${fn:length(journal.assignments) > 0}">
<ul>
  <c:forEach items="${journal.assignments}" var="assignment">
  <li>
  <c:choose>
    <c:when test="${assignment.online}">
      <a href="<c:url value='/assignment/online/view?id=${assignment.id}' />">${assignment.name}</a>
    </c:when>
    <c:when test="${not assignment.online and assignment.description != null}">
      <a href="<c:url value='/assignment/view?id=${assignment.id}' />">${assignment.name}</a>
    </c:when>
    <c:otherwise>${assignment.name}</c:otherwise>
  </c:choose>
  </li>
  </c:forEach>
</ul>
</c:if>
</div>

<div class="journal-section-title">5. Student Samples
  <div class="journal-section-operations">
    <a href="samples?sectionId=${section.id}"><img title="Edit Student Samples"
       alt="[Edit Student Samples]" src="<c:url value='/img/icons/table_edit.png' />" /></a>
  </div>
</div>
<div class="journal-section-content">
<p>Each course journal should include <em>three</em> student samples. You can click on
<a href="samples?sectionId=${section.id}"><img title="Edit Student Samples"
alt="[Edit Student Samples]" src="<c:url value='/img/icons/table_edit.png' />" /></a>
to select the students. It is recommended that you select a good student, an average
student, and a bad student to represent the class.</p>
<c:if test="${fn:length(journal.studentSamples) > 0}">
<ul>
  <c:forEach items="${journal.studentSamples}" var="enrollment">
  <li><a href="../grade?enrollmentId=${enrollment.id}">${enrollment.student.name}<c:if
    test="${not empty enrollment.grade}"> (${enrollment.grade.symbol})</c:if></a></li>
  </c:forEach>
</ul>
</c:if>
</div>
