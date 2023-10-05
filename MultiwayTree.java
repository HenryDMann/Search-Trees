import java.util.*;
import edu.gwu.algtest.*;
import edu.gwu.debug.*;
import edu.gwu.util.*;  



public class MultiwayTree implements MultiwayTreeSearchAlgorithm, OrderedSearchAlgorithm, SearchAlgorithm, Algorithm {
    //Create an integer to track the current size of the multiway tree
    private int currentSize;
    //Create an integer to track the maximum size of the tree
    private int maxSize;
    //Create a pointer for the root of the tree
    private edu.gwu.algtest.MultiwayNode root;
    //Create a stack to store the nodes
    private Stack<MultiwayNode> nodes = new Stack<>();
    //Create keys arraylist
    private List<Comparable> keys = new ArrayList<>();
    //Create values arraylist
    private List<Object> values = new ArrayList<>();
    //These are to check for duplicates
    private MultiwayNode dfNode = null;
    private int dfIdx = -1;

    public static void main(String[] args) {
        // Initialize Tree
        MultiwayTree tree = new MultiwayTree();

        // Size test 1
        tree.initialize(0);
        System.out.println(tree.getRoot() == null ? "PASS" : "FAIL");

        // Size test 2
        tree.initialize(5);
        System.out.println(tree.getRoot() == null ? "PASS" : "FAIL");

        // Size test 3
        System.out.println(tree.getCurrentSize() == 0 ? "PASS" : "FAIL");

        // Size test 4
        System.out.println(tree.insert(null, "value") == null ? "PASS" : "FAIL");

    }

    public void setPropertyExtractor(int algID, edu.gwu.util.PropertyExtractor prop) {
        //Empty
    }

    public void initialize(int maxSize) {
        //Set the max size
        this.maxSize = maxSize;
        //Clear the root pointer
        root = null;
        //Reset the current size to 0
        currentSize = 0;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    //Lesson learned from last time, function to add a node to enumeration
    private void recNodeEnumerate(MultiwayNode node) {
        //Base Case: If the node is null, then do nothing
        if (node == null) {
            return;
        }

        //Traverse the node
        for (int i = 0; i < node.numEntries; i++) {
            //Add the node's children
            recNodeEnumerate(node.children[i]);
            //Add the node's keys and values
            keys.add(node.data[i].key);
            values.add(node.data[i].value);
        }

        //Call the function recursively
        recNodeEnumerate(node.children[node.numEntries]);
    }


    public java.util.Enumeration getKeys() {
        //Clear the keys from before
        keys.clear();
        //Reenumerate all the ndoes in the tree
        recNodeEnumerate(root);
        //Now return the keys
        return Collections.enumeration(keys);
    }

    public java.util.Enumeration getValues() {
        //Clear the values from before
        values.clear();
        //Reenumerate all the nodes in the tree
        recNodeEnumerate(root);
        //Now return the values
        return Collections.enumeration(values); 
    }

    public java.lang.Object insert(java.lang.Comparable key, java.lang.Object value){
        //If the key is null, return null
        if(key == null) {
            return null;
        }
        //Increment currentSize 
        currentSize++;
        //If the tree is empty
        if(root == null) {
            //Create the new node
            edu.gwu.algtest.MultiwayNode insertingNode = new edu.gwu.algtest.MultiwayNode(maxSize);
            //Populate the new node
            insertingNode.data[0] = new edu.gwu.algtest.ComparableKeyValuePair(key, value);
            insertingNode.numEntries++;
            //Set the inserting node as a leaf and make it the root
            insertingNode.leaf = true;
            root = insertingNode;
            //Return the root
            return root;
        }

        //Find the key being added
        search(key);
        //If the node is found
        if(dfNode != null) {
            //Decrement the size (to make up for it being incremented before)
            currentSize--;
            //Return the previous node
            Object prevValue = dfNode.data[dfIdx].value;
            dfNode.data[dfIdx].value = value;
            return prevValue;
        }

        //Call the next part recursively
        recInsert(key,value, null, null);
        return null;
    }

    public void recInsert(java.lang.Comparable key, java.lang.Object value, edu.gwu.algtest.MultiwayNode left,edu.gwu.algtest.MultiwayNode right){
        //Pop the top node from the stack and set it as the focus node
        edu.gwu.algtest.MultiwayNode focusNode = nodes.pop();
        //Divide the maxsize by 2 (for the sub tree)
        int m = maxSize / 2;

        //If the current node can take more keys
        if(focusNode.numEntries < maxSize){
            //Insert the keys into the node
            intraNodeInsert(focusNode, key, value, left, right);
            //Done
            return;
        }
        //Otherwise the node cannot take more keys, make a new node
        edu.gwu.algtest.MultiwayNode rightNode = new edu.gwu.algtest.MultiwayNode(maxSize + 1);
        //Initialize the median values and keys
        java.lang.Object medianValue = null;
        java.lang.Comparable medianKey = null;
        //If there is data in the focus node
        if (focusNode.data[m] != null) {
            //Set the median values and keys
            medianValue = focusNode.data[m].value;
            medianKey = focusNode.data[m].key;
        } else {
            //Done
            return;
        }
        //Iterate through the focus node
        for(int i = 0; i < m; i++) {
            //Transfer the data and children from the focus node into the right node
            rightNode.data[i] = focusNode.data[i + m + 1];
            rightNode.children[i] = focusNode.children[i + m + 1];
        }
        //Now superimpose the children from the focus node to the right node correctly
        rightNode.children[m] = focusNode.children[2 * m];

        //Set both their numbers of entries
        focusNode.numEntries = m;
        rightNode.numEntries = m;

        //Now it is no longer a leaf, set that to false
        focusNode.leaf = false;

        //If the focus node is the root
        if(focusNode == root) {
            //Create the new root to be inserted
            edu.gwu.algtest.MultiwayNode newRoot = new edu.gwu.algtest.MultiwayNode(maxSize + 1);
            //Populate the new root
            newRoot.data[0] = new edu.gwu.algtest.ComparableKeyValuePair(key, value);
            newRoot.children[0] = left;
            newRoot.children[1] = right;
            newRoot.numEntries = 1;
            //Set the new root as the root
            root = newRoot;
        } else {
            //Otherwise, use the recursive insert function
            recInsert(medianKey,medianValue,focusNode,rightNode);
        }
    }

    //For insertion of a key within an element
    private void intraNodeInsert(edu.gwu.algtest.MultiwayNode node, java.lang.Comparable key, java.lang.Object value, edu.gwu.algtest.MultiwayNode leftChild, edu.gwu.algtest.MultiwayNode rightChild) {
        //Initialize the position as -1
        int position = -1;

        //If the key is less than the first key in a node
        if (key.compareTo(node.data[0].key) < 0) {
            //Set the position to 0
            position = 0;
        } else {
            //Otherwise iterate through the node 
            for (int i = 0; i < node.numEntries - 1; i++) {
                //Check if the key is in between 2 existing keys
                if (key.compareTo(node.data[i].key) > 0 && key.compareTo(node.data[i + 1].key) < 0) {
                    //Set the position to 1 above i and end
                    position = i + 1;
                    break;
                }
            }

            // Otherwise, we know it's the largest
            if (position == -1) {
                //Set the position to the maximum
                position = node.numEntries;
            }
        }
            //When it gets to here, iterate through the node backwards
            for (int i = node.numEntries; i > position; i--) {
                //Going backwards, move all the keys down by 1
                node.data[i] = node.data[i - 1];
                node.children[i + 1] = node.children[i];
            }

            //Insert the new KVP here
            node.data[position] = new edu.gwu.algtest.ComparableKeyValuePair(key, value);
            node.numEntries++;
            //Add the child pointers if a KVP has to move up
            if (leftChild != null && rightChild != null) {
                node.children[position] = leftChild;
                node.children[position + 1] = rightChild;
            }
    }

    public int findPointer(MultiwayNode node, java.lang.Comparable key) {
        //If the key is less than the compared key
        if(key.compareTo(node.data[0].key) < 0){
            //Return 0
            return 0;
        }
        //Case key is greater than the previous key
        if(key.compareTo(node.data[node.numEntries-1].key) > 0) {
            //Return 1 greater than the number of slots in the node
            return node.numEntries + 1;
        }

        //Iterate through the node
        for(int i = 0; i < node.numEntries - 1; i++) {
            //If the key is somewhere in the middle
            if((key.compareTo(node.data[i].key) > 0) && (key.compareTo(node.data[i+1].key) < 0)) {
                //Return the pointer to the child array
                return i + 1;
            }
        }
        //If unfound, return -1
        return -1;
    }

    public edu.gwu.algtest.ComparableKeyValuePair search(java.lang.Comparable key) {
        //If the root or the key is null, return null
        if(root == null || key == null) {
            return null;
        }
        //Save these for duplicate finding
        dfNode = null; 
        dfIdx = -1;
        
        //Clear the nodes list
        nodes.clear();

        //Set the root as the focus node
        edu.gwu.algtest.MultiwayNode focusNode = root;

        //Loop until done
        while(true) {
            //Push the next node
            nodes.push(focusNode);
            //Iterate through the node
            for(int i = 0; i < focusNode.numEntries; i++){
                //If it is found
                if(key.compareTo(focusNode.data[i].key) == 0){
                    //Set the duplicate finder and index
                    dfNode = focusNode;
                    dfIdx = i;
                    //Return the data of the current node
                    return focusNode.data[i];
                }
            }
            //If the focus node is a leaf, return null since there is no child
            if(focusNode.leaf==true){
                return null;
            }

            //Get focus on the next node
            focusNode = focusNode.children[findPointer(focusNode,key)];
        }
    }

    public edu.gwu.algtest.ComparableKeyValuePair minimum(){
        //If there is no tree, return null
        if (root == null) {
            return null;
        }
        //Set the current node as the root
        edu.gwu.algtest.MultiwayNode currentNode = root;

        //Find the left most node
        while (!currentNode.leaf) {
            currentNode = currentNode.children[0];
        }
        //Return the leftmost data of the leftmost node
        return currentNode.data[0];
    }

    public edu.gwu.algtest.ComparableKeyValuePair maximum(){
        //If there is no tree, return null
        if (root == null) {
            return null;
        }

        //Set the current node as the root
        edu.gwu.algtest.MultiwayNode currentNode = root;
        //Find the right most node
        while (!currentNode.leaf) {
                
            currentNode = currentNode.children[currentNode.numEntries];
        }
        //Return the rightmost data of the rightmost node
        return currentNode.data[currentNode.numEntries - 1];
    }

    public java.lang.Object delete(java.lang.Comparable key){
        //Not needed
        return null;
    }

    public java.lang.Comparable successor(java.lang.Comparable key){
        //Not needed
        return null;
    }

    public java.lang.Comparable predecessor(java.lang.Comparable key){
        //Not needed
        return null;
    }


    public MultiwayNode getRoot(){
        //Return the root
        return root;
    }

    public java.lang.String getName(){
        return "Henry Mann's Multiway Search Tree";
    }

}