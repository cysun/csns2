<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
$(function(){
    document.title = "${section.course.code}-${section.number} Settings";
    $(".setting").change(function(){
        $.ajax({
            url: "toggle",
            cache: false,
            data: {
                "setting" : $(this).attr("name")
            }
        });
    });
});
</script>

<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li>Settings</li>
</ul>

<div class="site-title">${section.course.code} ${section.course.name}</div>
<div class="site-quarter">${section.quarter}</div>

<form:form modelAttribute="site">
<table class="general">
<tr>
  <th class="nowrap">Accessible By Class Only</th>
  <td style="vertical-align: middle; padding: 5px;"><form:checkbox cssClass="setting" path="restricted" /></td>
  <td style="padding: 5px;">If this option is checked, only the instructors and the students of the class
    can access the site; otherwise the site is open to public.</td>
</tr>
<tr>
  <th class="nowrap">Available During Term Only</th>
  <td style="vertical-align: middle; padding: 5px;"><form:checkbox cssClass="setting" path="limited" /></td>
  <td style="padding: 5px;">If this option is checked, the site will no longer be available to the
    students after the current term ends. The instructor(s) of the section can
    always access the site.</td>
</tr>
<tr>
  <th class="nowrap">Cloneable By Other Instructors</th>
  <td style="vertical-align: middle; padding: 5px;"><form:checkbox cssClass="setting" path="shared" /></td>
  <td style="padding: 5px;">If this option is checked, other instructors are allowed to clone this
    site; otherwise only the instructor(s) of this section can clone this site.</td>
</tr>
</table>
</form:form>
