<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("#remove").click(function(){
        if( confirm("Are you sure you want to remove this group?") )
            window.location.href="remove?id=${group.id}";
    });
});
</script>
<ul id="title">
<li><a class="bc" href="<c:url value='/user/search' />">Users</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/people#group' />">${group.department.name}</a></li>
<li>Edit User Group</li>
<li class="align_right"><a id="remove" href="javascript:void(0)"><img title="Remove Group"
    alt="[Remove Group]" src="<c:url value='/img/icons/group_delete.png' />" /></a></li>
</ul>

<form:form modelAttribute="group">
<table class="general">
  <tr>
    <th class="shrink">Name *</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 30%;" maxlength="50" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>
  <tr>
    <th>Short Description</th>
    <td>
      <form:input path="description" cssClass="leftinput" cssStyle="width: 99%;" maxlength="255" />
    </td>
  </tr>
  <tr><th></th><td><input class="subbutton" type="submit" value="Save" /></td></tr>
</table>
</form:form>
