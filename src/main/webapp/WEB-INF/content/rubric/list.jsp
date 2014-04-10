<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("#tabs").tabs({
        cache: false
    }); 
});
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
  <li><a href="#personal">Personal</a></li>
  <li><a href="#department">Department</a></li>
  <li><a href="#search">All</a>
</ul>

<div id="personal">
<c:if test="${fn:length(personalRubrics) == 0}">
<p>No rubrics yet.</p>
</c:if>

<c:if test="${fn:length(openSurveys) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Name</th><th>Published</th><th></th></tr>
</thead>
<tbody>
  <c:forEach items="${personalRubrics}" var="rubric">
  <tr>
    <td><a href="view?id=${rubric.id}">${rubric.name}</a></td>
    <td class="date"><csns:publishDate item="${rubric}" /></td>
    <td class="action">
      <a href="edit?id=${rubric.id}"><img alt="[Edit Rubric]"
         title="Edit Rubric" src="<c:url value='/img/icons/table_edit.png'/>" /></a>
      <a href="javascript:promote(${rubric.id})"><img alt="[Promote Rubric]"
         title="Promote Rubric" src="<c:url value='/img/icons/table_up.png'/>" /></a>
    </td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
</div>

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
    <td class="date"><csns:publishDate item="${rubric}" /></td>
    <td class="action">
      <a href="edit?id=${rubric.id}"><img alt="[Edit Rubric]"
         title="Edit Rubric" src="<c:url value='/img/icons/table_edit.png'/>" /></a>
    </td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
</div>

<div id="search">
<form action="search" method="post">
<p><input  name="term" type="text" class="leftinput" style="width: 15em;" value="${surveySearchTerm}"/>
<input name="search" type="submit" value="Search" class="subbutton" /></p>
</form>

<c:if test="${fn:length(rubricSearchResults) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Name</th><th>Department</th><th>Author</th><th>Published</th><th></th></tr>
</thead>
<tbody>
  <c:forEach items="${rubricSearchResults}" var="survey">
  <tr>
    <td><a href="view?id=${rubric.id}">${rubric.name}</a></td>
    <td class="shrink">${rubric.department.name}</td>
    <td class="shrink">${rubric.creator.username}</td>
    <td class="date"><csns:publishDate item="${rubric}" /></td>
    <td class="action">
      <a href="edit?id=${rubric.id}"><img alt="[Edit Rubric]"
         title="Edit Rubric" src="<c:url value='/img/icons/table_edit.png'/>" /></a>
    </td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
</div>

</div> <!-- tabs -->
