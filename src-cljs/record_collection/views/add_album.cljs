(ns record-collection.views.add-album
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent-forms.core :refer [bind-fields]]
            [reagent.core :refer [atom]]))

(defn artist-select-option
  [artist]
  [:option {:key (:id artist)} (:name artist)])

(defn form-template [artists]    
    [:div
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
                    (map artist-select-option artists)))]
      ]])

(defn add-album-form []
  (let [artists (subscribe [:current-artists])
        album (atom {:id 0, :title "", :year 2015, :artist 0})]
    (fn add-album-form-renderer
      []
      [:div
       [bind-fields (form-template @artists) album]
       [:button.btn.btn-primary
        {:on-click #(dispatch [:add-album @album])}
        "Add Album"]])))