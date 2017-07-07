/*
 * Licensed to CRATE Technology GmbH ("Crate") under one or more contributor
 * license agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.  Crate licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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

package io.crate.metadata.information;

import com.google.common.collect.ImmutableList;
import io.crate.analyze.WhereClause;
import io.crate.metadata.ColumnIdent;
import io.crate.metadata.Reference;
import io.crate.metadata.Routing;
import io.crate.metadata.RowGranularity;
import io.crate.metadata.TableIdent;
import io.crate.metadata.table.StaticTableInfo;
import org.elasticsearch.cluster.service.ClusterService;

import javax.annotation.Nullable;
import java.util.Map;

public class InformationTableInfo extends StaticTableInfo {

    private final ClusterService clusterService;

    InformationTableInfo(ClusterService clusterService,
                         TableIdent ident,
                         ImmutableList<ColumnIdent> primaryKeyIdentList,
                         Map<ColumnIdent, Reference> references) {
        this(clusterService, ident, primaryKeyIdentList, references, null);
    }

    InformationTableInfo(ClusterService clusterService,
                         TableIdent ident,
                         ImmutableList<ColumnIdent> primaryKeyIdentList,
                         Map<ColumnIdent, Reference> references,
                         @Nullable ImmutableList<Reference> columns) {
        super(ident, references, columns, primaryKeyIdentList);
        this.clusterService = clusterService;
    }

    @Override
    public RowGranularity rowGranularity() {
        return RowGranularity.DOC;
    }

    @Override
    public Routing getRouting(WhereClause whereClause, @Nullable String preference) {
        return Routing.forTableOnSingleNode(ident(), clusterService.localNode().getId());
    }
}
