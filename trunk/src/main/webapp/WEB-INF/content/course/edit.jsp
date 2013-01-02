<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("#coordinator").autocomplete({
        source: "<c:url value='/autocomplete/user' />",
        select: function(event, ui) {
            if( ui.item )
                $("input[name='coordinator']").val(ui.item.id);
        }
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
<li><a class="bc" href="search">Courses</a></li>
<li>${course.code}</li>
</ul>

<form:form modelAttribute="course">
<table class="general">
  <tr>
    <th><csns:help name="code">Code</csns:help> *</th>
    <td>
      <form:input path="code" cssClass="forminput" />
      <div class="error"><form:errors path="code" /></div>
    </td>
  </tr>
  <tr>
    <th>Name *</th>
    <td>
      <form:input path="name" cssClass="forminput" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>
  <tr>
    <th>Coordinator</th>
    <td>
      <input id="coordinator" name="cname" class="forminput" value="${course.coordinator.name}" />
      <input name="coordinator" type="hidden" value="${course.coordinator.id}" />
    </td>
  </tr>
  <tr>
    <th>Obsolete</th>
    <td><form:checkbox path="obsolete" cssStyle="width: auto;" /></td>
  </tr>
  <tr>
    <th></th>
    <td>
      <input type="submit" class="subbutton" value="Save" />
    </td>
  </tr>
</table>
</form:form>

<div id="help-code" class="help">A course code must consist of uppercase letters
followed by a number, and optionally, followed by another uppercase letter, e.g.
<span class="tt">CS101</span> or <span class="tt">CS496A</span>.</div>
