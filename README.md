# gauntlet

[![gauntlet](https://img.shields.io/maven-central/v/dev.marksman/gauntlet.svg)](http://search.maven.org/#search%7Cga%7C1%7Cdev.marksman.gauntlet)
[![CircleCI](https://circleci.com/gh/kschuetz/gauntlet.svg?style=svg)](https://circleci.com/gh/kschuetz/gauntlet)

WORK IN PROGRESS

## <a name="prop">`Prop`</a>

## <a name="arbitrary">`Arbitrary`</a>

### <a name="built-in-arbitraries">Built-in Arbitraries</a>

#### Primitives and simple types

|Type|Method|Description|
|---|---|---|
|`Integer`|`ints()`|any integer|              
|`Integer`|`ints(IntRange range)`|integers within a given range|              
|`Integer`|`intIndices(int bound)`|integers from 0..bound (exclusive), not affected by edge-case bias|              
|`Integer`|`intNaturals()`|integers >= 0|  
|`Long`|`longs()`|any long|              
|`Long`|`longs(LongRange range)`|longs within a given range|              
|`Long`|`longIndices(int bound)`|longs from 0..bound (exclusive), not affected by edge-case bias|              
|`Long`|`longNaturals()`|longs >= 0| 
|`Short`|`shorts()`|any short|              
|`Short`|`shorts(ShortRange range)`|shorts within a given range|              
|`Short`|`shortsNaturals()`|shorts >= 0|   
|`Byte`|`bytes()`|any byte|              
|`Byte`|`bytes(ByteRange range)`|bytes within a given range|          
|`Boolean`|`booleans()`|any boolean|          
|`Character`|`characters()`|any character|          
|`Character`|`characters(CharRange range)`|characters within a given range|          
|`Float`|`floats()`|any float|          
|`Float`|`floats(FloatRange range)`|floats within a given range|          
|`Float`|`floatFractionals()`|floats between 0 and 1 (exclusive)|          
|`Double`|`doubles()`|any double|          
|`Double`|`doubles(DoubleRange range)`|doubles within a given range|          
|`Double`|`doubleFractionals()`|doubles between 0 and 1 (exclusive)|            
|`Object`|`boxedPrimitives()`|`Integer`s, `Long`s, `Short`s, `Byte`s, `Double`s, `Float`s, `Boolean`s, or `Character`s|            
|`Byte[]`|`byteArrays()`|byte arrays|            
|`Byte[]`|`byteArrays(int size)`|byte arrays with a given size|            
|`String`|`strings()`|any string|            
|`String`|`stringsOfLength(int length)`|strings with a given length|            
|`String`|`stringsOfLength(IntRange lengthRange)`|strings with a length within a given range|            
|`BigInteger`|`bigIntegers()`|any `BigInteger`|            
|`BigInteger`|`bigIntegers(BigIntegerRange range)`|`BigInteger`s within a given range|            
|`BigDecimal`|`bigDecimals()`|any `BigDecimal`|            
|`BigDecimal`|`bigDecimals(BigDecimalRange range)`|`BigDecimal`s within a given range|            
|`BigDecimal`|`bigDecimals(int decimalPlaces, BigDecimalRange range)`|`BigDecimals` with a given number of decimal places and in a given range|            
|`BigDecimal`|`bigDecimals(IntRange decimalPlaces, BigDecimalRange range)`|`BigDecimals` with a given range of decimal places and in a given range|            
|`enum`|`valuesOfEnumClass(Class<A> enumType)`|any value from the supplied class|            
|`LocalDate`|`localDates()`|any `LocalDate`|            
|`LocalDate`|`localDates(LocalDateRange range)`|`LocalDate`s within a given range|            
|`LocalDate`|`localDatesForYear(Year year)`|`LocalDate`s within a given year|            
|`LocalDate`|`localDatesForMonth(YearMonth month)`|`LocalDate`s with in a given year and month|            
|`LocalTime`|`localTimes()`|any `LocalTime`|            
|`LocalTime`|`localTimes(LocalTimeRange range)`|`LocalTime`s within a given range|            
|`LocalDateTime`|`localDateTimes()`|any `LocalDateTime`|            
|`LocalDateTime`|`localDateTimes(LocalDateRange range)`|`LocalDateTime`s within a given range|            
|`LocalDateTime`|`localDateTimes(LocalDateTimeRange range)`|`LocalDateTime`s within a given range|            
|`Duration`|`durations()`|any `Duration`|            
|`Duration`|`durations(DurationRange range)`|`Duration`s in a given range|            
|`DayOfWeek`|`daysOfWeek()`|any day of the week|            
|`Month`|`months()`|any month of the year|            
|`UUID`|`uuid()`|any `UUID`|   

#### Collections

|Type|Method|Description|
|---|---|---|
|`Vector<?>`|`vectors()`|`Vector`s of any arbitrary type|
|`Vector<?>`|`vectors(int size)`|`Vector`s of any arbitrary type, with a given size|
|`Vector<?>`|`vectors(IntRange sizeRange)`|`Vector`s of any arbitrary type, with a given size range|
|`Vector<A>`|`vectorsOf(Arbitrary<A> elements)`|`Vector`s with elements of a given type|
|`Vector<A>`|`vectorsOf(int size, Arbitrary<A> elements)`|`Vector`s with elements of a given type, with a given size|
|`Vector<A>`|`vectorsOf(IntRange sizeRange, Arbitrary<A> elements)`|`Vector`s with elements of a given type, with a given size range|
|`NonEmptyVector<?>`|`nonEmptyVectors()`|`NonEmptyVector`s of any arbitrary type|
|`NonEmptyVector<?>`|`nonEmptyVectors(int size)`|`NonEmptyVector`s of any arbitrary type, with a given size|
|`NonEmptyVector<?>`|`nonEmptyVectors(IntRange sizeRange)`|`NonEmptyVector`s of any arbitrary type, with a given size range|
|`NonEmptyVector<A>`|`nonEmptyVectorsOf(Arbitrary<A> elements)`|`NonEmptyVector`s with elements of a given type|
|`NonEmptyVector<A>`|`nonEmptyVectorsOf(int size, Arbitrary<A> elements)`|`NonEmptyVector`s with elements of a given type, with a given size|
|`NonEmptyVector<A>`|`nonEmptyVectors(IntRange sizeRange, Arbitrary<A> elements)`|`NonEmptyVector`s with elements of a given type, with a given size range|         
|`ArrayList<?>`|`arrayLists()`|`ArrayList`s of any arbitrary type|         
|`ArrayList<?>`|`arrayLists(int size)`|`ArrayList`s of any arbitrary type, with a given size|         
|`ArrayList<?>`|`arrayLists(IntRange sizeRange)`|`ArrayList`s of any arbitrary type, with a given size range|  
|`ArrayList<A>`|`arrayListsOf(Arbitrary<A> elements)`|`ArrayList`s with elements of a given type|         
|`ArrayList<A>`|`arrayListsOf(int size, Arbitrary<A> elements)`|`ArrayList`s with elements of a given type, with a given size|         
|`ArrayList<A>`|`arrayListsOf(IntRange sizeRange, Arbitrary<A> elements)`|`ArrayList`s with elements of a given type, with a given size range|        
|`ArrayList<A>`|`nonEmptyArrayListsOf(Arbitrary<A> elements)`|`ArrayList`s with elements of a given type, and at least one element|        
|`HashSet<A>`|`hashSetsOf(Arbitrary<A> elements)`|`HashSet`s with elements of a given type|        
|`HashSet<A>`|`nonEmptyHashSetsOf(Arbitrary<A> elements)`|`HashSet`s with elements of a given type, and at least one element|        
|`HashMap<?, ?>`|`hashMaps()`|`HashMap`s of any arbitrary key type and any arbitrary value type|        
|`HashMap<K, V>`|`hashMaps(Arbitrary<K> keys, Arbitrary<V> values)`|`HashMap`s of a given key type and a given value type|        
|`HashMap<K, V>`|`nonEmptyHashMapsOf(Arbitrary<K> keys, Arbitrary<V> values)`|`HashMap`s of a given key type and a given value type, with at least one entry|      

#### Products

|Type|Method|Description|
|---|---|---|
|`Tuple2`..`Tuple8`|`tuple2s()`..`tuples8s()`|`Tuple`s with arbitrary component types|
|`Tuple2`..`Tuple8`|`tuplesOf(Arbitrary<A> a, Arbitrary<B>, `...`)`|`Tuple`s with component types of the provided `Arbitrary`s|

#### Coproducts   

|Type|Method|Description|
|---|---|---|
|`Choice2`..`Choice8`|`choicesOf(Arbitrary<A> a, Arbitrary<B>, `...`)`|values selected and drawn from provided arbitraries, with equal probability for each|
|`Choice2`..`Choice8`|`choicesOf(Weighted<Arbitrary<A>> a, Weighted<Arbitrary<B>>, `...`)`|values selected and drawn from provided arbitraries, with custom probabilities|
|`Maybe<A>`|`maybesOf(Arbitrary<A> a)`|`Maybe<A>` that is most frequently a `just`, with an occasional `nothing` thrown in| 
|`Maybe<A>`|`maybesOf(MaybeWeights weights, Arbitrary<A> a)`|`Maybe<A>` with custom probability of generating a `nothing` |
|`Either<L, R>`|`eithersOf(Arbitrary<L> left, Arbitrary<R> right)`|equal probability of a `left` or a `right`|
|`Either<L, R>`|`eithersOf(Weighted<Arbitrary<L>> left, Weighted<Arbitrary<R>> right)`|`Either<L, R>` with custom probabilities of selecting a `left` or a `right`|
|`Either<L, R>`|`eithersOf(EitherWeights weights, Arbitrary<L> left, Arbitrary<R> right)`|`Either<L, R>` with custom probabilities of selecting a `left` or a `right`|
|`These<A, B>`|`theseOf(Arbitrary<A> a, Arbitrary<B> b)`|equal probability of selecting an `a`, a `b`, or a `both`|

# <a name="license">License</a>

*gauntlet* is distributed under [The MIT License](http://choosealicense.com/licenses/mit/).

The MIT License (MIT)

Copyright (c) 2020 Kevin Schuetz

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
