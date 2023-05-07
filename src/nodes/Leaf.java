package nodes;

import index.Index;
import key.Key;

import java.util.LinkedList;
import java.util.List;

public class Leaf<T> extends Node<T> {
    public Leaf() {
        super();
        int m = Index.getM();
        minKeys = m/2;
        maxKeys = m-1;
    }

    public boolean hasEnoughKeysLeft() {
        return (numOfKeys-1) >= minKeys;
    }

    @Override
    public String toString() {
        long printValueFormat = Index.getPrintValueFormat();

        StringBuilder sb = new StringBuilder();

        sb.append("|");

        for(Key<T> key: this.getKeys()) {
            sb.append("=");
            sb.append(String.format("%2d", key.getValue() % printValueFormat));
        }

        if(!this.full()) {
            sb.append("=");
            sb.append(String.format("%" + (maxKeys-numOfKeys)*2 + "s", " "));
        }
        sb.append("|*|");

        return sb.toString();
    }

    public List<Key<T>> getFirstKeys(List<Key<T>> currentKeys) {
        Key<T> newDelimiterKey = getDelimiterKey(currentKeys);
        int newDelimiterKeyIndex = currentKeys.indexOf(newDelimiterKey);

        return new LinkedList<>(currentKeys.subList(0, newDelimiterKeyIndex + 1));
    }

    public Key<T> getDelimiterKey(List<Key<T>> currentKeys) {
        return currentKeys.get((int) Math.ceil(Index.getM() / 2.0) - 1);
    }

    public List<Key<T>> getRest(List<Key<T>> currentKeys) {
        Key<T> newDelimiterKey = getDelimiterKey(currentKeys);
        int newDelimiterKeyIndex = currentKeys.indexOf(newDelimiterKey);

        return new LinkedList<>(currentKeys.subList(newDelimiterKeyIndex + 1, currentKeys.size()));
    }

    public int lastElementPosition() {
        return this.getKeys().size()-1;
    }
    public void rightBrotherLoan(long deletedValue) {
        Node<T> rightBrother = this.getRightBrother(deletedValue);
        Key<T> loanKey = rightBrother.min();
        rightBrother.remove(loanKey.getValue());

        Key<T> delimiterKey = parent.findLeftDelimiterKey(deletedValue);
        delimiterKey.setValue(loanKey.getValue());

        this.insertInOrder(loanKey);
    }

    public void leftBrotherLoan(long deletedValue) {
        Node<T> leftBrother = this.getLeftBrother(deletedValue);
        Key<T> loanKey = leftBrother.max();
        leftBrother.remove(loanKey.getValue());

        Key<T> leftBrotherNewMax = leftBrother.max();
        Key<T> delimiterKey = parent.findLeftDelimiterKey(loanKey.getValue());
        delimiterKey.setValue(leftBrotherNewMax.getValue());

        this.insertInOrder(loanKey);
    }

    public void rightBrotherCompression(long deletedValue, List<Leaf<T>> leavesList) {
        Node<T> rightBrother = this.getRightBrother(deletedValue);

        Key<T> delimiterKey = parent.findLeftDelimiterKey(deletedValue);
        Key<T> delimiterKeyNext = parent.getRightKey(parent.getKeys(), delimiterKey);

        List<Key<T>> currentKeys = new LinkedList<>();
        currentKeys.addAll(this.getKeys());
        currentKeys.addAll(rightBrother.getKeys());
        this.setKeys(currentKeys);

        if(delimiterKeyNext == null) {
            parent.rightPointer = delimiterKey.getLeft();
        }

        else {
            delimiterKeyNext.setLeft(delimiterKey.getLeft());
        }

        parent.remove(delimiterKey.getValue());
        leavesList.remove((Leaf<T>) rightBrother);
    }

    public void leftBrotherCompression(long deletedValue, List<Leaf<T>> leavesList) {
        Node<T> leftBrother = this.getLeftBrother(deletedValue);

        Key<T> delimiterKey = this.findRightDelimiterKey(deletedValue);
        Key<T> delimiterKeyNext = parent.getRightKey(parent.getKeys(), delimiterKey);

        List<Key<T>> currentKeys = new LinkedList<>();
        currentKeys.addAll(leftBrother.getKeys());
        currentKeys.addAll(this.getKeys());
        leftBrother.setKeys(currentKeys);

        if(delimiterKeyNext == null) {
            leftBrother.parent.rightPointer = delimiterKey.getLeft();
        }

        else {
            delimiterKeyNext.setLeft(delimiterKey.getLeft());
            delimiterKeyNext.setValue(delimiterKeyNext.getLeft().max().getValue());
        }

        leftBrother.parent.remove(delimiterKey.getValue());
        leavesList.remove(this);
    }

    public boolean alreadyExists(long value) {
        for(Key<T> key: this.getKeys()) {
            if(key.getValue() == value) {
                return true;
            }
        }

        return false;
    }

    public boolean doesntExist(long value) {
        for(Key<T> key: this.getKeys()) {
            if(key.getValue() == value) {
                return false;
            }
        }

        return true;
    }
}
