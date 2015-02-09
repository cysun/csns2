<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

 <script>
$(function(){
    $("#major").change(function(){
        window.location.href = "profile/setMajor?majorId=" + $(this).val();
    });
    $("#program").change(function(){
        window.location.href = "profile/setProgram?programId=" + $(this).val();
    });
});
</script>

<form:form modelAttribute="user">
<table class="general autowidth">
<tr>
  <th>Major</th>
  <td>
    <form:select path="major">
      <form:option value="" label=""/>
      <form:options items="${departments}" itemValue="id" itemLabel="name"/>
    </form:select>
  </td>
</tr>  
<tr>
  <th>Program</th>
  <td>
    <form:select path="program">
      <form:option value="" label=""/>
      <form:options items="${programs}" itemValue="id" itemLabel="name"/>
    </form:select>
  </td>
</tr>
</table>
</form:form>

<h4>Required Courses</h4>
