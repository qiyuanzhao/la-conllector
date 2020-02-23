package com.lavector.collector.crawler.test;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 2018/11/22.
 *
 * @author zeng.zhao
 */
public class EqualsTest {

    public static void main(String[] args) {
        Person person = new Person("张三", 12);
        Person person1 = new Person("张三", 12);

        Set<Person> personSet = new HashSet<>();
        personSet.add(person);
        personSet.add(person1);
        System.out.println(personSet);
        System.out.println(person);
        System.out.println(person1);
    }


}

class Person{
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Person person = (Person) o;
//
//        return new EqualsBuilder()
//                .append(age, person.age)
//                .append(name, person.name)
//                .isEquals();
//    }
//
//    @Override
//    public int hashCode() {
//        return new HashCodeBuilder(17, 37)
//                .append(name)
//                .append(age)
//                .toHashCode();
//    }
}
