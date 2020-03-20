<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:import url="/WEB-INF/jsp/common/header.jsp" />
 
<h1>Visit your favorite parks!</h1>

<hr>

<!-- Manually written links to each national park found in npgeek database -->
<ul>

	<li><c:url
			value="https://www.nationalparks.org/explore-parks/cuyahoga-valley-national-park"
			var="cuyahoga" /> <a href="${cuyahoga}"><button class="btn info">Cuyahoga
				Valley National Park</button></a></li>
	<li><c:url
			value="https://www.nationalparks.org/explore-parks/everglades-national-park"
			var="everglades" /> <a href="${everglades}"><button
				class="btn info">Everglades National Park</button></a></li>
	<li><c:url
			value="https://www.nationalparks.org/explore-parks/grand-canyon-national-park"
			var="grandCanyon" /> <a href="${grandCanyon}"><button
				class="btn info">Grand Canyon National Park</button></a></li>
	<li><c:url
			value="https://www.nationalparks.org/explore-parks/glacier-national-park"
			var="glacier" /> <a href="${glacier}"><button class="btn info">Glacier
				National Park</button></a></li>
	<li><c:url
			value="https://www.nationalparks.org/explore-parks/great-smoky-mountains-national-park"
			var="smokies" /> <a href="${smokies}"><button class="btn info">Great
				Smoky Mountains National Park</button></a></li>
	<li><c:url
			value="https://www.nationalparks.org/explore-parks/grand-teton-national-park"
			var="grandTeton" /> <a href="${grandTeton}"><button
				class="btn info">Grand Teton National Park</button></a></li>
	<li><c:url
			value="https://www.nationalparks.org/explore-parks/mount-rainier-national-park"
			var="rainier" /> <a href="${rainier}"><button class="btn info">Mount
				Rainier National Park</button></a></li>
	<li><c:url
			value="https://www.nationalparks.org/explore-parks/rocky-mountain-national-park"
			var="rockies" /> <a href="${rockies}"><button class="btn info">Rocky
				Mountain National Park</button></a></li>
	<li><c:url
			value="https://www.nationalparks.org/explore-parks/yellowstone-national-park"
			var="yellowstone" /> <a href="${yellowstone}"><button
				class="btn info">Yellowstone National Park</button></a></li>
	<li><c:url
			value="https://www.nationalparks.org/explore-parks/yosemite-national-park"
			var="yosemite" /> <a href="${yosemite}"><button
				class="btn info">Yosemite National Park</button></a></li>

</ul>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />