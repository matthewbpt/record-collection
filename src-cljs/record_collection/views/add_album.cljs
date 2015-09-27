(ns record-collection.views.add-album
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent-forms.core :refer [bind-fields]]
            [reagent.core :refer [atom]]))

(def thumbnail "data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/PjxzdmcgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB3aWR0aD0iMTkyIiBoZWlnaHQ9IjIwMCIgdmlld0JveD0iMCAwIDE5MiAyMDAiIHByZXNlcnZlQXNwZWN0UmF0aW89Im5vbmUiPjwhLS0KU291cmNlIFVSTDogaG9sZGVyLmpzLzEwMCV4MjAwCkNyZWF0ZWQgd2l0aCBIb2xkZXIuanMgMi42LjAuCkxlYXJuIG1vcmUgYXQgaHR0cDovL2hvbGRlcmpzLmNvbQooYykgMjAxMi0yMDE1IEl2YW4gTWFsb3BpbnNreSAtIGh0dHA6Ly9pbXNreS5jbwotLT48ZGVmcz48c3R5bGUgdHlwZT0idGV4dC9jc3MiPjwhW0NEQVRBWyNob2xkZXJfMTRmZTUwNTVkYTEgdGV4dCB7IGZpbGw6I0FBQUFBQTtmb250LXdlaWdodDpib2xkO2ZvbnQtZmFtaWx5OkFyaWFsLCBIZWx2ZXRpY2EsIE9wZW4gU2Fucywgc2Fucy1zZXJpZiwgbW9ub3NwYWNlO2ZvbnQtc2l6ZToxMHB0IH0gXV0+PC9zdHlsZT48L2RlZnM+PGcgaWQ9ImhvbGRlcl8xNGZlNTA1NWRhMSI+PHJlY3Qgd2lkdGg9IjE5MiIgaGVpZ2h0PSIyMDAiIGZpbGw9IiNFRUVFRUUiLz48Zz48dGV4dCB4PSI3MC4wNjI1IiB5PSIxMDQuNSI+MTkyeDIwMDwvdGV4dD48L2c+PC9nPjwvc3ZnPg==")

(defn artist-select-option
  [artist]
  [:option {:key (:id artist)} (:name artist)])

(defn form-template [artists fileatom]    
    [:div.row
     [:div.col-md-3.col-sm-4
      [:input {:value nil
               :type "file"
               :name "Album Cover"
               :on-change #(let [file (-> % .-target .-files (aget 0))]
                             (.log js/console file)
                             (swap! fileatom assoc :file file))}
       [:img
               {:src thumbnail :style {:height "200px" :width "200px" :display "block"}}]]]
     [:div.col-md-9.col-sm-8
      [:h3 "Add Album"]
        [:form
         [:div.form-group
          [:label "Title"]
          [:input.form-control {:field :text :id :title}]]
         [:div.form-group
          [:label "Year"]
          [:input.form-control {:field :numeric :id :year}]]
         [:div.form-group
          [:label "Artist"]
          (vec (concat [:select.form-control {:field :list :id :artist}]
                       (map artist-select-option artists)))]]]])

(defn add-album-form []
  (let [artists (subscribe [:current-artists])
        album (atom {:id 0, :title "", :year 2015, :artist 0})
        image-file (atom {:file ""})]
    (fn add-album-form-renderer
      []
      [:div
       [bind-fields (form-template @artists image-file) album]
       [:button.btn.btn-primary.pull-right
        {:on-click #(dispatch [:add-album @album])}
        "Add Album"]])))