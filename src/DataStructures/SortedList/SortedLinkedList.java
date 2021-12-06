package SortedList;


/**
 * Implementation of a SortedList using a SinglyLinkedList
 * @author Fernando J. Bermudez, Juan O. Lopez
 * @author Sebastian J. Caballero Diaz
 * @version 2.0
 * @since 10/16/2021
 */
public class SortedLinkedList<E extends Comparable<? super E>> extends AbstractSortedList<E> {

	@SuppressWarnings("unused")
	private static class Node<E> {

		private E value;
		private Node<E> next;

		public Node(E value, Node<E> next) {
			this.value = value;
			this.next = next;
		}

		public Node(E value) {
			this(value, null); // Delegate to other constructor
		}

		public Node() {
			this(null, null); // Delegate to other constructor
		}

		public E getValue() {
			return value;
		}

		public void setValue(E value) {
			this.value = value;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setNext(Node<E> next) {
			this.next = next;
		}

		public void clear() {
			value = null;
			next = null;
		}				
	} // End of Node class


	private Node<E> head; // First DATA node (This is NOT a dummy header node)

	public SortedLinkedList() {
		head = null;
		currentSize = 0;
	}

	/**
	 * Inserts the element e into a new node. If the head is null, the head is now the
	 * newNode. If the new value is the smallest, then the newNode becomes the head.
	 * Loops through the list to add this new node before the value that it should be added.
	 * 
	 * @param e Element with value to be inserted into the list
	 */
	@Override
	public void add(E e) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when the new value is the smallest */
		Node<E> newNode = new Node<>(e); // creating a new node with value e
		Node<E> ref = head; // a reference to the head node
		if(head==null) {
			currentSize++; // incrementing the size since a node was added
			head = newNode;
			return;
		}
		else if(newNode.getValue().compareTo(head.getValue()) < 0) { // special case
			currentSize++; // incrementing the size since a node was added
			newNode.setNext(head); // add new node before the head
			head = newNode; // now the head of the node is the new node
			return;
		}
		else {
			ref = head;
			while(ref.getNext() != null) { // looping through
				if(newNode.getValue().compareTo(ref.getNext().getValue()) < 0) { // if newNode's value is less than ref's next node value
					newNode.setNext(ref.getNext()); // add the newnode before ref's next node
					ref.setNext(newNode); // now ref's next node is newNode
					currentSize++; // incrementing the size since a node was added
					return;
				}
				else if(newNode.getValue().equals(ref.getValue())) { // newNode's value is equal to ref's value
					newNode.setNext(ref.getNext()); // adding the newNode before ref's next node
					ref.setNext(newNode); // now ref's next node is newNode
					currentSize++; // incrementing the size since a node was added
					return;
				}
				ref = ref.getNext();
			}
		}
		// adding the newNode at the end, if it looped through and never got added
		ref.setNext(newNode);
		newNode = ref;
		currentSize++;
	}

	/**
	 * If the element e is found in the nodes, it's removed and the currentSize is updated.
	 * After removal, the nodes that were next and previous of the removed node are connected.
	 * When the value is found at the head node, head becomes the node after head, and the
	 * head is cleared, returning true. The method loops through the list and if it doesn't find e,
	 * returns false
	 * 
	 *  @param e Element to be removed
	 *  @return True if it has been found and removed, false otherwise.
	 */
	@Override
	public boolean remove(E e) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when the value is found at the head node */
		Node<E> rmNode = null, curNode = head;
		if(currentSize <= 0) throw new IndexOutOfBoundsException();
		if(head == null) return false;

		//special case
		if(head.getValue().equals(e)) { // if the value is at the head node
			head = curNode.getNext(); // head node passes on to be its next node
			curNode = head; // head now is now assigned to curNode
			currentSize--; // decreasing the size since a node was removed
			return true;	
		}

		while(curNode != null) { // looping through 
			if(curNode.getValue().equals(e)) { // if the element to remove has been found
				rmNode.setNext(curNode.getNext()); // rmNode's next node is now curNode's next node
				curNode = curNode.getNext(); // curNode is now it's next node, hence removing/unlinking the node that it was
				currentSize--; // decreasing the size since a node was removed
				return true;
			}
			else {
				rmNode = curNode; 
				curNode = curNode.getNext(); // curNode is now its next node (to keep the loop going)
			}
		}
		return false; // not found
	}

	/**
	 * Like remove, but this time it searches by index instead of by the element itself
	 * to remove a node. When it reaches the index, it removes the node in this index and 
	 * connects the other nodes. 
	 * 
	 * @param index The index in the list of the node to be removed 
	 * @return The value that got removed
	 */
	@Override
	public E removeIndex(int index) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when index = 0 */
		Node<E> rmNode = head, curNode = head, tempNode = head;
		E value = null;
		if(currentSize <= 0 || index >= currentSize || index < 0) throw new IndexOutOfBoundsException();
		if(head == null) {
			return null;
		}
		else if(index == 0) { // special case
			rmNode = head; // rmNode is now the head (the one that will be removed)
			head = curNode.getNext(); // the new head is now head's next node
			value = rmNode.getValue(); // assigning the value of the node to be removed
			rmNode.clear(); // clearing said node
			currentSize--; // decreasing the size since a node was removed
			return value;
		}
		else { // is not the head node
			int i = 0;
			while(curNode.getNext() != null) { // looping through the list
				if(i == index) { // if we found the element (matching index)
					// start the process above to remove rmNode
					rmNode = curNode; 
					value = rmNode.getValue();
					tempNode = rmNode;
					tempNode.setNext(curNode.getNext());
					rmNode.clear();
					return value;
				}
				i++;
				curNode = curNode.getNext(); // keep going through the loop
			}
		}
		return value; 
	}

	/**
	 * This method uses a value to search in the list, and returns the first index where this 
	 * value is located. Loops through the list, returning -1 if the value is not found.
	 * 
	 * @param e Element to be found
	 * @return The index of the element that was looked for, or -1 if the element wasn't found
	 */
	@Override
	public int firstIndex(E e) {
		/* TODO ADD CODE HERE */
		int indexOf = -1;
		Node<E> curNode = head;
		if(currentSize <= 0) throw new IndexOutOfBoundsException();
		if(head.getValue().equals(e)) { // value in the head node (first index)
			return 0;
		}
		for (int i = 0; i < currentSize; i++) { // looping through list
			if(curNode.getValue().equals(e)) { // if we found the element (values are matching)
				indexOf = i; // store the index which it was in
				break; // break out the loop
			}
			else { // haven't found it
				curNode = curNode.getNext(); // to keep looping
			}
		}
		return indexOf; // return the index, will return -1 if not found
	}

	/**
	 * Looks for the index specified, and returns the element found in this index.
	 * If after looping the index isn't found, returns null
	 * 
	 * @param index The index to search for the element to return
	 * @return The value that was in the position of the index specified.
	 */
	@Override
	public E get(int index) {
		/* TODO ADD CODE HERE */
		if(currentSize <= 0 || index >= currentSize || index < 0) throw new IndexOutOfBoundsException();
		if(head == null) return null;
		Node<E> target = head; // make the target node
		int i = 0; // count variable 
		while(target != null) { // loop through list
			if(i == index) { // found the index of the element we want
				return target.getValue();  // return the value
			}
			// keep looping
			target = target.getNext(); 
			i++;
		}
		return null; // null if never found
	}



	@SuppressWarnings("unchecked")
	@Override
	public E[] toArray() {
		int index = 0;
		E[] theArray = (E[]) new Comparable[size()]; // Cannot use Object here
		for(Node<E> curNode = this.head; index < size() && curNode  != null; curNode = curNode.getNext(), index++) {
			theArray[index] = curNode.getValue();
		}
		return theArray;
	}

}
