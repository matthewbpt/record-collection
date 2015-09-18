(ns record-collection.views.homepage
  (:require [re-frame.core :refer [dispatch
                                   dispatch-sync
                                   subscribe]]))


(defn artists-header []
  (fn []
    [:thead
     [:th.col-md-2 "Artists"]]))

(defn artist-row []
  (fn [artist]
    [:tr
     [:td.col-md-2 (:name artist)]]))

(defn artists []
  (let [artists (subscribe [:current-artists])]
    (fn []
      [:div
       [:div
        [:table.table
         [artists-header]
         [:tbody
          (map (fn [artist]
                 ^{:key (str "artist-" (:id artist))}
                 [artist-row artist]) @artists)
          ]]]]
      )))


