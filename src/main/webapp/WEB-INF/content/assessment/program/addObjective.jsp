<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Basic"
        });
    });
});
</script>

<ul id="title">
<li><a href="list" class="bc">Program Assessment</a></li>
<li><a href="view?id=${program.id}" class="bc"><csns:truncate value="${program.name}" length="50" /></a></li>
<li>Add Objective</li>
</ul>

<form:form modelAttribute="objective">
<table class="general">
  <tr><td><form:textarea path="text" rows="5" cols="80" /></td></tr>
  <tr><td><input type="submit" class="subbutton" value="Add" /></td></tr>
</table>
</form:form>
