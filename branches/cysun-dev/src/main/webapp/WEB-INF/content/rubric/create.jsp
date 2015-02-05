<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

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
</script>

<ul id="title">
<li><a href="list" class="bc">Rubrics</a></li>
<li>Create</li>
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
      <form:input path="scale" cssClass="leftinput center" size="1" maxlength="1" />
    </td>
  </tr>

  <tr>
    <th><csns:help name="public">Public</csns:help></th>
    <td>
      <form:checkbox path="public" />
    </td>
  </tr>

  <tr><th></th><td><input class="subbutton" type="submit" value="Create" /></td></tr>
</table>
</form:form>

<div id="help-scale" class="help"><em>Scale</em> is the number of ranks for
each performance indicator. Scale should be between 2 and 6.</div>

<div id="help-public" class="help">If a rubric is <em>public</em>, everybody
can see and use it (though only the author or an administrator can edit it).</div>
