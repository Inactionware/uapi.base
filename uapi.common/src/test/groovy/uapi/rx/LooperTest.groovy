/**
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx

import spock.lang.Ignore
import spock.lang.Specification

/**
 * Test case for Looper
 */
class LooperTest extends Specification {

    def 'Test iterator from one item'() {
        when:
        List<Integer> list = new ArrayList<>()
        Looper.on("1").map({ item -> Integer.parseInt(item)}).foreach({ item -> list.add(item)})

        then:
        list.size() == size
        list.get(0) == first

        where:
        size    | first
        1       | 1
    }

    def 'Test iterator from array'() {
        when:
        List<Integer> list = new ArrayList<>()
        Looper.on("1", "2", "3").map({ item -> Integer.parseInt(item)}).foreach({ item -> list.add(item)})

        then:
        list.size() == size
        list.get(0) == first
        list.get(1) == second
        list.get(2) == third

        where:
        size    | first     | second    | third
        3       | 1         | 2         | 3
    }

    def 'Test iterator from collection'() {
        when:
        List<Integer> list = new ArrayList<>()
        Looper.on(["1", "2", "3"] as Collection).map({ item -> Integer.parseInt(item)}).foreach({ item -> list.add(item)})

        then:
        list.size() == size
        list.get(0) == first
        list.get(1) == second
        list.get(2) == third

        where:
        size    | first     | second    | third
        3       | 1         | 2         | 3
    }

    def 'Test iterator from iterator'() {
        when:
        List<Integer> list = new ArrayList<>()
        Looper.on(["1", "2", "3"].iterator()).map({ item -> Integer.parseInt(item)}).foreach({ item -> list.add(item)})

        then:
        list.size() == size
        list.get(0) == first
        list.get(1) == second
        list.get(2) == third

        where:
        size    | first     | second    | third
        3       | 1         | 2         | 3
    }

    def 'Test iterator from iterable'() {
        when:
        List<Integer> list = new ArrayList<>()
        Looper.on(["1", "2", "3"] as Iterable).map({ item -> Integer.parseInt(item)}).foreach({ item -> list.add(item)})

        then:
        list.size() == size
        list.get(0) == first
        list.get(1) == second
        list.get(2) == third

        where:
        size    | first     | second    | third
        3       | 1         | 2         | 3
    }

    def 'Test iterator from Range'() {
        when:
        def list = []
        Looper.on(uapi.common.Range.from(start).to(end)).foreach({item -> list.add(item)})

        then:
        list == result

        where:
        start   | end   | result
        0       | 10    | [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    }

    def 'Test on enumeration'() {
        setup:
        Enumeration<String> enumeration = Mock(Enumeration) {
            hasMoreElements() >>> [true, true, true, true, true, true, false]
            nextElement() >>> [item1, item2, item3, item4]
        }

        when:
        def list = []
        Looper.on(enumeration).foreach({item -> list.add(item)})

        then:
        list == result

        where:
        item1   | item2 | item3 | item4 | result
        '1'     | '2'   | '3'   | null  | ['1', '2', '3']
    }

    @Ignore
    def 'test rv vx. rxJava'() {
        List<Integer> list = new ArrayList<>();
        for (id in 1..1000000) {
            list.add(id)
        }

        given:
        List<String> strList2 = new ArrayList<>()
        long start2 = System.currentTimeMillis()
        Looper.on(list).map({ item -> String.valueOf(item)}).foreach({ item -> strList2.add(item)})
        long end2 = System.currentTimeMillis()
        System.out.println(end2 - start2)

        List<String> strList1 = new ArrayList<>()
        long start1 = System.currentTimeMillis()
        Looper.on(list).map({ item -> String.valueOf(item)}).foreach({ item -> strList1.add(item)})
        long end1 = System.currentTimeMillis()
        System.out.println(end1 - start1)

        expect:
        strList1.size() == 1000000
        strList1.get(0) == "1"
        strList1.get(99) == "100"

        strList2.size() == 1000000
        strList2.get(0) == "1"
        strList2.get(99) == "100"
    }
}
