<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<ul id="title">
<li>MFT</li>
<li class="align_right"><a href="score/import"><img alt="[Import Score]"
  title="Import Score" src="<c:url value='/img/icons/table_add.png' />" /></a></li>
</ul>

<c:if test="${fn:length(dates) == 0}">
<p>No MFT scores yet.</p>
</c:if>

<c:if test="${fn:length(dates) > 0}">
<form method="get" action="score">
<p>Select date: <select name="date" onchange="this.form.submit()">
<c:forEach items="${dates}" var="date">
<option value="<fmt:formatDate value='${date}' pattern='yyyy-MM-dd' />"
  <c:if test="${date.time == selectedDate.time}">selected="selected"</c:if>>
  <fmt:formatDate value='${date}' pattern='yyyy-MM-dd' />
</option>
</c:forEach>
</select>
<input type="submit" class="subbutton" value="Go" />
</p>
</form>
</c:if>
