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
package org.sindice.siren.qparser.json.parser;

import java.util.Iterator;

import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode.Modifier;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.codehaus.jackson.JsonNode;
import org.sindice.siren.qparser.json.ParseException;
import org.sindice.siren.qparser.json.nodes.BooleanQueryNode;

/**
 * Parses a <code>boolean</code> property and returns a {@link BooleanQueryNode}.
 */
public class BooleanPropertyParser extends JsonPropertyParser {

  public static final String BOOLEAN_PROPERTY = "boolean";

  public BooleanPropertyParser(final JsonNode node, final CharSequence field) {
    super(node, field);
  }

  @Override
  String getProperty() {
    return BOOLEAN_PROPERTY;
  }

  @Override
  BooleanQueryNode parse() throws ParseException {
    final JsonNode value = node.path(BOOLEAN_PROPERTY);
    if (!value.isArray()) {
      throw new ParseException("Invalid property '" + BOOLEAN_PROPERTY + "': value is not an array");
    }

    final BooleanQueryNode booleanNode = new BooleanQueryNode();

    final Iterator<JsonNode> elements = value.getElements();
    while (elements.hasNext()) {
      final JsonNode element = elements.next();

      // parse occur
      final OccurPropertyParser occurParser = new OccurPropertyParser(element, field);
      Modifier mod = null;
      if (occurParser.isPropertyDefined()) {
        mod = occurParser.parse();
      }

      // check if there is either a node or a twig property and parse it
      QueryNode queryNode = null;
      if (element.has(NodePropertyParser.NODE_PROPERTY)) {
        final NodePropertyParser nodeParser = new NodePropertyParser(element, field);
        queryNode = nodeParser.parse();
      }
      if (element.has(TwigPropertyParser.TWIG_PROPERTY)) {
        final TwigPropertyParser twigParser = new TwigPropertyParser(element, field);
        queryNode = twigParser.parse();
      }

      // check if either a node or twig property has been defined
      if (queryNode == null) {
        throw new ParseException("Invalid property '" + BOOLEAN_PROPERTY + "': object does not define a twig or node query");
      }

      // wrap the query node with a modifier, and add it to the boolean query
      booleanNode.add(new ModifierQueryNode(queryNode, mod));
    }

    return booleanNode;
  }

}
