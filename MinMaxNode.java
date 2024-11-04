import java.util.LinkedList;

public class MinMaxNode {
	private int wert;
	private LinkedList<State> zugHistory = new LinkedList<State>();
	
	public MinMaxNode(State zug, int heu) {
		this.zugHistory.add(zug);
		this.wert = heu;
	}
	
	// ErsterZug der wichtig für unsere Betrachtung ist
	public State getErsterZug() {
		return this.zugHistory.get(this.zugHistory.size() - 2);
	}
	
	public LinkedList<State> getZugHist(){
		return this.zugHistory;
	}
	
	public int getHeu() {
		return this.wert;
	}
	
	public void setZug(State s) {
		this.zugHistory.add(s);
	}
}
