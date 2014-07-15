<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
    $("#tabs").tabs({
        cache: false
    });
});
function toggle( id )
{
    $.ajaxSetup({ cache: false });
    $("#rubric-" + id).load("toggle?id=" + id);
}
function publish( id )
{
    var msg = "Do you want to publish this rubric now?";
    if( confirm(msg) )
        $("#pdate-"+id).load( "publish?id=" + id );
}
function clone( id )
{
    var msg = "Do you want to clone this rubric?";
    if( confirm(msg) )
        window.location.href = "clone?id=" + id;
}
</script>

<ul id="title">
<li>Rubrics</li>
<li class="align_right"><a href="create"><img alt="[Create Rubric]"
  title="Create Rubric" src="<c:url value='/img/icons/table_add.png' />" /></a></li>
</ul>

<div id="tabs">
<ul>
<c:if test="${fn:length(personalRubrics) > 0}">
  <li><a href="#personal">Personal</a></li>
</c:if>
  <li><a href="#department">Department</a></li>
  <li><a href="#search">All</a>
</ul>

<c:if test="${fn:length(personalRubrics) > 0}">
<div id="personal">
<table class="viewtable">
<thead>
  <tr><th>Name</th><th>Published</th><th></th></tr>
</thead>
<tbody>
  <c:forEach items="${personalRubrics}" var="rubric">
  <tr>
    <td>
      <c:set var="img" value="closed_book.png" />
      <c:if test="${rubric.isPublic()}">
        <c:set var="img" value="open_book.png" />
      </c:if>
      <span style="margin-right: 0.5em;"><a id="rubric-${rubric.id}"
          href="javascript:toggle(${rubric.id})"><img border="0" alt="[*]"
          title="Toggle Public/Private" src="<c:url value='/img/icons/${img}' />" /></a></span>
      <a href="view?id=${rubric.id}">${rubric.name}</a>
    </td>
    <td class="date"><csns:publishDate date="${rubric.publishDate.time}"
      datePast="${rubric.published}" itemId="${rubric.id}" /></td>
    <td class="action">
      <a href="results?id=${rubric.id}"><img alt="[Results]" 
         title="Results" src="<c:url value='/img/icons/table_multiple.png'/>" /></a>
      <a href="javascript:clone(${rubric.id})"><img alt="[Clone Rubric]"
         title="Clone Rubric" src="<c:url value='/img/icons/table_code.png'/>" /></a>
    </td>
  </tr>
  </c:forEach>
</tbody>
</table>
</div>
</c:if>

<div id="department">
<c:if test="${fn:length(departmentRubrics) == 0}">
<p>No rubrics yet.</p>
</c:if>

<c:if test="${fn:length(departmentRubrics) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Name</th><th>Published</th><th></th></tr>
</thead>
<tbody>
  <c:forEach items="${departmentRubrics}" var="rubric">
  <tr>
    <td><a href="view?id=${rubric.id}">${rubric.name}</a></td>
    <td class="date">
<security:authorize access="principal.isAdmin('${dept}') or principal.id.toString() == '${rubric.creator.id}'">    
      <csns:publishDate date="${rubric.publishDate.time}" datePast="${rubric.published}" itemId="${rubric.id}" />
</security:authorize>
<security:authorize access="not (principal.isAdmin('${dept}') or principal.id.toString() == '${rubric.creator.id}')">    
      <c:if test="${rubric.published}">Yes</c:if>
      <c:if test="${not rubric.published}">No</c:if>
</security:authorize>
    </td>
    <td class="action">
      <a href="results?id=${rubric.id}"><img alt="[Results]" 
         title="Results" src="<c:url value='/img/icons/table_multiple.png'/>" /></a>
      <a href="javascript:clone(${rubric.id})"><img alt="[Clone Rubric]"
         title="Clone Rubric" src="<c:url value='/img/icons/table_code.png'/>" /></a>
    </td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
</div>

<div id="search">
<form action="search" method="post">
<p><input  name="term" type="text" class="leftinput" style="width: 15em;" value="${rubricSearchTerm}"/>
<input name="search" type="submit" value="Search" class="subbutton" /></p>
</form>

<c:if test="${fn:length(rubricSearchResults) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Name</th><th>Department</th><th>Author</th><th>Published</th><th></th></tr>
</thead>
<tbody>
  <c:forEach items="${rubricSearchResults}" var="rubric">
  <tr>
    <td><a href="view?id=${rubric.id}">${rubric.name}</a></td>
    <td class="shrink">${rubric.department.name}</td>
    <td class="shrink">${rubric.creator.username}</td>
    <td class="shrink">
      <c:if test="${rubric.published}">Yes</c:if>
      <c:if test="${not rubric.published}">No</c:if>
    </td>
    <td class="action">
      <a href="javascript:clone(${rubric.id})"><img alt="[Clone Rubric]"
         title="Clone Rubric" src="<c:url value='/img/icons/table_code.png'/>" /></a>
    </td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
</div>

</div> <!-- tabs -->
