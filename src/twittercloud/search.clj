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

(defn fetch-tweets[query total max-id]
  (println total)
  (let [results ; no max id the first time, after that we go further down into the timeline
        (search :oauth-creds creds
                :params {:q query :count 100 :max_id max-id})]
    (let [tweets (:statuses (:body results))]
      (if (< (+ total (count tweets)) 1000)
        (str (print-str (map :text tweets)) " " (fetch-tweets query (+ total (count tweets)) (maxid tweets)))))))

(defn -main [& args]
                                        ;don't show hashtags or the query word (it would be huge)
  (defn keeper[word] (and (wanted word) (not= (first word) \#) (not= word (first args))))
  (draw (apply str (interpose " " (filter keeper (status-tokens (lower-case (fetch-tweets (first args) 0 -1)))))) (str (first args) ".png"))
  (System/exit 0))
