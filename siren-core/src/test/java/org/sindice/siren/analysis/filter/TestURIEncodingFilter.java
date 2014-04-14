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

package org.sindice.siren.analysis.filter;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Random;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.LuceneTestCase;
import org.junit.Test;
import org.sindice.siren.analysis.TupleTokenizer;

public class TestURIEncodingFilter extends LuceneTestCase {

  private final String uritype = TupleTokenizer.getTokenTypes()[TupleTokenizer.URI];

  private final Tokenizer _t = new TupleTokenizer(new StringReader(""));

  /**
   * Check if an URI with no URL encoded characters is left as it is
   * @throws Exception
   */
  @Test
  public void testNoURLEncodedCharacters()
  throws Exception {
    this.assertURLDecodedTo(_t, "<http://stephane.net>", new String[] { "http://stephane.net" });
  }

  /**
   * Check if special characters are correctly decoded and if the filters produces the two stems of the URI
   * @throws Exception
   */
  @Test
  public void testSpecialcharacters()
  throws Exception {
    this.assertURLDecodedTo(_t, "<http://stephane.net/%32%21Space%21space>",
      new String[] { "http://stephane.net/%32%21Space%21space", "http://stephane.net/2!Space!space" });
    this.assertURLDecodedTo(_t, "<http://stephane.net/%57%68%4F%61%72%65%79%6f%75%3F>",
      new String[] { "http://stephane.net/%57%68%4F%61%72%65%79%6f%75%3F", "http://stephane.net/WhOareyou?" });
    // We does not decode space
    this.assertURLDecodedTo(_t, "<http://stephane.net/%57%68%4F+%61%72%65+%79%6f%75%20%3F>",
      new String[] { "http://stephane.net/%57%68%4F+%61%72%65+%79%6f%75%20%3F", "http://stephane.net/WhO+are+you+?" });
  }

  /**
   * Check if the boundaries of the internal buffers are correct.
   * @throws Exception
   */
  @Test
  public void testLongURLEncodedChain()
  throws Exception {
    this.assertURLDecodedTo(_t, "<deus%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40>",
      new String[] { "deus%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40",
                     "deus@@@@@@@@@@@@@@@@@@@@" });
    this.assertURLDecodedTo(_t, "<deus%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40+%3f>",
      new String[] { "deus%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40+%3f",
                     "deus@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@+?" });
    final String looong = "%20%21%22%23%24%25%26%27%28%29%2a%2b%2c%2d%2e%2f%30" +
    		"%31%32%33%34%35%36%37%38%39%3a%3b%3c%3d%3e%3f%40%41%42%43%44%45%46%47" +
    		"%48%49%4a%4b%4c%4d%4e%4f%50%51%52%53%54%55%56%57%58%59%5a%5b%5c%5d%5e" +
    		"%5f%60%61%62%63%64%65%66%67%68%69%6a%6b%6c%6d%6e%6f%70%71%72%73%74%75" +
    		"%76%77%78%79%7a%7b%7c%7d%7e";
    final String decLooong = "+!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOP" +
    		"QRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    this.assertURLDecodedTo(_t, "<" + looong + looong + looong + looong + ">",
      new String[]{ looong + looong + looong + looong,
                    decLooong + decLooong + decLooong + decLooong });
    this.assertURLDecodedTo(_t, "<" + looong + looong + looong + looong + looong + looong + looong + looong + ">",
      new String[]{ looong + looong + looong + looong + looong + looong + looong + looong,
                    decLooong + decLooong + decLooong + decLooong + decLooong + decLooong + decLooong + decLooong });
  }

  /**
   * Check that badly encoded URL characters are left as they are. The decoding continue
   * nonetheless on the rest of the URI
   * @throws Exception
   */
  @Test
  public void testWronglyEncodedCharacters()
  throws Exception {
    this.assertURLDecodedTo(_t, "<http://stephane.net/%>",
      new String[] { "http://stephane.net/%", "http://stephane.net/%" });
    this.assertURLDecodedTo(_t, "<http://stephane.net/%8>",
      new String[] { "http://stephane.net/%8", "http://stephane.net/%8" });
    this.assertURLDecodedTo(_t, "<http://stephane.net/%%3f>",
      new String[] { "http://stephane.net/%%3f", "http://stephane.net/%%3f" });
    this.assertURLDecodedTo(_t, "<http://stephane.net/%GGporco>",
      new String[] { "http://stephane.net/%GGporco", "http://stephane.net/%GGporco" });
    this.assertURLDecodedTo(_t, "<http://stephane.net/%G3porco%2erosso>",
      new String[] { "http://stephane.net/%G3porco%2erosso", "http://stephane.net/%G3porco.rosso" });
  }

  /**
   * Test bad Charset name
   * @throws Exception
   */
  @Test(expected=UnsupportedCharsetException.class)
  public void testUnsupportedCharset()
  throws Exception {
    this.assertURLDecodedTo(_t, "FTU_8", "", new String[] {});
  }

  /**
   * Test where {@link URIDecodingFilter#hexaToInt} return a negative value
   * @throws Exception
   */
  @Test
  public void testBadHexadecimalNumber()
  throws Exception {
    this.assertURLDecodedTo(_t, "<http://stephane%3f%FGnet/>", new String[] { "http://stephane%3f%FGnet/", "http://stephane?%FGnet/" });
  }

  /**
   * Test a sequence of tokens with different types.
   * @throws Exception
   */
  @Test
  public void testDifferentTypes()
  throws Exception {
    this.assertURLDecodedTo(_t, "<stephane%3Fnet/> \"A literal !!!!\" <porco%2erosso>",
      new String[] { "stephane%3Fnet/", "stephane?net/", "A literal !!!!", "porco%2erosso", "porco.rosso" },
      new String[] { uritype, uritype, TupleTokenizer.getTokenTypes()[TupleTokenizer.LITERAL], uritype, uritype },
      new int[] { 1, 0, 1, 1, 0 });
  }

  @Test
  public void testSpaces()
  throws Exception {
    this.assertURLDecodedTo(_t, "<http://s+t+e%20%20p+h%20+%20ane/>", new String[] { "http://s+t+e%20%20p+h%20+%20ane/",
                                                                                     "http://s+t+e++p+h+++ane/" });
  }

  @Test
  public void testBufferOverflow()
  throws Exception {
    final StringBuilder sb = new StringBuilder();
    final Random r = LuceneTestCase.random();

    for (int i = 0; i < 300; i++) {
      sb.append((char) 65 + r.nextInt(26));
    }
    this.assertURLDecodedTo(_t, "<" + sb.toString() + ">", new String[] { sb.toString() });
  }

  /*
   * Helpers
   */

  private void assertURLDecodedTo(final Tokenizer t, final String uri, final String[] expectedStems)
  throws IOException {
    this.assertURLDecodedTo(t, "UTF-8", uri, expectedStems, null, null);
  }

  private void assertURLDecodedTo(final Tokenizer t, final String encoding, final String uri, final String[] expectedStems)
  throws IOException {
    this.assertURLDecodedTo(t, encoding, uri, expectedStems, null, null);
  }

  private void assertURLDecodedTo(final Tokenizer t, final String uri, final String[] expectedStems, final String[] expectedTypes, final int[] expectedPosIncr)
  throws IOException {
    this.assertURLDecodedTo(t, "UTF-8", uri, expectedStems, expectedTypes, expectedPosIncr);
  }

  private void assertURLDecodedTo(final Tokenizer t, final String encoding, final String uri, final String[] expectedStems, final String[] expectedTypes, final int[] expectedPosIncr)
  throws IOException {
    assertTrue("has CharTermAttribute", t.hasAttribute(CharTermAttribute.class));
    final CharTermAttribute termAtt = t.getAttribute(CharTermAttribute.class);

    assertTrue("has TypeAttribute", t.hasAttribute(TypeAttribute.class));
    final TypeAttribute typeAtt = t.getAttribute(TypeAttribute.class);

    assertTrue("has PositionIncrementAttribute", t.hasAttribute(PositionIncrementAttribute.class));
    final PositionIncrementAttribute posIncrAtt = t.getAttribute(PositionIncrementAttribute.class);

    t.setReader(new StringReader(uri));
    t.reset();

    final URIDecodingFilter filter = new URIDecodingFilter(t, encoding);
    for (int i = 0; i < expectedStems.length; i++) {
        assertTrue("token " + i + " exists", filter.incrementToken());
        assertEquals(expectedStems[i], termAtt.toString());
        if (expectedTypes == null)
          assertEquals(uritype, typeAtt.type());
        else
          assertEquals(expectedTypes[i], typeAtt.type());
        if (expectedPosIncr != null)
          assertEquals(expectedPosIncr[i], posIncrAtt.getPositionIncrement());
    }
    filter.end();
    filter.close();
  }

}
