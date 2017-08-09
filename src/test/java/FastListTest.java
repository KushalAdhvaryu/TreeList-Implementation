/**
 * Created by KushalAdhvaryu on 2/28/17.
 */

import java.util.*;
import java.lang.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// For testing I have verified my test cases for FastList against Array List implementation of java. 

public class FastListTest {

    @Test
    // Testing right right case of insertion.
    public void rightRightCase(){
        List<Integer> al = new ArrayList<>();
        List<Integer> fl = new FastList<>();

        for(int i = 0; i < 3; i++){
            al.add(i);
            fl.add(i);
        }

        Iterator<Integer> alItr = al.iterator();
        Iterator<Integer> flItr = fl.iterator();
        while(alItr.hasNext()){
            assertEquals(alItr.next(), flItr.next());
        }

    }

    @Test
    // Testing left left case of insertion.
    public void leftLeftCase(){
        List<Integer> al = new ArrayList<>();
        List<Integer> fl = new FastList<>();

        for(int i = 0; i < 3; i++){
            al.add(i);
            fl.add(i);
        }

        al.add(0,10);
        fl.add(0,10);
        al.add(0,20);
        fl.add(0,20);

        Iterator<Integer> alItr = al.iterator();
        Iterator<Integer> flItr = fl.iterator();
        while(alItr.hasNext()){
            assertEquals(alItr.next(), flItr.next());
        }


    }

    @Test
    // Testing  left right  case of insertion.
    public void leftRightCase(){
        List<Integer> al = new ArrayList<>();
        List<Integer> fl = new FastList<>();

        for(int i = 0; i < 10; i++){
            al.add(i);
            fl.add(i);
        }
        al.add(0,-1);
        fl.add(0,-1);
        al.add(0,-2);
        fl.add(0,-2);

        al.add(2,-3);
        fl.add(2,-3);

        Iterator<Integer> alItr = al.iterator();
        Iterator<Integer> flItr = fl.iterator();
        while(alItr.hasNext()){
            assertEquals(alItr.next(), flItr.next());
        }

    }

    @Test
    // Testing right left  case of insertion.
    public void rightLeftCase(){
        List<Integer> al = new ArrayList<>();
        List<Integer> fl = new FastList<>();

        for(int i = 0; i < 3; i++){
            al.add(i);
            fl.add(i);
        }

        al.add(1,10);
        fl.add(1,10);
        al.add(1,20);
        fl.add(1,20);

        Iterator<Integer> alItr = al.iterator();
        Iterator<Integer> flItr = fl.iterator();
        while(alItr.hasNext()){
            assertEquals(alItr.next(), flItr.next());
        }

    }

    @Test
    // Case when there is only one root node. Deletion.
    public void delRoot(){
        List<Integer> al = new ArrayList<>();
        List<Integer> fl = new FastList<>();

        al.add(1);
        fl.add(1);

        assertEquals(al.remove(0),fl.remove(0));

    }

    @Test
    // Case when internal node is deleted
    public void delInternal(){
        List<Integer> al = new ArrayList<>();
        List<Integer> fl = new FastList<>();

        for(int i = 0; i < 3; i++){
            al.add(i);
            fl.add(i);
        }

        assertEquals(al.remove(1),fl.remove(1));

    }

    @Test
    // Case when leaf node is deleted
    public void delLeaf(){
        List<Integer> al = new ArrayList<>();
        List<Integer> fl = new FastList<>();

        for(int i = 0; i < 3; i++){
            al.add(i);
            fl.add(i);
        }

        assertEquals(al.remove(2),fl.remove(2));

    }

    @Test
    // Case node has only right child.
    public void delInternalRight(){
        List<Integer> al = new ArrayList<>();
        List<Integer> fl = new FastList<>();

        for(int i = 0; i < 4; i++){
            al.add(i);
            fl.add(i);
        }


        assertEquals(al.remove(2),fl.remove(2));

    }

    @Test
    // Case node has only left child.
    public void delInternalLeft(){
        List<Integer> al = new ArrayList<>();
        List<Integer> fl = new FastList<>();

        for(int i = 0; i < 6; i++){
            al.add(i);
            fl.add(i);
        }

        assertEquals(al.remove(2),fl.remove(2));
        assertEquals(al.remove(1),fl.remove(1));
    }

    @Test
    // Add and remove single element in list.
    public void testCase1(){
        List<String> al = new ArrayList<>();
        List<String> fl = new FastList<>();

        al.add("abc");
        fl.add("abc");

        Iterator<String> alItr = al.iterator();
        Iterator<String> flItr = fl.iterator();
        while(alItr.hasNext()){
            assertEquals(alItr.next(), flItr.next());
        }

        al.remove(0);
        fl.remove(0);
        assertEquals(al.size(), fl.size());


    }

    @Test
    // Add elements at end of list.
    public void testCase2(){
        List<Integer> al = new ArrayList<Integer>();
        List<Integer> fl = new FastList<Integer>();

        for(int i = 0; i < 100; i++){
            al.add(i);
            fl.add(i);
        }

        Iterator<Integer> alItr = al.iterator();
        Iterator<Integer> flItr = fl.iterator();
        while(alItr.hasNext()){
            assertEquals(alItr.next(), flItr.next());
        }
    }

    @Test
    // Checking null handling in list.
    public void testCase3(){
        List<Integer> fl = new FastList<Integer>();
        List<Integer> al = new ArrayList<Integer>();

        for(int i = 0; i < 10; i++){
            al.add(i);
            fl.add(i);
        }

        // Adding few null in list.
        al.add(1,null);
        fl.add(1,null);
        al.add(1,null);
        fl.add(1,null);
        al.remove(1);
        fl.remove(1);

        Iterator<Integer> alItr = al.iterator();
        Iterator<Integer> flItr = fl.iterator();
        while(alItr.hasNext()){
            assertEquals(alItr.next(), flItr.next());
        }


    }

    @Test
    // Verifying add element at index metohd.
    public void testCase4(){
        List<Integer> al = new ArrayList<Integer>();
        List<Integer> fl = new FastList<Integer>();


        //Adding few 100 objects to list.
        for(int i = 0; i < 100; i++){
            al.add(i);
            fl.add(i);
        }

        // Adding 100 objects at front.
        for(int i = 0; i < 100; i++){
            al.add(0,i*4);
            fl.add(0,i*4);
        }

        // Adding 100 objects in between.
        for(int i = 0; i < 100; i++){
            al.add(100,i*5);
            fl.add(100,i*5);
        }


        //check the lists are equal..
        Iterator<Integer> flItr = fl.iterator();
        Iterator<Integer> alItr = al.iterator();
        while(alItr.hasNext()){
            assertEquals(alItr.next(), flItr.next());
        }
    }

    @Test
    // Verifying remove object at index.
    public void testCase5(){
        List<Integer> al = new ArrayList<Integer>();
        List<Integer> fl = new FastList<Integer>();

        // Adding 1k nodes to list.
        for(int i = 0; i < 1000; i++){
                al.add(i);
                fl.add(i);
        }

        // Removing nodes at some location in list.
        for(int i=1000-1; i >=0 ; i = i-2){
            assertEquals(al.remove(i),fl.remove(i));

        }

        // Verifying values after removal.
        Iterator<Integer> alItr = al.iterator();
        Iterator<Integer> flItr = fl.iterator();
        while(alItr.hasNext()){
            assertEquals(alItr.next(), flItr.next());
        }
    }

    @Test
    // Verifying set index at method.
    public void testCase6(){
        List<Integer> al = new ArrayList<Integer>();
        List<Integer> fl = new FastList<Integer>();

        // Adding 100 objects
        for(int i = 0; i < 100; i++){
            al.add(i);
            fl.add(i);
        }

        // Setting values at some indexes.
        for(int i = 1; i < 100; i=i*2){
            al.set(i, i-1);
            fl.set(i, i-1);
        }

        // Verifying both list for values.
        Iterator<Integer> flItr = fl.iterator();
        Iterator<Integer> alItr = al.iterator();

        while(alItr.hasNext()){
            assertEquals(alItr.next(), flItr.next());
        }
    }

    @Test
    // Generating entire list and emptying entire list.
    public void testCase7(){
        List<Integer> al = new ArrayList<Integer>();
        List<Integer> fl = new FastList<Integer>();

        // Adding 500 objects.

        for(int i = 0; i < 500; i++){
            al.add(i);
            fl.add(i);
        }

        // Adding another 500 object but from 0 -499 index.

        for(int i = 0; i < 500; i++){
            al.add(i,i+2);
            fl.add(i,i+2);
        }

        // Manipulating values of objects between [100,250)
        for(int i = 100; i < 250; i++){
            al.set(i,i*2);
            fl.set(i,i*2);
        }

        // Iterating to verify.
        Iterator<Integer> flItr = fl.iterator();
        Iterator<Integer> alItr = al.iterator();

        while(alItr.hasNext()){
            assertEquals(alItr.next(), flItr.next());
        }

        // Removing some elements form 100 - 200.
        for(int i = 0; i < 200; i++){
            al.remove(100);
            fl.remove(100);
        }

        // Emptying entire tree.
        for(int i = 0; i < al.size(); i++){
            al.remove(0);
            fl.remove(0);
        }

        // Evaluating values to be equal which in fact is empty.
        assertEquals(al.size(), fl.size());
    }

}
