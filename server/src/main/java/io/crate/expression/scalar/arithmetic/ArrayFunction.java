/*
 * Licensed to Crate.io GmbH ("Crate") under one or more contributor
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

package io.crate.expression.scalar.arithmetic;

import static io.crate.metadata.functions.TypeVariableConstraint.typeVariable;

import java.util.ArrayList;

import io.crate.data.Input;
import io.crate.expression.scalar.ScalarFunctionModule;
import io.crate.metadata.FunctionType;
import io.crate.metadata.NodeContext;
import io.crate.metadata.Scalar;
import io.crate.metadata.TransactionContext;
import io.crate.metadata.functions.BoundSignature;
import io.crate.metadata.functions.Signature;
import io.crate.types.TypeSignature;

public class ArrayFunction extends Scalar<Object, Object> {

    public static final String NAME = "_array";
    public static final Signature SIGNATURE =
        Signature.builder()
            .name(NAME)
            .kind(FunctionType.SCALAR)
            .typeVariableConstraints(typeVariable("E"))
            .argumentTypes(TypeSignature.parse("E"))
            .returnType(TypeSignature.parse("array(E)"))
            .setVariableArity(true)
            .feature(Feature.NON_NULLABLE)
            .build();


    public static void register(ScalarFunctionModule module) {
        module.register(
            SIGNATURE,
            ArrayFunction::new
        );
    }

    private ArrayFunction(Signature signature, BoundSignature boundSignature) {
        super(signature, boundSignature);
    }

    @SafeVarargs
    @Override
    public final Object evaluate(TransactionContext txnCtx, NodeContext nodeCtx, Input<Object>... args) {
        ArrayList<Object> values = new ArrayList<>(args.length);
        for (Input<Object> arg : args) {
            values.add(arg.value());
        }
        return values;
    }
}
