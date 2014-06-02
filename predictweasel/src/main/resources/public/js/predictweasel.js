/**
 * This event handler is called to signify that the page is ready as the DOM
 * has been fully initialised.
 */
$(document).ready(function() {
	var activeMenu = $('meta[name=active-menu]').attr("content");
	
	if (activeMenu) {
		selectNavMenu(activeMenu);
	}
});

/*
 * Select (by giving it an extra "active" class) the menu item identified
 * by the given jQuery selector.
 * Also deselects all other list items in the nav menu.
 */
function selectNavMenu(menuSelector) {
	$('.navbar-nav li').removeClass('active');
	$(menuSelector).addClass('active');
}
