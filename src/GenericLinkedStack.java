public class GenericLinkedStack<E> implements Stack<E>  {
    
    private static class Elem<E> {
        private E info;
        private Elem<E> next;
        
        private Elem( E info, Elem<E> next) {
            this.info = info;
            this.next = next;
        }
    }
    
    private Elem<E> top; // Instance variable
    
    public GenericLinkedStack() {
        top = null;
    }    
    
    public boolean isEmpty() {
        return top == null;
    }
    
    public void push( E info ) {
        if (info == null) throw new NullPointerException();
        top = new Elem (info, top);
    }
    
    public E peek() {
        if (isEmpty()) throw new EmptyStackException();
        return top.info;
    }
    
    public E pop() {
        if (isEmpty()) throw new EmptyStackException();
        E elem = top.info;
        top = top.next;
        return elem;
    }
    
}
