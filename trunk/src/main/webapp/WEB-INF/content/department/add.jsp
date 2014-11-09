<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
$(function(){
    $("#admin").autocomplete({
        source: "<c:url value='/autocomplete/user' />",
        select: function(event, ui) {
            if( ui.item )
            	$("<input>").attr({
            	    type: "hidden",
            	    name: "administrators",
            	    value: ui.item.id
            	}).appendTo("form");
        }
    });
});
</script>

<ul id="title">
<li><a class="bc" href="list">Departments</a></li>
<li>Add Department</li>
</ul>

<form:form modelAttribute="department">
<table class="general">
  <tr>
    <th>Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" size="80" maxlength="255" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>

  <tr>
    <th>Full Name</th>
    <td>
      <form:input path="fullName" cssClass="leftinput" size="80" maxlength="255" />
      <div class="error"><form:errors path="fullName" /></div>
    </td>
  </tr>

  <tr>
    <th>Abbreviation</th>
    <td>
      <form:input path="abbreviation" cssClass="leftinput" size="10" maxlength="10" />
      <div class="error"><form:errors path="abbreviation" /></div>
    </td>
  </tr>

  <tr>
    <th>Administrator</th>
    <td>
      <input id="admin" type="text" class="forminput" name="term" size="40"
        value="${department.administrators[0].name}" />
      <div class="error"><form:errors path="administrators" /></div>
    </td>
  </tr>

  <tr>
    <th>Assessment Tools</th>
    <td>
      MFT <form:checkbox path="options" value="MFT"/>
    </td>
  </tr>

  <tr><th></th><td><input class="subbutton" type="submit" value="Add" /></td></tr>
</table>
</form:form>
