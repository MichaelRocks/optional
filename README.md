Optional
========

An implementation of `Optional` for Kotlin.
 
`Optional` is a container that may or may not contain a single non-nullable value. It can be used instead of `null`
references when `null` values are prohibited, like in RxJava 2.

Download
--------
Gradle:
```groovy
repositories {
  jcenter()
}

dependencies {
  compile 'io.michaelrocks:optional:1.0.1'
}
```

Usage
-----
You can create an `Optional` in several ways.

1. By accessing `Optional` subclasses directly.

```kotlin
val optional1 = Optional.Some("Value")
val optional2 = Optional.None
``` 

2. By calling convenience functions.

```kotlin
val optional1 = some("Value")
val optional2 = none()
```

3. By invoking `toOptional()` extension function on some value.

```kotlin
val optional1 = "Value".toOptional()
val optional2 = null.toOptional()
```

To get a value back from an `Optional` you can invoke one of `get()` functions on the `Optional` reference.

```
val optional1 = some("Value")
val optional2 = none()

optional1.get() // returns "Value"
optional2.get() // throws NoSuchElementException

optional1.getOrNull() // returns "Value"
optional2.getOrNull() // returns null

optional1.getOrElse { "Other value" } // returns "Value"
optional2.getOrElse { "Other value" } // returns "Other value"
```

There're a number of functions to work with an `Optional` as with a `Collection`, such as `filter`, `map`, `flatMap`
and many others. Moreover, you can convert an `Optional` to a `Collection`, an `Iterable`, or a `Sequence`. 

License
=======
    Copyright 2016 Michael Rozumyanskiy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
