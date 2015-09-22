(ns record-collection.api
  (:require [record-collection.handlers]
            [record-collection.subs]
            [re-frame.core :refer [dispatch-sync]]))

(defn init []
  (dispatch-sync [:initdb])
  (dispatch-sync [:get-artists])
  (dispatch-sync [:get-albums nil]))