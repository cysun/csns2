<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
$(function(){
    $("#coordinator").autocomplete({
        source: "<c:url value='/autocomplete/user' />",
        select: function(event, ui) {
            if( ui.item )
                $("<input>").attr({
                    type: "hidden",
                    name: "coordinator",
                    value: ui.item.id
                }).appendTo("form");
        }
    });
});
</script>

<ul id="title">
<li><a class="bc" href="search">Courses</a></li>
<li>Add Course</li>
</ul>

<form:form modelAttribute="course">
<table class="general">
  <tr>
    <th>Code *</th>
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
      <input id="coordinator" name="cname" class="forminput" />
    </td>
  </tr>
  <tr>
    <th></th>
    <td>
      <input type="submit" class="subbutton" value="Add" />
    </td>
  </tr>
</table>
</form:form>
