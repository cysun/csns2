<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<script>
$(function(){
    document.title = "${section.course.code} ${section.quarter.shortString}";
});
</script>

<div class="site-title">${site.section.course.code} ${site.section.course.name}</div>
<div class="site-quarter">${site.section.quarter}</div>

<table id="site">
<tr>
<!-- sidebar -->
<td id="site-sidebar" colspan="1" rowspan="2">
<ul>
  <c:forEach items="${site.blocks}" var="block">
  <li><a href="#b${block.id}">${block.name}</a></li>
  </c:forEach>
</ul>
</td> <!-- end of sidebar -->

<!-- class info -->
<td>
<p style="margin-left: 10px;">Some class information here.</p>
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
