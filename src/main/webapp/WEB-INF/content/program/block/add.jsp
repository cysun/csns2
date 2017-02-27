<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
$(function(){
    var $unitsRequired = $("input[name='unitsRequired']");
    if( $("input[name='requireAll']:checked").val() == "true" )
        $unitsRequired.prop( "disabled", true );
    $("input[name='requireAll']").change(function(){
        if( $(this).val() == "true" )
        {
            $unitsRequired.val("");
            $unitsRequired.prop( "disabled", true );
        }
        else
            $unitsRequired.prop( "disabled", false );
    });
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Basic"
        });
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/program/search' />">Programs</a></li>
<li><a class="bc" href="../list">${program.department.name}</a></li>
<li><a class="bc" href="list?programId=${program.id}">${program.name}</a></li>
<li>Add Block</li>
</ul>

<form:form modelAttribute="block">
<table class="general">
  <tr>
    <th class="shrink">Name *</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%;" maxlength="255" required="true" />
    </td>
  </tr>
  <tr>
    <th>Description</th>
    <td><form:textarea path="description" rows="5" cols="80" /></td>
  </tr>
  <tr>
    <th class="shrink">Requirements</th>
    <td>
      <form:radiobutton path="requireAll" value="true" /> All Courses Required
      <form:radiobutton path="requireAll" value="false" /> Require
      <form:input path="unitsRequired" cssClass="smallinput" required="true" /> Units
    </td>
  </tr>
  <tr>
    <th></th>
    <td><input type="submit" class="subbutton" value="Add" /></td>
  </tr>
</table>
</form:form>
