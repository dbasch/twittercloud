(defproject twittercloud "1.0.0-SNAPSHOT"
  :description "Create a word cloud from a twitter searchusing WordCram"
  :repositories {"local" ~(str (.toURI (java.io.File. "maven_repository")))}
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [clj-time "0.4.4"]
                 [wordcram "0.5.6"]
                 [cue "1.0.0"]
                 [jsoup "1.3.3"]
                 [processing "1.0.3"]
                 [twitter-api "0.7.4"]
                 [com.novemberain/monger "1.2.0"]
                 [org.clojure/data.json "0.1.3"]
                 [hiccup "1.0.1"]]
  :main twittercloud.search)
