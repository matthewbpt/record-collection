(ns record-collection.views.homepage
  (:require [re-frame.core :refer [dispatch
                                   dispatch-sync
                                   subscribe]]))

(defn on-event-trigger [key]
  (fn [event]
    (dispatch [key (-> event .-target .-value)])))

(defn search [key]
  (fn []
    [:div {:class "search"}
     [:input {:placeholder "Enter Search"
              :on-change  (on-event-trigger key)}]]))

(defn artists-header []
  [:div [:thead
         [:th.col-md-2 "Artists"]]])

(defn artist-row []
  (fn [artist]
    [:tr {}
     [:td.col-md-2 [:a {:href (str "#/artist/" (:name artist))} (:name artist)]]]))

(defn artists []
  (let [artists (subscribe [:filtered-artists])]
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





