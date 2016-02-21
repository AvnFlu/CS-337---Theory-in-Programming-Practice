import java.util.ArrayList;

/* This is the MultiNode class for the LZ comp/decomp file.
 * Labeled as MultiNode for the numerous branches each node may have.
 * Contains all characters that lead up to here from the root,
 * the index number (or number in which it was placed into the
 * trie list, and an initialy empty list to contain all
 * other nodes that may branch off from here
*/

public class MultiNode {

	private ArrayList<Integer> values;     //contains the characters of the parent node plus this node's new character
	private ArrayList<MultiNode> subNodes; //subNodes will contain all Nodes that branch off from this one
    private int index;                     //identifies the node's location in the trie


	//default constructor used for the root node
	MultiNode(){
		this.values = new ArrayList<Integer>();
		this.subNodes = new ArrayList<MultiNode>();
        this.index = 0;
	}

	//constructor
	MultiNode(ArrayList<Integer> list, int ch, int idx){

		//add all the characters from the parent node followed by the new character being added
		this.values = new ArrayList<Integer>(list);
		this.values.add(ch);

		this.subNodes = new ArrayList<MultiNode>();
        this.index = idx;
	}


	//checks if there is a node branching off from this one that holds the next immediate value
	public MultiNode nextNode(int ch){
		for(int i = 0; i < subNodes.size(); i++){
			if(this.subNodes.get(i).getValue() == ch)
				return this.subNodes.get(i);
		}
		return null;  //no child node was found for the next character
	}

	//gives a String of all the values to the path of this node
	public String toString(){
		StringBuffer strBuf = new StringBuffer();
		for(int i = 0; i < values.size(); i++){
			strBuf.append((char) values.get(i).intValue());
		}
		return strBuf.toString();
	}

	public int getValue() { return this.values.get(values.size() - 1); }  //returns the new character of the node
	public int getIndex() { return this.index; }                          //returns the node's location in the trie
	public void addNode(MultiNode n) { this.subNodes.add(n); }            //attach a new child node to this one
	public ArrayList<Integer> getList() { return this.values; }           //return all characters that led to this node
}	
