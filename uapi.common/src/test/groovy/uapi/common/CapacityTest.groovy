package uapi.common

import spock.lang.Specification

class CapacityTest extends Specification {

    def 'Test parse'() {
        when:
        def cap = Capacity.parse(str)

        then:
        cap.toByteVolume() == bytes
        cap.toKBVolume() == kb
        cap.toMBVolume() == mb
        cap.toGBVolume() == gb
        cap.toTBVolume() == tb
        cap.toPBVolume() == pb

        where:
        str         | bytes                                 | kb                                | mb                        | gb                | tb        | pb
        '100Byte'   | 100                                   | 0                                 | 0                         | 0                 | 0         | 0
        '512KB'     | 512 * 1024                            | 512                               | 0                         | 0                 | 0         | 0
        '200MB'     | 200 * 1024 * 1024                     | 200 * 1024                        | 200                       | 0                 | 0         | 0
        '2GB'       | 2L * 1024 * 1024 * 1024               | 2 * 1024 * 1024                   | 2 * 1024                  | 2                 | 0         | 0
        '3TB'       | 3L * 1024 * 1024 * 1024 * 1024        | 3L * 1024 * 1024 * 1024           | 3L * 1024 * 1024          | 3L * 1024         | 3         | 0
        '5PB'       | 5L * 1024 * 1024 * 1024 * 1024 * 1024 | 5L * 1024 * 1024 * 1024 * 1024    | 5L * 1024 * 1024 * 1024   | 5L * 1024 * 1024  | 5L * 1024 | 5
    }
}
