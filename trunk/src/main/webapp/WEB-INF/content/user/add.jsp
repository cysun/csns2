<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script type="text/javascript">
$(function() {
    $("#birthday").datepicker({
        inline: true,
        changeMonth: true,
        changeYear: true,
        yearRange: "-60:+00"
    });
    $("#generate_cin").click(function(event){
        event.preventDefault();
        var cin = "G" + (100000000 + Math.floor(Math.random()*100000000));
        $("#cin").val( cin );
    });
});
</script>

<ul id="title">
<li>Users</li>
</ul>

<h3><a href="search">Search</a> | Add</h3>

<p>When a new account is created, the username and the password for the account
are the user's CIN. When the user logs onto the system for the first time,
they will be asked to choose their own username and password.</p>

<form:form commandName="user">
<table class="general">
  <tr>
    <th>First Name *:</th>
    <td>
      <form:input path="firstName" cssClass="forminput" />
      <div class="error"><form:errors path="firstName" /></div>
    </td>
  </tr>
  <tr>
    <th>Last Name *:</th>
    <td>
      <form:input path="lastName" cssClass="forminput" />
      <div class="error"><form:errors path="lastName" /></div>
    </td>
  </tr>
  <tr>
    <th>Gender:</th>
    <td>
      <form:select path="gender">
        <option />
        <form:option value="M">Male</form:option>
        <form:option value="F">Female</form:option>
      </form:select>
    </td>
  </tr>
  <tr>
    <th>Birthday (mm/dd/yyyy):</th>
    <td><form:input path="birthday" cssClass="forminput" /></td>
  </tr>
  <tr>
    <th>CIN *:</th>
    <td>
      <form:input path="cin" cssClass="forminput" />
      <button id="generate_cin" class="subbutton">Generate</button>
      <div class="error"><form:errors path="cin" /></div>
    </td>
  </tr>
  <tr>
    <th>Primary Email:</th>
    <td>
      <form:input path="primaryEmail" cssClass="forminput" />
      <div class="error"><form:errors path="primaryEmail" /></div>
    </td>
  </tr>
  <tr>
    <th>Secondary Email:</th>
    <td>
      <form:input path="secondaryEmail" cssClass="forminput" />
      <div class="error"><form:errors path="secondaryEmail" /></div>
    </td>
  </tr>
  <tr>
    <th>Street:</th>
    <td><form:input path="street" cssClass="forminput" /></td>
  </tr>
  <tr>
    <th>City:</th>
    <td><form:input path="city" cssClass="forminput" /></td>
  </tr>
  <tr>
    <th>State:</th>
    <td><form:input path="state" cssClass="forminput" /></td>
  </tr>
  <tr>
    <th>Zip:</th>
    <td>
      <form:input path="zip" maxlength="5" cssClass="forminput" />
    </td>
  </tr>
  <tr>
    <th>Cell Phone:</th>
    <td>
      <form:input path="cellPhone" cssClass="forminput" />
    </td>
  </tr>
  <tr>
    <th>Home Phone:</th>
    <td>
      <form:input path="homePhone" cssClass="forminput" />
    </td>
  </tr>
  <tr>
    <th>Office Phone:</th>
    <td>
      <form:input path="officePhone" cssClass="forminput" />
    </td>
  </tr>
  <tr>
    <th></th>
    <td>
      <input type="submit" class="subbutton" value="Done" />
    </td>
  </tr>
</table>
</form:form>
