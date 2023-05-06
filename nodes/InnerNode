package nodes;

import index.Index;
import key.Key;

import java.util.LinkedList;
import java.util.List;

public class InnerNode<T> extends Node<T> {
    public InnerNode() {
        super();
        int m = Index.getM();
        minKeys = (int) (Math.ceil(m / 2.0) - 1);
        maxKeys = m - 1;
    }

    public boolean hasEnoughKeysLeft() {
        return (numOfKeys-1) >= minKeys;
    }

    @Override
    public String toString() {
        long printValueFormat = Index.getPrintValueFormat();
        StringBuilder sb = new StringBuilder();

        sb.append("|");

        if(this.getKeys() == null)
            return "| |";

        for(Key<T> key: this.getKeys()) {
            sb.append("|");
            sb.append(String.format("%2d", key.getValue() % printValueFormat));
        }

        sb.append("|");
        if(!this.full()) {
            sb.append(String.format("%" + (maxKeys-numOfKeys)*2 + "s", " "));
        }
        sb.append("|");

        return sb.toString();
    }

    public List<Key<T>> getFirstKeys(List<Key<T>> currentKeys) {
        Key<T> newDelimiterKey = getDelimiterKey(currentKeys);
        int newDelimiterKeyIndex = currentKeys.indexOf(newDelimiterKey);

        return new LinkedList<>(currentKeys.subList(0, newDelimiterKeyIndex));
    }

    public Key<T> getDelimiterKey(List<Key<T>> currentKeys) {
        return currentKeys.get((int) Math.ceil(Index.getM() / 2.0) - 1);
    }

    public List<Key<T>> getRest(List<Key<T>> currentKeys) {
        Key<T> newDelimiterKey = getDelimiterKey(currentKeys);
        int newDelimiterKeyIndex = currentKeys.indexOf(newDelimiterKey);

        return new LinkedList<>(currentKeys.subList(newDelimiterKeyIndex + 1, currentKeys.size()));
    }

    public void rightBrotherLoan(long value) {
        Node<T> rightBrother = this.getRightBrother(value);
        Key<T> loanKey = rightBrother.min();
        rightBrother.remove(loanKey.getValue());

        this.insertInOrder(loanKey);

        Node<T> tmpNode = this.rightPointer;
        this.rightPointer = loanKey.getLeft();
        loanKey.setLeft(tmpNode);

        this.rightPointer.parent = this;

        Key<T> delimiterKey = parent.findLeftDelimiterKey(value);
        long tmpValue = delimiterKey.getValue();
        delimiterKey.setValue(loanKey.getValue());
        loanKey.setValue(tmpValue);
    }

    public void leftBrotherLoan(long value) {
        Node<T> leftBrother = this.getLeftBrother(value);
        Key<T> loanKey = leftBrother.max();
        leftBrother.remove(loanKey.getValue());

        Node<T> tmpNode = leftBrother.rightPointer;
        leftBrother.rightPointer = loanKey.getLeft();
        loanKey.setLeft(tmpNode);

        loanKey.getLeft().parent = this;

        Key<T> delimiterKey = this.findRightDelimiterKey(value);
        long tmpValue = delimiterKey.getValue();
        delimiterKey.setValue(loanKey.getValue());
        loanKey.setValue(tmpValue);

        this.insertInOrder(loanKey);
    }

    public void rightBrotherCompression(long value) {
        Node<T> rightBrother = this.getRightBrother(value);

        Key<T> delimiterKey = parent.findLeftDelimiterKey(value);
        Key<T> delimiterKeyNext = parent.getRightKey(parent.getKeys(), delimiterKey);

        List<Key<T>> currentKeys = new LinkedList<>(this.getKeys());
        currentKeys.add(delimiterKey);
        currentKeys.addAll(rightBrother.getKeys());
        this.setKeys(currentKeys);

        for(Key<T> key: rightBrother.getKeys()) {
            key.getLeft().parent = this;
        }
        rightBrother.rightPointer.parent = this;

        delimiterKey.setLeft(this.rightPointer);
        this.rightPointer = rightBrother.rightPointer;

        if(delimiterKeyNext == null) {
            parent.rightPointer = this;
        }

        else {
            delimiterKeyNext.setLeft(this);
        }

        parent.remove(delimiterKey.getValue());
    }

    public void leftBrotherCompression(long value) {
        Node<T> leftBrother = this.getLeftBrother(value);

        Key<T> delimiterKey = this.findRightDelimiterKey(value);
        Key<T> delimiterKeyNext = parent.getRightKey(parent.getKeys(), delimiterKey);

        List<Key<T>> currentKeys = new LinkedList<>(leftBrother.getKeys());
        currentKeys.add(delimiterKey);
        currentKeys.addAll(this.getKeys());
        leftBrother.setKeys(currentKeys);

        for(Key<T> key: this.getKeys()) {
            key.getLeft().parent = (InnerNode<T>) leftBrother;
        }
        this.rightPointer.parent = (InnerNode<T>) leftBrother;

        delimiterKey.setLeft(leftBrother.rightPointer);
        leftBrother.rightPointer = this.rightPointer;

        if(delimiterKeyNext == null) {
            leftBrother.parent.rightPointer = leftBrother;
        }

        else {
            delimiterKeyNext.setLeft(leftBrother);
        }

        leftBrother.parent.remove(delimiterKey.getValue());
    }
}
