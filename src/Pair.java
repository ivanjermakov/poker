public class Pair<F, S> {

	public F first;

	public S second;

	public Pair getPair(F first, S second) {
		this.first = first;
		this.second = second;

		return this;
	}
}
