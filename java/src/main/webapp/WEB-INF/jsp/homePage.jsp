<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:import url="/WEB-INF/jsp/common/header.jsp" />

<h1>List of National Parks</h1>

<!-- List of every park in npgeek database -->
<table id="home-page-table">
	<c:forEach var="park" items="${parks}">
		<tr>
			
			<!-- First data container is the image of the park 
			located based on the parkcode from npgeek database-->
			<td class="tabletop tablebottom">
			<c:url value="/img/parks/${park.parkCode}.jpg" var="parkPicture"/>
			<c:url value="/detail?parkCode=${park.parkCode}" var="parkLink" />
			<a href= "${parkLink}"><img src= "${parkPicture}" id="home-page-picture"/></a>
			</td>
			
			<!-- Second data container is the name, state, description of the park 
			pulled from npgeek database -->
			<td class="tabletop tablebottom">
				<div id="home-page-table-text">
					<a href = "${parkLink}"><button class="button park"><h3>${park.parkName}, ${park.state}</h3></button></a>
				</div>
				<br>
				<div>${park.description}</div>
			</td>
		</tr>
	</c:forEach>
</table>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />