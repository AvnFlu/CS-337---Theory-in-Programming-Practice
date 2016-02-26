//Michael Both
//mjb3299

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;


/*
*  LEMPEL-ZIV COMPRESSION
*  This program will take in any input file and read each character/byte of that file as an integer.
*  As characters are read, the program remembers if a pattern of characters has already been used before
*  and will search through a tree of nodes to find if a branch of a node contains the next character being read.
*  If there is no branch to represent the pattern currently being read.  The program will add a branch that
*  represents this new pattern to the tree, then write out the index of the last node it reached with the
*  last character of this new pattern, then return to the root node and start searching for patterns again
*
*
*  For example, the input "aabbaa" will be read as such:
*
* COMPRESS
*
*   "a"  - new pattern, create node 1, write to output file, clear pattern and start over
*   "a"  - used pattern, go to node 1, add next character to pattern
*   "ab" - new pattern, create node 2, write to output file, clear pattern and start over
*   "b"  - new pattern, create node 3, write to output file, clear pattern and start over
*   "a"  - used pattern, go to node 1, add next character to pattern
*   "aa" - new pattern, create node 4, write to output file, clear pattern and start over
*  
*
*  which creates the following tree
*
*  (0, [ROOT]) ---- (3, "b")
*     |
*     |
*  (1, "a")   ----- (4, "aa")
*     |
*     |
*  (2, "ab")
*
*
*  and gives the final output
*
*  (0, 'a')
*  (1, 'b')
*  (0, 'b')
*  (1, 'a')
*
*  this output will be used by the decompress function to build the exact same tree while writing
*  back the file in its original format, like this:
*
* DECOMPRESS
*
*  create node 1 from node 0 - write "a"
*  create node 2 from node 1 - write "ab"
*  create node 3 from node 0 - write "b"
*  create node 4 from node 1 - write "aa"
*
*  giving us the original "aabbaa" we started with
*
*
*
* NOTE
*
*  This program requires MultiNode.java to be in the same directory to run
*
*  If the user does not enter add a file name to compress/decompress upon execution,
*  they will be prompted to enter one.
*
*  Files ending in "*.myZ" will be decompressed, all other files will be compressed to "*.myZ".
*
*
*
* IMPORTANT VARIABLES
*
*  Decompress.list    - stores all nodes in the order they were created with the root node at 0
*  MultiNode.values   - the entire pattern the current node represents
*  MultiNode.subNodes - all branches/child nodes of the current node
*  MultiNode.index    - the current node's index number
*
*/


public class LZ{

    public static void main(String[] args) throws Exception{
        
        //if no input was given through the command prompt, ask the user to enter a file name 
        if(args.length == 0){
            System.out.print("Please enter a filename to compress to \"[FILE].myZ\".\n" + 
                             "Files ending in \"*.myZ\" will automatically be decompressed as [FILE].myZ.unZ: ");
                                
            Scanner sc = new Scanner(System.in);
            String path = new String(sc.next());
            
            if(path.endsWith(".myZ")) Decompress(path); 
            else Compress(path);
            
            sc.close();
            return;
        }
        
        //use the filename included in the command prompt if one was included
        if(args[0].endsWith(".myZ")) Decompress(args[0]); 
        else Compress(args[0]);
    }
    

    //COMPRESS: If the file ends in anything except "*.myZ", create a compressed format of the file
    //Output file will be stored as "[FILENAME].myZ"
    public static void Compress (String path) throws Exception {
        try {
            File file = new File(path);  //locate the input file entered since FileReader cannot use a path string
            FileReader in = new FileReader(file);
            FileWriter out = new FileWriter(file.getName() + ".myZ");

            int size = 0;     //SIZE stores the number of nodes in the tree
            int ch = 0;       //CH holds the integer value of the next character from the file
            
            MultiNode root = new MultiNode();
    
            //build the trie until there is no more input
            while(ch != -1){
                
                MultiNode node = root;
                MultiNode next = node.nextNode(ch = in.read());
                int lastIndex = 0;

                //serach down the trie from the top to find the next pattern
                while(next != null){
                    node = next;
                    lastIndex = node.getIndex();
                    next = node.nextNode(ch = in.read());
                } 

                //add a new branch
                MultiNode n = new MultiNode(node.getList(), ch, ++size);
                node.addNode(n);
                        
                //write to the output file as an index-value pair, then break from the loop
                out.write(lastIndex + " " + n.getValue() + "\n");
                        
            }
    
            System.out.println("\n\"" + file.getName() + ".myZ\" created");
            in.close();
            out.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("File Not Found");
        }
    }


    //DECOMPRESS: If the file ends in "*.myZ", return the file to its orignial format
    //Output file stored as "[FILENAME].myZ.unZ" to prevent overwritting the original file
    public static void Decompress (String path) throws Exception {
        try {
            File file = new File(path);  //locate the input file entered since FileReader cannot use a path string
            FileReader in = new FileReader(file);
            FileWriter out = new FileWriter(file.getName() + ".unZ");
            Scanner sc = new Scanner(in);  //used to read integers directly from the input file

            //list contains all nodes in the trie, arranged by their index number
            ArrayList<MultiNode> list = new ArrayList<MultiNode>();

            int size = 0;       //SIZE stores the number of nodes in the tree
            int index = 0;      //INDEX tells which node in the trie to go to
            int ch = 0;         //CH holds the integer value of the next character from the file

            MultiNode root = new MultiNode();
            list.add(root);

            //builds the trie and the file
            while (sc.hasNextInt()) {

                index = sc.nextInt();
                ch = sc.nextInt();

                //create node and add to trie
                MultiNode node = new MultiNode(list.get(index).getList(), ch, ++size);
                list.add(node);
                list.get(index).addNode(node);

                //write output
                if(ch != -1)
                    out.write(node.toString());
                //last write of the program, exludes the '-1' character
                else
                    out.write(list.get(index).toString());
            }

            System.out.println("\n\"" + file.getName() + ".unZ\" created");
            sc.close();
            in.close();
            out.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("File Not Found");
        }
    }
}
