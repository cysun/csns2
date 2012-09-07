<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<div id="csns_header">
<div class="wrap">
  <a href="<c:url value='/' />"><img style="border: 0;" id="csns_logo" alt="csns"
    src="<c:url value='/img/layout/csns_logo.png' />" /></a>
  <tiles:insertAttribute name="lpanel" defaultValue="lpanel.jsp" defaultValueType="template" />
</div>
</div>
