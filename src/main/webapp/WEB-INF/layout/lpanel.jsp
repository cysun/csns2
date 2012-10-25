<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<div id="lpanel"> 
<security:authorize access="anonymous">
<form id="login" name="login" action="<c:url value='/j_spring_security_check' />" method="post">
  <label><input class="forminput" placeholder="Username" type="text" name="j_username" /></label>
  <label><input class="forminput" placeholder="Password" type="password" name="j_password" /></label>
  <input class="submit" type="submit" value="Login" name="submit"/> &nbsp;
  <a href="<c:url value="/resetPassword" />">
    <img alt="reset password"  class="f_password" src="<c:url value='/img/icons/f_password.png' />"
         border="0" title="Forgot Password?" />
  </a>
</form>
</security:authorize>

<security:authorize access="authenticated">
  <a href="<c:url value='/profile' />"><security:authentication property="principal.firstName" />'s
  Profile</a>&nbsp; | &nbsp;<a href="<c:url value='/j_spring_security_logout' />">Logout</a>
</security:authorize>
</div>
