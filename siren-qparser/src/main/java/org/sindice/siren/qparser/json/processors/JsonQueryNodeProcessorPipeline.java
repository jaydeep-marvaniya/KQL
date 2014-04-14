/**
 * Copyright 2014 National University of Ireland, Galway.
 *
 * This file is part of the SIREn project. Project and contact information:
 *
 *  https://github.com/rdelbru/SIREn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sindice.siren.qparser.json.processors;

import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.search.Query;
import org.sindice.siren.qparser.json.builders.JsonQueryTreeBuilder;
import org.sindice.siren.qparser.json.config.JsonQueryConfigHandler;
import org.sindice.siren.qparser.json.parser.JsonSyntaxParser;

/**
 * This pipeline has all the processors needed to process a query node tree,
 * generated by {@link JsonSyntaxParser}, already assembled.
 *
 * <p>
 *
 * The order they are assembled affects the results.
 *
 * <p>
 *
 * This processor pipeline was designed to work with
 * {@link JsonQueryConfigHandler}.
 *
 * <p>
 *
 * The result query node tree can be used to build a {@link Query} object using
 * {@link JsonQueryTreeBuilder}.
 *
 * @see JsonQueryTreeBuilder
 * @see JsonQueryConfigHandler
 * @see JsonSyntaxParser
 */
public class JsonQueryNodeProcessorPipeline extends QueryNodeProcessorPipeline {

  public JsonQueryNodeProcessorPipeline() {}

  /**
   * @param queryConfigHandler
   */
  public JsonQueryNodeProcessorPipeline(final JsonQueryConfigHandler queryConfigHandler) {
    super(queryConfigHandler);
  }

}
