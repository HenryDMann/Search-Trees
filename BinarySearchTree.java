import java.util.*;
import edu.gwu.algtest.*;
import edu.gwu.debug.*;
import edu.gwu.util.*;  


public class BinarySearchTree implements Algorithm, SearchAlgorithm, OrderedSearchAlgorithm, TreeSearchAlgorithm, Enumeration<String>{

    //A global variable to track the root of the tree
    private edu.gwu.algtest.TreeNode root;  
    //A global variable to track the size of the tree
    private int currentSize;  
    //A global variable to track the maximum size of the tree
    private int maxSize; 
    //A global array to track the enumeration
    private String[] enumerationArray;
    //A global pointer to the current enumeration index
    private int currentIdx;

    public static void main(String[] args) {
        //Create a BST for testing
        BinarySearchTree testBinarySearchTree = new BinarySearchTree();
        testBinarySearchTree.initialize(500);

        //Empty Tree Test
        System.out.print("Empty Tree Test: ");
        System.out.println(0 == testBinarySearchTree.getCurrentSize());

        //Node insert and check size
        testBinarySearchTree.insert(10, "Melbourne");
        System.out.print("Tree size after insertion test: ");
        System.out.println(1 == testBinarySearchTree.getCurrentSize());

        //Search test
        edu.gwu.algtest.ComparableKeyValuePair result = testBinarySearchTree.search(10);
        System.out.print("Search Result (Should be a key, value pair): ");
        System.out.println(result);

        //Null seach test
        result = testBinarySearchTree.search(11);
        System.out.print("Search Result (For null key): "); 
        System.out.println(result == null);

        //Size check 2
        testBinarySearchTree.insert(50, "Sydney");
        testBinarySearchTree.insert(5, "Perth");
        testBinarySearchTree.insert(45, "Darwin");
        System.out.print("Size check 2: ");
        System.out.println(testBinarySearchTree.getCurrentSize() == 4);

        //Minimum check
        result = testBinarySearchTree.minimum();
        System.out.println("Search Result (Should be the minimum key, value pair): ");
        System.out.println(result);

        //Maximum check
        result = testBinarySearchTree.maximum();
        System.out.print("Search Result (Should be the maximum key, value pair): ");
        System.out.println(result);

        //Deletion check
        Object deletedValue = testBinarySearchTree.delete(50);
        System.out.print("Deleted node: ");
        System.out.println(deletedValue.equals("Sydney"));

        //Deletion check for a null key
        deletedValue = testBinarySearchTree.delete(69);
        System.out.print("Try to delete a null key: ");
        System.out.println(deletedValue == null);

        //Successor Test
        Comparable successorKey = testBinarySearchTree.successor(10);
        System.out.print("Successor Test: ");
        System.out.println("45".equals(successorKey.toString()));
    }


    public void setPropertyExtractor(int algID, edu.gwu.util.PropertyExtractor prop){
        //Nothing Here
    }

    //Initialize the tree to a certain size
    public void initialize(int maxSize){
        //Set the root to null
        this.root = null;
        //Set the current size to 0
        this.currentSize = 0;
        //Set the max size to the specified amount
        this.maxSize = maxSize; 
        //Set the enum array to an empty array of the desired size
        this.enumerationArray = new String[maxSize];
        //Set the index to 0
        this.currentIdx = 0;
    }

    //Find the current size of the array
    public int getCurrentSize(){
        return this.currentSize;
    }

    @Override
    public boolean hasMoreElements() {
        //Check if there are more elements above the current pointer
        return currentIdx < currentSize;
    }


    public String nextElement() {
        //Return the next element in the enumeration array 
        return this.enumerationArray[currentIdx++];
    }


    private void RecursiveDFSTraversal(edu.gwu.algtest.TreeNode node, List<String> list) {
        //Base case, when the end of the tree is reached, return
        if (node == null) {
            return;
        }
        //Recursiveley call this function on the left side
        RecursiveDFSTraversal(node.left, list);
        //Append the current node to the array
        list.add(node.key.toString());  
        //Recursively call this function on the right side
        RecursiveDFSTraversal(node.right, list);
    }

    public java.util.Enumeration getKeys(){
        //Made this entire function elsewhere since it used many times
        //Create an array list to be used to fill out the enumeration array
        List<String> enumerationArrayPrep = new ArrayList<>();
        //Use the recursive traveral function to go through the array
        RecursiveDFSTraversal(root, enumerationArrayPrep);
        //Set the real enum array to the array being prepped
        enumerationArray = enumerationArrayPrep.toArray(new String[0]);
        //Set the index back to 0
        currentIdx = 0; 
        return this;
    }

    //Return and Enumertation that can iterate thru values
    @Override
    public java.util.Enumeration getValues(){
        //Create an array list to be used to fill out the enumeration array
        List<String> enumerationArrayPrep = new ArrayList<>();
        //Use the recursive traveral function to go through the array
        RecursiveDFSTraversal(root, enumerationArrayPrep);
        //Set the real enum array to the array being prepped
        enumerationArray = enumerationArrayPrep.toArray(new String[0]);
        //Set the index back to 0
        currentIdx = 0; 
        return this;
    }

    //This function has been used previously
    public void fullEnumerationArray() {
        //Create an array list to be used to fill out the enumeration array
        List<String> enumerationArrayPrep = new ArrayList<>();
        //Use the recursive traveral function to go through the array
        RecursiveDFSTraversal(root, enumerationArrayPrep);
        //Set the real enum array to the array being prepped
        enumerationArray = enumerationArrayPrep.toArray(new String[0]);
        //Set the index back to 0
        currentIdx = 0; 
    }

    public java.lang.Object insert(java.lang.Comparable key, java.lang.Object value){
        //Make the node to be inserted
        edu.gwu.algtest.TreeNode insertingNode = new edu.gwu.algtest.TreeNode(key, value);
        //If the tree is already full or the key is null, return null
        if (currentSize >= maxSize) {
            return null; 
        } else if (key == null) {
            return null;
        //If the tree is empty, set the new node as the root
        } else if (root == null) {
            root = insertingNode;
            currentSize++;
            return null;
        }
        
        //Otherwise, populate the node being insert
        insertingNode.left = null;
        insertingNode.right = null;
        
        //Then start with the root
        edu.gwu.algtest.TreeNode examNode = root;
        //Create a variable to track the parent
        edu.gwu.algtest.TreeNode parent = null;

        //Loop until done
        while(true) {
            //Set the current node as the parent
            parent = examNode;
            //If the node being examined's key is less than the key, go left
            if(key.compareTo(examNode.key) < 0) {
                //Move the node being examined to the left child
                examNode = examNode.left;
                //If that left child is null, insert the node being inserted here
                if(examNode == null) {
                    //Set the parent's left pointer to the node being inserted
                    parent.left = insertingNode;
                    //Set the parent as the parent of the node being inserted
                    insertingNode.parent = parent;
                    //Increment the size of the current array
                    currentSize++;
                    //Redo the enumeration array
                    fullEnumerationArray();
                    //Finished, end the while loop
                    return null;
                }
            }

            //If the node being examined's key is greater than the key, go right
            else if(key.compareTo(examNode.key) > 0) {
                //Move the node being examined to the right child
                examNode = examNode.right;
                //If that right child is null, insert the node being inserted here
                if(examNode == null) {
                    //Set the parent's right pointer to the node being inserted
                    parent.right = insertingNode;
                    //Set the parent as the parent of the node being inserted
                    insertingNode.parent = parent;
                    //Increment the size of the current array
                    currentSize++;
                    //Redo the enumeration array
                    fullEnumerationArray();
                    //Finished, end the while loop
                    return null;
                }
            }
        }
    }

    public edu.gwu.algtest.ComparableKeyValuePair search(java.lang.Comparable key) {
        //Set the root as the node being examined
        edu.gwu.algtest.TreeNode examNode = root;

        //If the key input is null, then return null
        if(key == null) {
            return null;
        }

        //Until the node being searched is found
        while(examNode != null) {
            //If the node being searched is less, go to the left child
            if(key.compareTo(examNode.key) < 0){
                examNode = examNode.left;
            //If it is greater, go to the right child
            }else if(key.compareTo(examNode.key) > 0){
                examNode = examNode.right;
            //Otherwise it is found, and return this node
            }else{
                return new edu.gwu.algtest.ComparableKeyValuePair(examNode.key, examNode.value);
            }
        }

        //If it gets here, the entire tree was run through without being found
        return null;

    }

    public edu.gwu.algtest.ComparableKeyValuePair minimum() {
        //If the root is null, there is no tree, so return null
        if(root == null) {
            return null;
        }

        //Set the root to the node currently being examined
        edu.gwu.algtest.TreeNode examNode = root;
        //Keep going left until the leftmost node is found
        while(examNode.left!=null) {
            examNode = examNode.left;
        }
        //Return the leftmost node with no left child.
        return new edu.gwu.algtest.ComparableKeyValuePair(examNode.key, examNode.value);

    }

    public edu.gwu.algtest.ComparableKeyValuePair maximum() {
        //If the root is null, there is no tree, so return null
        if(root == null){
            return null;
        }
        
        //Set the root to the node currently being examined
        edu.gwu.algtest.TreeNode examNode = root;
        //Keep going right until the rightmost node is found
        while(examNode.right!=null) {
            examNode = examNode.right;
        }
        //Return the rightmost node with no right child.
        return new edu.gwu.algtest.ComparableKeyValuePair(examNode.key, examNode.value);

    }

    //The delete function that made me cry
    public java.lang.Object delete(java.lang.Comparable key) {
        //Use the search function to find the node being deleted
        edu.gwu.algtest.ComparableKeyValuePair result = search(key);
        //If that node is not found, no deletion can occur
        if (result == null) {
            return null;
        }
        
        //Create the target for deletion
        edu.gwu.algtest.TreeNode targetNode = null;
        //Set the node being examined to the root
        edu.gwu.algtest.TreeNode examNode = root;
        //Until the current node being examined is null, keep going
        while (examNode != null) {
            //If the examNode is greater than the target node, go left
            if (key.compareTo(examNode.key) < 0) {
                examNode = examNode.left;
            //If the examNode is less than the target node, go right
            } else if (key.compareTo(examNode.key) > 0) {
                examNode = examNode.right;
            //If the node is found, set it as the target node and end the loop
            } else {
                targetNode = examNode;
                break;
            }
        }
        
        //Save the value of the target node
        Object value = targetNode.value;
        //Check if the targetNode is the root
        boolean isRoot = (targetNode == root);

        
        //The good ending, the node has no children
        if(targetNode.left == null && targetNode.right == null){
            //If the target node is the root (the best outcome)
            if(isRoot) {
                //Just delete the tree
                root = null;
            //Otherwise (still a good ending)
            } else {
                //If the target node is a left child, delete the parent's left pointer
                if(targetNode.parent.left == targetNode) {
                    targetNode.parent.left = null;
                //Otherwise delete the parent's right child
                } else {                
                    targetNode.parent.right = null;
                } 
            }
        }
        
        //The bad ending, the node has 1 child
        if((targetNode.left == null && targetNode.right!=null) || (targetNode.left != null && targetNode.right==null)) {
            //If it is the root, then everything is good
            if(isRoot) {
                //If it has a left child (then the left child is the only child)
                if (targetNode.left != null) {
                    //Then set the root to the left child
                    root = targetNode.left;
                } else {
                    //Otherwise set the root to the right child
                    root = targetNode.right;
            }
            
            //Otherwise if not the root
            } else {
                //If the target node is the left child
                if(targetNode.parent.left == targetNode) {
                    //And the target node has a left child
                    if(targetNode.left != null) {
                        //Set the parent's left child to the target's left child, cutting out the target
                        targetNode.parent.left = targetNode.left;
                    } else {
                        //Otherwise, the target has a right child and the parent's left child should be set to the target's right child
                        targetNode.parent.left = targetNode.right;
                    }
                //Otherwise the target node is the right child
                } else {
                    //And the target node has a right child
                    if(targetNode.right != null) {
                        //Set the parent's right child to the target's right child, cutting out the target
                        targetNode.parent.right = targetNode.right;
                    } else {
                        //Otherwise, the target has a left child and the parent's right child should be set to the target's left child
                        targetNode.parent.right = targetNode.left;
                    }
                }
            }
        }
        
        //The hell ending, the node has 2 children
        if(targetNode.left != null && targetNode.right != null) {
            //Set the target node to be the node that will move
            edu.gwu.algtest.TreeNode successorNode = targetNode;
            //Assuming the target node is not the root
            if(!isRoot) {
                //Set the node that will move to be the right child
                successorNode = successorNode.right;

                //Find the successor node
                while(successorNode.left != null) {
                    successorNode = successorNode.left;
                }
                
                //If the successor does have a right child
                if(successorNode.right != null) {
                    //If the successor is a right child
                    if(successorNode.parent.right == successorNode) {
                        //Set the successor's parent's right child to the successor's right child
                        successorNode.parent.right = successorNode.right;
                    //Otherwise the successor is a left child
                    } else {
                        //Set the successor's parent's left child to the successor's right child
                        successorNode.parent.left = successorNode.right;
                    }
                }

                //Now that the successor node has been identified, swap it with the target node in it's parent's pointers
                if(targetNode.parent.right == targetNode) {
                    targetNode.parent.right = successorNode;
                }else{
                    targetNode.parent.left = successorNode;
                }
            }

            //Handle case where the successor node has a right child
            if(successorNode.right != null){
                //If the successor node is a right child
                if(successorNode.parent.right == successorNode) {
                    //Set the successor node's parent's right child to the successor node's right child, cutting out the successor node
                    successorNode.parent.right = successorNode.right;
                } else {
                    //Otherwise set the successor's parent's left child to the successor node's right child
                    successorNode.parent.left = successorNode.right;
                }
            }

            //Now finally cycle the successor node to the root
            root = successorNode;
        }

        //Redo the enumeratuion
        fullEnumerationArray();
        //Decrement the size counter
        currentSize--;
        //Return the value deleted
        return value;

    }


    //A function to find the successor node (this would've been useful earlier)
    public java.lang.Comparable successor(java.lang.Comparable key) {
        //If either the key or the root is null, return null
        if(key == null||root==null) {
            return null;
        }
        
        //Set the root as the node being examined
        edu.gwu.algtest.TreeNode examNode = root;
        //Find the node of the key
        while(examNode != null) {
            //If the key is less than the examined node, go left
            if(key.compareTo(examNode.key) < 0) {
                examNode = examNode.left;
            //If the key is greater than the examined node, go right
            } else if(key.compareTo(examNode.key) > 0) {
                examNode = examNode.right;
            //Otherwise, we found the node
            } else {
                break;
            }
        }

        //Now if the node being examined is null, or it's right child is null, return null
        if(examNode == null || examNode.right == null){
            return null;
        }

        //Otherwise, go right
        examNode = examNode.right;


        //Now go left until the leftmose node of this subtree is found
        while(examNode.left != null){
            examNode = examNode.left;
        }

        //Now return the key of this successor
        return examNode.key;
    }


    public java.lang.Comparable predecessor(java.lang.Comparable key){
        //If either the key or the root is null, return null
        if (key == null || root == null) {
            return null;
        }

        //Set the root as the node being examined
        edu.gwu.algtest.TreeNode examNode = root;
        //Create an ancestor pointer
        edu.gwu.algtest.TreeNode ancestor = null;
    
        //Until the node being examined is null
        while (examNode != null) {
            //If the key is less than the examined node, go left
            if (key.compareTo(examNode.key) < 0) {
                examNode = examNode.left;
            //If the key is greater than the examined node, go right
            } else if (key.compareTo(examNode.key) > 0) {
                ancestor = examNode;
                examNode = examNode.right;
            //Otherwise, we found the node
            } else { 
                break;
            }
        }

        //If the key has a left child
        if (examNode != null && examNode.left != null) {
            //Move to the left
            examNode = examNode.left;
            //Then go right until the rightmost node is found
            while (examNode.right != null) {
                examNode = examNode.right;
            }
            //Return the rightmost node
            return examNode.key;
        }

        // Otherwise, the ancestor is the predecessor
        if (ancestor != null) {
            return ancestor.key;
        }

        //In case no predecessor is found
        return null;

    }

    public edu.gwu.algtest.TreeNode getRoot() {
        return root;
    }

    public java.lang.String getName(){
        return "Henry Mann's Binary Search Tree";
    }
}