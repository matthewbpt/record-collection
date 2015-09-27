(ns record-collection.views.add-artist
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent-forms.core :refer [bind-fields]]
            [reagent.core :refer [atom]]))
(def thumbnail "data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/PjxzdmcgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB3aWR0aD0iMTkyIiBoZWlnaHQ9IjIwMCIgdmlld0JveD0iMCAwIDE5MiAyMDAiIHByZXNlcnZlQXNwZWN0UmF0aW89Im5vbmUiPjwhLS0KU291cmNlIFVSTDogaG9sZGVyLmpzLzEwMCV4MjAwCkNyZWF0ZWQgd2l0aCBIb2xkZXIuanMgMi42LjAuCkxlYXJuIG1vcmUgYXQgaHR0cDovL2hvbGRlcmpzLmNvbQooYykgMjAxMi0yMDE1IEl2YW4gTWFsb3BpbnNreSAtIGh0dHA6Ly9pbXNreS5jbwotLT48ZGVmcz48c3R5bGUgdHlwZT0idGV4dC9jc3MiPjwhW0NEQVRBWyNob2xkZXJfMTRmZTUwNTVkYTEgdGV4dCB7IGZpbGw6I0FBQUFBQTtmb250LXdlaWdodDpib2xkO2ZvbnQtZmFtaWx5OkFyaWFsLCBIZWx2ZXRpY2EsIE9wZW4gU2Fucywgc2Fucy1zZXJpZiwgbW9ub3NwYWNlO2ZvbnQtc2l6ZToxMHB0IH0gXV0+PC9zdHlsZT48L2RlZnM+PGcgaWQ9ImhvbGRlcl8xNGZlNTA1NWRhMSI+PHJlY3Qgd2lkdGg9IjE5MiIgaGVpZ2h0PSIyMDAiIGZpbGw9IiNFRUVFRUUiLz48Zz48dGV4dCB4PSI3MC4wNjI1IiB5PSIxMDQuNSI+MTkyeDIwMDwvdGV4dD48L2c+PC9nPjwvc3ZnPg==")

(def form-template
  [:div.row
   [:div.col-md-3.col-sm-4>img
         {:src thumbnail :style {:height "200px" :width "200px" :display "block"}}]
   [:div.col-md-9.col-sm-8
     [:h3 "Add Artist"]
     [:form
      [:div.form-group
       [:label "Name"]
       [:input.form-control {:field :text :id :name}]]
      [:div.form-group
       [:label "Bio"]
       [:textarea.form-control {:field :textarea :id :bio}]]
      ]]])

(defn add-artist-form []
  (let [artist (atom {:id 0 :name "" :bio "" :sortName ""})]
    (fn add-artist-form-renderer []
      [:div
       [bind-fields form-template artist]
       [:button.btn.btn-primary.pull-right
        {:on-click #(dispatch [:add-artist @artist])}
        "Add Artist"]])))