package portfoliomanager.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Creates a collection class of cryptos
 * 
 * @author Group 2
 * @version Spring 2025
 */
public class CryptoCollection implements List<Crypto> {
	private ArrayList<Crypto> cryptos;
	
	/**
	 * Instantiates a new collection of cryptos
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 */
	public CryptoCollection() {
		this.cryptos = new ArrayList<Crypto>();
	}
	
	/** Gets the list of cryptos
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the list of cryptos
	 */
	public ArrayList<Crypto> getCryptos() {
		return this.cryptos;
	}
	
	/** Adds the kind of crypto to the list of cryptos
	 * 
	 * @precondition crypto != null
	 * @postcondition this.cryptos.size() @post = this.cryptos.size() @pre + 1
	 * 
	 * @param crypto the crypto to be added to the collection
	 */
	public void addCrypto(Crypto crypto) {
		if (crypto == null) {
			throw new IllegalArgumentException("Crypto data to be added in the collection must not be null.");
		}
		this.cryptos.add(crypto);
	}

	@Override
	public int size() {
		return this.cryptos.size();
	}

	@Override
	public boolean isEmpty() {
		return this.cryptos.isEmpty();
	}

	@Override
	public boolean contains(Object object) {
		return this.cryptos.contains(object);
	}

	@Override
	public Iterator<Crypto> iterator() {
		return this.cryptos.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.cryptos.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return this.cryptos.toArray(array);
	}

	@Override
	public boolean add(Crypto crypto) {
		return this.cryptos.add(crypto);
	}

	@Override
	public boolean remove(Object object) {
		return this.cryptos.remove(object);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return this.cryptos.containsAll(collection);
	}

	@Override
	public boolean addAll(Collection<? extends Crypto> collection) {
		return this.cryptos.addAll(collection);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Crypto> collection) {
		return this.cryptos.addAll(index, collection);
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return this.cryptos.removeAll(collection);
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		return this.cryptos.retainAll(collection);
	}

	@Override
	public void clear() {
		this.cryptos.clear();
		
	}

	@Override
	public Crypto get(int index) {
		return this.cryptos.get(index);
	}

	@Override
	public Crypto set(int index, Crypto element) {
		return this.cryptos.set(index, element);
	}

	@Override
	public void add(int index, Crypto element) {
		this.cryptos.add(index, element);
		
	}

	@Override
	public Crypto remove(int index) {
		return this.cryptos.remove(index);
	}

	@Override
	public int indexOf(Object object) {
		return this.cryptos.indexOf(object);
	}

	@Override
	public int lastIndexOf(Object object) {
		return this.cryptos.lastIndexOf(object);
	}

	@Override
	public ListIterator<Crypto> listIterator() {
		return this.cryptos.listIterator();
	}

	@Override
	public ListIterator<Crypto> listIterator(int index) {
		return this.cryptos.listIterator(index);
	}

	@Override
	public List<Crypto> subList(int fromIndex, int toIndex) {
		return this.cryptos.subList(fromIndex, toIndex);
	}
}
