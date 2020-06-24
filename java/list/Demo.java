

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

public class Demo {

    private static ArrayList<Person> personsByAL = new ArrayList<>();
    private static LinkedList<Person> personsByLL = new LinkedList<>();
    private static Long timeStart;
    private static Long timeEnd;
    public static void main(String[] args) {
        timeStart=System.currentTimeMillis();
        addByArrayList();
        timeEnd=System.currentTimeMillis();
        System.out.println("time add AL :"+(timeEnd-timeStart));


        timeStart=System.currentTimeMillis();
        addByLinkedList();
        timeEnd=System.currentTimeMillis();
        System.out.println("time add LL :"+(timeEnd-timeStart));

        Comparator<Person> c = new Comparator<Person>()
        {
            public int compare(Person u1, Person u2)
            {
                return u1.getName().compareTo(u2.getName());
            }
        };

        Person person = personsByAL.get(5000);

        Collections.sort(personsByAL,c);
        timeStart=System.currentTimeMillis();
        int indexAL = Collections.binarySearch(personsByAL,person,c);
        timeEnd=System.currentTimeMillis();
        System.out.println("index : " + indexAL);
        System.out.println("time search AL:"+(timeEnd-timeStart));


        Collections.sort(personsByLL,c);
        timeStart=System.currentTimeMillis();
        int indexLL = Collections.binarySearch(personsByLL,person,c);
        timeEnd=System.currentTimeMillis();
        System.out.println("index : " + indexLL);
        System.out.println("time search LL:"+(timeEnd-timeStart));
//        personByLL = new LinkedList<>(personByLL);
//        printList();
    }

    public static void addByArrayList(){
        for(int i=0;i<10000000;i++){
            String rdName = "pham van dat"+i;
            personsByAL.add(new Person(rdName,12,false));
        }
    }

    public static void addByLinkedList(){
        for(int i=0;i<10000000;i++){
            String rdName = "pham van dat"+i;
            personsByLL.add(new Person(rdName,12,false));
        }
    }

//    public static void searchBinary(){
//        int mid = personsByAL.size();
//        if()
//    }
//    public static void printList(){
//        for (Person person : personsByAL) {
//            System.out.println(person.toString());
//        }
//    }


}
 class Person{
    private String name;
    private int age;
    private boolean gender;

    public Person(String name, int age, boolean gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        age = age;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
}
