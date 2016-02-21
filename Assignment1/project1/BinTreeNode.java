
public class BinTreeNode implements Comparable<BinTreeNode>{

	private int value;
	private BinTreeNode left;
	private BinTreeNode right;
	private int count;
	
	BinTreeNode(int x, int c){
		this.value = x;
		this.left = null;
		this.right = null;
		this.count = c;
	}
	
	BinTreeNode(BinTreeNode l, BinTreeNode r){
		this.value = 0;
		this.left = l;
		this.right = r;
		this.count = l.getCount() + r.getCount();
	}

	public void setValue(int x)  { this.value = x; }
	public void setLeft(BinTreeNode l)  { this.left  = l; }
	public void setRight(BinTreeNode r) { this.right = r; }
	public void setCount(int c)  { this.count = c; }

	public int getValue()  { return this.value; }
	public BinTreeNode getLeft()  { return this.left; }	
	public BinTreeNode getRight() { return this.right; }
	public int getCount()  { return this.count; }

	@Override
	public int compareTo(BinTreeNode n) { return Integer.valueOf(this.count).compareTo(Integer.valueOf(n.getCount())); }
}	
