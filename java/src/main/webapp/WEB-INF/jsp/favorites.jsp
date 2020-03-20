<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:import url="/WEB-INF/jsp/common/header.jsp" />

<h1>User-Voted Favorite National Parks!</h1>

<!-- Variable to store rank of park based on order of appearance -->
<c:set var="rank" value="0"/>
<table id="home-page-table">

	<!-- Loop through generated map<Park, Integer> to display park info
	and their respective number of votes found in the survey_result table in npgeek database -->
	<c:forEach var="map" items="${map}">
		<c:set var="rank" value="${rank + 1}"/>
		<c:set var="park" value="${map.key}"/>
		<tr>
			<td class="tabletop tablebottom" id="table-right-border">#${rank}</td>
			<td class="tabletop tablebottom">
				<c:url value="/img/parks/${park.parkCode}.jpg" var="parkPicture"/>
				<c:url value="/detail?parkCode=${park.parkCode}" var="parkLink" />
				<a href= "${parkLink}"><img src= "${parkPicture}" id="home-page-picture"/></a>
			</td>
			
			<td class="tabletop tablebottom">
				<div id="home-page-table-text">
					<a href = "${parkLink}"><button class="button park"><h3>${park.parkName}, ${park.state}</h3></button></a>
				</div>
				<br>
				<div>Number of votes: ${map.value}</div>
			</td>
		</tr>
	</c:forEach>
	
</table>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />