(ns twittercloud.search
  (:use [clojure.string :only (split trim lower-case)])
  (:require twitter.api.search twitter.oauth twittercloud.core
            [twittercloud.config :as conf]
            [twittercloud.util :as util])
   
  (:gen-class :main true))

;; twitter credentials
(def creds (twitter.oauth/make-oauth-creds
            conf/twitter-key
            conf/twitter-secret
            conf/oauth-access-token
            conf/oauth-access-token-secret))

(defn oldest-id [tweets]
  (dec (reduce min (map :id tweets))))

;; search twitter for results older than max-id, concatenate the text into a string
;; if max-id is -1, then we get the most recent tweets
(defn fetch-tweets [buf query total max-id]
  (let [results (twitter.api.search/search
                 :oauth-creds creds
                 :params {:q query :count 100 :max_id max-id})
        tweets (:statuses (:body results))
        new-total (+ total (count tweets))
        new-buf (str (print-str (map :text tweets)) " " buf)]
    (println "tweets fetched: " new-total)

    ;; keep searching for older tweets until we have at least 1500
    (if (< new-total 1500)
      (recur new-buf query new-total (oldest-id tweets))
      new-buf)))

;; keep "interesting" words, remove the query word (it would dwarf everything else)
(defn keeper[word query-word] (and (util/interesting word) (not= word query-word)))

;; draw a word cloud for the most recent tweets containing a given word.
(defn -main [& args]
  (let [filename (str (first args) ".png")
        query-word (first args)
        textbuffer (fetch-tweets " " query-word 0 -1)
        wordlist (filter #(keeper % query-word) (util/status-tokens (lower-case textbuffer)))
        words (apply str (interpose " " wordlist))]
    (twittercloud.core/draw words filename))
    (System/exit 0))
