/**
 * Created by KushalAdhvaryu on 2/28/17.
 */

// Basic implementation is done using AVL Tree and using modCount value as reference
// whenever tree structure is touched by any of add, remove, set methods.

// Complexity of below implementation is order of log(n) since insert, delete , update operation
// in AVL are performed in log(n) time, as well as Index search and updatation of values of number of children and
// height takes log(n) time.

import java.lang.*;
import java.util.*;

// Inheritance of Abstract list to handle modCount.
public class FastList<T> extends AbstractList<T> {



    // Intialize root of tree for class Node.
    private Node root;

    // To resolve errors.
    @Override
    public T get(int index) {
        return findNode(index).value;
    }

    // To resolve errors.
    @Override
    public int size() {
        if(root == null){
            return 0;
        }
        return root.childCount + 1;
    }

    // Add Object at end of the list.
    // As per Standard Java list method this function returns boolean status.
    public boolean add(T object){
        // Getting size of tree which is index at the end where new object has to be inserted.
        int index = size();
        Node new_Node = new Node(object, index);
        // Calling function to Handle insertion of node in avl tree.
        add(new_Node);
        return true;
    }

    // Add Object at specified index.
    // As per Standard Java method this function returns void.
    public void add(int index, T object){
        // Checking if given index is within range of list else it is out of bounds.
        if(index < 0 || index > size()) {
            throw new IndexOutOfBoundsException(index + " is not a valid index for inserting an element, must be between 0 and size() inclusive.");
        }
        Node new_Node = new Node(object, index);
        // Calling function to Handle insertion of node in avl tree.
        add(new_Node);
    }

    // Inserting Node in AVL Tree.
    protected void add(Node new_Node){
        // Base case
        if(root == null){
            root = new_Node;
        } else {
            // Starting from root traverse left or right branches and insert node at leaf as per Binary search tree insertion.
            Node cur_Node = root;
            while(cur_Node != null) {
                // Comparing two nodes based on index. In node class.
                int dif = compare(new_Node, cur_Node);

                // Index is less than current node hence traverse or insert in left branch.
                if(dif < 0){
                    // If is leaf or has right branch and no left branch insert node.
                    if(cur_Node.left == null){
                        cur_Node.setLeft(new_Node);
                        break;
                    } else {
                        // As per BST Insertion keep traversing down the tree for internal node.
                        cur_Node = cur_Node.left;
                    }
                } else { // Index is less than current node hence traverse or insert in left branch.
                    // If is leaf or has left branch and no right branch insert node.
                    if(cur_Node.right == null){
                        cur_Node.setRight(new_Node);
                        break;
                    } else {
                        // As per BST Insertion keep traversing down the tree for internal node.
                        cur_Node = cur_Node.right;
                    }
                }
            }
        }
        // Incrementing mode count since status of tree is now changed.
        // This will help to calculate index stack and also indicate the state of tree is now changed.
        // Hence whenever new value will be added affected node and new node will have latest mode count.
        // Affected nodes are those which are visited for comparision and index evaluation.
        modCount++;
    }

    // Method to compare
    protected int compare(Node new_Node,Node cur_Node){
        // If indexes are different we get value of dif not equal to 0.
        int dif = new_Node.getIndex() - cur_Node.getIndex();
        // Consider a case where node is set at index 0.
        if(dif == 0){
            // In such a case insert node as Left child.
            dif = -1;
        }
        return dif;
    }

    // Remove Object at specified index.
    public T remove(int index){
        // Since Index values might not be consistent computing value of node based on index value.
        Node new_Node = findNode(index);
        T temp = new_Node.value;
        remove(new_Node);
        return temp;
    }

    protected void remove(Node rm_Node){
        // Case where rm_Node is leaf node of tree.
        boolean leaf = rm_Node.left == null && rm_Node.right == null;
        // If leaf remove from tree other if its only single node empth tree.
        if (leaf){
            Node parent = rm_Node.parent;
            if ( parent == null ){
                root = null;

            } else {
                rm_Node.del_Leaf();

            }
        } else if(rm_Node.left != null && rm_Node.right != null){ // rm_Node is at internal node.
            Node next = rm_Node.follower(); // Identify the next node after this node.
            // Update value of next node.
            rm_Node.value = next.value;
            remove(next);
        } else if(rm_Node.left != null){ // rm_Node has only Left branch.
            rm_Node.left.updateParent();
        } else {  // rm_node has only right branch.
            rm_Node.right.updateParent();
        }
        modCount++;
    }

    // Set Object at specified index.
    public T set(int index, T element){
        // First remove node from tree since it wil rebalance tree with updated values this will take log N.
        T removed = remove(index);
        // Now adding node at given index again in log N time.
        add(index, element);
        return removed;
    }

    // Finds Node at Index.
    protected Node findNode(int index){
        // Throw exception if invalid index.
        if(index < 0 || index >= size()){
            throw new IllegalArgumentException( "Invalid index" + index );
        }
        // Starting from root, moving down the tree.
        Node cur = root;
        //Number of element which are smaller than root is size of left sub tree.
        int leftSize = (cur.left == null) ? 0 : cur.left.childCount + 1;
        while(cur!= null){
            if(leftSize == index){
                break;
            }
            // Step into Left subtree.
            if(leftSize > index){
                cur = cur.left;
                // Reducing size by one since stepping down.
                leftSize--;
                // If right subtree exist index is difference of index and right subtree size.
                if(cur.right != null){
                    leftSize -= cur.right.childCount + 1;
                }
            } else { // Step into Right subtree.
                leftSize++;
                cur = cur.right;
                // If left subtree exist index is sum of index and left subtree size.
                if(cur.left != null){
                    leftSize += cur.left.childCount + 1;
                }
            }
        }
        return cur;
    }

    // Re balance the AVL Tree.
    protected void reBalanceTree(Node newNode){
        Node cur = newNode;
        while(cur!= null){
            //Find difference between heights of left and right subtree.
            int balanceFactor = cur.getBalance();
            // height of left tree is higher.
            if(balanceFactor == 2){
                // Left Right Case.
                if(cur.left.getBalance() == -1){
                    // First rotation left rotate Right child.
                    Node temp = cur.left.right;
                    temp.rotateLeft();
                }
                // Left Left Case.
                // One single rotation required which is right otherwise its second rotation of case Left Right.
                cur.left.rotateRight();
            } else if(balanceFactor == -2){
                // Right Left Case.
                // Identify if left branch of node is longer.
                if(cur.right.getBalance() == 1){
                    // First rotation right rotate child.
                    Node temp = cur.right.left;
                    temp.rotateRight();
                }
                // Right Right Case.
                // One single rotation required which is right otherwise its second rotation of case Left Right.
                cur.right.rotateLeft();
            }
            // Maintaining latest modified root.
            if(cur.parent == null){
                root = cur;
                break;
            } else {
                // Recursing up the tree towards node.
                cur = cur.parent;
            }
        }
    }



    protected class Node  {

        private T value; // Value of Node.
        private Node left; // Left Pointer to node.
        private Node right; // Right pointer to node.
        private Node parent; // Parent to decide where to join node when some child is deleted.
        private int height; // In AVL Tree to maintain BST property need to keep track of height balance at each node.
        private int childCount; // Helped parameter to calculate index and also get size of tree when used by root node.
        private int index;  // TO store index of node in tree. But not necessarily up to date hence we calculate every time.
        private int curModCount; // To keep track of when latest modification was done.


        // Constructor
        Node(T obj, int initialIndex){
            this.value = obj;
            index = initialIndex;
            curModCount = modCount;
        }
/*
        // Evaluate index of node in AVL Tree, as we evaluate index of nodes in tree we update them irrespective of current value.
        protected int getIndex(){
            // Backtracking path from the given node up to parent until root is found.
            // We store entire path in stack and then for each element in stack set index as per existing tree structure.
            // If this is called by add method current tree structure is equal to before node is inserted.
            Stack<Node> stk = new Stack<>();
            // Set current equal to node which has called this function.
            Node cur = this;
            // Storing path up-to root and also compare initial mod Count value with modeCount value at current state in tree.
            while(cur != null && cur.curModCount != modCount ){
                stk.push(cur);
                cur =  cur.parent;
            }
            // Evaluating index for each element in stack and updating its index parameter of node.
            while(!stk.isEmpty()){
               cur = stk.pop();
                Node parent = cur.parent;
                // Base Case when node is Root its parent is null
                if(parent == null){
                    Node left = cur.left;
                    cur.index = (left== null) ? 0 : left.childCount + 1;
                } else {
                    // Case where Node is internal node or leaf node.
                    // In BST property is Left sub tree has all nodes value less than root and Right sub tree
                    // has all node values greater than root.

                    //For Right child Index is given by summation between parent and number of nodes
                    // in left arm of current node.
                    if(cur.isRight()){
                        Node left = cur.left;
                        int lSize;
                        if (left == null){
                            lSize = 1;
                        }else{
                            //Size of subtree  + 1 considering it self.
                            lSize = 1 + left.childCount + 1;
                        }
                        cur.index = parent.index + lSize;
                    } else {
                        //Other case is for left child where Index is given by difference between parent and number of nodes
                        // in right arm of current node.
                        Node right = cur.right;
                        int rSize;
                        if (right == null){
                            rSize = 1;
                        }else{
                            //Size of subtree  + 1 considering it self.
                            rSize = 1 + right.childCount + 1;
                        }
                        cur.index = parent.index - rSize;

                    }
                }
                // Updating Value of mod count of node as it is now been visited with latest state.
                cur.curModCount = modCount;
            }
            return index;
        }
*/
protected int getIndex(){

        Node cur = this;
        //Verifies if mod-count of node is not as per latest mod count.
        if(cur.curModCount != modCount) {

            //It checks for parent of node whose index needs to be evaluated.
            // If it is null the node is root node hence index is either 0  or left subtree size.
            if (cur.parent == null) {
                // Is root node in such case index will 0 if left subtree is null else it will size of left subtree +1.
                Node left = cur.left;
                cur.index = (left == null) ? 0 : left.childCount + 1;
            } else {
                //Case when node is right child, its index is evaluated based on
                // summation of node's parent and size of left subtree.
                Node parent = cur.parent;
                if (cur.isRight()) {
                    Node left = cur.left;
                    int lSize;
                    if (left == null) {
                        lSize = 1;
                    } else {
                        lSize = 1 + left.childCount + 1;
                    }
                    cur.index = parent.index + lSize;
                } else {
                    //Case when node is in left child, its index is evaluated based on
                    // difference between node's parent and size of right subtree.
                    Node right = cur.right;
                    int rSize;
                    if (right == null) {
                        rSize = 1;
                    } else {
                        rSize = 1 + right.childCount + 1;
                    }
                    cur.index = parent.index - rSize;
                }
            }
            // Updates current ModCount to keep track.
            cur.curModCount = modCount;
        }
    return index;
}

        // Setting node as left child of given node.
        protected void setLeft(Node lNode){
            if(lNode != null){
                lNode.parent = this;
            }
            left = lNode;
            resetValues();
            reBalanceTree(this);
        }

        // Setting node as right child of given node.
        protected void setRight(Node rNode){
            if(rNode != null){
                rNode.parent = this;
            }
            right = rNode;
            resetValues();
            reBalanceTree(this);
        }

        // Update the values of height and number of children for path starting from node where its inserted to root.
        protected void resetValues(){
            Node cur = this;
            // Starting from leaf until root is reached.
            while(cur != null){
                // Check if current node is leaf or not.
                if(cur.left == null && cur.right == null){
                    cur.height = 0;
                    cur.childCount = 0;
                } else {
                    int lHeight, rHeight, lSize , rSize;
                    if(cur.left == null){
                        lHeight = 0;
                        lSize = 0;
                    } else {
                        lSize = cur.left.childCount + 1;
                        lHeight = cur.left.height;
                    }
                    if(cur.right == null) {
                        rHeight = 0;
                        rSize = 0;
                    } else {
                        rSize = cur.right.childCount + 1;
                        rHeight = cur.right.height;
                    }

                    // Updating height and number of children count for current node.
                    cur.height = 1 + Math.max(lHeight, rHeight);
                    cur.childCount = lSize + rSize;
                }
                // Move level up in the tree towards root.
                cur = cur.parent;
            }
        }

        // Returns balance factor between left and right side.
        protected int getBalance(){
            return ((left == null) ? 0 : left.height + 1) - ((right == null) ? 0 : right.height + 1);
        }

        // Performing left rotation.
        protected void rotateLeft(){
            //Replacing current as left or right child of current's parent of parent.
            Node temp = parent;
            Node superParent = temp.parent;
            // Identify if it is Left or Right arm of super parent node and update its child accordingly.
            if(superParent != null){
                if(parent.isLeft()){
                    superParent.left = this;
                } else {
                    superParent.right = this;
                }
            }

            //Updating Parent of current node as Super Parent.
            this.parent = superParent;

            //Now linking parent of current as left child of current and linking current's left as right child of current's left child.
            Node T2 = left;
            // Replacing current node as parent of current's parent instead of super parent.
            temp.parent = this;
            // Setting left child of current as parent.
            left = temp;
            if(T2 != null){
                //If left child of current is not null linking it to T2.
                T2.parent = temp;
            }
            temp.right = T2;

            // Since tree structure is manipulated updating count of children and height.
            temp.resetValues();
        }

        //Performing Right rotation.
        protected void rotateRight(){
            //Replacing current as left or right child of current's parent of parent.
            Node temp = parent;
            Node superParent = temp.parent;
            if(superParent != null){
                if(parent.isLeft()){
                    superParent.left = this;
                } else {
                    superParent.right = this;
                }
            }
            //Updating Parent of current node as Super Parent.
            this.parent = superParent;

            //Now linking parent of current as right child of current and linking current's right as left child of current's parent.
            Node T3 = right;
            // Replacing current node as parent of current's parent instead of super parent.
            temp.parent = this;
            // Setting right child of current as parent.
            right = temp;
            if(T3 != null){
                //If right child of current is not null linking it to T3.
                T3.parent = temp;
            }
            temp.left =T3;

            // Since tree structure is manipulated updating count of children and height.
            temp.resetValues();
        }

        protected boolean isLeft(){

            return parent != null && parent.left == this;
        }

        protected boolean isRight(){

            return parent != null && parent.right == this;
        }

        // Removes node if it is at leaf position.
        protected void del_Leaf(){
            if(isLeft()){
                parent.setLeft(null);
            } else {
                parent.setRight(null);
            }
        }

        // To identify the next element to be connected after removal of current node.
        protected Node follower(){
            Node follower = null;
            if(right != null){
                follower = right.leastValNode();
            } else if(parent != null){
                Node current = this;
                while(current != null && current.isRight()){
                    current = current.parent;
                }
                follower = current.parent;
            }
            return follower;
        }

        // Used in follower to identify the least value Node in the Right sub tree, which is always left most leaf node.
        protected Node leastValNode(){
            Node current = this;
            while(current != null){
                if(current.left == null){
                    break;
                } else {
                    current = current.left;
                }
            }
            return current;
        }

        // Replace child of removal node to location of removal node and update child and height counts.
        protected void updateParent(){
            Node temp = parent;
            Node superParent = temp.parent;

            // Check if super Parent of current exists.
            // If yes.
            if(superParent != null){
                // Current node is left of its parent.
                if(isLeft()){
                    // If current's parent node is also left.
                    if(parent.isLeft()){
                        superParent.left = this;
                    } else {
                        superParent.right = this;
                    }
                } else {
                    // Current node is right of its parent.
                    // If current's parent node is left.
                    if(parent.isLeft()){
                        superParent.left = this;
                    } else {
                        superParent.right = this;
                    }
                }
                // Updating parent of current as superParent.
                parent = superParent;
            } else { //If super parent does not exist make current node root.
                parent = null;
                root = this;
            }

            // Updating child and height count as one node is now removed.
            resetValues();
            // Also , ensuring tree is balanced.
            reBalanceTree(this);
        }
    }
}

