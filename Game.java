import java.util.LinkedList;

public class Game {

	private State primo; // Wurzelknoten
	private int player; // Welcher Spieler sind wir

	public Game(int spieler) {
		this.player = spieler;
		build();
	}

	public State getPrimo() {
		return this.primo;
	}

	// Hier wird der Spielbaum aufgebaut
	private void build() {
		primo = new State();
		LinkedList<State> ebene = new LinkedList<State>();// Ebene weche gerade bearbeitet wird
		ebene.add(primo);

		LinkedList<State> helfer = new LinkedList<State>();
		LinkedList<State> setterChild;

		if (player == 1 || player == 2) {
			for (int i = 0; i < 6; i++) {
				if (i % 2 == 0) {
					for (State s : ebene) {
						setterChild = expandedHome(s);
						s.setChildren(setterChild);
						helfer.addAll(setterChild);
					}
					ebene = new LinkedList<State>(helfer);
					helfer = new LinkedList<State>();
				} else {
					for (State s : ebene) {
						setterChild = expandedAway(s);
						s.setChildren(setterChild);
						helfer.addAll(setterChild);
					}
					ebene = new LinkedList<State>(helfer);
					helfer = new LinkedList<State>();
				}
			}
		}
	}

	// Hier wir unser Spielbaum mitsamt dem Knoten geupdated
	public void update(int selectedStateNum) {
		for (State s : this.primo.getChildren()) {
			if (s.getSpielzug() == selectedStateNum) {
				this.primo = s;
				break;
			}
		}

		this.neueEbene(primo);
	}

	// Gebe den Knoten Kinder, die keine Kinder haben F
	private void neueEbene(State s) {
		if (s.getChildren() == null) {
			if (!s.getTerminal()) {
				if (s.getSpielzug() > 5) {
					s.setChildren(expandedHome(s));
				} else {
					s.setChildren(expandedAway(s));
				}
			}
			return;
		}

		for (State now : s.getChildren()) {
			neueEbene(now);
		}
	}

	// Hier werden moegliche Zuege von 1-6 expanded
	private LinkedList<State> expandedHome(State s) {
		LinkedList<State> back = new LinkedList<State>();
		int[] mulden = s.getMulden();
		State uebergang;
		int[] cloneMulden;

		for (int i = 0; i < 6; i++) {
			if (mulden[i] == 0) {
				continue;
			}
			cloneMulden = mulden.clone();
			uebergang = erzeugeZustand(s, i, cloneMulden, 1);

			// Hier Spielzug und die Heuristik fuer jeweiligen erzeugten State
			if (this.player == 1) {
				uebergang.setHeuristik((-1) * (uebergang.getKammer2() - uebergang.getKammer1()));
			} else {
				uebergang.setHeuristik(uebergang.getKammer2() - uebergang.getKammer1());
			}

			uebergang.setSpielzug(i);

			back.add(uebergang);
		}

		// Wenn S ein Terminalzustand ist (Heuristik wird neu berechnet)
		if (back.isEmpty()) {
			int[] muldenBerechner = s.getMulden();
			int endeScore = 0;
			for (int i = 7; i < 12; i++) {
				endeScore += muldenBerechner[i];
				muldenBerechner[i] = 0;
			}
			s.addKammer2(endeScore);

			int diff = s.getKammer2() - s.getKammer1();
			if (diff > 0) {
				if (this.player == 1) {
					s.setHeuristik(Integer.MIN_VALUE);
				} else {
					s.setHeuristik(Integer.MAX_VALUE);
				}
			} else if (diff == 0) {
				s.setHeuristik(0);
			} else {
				if (this.player == 1) {
					s.setHeuristik(Integer.MAX_VALUE);
				} else {
					s.setHeuristik(Integer.MIN_VALUE);
				}
			}
			s.setTerminal(true);

			return null;
		}
		return back;
	}

	// Hier werden moegliche Zuege von 7-12 expanded
	private LinkedList<State> expandedAway(State s) {

		LinkedList<State> back = new LinkedList<State>();
		int[] mulden = s.getMulden();
		State uebergang;
		int[] cloneMulden;

		for (int i = 6; i < 12; i++) {
			if (mulden[i] == 0) {
				continue;
			}
			cloneMulden = mulden.clone();
			uebergang = erzeugeZustand(s, i, cloneMulden, 2);

			// Hier Spielzug und die Heuristik fuer jeweiligen erzeugten State
			if (this.player == 1) {
				uebergang.setHeuristik((-1) * (uebergang.getKammer2() - uebergang.getKammer1()));
			} else {
				uebergang.setHeuristik(uebergang.getKammer2() - uebergang.getKammer1());
			}

			uebergang.setSpielzug(i);

			back.add(uebergang);
		}

		// Wenn S ein Terminalzustand ist (Heuristik wird neu Berechnet)
		if (back.isEmpty()) {
			int[] muldenBerechner = s.getMulden();
			int endeScore = 0;
			for (int i = 0; i < 6; i++) {
				endeScore += muldenBerechner[i];
				muldenBerechner[i] = 0;
			}
			s.addKammer1(endeScore);

			int diff = s.getKammer2() - s.getKammer1();
			if (diff > 0) {
				if (this.player == 1) {
					s.setHeuristik(Integer.MIN_VALUE);
				} else {
					s.setHeuristik(Integer.MAX_VALUE);
				}
			} else if (diff == 0) {
				s.setHeuristik(0);
			} else {
				if (this.player == 1) {
					s.setHeuristik(Integer.MAX_VALUE);
				} else {
					s.setHeuristik(Integer.MIN_VALUE);
				}
			}
			s.setTerminal(true);

			return null;
		}
		return back;
	}

	// Wie der neue Zustand aussieht
	private State erzeugeZustand(State s, int i, int[] mulden, int spieler) {
		int einsDazu = i + 1;
		int verteilen = mulden[i];
		mulden[i] = 0;
		while (verteilen > 0) {
			if (einsDazu == 12) {
				einsDazu = 0;
			}

			// hier werden die Mulden im Uhrzeigersinn um ein erhöht
			mulden[einsDazu]++;
			verteilen--;
			einsDazu++;
		}

		// ueberpruefe ob was bei dem Score dazugekommen ist
		int score = 0;
		einsDazu--;

		while (true) {
			if (einsDazu == -1) {
				einsDazu = 11;
			}
			if (mulden[einsDazu] == 2 || mulden[einsDazu] == 4 || mulden[einsDazu] == 6) {
				score += mulden[einsDazu];
				mulden[einsDazu] = 0;
			} else {
				break;
			}
			einsDazu--;
		}

		// welcher SpielerTeilBrett war an dem Zug
		if (spieler == 1) {
			return new State(mulden, s.getKammer1() + score, s.getKammer2());
		} else {
			return new State(mulden, s.getKammer1(), s.getKammer2() + score);
		}
	}

}
