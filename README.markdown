haddit
======

For a more detailed article see [Hadoop - Feeding Reddit to Hadoop](http://www.bestinclass.dk/index.clj/2010/01/hadoop-feeding-reddit-to-hadoop.html).

This version has following modifications:

- clojure-contrib 1.1.0 json support

- clojure-hadoop 1.2.0 defjob (input-format)

- added more job definitions: output-format and compress-output


building and running
--------------------

To build the uberjar with leiningen, just type:

    lein uberjar

To execute haddit with some example data:

    ./run.sh

There're already some example data in the data folder. But I think they has duplicated entries and the reddit data download needs some modification ...
