package org.hitachivantara.ci

import org.hitachivantara.ci.config.FilteredMapWithDefault
import spock.lang.Specification

class TestMapWrapper extends Specification {

  def "test getInt return's an Integer"() {
    given:
      FilteredMapWithDefault map = new FilteredMapWithDefault([:])
      map.putAll(defaults)

    expect: "asking for the specific type should return anything but null"
      map.getInt('k').class == Integer

    where:
      defaults << [
          [:],
          ['k': 1],
          ['k': null]
      ]
  }

  def "test getDouble return's a Double"() {
    given:
      FilteredMapWithDefault map = new FilteredMapWithDefault([:])
      map.putAll(defaults)

    expect: "asking for the specific type should return anything but null"
      map.getDouble('k').class == Double

    where:
      defaults << [
          [:],
          ['k': 1d],
          ['k': null]
      ]
  }

  def "test getBool return's a Boolean"() {
    given:
      FilteredMapWithDefault map = new FilteredMapWithDefault([:])
      map.putAll(defaults)

    expect: "asking for the specific type should return anything but null"
      map.getBool('k').class == Boolean

    where:
      defaults << [
          [:],
          ['k': true],
          ['k': null]
      ]
  }

  def "test getString return's a String"() {
    given:
      FilteredMapWithDefault map = new FilteredMapWithDefault([:])
      map.putAll(defaults)

    expect: "asking for the specific type should return anything but null"
      map.getString('k').class == String

    where:
      defaults << [
          [:],
          ['k': 'hello world'],
          ['k': null]
      ]
  }

  def "test getList return's a List"() {
    given:
      FilteredMapWithDefault map = new FilteredMapWithDefault([:])
      map.putAll(defaults)

    expect: "asking for the specific type should return anything but null"
      map.getList('k').class == ArrayList

    where:
      defaults << [
          [:],
          ['k': ['hello world']],
          ['k': null]
      ]
  }

  def "test getting raw map"() {
    given:
      FilteredMapWithDefault map = new FilteredMapWithDefault([:], [
        PROP_1: '1',
        PROP_2: '${PROP_1} + 2',
        PROP_3: '${PROP_2} + 3'
      ])

    expect:
      map == [
        PROP_1: '1',
        PROP_2: '1 + 2',
        PROP_3: '1 + 2 + 3'
      ]
      map.getRawMap() == [
        PROP_1: '1',
        PROP_2: '${PROP_1} + 2',
        PROP_3: '${PROP_2} + 3'
      ]
  }

  def "test plus implementation does not wrongly perform filtering"() {
    given:
      FilteredMapWithDefault map = new FilteredMapWithDefault([:], [
        PROP_1: '1',
        PROP_2: '${PROP_1} + 2',
        PROP_3: '${PROP_2} + 3'
      ])
      Map right = [PROP_1: 'X']

    expect:
      map + right == [
        PROP_1: 'X',
        PROP_2: 'X + 2',
        PROP_3: 'X + 2 + 3'
      ]
  }

  def "test leftshift "() {
    given:
      FilteredMapWithDefault map = new FilteredMapWithDefault([:], [
        PROP_1: [
          INNER_PROP_1: 1,
          INNER_PROP_2: 2,
          INNER_PROP_3: [
            INNER_INNER_PROP_1: 1
          ],
        ],
        PROP_2: 'hello',
        PROP_3: '${PROP_2} world'
      ])
      Map right = [
        PROP_1: [
          INNER_PROP_2: 3,
          INNER_PROP_3: [
            INNER_INNER_PROP_2: 2
          ],
          INNER_PROP_4: 4
        ],
        PROP_2: 'bye',
      ]

    expect:
      map << right == [
        PROP_1: [
          INNER_PROP_1: 1,
          INNER_PROP_2: 3,
          INNER_PROP_3: [
            INNER_INNER_PROP_1: 1,
            INNER_INNER_PROP_2: 2
          ],
          INNER_PROP_4: 4
        ],
        PROP_2: 'bye',
        PROP_3: 'bye world'
      ]
  }

}