package models;

public class Edge {
	public int src;
	public int dst;

	public Edge(int src, int dst) {
		this.src = src;
		this.dst = dst;
	}

	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	public int getDst() {
		return dst;
	}

	public void setDst(int dst) {
		this.dst = dst;
	}

}
