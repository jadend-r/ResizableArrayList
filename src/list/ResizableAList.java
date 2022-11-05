package list;

import java.util.Arrays;

import list.ListInterface;
import utils.Node;

public class ResizableAList<T> implements ListInterface<T> {
	private Node<T[]> startNode;
	private int numOfEntries;
	private int capacity;
	private static final int DEFAULT_CAPACITY = 10;
	
	@SuppressWarnings("unchecked")
	public ResizableAList(int capacity) {
		this.capacity = capacity;
		startNode = new Node<>((T[]) new Object[capacity]);
	}
	
	public ResizableAList() {
		this(DEFAULT_CAPACITY);
	}
	
	private Node<T[]> getNodeForIndex(int index){
		if (index < 0 || index > numOfEntries)
			throw new IndexOutOfBoundsException();
		int totalSize = 0;
		for(Node<T[]> curr = startNode; curr != null; curr = curr.getNext()) {
			totalSize += curr.getData().length;
			if(index <= totalSize - 1)
				return curr;
		}
		return null;
	}
	
	private boolean isFull() {
		return numOfEntries % capacity == 0;
	}
	
	private Node<T[]> getLastNode(){
		Node<T[]> curr = null;
		Node<T[]> next = startNode;
		while(next != null) {
			curr = next;
			next = next.getNext();
		}
		return curr;
	}
	
	private void ensureCapacity() {
		if(isFull()) {
			@SuppressWarnings("unchecked")
			Node<T[]> extension = new Node<>((T[]) new Object[capacity]);
			Node<T[]> lastNode = getLastNode();
			lastNode.setNext(extension);
		}
	}
	
	private void makeRoom(Node<T[]> currNode, int position) {
		T[] nodeData = currNode.getData();
		T lastElem = nodeData[capacity - 1];
		position = position % capacity;
		for(int idx = capacity - 1; idx > position; idx--) {
			nodeData[idx] = nodeData[idx - 1];
		}
		if(currNode.getNext() != null) {
			makeRoom(currNode.getNext(), 0);
			currNode.getNext().getData()[0] = lastElem;
		}
	}

	@Override
	public void add(T newEntry) {
		add(numOfEntries, newEntry);
	}

	@Override
	public void add(int newPosition, T newEntry) {
		if(newPosition < 0 || newPosition > numOfEntries) 
			throw new IndexOutOfBoundsException();
		Node<T[]> nodeForIdx = getNodeForIndex(newPosition);
		makeRoom(nodeForIdx, newPosition);
		nodeForIdx.getData()[newPosition % capacity] = newEntry;
		numOfEntries++;
		ensureCapacity();
	}
	
	private void fillGap(Node<T[]> currNode, int position) {
		T[] nodeData = currNode.getData();
		position = position % capacity;
		for(int idx = position; idx < capacity - 1; idx++) {
			nodeData[idx] = nodeData[idx + 1];
		}
		if(currNode.getNext() != null) {
			T firstElem = currNode.getNext().getData()[0];
			fillGap(currNode.getNext(), 0);
			nodeData[capacity - 1] = firstElem;
		} else {
			numOfEntries--;
		}
	}

	@Override
	public T remove(int givenPosition) {
		Node<T[]> nodeForIdx = getNodeForIndex(givenPosition);
		T outData = nodeForIdx.getData()[givenPosition % capacity];
		fillGap(nodeForIdx, givenPosition);
		return outData;
	}

	@Override
	public Object[] toArray() {
		Object[] result = new Object[numOfEntries];
		int resultIdx = 0;
		for(Node<T[]> curr = startNode; curr != null; curr = curr.getNext()) {
			T[] nodeData = curr.getData();
			for(int i = 0; i < capacity; i++) {
				if(nodeData[i] == null)
					break;
				result[resultIdx++] = nodeData[i];
			}
		}
		return result;
	}

	@Override
	public boolean remove(T anEntry) {
		int chainIdx = 0;
		for(Node<T[]> curr = startNode; curr != null; curr = curr.getNext()) {
			for(int idx = 0; idx < capacity; idx++) {
				if(curr.getData()[idx].equals(anEntry)) {
					remove(chainIdx);
					return true;
				}
				chainIdx++;
			}
		}
		return false;
	}

	@Override
	public T replace(int givenPosition, T newEntry) {
		if(givenPosition < 0 || givenPosition >= numOfEntries)
			throw new IndexOutOfBoundsException("Invalid index to replace");
		Node<T[]> nodeForIdx = getNodeForIndex(givenPosition);
		T[] nodeData = nodeForIdx.getData();
		T replacedData = nodeData[givenPosition % capacity];
		nodeData[givenPosition % capacity] = newEntry;
		return replacedData;
	}

	@Override
	public boolean isEmpty() {
		return numOfEntries == 0;
	}

	@Override
	public boolean contains(T anEntry) {
		for(Node<T[]> curr = startNode; curr != null; curr = curr.getNext()) {
			for(T entry : curr.getData()) {
				if(anEntry.equals(entry)) 
					return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		numOfEntries = 0;
		startNode = new Node<>((T[]) new Object[capacity]);
	}

	@Override
	public T getEntry(int givenPosition) {
		if(givenPosition < 0 || givenPosition >= numOfEntries)
			throw new IndexOutOfBoundsException("Cannot get entry with given position");
		Node<T[]> nodeForIdx = getNodeForIndex(givenPosition);
		return nodeForIdx.getData()[givenPosition % capacity];
	}

	@Override
	public int getLength() {
		return numOfEntries;
	}
	
	public static void main(String[] args) {
		ResizableAList<Integer> testList = new ResizableAList<>();
		System.out.println(testList.isEmpty());
		for(int i = 0; i < 1400; i++) {
			testList.add(i);
		}
		System.out.println(Arrays.toString(testList.toArray()));
		
		System.out.println(testList.contains(7));
		
		System.out.println(testList.contains(23));
		
		System.out.println(testList.isEmpty());
		
		testList.add(0, 69);
		
		System.out.println(Arrays.toString(testList.toArray()));
		
		testList.add(3, 72);
		
		System.out.println(Arrays.toString(testList.toArray()));
		
		testList.add(15, 799);
		
		System.out.println(Arrays.toString(testList.toArray()));
		
		System.out.println(testList.getEntry(15));
		
		System.out.println(testList.getEntry(16));
		
		System.out.println(testList.getEntry(6));
		
		testList.remove(2);
		
		System.out.println(Arrays.toString(testList.toArray()));
		
		ResizableAList<String> nameList = new ResizableAList<>();
		
		nameList.add("Jaden");
		nameList.add("Jada");
		nameList.add("Torria");
		
		System.out.println(Arrays.toString(nameList.toArray()));
		
		String removed = nameList.remove(1);
		
		System.out.println(removed);
		System.out.println(Arrays.toString(nameList.toArray()));
		
		testList.remove(Integer.valueOf(799));
		
		System.out.println(Arrays.toString(testList.toArray()));
		
		nameList.remove("Jaden");
		System.out.println(Arrays.toString(nameList.toArray()));
		
		testList.replace(14, 5667);
		
		System.out.println(Arrays.toString(testList.toArray()));
		
		nameList.replace(0, "Bob");
		
		System.out.println(Arrays.toString(nameList.toArray()));
		
	}

}
