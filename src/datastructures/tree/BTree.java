package datastructures.tree;

public class BTree<T extends Comparable<T>> {

    private BTreeNode<T> root;

    private final int degree;

    public BTree(int degree) {

        this.degree = degree;

        root = new BTreeNode<>(degree, true);

    }

    public BTreeNode<T> search(T key) {
        return search(root, key);
    }

    @SuppressWarnings("unchecked")
  private BTreeNode<T> search(BTreeNode<T> node, T key) {

      int i = 0;

      while (i < node.getNumberOfKeys()
              && key.compareTo((T) node.getKeys()[i]) > 0) {

          i++;

      }

      if (i < node.getNumberOfKeys()
              && key.compareTo((T) node.getKeys()[i]) == 0) {

          return node;

      }

      if (node.isLeaf()) {

          return null;

      }

      return search(node.getChildren()[i], key);

  }


  private void splitChild(
          BTreeNode<T> parent,
          int childIndex,
          BTreeNode<T> fullChild) {

      BTreeNode<T> newChild =
              new BTreeNode<>(degree, fullChild.isLeaf());

      newChild.setNumberOfKeys(degree - 1);

      for (int j = 0; j < degree - 1; j++) {

          newChild.getKeys()[j] =
                  fullChild.getKeys()[j + degree];

      }

      if (!fullChild.isLeaf()) {

        for (int j = 0; j < degree; j++) {

            newChild.getChildren()[j] =
                    fullChild.getChildren()[j + degree];

        }

    }
    fullChild.setNumberOfKeys(degree - 1);
    for (int j = parent.getNumberOfKeys();
                j >= childIndex + 1;
                j--) {

                parent.getChildren()[j + 1] =
                            parent.getChildren()[j];

                }

            parent.getChildren()[childIndex + 1] =
                    newChild;
            
            for (int j = parent.getNumberOfKeys() - 1;
            j >= childIndex;
            j--) {

            parent.getKeys()[j + 1] =
                    parent.getKeys()[j];

        }

        parent.getKeys()[childIndex] =
                fullChild.getKeys()[degree - 1];

        parent.setNumberOfKeys(
                parent.getNumberOfKeys() + 1
        ); 
    }

    @SuppressWarnings("unchecked")
    private void insertNonFull(BTreeNode<T> node, T key) {

        int i = node.getNumberOfKeys() - 1;
        if (node.isLeaf()) {

            while (i >= 0 &&
                    key.compareTo((T) node.getKeys()[i]) < 0) {

                node.getKeys()[i + 1] = node.getKeys()[i];

                i--;

            }

            node.getKeys()[i + 1] = key;

            node.setNumberOfKeys(
                    node.getNumberOfKeys() + 1
            );

        }

        else {

            while (i >= 0 &&
                    key.compareTo((T) node.getKeys()[i]) < 0) {

                i--;

            }

            i++;

            if (node.getChildren()[i].getNumberOfKeys()
                    == 2 * degree - 1) {

                splitChild(
                        node,
                        i,
                        node.getChildren()[i]
                );

                if (key.compareTo((T) node.getKeys()[i]) > 0) {

                    i++;

                }

            }

            insertNonFull(node.getChildren()[i], key);

        }
    }

    public void insert(T key) {

        if (root.getNumberOfKeys()
                == 2 * degree - 1) {

            BTreeNode<T> newRoot =
                    new BTreeNode<>(degree, false);

            newRoot.getChildren()[0] = root;

            splitChild(newRoot, 0, root);

            root = newRoot;

        }

        insertNonFull(root, key);

    }

    public void traverse() {
        traverse(root);
    }

    @SuppressWarnings("unchecked")
    private void traverse(BTreeNode<T> node) {

        if (node == null) {
            return;
        }

        int i;

        for (i = 0; i < node.getNumberOfKeys(); i++) {

            if (!node.isLeaf()) {
                traverse(node.getChildren()[i]);
            }

            System.out.print(node.getKeys()[i] + " ");

        }

        if (!node.isLeaf()) {
            traverse(node.getChildren()[i]);
        }

    }
}