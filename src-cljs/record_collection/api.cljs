(ns record-collection.api
  (:require [record-collection.handlers]
            [record-collection.subs]
            [re-frame.core :refer [dispatch]]))

(defn init []
  (dispatch [:get-artists])
  (dispatch [:get-albums nil]))