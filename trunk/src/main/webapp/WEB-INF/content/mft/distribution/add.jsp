<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<ul id="title">
<li><a class="bc" href="../overview">MFT</a></li>
<li><a class="bc" href="../distribution">National Distributions</a></li>
<li>Add</li>
</ul>

<form:form modelAttribute="distribution">
<table class="general">
  <tr>
    <th>Year</th>
    <td><form:input path="year" class="smallinput" placeholder="YYYY" required="true" />
  </tr>
  <tr>
    <th>Type</th>
    <td><form:select path="type" items="${distributionTypes}" itemLabel="name" /></td>
  </tr>
  <tr>
    <th></th><td><input type="submit" class="subbutton" value="Next" />
  </tr>
</table>
</form:form>
