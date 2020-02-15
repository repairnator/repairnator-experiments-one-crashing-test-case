/*
 * MIT License
 *
 * Copyright 2018 Sabre GLBL Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.sabre.oss.yare.common.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StringTypeConverterTest {
    private StringTypeConverter typeConverter;

    @BeforeEach
    void setUp() {
        typeConverter = new StringTypeConverter();
    }

    @ParameterizedTest
    @MethodSource("applicableParameters")
    void shouldBeApplicableWhenString() {
        boolean isApplicable = typeConverter.isApplicable(String.class);

        assertThat(isApplicable).isTrue();
    }

    private static Stream<Arguments> applicableParameters() {
        return Stream.of(
                Arguments.of(String.class, true),
                Arguments.of(Integer.class, false),
                Arguments.of(null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("conversionParameters")
    void shouldProperlyConvertFromString(String toConvert, String expected) {
        String converted = typeConverter.fromString(String.class, toConvert);

        assertThat(converted).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("conversionParameters")
    void shouldProperlyConvertToString(String expected, String toConvert) {
        String converted = typeConverter.toString(String.class, toConvert);

        assertThat(converted).isEqualTo(expected);
    }

    private static Stream<Arguments> conversionParameters() {
        return Stream.of(
                Arguments.of("test", "test"),
                Arguments.of("@null", null),
                Arguments.of("\\@null", "@null"),
                Arguments.of("\\\\@null", "\\\\@null")
        );
    }
}
