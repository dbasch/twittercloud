(ns twittercloud.search
  (:use [clojure.string :only (split trim lower-case)] twittercloud.config twittercloud.util [twittercloud.core :only (draw)]
        [twitter.oauth]
        [twitter.api.search])
  (:gen-class :main true))

(def creds (make-oauth-creds twitter-key
                             twitter-secret
                             oauth-access-token
                             oauth-access-token-secret))

(defn maxid[tweets]
  (dec (reduce min (map :id tweets))))

;; search twitter for results older than max-id, concatenate the text into a string
;; if max-id is -1, then we get the most recent tweets
(defn fetch-tweets[buf query total max-id]
  (let [results (search :oauth-creds creds
                        :params {:q query :count 100 :max_id max-id})]
    (let [tweets (:statuses (:body results))
          new-total (+ total (count tweets))]
      (println "Tweets fetched: " new-total)
      ;; have we collected at least 1500 tweets yet?
      (if (< new-total 1500)
        (let [newbuf (str (print-str (map :text tweets)) " " buf)]
          ;; keep searching for older tweets
          (recur newbuf query new-total (maxid tweets)))
        buf))))

(defn -main [& args]

  ;; filter out hashtags and the query word (it would dwarf everything else)
  (defn keeper[word] (and (wanted word) (not= (first word) \#) (not= word (first args))))

  (let [filename (str (first args) ".png")
        textbuffer (fetch-tweets " " (first args) 0 -1)
        wordlist (filter keeper (status-tokens (lower-case textbuffer)))
        words (apply str (interpose " " wordlist))]
    (draw words filename))
    (System/exit 0))
