/*
 * Select (by giving it an extra "active" class) the menu item identified
 * by the given jQuery selector.
 * Also deselects all other list items in the nav menu.
 */
function selectNavMenu(menuSelector) {
	$('.navbar-nav li').removeClass('active');
	$(menuSelector).addClass('active');
}
