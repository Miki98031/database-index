package key;

import nodes.Node;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

public class Key<T> {
    private long value;
    private Node<T> left;
    private Node<T> right;
    private T content;

    public static <T> T createContents(Class<T> clazz, String line) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Constructor<?>[] constructors = clazz.getConstructors();
        Constructor<?> constructor = null;
        for(Constructor<?> con: constructors) {
            Class<?>[] parameterTypes = con.getParameterTypes();
            if(parameterTypes.length == 1 && parameterTypes[0] == String.class) {
                constructor = con;
                break;
            }
        }
        assert constructor != null;
        return (T) constructor.newInstance(line);
    }
    public Key(long value, T content) {
        this.value = value;
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public Node<T> getLeft() {
        return left;
    }

    public Node<T> getRight() {
        return right;
    }

    public long getValue() {
        return value;
    }
    public static final Comparator<Key> comparator = (k1, k2) -> {
        double valuesDifference = k1.value - k2.value;
        return valuesDifference > 0 ? 1 : (valuesDifference == 0 ? 0 : -1);
    };

    public void setLeft(Node<T> problemNode) {
        left = problemNode;
    }

    public void setRight(Node<T> newNode) {
        right = newNode;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
