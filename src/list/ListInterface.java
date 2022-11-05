package list;

public interface ListInterface<T> {
	public void add(T newEntry);
	public void add(int newPosition, T newEntry);
	public T remove(int givenPosition);
	public Object[] toArray();
	public boolean remove(T anEntry);
	public T replace(int givenPosition, T newEntry);
	public boolean isEmpty();
	public boolean contains(T anEntry);
	public void clear();
	public T getEntry(int givenPosition);
	public int getLength();
}
