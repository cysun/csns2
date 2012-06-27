<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<div id="csns_header">
<div class="wrap">
  <a href="<c:url value='/' />"><img border="0" id="csns_logo" src="<c:url value='/img/layout/csns_logo.png' />" alt="csns" /></a>
  <tiles:insertAttribute name="lpanel" defaultValue="lpanel.jsp" defaultValueType="template" />
</div>
</div>
