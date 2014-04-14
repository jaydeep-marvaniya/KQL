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

package org.sindice.siren.similarity;

import org.apache.lucene.search.similarities.DefaultSimilarity;

/**
 * Implements the BM25 saturation function for the term frequency.
 */
public class SirenSimilarity extends DefaultSimilarity {

  private static final long serialVersionUID = 1L;

  private final float K1 = 1.2f;

  @Override
  public float tf(final float freq) {
    return freq * (K1 + 1) / (freq + K1);
  }

}
