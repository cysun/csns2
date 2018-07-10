<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<ul id="title">
<li><a href="list" class="bc">Program Assessment</a></li>
<li>${program.name}</li>
<li class="align_right"><a href="edit?id=${program.id}"><img alt="[Edit Program]"
  title="Edit Program" src="<c:url value='/img/icons/report_edit.png' />" /></a></li>
</ul>

<div id="site-sidebar" style="float: left; margin-top: 0px;">
<ul>
  <li><a href="#vision">Vision Statement</a></li>
  <li><a href="#mission">Mission Statement</a></li>
  <li><a href="#objectives">Educational Objectives</a></li>
  <li><a href="#outcomes">Student Outcomes</a></li>
  <c:forEach items="${program.sections}" var="section">
    <c:if test="${not section.hidden}">
    <li><a href="#s${section.id}">${section.name}</a></li>
    </c:if>
  </c:forEach>
</ul>
</div>

<div id="site-content" style="margin-left: 180px;">

<div class="site-block">
<div id="vision" class="site-block-title">Vision Statement</div>
<div class="site-block-content">${program.vision}</div>
</div>

<div class="site-block">
<div id="mission" class="site-block-title">Mission Statement</div>
<div class="site-block-content">${program.mission}</div>
</div>

<div class="site-block">
<div id="objectives" class="site-block-title">Program Educational Objectives (PEO)</div>
<div class="site-block-content">
<table class="general">
  <c:forEach items="${program.objectives}" var="objective" varStatus="status">
  <tr>
    <th class="shrink">${status.index+1}</th>
    <td>${objective.text}</td>
    <td class="shrink">
      <a href="objective/description?fieldId=${objective.id}"><img alt="[Details]"
           title="Details" src="<c:url value='/img/icons/table_chart.png' />" /></a>
    </td>
  </tr>
  </c:forEach>
</table>
</div>
</div>

<div class="site-block">
<div id="outcomes" class="site-block-title">Student Outcomes (SO)</div>
<div class="site-block-content">
<table class="general">
  <c:forEach items="${program.outcomes}" var="outcome" varStatus="status">
  <tr>
    <th class="shrink">${status.index+1}</th>
    <td>${outcome.text}</td>
    <td class="shrink">
      <a href="outcome/description?fieldId=${outcome.id}"><img alt="[Details]"
           title="Details" src="<c:url value='/img/icons/table_chart.png' />" /></a>
    </td>
  </tr>
  </c:forEach>
</table>
</div>
</div>

<c:forEach items="${program.sections}" var="section">
<c:if test="${not section.hidden}">
<div class="site-block">
<div id="s${section.id}" class="site-block-title">${section.name}</div>
<div class="site-block-content">
<ul>
  <c:forEach items="${section.resources}" var="resource">
  <li><a href="resource/view?programId=${program.id}&resourceId=${resource.id}"
           id="r${resource.id}">${resource.name}</a></li>
  </c:forEach>
</ul>
</div>
</div>
</c:if>
</c:forEach>

</div> <!-- end of site-content -->
