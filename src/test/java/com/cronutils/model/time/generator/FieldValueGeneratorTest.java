package com.cronutils.model.time.generator;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cronutils.model.field.CronField;
import com.cronutils.model.field.CronFieldName;
import com.cronutils.model.field.constraint.FieldConstraints;
import com.cronutils.model.field.expression.FieldExpression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/*
 * Copyright 2015 jmrozanec
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class FieldValueGeneratorTest {
    private FieldValueGenerator fieldValueGenerator;

    @Before
    public void setUp() {
        fieldValueGenerator = new MockFieldValueGenerator(new CronField(CronFieldName.HOUR, mock(FieldExpression.class), mock(FieldConstraints.class)));
    }

    @Test
    public void testGenerateCandidates() throws Exception {
        int start = 1;
        int end = 2;
        assertTrue(fieldValueGenerator.generateCandidatesNotIncludingIntervalExtremes(start, end).isEmpty());
        List<Integer> candidates = fieldValueGenerator.generateCandidates(1, 2);
        assertFalse(candidates.isEmpty());
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(start));
        assertTrue(candidates.contains(end));
    }
}
