/*
 * Licensed to Crate.io GmbH ("Crate") under one or more contributor
 * license agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.  Crate licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial agreement.
 */

package io.crate.analyze.where;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import io.crate.metadata.ColumnIdent;

public class ColumnsUnderNotPredicateFinderTest extends EqualityExtractorBaseTest {

    @Test
    public void test_can_find_x_from_x_neq_1() {
        var query = query("x != 1 or x = 1");
        var columns = List.of(new ColumnIdent("x"));
        assertThat(new EqualityExtractor.ColumnsUnderNotPredicateFinder().find(query, columns)).isTrue();
    }

    @Test
    public void test_can_find_x_from_not_x_neq_1() {
        var query = query("not(x != 1) or x = 1");
        var columns = List.of(new ColumnIdent("x"));
        assertThat(new EqualityExtractor.ColumnsUnderNotPredicateFinder().find(query, columns)).isTrue();
    }

    @Test
    public void test_x_is_not_under_not() {
        var query = query("i != 1 or x = 1");
        var columns = List.of(new ColumnIdent("x"));
        assertThat(new EqualityExtractor.ColumnsUnderNotPredicateFinder().find(query, columns)).isFalse();
    }

    @Test
    public void test_can_find_x_under_not_over_larger_query() {
        var query = query("not(i != 1 or x = 1)");
        var columns = List.of(new ColumnIdent("x"));
        assertThat(new EqualityExtractor.ColumnsUnderNotPredicateFinder().find(query, columns)).isTrue();
    }

    @Test
    public void test_x_is_under_not_but_is_not_interested_column() {
        var query = query("not(i != 1 or x = 1)");
        assertThat(new EqualityExtractor.ColumnsUnderNotPredicateFinder().find(query, List.of())).isFalse();
    }
}
