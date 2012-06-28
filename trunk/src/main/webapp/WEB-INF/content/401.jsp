<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul id="title">
<li>Login Error</li>
</ul>
<p class="error">Invalid username or password.</p>
<p>Please try again or follow the instructions below:</p>
<ul class="iconlist">
<li class="blue_arrow">If you never used CSNS before, please read the
<a class="link" href="<c:url value='/wiki/content/csns/help' />">help page</a> on
how to access your account.</li>
<li class="blue_arrow">If you forgot your username and/or password, please follow the
<a class="link" href="<c:url value="/resetPassword.html" />"> Forgot Password?</a> link to
reset your password. Your username and a new password will be sent to the email
address in your account profile.</li>
</ul>
