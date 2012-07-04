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
    $("#cellPhone").mask("(999) 999-9999");
    $("#homePhone").mask("(999) 999-9999");
    $("#officePhone").mask("(999) 999-9999");
});
</script>

<ul id="title">
<li>${user.name}</li>
</ul>

<div id="tabs">
<ul>
  <li><a href="#account">Account</a></li>
  <li><a href="forum/subscriptions">Forums</a></li>
  <li><a href="mailinglist/subscriptions">Mailing Lists</a></li>
</ul>
<div id="account">
<form:form modelAttribute="user">
<table class="general">
  <tr>
    <th>Name</th>
    <td>${user.firstName} ${user.middleName} ${user.lastName}</td>
  </tr>
  <tr>
    <th>CIN</th>
    <td>
      <c:if test="${! user.cinEncrypted}">${user.cin}</c:if>
      <c:if test="${user.cinEncrypted}">(encrypted)</c:if>
    </td>
  </tr>
  <tr>
    <th>Username *</th>
    <td>${user.username}</td>
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
    <th>Primary Email *</th>
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
    <th></th>
    <td>
      <input type="submit" style="width: auto;" class="subbutton" value="Save" />
    </td>
  </tr>
</table>
</form:form>
</div> <!-- account -->
</div> <!-- tabs -->
