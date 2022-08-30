
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

/**
 * This class represents an n-dimensional array, stored in a 2D row-major memory layout.
 * More info:
 * https://eli.thegreenplace.net/2015/memory-layout-of-multi-dimensional-arrays/
 *
 * @author J.L.
 */
public class NdArray<E> extends AbstractCollection<E> {

    private final Object[] mArray;
    private final int[] mIndices;
    private int size;

    /**
     * Constructor for NdArray. Check for illegal dimensions and populate mIndices.
     * @param firstDimLen Length of first dimension
     * @param furtherDimLens Lengths of following dimension
     */
    NdArray(Integer firstDimLen, Integer... furtherDimLens) {

        if (firstDimLen < 0) {
            throw new NegativeArraySizeException("Illegal dimension size "+firstDimLen+".");
        }
        mIndices = new int[furtherDimLens.length + 1];
        mIndices[0] = firstDimLen;
        size = firstDimLen;

        IntStream.range(0, furtherDimLens.length)
                .forEach(i -> {
                    if (furtherDimLens[i] < 0) {
                        throw new NegativeArraySizeException("Illegal dimension size "+furtherDimLens[i]+".");
                    }
                    mIndices[i+1] = furtherDimLens[i];
                    size *= furtherDimLens[i];
                });


        mArray = new Object[size];
    }

    @Override
    public Iterator<E> iterator() {
        return new mIterator();
    }

    private class mIterator implements Iterator<E> {
        private int pos = 0;

        @Override
        public boolean hasNext() {
            return pos < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if(pos >= size) {
                throw new NoSuchElementException("No more values in the array!");
            }
            return (E) mArray[pos++];
        }
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Check for illegal arguments. Return value from multi-dimensional array.
     * @param indices Reference to the indices given
     * @return The value at the given position
     * @throws IllegalArgumentException if not enough indices given
     * @throws IndexOutOfBoundsException if the length on any index is out of bounds
     */
    @SuppressWarnings("unchecked")
    public E get(int... indices) throws IllegalArgumentException, IndexOutOfBoundsException {
        checkIndices(indices);
        return (E) mArray[offset(indices)];
    }

    /**
     * Check for illegal arguments. Set value at position in the multi-dimensional array.
     * @param indices Reference to the indices given
     * @return The value at the given position
     * @throws IllegalArgumentException if not enough indices given
     * @throws IndexOutOfBoundsException if the length on any index is out of bounds
     */
    public void set(E e, int... indices) throws IllegalArgumentException, IndexOutOfBoundsException {
        checkIndices(indices);
        mArray[offset(indices)] = e;
    }

    
    private void checkIndices(int... indices) {
        if (indices.length != mIndices.length)
            throw new IllegalArgumentException(String.format("The array has %d dimensions but %d indices were given.",
                    mIndices.length, indices.length));

        IntStream.range(0, indices.length)
                .forEach(i -> {
                    if (0 > indices[i] || indices[i] >= mIndices[i])
                        throw new IndexOutOfBoundsException(String.format("Illegal index %d for dimension %d of length %d.",
                                indices[i], i+1, mIndices[i]));
                });
    }

    /**  Return offset of item in a row-major layout
     *   of a multidimensional array */
    private int offset(int... indices) {

        return IntStream.range(1, indices.length)
                .reduce(indices[0], (a, b) -> {
                    a *= mIndices[b];
                    a += indices[b];
                    return a;
                });
    }

    public int[] getDimensions() {
        return mIndices.clone();
    }

}
