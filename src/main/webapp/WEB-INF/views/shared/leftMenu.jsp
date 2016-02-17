<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id = 'leftMenuUnderlay'></div>
<div id = 'leftMenu'>
	<ul>
		<c:forEach var = 'i' begin = '0' end = '${param.noOfMenuItems}'>
			
				<c:choose>
				    <c:when test="${param.activeMenuItem == paramValues.menuItemsList[i] }">
						<li class = 'activeMenuItem'>
							<a href = '${paramValues.menuItemsLink[i]}'>
								<img src = '${paramValues.menuItemsImage[i]}' />
								<span>${paramValues.menuItemsList[i]}</span>
							</a>
						</li>
					</c:when>
				    <c:otherwise>
				    	<li>
				    		<a href = '${paramValues.menuItemsLink[i]}'>
								<img src = '${paramValues.menuItemsImage[i]}' />
								<span>${paramValues.menuItemsList[i]}</span>
							</a>
						</li>
				    </c:otherwise>
				</c:choose>				
		</c:forEach>
	</ul>
</div>