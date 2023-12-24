package lemmings.game.app;

import java.util.concurrent.atomic.AtomicReference;


/**
 * Implémentation d'une file d'attente lock-free.
 * @param <T> Type de valeur stockée dans la file d'attente.
 */
public class LockFreeQueue<T> {
    private AtomicReference<Node<T>> head;
    private AtomicReference<Node<T>> tail;

    /**
     * Initialise une nouvelle file d'attente lock-free.
     */
    public LockFreeQueue() {
        Node<T> mark = new Node<>(null);
        this.head = new AtomicReference<>(mark);
        this.tail = new AtomicReference<>(mark);
    }


    /**
     * Ajoute un élément à la file d'attente.
     * @param value Valeur à ajouter.
     */
    public void offer(T value) {
        Node<T> newNode = new Node<>(value);
        while (true) {
            Node<T> currentTail = tail.get();
            Node<T> tailNext = currentTail.next.get();
            if (currentTail == tail.get()) {
                if (tailNext != null) {
                    tail.compareAndSet(currentTail, tailNext);
                } else {
                    if (currentTail.next.compareAndSet(null, newNode)) {
                        tail.compareAndSet(currentTail, newNode);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Récupère l'élément en tête de la file d'attente sans le supprimer.
     * @return Élément en tête de la file d'attente, null si la file est vide.
     */
    public T peek() {
        while (true) {
            Node<T> currentHead = head.get();
            Node<T> currentTail = tail.get();
            Node<T> headNext = currentHead.next.get();
            if (currentHead == head.get()) {
                if (currentHead == currentTail) {
                    if (headNext == null) {
                        return null;
                    }
                    tail.compareAndSet(currentTail, headNext);
                } else {
                    return headNext.value; 
                }
            }
        }
    }

    /**
     * Récupère et supprime l'élément en tête de la file d'attente.
     * @return Élément en tête de la file d'attente, null si la file est vide.
     */
    public T poll() {
        while (true) {
            Node<T> currentHead = head.get();
            Node<T> currentTail = tail.get();
            Node<T> headNext = currentHead.next.get();
            if (currentHead == head.get()) {
                if (currentHead == currentTail) {
                    if (headNext == null) {
                        return null;
                    }
                    tail.compareAndSet(currentTail, headNext);
                } else {
                    T value = headNext.value;
                    if (head.compareAndSet(currentHead, headNext)) {
                        return value;
                    }
                }
            }
        }
    }



/**
 * Vérifie si l'élément spécifié est présent dans la file d'attente.
 * @param value Valeur à rechercher dans la file d'attente.
 * @return true si l'élément est trouvé, sinon false.
 */
public boolean contains(T value) {
    Node<T> current = head.get().next.get();
    while (current != null) {
        if (current.value.equals(value)) {
            return true;
        }
        current = current.next.get();
    }
    return false;
}

   /**
    * Représente un nœud dans la file d'attente lock-free.
    * @param <T> Type de valeur stockée dans le nœud.
    */
    class Node<T> {
        T value;
        AtomicReference<Node<T>> next;

        Node(T value) {
            this.value = value;
            this.next = new AtomicReference<>(null);
        }
    }
}

