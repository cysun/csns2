<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
function deleteIndicator()
{
    var msg = "Are you sure you want to delete this performance indicator?";
    if( confirm(msg) )
        window.location.href = "deleteIndicator?rubricId=${rubric.id}&indicatorIndex=${param.indicatorIndex}";
}
</script>

<ul id="title">
<li><a href="list" class="bc">Rubrics</a></li>
<li><a href="view?id=${rubric.id}" class="bc">${rubric.name}</a></li>
<li>Edit Performance Indicator</li>
<li class="align_right"><a href="javascript:deleteIndicator()"><img alt="[Delete Indicator]"
  title="Delete Indicator" src="<c:url value='/img/icons/row_delete.png' />" /></a></li>
</ul>

<form:form modelAttribute="indicator">
<table class="general">
  <tr>
    <th class="shrink">Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%;" maxlength="255" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>

  <c:forEach begin="0" end="${rubric.scale-1}" step="1" var="index">
  <tr>
    <th>${index+1}</th>
    <td><form:textarea path="criteria[${index}]" rows="5" cols="80" /></td>
  </tr>
  </c:forEach>
  
  <tr><th></th><td><input class="subbutton" type="submit" value="Save" /></td></tr>
</table>

<input type="hidden" name="rubricId" value="${rubric.id}" />
<input type="hidden" name="indicatorIndex" value="${param.indicatorIndex}" />
</form:form>
