(use 'clojure.contrib.json
     'clojure.contrib.duck-streams)

(defn download-url [url]
  (let [s (.openStream (java.net.URL. url))]
    (apply str
           (map #(char %) (take-while pos? (repeatedly #(.read s)))))))

(defn parse-page [url]
  (let [page (read-json (download-url url))
        ks   [:id :title :domain :url :title :author :ups :downs :subreddit :num_comments]
        vmap (vec (for [child ((page :data) :children)]
                    (zipmap ks (map #((child :data) %) ks))))]
    (if (nil? ((page :data) :after))
      [vmap :done] vmap)))

(defn emit-results [outfile data]
  ((if (.isFile (java.io.File. outfile)) append-spit spit)
   outfile (with-out-str (->> (butlast data)
                              (map #(doall (map prn %)))
                              doall))))

(defn scrape-channel [max target channel]
  (let [base   (format "http://reddit.com/r/%s/" channel)
        page   (fn [data idx]
                 (str base ".json?count=" idx "&after=t3_"
                      (-> data peek peek :id)))
        scrape (reduce (fn [data idx]
                         (if (= :done (-> data peek peek))
                           data
                           (conj data (parse-page (page data idx)))))
                       [(parse-page (str base ".json"))]
                       (take max (iterate #(+ 25 %) 0)))]
    (emit-results target scrape)))

(defn getdata []
  (doseq [channel0 ["Clojure" "programming"]
	  channel ["Clojure"]]
    (scrape-channel 100 "data/dataset.txt" channel)))
