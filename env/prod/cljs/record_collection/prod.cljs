(ns record-collection.app
  (:require [record-collection.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
