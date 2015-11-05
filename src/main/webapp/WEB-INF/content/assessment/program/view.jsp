<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<ul id="title">
<li><a href="list" class="bc">Program Assessment</a></li>
<li>${program.name}</li>
<li class="align_right"><a href="edit?id=${program.id}"><img alt="[Edit Program]"
  title="Edit Program" src="<c:url value='/img/icons/report_edit.png' />" /></a></li>
</ul>



<div class="site-block">
<div class="site-block-title">Vision Statement</div>
<div class="site-block-content">${program.vision}</div>
</div>

<div class="site-block">
<div class="site-block-title">Mission Statement</div>
<div class="site-block-content">${program.mission}</div>
</div>

<div class="site-block">
<div class="site-block-title">Educational Objectives
  <div class="site-block-operations">
    <a href="objectives?programId=${program.id}"><img title="Edit Objectives"
       alt="[Edit Objectives]" src="<c:url value='/img/icons/page_edit.png' />" /></a>
  </div>
</div>
<div class="site-block-content">
<table class="viewtable autowidth">
  <c:forEach items="${program.objectives}" var="objective" varStatus="status">
  <tr>
    <td>${status.index+1}</td>
    <td>${objective.text}</td>
  </tr>
  </c:forEach>
</table>
</div>
</div>

<div class="site-block">
<div class="site-block-title">Student Outcomes
  <div class="site-block-operations">
    <a href="outcomes?programId=${program.id}"><img title="Edit Outcomes"
       alt="[Edit Outcomes]" src="<c:url value='/img/icons/page_edit.png' />" /></a>
  </div>
</div>
<div class="site-block-content">
<table class="viewtable autowidth">
  <c:forEach items="${program.outcomes}" var="outcome" varStatus="status">
  <tr>
    <td>${status.index+1}</td>
    <td>${outcome.text}</td>
  </tr>
  </c:forEach>
</table>
</div>
</div>
