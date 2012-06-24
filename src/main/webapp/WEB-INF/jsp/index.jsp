<%@include file="/WEB-INF/jsp/include/header.jsp" %>

<img id="welcome" src="<c:url value='/img/style/welcome_banner.jpg' />" alt="welcome" />

<div id="blk">
<div class="blk_wrap"><div class="blk_cnt">
<p>CSNetwork Services, or CSNS, is a web-based system that provides a number of services to 
facilitates teaching, learning, student administration, and program assessment.</p>

<p>To proceed, please select a department:  
  <select class="formselect" name="department">
    <c:forEach items="${departments}" var="department">
    <option value="${department.abbreviation}">${department.name}</option>
    </c:forEach>
  </select>
</p>
</div></div>
</div>

<%@include file="/WEB-INF/jsp/include/footer.jsp" %>
