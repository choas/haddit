(ns haddit
  (:gen-class)
  (:require [clojure-hadoop.wrap :as wrap]
            [clojure-hadoop.defjob :as defjob])
  (:import  [java.io BufferedReader InputStreamReader]))

(defn mapper [key value]
  (let [{:keys [domain ups downs]} value]
    [[domain ups]]))

(defn reducer [key values-fn]
  (let [values  (values-fn)]
    [[key (reduce + values)]]))

(defjob/defjob   job
  :map           mapper
  :reduce        reducer
  :map-reader    wrap/clojure-map-reader
  :input-format :text
  :output-format :text
  :compress-output false)
