<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Basic"
        });
    });
    $("div.help").dialog({
        autoOpen: false,
        modal: true
    });
});
function help( name )
{
    $("#help-"+name).dialog("open");
}
function deleteRubric( id )
{
    var msg = "Are you sure you want to delete this rubric?";
    if( confirm(msg) )
        window.location.href = "delete?id=" + id;
}
</script>

<ul id="title">
<li><a href="list" class="bc">Rubrics</a></li>
<li><a href="view?id=${rubric.id}" class="bc">${rubric.name}</a></li>
<li>Edit</li>
<c:if test="${not rubric.published}">
<security:authorize access="principal.isAdmin('${dept}') or principal.id.toString() == '${rubric.creator.id}'">
<li class="align_right"><a href="javascript:deleteRubric(${rubric.id})"><img alt="[Delete Rubric]"
  title="Delete Rubric" src="<c:url value='/img/icons/table_delete.png' />" /></a></li>
</security:authorize>
</c:if>
</ul>

<form:form modelAttribute="rubric">
<table class="general">
  <tr>
    <th class="shrink">Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%;" maxlength="255" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>

  <tr>
    <th>Description</th>
    <td><form:textarea path="description" rows="5" cols="80" /></td>
  </tr>

  <tr>
    <th><csns:help name="scale">Scale</csns:help></th>
    <td>
      <c:if test="${rubric.published}">${rubric.scale}</c:if>
      <c:if test="${not rubric.published}">
      <form:input path="scale" cssClass="leftinput center" size="1" maxlength="1" />
      <div class="error"><form:errors path="scale" /></div>
      </c:if>
    </td>
  </tr>

<c:if test="${empty rubric.department}">
  <tr>
    <th><csns:help name="public">Public</csns:help></th>
    <td>
      <form:checkbox path="public" />
    </td>
  </tr>
</c:if>

  <tr><th></th><td><input class="subbutton" type="submit" value="Save" /></td></tr>
</table>
</form:form>

<div id="help-scale" class="help"><em>Scale</em> is the number of ranks for
each performance indicator. Scale should be between 2 and 6.</div>

<div id="help-public" class="help">If a rubric is <em>public</em>, everybody
can see and use it (though only the author or an administrator can edit it).</div>
