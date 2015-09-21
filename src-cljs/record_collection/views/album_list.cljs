(ns record-collection.views.album-list
  (:require [re-frame.core :refer [dispatch
                                   dispatch-sync
                                   subscribe]]))

(defn album-row [album]
  (fn album-row-renderer [album]
    [:tr
     [:td.col-md-2 (:title album)]
     [:td.col-md-2 (:year album)]]))

(defn albums [artist]
  (fn albums-renderer [artist]
    (let [albums (subscribe [:albums artist])]
      [:div>div>table.table
       [:thead>th.col-md-2 "Albums"]
       [:tbody
        (map (fn [album]
               ^{:key (str "album-" (:id album))}
               [album-row album]) @albums)]])))
