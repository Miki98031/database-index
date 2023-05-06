package index;

import key.Key;
import nodes.InnerNode;
import nodes.Leaf;
import nodes.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Index<T> {
    private Node<T> root;
    private List<Leaf<T>> leavesList = new LinkedList<>();
    private final List<Key<T>> tableKeys;
    private static int m;
    private static long printValueFormatDigitNumber;

    public Index(int nodeSize, List<Key<T>> tableKeys) {
        this.tableKeys = tableKeys;
        m = nodeSize;
        setPrintParameters();
    }

    private void setPrintParameters() {
        printValueFormatDigitNumber = String.valueOf(tableKeys.size()).length() + 1;
    }

    public static int getM() {
        return m;
    }
    public static long getPrintValueFormat() {
        return (long) Math.pow(10, printValueFormatDigitNumber);
    }
    public void changeRootConfiguration() {
        root.setMinKeys(1);
        root.setMaxKeys(m-1);
        root.setParent(null);
    }

    public void insertTableValues() {
        for(Key<T> key: tableKeys) {
            insertInLeaf(key);
        }
    }

    public List<Leaf<T>> getLeavesList() {
        return leavesList;
    }

    public Leaf<T> searchLeaf(long value) {
        if(root == null) {
            return null;
        }

        Node<T> currentNode = root;
        while(currentNode.getClass() != Leaf.class) {
            boolean isRightSon = true;

            for(Key<T> currentKey: currentNode.getKeys()) {
                if(value <= currentKey.getValue()) {
                    currentNode = currentKey.getLeft();
                    isRightSon = false;
                    break;
                }
            }

            if(isRightSon) {
                currentNode = currentNode.getRightPointer();
            }
        }

        return (Leaf<T>) currentNode;
    }
    public void insertInLeaf(Key<T> key) {
        Leaf<T> insertDestinationLeaf = searchLeaf(key.getValue());


        if(insertDestinationLeaf == null) {
            firstLeafInsert(key);
        }

        else if(insertDestinationLeaf.alreadyExists(key.getValue())) {
            System.out.println("Key already exists");
            return;
        }

        else if (!insertDestinationLeaf.full()) {
            normalLeafInsert(key, insertDestinationLeaf);
        }

        else {
            Key<T> newDelimiterKey = leafInsertWithOverload(key, insertDestinationLeaf);
            insertInInnerNode(newDelimiterKey);
        }
    }

    private void insertInInnerNode(Key<T> newDelimiterKey) {
        boolean treeSatisfied = false;

        while(true) {
            InnerNode<T> problemNode = newDelimiterKey.getLeft().getParent();

            if(problemNode == null) {
                rootInsert(newDelimiterKey);
                treeSatisfied = true;
            }

            else if(!problemNode.full()) {
                normalInnerNodeInsert(newDelimiterKey, problemNode);
                treeSatisfied = true;
            }

            else {
                newDelimiterKey = InnerNodeInsertWithOverload(newDelimiterKey, problemNode);
            }

            if(treeSatisfied) {
                break;
            }
        }
    }

    private void firstLeafInsert(Key<T> key) {
        Leaf<T> insertDestinationLeaf = new Leaf<>();
        insertDestinationLeaf.insertInOrder(key);
        root = insertDestinationLeaf;
        changeRootConfiguration();
        leavesList.add(insertDestinationLeaf);
    }

    private void normalLeafInsert(Key<T> key, Leaf<T> insertDestinationLeaf) {
        insertDestinationLeaf.insertInOrder(key);
    }

    public Key<T> leafInsertWithOverload(Key<T> key, Leaf<T> insertDestinationLeaf) {
        List<Key<T>> currentKeys = new LinkedList<>(insertDestinationLeaf.getKeys());
        currentKeys.add(key);
        currentKeys.sort(Key.comparator);

        insertDestinationLeaf.setKeys(insertDestinationLeaf.getFirstKeys(currentKeys));

        Leaf<T> newLeaf = new Leaf<>();
        newLeaf.setKeys(insertDestinationLeaf.getRest(currentKeys));
        int insertPosition = leavesList.indexOf(insertDestinationLeaf) + 1;
        leavesList.add(insertPosition, newLeaf);

        Key<T> delimiterKey = insertDestinationLeaf.getDelimiterKey(currentKeys);
        Key<T> newDelimiterKey = new Key<>(delimiterKey.getValue(), null);
        newDelimiterKey.setLeft(insertDestinationLeaf);
        newDelimiterKey.setRight(newLeaf);

        return newDelimiterKey;
    }
    private void rootInsert(Key<T> newDelimiterKey) {
        InnerNode<T> problemNode = new InnerNode<>();

        //Change previous root(if root-leaf) to leaf
        if(root.getClass() == Leaf.class) {
            newDelimiterKey.getLeft().setMinKeys(m/2);
        }

        problemNode.insertInOrder(newDelimiterKey);
        problemNode.setRightPointer(newDelimiterKey.getRight());
        problemNode.setCurrentLevel(newDelimiterKey.getLeft().getCurrentLevel() + 1);
        newDelimiterKey.getLeft().setParent(problemNode);
        problemNode.getRightPointer().setParent(problemNode);

        root = problemNode;
        changeRootConfiguration();
    }

    private void normalInnerNodeInsert(Key<T> newDelimiterKey, InnerNode<T> problemNode) {
        problemNode.insertInOrder(newDelimiterKey);
        List<Key<T>> currentKeys = problemNode.getKeys();

        Key<T> rightKey = problemNode.getRightKey(currentKeys, newDelimiterKey);
        if(rightKey == null) {
            problemNode.setRightPointer(newDelimiterKey.getRight());
        }
        else {
            rightKey.setLeft(newDelimiterKey.getRight());
        }

        newDelimiterKey.getLeft().setParent(problemNode);
        newDelimiterKey.getRight().setParent(problemNode);
    }

    private Key<T> InnerNodeInsertWithOverload(Key<T> newDelimiterKey, InnerNode<T> problemNode) {
        List<Key<T>> currentKeys = new LinkedList<>(problemNode.getKeys());
        currentKeys.add(newDelimiterKey);
        currentKeys.sort(Key.comparator);

        Key<T> rightKey = problemNode.getRightKey(currentKeys, newDelimiterKey);
        if(rightKey == null) {
            problemNode.setRightPointer(newDelimiterKey.getRight());
        }
        else {
            rightKey.setLeft(newDelimiterKey.getRight());
        }

        problemNode.setKeys(problemNode.getFirstKeys(currentKeys));

        InnerNode<T> newNode = new InnerNode<>();
        newNode.setKeys(problemNode.getRest(currentKeys));
        newNode.changeConfiguration();
        newNode.setCurrentLevel(problemNode.getCurrentLevel());

        newDelimiterKey = problemNode.getDelimiterKey(currentKeys);

        newNode.setRightPointer(problemNode.getRightPointer());
        problemNode.setRightPointer(newDelimiterKey.getLeft());

        newDelimiterKey.setLeft(problemNode);
        newDelimiterKey.setRight(newNode);

        for(Key<T> key: problemNode.getKeys()) {
            key.getLeft().setParent(problemNode);
        }
        problemNode.getRightPointer().setParent(problemNode);

        for(Key<T> key: newNode.getKeys()) {
            key.getLeft().setParent(newNode);
        }
        newNode.getRightPointer().setParent(newNode);

        problemNode.setMinKeys((int) (Math.ceil(m / 2.0) - 1));

        return newDelimiterKey;
    }
    public void print() {
        if(root == null) {
            System.out.println("Empty index.");
            return;
        }

        Queue<Node<T>> queue = new LinkedBlockingQueue<>();
        queue.offer(root);

        int currentLevel = root.getCurrentLevel();
        long leftBlankSpace = 64;
        long betweenBlankSpace = 64;

        System.out.printf("%" + leftBlankSpace + "s", " ");
        while(!queue.isEmpty()) {
            Node<T> currentNode = queue.remove();

            if(currentNode.getCurrentLevel() < currentLevel) {
                System.out.println("\n");
                currentLevel--;

                leftBlankSpace /= 2;
                betweenBlankSpace /= 2;

                System.out.printf("%" + leftBlankSpace + "s", " ");
            }

            System.out.print(currentNode);
            System.out.printf("%" + betweenBlankSpace + "s", " ");

            if(currentNode.getClass() != Leaf.class) {
                for (Key<T> key : currentNode.getKeys()) {
                    if (key.getLeft() != null) {
                        queue.offer(key.getLeft());
                    }
                }

                if (currentNode.getRightPointer() != null) {
                    queue.offer(currentNode.getRightPointer());
                }
            }
        }

        int indentation = 5;
        for(int i = 0 ; i < indentation ; i++) {
            System.out.println();
        }
    }

    public void deleteKey(long value) {
        Leaf<T> deleteDestinationLeaf = searchLeaf(value);

        if(deleteDestinationLeaf == null) {
            return;
        }

        if(deleteDestinationLeaf.doesntExist(value)) {
            System.out.println("Key doesn't exist.");
            return;
        }

        if(deleteDestinationLeaf == root) {
            if(deleteDestinationLeaf.getNumOfKeys() == 1) {
                deleteDestinationLeaf.remove(value);
                root = null;
                return;
            }
        }

        int keyPosition = deleteDestinationLeaf.position(value);

        if(deleteDestinationLeaf.hasEnoughKeysLeft()) {

            if(keyPosition != deleteDestinationLeaf.lastElementPosition()) {
                deleteDestinationLeaf.remove(value);
            }

            else {
                deleteDestinationLeaf.remove(value);
                changeDelimiterKey(deleteDestinationLeaf, deleteDestinationLeaf.max());
            }
        }

        else {
            int lastElementPosition = deleteDestinationLeaf.lastElementPosition();
            Key<T> maxToPropagate = null;
            boolean specificCase = false;

            deleteDestinationLeaf.remove(value);

            Node<T> rightBrother = deleteDestinationLeaf.getRightBrother(value);
            Node<T> leftBrother = deleteDestinationLeaf.getLeftBrother(value);

            boolean rightBrotherExists = rightBrother != null;
            boolean leftBrotherExists = leftBrother != null;

            if(rightBrotherExists && rightBrother.hasEnoughKeysLeft()) {
                deleteDestinationLeaf.rightBrotherLoan(value);
            }

            else if(leftBrotherExists && leftBrother.hasEnoughKeysLeft()) {
                deleteDestinationLeaf.leftBrotherLoan(value);
            }

            else {
                if(rightBrotherExists) {
                    deleteDestinationLeaf.rightBrotherCompression(value, leavesList);
                }

                else {
                    if(deleteDestinationLeaf.getKeys().size() == 0) {
                        specificCase = true;
                        maxToPropagate = leftBrother.max();
                    }
                    deleteDestinationLeaf.leftBrotherCompression(value, leavesList);
                }

                if(specificCase) {
                    this.propagateDeletion(leftBrother.getParent(), value);
                }

                else {
                    this.propagateDeletion(deleteDestinationLeaf.getParent(), value);
                }
            }

            if(keyPosition == lastElementPosition) {
                if(specificCase) {
                    changeDelimiterKey(leftBrother, maxToPropagate);
                }

                else {
                    changeDelimiterKey(deleteDestinationLeaf, deleteDestinationLeaf.max());
                }
            }
        }
    }

    private void propagateDeletion(InnerNode<T> problemNode, long value) {
        while(true) {
            if(problemNode.getKeys().size() >= problemNode.getMinKeys()) {
                break;
            }

            if(problemNode.getParent() == null) {
                root = problemNode.getRightPointer();
                changeRootConfiguration();
                break;
            }

            Node<T> rightBrother = problemNode.getRightBrother(value);
            Node<T> leftBrother = problemNode.getLeftBrother(value);

            boolean rightBrotherExists = rightBrother != null;
            boolean leftBrotherExists = leftBrother != null;

            if(rightBrotherExists && rightBrother.hasEnoughKeysLeft()) {
                problemNode.rightBrotherLoan(value);
            }

            else if(leftBrotherExists && leftBrother.hasEnoughKeysLeft()) {
                problemNode.leftBrotherLoan(value);
            }

            else {
                if(rightBrotherExists) {
                    problemNode.rightBrotherCompression(value);
                }

                else {
                    problemNode.leftBrotherCompression(value);
                }

                problemNode = problemNode.getParent();
            }

        }
    }

    private void changeDelimiterKey(Node<T> problemNode, Key<T> newKey) {
        while(true) {
            Node<T> parent = problemNode.getParent();

            if(parent == null) {
                return;
            }

            Key<T> leftDelimiterKey = parent.findLeftDelimiterKey(newKey.getValue());

            if(leftDelimiterKey != null) {
                leftDelimiterKey.setValue(newKey.getValue());
                break;
            }

            else {
                problemNode = parent;
            }
        }
    }
}
