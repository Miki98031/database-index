package nodes;

import key.Key;

import java.util.LinkedList;
import java.util.List;

public abstract class Node<T> {
    protected Node<T> rightPointer;
    protected List<Key<T>> keys = new LinkedList<>();
    protected int minKeys;
    protected int maxKeys;
    protected InnerNode<T> parent;
    protected int numOfKeys;
    protected int currentLevel = 0;

    public Node() {}

    public List<Key<T>> getKeys() {
        return keys;
    }
    public InnerNode<T> getParent() {
        return parent;
    }
    public Node<T> getRightPointer() {
        return rightPointer;
    }

    public int getMinKeys() {
        return minKeys;
    }

    public int getNumOfKeys() {
        return numOfKeys;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setKeys(List<Key<T>> keys) {
        this.keys = keys;
        this.changeConfiguration();
    }
    public void setParent(InnerNode<T> parent) {
        this.parent = parent;
    }
    public void setRightPointer(Node<T> rightPointer) {
        this.rightPointer = rightPointer;
    }

    public void setMinKeys(int minKeys) {
        this.minKeys = minKeys;
    }

    public void setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void changeConfiguration() {
        numOfKeys = keys.size();
    }

    public void insertInOrder(Key<T> key) {
        LinkedList<Key<T>> tmp = new LinkedList<>(keys);
        tmp.add(key);
        tmp.sort(Key.comparator);
        this.keys = tmp;
        this.numOfKeys++;
    }

    public void remove(long value) {
        this.keys.remove(this.position(value));
        this.numOfKeys--;
    }

    public boolean full() {
        return numOfKeys == maxKeys;
    }

    public Key<T> getRightKey(List<Key<T>> currentKeys, Key<T> newDelimiterKey) {
        int newDelimiterKeyPosition = currentKeys.indexOf(newDelimiterKey);

        if(newDelimiterKeyPosition+1 >= currentKeys.size()) {
            return null;
        }

        else {
            return currentKeys.get(newDelimiterKeyPosition + 1);
        }
    }

    public Key<T> findLeftDelimiterKey(long newValue) {
        for(Key<T> key: this.getKeys()) {
            if(newValue <= key.getValue()) {
                return key;
            }
        }

        return null;
    }

    public Key<T> findRightDelimiterKey(long value) {
        boolean leftBrother = false;
        Key<T> tmp = null;
        Key<T> last = null;
        int i = 0;

        if(this.parent == null) {
            return null;
        }

        for(Key<T> key: this.parent.getKeys()) {
            if(value <= key.getValue()) {
                leftBrother = true;
            }

            if(i == this.parent.getKeys().size()-1) {
                last = key;
            }

            if(leftBrother) {
                return tmp;
            }

            else {
                tmp = key;
                i++;
            }
        }

        return last;
    }

    public abstract boolean hasEnoughKeysLeft();

    public Key<T> max() {
        int maxPosition = this.keys.size()-1;
        return this.keys.get(maxPosition);
    }

    public Key<T> min() {
        int minPosition = 0;
        return this.keys.get(minPosition);
    }

    public int position(long value) {
        int pos = 0;

        for(Key<T> key: this.getKeys()) {
            if(key.getValue() == value) {
                return pos;
            }

            else {
                pos++;
            }
        }

        return -1;
    }

    public Node<T> getRightBrother(long value) {
        boolean rightBrother = false;
        for(Key<T> key: this.parent.getKeys()) {
            if(rightBrother) {
                return key.getLeft();
            }

            if(value <= key.getValue()) {
                rightBrother = true;
            }
        }

        if(rightBrother) {
            return this.parent.rightPointer;
        }

        else {
            return null;
        }
    }

    public Node<T> getLeftBrother(long value) {
        boolean leftBrother = false;
        Key<T> tmp = null;
        Key<T> last = null;
        int i = 0;

        for(Key<T> key: this.parent.getKeys()) {
            if(value <= key.getValue()) {
                if(i != 0) {
                    leftBrother = true;
                }
                else {
                    return null;
                }
            }

            if(leftBrother) {
                return tmp.getLeft();
            }

            if(i == this.parent.getKeys().size()-1) {
                last = key;
                break;
            }

            else {
                tmp = key;
                i++;
            }
        }

        return last.getLeft();
    }
}
