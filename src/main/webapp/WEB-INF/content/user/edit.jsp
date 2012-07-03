<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script type="text/javascript">
$(function() {
    $("#tabs").tabs({
        cache: false
    });
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
    $("#cellPhone").mask("(999) 999-9999");
    $("#homePhone").mask("(999) 999-9999");
    $("#officePhone").mask("(999) 999-9999");
});
</script>

<ul id="title">
<li><a class="bc" href="search">Users</a></li>
<li><a class="bc" href="view?id=${user.id}">${user.name}</a></li>
<li>Edit User</li>
</ul>

<div id="tabs">
<ul>
  <li><a href="#account">Account</a></li>
  <li><a href="standings">Standings</a></li>
  <li><a href="coursework">Course Work</a></li>
  <li><a href="advisement">Advisement</a></li>
</ul>
<div id="account">
<form:form modelAttribute="user">
<table class="general">
  <tr>
    <th>First Name *</th>
    <td>
      <form:input path="firstName" cssClass="forminput" />
      <div class="error"><form:errors path="firstName" /></div>
    </td>
  </tr>
  <tr>
    <th>Last Name *</th>
    <td>
      <form:input path="lastName" cssClass="forminput" />
      <div class="error"><form:errors path="lastName" /></div>
    </td>
  </tr>
  <tr>
    <th>Middle Name</th>
    <td>
      <form:input path="middleName" cssClass="forminput" />
    </td>
  </tr>
  <tr>
    <th>Gender:</th>
    <td>
      <form:select path="gender">
        <form:option value="" />
        <form:option value="M">Male</form:option>
        <form:option value="F">Female</form:option>
      </form:select>
    </td>
  </tr>
  <tr>
    <th>Birthday</th>
    <td>
      <form:input path="birthday" cssClass="forminput" placeholder="MM/DD/YYYY" />
    </td>
  </tr>
  <tr>
    <th>CIN *</th>
    <td>
      <form:input path="cin" cssClass="forminput" />
      <div class="error"><form:errors path="cin" /></div>
    </td>
  </tr>
  <tr>
    <th>Username *</th>
    <td>
      <form:input path="username" cssClass="forminput" />
      <div class="error"><form:errors path="username" /></div>
    </td>
  </tr>
  <tr>
    <th>Password</th>
    <td>
      <form:password path="password1" cssClass="forminput" />
      <div class="error"><form:errors path="password1" /></div>
    </td>
  </tr>
  <tr>
    <th>Confirm password:</th>
    <td>
      <form:password path="password2" cssClass="forminput" />
      <div class="error"><form:errors path="password2" /></div>
    </td>
  </tr>
  <tr>
    <th>Primary Email</th>
    <td>
      <form:input path="primaryEmail" cssClass="forminput" />
      <div class="error"><form:errors path="primaryEmail" /></div>
    </td>
  </tr>
  <tr>
    <th>Secondary Email</th>
    <td>
      <form:input path="secondaryEmail" cssClass="forminput" />
      <div class="error"><form:errors path="secondaryEmail" /></div>
    </td>
  </tr>
  <tr>
    <th>Street</th>
    <td><form:input path="street" cssClass="forminput" /></td>
  </tr>
  <tr>
    <th>City</th>
    <td><form:input path="city" cssClass="forminput" /></td>
  </tr>
  <tr>
    <th>State</th>
    <td><form:input path="state" cssClass="forminput" /></td>
  </tr>
  <tr>
    <th>Zip</th>
    <td>
      <form:input path="zip" maxlength="5" cssClass="forminput" />
    </td>
  </tr>
  <tr>
    <th>Cell Phone</th>
    <td>
      <form:input path="cellPhone" cssClass="forminput" placeholder="(###) ###-####" />
    </td>
  </tr>
  <tr>
    <th>Home Phone</th>
    <td>
      <form:input path="homePhone" cssClass="forminput" placeholder="(###) ###-####" />
    </td>
  </tr>
  <tr>
    <th>Office Phone</th>
    <td>
      <form:input path="officePhone" cssClass="forminput" placeholder="(###) ###-####" />
    </td>
  </tr>
  <tr>
    <th>Enabled:</th>
    <td><form:checkbox path="enabled" cssStyle="width: auto;" /></td>
  </tr>
  <tr>
    <th>Expired:</th>
    <td><form:checkbox path="expired" cssStyle="width: auto;" /></td>
  </tr>
  <tr>
    <th></th>
    <td>
      <input type="submit" style="width: auto;" class="subbutton" value="Save" />
    </td>
  </tr>
</table>
</form:form>
</div> <!-- account -->
</div> <!-- tabs -->
