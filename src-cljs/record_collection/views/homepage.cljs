(ns record-collection.views.homepage
  (:require [re-frame.core :refer [dispatch
                                   dispatch-sync
                                   subscribe]]))

(defn change-trigger [key]
  (fn [event]
    (dispatch [key (-> event .-target .-value)])))

(defn search [key]
  (fn []
    [:div {:class "search"}
     [:input {:placeholder "Enter Search"
              :on-change  (change-trigger key)}]]))

(defn artists-header []
  [:div [:thead
         [:th.col-md-2 "Artists"]]])

(defn artist-row []
  (fn [artist]
    [:tr {:on-click #(dispatch [:get-albums (:name artist)])}
     [:td.col-md-2 (:name artist)]]))

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

(defn album-row []
  (fn [album]
    [:tr
     [:td.col-md-2 (:name album)]]))

(defn albums []
  (fn [albums]
    [:div
     [:div
      [:table.table
       [:thead [:th.col-md-2 "Albums"]]
       [:tbody
        (map (fn [album]
               ^{:key (str "artist-" (:id album))}
               [album-row album]) albums)
        ]]]]))

(def thumbnail "data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/PjxzdmcgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB3aWR0aD0iMTkyIiBoZWlnaHQ9IjIwMCIgdmlld0JveD0iMCAwIDE5MiAyMDAiIHByZXNlcnZlQXNwZWN0UmF0aW89Im5vbmUiPjwhLS0KU291cmNlIFVSTDogaG9sZGVyLmpzLzEwMCV4MjAwCkNyZWF0ZWQgd2l0aCBIb2xkZXIuanMgMi42LjAuCkxlYXJuIG1vcmUgYXQgaHR0cDovL2hvbGRlcmpzLmNvbQooYykgMjAxMi0yMDE1IEl2YW4gTWFsb3BpbnNreSAtIGh0dHA6Ly9pbXNreS5jbwotLT48ZGVmcz48c3R5bGUgdHlwZT0idGV4dC9jc3MiPjwhW0NEQVRBWyNob2xkZXJfMTRmZTUwNTVkYTEgdGV4dCB7IGZpbGw6I0FBQUFBQTtmb250LXdlaWdodDpib2xkO2ZvbnQtZmFtaWx5OkFyaWFsLCBIZWx2ZXRpY2EsIE9wZW4gU2Fucywgc2Fucy1zZXJpZiwgbW9ub3NwYWNlO2ZvbnQtc2l6ZToxMHB0IH0gXV0+PC9zdHlsZT48L2RlZnM+PGcgaWQ9ImhvbGRlcl8xNGZlNTA1NWRhMSI+PHJlY3Qgd2lkdGg9IjE5MiIgaGVpZ2h0PSIyMDAiIGZpbGw9IiNFRUVFRUUiLz48Zz48dGV4dCB4PSI3MC4wNjI1IiB5PSIxMDQuNSI+MTkyeDIwMDwvdGV4dD48L2c+PC9nPjwvc3ZnPg==")

(defn artist-view []
  (let [artist {:name "Eric Clapton" :bio "Eric Patrick Clapton, CBE (born 30 March 1945),
  is an English rock and blues guitarist, singer and songwriter. He is the only three-time
  inductee to the Rock and Roll Hall of Fame: once as a solo artist and separately as a member
  of the Yardbirds and Cream. Clapton has been referred to as one of the most important and
  influential guitarists of all time.[1] Clapton ranked second in Rolling Stone magazine's list of
  the \"100 Greatest Guitarists of All Time\"[2] and fourth in Gibson's \"Top 50 Guitarists of All Time\".[3]
  He was also named number five in Time magazine's list of \"The 10 Best Electric Guitar Players\" in 2009"
                :albums [{:id 1 :name "461 Ocean Bloulevard"} {:id 2 :name "Slowhand"}]}]
    (fn []
      [:div.row
       [:div.media
        [:div.media-left
         [:img.col-md-3 {:src thumbnail :style {:height "200px" :width "200px" :display "block"}}]]
        [:div.media-body
         [:h3.media-heading (:name artist)]
         [:br]
         [:p (:bio artist)]
         ] [:br] [albums (:albums artist)]]
       ])))

