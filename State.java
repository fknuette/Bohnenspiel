import java.util.LinkedList;

public class State {

	private int heuristik;
	private int spielzug;
	private LinkedList<State> child = null;
	private boolean terminal = false;

	// public
	private int[] mulden; // Mulden mit den Bohnen
	private int kammer1; // Schatzkammer Spieler 1
	private int kammer2; // Schatzkammer Spieler 2

	public State() {
		// Startzustand des Spiels: in jeder Mulde sind 6 Bohnen und beide Kammern sind
		// leer
		this.mulden = new int[12];
		/*
		 * Anordnung der Mulden (Index im Array) 11 10 9 8 7 6 (Spieler 2) 0 1 2 3 4 5
		 * (Spieler 1)
		 */
		for (int i = 0; i < this.mulden.length; i++) {
			this.mulden[i] = 6;
		}
		this.kammer1 = 0;
		this.kammer2 = 0;
	}

	public State(int[] zahlen, int spieler1, int spieler2) {
		mulden = zahlen;
		kammer1 = spieler1;
		kammer2 = spieler2;
	}

	public int[] getMulden() {
		return this.mulden;
	}

	public int getKammer1() {
		return this.kammer1;
	}

	public int getKammer2() {
		return this.kammer2;
	}

	public int getSpielzug() {
		return this.spielzug;
	}

	public boolean getTerminal() {
		return this.terminal;
	}

	public int getHeuristik() {
		return this.heuristik;
	}

	public LinkedList<State> getChildren() {
		return this.child;
	}

	public void addKammer1(int hinzu) {
		this.kammer1 += hinzu;
	}

	public void addKammer2(int hinzu) {
		this.kammer2 += hinzu;
	}

	public void setHeuristik(int bestimmer) {
		heuristik = bestimmer;
	}

	public void setSpielzug(int num) {
		this.spielzug = num;
	}

	public void setChildren(LinkedList<State> child) {
		this.child = child;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}
}
