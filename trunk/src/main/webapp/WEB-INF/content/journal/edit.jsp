<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="section" value="${courseJournal.section}" />
<c:set var="course" value="${section.course}" />

<ul id="title">
<li><a href="list" class="bc">Course Journals</a></li>
<li>${course.code}</li>
<li class="align_right"><a href="edit?id=${courseJournal.id}"><img title="Edit Course Journal"
  alt="[Edit Course Journal]" src="<c:url value='/img/icons/report_edit.png' />" /></a></li>
</ul>

<div class="journal-title">${course.code} ${course.name}</div>
<div class="journal-quarter">${section.quarter}</div>
<div class="journal-instructor">${section.instructors[0].name}</div>

<div class="journal-section-title">1.
 <c:if test="${not empty course.syllabus}">
   <a href="<c:url value='/download?fileId=${course.syllabus.id}' />">Course Description</a>
 </c:if>
 <c:if test="${empty course.syllabus}">Course Description</c:if>
</div>

<div class="journal-section-title">2.
 <c:if test="${not empty section.syllabus}">
   <a href="viewResource?id=${section.syllabus.id}">Syllabus</a>
 </c:if>
 <c:if test="${empty section.syllabus}">Syllabus</c:if>
</div>

<div class="journal-section-title">3. Handouts</div>
<div class="journal-section-content">
<c:if test="${fn:length(courseJournal.handouts) > 0}">
<ul>
  <c:forEach items="${courseJournal.handouts}" var="handout">
  <li><a href="viewResource?id=${handout.id}">${handout.name}</a></li>
  </c:forEach>
</ul>
</c:if>
</div>

<div class="journal-section-title">4. Assignments</div>
<div class="journal-section-content">
<c:if test="${fn:length(courseJournal.assignments) > 0}">
<ul>
  <c:forEach items="${courseJournal.assignments}" var="assignment">
  <li><a href="viewAssignment?id=${assignment.id}">${assignment.name}</a></li>
  </c:forEach>
</ul>
</c:if>
</div>

<div class="journal-section-title">5. Student Samples</div>
<div class="journal-section-content">
<c:if test="${fn:length(courseJournal.studentSamples) > 0}">
<ul>
  <c:forEach items="${courseJournal.studentSamples}" var="enrollment">
  <li><a href="viewEnrollment?id=${enrollment.id}">${enrollment.student.name}</a></li>
  </c:forEach>
</ul>
</c:if>
</div>
