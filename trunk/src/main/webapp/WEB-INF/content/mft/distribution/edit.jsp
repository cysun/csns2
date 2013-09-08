<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
function addEntry()
{
	var value = $("input[name='value']").val();
	var percentile = $("input[name='percentile']").val();
    $.ajax({
        url: "addEntry",
        data: {
        	"value": value,
        	"percentile": percentile,
        	"_cid": "${_cid}"
        },
        success: function(){
        	var newRow = "<tr id='entry-" + value + "'><td class='center'>"
        	   + value + "</td><td class='center'>" + percentile + "</td><td>"
        	   + "<a href='javascript:deleteEntry(" + value +")'><img title='Delete' "
        	   + "alt='[Delete]' src='<c:url value='/img/icons/delete.png' />' /></a>"
        	   + "</td></tr>";
            $("#entryform").after(newRow);
            $("input[name='value']").val("");
            $("input[name='percentile']").val("");
        },
        cache: false
    });
}
function deleteEntry( value )
{
    $.ajax({
        url: "deleteEntry",
        data: {
            "value": value,
            "_cid": "${_cid}"
        },
        success: function(){
            $("#entry-"+value).remove();
        },
        cache: false
    });    
}
</script>

<ul id="title">
<li><a class="bc" href="overview">MFT</a></li>
<li><a class="bc" href="../distribution">National Distributions</a></li>
<li><a class="bc" href="view?id=${distribution.id}">${distribution.year} ${distribution.type.name}</a></li>
<li>Edit</li>
</ul>

<form:form modelAttribute="distribution">
<table class="viewtable autowidth">
  <tr><th colspan="2">Time Period</th><th>Sample Size</th><th>Mean</th><th>Median</th><th>StdDev</th></tr>
  <tr>
    <td><form:input path="fromDate" cssClass="smallinput" placeholder="MM/YYYY" /></td>
    <td><form:input path="toDate" cssClass="smallinput" placeholder="MM/YYYY" /></td>
    <td><form:input path="numOfSamples" cssClass="smallinput" /></td>
    <td><form:input path="mean" cssClass="smallinput" /></td>
    <td><form:input path="median" cssClass="smallinput" /></td>
    <td><form:input path="stdev" cssClass="smallinput" /></td>
  </tr>
</table>

<p></p>

<table class="viewtable autowidth">
<tr><th>${distribution.type.valueLabel}</th><th>Percentile</th><th></th></tr>
<tr id="entryform">
  <td class="center"><input type="text" name="value" class="smallinput" /></td>
  <td class="center"><input type="text" name="percentile" class="smallinput" /></td>
  <td><a href="javascript:addEntry()"><img title="Add" alt="[Add]"
         src="<c:url value='/img/icons/add.png' />" /></a></td>
<c:forEach items="${distribution.entries}" var="entry">
<tr id="entry-${entry.value}">
  <td class="center">${entry.value}</td>
  <td class="center">${entry.percentile}</td>
  <td><a href="javascript:deleteEntry(${entry.value})"><img title="Delete"
         alt="[Delete]" src="<c:url value='/img/icons/delete.png' />" /></a></td>
</tr>
</c:forEach>
</table>

<p><input type="submit" name="save" value="Save" class="subbutton" /></p>
</form:form>
