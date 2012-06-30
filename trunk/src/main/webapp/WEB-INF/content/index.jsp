<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
$(function(){
    $("select").change(function(){
    	window.location.href = $("select").val();
    });
    $("button").click(function(){
    	window.location.href = $("select").val();
    });
});
</script>

<img id="welcome" src="<c:url value='/img/style/welcome_banner.jpg' />" alt="welcome" />

<ul id="title" style="margin:0;">
<li>Welcome to CSNetwork Services</li>
</ul>

<div id="blk">
<div class="blk_wrap"><div class="blk_cnt">
<p>CSNetwork Services, or CSNS, is a web-based system that provides a number of services to 
facilitates teaching, learning, student administration, and program assessment.</p>

<p>To proceed, please select a department:  
  <select name="department">
    <c:forEach items="${departments}" var="department">
      <option value="department/${department.abbreviation}/">${department.name}</option>
    </c:forEach>
  </select>
  <button>OK</button>
</p>
</div></div>
</div>
