/*
 * Copyright (C) 2016 Michael Rozumyanskiy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.michaelrocks.optional

import io.kotlintest.matchers.be
import io.kotlintest.specs.StringSpec
import java.util.ArrayList
import java.util.NoSuchElementException

class OptionalTest : StringSpec() {
  init {
    "isSome for Optional.Some should return true" {
      some(1).isSome shouldBe true
    }

    "isSome for Optional.None should return false" {
      none().isSome shouldBe false
      noneOfType<Unit>().isSome shouldBe false
    }

    "isNone for Optional.Some should return false" {
      some(1).isNone shouldBe false
    }

    "isNone for Optional.None should return true" {
      none().isNone shouldBe true
      noneOfType<Unit>().isNone shouldBe true
    }

    "some() should be equal to Optional.Some()" {
      some(1) shouldBe Optional.Some(1)
    }

    "none() should be equal to Optional.None" {
      none() should be theSameInstanceAs Optional.None
    }

    "noneOfType() should be equal to Optional.None" {
      noneOfType<Unit>() should be theSameInstanceAs Optional.None
    }

    "get() for Optional.Some should return its value" {
      some(1).get() shouldBe 1
    }

    "get() for Optional.None should throw" {
      shouldThrow<NoSuchElementException> { none().get() }
    }

    "getOrNull() for Optional.Some should return its value" {
      some(1).getOrNull() shouldBe 1
    }

    "getOrNull() for Optional.None should return null" {
      none().getOrNull() shouldBe null
    }

    "getOrElse() for Optional.Some should return its value" {
      some(1).getOrElse { 2 } shouldBe 1
    }

    "getOrElse() for Optional.None should return a fallback value" {
      none().getOrElse { 2 } shouldBe 2
    }

    "getIf() for Optional.Some should return its value if the condition is met" {
      some(1).getIf { it == 1 } shouldBe 1
    }

    "getIf() for Optional.Some should throw if the condition is not met" {
      shouldThrow<NoSuchElementException> { some(1).getIf { it != 1 } }
    }

    "getIf() for Optional.None should throw" {
      shouldThrow<NoSuchElementException> { none().getIf { true } }
      shouldThrow<NoSuchElementException> { none().getIf { false } }
    }

    "getIfOrNull() for Optional.Some should return its value if the condition is met" {
      some(1).getIfOrNull { it == 1 } shouldBe 1
    }

    "getIfOrNull() for Optional.Some should return null if the condition is not met" {
      some(1).getIfOrNull { it != 1 } shouldBe null
    }

    "getIfOrNull() for Optional.None should return null" {
      none().getIfOrNull { neverCalled() } shouldBe null
    }

    "filter() should return an Optional with a value matching a predicate" {
      some(1).filter { it == 1 } shouldBe some(1)
      some(1).filter { it != 1 } shouldBe none()
      none().filter { neverCalled() } shouldBe none()
    }

    "filterNot() should return an Optional with a value not matching the given predicate" {
      some(1).filterNot { it != 1 } shouldBe some(1)
      some(1).filterNot { it == 1 } shouldBe none()
      none().filterNot { neverCalled() } shouldBe none()
    }

    "filterIsInstance() should return an Optional with a value that is an instance of the given class" {
      some<Any>(1).filterIsInstance<Int>() shouldBe some(1)
      some<Any>(1).filterIsInstance<String>() shouldBe none()
      none().filterIsInstance<Any>() shouldBe none()

      some<Any>(1).filterIsInstance(Int::class.javaObjectType) shouldBe some(1)
      some<Any>(1).filterIsInstance(String::class.java) shouldBe none()
      none().filterIsInstance(Any::class.java) shouldBe none()

    }

    "flatMap() should return a transformed Optional" {
      some(1).flatMap { some(it + 1) } shouldBe some(2)
      some(1).flatMap { none() } shouldBe none()
      none().flatMap<Nothing, Nothing> { neverCalled() } shouldBe none()
    }

    "map() should return a transformed value" {
      some(1).map { it + 1 } shouldBe some(2)
      none().map { neverCalled() } shouldBe none()
    }

    "mapNotNull() should return a transformed value is it's not null" {
      some(1).mapNotNull { it + 1 } shouldBe some(2)
      some(1).mapNotNull { null } shouldBe none()
      none().mapNotNull { neverCalled() } shouldBe none()
    }

    "switchIfEmpty() should return a new Optional if the receiver is Optional.None" {
      some(1).switchIfNone { some(2) } shouldBe some(1)
      none().switchIfNone { some(2) } shouldBe some(2)
    }

    "matches() should return true if the value matches the given predicate" {
      some(1).matches { it == 1 } shouldBe true
      some(1).matches { it != 1 } shouldBe false
      none().matches { neverCalled() } shouldBe false
    }

    "matchesOrNone() should return true if the value matches the given predicate or the receiver is Optional.None" {
      some(1).matchesOrNone { it == 1 } shouldBe true
      some(1).matchesOrNone { it != 1 } shouldBe false
      none().matchesOrNone { neverCalled() } shouldBe true
    }

    "fold() should return the initial value combined with the optional value" {
      some(2).fold(8) { result, current -> result / current } shouldBe 4
      none().fold(8) { result, current -> neverCalled() } shouldBe 8
    }

    "forEach() should invoke the action for the optional value" {
      some(1).forEach { it shouldBe 1 }
      none().forEach { neverCalled() }
    }

    "combine() should apply the given transformation when both optionals have values" {
      some(2).combine(some(8)) { left, right -> right / left } shouldBe some(4)
      some(1).combine(none()) { left, right -> neverCalled() } shouldBe some(1)
      none().combine(some(1)) { left, right -> neverCalled() } shouldBe some(1)
      none().combine(none()) { left, right -> neverCalled() } shouldBe none()
    }

    "partition() should return a pair with the first value satisfying the predicate and the second value not satisfying" {
      some(1).partition { it == 1 } shouldBe (some(1) to none())
      some(1).partition { it != 1 } shouldBe (none() to some(1))
      none().partition { neverCalled() } shouldBe (none() to none())
    }

    "zip() should return a pair of values when both optionals contain values" {
      some(1).zip(some(2)) shouldBe some(1 to 2)
      some(1).zip(none()) shouldBe none()
      none().zip(some(1)) shouldBe none()
    }

    "zip() should call a transformation function when both optionals contain values" {
      some(2).zip(some(8)) { left, right -> right / left } shouldBe some(4)
      some(1).zip(none()) { left, right -> neverCalled() } shouldBe none()
      none().zip(some(1)) { left, right -> neverCalled() } shouldBe none()
    }

    "toCollection() should return its argument" {
      val collection = ArrayList<Nothing>()
      none().toCollection(collection) should be theSameInstanceAs collection
    }

    "toCollection() should add the optional value to the given collection" {
      some(1).toCollection(ArrayList()) shouldBe listOf(1)
      some(2).toCollection(ArrayList(listOf(1))) shouldBe listOf(1, 2)
      none().toCollection(ArrayList()) shouldBe listOf<Nothing>()
    }

    "toList() should return a list with the optional value" {
      some(1).toList() shouldBe listOf(1)
      none().toList() shouldBe listOf<Nothing>()
    }

    "toMutableList() should return a mutable list with the optional value" {
      some(1).toMutableList().apply { add(2) } shouldBe listOf(1, 2)
      none().toMutableList<Int>().apply { add(2) } shouldBe listOf(2)
    }

    "toSet() should return a set with the optional value" {
      some(1).toSet() shouldBe setOf(1)
      none().toSet() shouldBe setOf<Nothing>()
    }

    "toHashSet() should return a HashSet with the optional value" {
      some(1).toHashSet() shouldBe setOf(1)
      none().toHashSet() shouldBe setOf<Nothing>()
    }

    "toSortedSet() should return a SortedSet with the optional value" {
      some(1).toSortedSet() shouldBe setOf(1)
      none().toSortedSet() shouldBe setOf<Nothing>()
    }

    "asIterable() should return an Iterable with the optional value" {
      some(1).asIterable().toList() shouldBe listOf(1)
      none().asIterable().toList() shouldBe listOf<Nothing>()
    }

    "asSequence() should return a Sequence with the optional value" {
      some(1).asSequence().toList() shouldBe listOf(1)
      none().asSequence().toList() shouldBe listOf<Nothing>()
    }

    "equals() for Optional.Some should return true for optionals with the equal values" {
      val optional = some(1)
      optional.equals(optional) shouldBe true
      some(1).equals(some(1)) shouldBe true
      some(1).equals(some(2)) shouldBe false
      some(1).equals(null) shouldBe false
      some(1).equals(none()) shouldBe false
      some(1).equals("") shouldBe false
    }

    "hashCode() for Optional.Some should return its value's hashCode" {
      some(1).hashCode() shouldBe 1.hashCode()
    }

    "toOptional() should return Optional.Some for a non-null receiver" {
      1.toOptional() shouldBe some(1)
      1.toOptional() should be an Optional.Some::class
    }

    "toOptional() should return Optional.None for a null receiver" {
      null.toOptional() shouldBe none()
      null.toOptional() should be an Optional.None::class
    }
  }

  private fun neverCalled(): Nothing {
    fail("Should not be called")
  }
}
