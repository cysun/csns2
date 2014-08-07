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
    var msg = "Do you want to publish this survey now?";
    if( confirm(msg) )
        $("#pdate-"+id).load( "publish?id=" + id );
}
function close( id )
{
    var msg = "Do you want to close this survey now?";
    if( confirm(msg) )
        $("#cdate-"+id).load( "close?id=" + id );
}
function clone( id )
{
    var msg = "Do you want to clone this survey?";
    if( confirm(msg) )
        window.location.href = "clone?id=" + id;
}
</script>

<ul id="title">
<li>Surveys</li>
<li class="align_right"><a href="create"><img alt="[Create Survey]"
  title="Create Survey" src="<c:url value='/img/icons/script_add.png' />" /></a></li>
<li class="align_right"><a href="chart/list"><img alt="[Survey Charts]"
  title="Survey Charts" src="<c:url value='/img/icons/chart_bar.png' />" /></a></li>
</ul>

<div id="tabs">
<ul>
  <li><a href="#open">Open</a></li>
  <li><a href="#unpublished">Unpublished</a></li>
  <li><a href="closed">Closed Recently</a></li>
  <li><a href="#search">All</a>
</ul>

<div id="open">
<c:if test="${fn:length(openSurveys) == 0}">
<p>No open surveys.</p>
</c:if>

<c:if test="${fn:length(openSurveys) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Name</th><th>Author</th><th>Published</th><th>Closed</th><th></th></tr>
</thead>
<tbody>
  <c:forEach items="${openSurveys}" var="survey">
  <tr>
    <td><a href="view?id=${survey.id}">${survey.name}</a></td>
    <td class="shrink">${survey.author.username}</td>
    <td class="date"><csns:publishDate date="${survey.publishDate.time}"
        datePast="${survey.published}" itemId="${survey.id}" /></td>
    <td class="date"><csns:closeDate survey="${survey}" /></td>
    <td class="action">
      <a href="results?id=${survey.id}"><img alt="[Results]" 
         title="Results" src="<c:url value='/img/icons/table_multiple.png'/>" /></a>
      <a href="javascript:clone(${survey.id})"><img alt="[Clone Survey]" 
         title="Clone Survey" src="<c:url value='/img/icons/script_code.png'/>" /></a>
      <a href="edit?id=${survey.id}"><img alt="[Edit Survey]"
         title="Edit Survey" src="<c:url value='/img/icons/script_edit.png'/>" /></a>
    </td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
</div>

<div id="unpublished">
<c:if test="${fn:length(unpublishedSurveys) == 0}">
<p>No unpublished surveys.</p>
</c:if>

<c:if test="${fn:length(unpublishedSurveys) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Name</th><th>Author</th><th>Published</th><th>Closed</th><th></th></tr>
</thead>
<tbody>
  <c:forEach items="${unpublishedSurveys}" var="survey">
  <tr>
    <td><a href="view?id=${survey.id}">${survey.name}</a></td>
    <td class="shrink">${survey.author.username}</td>
    <td class="date"><csns:publishDate date="${survey.publishDate.time}"
        datePast="${survey.published}" itemId="${survey.id}" /></td>
    <td class="date"><csns:closeDate survey="${survey}" /></td>
    <td class="action">
      <a href="javascript:clone(${survey.id})"><img alt="[Clone Survey]" 
         title="Clone Survey" src="<c:url value='/img/icons/script_code.png'/>" /></a>
      <a href="edit?id=${survey.id}"><img alt="[Edit Survey]"
         title="Edit Survey" src="<c:url value='/img/icons/script_edit.png'/>" /></a>
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

<c:if test="${fn:length(surveySearchResults) > 0}">
<table class="viewtable">
<thead>
  <tr><th>Name</th><th>Author</th><th>Published</th><th>Closed</th><th></th></tr>
</thead>
<tbody>
  <c:forEach items="${surveySearchResults}" var="survey">
  <tr>
    <td><a href="view?id=${survey.id}">${survey.name}</a></td>
    <td class="shrink">${survey.author.username}</td>
    <td class="date"><csns:publishDate date="${survey.publishDate.time}"
        datePast="${survey.published}" itemId="${survey.id}" /></td>
    <td class="date"><csns:closeDate survey="${survey}" /></td>
    <td class="action">
<c:if test="${survey.published}">
      <a href="results?id=${survey.id}"><img alt="[Results]" 
         title="Results" src="<c:url value='/img/icons/table_multiple.png'/>" /></a>
</c:if>
      <a href="javascript:clone(${survey.id})"><img alt="[Clone Survey]" 
         title="Clone Survey" src="<c:url value='/img/icons/script_code.png'/>" /></a>
      <a href="edit?id=${survey.id}"><img alt="[Edit Survey]"
         title="Edit Survey" src="<c:url value='/img/icons/script_edit.png'/>" /></a>
    </td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
</div>

</div> <!-- tabs -->
