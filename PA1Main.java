/**
 * The main program for creating and testing B-Trees
 * @author Srini Badri
 */
public class PA1Main {

    /**
     * The entry point
     * @param args command line arguments
     */
    public static void main(String[] args) {

        // create an empty B-Tree
        BTree<Integer> tree = new BTree<>(5);

        // insert sample data
        for (int i = 1; i <= 10; i++) {

            int x = i * 10;
            tree.insert(x);

            x = 200 - i * 10;
            tree.insert(x);
        }

        System.out.println("In-order Traversal");

        String s = tree.inorder();
        System.out.println(s);

        java.util.Scanner scan =
                new java.util.Scanner(System.in);

        while (true) {

            System.out.print(
                "Enter an integer value to search for: ");

            int y = scan.nextInt();

            boolean found = tree.contains(y);

            if (found)
                System.out.println(
                        "Data " + y +
                        " is present in the tree");
            else
                System.out.println(
                        "Data " + y +
                        " is NOT present in the tree");
        }
    }
}
