(ns twittercloud.util
  ;; Utilities for parsing text
  (:gen-class :main true)
  (:require clojure.string))

(def stopwords (set (clojure.string/split (slurp "stopwords.txt") #"\n")))

(def urls (partial re-seq #"http://\S+"))

;; twitter-style @mentions
(def mentions (partial re-seq #"@\S+"))

(defn remove-urls[text]
  (reduce #(.replace %1 %2 "") text (urls text)))

(defn remove-mentions[text]
  (reduce #(.replace %1 %2 "") text (mentions text)))

(defn tokenize[text]
  (clojure.string/split text #"[\"\s#&!:,;\.\\\+-]+"))

(defn status-tokens[text] ((comp tokenize remove-urls remove-mentions) text))

;; heuristic for deciding which words to keep from a twitter-style corpus
(defn interesting[word]
  (and (not= (first word) \@)
       (not= (first word) \#)
       (= nil (stopwords word))
       (> (.length word) 2)))
