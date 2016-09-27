function test() {
	var test = $("<p></p>");

	var string = " FAMILY / GREAT ROOM Description:Whether you use these rooms for entertaining, watching television, reading, or playing games, three to four layers of lighting should be used. These might include recessed lighting at the perimeter of the room, a chandelier or central decorative fixture for general lighting, wall sconces for mood and lamps for reading and other tasks. Use dimmers whenever possible for maximum control of all fixtures.  ";
    test.text(string);
	$("#content").append(test);
}