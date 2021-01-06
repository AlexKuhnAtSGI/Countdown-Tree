package comp2402a5;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.lang.reflect.Array;

/**
 * An unfinished implementation of an Countdown tree (for exercises)
 * @author morin
 *
 * @param <T>
 */
public class CountdownTree<T> extends
  BinarySearchTree<CountdownTree.Node<T>, T> implements SSet<T> {
  
  // countdown delay factor
  double d;
  
  public static class Node<T> extends BSTNode<Node<T>,T> {
    int timer;  // the height of the node
  }
  
  public CountdownTree(double d) {
    this.d = Math.ceil(d);
    sampleNode = new Node<T>();
    c = new DefaultComparator<T>();
  }
  
  public boolean add(T x) {
    Node<T> u = new Node<T>();
    u.timer = (int)Math.ceil(d);
    u.x = x;
    if (super.add(u)) {
      for (Node<T> w = u; w != nil; w = w.parent) {
        w.timer--;
        if (w.timer <= 0) {
          explode(w);
        }
      }
      return true;
    }
    return false;
  }
  
  public void splice(Node<T> u) {
    Node<T> w = u.parent;
    super.splice(u);
    // add some code here (we just removed u from the tree
    for (Node<T> v = w; v != nil; v = v.parent) {
        v.timer--;
        if (v.timer <= 0) {
          explode(v);
        }
      }
  }
  
  protected void explode(Node<T> u) {
    int ns = size(u);
    Node<T> p = u.parent;
    Node<T>[] a = (Node<T>[]) Array.newInstance(Node.class, ns);
    packIntoArray(u, a, 0);
    if (p == nil) {
      r = buildBalanced(a, 0, ns);
      r.parent = nil;
    } else if (p.right == u) {
      p.right = buildBalanced(a, 0, ns);
      p.right.parent = p;
    } else {
      p.left = buildBalanced(a, 0, ns);
      p.left.parent = p;
    }
  }
  int packIntoArray(Node<T> u, Node<T>[] a, int i) {
    if (u == nil) {
      return i;
    }
    i = packIntoArray(u.left, a, i);
    a[i++] = u;
    return packIntoArray(u.right, a, i);
  }
  Node<T> buildBalanced(Node<T>[] a, int i, int ns) {
    if (ns == 0)
      return nil;
    int m = ns / 2;
    a[i + m].left = buildBalanced(a, i, m);
    if (a[i + m].left != nil)
      a[i + m].left.parent = a[i + m];
    a[i + m].right = buildBalanced(a, i + m + 1, ns - m - 1);
    if (a[i + m].right != nil)
      a[i + m].right.parent = a[i + m];
    a[i + m].timer = (int)Math.ceil(d * size(a[i + m]));
    return a[i + m];
  }
  
  // Here is some test code you can use
  public static void main(String[] args) {
    CountdownTree<Integer> t = new CountdownTree<Integer>(1);
    t.add(2);
    t.add(1);
    System.out.println(t);
    t.add(3);
    System.out.println(t);
    t.add(5);
    System.out.println(t);
    t.add(4);
    System.out.println(t);
    t.add(8);
    System.out.println(t);
    t.add(7);
    System.out.println(t);
    /*Testum.sortedSetSanityTests(new SortedSSet<Integer>(new CountdownTree<Integer>(1)), 1000);
    Testum.sortedSetSanityTests(new SortedSSet<Integer>(new CountdownTree<Integer>(2.5)), 1000);
    Testum.sortedSetSanityTests(new SortedSSet<Integer>(new CountdownTree<Integer>(0.5)), 1000);
    
    java.util.List<SortedSet<Integer>> ell = new java.util.ArrayList<SortedSet<Integer>>();
    ell.add(new java.util.TreeSet<Integer>());
    ell.add(new SortedSSet<Integer>(new CountdownTree<Integer>(1)));
    ell.add(new SortedSSet<Integer>(new CountdownTree<Integer>(2.5)));
    ell.add(new SortedSSet<Integer>(new CountdownTree<Integer>(0.5)));
    Testum.sortedSetSpeedTests(ell, 1000000);*/
  }
}
