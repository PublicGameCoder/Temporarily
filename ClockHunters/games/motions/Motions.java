package games.motions;

public enum Motions {
	
	left(left.class),
	right(right.class),
	up(up.class),
	down(down.class),
	;
	
	Motions(final Class<?> data) {
		@SuppressWarnings("unused")
		Class<?> mo = data;
	}
}
