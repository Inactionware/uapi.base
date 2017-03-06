package uapi.common

import spock.lang.Specification
import uapi.IIdentifiable

/**
 * Unit test for Repository
 */
class RepositoryTest extends Specification {

    def 'Test create instance'() {
        when:
        def repo = new Repository()

        then:
        noExceptionThrown()
        repo.count() == 0
    }

    def 'Test get and set data'() {
        given:
        def testId = new TestIdentify(id)
        def repo = new Repository<String, TestIdentify>()

        when:
        repo.put(testId)
        def data = repo.get(id)

        then:
        noExceptionThrown()
        data.id == id
        repo.count() == 1

        where:
        id      | placeholder
        'Test'  | null
    }

    class TestIdentify implements IIdentifiable<String> {

        private String id

        TestIdentify(String id) {
            this.id = id
        }

        @Override
        String getId() {
            return this.id
        }
    }
}
