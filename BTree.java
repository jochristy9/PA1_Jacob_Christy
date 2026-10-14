
/**
 * The B-Tree class
 * @author Jacob Christy
 */
public class BTree<E extends Comparable<E>> {

    // order of the tree
    private int m;

    // root of the B-Tree
    private BTreeNode<E> root;

    /**
     * Constructor
     * @param t minimum degree
     */
    public BTree(int m) {
        this.m = m;
        root = null;
    }

    /**
     * Performs an inorder traversal
     * @return string containing all keys
     */
    public String inorder() {
        StringBuilder sb = new StringBuilder();
        inorderHelper(root, sb);
        return sb.toString();
    }

    private void inorderHelper(BTreeNode<E> node, StringBuilder sb) {
        if (node == null) {
            return;
        }

        for (int i = 0; i < node.numKeys; i++) {
            if (!node.leaf) {
                inorderHelper(node.children[i], sb);
            }

            sb.append(node.keys[i]).append(" ");
        }

        if (!node.leaf) {
            inorderHelper(node.children[node.numKeys], sb);
        }
    }

    /**
     * Search operation
     * @param x data to search
     * @return true if found, false otherwise
     */
    public boolean contains(E x) {
        return search(root, x);
    }

    private boolean search(BTreeNode<E> node, E x) {
        if (node == null) {
            return false;
        }

        int i = 0;

        while (i < node.numKeys &&
               x.compareTo(node.keys[i]) > 0) {
            i++;
        }

        if (i < node.numKeys &&
            x.compareTo(node.keys[i]) == 0) {
            return true;
        }

        if (node.leaf) {
            return false;
        }

        return search(node.children[i], x);
    }

    /**
     * Insert operation
     * @param x data to insert
     */
    public void insert(E x) {
        // Do not insert duplicate keys
        if (contains(x)) {
            return;
        }

        if (root == null) {
            root = new BTreeNode<>(m, true);
            root.keys[0] = x;
            root.numKeys = 1;
            return;
        }

        // Split a full root
        if (root.numKeys == m - 1) {
            BTreeNode<E> newRoot = new BTreeNode<>(m, false);
            newRoot.children[0] = root;
            root = newRoot;
            splitChild(root, 0);
        }

        insertNonFull(root, x);
    }

    private void insertNonFull(BTreeNode<E> node, E x) {
        int i = node.numKeys - 1;

        if (node.leaf) {
            while (i >= 0 && x.compareTo(node.keys[i]) < 0) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }

            node.keys[i + 1] = x;
            node.numKeys++;
        } else {
            while (i >= 0 && x.compareTo(node.keys[i]) < 0) {
                i--;
            }

            i++;

            if (node.children[i].numKeys == m - 1) {
                splitChild(node, i);

                if (x.compareTo(node.keys[i]) > 0) {
                    i++;
                }
            }

            insertNonFull(node.children[i], x);
        }
    }

    private void splitChild(BTreeNode<E> parent, int index) {
        BTreeNode<E> child = parent.children[index];

        // Promote the middle key
        int middle = (child.numKeys - 1) / 2;

        BTreeNode<E> right = new BTreeNode<>(m, child.leaf);

        right.numKeys = child.numKeys - middle - 1;

        // Copy keys into the new right node
        for (int j = 0; j < right.numKeys; j++) {
            right.keys[j] = child.keys[middle + 1 + j];
        }

        // Copy children if the node is not a leaf
        if (!child.leaf) {
            for (int j = 0; j <= right.numKeys; j++) {
                right.children[j] = child.children[middle + 1 + j];
            }
        }

        E middleKey = child.keys[middle];

        // Reduce the size of the original child
        child.numKeys = middle;

        // Shift parent children to make room
        for (int j = parent.numKeys; j >= index + 1; j--) {
            parent.children[j + 1] = parent.children[j];
        }

        parent.children[index + 1] = right;

        // Shift parent keys to make room
        for (int j = parent.numKeys - 1; j >= index; j--) {
            parent.keys[j + 1] = parent.keys[j];
        }

        parent.keys[index] = middleKey;
        parent.numKeys++;
    }
}