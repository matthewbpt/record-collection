(ns record-collection.views.homepage
  (:require [re-frame.core :refer [dispatch
                                   subscribe]]))

(defn on-event-trigger [key]
  (fn [event]
    (dispatch [key (-> event .-target .-value)])))

(defn search [key]
  [:div.search>input {:placeholder "Enter Search"
                      :on-change   (on-event-trigger key)}])

(defn artist-row [artist]
  [:tr>td.col-md-2>a
   {:href (str "#/artist/" (:name artist))}
   (:name artist)])

(defn artists []
  (let [artists (subscribe [:filtered-artists])]
    (fn []
      [:div>div>table.table
       [:div>thead>th.col-md-2 "Artists"]
       [:tbody
        (map (fn [artist]
               ^{:key (str "artist-" (:id artist))}
               [artist-row artist]) @artists)]])))





