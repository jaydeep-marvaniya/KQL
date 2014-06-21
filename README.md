
#KQL, Keyword Query Language.

#Overview

Semantic web has potential to serve both type of information need ,data retrieval and document retrieval  whereas traditional web setting was limited to document retrieval.This scenario calls for Keyword based schema agnostic data retrieval system which requires effective semantic query construction from keyword query.In this code we present SWIS, a Semantic Web Index Store designed to provide scalable  Keyword to Entity discovery service over dataset as large as entire web of data.Our system support full text Mapping,semistructured queries and Top-K mapping results  while exhibiting a concise and distributed index of semantically annotated resources over the web. 

#Technology Used
Java,
RDF,
Solr
Lucene


# SIREn: Open-Source Semi-Structured Information Retrieval Engine

## Overview

SIREn is a Lucene/Solr extension for efficient schemaless semi-structured full-text search.
SIREn is not a complete application by itself, but rather a code library and API
that can easily be used to create a full-featured semi-structured search engine.

Efficient, large scale handling of semi-structured data is an increasingly
important issue in information search scenarios on the web as well as in the enterprise..

While Lucene has long offered these capabilities, its native capabilities are
not intended for collections of schemaless semi-structured documents, e.g.,
collections where the schema varies across documents or collections with a
complex schema and a complex nested structure. For this reason we have developed SIREn, a
Lucene/Solr plugin to overcome these shortcomings and to efficiently index and
query complex JSON documents with arbitrary schema.

For its features, SIREn can be seen as being halfway between Solr (of which
it offers all the search features) and MongoDB (given it can index arbitrary
JSON documents).

