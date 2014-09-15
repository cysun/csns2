<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<script>
$(function(){
    document.title = "${section.course.code} ${section.quarter.shortString}";
});
</script>

<c:if test="${isInstructor}">
<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li>${section.course.code}-${section.number}</li>
<li class="align_right"><a href="blocks?id=${site.id}"><img title="Edit Blocks"
  alt="[Edit Blocks]" src="<c:url value='/img/icons/bricks.png' />" /></a></li>
<li class="align_right"><a href="info?id=${site.id}"><img title="Edit Class Info"
  alt="[Edit Class Info]" src="<c:url value='/img/icons/document_info.png' />" /></a></li>
<li class="align_right"><a href="syllabus?id=${site.id}"><img title="Edit Syllabus"
  alt="[Edit Syllabus]" src="<c:url value='/img/icons/script_text.png' />" /></a></li>
</ul>
</c:if>

<div class="site-title">${site.section.course.code} ${site.section.course.name}</div>
<div class="site-quarter">${site.section.quarter}</div>

<table id="site" style="width: 100%;">
<tr>
<!-- sidebar -->
<td id="site-sidebar" colspan="1" rowspan="2" class="shrink">
<ul>
  <c:if test="${not empty section.syllabus}">
  <li><a href="/syllabus">Syllabus</a></li>
  </c:if>
  <c:forEach items="${site.blocks}" var="block">
  <li><a href="#b${block.id}">${block.name}</a></li>
  </c:forEach>
</ul>
</td> <!-- end of sidebar -->

<!-- class info -->
<td>
<table class="general autowidth" style="margin-left: 10px">
<tr>
  <th>Instructor</th>
  <td style="vertical-align: middle; padding: 5px;">Chengyu Sun</td>
</tr>
<tr>
  <th>Lecture</th>
  <td style="vertical-align: middle; padding: 5px;">Monday 2-3pm</td>
</tr>
<tr>
  <th>Lab</th>
  <td style="vertical-align: middle; padding: 5px;">Wednesday 3-4pm</td>
</tr>
<tr>
  <th>Office Hours</th>
  <td style="vertical-align: middle; padding: 5px;">MW 2-3pm in E&amp;T A317</td>
</tr>
</table>
</td> <!-- end of class info -->
</tr>

<tr>
<!-- blocks -->
<td id="site-content">
<c:forEach items="${site.blocks}" var="block">
<a id="b${block.id}"></a><div class="site-block">
<div class="site-block-title">${block.name}</div>
<div class="site-block-content">
  <ul>
    <li>This is an item.</li>
    <li>This is another item.</li>
  </ul>
</div>
</div> <!-- end of site-block -->
</c:forEach>
</td> <!-- end of blocks -->
</tr>
</table>
