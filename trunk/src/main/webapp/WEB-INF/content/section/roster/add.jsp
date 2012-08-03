<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
$(function() {
    $("#generate_cin").click(function(event){
        event.preventDefault();
        var cin = "G" + (100000000 + Math.floor(Math.random()*100000000));
        $("#cin").val( cin );
    });
    $("#user").validate({
        errorPlacement: function(error, element){
            error.appendTo( element.parent("td").find("div") );
        }
    });
});
</script>

<p>Here you may add a student to the class if the student is not on the
GET roster.</p>

<form:form modelAttribute="user">
<table class="general">
  <tr>
    <th>First Name</th>
    <td>
      <form:input path="firstName" cssClass="forminput required" />
      <div class="error"></div>
    </td>
  </tr>
  <tr>
    <th>Last Name</th>
    <td>
      <form:input path="lastName" cssClass="forminput required" />
      <div class="error"></div>
    </td>
  </tr>
  <tr>
    <th>CIN</th>
    <td>
      <form:input path="cin" cssClass="forminput required" />
      <button id="generate_cin" class="subbutton">Generate</button>
      <div class="error"></div>
    </td>
  </tr>
  <tr>
    <th></th>
    <td>
      <input type="submit" class="subbutton" value="Add" />
    </td>
  </tr>
</table>
<input type="hidden" name="sectionId" value="${param.sectionId}" />
</form:form>
